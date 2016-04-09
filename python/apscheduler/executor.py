import os, time
from datetime import datetime
from apscheduler.schedulers.background import BackgroundScheduler

def myjob_1():
	print('myjob_1: %s' % datetime.now())

def myjob_2():
	print('myjob_2: %s' % datetime.now())

if __name__ == '__main__':
	scheduler = BackgroundScheduler()
	scheduler.add_executor('debug')
	scheduler.add_executor('threadpool')
	# scheduler.add_executor('processpool')
	scheduler.add_job(myjob_1, 'interval', seconds=2)
	scheduler.add_job(myjob_2, 'interval', seconds=5)
	scheduler.start()

	jobs = scheduler.get_jobs()
	print(jobs)

	try:
		while True:
			time.sleep(5)
	except (KeyboardInterrupt, SystemExit):
		scheduler.shutdown()
 
