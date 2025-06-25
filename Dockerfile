# =================================================================
# Stage 1: 애플리케이션 빌드 단계 (Builder)
# =================================================================
FROM gradle:8.5.0-jdk17 AS builder

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew .

RUN chmod +x ./gradlew
RUN ./gradlew dependencies --build-cache || return 0

COPY src ./src

RUN ./gradlew build -x test

# =================================================================
# Stage 2: 최종 실행 이미지 생성 단계 (Runner)
# =================================================================
FROM azul/zulu-openjdk:17-jre-latest

RUN groupadd appgroup && useradd -m -g appgroup appuser
USER appuser

WORKDIR /app

COPY .env .env

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
