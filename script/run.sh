#!/bin/zsh -euo pipefail

#FX=javafx-jmods-14
FX=javafx-sdk-15/lib
# -Djavafx.animation.fullspeed=true

javac --module-path $FX --add-modules javafx.graphics -d classes src/FX.java src/fx/*.java

# -Djavafx.pulseLogger=true

java --module-path $FX \
  --add-modules javafx.graphics \
  -cp classes \
  -Dprism.verbose=true \
  -Djavafx.animation.fullspeed=true \
  FX