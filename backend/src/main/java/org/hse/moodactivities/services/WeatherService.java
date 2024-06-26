package org.hse.moodactivities.services;

import org.hse.moodactivities.common.proto.requests.weather.GeoForWeatherRequest;
import org.hse.moodactivities.common.proto.responses.weather.GeoForWeatherResponse;
import org.hse.moodactivities.common.proto.services.WeatherServiceGrpc;
import org.hse.moodactivities.data.entities.mongodb.User;
import org.hse.moodactivities.data.entities.mongodb.UserDayMeta;
import org.hse.moodactivities.utils.JWTUtils.JWTUtils;
import org.hse.moodactivities.utils.MongoDBSingleton;
import org.hse.moodactivities.utils.WeatherApp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import io.grpc.stub.StreamObserver;

public class WeatherService extends WeatherServiceGrpc.WeatherServiceImplBase {
    @Override
    public void sendGeo(GeoForWeatherRequest request, StreamObserver<GeoForWeatherResponse> responseObserver) {
        String id = JWTUtils.CLIENT_ID_CONTEXT_KEY.get();
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("_id", id);
        var users = MongoDBSingleton.getInstance().getConnection().findEntityWithFilters(User.class, queryMap);
        User user = null;
        if (users != null && users.size() > 0) {
            user = users.get(0);
        } else {
            user = new User(id, new ArrayList<>());
        }
        handler(user, request.getLat(), request.getLon(), 0);
        responseObserver.onCompleted();
    }

    public static void handler(User user, double lat, double lon, int mood) {
        Optional<UserDayMeta.Weather> weather = WeatherApp.getWeather(lat, lon, mood);
        if (weather.isPresent()) {
            if (user.getMetas() != null && !user.getMetas().isEmpty() && user.getMetas().getLast().getDate().equals(LocalDate.now())) {
                user.getMetas().getLast().addWeather(weather.get());
            } else {
                UserDayMeta newMeta = new UserDayMeta();
                newMeta.addWeather(weather.get());
                if (user.getMetas() == null) {
                    user.setMetas(new ArrayList<>());
                }
                user.getMetas().add(newMeta);
            }
            MongoDBSingleton.getInstance().getConnection().updateEntity(user);
        }
    }
}
