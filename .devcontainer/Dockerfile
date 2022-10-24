# See here for image contents: https://github.com/microsoft/vscode-dev-containers/tree/v0.148.1/containers/java/.devcontainer/base.Dockerfile

# [Choice] Java version: 11, 15
ARG VARIANT="15"

FROM openjdk:11-jdk-sid
RUN export DEBIAN_FRONTEND=noninteractive \
    && apt update \
    && apt install -y unzip

RUN wget https://services.gradle.org/distributions/gradle-6.5.1-bin.zip -P /tmp \
    && unzip -d /opt/gradle /tmp/gradle-6.5.1-bin.zip \
    && ln -s /opt/gradle/gradle-6.5.1 /opt/gradle/latest
    
ENV GRADLE_HOME=/opt/gradle/latest
ENV PATH=${GRADLE_HOME}/bin:${PATH}

# [Optional] Uncomment this line to install global node packages.
# RUN su vscode -c "source /usr/local/share/nvm/nvm.sh && npm install -g <your-package-here>" 2>&1