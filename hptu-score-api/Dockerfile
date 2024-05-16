FROM eclipse-temurin:17-alpine
WORKDIR /app
COPY build/quarkus-app /app
ENTRYPOINT ["java"]
CMD ["-jar", "/app/quarkus-run.jar"]
EXPOSE 5441