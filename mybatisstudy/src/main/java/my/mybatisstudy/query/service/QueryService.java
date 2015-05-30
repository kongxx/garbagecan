package my.mybatisstudy.query.service;

import my.mybatisstudy.query.mapper.QueryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("queryService")
@Scope("prototype")
public class QueryService {
	@Autowired
	private QueryMapper queryMapper;

	public List<Map<String, Object>> listLicenseName() {
		return queryMapper.listLicenseName();
	}

	public List<Map<String, Object>> listValueByLicense(String licenseName, String column) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("license_name", licenseName);
		params.put("column", column);
		return queryMapper.listValueByLicense(params);
	}

	public List<Map<String, Object>> queryChartData(Map<String, Object> params) {
		return queryMapper.queryChartData(params);
	}
}
