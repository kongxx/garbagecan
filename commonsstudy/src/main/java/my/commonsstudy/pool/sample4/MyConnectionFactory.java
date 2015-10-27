package my.commonsstudy.pool.sample4;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyConnectionFactory {

	private static Logger logger = LoggerFactory.getLogger(MyConnectionFactory.class);

	private static MyConnectionFactory factory = new MyConnectionFactory();

	private MyConnectionFactory() {

	}

	public static MyConnectionFactory getInstance() {
		return factory;
	}

	public MyConnection getMyConnection(String name) throws Exception {
		MyConnectionEnhancer enhancer = new MyConnectionEnhancer();
		return enhancer.create(name);
	}

	class MyConnectionEnhancer implements MethodInterceptor {

		public MyConnection create(String name) {
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(MyConnection.class);
			enhancer.setCallback(this);
			return (MyConnection)enhancer.create(new Class[]{String.class}, new Object[]{name});
		}

		public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
			if (method.getName().equals("close")) {
				StackTraceElement[] stes = (new Throwable()).getStackTrace();
				for (StackTraceElement ste : stes) {
					if (ste.getClassName().endsWith("MyConnectionPoolableObjectFactory")
						&& ste.getMethodName().equals("destroyObject")) {
						logger.info("Real close() method will be called.");
						return proxy.invokeSuper(obj, args);
					}
				}
				logger.info("Mock close() method will be called.");
				return null;
			}
			return proxy.invokeSuper(obj, args);
		}
	}
}
