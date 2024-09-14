# Step 1: OpenJDK 22의 공식 이미지를 베이스 이미지로 사용
FROM openjdk:22-jdk-slim

# Step 2: 컨테이너 내 작업 디렉토리 설정
WORKDIR /app

# Step 3: Gradle 래퍼 및 빌드 스크립트를 컨테이너에 복사
COPY backend/gradlew backend/gradlew.bat backend/settings.gradle backend/build.gradle /app/
COPY backend/gradle /app/gradle

# Step 4: 소스 코드를 컨테이너로 복사
COPY backend/src /app/src

# Step 5: Gradle 래퍼에 실행 권한 부여
RUN chmod +x gradlew

# Step 6: Gradle을 사용하여 프로젝트 빌드 (데몬 모드 비활성화)
RUN ./gradlew build --no-daemon

# Step 7: 애플리케이션 포트(예: 8080) 노출
EXPOSE 8080

# Step 8: Java 옵션을 위한 환경 변수 설정 (선택 사항)
ENV JAVA_OPTS="-Xms512m -Xmx1024m"

# Step 9: 스프링 부트 애플리케이션 실행을 위한 엔트리포인트 설정
ENTRYPOINT ["java", "-jar", "build/libs/backend-0.0.1-SNAPSHOT.jar"]