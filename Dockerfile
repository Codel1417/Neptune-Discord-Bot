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

FROM openjdk:11.0.9.1-jre as FINAL
WORKDIR /nep/

# Copy build binaries
COPY --from=buildNep /nep/Neptune-Discord-Bot/target/Neptune_Discord_Bot-1.0-SNAPSHOT.jar .
COPY --from=codel1417/anime4kccp-builder:latest /nep/Neptune-Discord-Bot/dependentcies/Anime4KCPP/build/bin/Anime4KCPP_CLI /nep/Neptune-Discord-Bot/dependentcies/Anime4KCPP/build/bin/Anime4KCPP_CLI
COPY --from=GIT /nep/Neptune-Discord-Bot/dependentcies/tessdata_best/ /nep/Neptune-Discord-Bot/dependentcies/tessdata_best/

#install runtime dependencies
RUN apt update && apt install -y tesseract-ocr

VOLUME /nep/Guilds
VOLUME /nep/Logs
VOLUME /nep/Media

ENV botToken=null
ENV tenorToken=null

ENTRYPOINT java -jar Neptune_Discord_Bot-1.0-SNAPSHOT.jar -d $botToken -t $tenorToken