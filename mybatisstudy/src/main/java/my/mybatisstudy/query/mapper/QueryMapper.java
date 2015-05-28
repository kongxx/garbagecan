package my.mybatisstudy.query.mapper;

import java.util.List;
import java.util.Map;

public interface QueryMapper {
	List<Map<String, Object>> queryChartData(Map<String, Object> params);
}
