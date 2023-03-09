
FROM eclipse-temurin:17-jre-alpine
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
WORKDIR /opt/$PROJECT*
ADD --chown=1000:1000 build/distributions/$PROJECT*.tar /opt
ENTRYPOINT /opt/$PROJECT*/bin/$PROJECT
#ADD --chown=1000:1000 build/$PROJECT /opt/$PROJECT
#ENTRYPOINT /opt/$PROJECT/bin/$PROJECT
