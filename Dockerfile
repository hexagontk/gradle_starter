
#FROM eclipse-temurin:17-jre-alpine
FROM ibm-semeru-runtimes:open-17-jre
#FROM ubuntu:jammy

# Project setup
ENV PROJECT gradle_starter
ENV TZ UTC

EXPOSE 9090
VOLUME /var/log

# Machine setup
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Project install
USER 1000
ADD --chown=1000:1000 build/distributions/$PROJECT*.tar /opt
WORKDIR /opt/$PROJECT*
ENTRYPOINT /opt/$PROJECT*/bin/$PROJECT
#ADD --chown=1000:1000 build/distributions/$PROJECT*-linux-amd64.zip /opt
#WORKDIR /opt
#ENTRYPOINT /opt/$PROJECT
