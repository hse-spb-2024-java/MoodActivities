package org.hse.moodactivities.services;

import io.grpc.stub.StreamObserver;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;
import org.hse.moodactivities.common.proto.services.*;
import org.hse.moodactivities.common.proto.requests.auth.*;
import org.hse.moodactivities.common.proto.responses.auth.*;
import org.hse.moodactivities.data.entities.postgres.UserProfile;
import org.hse.moodactivities.utils.JWTUtils.JWTUtils;
import org.hse.moodactivities.utils.UserProfileRepository;

import java.util.Optional;

public class AuthService extends AuthServiceGrpc.AuthServiceImplBase {
    @Override
    public void registration(RegistrationRequest request, StreamObserver<RegistrationResponse> responseObserve) {
        RegistrationResponse response = null;
        UserProfile newProfile = null;
        try {
            newProfile = UserProfileRepository.createUserProfile(request.getUsername(), request.getPassword());
        } catch (Exception e) {
            Throwable cause = e.getCause();

            while (cause != null && !(cause instanceof ConstraintViolationException)) {
                cause = cause.getCause();
            }

            if (cause == null) {
                response = RegistrationResponse.newBuilder()
                        .setResponseType(RegistrationResponse.ResponseType.ERROR)
                        .setMessage("Unknown error")
                        .build();
            } else {
                // Constraint violation
                ConstraintViolationException cve = (ConstraintViolationException) cause;
                String constraintName = cve.getConstraintName();

                if (constraintName != null && (constraintName.contains("login"))) {
                    response = RegistrationResponse.newBuilder()
                            .setResponseType(RegistrationResponse.ResponseType.ERROR)
                            .setMessage("User with such login already exists")
                            .build();
                } else {
                    // Total and utter CATASTROPHE!
                    System.err.println("Constraint check failed in non-constrained field!");
                    response = RegistrationResponse.newBuilder()
                            .setResponseType(RegistrationResponse.ResponseType.ERROR)
                            .setMessage("Unknown error")
                            .build();
                }
            }
        }

        if (response == null) {
            assert newProfile != null;
            response = RegistrationResponse.newBuilder()
                    .setResponseType(RegistrationResponse.ResponseType.SUCCESS)
                    .setMessage("")
                    .setToken(generateJwsForUserProfile(newProfile))
                    .build();
        }


        responseObserve.onNext(response);
        responseObserve.onCompleted();
    }

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserve) {
        Optional<UserProfile> userProfileOpt = Optional.empty();
        if (request.getType() == LoginRequest.loginType.LOGIN) {
            userProfileOpt = UserProfileRepository.findByLogin(request.getUserInfo());
        } else if (request.getType() == LoginRequest.loginType.MAIL) {
            userProfileOpt = UserProfileRepository.findByEmail(request.getUserInfo());
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
}
