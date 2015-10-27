package my.commonsstudy.pool.sample4;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {
	
	private static Logger logger = LoggerFactory.getLogger(Test.class);

	public static void main(String[] args) {
		PoolableObjectFactory factory = new MyConnectionPoolableObjectFactory();
		GenericObjectPool.Config config = new GenericObjectPool.Config();
		config.lifo = false;
		config.maxActive = 10;
		config.maxIdle = 10;
		config.minIdle = 0;
		config.maxWait = -1;
		config.testOnBorrow = true;
		config.testOnReturn = true;
		config.timeBetweenEvictionRunsMillis = 10 * 1000;
		config.minEvictableIdleTimeMillis = 10 * 1000;
		ObjectPool pool = new GenericObjectPool(factory, config);

		for (int i = 0; i < 5; i++) {
			doIt(pool);
			sleep(15 * 1000L);
		}
		
		try {
			pool.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void doIt(ObjectPool pool) {
		MyConnection myConn = null;

		try {
			myConn = (MyConnection)pool.borrowObject();
			try {
				myConn.print();
			} catch(Exception ex) {
				pool.invalidateObject(myConn);
				myConn = null;
			}
		} catch(Exception ex) {
			logger.error("Cannot borrow connection from pool.", ex);
		} finally {
			if (myConn != null) {
				// will invoke the mock close method.
				myConn.close();

				try {
					pool.returnObject(myConn);
				} catch (Exception ex) {
					logger.error("Cannot return connection from pool.", ex);
				}
			}
		}
	}

	private static void sleep(long ms) {
		try {
			Thread.sleep(20 * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
