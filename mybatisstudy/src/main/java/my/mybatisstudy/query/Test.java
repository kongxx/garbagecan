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

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("license_name", "");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startTime = sdf.parse("2015-05-28 15:00:00");
		Date endTime = sdf.parse("2015-05-28 16:00:00");
		params.put("start_time", startTime);
		params.put("end_time", endTime);
		List<Map<String, Object>> records = service.queryChartData(params);
		for (Map<String, Object> record : records) {
			System.out.println(record);
		}
	}
}
