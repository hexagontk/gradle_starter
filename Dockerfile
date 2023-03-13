
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
CMD /opt/$PROJECT*/bin/$PROJECT
#ADD --chown=1000:1000 build/native/nativeCompile/$PROJECT /opt/$PROJECT
#WORKDIR /opt
#CMD /opt/$PROJECT
