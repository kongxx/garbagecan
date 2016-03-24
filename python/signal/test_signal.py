#!/usr/bin/env python
# -*- coding: utf-8 -*-
import signal
import time

def signal_handler(signum, frame):
    print('Received signal: ', signum)

while True:
    signal.signal(signal.SIGHUP, signal_handler) # 1
    signal.signal(signal.SIGINT, signal_handler) # 2
    signal.signal(signal.SIGQUIT, signal_handler) # 3
    signal.signal(signal.SIGALRM, signal_handler) # 14
    signal.signal(signal.SIGTERM, signal_handler) # 15
    signal.signal(signal.SIGCONT, signal_handler) # 18
    while True:
        print('waiting')
        time.sleep(1)
