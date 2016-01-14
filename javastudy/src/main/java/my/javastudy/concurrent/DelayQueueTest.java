package my.javastudy.concurrent;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayQueueTest {

	public static void main(String[] args) throws Exception {
		final DelayQueue<MyConnection> queue = new DelayQueue<MyConnection>();
		final MyConnection myConn1 = new MyConnection("conn1");
		final MyConnection myConn2 = new MyConnection("conn2");
		final MyConnection myConn3 = new MyConnection("conn3");
		queue.add(myConn1);
		queue.add(myConn2);
		queue.add(myConn3);

		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					myConn1.query();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 5; i++) {
					myConn2.query();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("Queue Size: " + queue.size());
					while (true) {
						MyConnection myConn = queue.take();
						myConn.close();
						System.out.println("Queue Size: " + queue.size());
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	static class MyConnection implements Delayed {
		private long longMaxIdleTime = 5 * 1000;
		private long lastTime = System.currentTimeMillis();
		private String name;

		public MyConnection(String name) {
			this.name = name;
		}

		public void query() {
			System.out.println("Run query with connection <" + name + ">.");
			updateLastTime();
		}

		public void close() {
			System.err.println("Close connection <" + name + ">.");
		}

		private synchronized void updateLastTime() {
			this.lastTime = System.currentTimeMillis();
		}

		@Override
		public long getDelay(TimeUnit unit) {
			long diff = longMaxIdleTime - (System.currentTimeMillis() - this.lastTime);
			return unit.convert(diff, TimeUnit.MILLISECONDS);
		}

		@Override
		public int compareTo(Delayed obj) {
			if(obj instanceof MyConnection) {
				MyConnection myConn = (MyConnection)obj;
				if (this.getDelay(TimeUnit.MILLISECONDS) - myConn.getDelay(TimeUnit.MILLISECONDS) > 0) {
					return 1;
				} else {
					return -1;
				}
			}
			return 0;
		}
	}
}
