# .env

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