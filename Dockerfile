# I need Tesseract version 4.1 or newer.
FROM gradle:7.5-jdk17-alpine as build

ADD ./ /nep
WORKDIR /nep/

RUN gradle uberJar -x test

FROM eclipse-temurin:17-jre-alpine  as runtime
USER 405:405

WORKDIR /nep/
COPY --chown=405:405 --from=build /nep/build/libs/neptune-1.0-SNAPSHOT.jar /nep/neptune.jar

VOLUME /nep/Media

ENV NEPTUNE_TOKEN=null
ENV NEPTUNE_TENOR_TOKEN=null
ENV NEPTUNE_COMMIT_ID="$(git rev-parse --short HEAD)"

CMD java -jar neptune.jar
