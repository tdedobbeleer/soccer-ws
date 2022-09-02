FROM amazoncorretto:17-alpine
COPY ws-0.0.1-SNAPSHOT.jar /opt/app.jar
# Run the image as a non-root user
RUN adduser -D javauser
USER javauser

ENTRYPOINT ["java", "-Dserver.port=$PORT","-Xmx268M","-Xss512K","-XX:CICompilerCount=2","-Dfile.encoding=UTF-8","-XX:+UseContainerSupport","-Djava.security.egd=file:/dev/./urandom","-Xlog:gc" ,"-jar","/opt/app.jar", "--spring.profiles.active=default"]