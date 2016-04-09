#!/usr/bin/env python
# -*- coding: utf-8 -*-
from apscheduler.triggers.base import BaseTrigger


class MultiCronTrigger(BaseTrigger):
    triggers = []

    def __init__(self, triggers):
        self.triggers = triggers

    def get_next_fire_time(self, previous_fire_time, now):
        min_next_fire_time = None
        for trigger in self.triggers:
            next_fire_time = trigger.get_next_fire_time(previous_fire_time, now)
            if next_fire_time is None:
                continue
            if min_next_fire_time is None:
                min_next_fire_time = next_fire_time
            if next_fire_time < min_next_fire_time:
                min_next_fire_time = next_fire_time
        return min_next_fire_time


import time
from datetime import datetime
from apscheduler.schedulers.background import BackgroundScheduler
from apscheduler.triggers.cron import CronTrigger

def myjob():
    print('job run at %s' % datetime.now())

if __name__ == '__main__':
    scheduler = BackgroundScheduler()
    trigger1 = CronTrigger(hour='*', minute='*', second='*/3')
    trigger2 = CronTrigger(hour='*', minute='*', second='*/5')
    job = scheduler.add_job(myjob, MultiCronTrigger([trigger1, trigger2]))
    scheduler.start()

try:
    while True:
        time.sleep(5)
except (KeyboardInterrupt, SystemExit):
    scheduler.shutdown()

