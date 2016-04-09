import os, time
from datetime import datetime
from apscheduler.schedulers.background import BackgroundScheduler

def myjob():
	print('myjob: %s' % datetime.now())
	time.sleep(5)

if __name__ == '__main__':
	scheduler = BackgroundScheduler()
	scheduler.add_job(myjob, 'interval', seconds=1, coalesce=True, max_instances=1, misfire_grace_time=1)
	scheduler.start()

	try:
		while True:
			time.sleep(5)
	except (KeyboardInterrupt, SystemExit):
		scheduler.shutdown()
 
