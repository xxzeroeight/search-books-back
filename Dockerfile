FROM openjdk:17-jdk-slim

WORKDIR /app

# 시스템 업데이트 및 Tesseract OCR 설치
RUN apt-get update && \
    apt-get install -y \
    tesseract-ocr \
    tesseract-ocr-eng \
    libtesseract-dev \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# 빌드 파일들 복사
COPY gradle/ gradle/
COPY gradlew build.gradle settings.gradle ./
COPY src/ src/

# 실행 권한 및 빌드
RUN chmod +x gradlew
RUN ./gradlew build -x test

# JAR 파일 복사
RUN cp build/libs/*.jar app.jar

# 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]