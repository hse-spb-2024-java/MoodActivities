package org.hse.moodactivities.services;

import io.grpc.stub.StreamObserver;
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
        RegistrationResponse response = RegistrationResponse.newBuilder().setMessage("Hello - " + request.getNickname()).build();
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
            LoginResponse response = LoginResponse.newBuilder().setMessage("No user with such login").build();
            responseObserve.onNext(response);
            responseObserve.onCompleted();
            return;
        }
        UserProfile userProfile = userProfileOpt.get();
        if (!userProfile.validatePassword(request.getPassword())) {
            LoginResponse response = LoginResponse.newBuilder().setMessage("Incorrect login or password").build();
            responseObserve.onNext(response);
            responseObserve.onCompleted();
            return;
        }
        // Just generate JWT
        String jws = JWTUtils.getBuilder().subject(String.valueOf(userProfile.getId())).compact();
        LoginResponse response = LoginResponse.newBuilder().setToken(jws).build();
        responseObserve.onNext(response);
        responseObserve.onCompleted();
    }
}
