import os, time, threading
from datetime import datetime
from apscheduler.schedulers.background import BackgroundScheduler

class MyJob(object):

	mutex = threading.Lock()

	def __init__(self):
		pass

	def start_vms(self):
		if self.mutex.acquire():
			print('start at %s' % datetime.now())
			time.sleep(5)
			self.mutex.release()

	def stop_vms(self):
		if self.mutex.acquire():
			print('stop at %s' % datetime.now())
			time.sleep(5)
			self.mutex.release()

if __name__ == '__main__':
	scheduler = BackgroundScheduler()
	myjob = MyJob()

	scheduler.add_job(myjob.start_vms, 'interval', seconds=1, coalesce=True, max_instances=1, misfire_grace_time=1)
	scheduler.add_job(myjob.stop_vms, 'interval', seconds=1, coalesce=True, max_instances=1, misfire_grace_time=1)

	scheduler.start()

	try:
		while True:
			time.sleep(5)
	except (KeyboardInterrupt, SystemExit):
		scheduler.shutdown()

