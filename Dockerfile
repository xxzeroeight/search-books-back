FROM openjdk:17-jdk-slim

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

# 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]