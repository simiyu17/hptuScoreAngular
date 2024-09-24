FROM eclipse-temurin:21-alpine
COPY . /home/hptu
WORKDIR /home/hptu
# Copy the Gradle wrapper and build.gradle files
# COPY gradlew ./
RUN ls
COPY gradle ./gradle
# Make sure the gradlew script is executable
RUN chmod +x ./gradlew
RUN ./gradlew clean -x test bootJar
RUN mv /home/hptu/build/libs/*.jar /home/hptu-1.0.0.jar && rm -rf /home/hptu
ENTRYPOINT ["java"]
CMD ["-jar", "/home/hptu-1.0.0.jar"]
EXPOSE 8082