# I need Tesseract version 4.1 or newer.
FROM ubuntu:impish-20211015

# https://packages.debian.org/sid/tesseract-ocr
# -o APT::Immediate-Configure=0  Fixes an issue with one of tesseract's deps
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get install -y --no-install-recommends tesseract-ocr openjdk-11-jdk -o APT::Immediate-Configure=0

RUN useradd -ms /bin/bash  neptune
# Set up Jstatd
#RUN git clone https://github.com/Codel1417/ejstatd.git /ejstatd && chown neptune:neptune -R /ejstatd
USER neptune:neptune
#WORKDIR /ejstatd/
#RUN mvn package


ADD --chown=neptune:neptune ./ /nep
WORKDIR /nep/

RUN chmod +x gradlew && ./gradlew build -x test

VOLUME /nep/Media
VOLUME /nep/tessdata

#prometheus
EXPOSE 1234

ENV NEPTUNE_TOKEN=null
ENV NEPTUNE_TENOR_TOKEN=null
ENV NEPTUNE_COMMIT_ID="$(git rev-parse --short HEAD)"
ENV NEPTUNE_TESSERACT_DIR=/nep/tessdata
ENV SENTRY_DSN=null

RUN chmod +x run.sh
CMD ./run.sh
