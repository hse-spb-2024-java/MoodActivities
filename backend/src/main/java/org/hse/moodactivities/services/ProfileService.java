package org.hse.moodactivities.services;

import org.hse.moodactivities.common.proto.requests.profile.ChangeInfoRequest;
import org.hse.moodactivities.common.proto.requests.profile.GetInfoRequest;
import org.hse.moodactivities.common.proto.responses.profile.ChangeInfoResponse;
import org.hse.moodactivities.common.proto.responses.profile.GetInfoResponse;
import org.hse.moodactivities.common.proto.services.ProfileServiceGrpc;

import io.grpc.stub.StreamObserver;

public class ProfileService extends ProfileServiceGrpc.ProfileServiceImplBase {
    @Override
    public void getInfo(GetInfoRequest request, StreamObserver<GetInfoResponse> responseObserver) {
        super.getInfo(request, responseObserver);
    }

    @Override
    public void changeInfo(ChangeInfoRequest request, StreamObserver<ChangeInfoResponse> responseObserver) {
        super.changeInfo(request, responseObserver);
    }
}
