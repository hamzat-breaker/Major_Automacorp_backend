FROM openjdk:17-alpine
LABEL authors="achille"

WORKDIR /app

COPY build/libs/automacorp-0.0.1-SNAPSHOT.jar /app/app.jar

ENV PORT=3014
EXPOSE ${PORT}

ENTRYPOINT java -jar /app/app.jar --server.port=$PORT