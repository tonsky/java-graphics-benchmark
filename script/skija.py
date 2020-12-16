#!/usr/bin/env python

import glob
import os
import platform
import re
import ssl
import subprocess
import sys

WD     = os.path.normpath(os.path.join(os.path.dirname(__file__), os.pardir))
HOME   = os.path.expanduser('~')
M2     = os.path.join(HOME, ".m2", "repository")
CP_SEP = ";" if platform.system() == "Windows" else ":"

def mkdirs(path):
  try:
    os.makedirs(path)
  except:
    pass

def fetch(url, file):
  if not (os.path.exists(file)):
    print("Downloading " + os.path.basename(file))
    mkdirs(os.path.dirname(file))
    subprocess.check_call(["curl", "--fail", "--location", "--show-error", "--silent", "-o", file, url], cwd=WD)

def fetch_jar(lib, repo="https://repo1.maven.org/maven2"):
  target = os.path.join(M2, lib)
  fetch(repo + "/" + lib, target)
  return target

def main(argv):
  lwjgl = "3.2.3"
  skija = "0.6.37"
  if platform.system() == "Windows":
    classifier = "windows"
  elif platform.system() == "Darwin":
    classifier = "macos"
  elif platform.system() == "Linux":
    classifier = "linux"

  cp = fetch_jar("org/lwjgl/lwjgl/" + lwjgl + "/lwjgl-" + lwjgl + ".jar")
  cp = cp + CP_SEP + fetch_jar("org/lwjgl/lwjgl-glfw/" + lwjgl + "/lwjgl-glfw-" + lwjgl + ".jar")
  cp = cp + CP_SEP + fetch_jar("org/lwjgl/lwjgl-opengl/" + lwjgl + "/lwjgl-opengl-" + lwjgl + ".jar")
  cp = cp + CP_SEP + fetch_jar("org/jetbrains/skija/skija-shared/" + skija + "/skija-shared-" + skija + ".jar", "https://packages.jetbrains.team/maven/p/skija/maven")

  cp = cp + CP_SEP + fetch_jar("org/lwjgl/lwjgl/" + lwjgl + "/lwjgl-" + lwjgl + "-natives-" + classifier + ".jar")
  cp = cp + CP_SEP + fetch_jar("org/lwjgl/lwjgl-glfw/" + lwjgl + "/lwjgl-glfw-" + lwjgl + "-natives-" + classifier + ".jar")
  cp = cp + CP_SEP + fetch_jar("org/lwjgl/lwjgl-opengl/" + lwjgl + "/lwjgl-opengl-" + lwjgl + "-natives-" + classifier + ".jar")
  cp = cp + CP_SEP + fetch_jar("org/jetbrains/skija/skija-" + classifier + "/" + skija + "/skija-" + classifier + "-" + skija + ".jar", "https://packages.jetbrains.team/maven/p/skija/maven")

  java_opts = ["-XstartOnFirstThread"] if platform.system() == "Darwin" else []
  
  subprocess.check_call(["javac", "-encoding", "UTF8", "--release", "11", "-cp", cp, "-d", "classes"] + glob.glob("src/skija/*.java"), cwd=WD)
  subprocess.check_call(["java", "-version"])
  subprocess.check_call(["java", "-cp", "classes" + CP_SEP + cp, "-Djava.awt.headless=true"] + java_opts + ["skija.Main"], cwd=WD)
  return 0

if __name__ == '__main__':
  exit(main(sys.argv[1:]))
