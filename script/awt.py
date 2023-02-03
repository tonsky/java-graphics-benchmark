#!/usr/bin/env python

import glob
import os
import platform
import re
import ssl
import subprocess
import sys

WD = os.path.normpath(os.path.join(os.path.dirname(__file__), os.pardir))

def mkdirs(path):
  try:
    os.makedirs(path)
  except:
    pass

def main(argv):
  subprocess.check_call(["javac", "-encoding", "UTF8", "--release", "11", "-d", "classes"] + glob.glob("src/awt/*.java"), cwd=WD)
  subprocess.check_call([
    "java", 
    "-cp", "classes",
    "-ea",
    "--illegal-access=warn",
    "--add-opens=java.desktop/java.awt.event=ALL-UNNAMED",
    "--add-opens=java.desktop/java.awt=ALL-UNNAMED",
    "--add-opens=java.desktop/sun.awt=ALL-UNNAMED",
    "--add-opens=java.base/java.lang=ALL-UNNAMED",
    "--add-opens=java.base/java.util=ALL-UNNAMED",
    "--add-opens=java.desktop/javax.swing=ALL-UNNAMED",
    "--add-opens=java.desktop/sun.swing=ALL-UNNAMED",
    "--add-opens=java.desktop/javax.swing.plaf.basic=ALL-UNNAMED",
    "--add-opens=java.desktop/java.awt.peer=ALL-UNNAMED",
    "--add-opens=java.desktop/javax.swing.text.html=ALL-UNNAMED",
    "--add-exports=java.desktop/sun.font=ALL-UNNAMED",
    "--add-exports=java.desktop/com.apple.eawt=ALL-UNNAMED",
    "--add-exports=java.desktop/com.apple.laf=ALL-UNNAMED",
    "--add-opens=java.desktop/sun.java2d.pipe.hw=ALL-UNNAMED",
    "awt.Main"], cwd=WD)
  return 0

if __name__ == '__main__':
  exit(main(sys.argv[1:]))