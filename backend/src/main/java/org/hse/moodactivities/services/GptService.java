package org.hse.moodactivities.services;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

import org.hse.moodactivities.common.proto.requests.gpt.GptSessionRequest;
import org.hse.moodactivities.common.proto.requests.gpt.MetaToGptRequest;
import org.hse.moodactivities.common.proto.responses.gpt.GptSessionResponse;
import org.hse.moodactivities.common.proto.responses.gpt.MetaToGptResponse;
import org.hse.moodactivities.common.proto.services.GptServiceGrpc;
import org.hse.moodactivities.utils.GptClientRequest;
import org.hse.moodactivities.utils.GptClientStream;
import org.hse.moodactivities.utils.GptMessages;
import org.hse.moodactivities.utils.GptResponse;

import io.grpc.stub.StreamObserver;

public class GptService extends GptServiceGrpc.GptServiceImplBase {

    @Override
    public StreamObserver<GptSessionRequest> gptSession(StreamObserver<GptSessionResponse> responseObserver) {
        GptClientStream stream = new GptClientStream();
        return new StreamObserver<GptSessionRequest>() {
            @Override
            public void onNext(GptSessionRequest request) {
                GptMessages.GptMessage gptRequest = new GptMessages.GptMessage(GptMessages.GptMessage.Role.user, request.getMessage());
                GptResponse responseMessage = stream.sendRequest(gptRequest);
                GptSessionResponse response;
                if (responseMessage.statusCode() >= HTTP_BAD_REQUEST) {
                    response = GptSessionResponse.newBuilder().setResponseCode(responseMessage.statusCode()).build();
                } else {
                    response = GptSessionResponse.newBuilder()
                            .setMessage(responseMessage.message().getContent())
                            .setResponseCode(responseMessage.statusCode())
                            .build();
                }
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onCompleted();
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void askWithMeta(MetaToGptRequest request, StreamObserver<MetaToGptResponse> responseObserver) {
        GptMessages gptRequest = new GptMessages(GptMessages.GptMessage.Role.user, request.getMeta() + " " + request.getMessage());
        GptResponse responseMessage = GptClientRequest.sendRequest(gptRequest);
        MetaToGptResponse response;
        if (responseMessage.statusCode() >= HTTP_BAD_REQUEST) {
            response = MetaToGptResponse.newBuilder().setResponseCode(responseMessage.statusCode()).build();
        } else {
            response = MetaToGptResponse.newBuilder()
                    .setMessage(responseMessage.message().getContent())
                    .setResponseCode(responseMessage.statusCode())
                    .build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
