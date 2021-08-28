# I need Tesseract version 4.1 or newer.
FROM ubuntu:latest

ARG COMMIT_ID

# https://packages.debian.org/sid/tesseract-ocr
# -o APT::Immediate-Configure=0  Fixes an issue with one of tesseract's deps
RUN  apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get install -y --no-install-recommends tesseract-ocr unzip openjdk-jdk-11 -o APT::Immediate-Configure=0

WORKDIR /nep/
RUN chown -R 1000:1000 .

USER 1000

RUN chmod +x gradlew && ./gradlew build

VOLUME /nep/Guilds
VOLUME /nep/Logs
VOLUME /nep/Media
VOLUME /nep/tessdata

#prometheus
EXPOSE 1234 
#jstatd
EXPOSE 1233
#jmx
EXPOSE 1232

ENV NEPTUNE_TOKEN=null
ENV NEPTUNE_TENOR_TOKEN=null
ENV NEPTUNE_COMMIT_ID=$COMMIT_ID
ENV NEPTUNE_TESSERACT_DIR=/nep/tessdata
ENV SENTRY_DSN=null

CMD run.sh
