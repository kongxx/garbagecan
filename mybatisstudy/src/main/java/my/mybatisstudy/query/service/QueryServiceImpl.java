package my.mybatisstudy.query.service;

import my.mybatisstudy.query.mapper.QueryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("queryService")
@Scope("prototype")
public class QueryServiceImpl implements QueryService {
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

	@Cacheable(value="myCache", key="'list'")
	public List<String> list() {
		System.out.println("execute list method");
		List<String> list = new ArrayList<String>();
		return list;
	}

	@CacheEvict(value = "myCache", key="'list'")
	public boolean IDU() {
		return true;
	}
}
