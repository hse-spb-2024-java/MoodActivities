FROM gradle:8.6.0-jdk21-alpine AS build-stage
COPY --chown=gradle:gradle . /home/gradle/backend/
WORKDIR /home/gradle/backend
RUN gradle build || return 1

FROM eclipse-temurin:21-alpine as run-stage
ENV ARTIFACT_NAME=backend-0.1-DEV.jar
ENV APP_HOME=/moodactivities
RUN mkdir $APP_HOME
COPY --from=build-stage /home/gradle/backend/build/libs/$ARTIFACT_NAME $APP_HOME/

WORKDIR $APP_HOME

EXPOSE 8080
ENTRYPOINT exec java -jar ${ARTIFACT_NAME}