# syntax=docker/dockerfile:experimental
FROM  maven:3.6.1-jdk-11 AS buildNep
WORKDIR /nep/
RUN git clone https://github.com/Codel1417/Neptune-Discord-Bot.git
WORKDIR /nep/Neptune-Discord-Bot
# cache maven repo
RUN --mount=type=cache,target=/root/.m2 \
    mvn clean package

FROM debian:bullseye-slim AS Anime4KCPP
WORKDIR /nep/Neptune-Discord-Bot/dependentcies/
ENV DEBIAN_FRONTEND=noninteractive
# caches the apt repo locally
RUN rm -f /etc/apt/apt.conf.d/docker-clean; echo 'Binary::apt::APT::Keep-Downloaded-Packages "true";' > /etc/apt/apt.conf.d/keep-cache
RUN --mount=type=cache,target=/var/cache/apt --mount=type=cache,target=/var/lib/apt \
    apt update && \
	apt install -y --no-install-recommends \
		git \
		ca-certificates \
        libopencv-dev \
        ocl-icd-opencl-dev \
        cmake \
		build-essential
RUN git clone https://github.com/TianZerL/Anime4KCPP.git
WORKDIR /nep/Neptune-Discord-Bot/dependentcies/Anime4KCPP/
# Switch to a stable build
RUN git checkout 0f36eb56027a22502694d3050a0935abb0abdc08 
WORKDIR /nep/Neptune-Discord-Bot/dependentcies/Anime4KCPP/build/
RUN cmake .. && make 
LABEL binary="/nep/Neptune-Discord-Bot/dependentcies/Anime4KCPP/build/bin/Anime4KCPP_CLI"


FROM openjdk:11-slim as FINAL
WORKDIR /nep/dependentcies/
RUN  \
    apt update \
    && apt install -y --no-install-recommends \
        tesseract-ocr \
        git \
        ca-certificates
RUN git clone https://github.com/tesseract-ocr/tessdata_best
WORKDIR /nep/
# Copy build binaries
COPY --from=Anime4KCPP /nep/Neptune-Discord-Bot/dependentcies/Anime4KCPP/build/bin/Anime4KCPP_CLI /nep/dependentcies/Anime4KCPP/build/bin/Anime4KCPP_CLI
COPY --from=buildNep /nep/Neptune-Discord-Bot/target/Neptune_Discord_Bot-1.0-SNAPSHOT.jar .
VOLUME /nep/Guilds
VOLUME /nep/Logs
VOLUME /nep/Media
ENV botToken=null
ENV tenorToken=null
ENTRYPOINT java -jar Neptune_Discord_Bot-1.0-SNAPSHOT.jar -d $botToken -t $tenorToken