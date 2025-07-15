# =================================================================
# Stage 1: 애플리케이션 빌드 단계 (Builder)
# =================================================================
FROM gradle:8.5.0-jdk17 AS builder

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew .

RUN chmod +x ./gradlew

# Gradle Daemon 비활성화
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false"

# 의존성 캐싱 (daemon 비활성화, 실패해도 계속 진행)
RUN ./gradlew dependencies --no-daemon --build-cache || true

COPY src ./src

# 테스트 제외하고 빌드, Daemon 비활성화
RUN ./gradlew build -x test --no-daemon

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
