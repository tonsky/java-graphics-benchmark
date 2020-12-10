#!/usr/bin/env python

import shutil
import os

WD = os.path.normpath(os.path.join(os.path.dirname(__file__), os.pardir))

if __name__ == '__main__':
  shutil.rmtree(os.path.join(WD, "classes"))