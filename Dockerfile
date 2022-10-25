# I need Tesseract version 4.1 or newer.
FROM gradle:7.5-jdk17-alpine AS build

ADD ./ /nep
WORKDIR /nep/

RUN gradle uberJar -x test

FROM eclipse-temurin:19-jre-alpine AS runtime

WORKDIR /nep/
COPY --from=build /nep/build/libs/Neptune-Discord-Bot-1.0-SNAPSHOT-uber.jar /nep/neptune.jar
USER 405:405
VOLUME /nep/Media

ENV NEPTUNE_TOKEN=null
ENV NEPTUNE_TENOR_TOKEN=null
ENV NEPTUNE_COMMIT_ID="$(git rev-parse --short HEAD)"

CMD java -jar neptune.jar
