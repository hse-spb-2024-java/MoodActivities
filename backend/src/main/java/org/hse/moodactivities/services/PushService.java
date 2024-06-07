package org.hse.moodactivities.services;

import org.hse.moodactivities.common.proto.requests.push.SetupPushRequest;
import org.hse.moodactivities.common.proto.responses.push.SetupPushResponse;
import org.hse.moodactivities.common.proto.services.PushServiceGrpc;
import org.hse.moodactivities.data.utils.HibernateUtils;
import org.hse.moodactivities.utils.JWTUtils.JWTUtils;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import io.grpc.stub.StreamObserver;
import jakarta.persistence.EntityManagerFactory;

public class PushService extends PushServiceGrpc.PushServiceImplBase {
    @Override
    public void setupUsersPush(SetupPushRequest request, StreamObserver<SetupPushResponse> responseObserver) {
        String userId = JWTUtils.CLIENT_ID_CONTEXT_KEY.get();
        LocalTime usersTime = LocalTime.of(20, 0);
        LocalTime serverTime = LocalTime.now();
        serverTime.plusMinutes(10);
        LocalTime roundedTime = serverTime.truncatedTo(ChronoUnit.HOURS);
        int diff = (int) ChronoUnit.HOURS.between(serverTime, usersTime);
        try {
            EntityManagerFactory factory = HibernateUtils.getEntityManagerFactory();
            // actory.
        }
    }
}
