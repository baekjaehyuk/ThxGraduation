# 1. JDK 17 이미지 사용
FROM openjdk:21-jdk-alpine

# 2. JAR 파일 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 3. 포트 열기
EXPOSE 8080

# 4. 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]