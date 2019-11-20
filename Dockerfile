FROM java:8
VOLUME /tmp
#ENV HTTP_PROXY "http://172.28.142.230:3128" HTTPS_PROXY "http://172.28.142.230:3128" FTP_PROXY "http://172.28.142.230:3128"
RUN mkdir -p /app/ && mkdir -p /app/logs/
#COPY src/main/resources/application.properties /app/application.properties
ADD target/quiz-bot-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=container", "-jar", "/app/app.jar", "-Dspring.config.location=classpath:/config/application.properties"]