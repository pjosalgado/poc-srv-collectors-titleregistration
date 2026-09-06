FROM eclipse-temurin:21-jre-alpine AS runtime

ARG VERSION=1.0.0

LABEL org.opencontainers.image.title="poc-srv-collectors-titleregistration"
LABEL org.opencontainers.image.version="${VERSION}"
LABEL org.opencontainers.image.authors="Paulo Salgado <pjosalgado@email.com>"

RUN addgroup -S app && adduser -S app -G app

WORKDIR /app

COPY target/*.jar app.jar

USER app

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
