FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="TheRavedApp Team"

RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

WORKDIR /app

RUN mkdir -p logs && chown -R appuser:appgroup /app
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-XX:+UseContainerSupport", "-jar", "/app/app.jar"]