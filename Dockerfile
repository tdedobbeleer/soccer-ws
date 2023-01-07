FROM maven:3-amazoncorretto-17 as builder

COPY src /usr/src/app/src
COPY pom.xml /usr/src/app

RUN mvn -f /usr/src/app/pom.xml clean package -Dmaven.test.skip=true

FROM amazoncorretto:17-alpine

COPY --from=builder /usr/src/app/target/ws-0.0.1-SNAPSHOT.jar /usr/app/app.jar

RUN adduser -D javauser
USER javauser

ENTRYPOINT ["java", "-Xss512K","-XX:CICompilerCount=2","-Dfile.encoding=UTF-8","-XX:+UseContainerSupport","-Djava.security.egd=file:/dev/./urandom", "-jar","/usr/app/app.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
