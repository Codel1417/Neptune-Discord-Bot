# I need Tesseract version 4.1 or newer.
FROM ubuntu:latest

ARG COMMIT_ID

# https://packages.debian.org/sid/tesseract-ocr
# -o APT::Immediate-Configure=0  Fixes an issue with one of tesseract's deps
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get install -y --no-install-recommends tesseract-ocr unzip openjdk-11-jdk git maven -o APT::Immediate-Configure=0

# Set up Jstatd
RUN git clone https://github.com/Codel1417/ejstatd.git /ejstatd && chown 1000:1000 -R /ejstatd
USER 1000
WORKDIR /ejstatd/
RUN mvn package


ADD --chown=1000:1000 ./ /nep
WORKDIR /nep/

RUN chmod +x gradlew && ./gradlew build

VOLUME /nep/Logs
VOLUME /nep/Media
VOLUME /nep/tessdata

#prometheus
EXPOSE 1234
#ejstatd
EXPOSE 2222
EXPOSE 2223
EXPOSE 2224
#jmx
EXPOSE 1232
EXPOSE 1231

ENV NEPTUNE_TOKEN=null
ENV NEPTUNE_TENOR_TOKEN=null
ENV NEPTUNE_COMMIT_ID=$COMMIT_ID
ENV NEPTUNE_TESSERACT_DIR=/nep/tessdata
ENV SENTRY_DSN=null

RUN chmod +x run.sh
CMD ./run.sh
