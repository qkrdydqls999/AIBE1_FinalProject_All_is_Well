# 📖 북적북적 - 중고 도서 거래 플랫폼

북적북은 Spring Boot 기반의 중고 도서 거래 서비스입니다. AI를 이용해 구매자와 판매자 모두에게 신뢰 있는 거래를 제공합니다.


## 🟥 주요 기능

**📚 도서 관리 & AI 가격 가이드 (판매자 중심)**
- **중고 도서 등록**
    - **ISBN 기반 정보 자동 완성**
    - **상세 상태 입력**
    - **도서 사진 업로드**.
- **판매 도서 목록 관리**

**💻 스마트 도서 탐색 (구매자 중심)**
- **검색 및 필터링**
- **도서 상세 정보**
- **판매자 프로필 조회**
- **찜하기**

**🤖 AI 기반 핵심 기능**
- **중고 서적 상태 판독**
- **중고 서적 가격 예측 및 추천**
   
**🧑‍🤝‍🧑 신뢰 기반 거래 & 소통**
- **구매 요청 및 거래 상태 관리**
- **DM**

## 🟧 기술 스택
| 분야                  | 항목                                                                                                                                                                                                                                                                                                                                   |
| --------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **프로그래밍 언어**   | ![Java](https://img.shields.io/badge/Java-000000.svg?&style=for-the-badge)                                                                                                                                                                                                                                                             |
| **백엔드 프레임워크** | ![Spring Boot](https://img.shields.io/badge/springboot-6DB33F.svg?&style=for-the-badge&logo=springboot&logoColor=white) ![Spring Security](https://img.shields.io/badge/springsecurity-6DB33F.svg?&style=for-the-badge&logo=springsecurity&logoColor=white) ![Jpa](https://img.shields.io/badge/Jpa-000000.svg?&style=for-the-badge) ![Redis](https://img.shields.io/badge/Redis-red?style=for-the-badge)  |
| **데이터베이스**      | ![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?&style=for-the-badge&logo=mysql&logoColor=white)                                                                                                                                                                                                                                |
| **인프라 & 컨테이너** | ![Docker](https://img.shields.io/badge/docker-2496ED.svg?&style=for-the-badge&logo=docker&logoColor=white) ![AWS EC2](https://img.shields.io/badge/Awsec2-000000.svg?&style=for-the-badge)                                     |
| **API 문서화**        | ![Swagger](https://img.shields.io/badge/swagger-85EA2D.svg?&style=for-the-badge&logo=swagger&logoColor=white)                                                                                                                                                                                                                          |
| **프론트엔드**        | ![Thymeleaf](https://img.shields.io/badge/Thymeleaf-purple?style=for-the-badge) |
| **배포**              |                                                                                                                                                                                                                              
## 🟨 프로젝트 구조

```
src/main/java/org/example/bookmarket
├── ai        # Gemini API 연동 서비스
├── auth      # 인증 및 보안 설정
├── book      # 도서 관리 기능
├── category  # 카테고리 관리
├── chat      # 채팅 기능
├── common    # 공통 설정 및 유틸리티
├── profile   # 프로필 관리
├── trade     # 거래 관리 기능
├── usedbook  # 중고 도서 도메인
├── user      # 사용자 관련 도메인
└── wishlist  # 찜 목록 기능
```

## 🟩 로컬 실행 방법

1. 저장소 클론 후 JDK 17 환경에서 다음 명령으로 빌드합니다.
   ```bash
   ./gradlew build
   ```
2. 프로젝트 루트에 `.env` 파일을 만들고 필요한 환경 변수를 설정합니다.
   ```env
   SPRING_PROFILES_ACTIVE=dev
   SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/bookmarket
   SPRING_DATASOURCE_USERNAME=root
   SPRING_DATASOURCE_PASSWORD=<YOUR_DB_PASSWORD>
   SPRING_REDIS_HOST=localhost
   AWS_ACCESS_KEY_ID=<YOUR_AWS_ACCESS_KEY_ID>
   AWS_SECRET_ACCESS_KEY=<YOUR_AWS_SECRET_ACCESS_KEY>
   NAVER_API_CLIENT_ID=<YOUR_NAVER_API_CLIENT_ID>
   NAVER_API_CLIENT_SECRET=<YOUR_NAVER_API_CLIENT_SECRET>
   KAKAO_CLIENT_ID=<YOUR_KAKAO_CLIENT_ID>
   KAKAO_CLIENT_SECRET=<YOUR_KAKAO_CLIENT_SECRET>
   GEMINI_API_KEY=<YOUR_GEMINI_API_KEY>
   ```
3. Docker Compose로 필요한 서비스를 실행합니다.
   ```bash
   docker-compose up --build
   ```
4. 브라우저에서 [http://localhost:8080] 접속.

## 🟦 개발 로드맵
### 🎯 1단계 : MVP
- 소셜 로그인 (카카오)
- ISBN 기반 중고 도서 등록 (판매자)
- 도서 검색 및 조회 (구매자)
- AI 기반 추천
- DM (WebSocket 기반 다이렉트 메시지)
### 📌 2단계 : 고도화
- 고급 데이터 분석
- 실시간 챗봇
- 맞춤 도서 추천 (AI 기반 성향 분석)
### 📍3단계 : 확장
- 모바일 앱
- 외부 결제 API 연동
- 도서 외 각종 거래래

## 🟦 비즈니스 모델
### 💰수익 모델
- 프리미엄 구독 (고도화된 AI 분석, 자동화 도구)
- 광고 및 제휴
### 💸수익화 전략
- 0~6개월 : 무료 서비스로 사용자 확보
- 6개월~1년 : 프리미엄 사용자 확보
- 1년~ : 모바일 앱 개발을 바탕으로 광고 및 제휴 수익 확보

## 🟪 팀원 정보
|팀장|팀원|팀원|
|:---:|:---:|:---:|
|박재우|박용빈|김호정|
|[@cnncnncom](https://github.com/cnncnncom)|[@qkrdydqls999](https://github.com/qkrdydqls999)|[@hj-kim12](https://github.com/hj-kim12)|
|백엔드|백엔드|백엔드|
