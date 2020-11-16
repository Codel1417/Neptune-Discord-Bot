# Store an unmodified git folder
FROM alpine:latest as GIT
WORKDIR /nep/
RUN apk add --no-cache git
RUN git clone --recursive https://github.com/Codel1417/Neptune-Discord-Bot.git


FROM maven:3.6.3-openjdk-11 AS buildNep
WORKDIR /nep/
COPY --from=GIT /nep/Neptune-Discord-Bot/ /nep/Neptune-Discord-Bot/
WORKDIR /nep/Neptune-Discord-Bot
RUN mvn package


FROM ubuntu:latest AS Anime4KCPP
WORKDIR /nep/

COPY --from=GIT /nep/Neptune-Discord-Bot/dependentcies/opencv/ /nep/Neptune-Discord-Bot/dependentcies/opencv/
WORKDIR /nep/Neptune-Discord-Bot/dependentcies/opencv/
RUN mkdir build
RUN \
    apt update && \
    apt install -y \
        libavcodec-dev \
        libavformat-dev \
        libswscale-dev \
        build-essential \
        cmake
WORKDIR /nep/Neptune-Discord-Bot/dependentcies/opencv/build/
RUN cmake -D CMAKE_BUILD_TYPE=RELEASE \
        -D CMAKE_INSTALL_PREFIX=/usr/local \
        -D INSTALL_PYTHON_EXAMPLES=ON \
        -D INSTALL_C_EXAMPLES=ON .. && \
        make -j$(nproc) && \
        make install


RUN \
    apt update && \
    apt install -y \
        ocl-icd-opencl-dev \
        cmake
COPY --from=GIT /nep/Neptune-Discord-Bot/dependentcies/Anime4KCPP/ /nep/Neptune-Discord-Bot/dependentcies/Anime4KCPP/
WORKDIR /nep/Neptune-Discord-Bot/dependentcies/Anime4KCPP/
RUN mkdir build && cd build && cmake && make 


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