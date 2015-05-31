package my.mybatisstudy.query;

import my.mybatisstudy.query.service.QueryService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("/spring.xml");
		QueryService service = (QueryService) ctx.getBean("queryService");

//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("license_name", null);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date startTime = sdf.parse("2015-05-28 15:00:00");
//		Date endTime = sdf.parse("2015-05-28 16:00:00");
//		params.put("start_time", startTime);
//		params.put("end_time", endTime);
//		List<Map<String, Object>> records = service.queryChartData(params);
//		for (Map<String, Object> record : records) {
//			System.out.println(record);
//		}

//		List<Map<String, Object>> licenseNames = service.listLicenseName();
//		System.out.println(licenseNames);
//
//		List<Map<String, Object>> values = null;
//		values = service.listValueByLicense(null, "feature_name");
//		System.out.println(values);
//		values = service.listValueByLicense("", "feature_name");
//		System.out.println(values);
//		values = service.listValueByLicense("MyLicense", "feature_name");
//		System.out.println(values);
//		values = service.listValueByLicense("MyLicense1", "feature_name");
//		System.out.println(values);
//
//		values = service.listValueByLicense("MyLicense", "license_vendor");
//		System.out.println(values);
//		values = service.listValueByLicense("MyLicense", "user_name");
//		System.out.println(values);
//		values = service.listValueByLicense("MyLicense", "host_name");
//		System.out.println(values);

		for (int i = 0; i < 10; i++) {
			System.out.println(service.list().size());
			Thread.sleep(2000);
			if (i % 2 == 0) {
				service.IDU();
			}
		}
	}
}
