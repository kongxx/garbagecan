package my.redisstudy;

public class ExecutorUtils {
	public static void execute(Executor executor) {
		long start = System.currentTimeMillis();
		
		executor.execute();
		
		long end = System.currentTimeMillis();
		System.out.printf("Time: %d \n", end - start);
	}
	
	public static void execute(String subject, Executor executor) {
		System.out.printf("Execute %s ...\n", subject);
		long start = System.currentTimeMillis();
		
		executor.execute();
		
		long end = System.currentTimeMillis();
		System.out.printf("Time: %d \n", end - start);
	}
}
