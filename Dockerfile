FROM gradle:jdk17 as BUILD
WORKDIR /build
COPY . .
RUN ./gradlew bootJar

FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=BUILD /build/chicken_spring/build/libs/*.jar app.jar
EXPOSE 8080
CMD ["java","-jar","app.jar"]
