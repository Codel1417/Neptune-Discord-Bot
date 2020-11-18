# syntax=docker/dockerfile:experimental
FROM  maven:3.6.1-jdk-11 AS buildNep
WORKDIR /nep/
RUN git clone https://github.com/Codel1417/Neptune-Discord-Bot.git
WORKDIR /nep/Neptune-Discord-Bot
# cache maven repo
RUN --mount=type=cache,target=/root/.m2 \
    mvn clean package

FROM openjdk:11 as FINAL
WORKDIR /nep/dependentcies/
RUN  \
    apt update \
    && apt install -y --no-install-recommends \
        tesseract-ocr
RUN git clone https://github.com/tesseract-ocr/tessdata_fast tessdata
WORKDIR /nep/
# Copy build binaries
COPY --from=codel1417/anime4kccp-builder:latest /nep/Neptune-Discord-Bot/dependentcies/Anime4KCPP/build/bin/Anime4KCPP_CLI /nep/dependentcies/Anime4KCPP/Anime4KCPP_CLI
COPY --from=buildNep /nep/Neptune-Discord-Bot/target/Neptune_Discord_Bot-1.0-SNAPSHOT.jar .
VOLUME /nep/Guilds
VOLUME /nep/Logs
VOLUME /nep/Media
ENV botToken=null
ENV tenorToken=null
ENTRYPOINT java -jar Neptune_Discord_Bot-1.0-SNAPSHOT.jar -d $botToken -t $tenorToken