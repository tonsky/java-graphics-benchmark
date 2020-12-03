#!/bin/zsh -euo pipefail

curl https://download2.gluonhq.com/openjfx/15/openjfx-15_osx-x64_bin-sdk.zip | tar -xz
curl https://download2.gluonhq.com/openjfx/15/openjfx-15_osx-x64_bin-jmods.zip | tar -xz
curl https://download2.gluonhq.com/openjfx/15/openjfx-15-javadoc.zip | tar -xz
unzip -q javafx-sdk-15/lib/src.zip -d javafx-src-15