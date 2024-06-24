FROM eclipse-temurin:21-alpine
COPY . /home/hptu
WORKDIR /home/hptu
RUN ./gradlew clean -x test bootJar
RUN mv /home/hptu/build/libs/*.jar /home/hptu-1.0.0.jar && rm -rf /home/hptu
ENTRYPOINT ["java"]
CMD ["-jar", "/home/hptu-1.0.0.jar"]
EXPOSE 5441