package my.commonsstudy.pool.sample3;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {
	
	private static Logger logger = LoggerFactory.getLogger(Test.class);
	
	public static void main(String[] args) {
		//test1();
		//test2();
		//test3();
	}
	
	private static void test1() {
		PoolableObjectFactory factory = new MyConnectionPoolableObjectFactory();
		GenericObjectPool.Config config = new GenericObjectPool.Config();
		config.lifo = false;
		config.maxActive = 5;
		config.maxIdle = 5;
		config.minIdle = 1;
		config.maxWait = 5 * 1000;
		
		ObjectPool pool = new GenericObjectPool(factory, config);
		for (int i = 0; i < 10; i++) {
			Thread thread = new Thread(new MyTask(pool));
			thread.start();
		}
		closePool(pool);
	}
	
	private static void test2() {
		PoolableObjectFactory factory = new MyConnectionPoolableObjectFactory();
		GenericObjectPool.Config config = new GenericObjectPool.Config();
		config.lifo = false;
		config.maxActive = 5;
		config.maxIdle = 5;
		config.minIdle = 1;
		config.maxWait = 20 * 1000;

		ObjectPool pool = new GenericObjectPool(factory, config);
		for (int i = 0; i < 10; i++) {
			Thread thread = new Thread(new MyTask(pool));
			thread.start();
		}
		closePool(pool);
	}

	private static void test3() {
		PoolableObjectFactory factory = new MyConnectionPoolableObjectFactory();
		GenericObjectPool.Config config = new GenericObjectPool.Config();
		config.lifo = false;
		config.maxActive = 5;
		config.maxIdle = 0;
		config.minIdle = 0;
		config.maxWait = -1;

		ObjectPool pool = new GenericObjectPool(factory, config);
		Thread thread = new Thread(new MyTask(pool));
		thread.start();

		closePool(pool);
	}

	private static void closePool(ObjectPool pool) {
		try {
			Thread.sleep(60L * 1000L);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			pool.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static class MyTask implements Runnable {
		private ObjectPool pool;
		
		public MyTask(ObjectPool pool) {
			this.pool = pool;
		}
		
		public void run() {
			MyConnection myConn = null;
			try {
				myConn = (MyConnection)pool.borrowObject();
				try {
					myConn.print();
				} catch(Exception ex) {
					pool.invalidateObject(myConn);
					myConn = null;
				}
				Thread.sleep(10L * 1000L);
			} catch(Exception ex) {
				logger.error("Cannot borrow connection from pool.", ex);
			} finally {
				if (myConn != null) {
					try {
						pool.returnObject(myConn);
					} catch (Exception ex) {
						logger.error("Cannot return connection from pool.", ex);
					}
				}
			}
		}
	}
}
