FROM bellsoft/liberica-runtime-container:jdk-all-21-cds-glibc

LABEL maintainer="JiaXue.Qin <qjx378@qq.com>"

ENV JAVA_OPTS="-Xms128m -Xmx512m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m"

ENV ENV_FILE=prod

ENV SERVER_PORT=8080

RUN apk update \
    && apk add --no-cache libgomp \
    && rm -rf /var/cache/apk/* \
    && mkdir -p /opt/app/config \
    && mkdir -p /opt/app/logs \
    && mkdir -p /opt/app/data

COPY target/face-service.jar /opt/app/app.jar

WORKDIR /opt/app
VOLUME /opt/app/config
VOLUME /opt/app/logs
VOLUME /opt/app/data
VOLUME /tmp

EXPOSE ${SERVER_PORT}

ENTRYPOINT ["java", "-Xshare:off", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/opt/app/app.jar"]