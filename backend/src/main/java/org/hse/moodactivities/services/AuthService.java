package org.hse.moodactivities.services;

import io.grpc.stub.StreamObserver;
import org.hse.moodactivities.common.proto.services.*;
import org.hse.moodactivities.common.proto.requests.auth.*;
import org.hse.moodactivities.common.proto.responses.auth.*;

public class AuthService extends AuthServiceGrpc.AuthServiceImplBase {
    @Override
    public void registration(RegistrationRequest request, StreamObserver<RegistrationResponse> responseObserve) {
        RegistrationResponse response = RegistrationResponse.newBuilder().setMessage("Hello - " + request.getNickname()).build();
        responseObserve.onNext(response);
        responseObserve.onCompleted();
    }
}
