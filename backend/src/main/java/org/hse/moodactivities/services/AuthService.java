package org.hse.moodactivities.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import org.hibernate.exception.ConstraintViolationException;
import org.hse.moodactivities.common.proto.requests.auth.LoginRequest;
import org.hse.moodactivities.common.proto.requests.auth.OauthLoginRequest;
import org.hse.moodactivities.common.proto.requests.auth.RegistrationRequest;
import org.hse.moodactivities.common.proto.responses.auth.LoginResponse;
import org.hse.moodactivities.common.proto.responses.auth.OauthLoginResponse;
import org.hse.moodactivities.common.proto.responses.auth.RegistrationResponse;
import org.hse.moodactivities.common.proto.services.AuthServiceGrpc;
import org.hse.moodactivities.data.entities.postgres.AuthProvider;
import org.hse.moodactivities.data.entities.postgres.UserProfile;
import org.hse.moodactivities.utils.GoogleUtils;
import org.hse.moodactivities.utils.JWTUtils.JWTUtils;
import org.hse.moodactivities.utils.UserProfileRepository;

import java.util.Collections;
import java.util.Optional;

import io.grpc.stub.StreamObserver;

public class AuthService extends AuthServiceGrpc.AuthServiceImplBase {
    @Override
    public void registration(RegistrationRequest request, StreamObserver<RegistrationResponse> responseObserve) {
        RegistrationResponse.Builder response = RegistrationResponse.newBuilder();

        UserProfile newProfile = null;
        try {
            if (request.getPassword().isEmpty()) {
                response.setResponseType(RegistrationResponse.ResponseType.ERROR)
                        .setMessage("Password cannot be empty");
            } else {
                newProfile = UserProfileRepository.createPlainUserProfile(
                        request.getUsername(),
                        request.getEmail(),
                        request.getPassword()
                );
            }
        } catch (Exception e) {
            Throwable cause = e.getCause();

            while (cause != null && !(cause instanceof ConstraintViolationException)) {
                cause = cause.getCause();
            }

            if (cause == null) {
                response.setResponseType(RegistrationResponse.ResponseType.ERROR)
                        .setMessage("Unknown error");
            } else {
                // Constraint violation
                ConstraintViolationException cve = (ConstraintViolationException) cause;
                String constraintName = cve.getConstraintName();

                if (constraintName != null && (constraintName.contains("login"))) {
                    response.setResponseType(RegistrationResponse.ResponseType.ERROR)
                            .setMessage("User with such login already exists");
                } else {
                    // Total and utter CATASTROPHE!
                    System.err.println("Constraint check failed in non-constrained field!");
                    response.setResponseType(RegistrationResponse.ResponseType.ERROR)
                            .setMessage("Unknown error");
                }
            }
        }

        if (newProfile != null) {
            response.setResponseType(RegistrationResponse.ResponseType.SUCCESS)
                    .setMessage("")
                    .setToken(generateJwsForUserProfile(newProfile));
        }

        responseObserve.onNext(response.build());
        responseObserve.onCompleted();
    }

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserve) {
        Optional<UserProfile> userProfileOpt = Optional.empty();
        if (request.getType() == LoginRequest.loginType.LOGIN) {
            userProfileOpt = UserProfileRepository.findByLogin(AuthProvider.PLAIN, request.getUserInfo());
        } else if (request.getType() == LoginRequest.loginType.MAIL) {
            userProfileOpt = UserProfileRepository.findByEmail(AuthProvider.PLAIN, request.getUserInfo());
        }
        if (userProfileOpt.isEmpty()) {
            LoginResponse response = LoginResponse.newBuilder()
                    .setType(LoginResponse.responseType.ERROR)
                    .setMessage("No user with such login").build();
            responseObserve.onNext(response);
            responseObserve.onCompleted();
            return;
        }
        UserProfile userProfile = userProfileOpt.get();
        if (!userProfile.validatePassword(request.getPassword())) {
            LoginResponse response = LoginResponse.newBuilder()
                    .setType(LoginResponse.responseType.ERROR)
                    .setMessage("Incorrect login or password").build();
            responseObserve.onNext(response);
            responseObserve.onCompleted();
            return;
        }
        // Just generate JWT
        LoginResponse response = LoginResponse.newBuilder()
                .setType(LoginResponse.responseType.SUCCESS)
                .setToken(generateJwsForUserProfile(userProfile)).build();
        responseObserve.onNext(response);
        responseObserve.onCompleted();
    }

    private String generateJwsForUserProfile(UserProfile userProfile) {
        return JWTUtils.getBuilder()
                .subject(String.valueOf(userProfile.getId()))
                .compact();
    }

    @Override
    public void oAuthLogin(OauthLoginRequest request, StreamObserver<OauthLoginResponse> responseObserver) {
        String oauthToken = request.getOauthToken();
        GoogleIdToken.Payload payload = parseAndVerifyGoogleToken(oauthToken);

        OauthLoginResponse.Builder responseBuilder = OauthLoginResponse.newBuilder();
        if (payload != null) {
            String email = payload.getEmail();
            String oauthId = payload.getSubject();

            Optional<UserProfile> optUserProfile = UserProfileRepository.findByOauthId(
                    AuthProvider.GOOGLE, oauthId
            );
            UserProfile profile = optUserProfile.orElseGet(
                    // TODO: proper error handling
                    () -> UserProfileRepository.createGoogleUserProfile(email, oauthId)
            );
            responseBuilder.setType(OauthLoginResponse.responseType.SUCCESS)
                    .setToken(generateJwsForUserProfile(profile));
        } else {
            responseBuilder.setType(OauthLoginResponse.responseType.ERROR)
                    .setMessage("Failed to validate oauth token");
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    public GoogleIdToken.Payload parseAndVerifyGoogleToken(String idToken) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(GoogleUtils.APP_ID))
                .build();

        try {
            GoogleIdToken token = verifier.verify(idToken);
            if (token != null) {
                GoogleIdToken.Payload payload = token.getPayload();
                return payload;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
