package my.javastudy.concurrent;

import org.apache.commons.lang.math.RandomUtils;

import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest {

	public static void main(String[] args) throws Exception {
		int count = 10;

		final CyclicBarrier startBarrier = new CyclicBarrier(count, new Runnable() {
			@Override
			public void run() {
				System.out.println("========== Started ==========");
			}
		});

		final CyclicBarrier stopBarrier = new CyclicBarrier(count, new Runnable() {
			@Override
			public void run() {
				System.out.println("========== Stopped ==========");
			}
		});

		for (int i = 0; i < count; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						startBarrier.await();
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						System.out.println("start");
						Thread.sleep(RandomUtils.nextInt(10) * 1000);
						System.out.println("stop");
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							stopBarrier.await();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
		}
	}

}
