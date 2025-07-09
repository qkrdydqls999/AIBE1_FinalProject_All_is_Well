# All is Well - 중고 도서 거래 플랫폼

All is Well은 Spring Boot 기반의 중고 도서 거래 서비스입니다. 책 등록과 거래, 채팅까지 한곳에서 해결할 수 있으며 Google Gemini API를 이용해 책 상태 분석 및 검색 키워드 추출 기능을 제공합니다.

**In a nutshell**

* Spring Boot + Gradle 기반 프로젝트
* Gemini API, Redis, 카카오 OAuth 등 다양한 연동 기능 제공
* Docker Compose 예제와 `.env` 설정으로 손쉬운 로컬 실행 가능

## 주요 기능

- **도서 등록/조회/거래**: 중고 도서를 등록하고 원하는 책을 검색하여 거래할 수 있습니다.
- **AI 이미지 분석**: 책 사진을 업로드하면 Gemini API를 통해 상태를 분석하고 적정 가격을 제안합니다.
- **채팅 기능**: 실시간 채팅을 통해 사용자 간 거래를 원활히 진행할 수 있습니다.
- **카카오 OAuth 로그인**: 카카오 계정을 이용한 간편 로그인 기능을 제공합니다.
- **Redis 캐시**: 성능 향상을 위해 Redis를 활용합니다.
- **Swagger**: API 문서를 Swagger UI로 제공합니다.

## 프로젝트 구조

```
src/main/java/org/example/bookmarket
├── ai            # Gemini API 연동 서비스
├── auth          # 인증 및 보안 설정
├── book          # 도서 관리 기능
├── chat          # 채팅 기능
├── trade         # 거래 관리 기능
├── user          # 사용자 관련 도메인
└── ...
```

## 로컬 실행 방법

1. 저장소 클론 후 JDK 17 환경에서 다음 명령으로 빌드합니다.
   ```bash
   ./gradlew build
   ```
2. 프로젝트 루트에 `.env` 파일을 만들고 필요한 환경 변수를 설정합니다. 기본 값은 다음과 같습니다.
   ```env
   SPRING_PROFILES_ACTIVE=dev
   DB_URL=jdbc:mysql://localhost:3306/bookmarket
   DB_USERNAME=root
   DB_PASSWORD=비밀번호
   SPRING_DATA_REDIS_HOST=redis-cache
   SPRING_DATA_REDIS_PORT=6379
   AWS_REGION=ap-northeast-2
   AWS_ACCESS_KEY_ID=...
   AWS_SECRET_ACCESS_KEY=...
   KAKAO_CLIENT_ID=...
   KAKAO_CLIENT_SECRET=...
   GEMINI_MODEL1_KEY=...
   ```
3. Docker Compose로 필요한 서비스를 실행합니다.
   ```bash
   docker-compose up --build
   ```
   기본 포트는 `8080` 입니다.
4. 실행 후 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) 에서 API 문서를 확인할 수 있습니다.

### 환경 변수 (.env) 상세 예시

아래 예시는 원본 README에서 제공하던 환경 변수 형식을 유지한 것입니다.

```env
# 서버 실행 프로파일 (dev, prod 등)
SPRING_PROFILES_ACTIVE=dev

# ===============================================================
# 데이터베이스 정보 (아래 2개 옵션 중 하나를 선택하여 사용)
# ===============================================================

# 옵션 1: 로컬 개발 환경용 (Docker 또는 로컬에 설치된 MySQL 사용 시)
# DB_URL=jdbc:mysql://localhost:3306/bookmarket
# DB_USERNAME=root
# DB_PASSWORD=your_local_db_password

# 옵션 2: AWS RDS 등 운영/통합 개발 환경용
DB_URL=jdbc:mysql://aibe-team06.coqwxjz7zumt.ap-northeast-2.rds.amazonaws.com:3306/bookmarket
DB_USERNAME=admin
DB_PASSWORD=your_rds_password # 여기에 실제 비밀번호를 입력하세요

# ===============================================================
# Redis 서버 정보 (Docker Compose 사용 시 기본값)
# ===============================================================
SPRING_DATA_REDIS_HOST=redis-cache
SPRING_DATA_REDIS_PORT=6379

# ===============================================================
# 클라우드 및 외부 서비스 API 키
# ===============================================================

# AWS S3 설정
AWS_REGION=ap-northeast-2
AWS_ACCESS_KEY_ID=your_aws_access_key_id
AWS_SECRET_ACCESS_KEY=your_aws_secret_access_key

# OAuth (Kakao) 설정
KAKAO_CLIENT_ID=your_kakao_client_id
KAKAO_CLIENT_SECRET=your_kakao_client_secret

# Google Gemini API 설정
GEMINI_MODEL1_KEY=your_gemini_key_1
```

## 테스트

Gradle을 통해 테스트를 실행할 수 있습니다.
```bash
./gradlew test
```

## 라이선스

본 프로젝트는 MIT 라이선스를 따릅니다.