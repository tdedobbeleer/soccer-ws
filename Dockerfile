FROM amazoncorretto:17-alpine
VOLUME /tmp
# Run the image as a non-root user
RUN adduser -D javauser
USER javauser

COPY target/ws-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-Dserver.port=$PORT","-Xmx268M","-Xss512K","-XX:CICompilerCount=2","-Dfile.encoding=UTF-8","-XX:+UseContainerSupport","-Djava.security.egd=file:/dev/./urandom","-Xlog:gc" ,"-jar","/app.jar", "--spring.profiles.active=heroku"]