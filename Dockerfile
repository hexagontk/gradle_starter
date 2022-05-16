
FROM eclipse-temurin:17-jre-alpine
LABEL description="description"

# Project setup
ENV PROJECT gradle_starter
ENV TZ UTC
ENV PORT 9090

EXPOSE $PORT

# Machine setup
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Project install
USER 1000
ADD --chown=1000:1000 build/distributions/$PROJECT*.tar /opt
WORKDIR /opt/$PROJECT*
ENTRYPOINT /opt/$PROJECT*/bin/$PROJECT
