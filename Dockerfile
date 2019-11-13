FROM java:8
ENV HTTP_PROXY "http://172.28.142.230:3128" HTTPS_PROXY "http://172.28.142.230:3128" FTP_PROXY "http://172.28.142.230:3128"
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/.urandom", "-jar", "app.jar"]