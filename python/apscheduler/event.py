import os, time
from datetime import datetime
from apscheduler.schedulers.background import BackgroundScheduler
from apscheduler.events import *

def myjob_1():
	print('myjob_1: %s' % datetime.now())

def myjob_2():
	print('myjob_2: %s' % datetime.now())

def my_listener(event):
    if event.exception:
        print('The job crashed :(')
    else:
        print('The job worked :)')

if __name__ == '__main__':
	scheduler = BackgroundScheduler()
	scheduler.add_job(myjob_1, 'interval', seconds=2)
	scheduler.add_job(myjob_2, 'interval', seconds=5)

	scheduler.add_listener(my_listener, EVENT_JOB_EXECUTED | EVENT_JOB_ERROR)

	scheduler.start()

	jobs = scheduler.get_jobs()
	print(jobs)

	try:
		while True:
			time.sleep(5)
	except (KeyboardInterrupt, SystemExit):
		scheduler.shutdown()
 