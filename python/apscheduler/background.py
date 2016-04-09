import os, time
from datetime import datetime
from apscheduler.schedulers.background import BackgroundScheduler

def myjob():
	print(datetime.now())

if __name__ == '__main__':
	scheduler = BackgroundScheduler()
	scheduler.add_job(myjob, 'interval', seconds=2)
	scheduler.start()

	try:
		while True:
			time.sleep(1)
	except (KeyboardInterrupt, SystemExit):
		scheduler.shutdown()

