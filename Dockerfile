FROM openjdk:17-jdk-slim

# 시스템 업데이트 및 Tesseract OCR 설치
RUN apt-get update && \
    apt-get install -y \
    openjdk-17-jdk \
    tesseract-ocr \
    tesseract-ocr-eng \
    libtesseract-dev \
    curl \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# 빌드 파일들 복사
COPY gradle/ gradle/
COPY gradlew build.gradle settings.gradle ./
COPY src/ src/

# 실행 권한 및 빌드
RUN chmod +x gradlew
RUN ./gradlew build -x test

# JAR 파일 복사
RUN cp build/libs/*.jar app.jar

# Tesseract 데이터를 위한 심볼릭 링크 생성
RUN mkdir -p /app/src/main/resources && \
    ln -s /usr/share/tesseract-ocr/4.00/tessdata /app/src/main/resources/tessdata

# 환경변수 설정
ENV TESSDATA_PREFIX=/usr/share/tesseract-ocr/5/tessdata/
ENV LD_LIBRARY_PATH=/usr/lib/x86_64-linux-gnu:/usr/lib

# 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]