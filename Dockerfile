
FROM adoptopenjdk:11-jre-hotspot-bionic
ENV PROJECT gradle_starter

USER 1000
ADD --chown=1000:1000 build/distributions/$PROJECT*.tar /opt
WORKDIR /opt/$PROJECT*
EXPOSE 9090
ENTRYPOINT /opt/$PROJECT*/bin/$PROJECT
