import os, time
from datetime import datetime

from apscheduler.schedulers.blocking import BlockingScheduler

def myjob_1():
	print('myjob_1: %s' % datetime.now())

def myjob_2():
	print('myjob_2: %s' % datetime.now())

if __name__ == '__main__':
	scheduler = BlockingScheduler()
	scheduler.add_job(myjob_1, 'interval', seconds=2)
	scheduler.add_job(myjob_2, 'interval', seconds=5)

	try:
		scheduler.start()
	except (KeyboardInterrupt, SystemExit):
		pass

