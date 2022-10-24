# I need Tesseract version 4.1 or newer.
FROM gradle:6.9.3-jdk17-alpine as build

RUN useradd -ms /bin/bash  neptune

USER neptune:neptune

ADD --chown=neptune:neptune ./ /nep
WORKDIR /nep/

RUN chmod +x gradlew && ./gradlew uberJar -x test

from openjdk:17-jdk-alpine as runtime
RUN useradd -ms /bin/bash  neptune
USER neptune:neptune

WORKDIR /nep/
COPY --chown=neptune:neptune --from=build /nep/build/libs/neptune-1.0-SNAPSHOT.jar /nep/neptune.jar

VOLUME /nep/Media

ENV NEPTUNE_TOKEN=null
ENV NEPTUNE_TENOR_TOKEN=null
ENV NEPTUNE_COMMIT_ID="$(git rev-parse --short HEAD)"

CMD java -jar neptune.jar
