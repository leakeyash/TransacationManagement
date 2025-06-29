FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY target/transaction-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]