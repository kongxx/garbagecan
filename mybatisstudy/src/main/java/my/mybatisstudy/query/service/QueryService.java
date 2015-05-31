package my.mybatisstudy.query.service;

import java.util.List;
import java.util.Map;

public interface QueryService {

	List<Map<String, Object>> listLicenseName();

	List<Map<String, Object>> listValueByLicense(String licenseName, String column);

	List<Map<String, Object>> queryChartData(Map<String, Object> params);

	List<String> list();

	boolean IDU();
}
