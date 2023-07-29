FROM openjdk:11-jdk-slim
ARG JAR_FILE=build/libs/*.jar

# 애플리케이션 빌드 및 JAR 파일 복사
WORKDIR /app
COPY $JAR_FILE /app/wanted-pre-onboarding-backend.jar

# 애플리케이션 실행
CMD ["java","-Dspring.profiles.active=prod" , "-jar", "/app/wanted-pre-onboarding-backend.jar"]