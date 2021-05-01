# See here for image contents: https://github.com/microsoft/vscode-dev-containers/tree/v0.148.1/containers/java/.devcontainer/base.Dockerfile

# [Choice] Java version: 11, 15
ARG VARIANT="15"

FROM openjdk:11-jdk-sid
RUN export DEBIAN_FRONTEND=noninteractive \
    && apt update \
    && apt install -y maven



# [Optional] Uncomment this section to install additional OS packages.
RUN echo "deb http://http.us.debian.org/debian unstable main non-free contrib" > /etc/apt/sources.list \
    && apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y --no-install-recommends install tesseract-ocr -o APT::Immediate-Configure=0
RUN git clone https://github.com/tesseract-ocr/tessdata_best /tessdata

# [Optional] Uncomment this line to install global node packages.
# RUN su vscode -c "source /usr/local/share/nvm/nvm.sh && npm install -g <your-package-here>" 2>&1