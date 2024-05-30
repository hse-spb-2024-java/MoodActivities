package org.hse.moodactivities.services;

import org.hse.moodactivities.common.proto.requests.push.SetupPushRequest;
import org.hse.moodactivities.common.proto.responses.push.SetupPushResponse;
import org.hse.moodactivities.common.proto.services.PushServiceGrpc;

import io.grpc.stub.StreamObserver;

public class PushService extends PushServiceGrpc.PushServiceImplBase {
    @Override
    public void setupUsersPush(SetupPushRequest request, StreamObserver<SetupPushResponse> responseObserver) {
        super.setupUsersPush(request, responseObserver);
    }
}
