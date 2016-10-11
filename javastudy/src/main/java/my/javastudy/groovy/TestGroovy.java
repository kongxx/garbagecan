package my.javastudy.groovy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyObject;

public class TestGroovy {
	
	private static String file = TestGroovy.class.getResource("/my/javastudy/groovy/test.groovy").getFile();
	
	public static void main(String[] args) throws Exception {
		test1();
		test2();
		test3();
	}

	private static void test1() throws Exception {
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("groovy");
		Integer sum = (Integer) engine.eval("(1..10).sum()");
		System.out.println(sum);

		for (int i = 0; i < 100; i++) {
			GroovyClassLoader gcl = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
			Class<?> clazz = gcl.parseClass(new GroovyCodeSource(new File(file)));
			GroovyObject instance = (GroovyObject) clazz.newInstance();
			System.out.println(instance.invokeMethod("sayHello", "kongxx"));
			gcl.close();
			Thread.sleep(1000);
		}
	}

	private static void test2() throws Exception {
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("groovy");
		for (int i = 0; i < 1000000; i++) { 
			BufferedReader reader = new BufferedReader(new FileReader(file));
			Class<?> clazz = (Class<?>)engine.eval(reader);
			Invocable inv = (Invocable) engine;
			Object result = inv.invokeMethod(clazz.newInstance(), "sayHello", new Object[]{"kongxx"});
			System.out.println(result);
			Thread.sleep(1000);
		}
	}

	private static void test3() throws Exception {
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("groovy");

		String fact = "def factorial(n) { n == 1 ? 1 : n * factorial(n - 1) }";
		engine.eval(fact);
		Invocable inv = (Invocable) engine;
		Object[] params = {5};
		Object result = inv.invokeFunction("factorial", params);
		System.out.println(result);
	}
}
