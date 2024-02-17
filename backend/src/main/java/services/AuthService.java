package services;

import io.grpc.stub.StreamObserver;
import org.hse.moodactivities.common.proto.services.AuthServiceGrpc;
import org.hse.moodactivities.common.utils.proto.requests.auth.*;
import org.hse.moodactivities.common.utils.proto.responses.auth.*;

public class AuthService extends AuthServiceGrpc.AuthServiceImplBase {
    @Override
    public void register(RegistrRequest request, StreamObserver<RegistrResponse> responseObserve) {
        RegistrResponse response = RegistrResponse.newBuilder().setMessage("Hello - " + request.getNickname()).build();
        responseObserve.onNext(response);
        responseObserve.onCompleted();
    }
}
