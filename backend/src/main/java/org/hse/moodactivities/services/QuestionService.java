package org.hse.moodactivities.services;

import static java.net.HttpURLConnection.HTTP_OK;

import org.hse.moodactivities.common.proto.requests.dailyQuestion.AnswerRequest;
import org.hse.moodactivities.common.proto.requests.dailyQuestion.QuestionRequest;
import org.hse.moodactivities.common.proto.responses.dailyQuestion.AnswerResponse;
import org.hse.moodactivities.common.proto.responses.dailyQuestion.QuestionResponse;
import org.hse.moodactivities.common.proto.services.QuestionServiceGrpc;
import org.hse.moodactivities.utils.StringGenerationService;

import io.grpc.stub.StreamObserver;

public class QuestionService extends QuestionServiceGrpc.QuestionServiceImplBase {

    @Override
    public void getDailyQuestion(QuestionRequest request, StreamObserver<QuestionResponse> responseObserver) {
        String question = StringGenerationService.getLastGeneratedString();
        QuestionResponse serviceResponse = QuestionResponse.newBuilder().setQuestion(question).setStatusCode(HTTP_OK).build();
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
