package org.hse.moodactivities.services;

import org.hse.moodactivities.common.proto.requests.dailyQuestion.AnswerRequest;
import org.hse.moodactivities.common.proto.requests.dailyQuestion.QuestionRequest;
import org.hse.moodactivities.common.proto.responses.dailyQuestion.AnswerResponse;
import org.hse.moodactivities.common.proto.responses.dailyQuestion.QuestionResponse;
import org.hse.moodactivities.common.proto.services.QuestionServiceGrpc;
import org.hse.moodactivities.utils.GptClientRequest;
import org.hse.moodactivities.utils.GptMessages;
import org.hse.moodactivities.utils.GptResponse;

import java.net.HttpURLConnection;

import io.grpc.stub.StreamObserver;

public class QuestionService extends QuestionServiceGrpc.QuestionServiceImplBase {

    @Override
    public void getDailyQuestion(QuestionRequest request, StreamObserver<QuestionResponse> responseObserver) {
        // StringBuilder requestString = new StringBuilder(PromptsStorage.getString("dailyQuestion.request"));
        StringBuilder requestString = new StringBuilder("Ask a person an interesting question about their day using no more than 5 words");
        if (request.getMeta() != null) {
            requestString.append(" With meta: ");
            requestString.append(request.getMeta());
        }
        GptResponse response = GptClientRequest.sendRequest(new GptMessages(GptMessages.GptMessage.Role.user, requestString.toString()));
        QuestionResponse serviceResponse;
        if (response.statusCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
            serviceResponse = QuestionResponse.newBuilder().setQuestion(response.message().getContent()).setStatusCode(response.statusCode()).build();
        } else {
            serviceResponse = QuestionResponse.newBuilder().setStatusCode(response.statusCode()).build();
        }
        responseObserver.onNext(serviceResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void sendDailyQuestionAnswer(AnswerRequest request, StreamObserver<AnswerResponse> responseObserver) {
        // TODO: load to mongo
        AnswerResponse response = AnswerResponse.newBuilder().setStatusCode(200).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
