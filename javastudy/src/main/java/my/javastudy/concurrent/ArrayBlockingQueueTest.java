package my.javastudy.concurrent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ArrayBlockingQueue;

public class ArrayBlockingQueueTest {
	public static void main(String[] args) throws Exception {
		final ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(10);
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						queue.put(""+System.currentTimeMillis());
						System.out.println("Put:  " + sdf.format(Calendar.getInstance().getTime()));
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
					}
				}
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					String s = queue.poll();
					if (s != null) {
						System.out.println("Poll: " + sdf.format(Calendar.getInstance().getTime()));
					}
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
					}
				}
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						System.out.println("Queue size: " + queue.size());
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
					}
				}
			}
		}).start();
	}

}
