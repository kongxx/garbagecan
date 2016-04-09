import os, time
from datetime import datetime
from apscheduler.schedulers.background import BackgroundScheduler

def myjob():
	print(datetime.now())

if __name__ == '__main__':
	scheduler = BackgroundScheduler()
	scheduler.start()
	job = scheduler.add_job(myjob, 'interval', seconds=1, id='myjob')
	print(job)

	jobs = scheduler.get_jobs()
	print(jobs)

	try:
		time.sleep(5)		
		print('pause job')
		scheduler.pause_job('myjob')
		time.sleep(5)
		print('resume job')
		scheduler.resume_job('myjob')

		print('reschedule job ...')
		scheduler.reschedule_job('myjob', trigger='cron', second='*/5')
		time.sleep(10)
	except (KeyboardInterrupt, SystemExit):
		scheduler.shutdown()
