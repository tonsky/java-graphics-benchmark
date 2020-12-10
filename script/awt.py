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
  subprocess.check_call(["java", "-cp", "classes", "-ea", "awt.Main"], cwd=WD)
  return 0

if __name__ == '__main__':
  exit(main(sys.argv[1:]))