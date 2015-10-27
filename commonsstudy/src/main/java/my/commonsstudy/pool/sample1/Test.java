package my.commonsstudy.pool.sample1;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.StackObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {
	private static Logger logger = LoggerFactory.getLogger(Test.class);
	
	public static void main(String[] args) throws Exception {
		PoolableObjectFactory factory = new MyConnectionPoolableObjectFactory();
		ObjectPool pool = new StackObjectPool(factory);
		try {
			logger.info("================================================");
			for (int i = 0; i < 10; i++) {
				MyConnection myConn = (MyConnection)pool.borrowObject();
				try {
					myConn.print();
				} catch(Exception ex) {
					pool.invalidateObject(myConn);
					myConn = null;
				} finally {
					if (myConn != null) {
						pool.returnObject(myConn);
					}
				}
			}
			
			logger.info("================================================");
			for (int i = 0; i < 10; i++) {
				MyConnection myConn1 = (MyConnection)pool.borrowObject();
				MyConnection myConn2 = (MyConnection)pool.borrowObject();
				myConn1.print();
				myConn2.print();
				pool.returnObject(myConn1);
				pool.returnObject(myConn2);
			}
		} finally {
			try {
				pool.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
