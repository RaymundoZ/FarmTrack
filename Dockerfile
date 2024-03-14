FROM alpine:3.19

RUN apk add openjdk17

WORKDIR $HOME/app
COPY ./target/FarmTrack-0.0.1.jar FarmTrack-0.0.1.jar
EXPOSE 8080
ENTRYPOINT java -jar FarmTrack-0.0.1.jar