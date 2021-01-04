FROM openjdk:11.0.6-jdk-slim

MAINTAINER  Zakaria MAARAKI <zmaaraki@email.com>

USER root

RUN apt update && apt install -y minisat

ADD src src

RUN chmod a+x src/Main.java

ENTRYPOINT ["java", "src/Main.java"]