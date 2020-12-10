#!/usr/bin/env python

import glob
import os
import platform
import re
import ssl
import subprocess
import sys
import urllib
import zipfile

WD     = os.path.normpath(os.path.join(os.path.dirname(__file__), os.pardir))
HOME   = os.path.expanduser('~')
M2     = os.path.join(HOME, ".m2", "repository")
CP_SEP = ";" if platform.system() == "Windows" else ":"

def mkdirs(path):
  try:
    os.makedirs(path)
  except:
    pass

if not os.path.isdir(os.path.join(WD, "libs")):
  mkdirs(os.path.join(WD, "libs"))

def fetch(url, file):
  if not (os.path.exists(file)):
    print("Downloading " + os.path.basename(file))
    mkdirs(os.path.dirname(file))
    subprocess.check_call(["curl", "--fail", "--location", "--show-error", "--silent", "-o", file, url], cwd=WD)

def add_cp(lib, repo="https://repo1.maven.org/maven2"):
  target = os.path.join(M2, lib)
  fetch(repo + "/" + lib, target)
  return target

def find_files(dir, pattern=re.compile(".*")):
  for root, dirs, files in os.walk(dir):
    for name in files:
      if pattern.match(name):
        yield os.path.join(root, name)

def main(argv):
  version = "15"
  if platform.system() == "Windows":
    classifier = "windows"
  elif platform.system() == "Darwin":
    classifier = "osx"
  elif platform.system() == "Linux":
    classifier = "linux"

  if not os.path.isdir(os.path.join(WD, "libs", "javafx-sdk-" + version)):
    fetch("https://download2.gluonhq.com/openjfx/" + version + "/openjfx-" + version + "_" + classifier + "-x64_bin-sdk.zip", os.path.join(WD, "libs", "openjfx-" + version + "_" + classifier + "-x64_bin-sdk.zip"))
    with zipfile.ZipFile(os.path.join(WD, "libs", "openjfx-" + version + "_" + classifier + "-x64_bin-sdk.zip"), "r") as zip:
      zip.extractall(os.path.join(WD, "libs"))

  if not os.path.isdir(os.path.join(WD, "libs", "javafx-src-" + version)):
    with zipfile.ZipFile(os.path.join(WD, "libs", "javafx-sdk-" + version, "lib", "src.zip"), "r") as zip:
      zip.extractall(os.path.join(WD, "libs", "javafx-src-" + version))

  if not os.path.isdir(os.path.join(WD, "libs", "javafx-mods-" + version)):
    fetch("https://download2.gluonhq.com/openjfx/" + version + "/openjfx-" + version + "_" + classifier + "-x64_bin-jmods.zip", os.path.join(WD, "libs", "openjfx-" + version + "_" + classifier + "-x64_bin-mods.zip"))
    with zipfile.ZipFile(os.path.join(WD, "libs", "openjfx-" + version + "_" + classifier + "-x64_bin-mods.zip"), "r") as zip:
      zip.extractall(os.path.join(WD, "libs"))

  if not os.path.isdir(os.path.join(WD, "libs", "javafx-" + version + "-javadoc")):
    fetch("https://download2.gluonhq.com/openjfx/" + version + "/openjfx-" + version + "-javadoc.zip", os.path.join(WD, "libs", "openjfx-" + version + "-javadoc.zip"))
    with zipfile.ZipFile(os.path.join(WD, "libs", "openjfx-" + version + "-javadoc.zip"), "r") as zip:
      zip.extractall(os.path.join(WD, "libs"))

  subprocess.check_call(["javac",
    "-encoding", "UTF8",
    "--release", "11",
    "--module-path", "libs/javafx-sdk-15/lib",
    "--add-modules", "javafx.graphics",
    "-d", "classes"] 
    + glob.glob("src/fx/*.java"),
    cwd=WD)

  subprocess.check_call(["java",
    "--module-path", "libs/javafx-sdk-15/lib",
    "--add-modules", "javafx.graphics",
    "-cp", "classes",
    "-Dprism.verbose=true",
    "-Djavafx.animation.fullspeed=true",
    # "-Djavafx.pulseLogger=true",
    "fx.Main"],
    cwd=WD)

  return 0

if __name__ == '__main__':
  exit(main(sys.argv[1:]))