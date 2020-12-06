#!/bin/bash
set -o errexit -o nounset -o pipefail
cd "`dirname $0`/.."

version=15

pushd libs > /dev/null

if [ ! -d javafx-sdk-${version} ] ; then
  curl https://download2.gluonhq.com/openjfx/${version}/openjfx-${version}_osx-x64_bin-sdk.zip | tar -xz
  unzip -q javafx-sdk-${version}/lib/src.zip -d javafx-src-${version}
fi

if [ ! -d javafx-jmods-${version} ] ; then
  curl https://download2.gluonhq.com/openjfx/${version}/openjfx-${version}_osx-x64_bin-jmods.zip | tar -xz
fi

if [ ! -d javafx-${version}-javadoc ] ; then
  curl https://download2.gluonhq.com/openjfx/${version}/openjfx-${version}-javadoc.zip | tar -xz
fi

popd > /dev/null

module_path=libs/javafx-sdk-${version}/lib
modules=javafx.graphics

javac --module-path ${module_path} \
  --add-modules ${modules} \
  -d classes \
  src/fx/*.java

# -Djavafx.pulseLogger=true

java --module-path ${module_path} \
  --add-modules ${modules} \
  -cp classes \
  -Dprism.verbose=true \
  -Djavafx.animation.fullspeed=true \
  fx.Main