
FROM ubuntu:jammy

# Project setup
ARG PROJECT
ARG TZ=UTC

VOLUME /var/log

# Machine setup
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Project install
ENV PROJECT $PROJECT
USER 1000
ADD --chown=1000:1000 build/distributions/$PROJECT*-1.0.0.tbz2 /opt/$PROJECT
WORKDIR /opt/$PROJECT
ENTRYPOINT /opt/$PROJECT/bin/$PROJECT
