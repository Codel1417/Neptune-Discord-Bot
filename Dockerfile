# I need Tesseract version 4.1 or newer.
FROM ubuntu

# https://packages.debian.org/sid/tesseract-ocr
# -o APT::Immediate-Configure=0  Fixes an issue with one of tesseract's deps
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get install -y --no-install-recommends openjdk-17-jdk-headless -o APT::Immediate-Configure=0

RUN useradd -ms /bin/bash  neptune

USER neptune:neptune


ADD --chown=neptune:neptune ./ /nep
WORKDIR /nep/

RUN chmod +x gradlew && ./gradlew build -x test

VOLUME /nep/Media

ENV NEPTUNE_TOKEN=null
ENV NEPTUNE_TENOR_TOKEN=null
ENV NEPTUNE_COMMIT_ID="$(git rev-parse --short HEAD)"

RUN chmod +x run.sh
CMD ./run.sh
