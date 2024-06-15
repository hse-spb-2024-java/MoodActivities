package org.hse.moodactivities.services;

import org.hse.moodactivities.common.proto.requests.profile.ChangeInfoRequest;
import org.hse.moodactivities.common.proto.requests.profile.GetInfoRequest;
import org.hse.moodactivities.common.proto.responses.profile.ChangeInfoResponse;
import org.hse.moodactivities.common.proto.responses.profile.GetInfoResponse;
import org.hse.moodactivities.common.proto.services.ProfileServiceGrpc;
import org.hse.moodactivities.data.entities.postgres.UserProfile;
import org.hse.moodactivities.utils.JWTUtils.JWTUtils;
import org.hse.moodactivities.utils.UserProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import io.grpc.stub.StreamObserver;

public class ProfileService extends ProfileServiceGrpc.ProfileServiceImplBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileService.class);

    @Override
    public void getInfo(GetInfoRequest request, StreamObserver<GetInfoResponse> responseObserver) {
        String userId = JWTUtils.CLIENT_ID_CONTEXT_KEY.get();
        Optional<UserProfile> userProfile = UserProfileRepository.findById(userId);
        if (userProfile.isEmpty()) {
            responseObserver.onNext(GetInfoResponse.getDefaultInstance());
            responseObserver.onCompleted();
        }
        UserProfile unwrappedUserProfile = userProfile.get();
        responseObserver.onNext(dumpInfo(unwrappedUserProfile));
        responseObserver.onCompleted();
    }

    @Override
    public void changeInfo(ChangeInfoRequest request, StreamObserver<ChangeInfoResponse> responseObserver) {
        String userId = JWTUtils.CLIENT_ID_CONTEXT_KEY.get();
        Optional<UserProfile> userProfile = UserProfileRepository.findById(userId);
        if (userProfile.isEmpty()) {
            LOGGER.error(String.format("%s user not in db"), userId);
            responseObserver.onNext(ChangeInfoResponse
                    .newBuilder()
                    .setCompleted(false)
                    .setErrorMessage("you aren't registered")
                    .build());
            responseObserver.onCompleted();
        }
        UserProfile unwrappedUserProfile = userProfile.get();
        if (request.getName() != null) {
            unwrappedUserProfile.setName(request.getName());
        }
        if (request.getDateOfBirth() != null) {
            unwrappedUserProfile.setDayOfBirth(request.getDateOfBirth());
        }
        if (request.getPassword() != null) {
            responseObserver.onNext(ChangeInfoResponse
                    .newBuilder()
                    .setCompleted(false)
                    .setErrorMessage("can't change password")
                    .setNewInfo(dumpInfo(unwrappedUserProfile))
                    .build());
            responseObserver.onCompleted();
        }
        ChangeInfoResponse response;
        if (UserProfileRepository.saveEntity(unwrappedUserProfile)) {
            response = ChangeInfoResponse
                    .newBuilder()
                    .setCompleted(true)
                    .setNewInfo(dumpInfo(unwrappedUserProfile))
                    .build();
        } else {
            response = ChangeInfoResponse
                    .newBuilder()
                    .setCompleted(false)
                    .setErrorMessage("failed to save changes")
                    .setNewInfo(dumpInfo(unwrappedUserProfile))
                    .build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private GetInfoResponse dumpInfo(UserProfile userProfile) {
        GetInfoResponse response = GetInfoResponse.newBuilder()
                .setName(userProfile.getName())
                .setLogin(userProfile.getLogin())
                .setEmail(userProfile.getEmail())
                .setDateOfBirth(userProfile.getDayOfBirth())
                .setGoogleEnabled(userProfile.hasGoogle())
                .build();
        return response;
    }
}
