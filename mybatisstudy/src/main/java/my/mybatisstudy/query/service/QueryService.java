package my.mybatisstudy.query.service;

import my.mybatisstudy.query.mapper.QueryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("queryService")
@Scope("prototype")
public class QueryService {
	@Autowired
	private QueryMapper queryMapper;

	public List<Map<String, Object>> queryChartData(Map<String, Object> params) {
		return queryMapper.queryChartData(params);
	}
}
