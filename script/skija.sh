#!/bin/bash
set -o errexit -o nounset -o pipefail
cd "`dirname $0`/.."

classpath=""
opts=""

add_cp() {
    lib=$1
    repo=${2:-https://repo1.maven.org/maven2}
    if [[ ! -f $HOME/.m2/repository/$lib ]] ; then
        echo "Downloading `basename $lib`"
        mkdir -p $HOME/.m2/repository/`dirname $lib`
        curl --fail --location --show-error --silent -o $HOME/.m2/repository/$lib $repo/$lib
    fi
    classpath=$classpath:$HOME/.m2/repository/$lib
}

add_cp "org/lwjgl/lwjgl/3.2.3/lwjgl-3.2.3.jar"
add_cp "org/lwjgl/lwjgl-glfw/3.2.3/lwjgl-glfw-3.2.3.jar"
add_cp "org/lwjgl/lwjgl-opengl/3.2.3/lwjgl-opengl-3.2.3.jar"
add_cp "org/jetbrains/skija/skija-shared/0.6.24/skija-shared-0.6.24.jar" "https://packages.jetbrains.team/maven/p/skija/maven"

if [[ `uname` == 'Linux' ]]; then
    add_cp "org/lwjgl/lwjgl/3.2.3/lwjgl-3.2.3-natives-linux.jar"
    add_cp "org/lwjgl/lwjgl-glfw/3.2.3/lwjgl-glfw-3.2.3-natives-linux.jar"
    add_cp "org/lwjgl/lwjgl-opengl/3.2.3/lwjgl-opengl-3.2.3-natives-linux.jar"
    add_cp "org/jetbrains/skija/skija-linux/0.6.24/skija-linux-0.6.24.jar" "https://packages.jetbrains.team/maven/p/skija/maven"
elif [[ `uname` == 'Darwin' ]]; then
    add_cp "org/lwjgl/lwjgl/3.2.3/lwjgl-3.2.3-natives-macos.jar"
    add_cp "org/lwjgl/lwjgl-glfw/3.2.3/lwjgl-glfw-3.2.3-natives-macos.jar"
    add_cp "org/lwjgl/lwjgl-opengl/3.2.3/lwjgl-opengl-3.2.3-natives-macos.jar"
    add_cp "org/jetbrains/skija/skija-macos/0.6.24/skija-macos-0.6.24.jar" "https://packages.jetbrains.team/maven/p/skija/maven"
    opts="-XstartOnFirstThread"
else
    add_cp "org/lwjgl/lwjgl/3.2.3/lwjgl-3.2.3-natives-windows.jar"
    add_cp "org/lwjgl/lwjgl-glfw/3.2.3/lwjgl-glfw-3.2.3-natives-windows.jar"
    add_cp "org/lwjgl/lwjgl-opengl/3.2.3/lwjgl-opengl-3.2.3-natives-windows.jar"
    add_cp "org/jetbrains/skija/skija-windows/0.6.24/skija-windows-0.6.24.jar" "https://packages.jetbrains.team/maven/p/skija/maven"
fi

javac -encoding UTF8 --release 11 -cp $classpath -d classes src/skija/*.java
java -cp $classpath:classes $opts -Djava.awt.headless=true skija.Main