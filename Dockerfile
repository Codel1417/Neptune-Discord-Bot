# Store an unmodified git folder
FROM alpine:latest as GIT
WORKDIR /nep/
RUN apk add --no-cache git
RUN git clone --recursive https://github.com/Codel1417/Neptune-Discord-Bot.git
WORKDIR /nep/Neptune-Discord-Bot/dependentcies/Anime4KCPP/
#Release 2.4
RUN git checkout -B 0f36eb56027a22502694d3050a0935abb0abdc08


FROM maven:3.6.3-openjdk-11 AS buildNep
WORKDIR /nep/
COPY --from=GIT /nep/Neptune-Discord-Bot/ /nep/Neptune-Discord-Bot/
WORKDIR /nep/Neptune-Discord-Bot
RUN mvn package


FROM ubuntu:latest AS Anime4KCPP
WORKDIR /nep/
ENV DEBIAN_FRONTEND=noninteractive

RUN \
    apt update && \
    apt install -y \
        libopencv-dev \
        ocl-icd-opencl-dev \
        cmake \
        gcc \
        g++ \
        clang \
        build-essential \
        lld \
        llvm \
        clang-tools \
        xz-utils \
        libboost-all-dev

COPY --from=GIT /nep/Neptune-Discord-Bot/dependentcies/Anime4KCPP/ /nep/Neptune-Discord-Bot/dependentcies/Anime4KCPP/
WORKDIR /nep/Neptune-Discord-Bot/dependentcies/Anime4KCPP/
RUN mkdir build && cd build && cmake .. && make 


FROM openjdk:11-alpine as FINAL
WORKDIR /nep/

# Copy build binaries
COPY --from=buildNep /nep/Neptune-Discord-Bot/target/Neptune_Discord_Bot-1.0-SNAPSHOT.jar .
COPY --from=Anime4KCPP /nep/Neptune-Discord-Bot/dependentcies/Anime4KCPP/build/bin/Anime4KCPP_CLI /nep/Neptune-Discord-Bot/dependentcies/Anime4KCPP/build/bin/Anime4KCPP_CLI
COPY --from=GIT /nep/Neptune-Discord-Bot/dependentcies/tessdata_best/ /nep/Neptune-Discord-Bot/dependentcies/tessdata_best/

#install runtime dependencies
RUN echo 'http://dl-cdn.alpinelinux.org/alpine/edge/testing' >> /etc/apk/repositories && \
    apk add --no-cache tesseract-ocr


VOLUME /nep/Guilds
VOLUME /nep/Logs
VOLUME /nep/Media

ENTRYPOINT java -jar Neptune_Discord_Bot-1.0-SNAPSHOT.jar -d $botToken -t $tenorToken