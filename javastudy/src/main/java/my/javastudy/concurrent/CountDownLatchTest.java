package my.javastudy.concurrent;

import org.apache.commons.lang.math.RandomUtils;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {

	public static void main(String[] args) throws Exception {
		test1(10);
		//test2(10);
	}

	private static void test1(int count) throws Exception {
		final CountDownLatch startGate = new CountDownLatch(count);
		final CountDownLatch endGate = new CountDownLatch(count);

		for (int i = 0; i < count; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					startGate.countDown();
					try {
						System.out.println("start");
						Thread.sleep(RandomUtils.nextInt(10) * 1000);
						System.out.println("stop");
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						endGate.countDown();
					}
				}
			}).start();
		}

		long start = System.currentTimeMillis();
		endGate.await();
		long stop = System.currentTimeMillis();
		System.out.print("Time: " + (stop - start));
	}

	private static void test2(int count) throws Exception {
		final CountDownLatch startGate = new CountDownLatch(1);
		final CountDownLatch endGate = new CountDownLatch(count);

		for (int i = 0; i < count; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						System.out.println("start");
						Thread.sleep(RandomUtils.nextInt(10) * 1000);
						System.out.println("stop");
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						endGate.countDown();
					}
				}
			}).start();
		}

		long start = System.currentTimeMillis();
		startGate.countDown();
		endGate.await();
		long stop = System.currentTimeMillis();
		System.out.print("Time: " + (stop - start));
	}
}
