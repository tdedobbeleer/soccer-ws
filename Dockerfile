FROM alpine:latest

RUN apk update \
  && apk upgrade \
  && apk add ca-certificates wget \
  && update-ca-certificates \
  && wget -O /etc/apk/keys/amazoncorretto.rsa.pub  https://apk.corretto.aws/amazoncorretto.rsa.pub \
  && echo "https://apk.corretto.aws/" >> /etc/apk/repositories \
  && apk add --update amazon-coretto-17 \
  && rm -rf /var/cache/apk/*

ARG RELEASE

COPY ./target/ws-${RELEASE}.jar /usr/app/soccer-ws.jar

RUN adduser -D javauser
USER javauser

ENTRYPOINT ["java", "-Xss512K","-XX:CICompilerCount=2","-Dfile.encoding=UTF-8","-XX:+UseContainerSupport","-Djava.security.egd=file:/dev/./urandom", "-jar","/usr/app/soccer-ws.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE:-local}"]
