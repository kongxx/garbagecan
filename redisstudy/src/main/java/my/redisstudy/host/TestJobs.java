package my.redisstudy.host;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.SerializationUtils;

import my.redisstudy.Executor;
import my.redisstudy.ExecutorUtils;

public class TestJobs {
	private static int jobNum = 1 * 10000;
	private static int jobAttrNum = 100;
	
	public static void main(String[] args) throws Exception {
		if (args.length == 2) {
			jobNum = Integer.parseInt(args[0]);
			jobAttrNum = Integer.parseInt(args[1]);
		}
		TestJobs t = new TestJobs();
		List<Map<String, String>> jobs = t.prepareData();
		t.testCopy(jobs);
		t.testClone(jobs);
	}
	
	private List<Map<String, String>> prepareData() {
		System.out.println("Prepare data ...");
		final List<Map<String, String>> jobs = new ArrayList<Map<String, String>>();
		ExecutorUtils.execute(new Executor() {
			public void execute() {
				for (int i = 0; i < jobNum; i++) {
					Map<String, String> job = new HashMap<String, String>();
					for (int j = 0; j < jobAttrNum; j++) {
						job.put("key_" + j, "value_" + System.currentTimeMillis());
					}
					jobs.add(job);
				}
			}
		});
		return jobs;
	}
	
	private void testCopy(final List<Map<String, String>> jobs) throws Exception {
		System.out.println("Run copy test ...");
		for (int i = 0; i < 5; i++) {
			ExecutorUtils.execute(new Executor() {
				public void execute() {
					List<Map<String, String>> newJobs = new ArrayList<Map<String, String>>();
					for (Map<String, String> job : jobs) {
						Map<String, String> newJob = new HashMap<String, String>();
						for (Entry<String, String> entry : job.entrySet()) {
							newJob.put(entry.getKey(), entry.getValue());
						}
						newJobs.add(newJob);
					}
					System.out.println("size: " + newJobs.size());
					newJobs = null;
				}
			});
		}
	}
	
	private void testClone(final List<Map<String, String>> jobs) throws Exception {
		System.out.println("Run clone test ...");
		for (int i = 0; i < 5; i++) {
			ExecutorUtils.execute(new Executor() {
				public void execute() {
					MyObject myObject = new MyObject(jobs);
					List<Map<String, String>> newJobs = SerializationUtils.clone(myObject).getJobs();
					System.out.println("size: " + newJobs.size());
					newJobs = null;
				}
			});
		}
	}
}

class MyObject implements Serializable {
	private List<Map<String, String>> jobs = null;
	public MyObject(List<Map<String, String>> jobs) {
		this.jobs = jobs;
	}
	public List<Map<String, String>> getJobs() {
		return jobs;
	}
	public void setJobs(List<Map<String, String>> jobs) {
		this.jobs = jobs;
	}

}