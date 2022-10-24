# I need Tesseract version 4.1 or newer.
FROM gradle:6.9.3-jdk17-alpine as build

RUN useradd -ms /bin/bash  neptune

USER neptune:neptune

ADD --chown=neptune:neptune ./ /nep
WORKDIR /nep/

RUN chmod +x gradlew && ./gradlew uberJar -x test

from eclipse-temurin:17-jre-alpine  as runtime
USER 405:405

WORKDIR /nep/
COPY --chown=405:405 --from=build /nep/build/libs/neptune-1.0-SNAPSHOT.jar /nep/neptune.jar

VOLUME /nep/Media

ENV NEPTUNE_TOKEN=null
ENV NEPTUNE_TENOR_TOKEN=null
ENV NEPTUNE_COMMIT_ID="$(git rev-parse --short HEAD)"

CMD java -jar neptune.jar
