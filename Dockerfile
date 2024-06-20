FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
COPY target/asset-management-0701-*.jar app.jar
ENTRYPOINT ["java", "-Djava.awt.headless=true", "-jar","/app.jar"]