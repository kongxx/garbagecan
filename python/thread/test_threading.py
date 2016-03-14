import time
import threading
from Queue import Queue


class MyThread(threading.Thread):
    def __init__(self, queue):
        self.queue = queue
        threading.Thread.__init__(self)

    def run(self):
        self.queue.put("", True)
        print "I am %s" % self.name
        time.sleep(1)
        self.queue.get()

if __name__ == "__main__":
    queue = Queue(1)
    for thread in range(0, 5):
        t = MyThread(queue)
        t.start()
    time.sleep(10000)