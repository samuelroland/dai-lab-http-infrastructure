FROM alpine:latest
RUN apk add --no-cache openjdk21
WORKDIR /froom
COPY target/server-*.jar /froom/server.jar
CMD ["java", "-jar", "server.jar"]
EXPOSE 7000