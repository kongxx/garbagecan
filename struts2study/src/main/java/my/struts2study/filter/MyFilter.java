package my.struts2study.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class MyFilter implements Filter {

	private static Log log = LogFactory.getLog(MyFilter.class);

	public void init(FilterConfig filterConfig) throws ServletException {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			String requestPath = getRequestPath((HttpServletRequest)request);

			log.error("##################################################" + requestPath);
			Map<?, ?> params = request.getParameterMap();
			for (Object key : params.keySet()) {
				if (params.get(key) != null && (params.get(key) instanceof Object[] || params.get(key) instanceof String[])) {
					Object[] values = (Object[])params.get(key);
					StringBuffer sb = new StringBuffer();
					sb.append("[");
					for (Object value: values) {
						sb.append(value).append(",");
					}
					sb.append("]");
					log.error(key + ": " + sb.toString());

				} else {
					log.error(key + ": " + params.get(key));
				}
			}

//			BufferedReader br = request.getReader();
//			StringBuffer sb = new StringBuffer();
//			while(true) {
//				String line = br.readLine();
//				if (line == null) {
//					break;
//				}
//				sb.append(line).append("\n");
//			}
//			if (sb.toString().length() > 0) {
//				log.error("reader: \n" + sb.toString());
//			}

			chain.doFilter(request, response);
		} catch(Throwable ex) {
			ex.printStackTrace();
		}
	}

	public void destroy() {

	}

	private String getRequestPath(HttpServletRequest request){
		String uri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String requestPath = uri.substring(uri.indexOf(contextPath) + contextPath.length());
		return requestPath;
	}
}
