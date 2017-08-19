FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/p2pdinner-api-0.0.1-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=" "
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar -Dspring.profiles.active=docker /app.jar" ]
