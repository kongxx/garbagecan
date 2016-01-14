package my.javastudy.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreTest {
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		final Semaphore semaphore = new Semaphore(10);
		for (int idx = 0; idx < 50; idx++) {
			final int thread = idx;
			Runnable run = new Runnable() {
				public void run() {
					try {
						semaphore.acquire();
						System.out.println(thread);
						Thread.sleep((long) (Math.random() * 5 * 1000));
						semaphore.release();
						System.out.println("semaphore available permits: " + semaphore.availablePermits());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
			exec.execute(run);
		}
		exec.shutdown();
	}
}
