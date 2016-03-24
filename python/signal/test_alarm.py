#!/usr/bin/env python
# -*- coding: utf-8 -*-
import signal
import time

def signal_handler(signum, frame):
    print('Received signal: ', signum)

while True:
    signal.signal(signal.SIGALRM, signal_handler) # 14
    signal.alarm(5)
    while True:
        print('waiting')
        time.sleep(1)
