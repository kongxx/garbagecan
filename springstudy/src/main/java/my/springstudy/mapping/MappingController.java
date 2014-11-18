package my.springstudy.mapping;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MappingController {

	@RequestMapping("/simple")
	public @ResponseBody
	String helloWorld() {
		String message = "Hello, this is a simple example";
		System.out.println(message);
		return message;
	}

	@RequestMapping("/mapping/path")
	public @ResponseBody String byPath() {
		return "Mapped by path!";
	}

	@RequestMapping(value="/mapping/path/*", method= RequestMethod.GET)
	public @ResponseBody String byPathPattern(HttpServletRequest request) {
		return "Mapped by path pattern ('" + request.getRequestURI() + "')";
	}

	@RequestMapping(value="/mapping/method", method=RequestMethod.GET)
	public @ResponseBody String byMethod() {
		return "Mapped by path + method";
	}

	@RequestMapping(value="/mapping/parameter", method=RequestMethod.GET, params="foo")
	public @ResponseBody String byParameter() {
		return "Mapped by path + method + presence of query parameter!";
	}

	@RequestMapping(value="/mapping/parameter", method=RequestMethod.GET, params="!foo")
	public @ResponseBody String byParameterNegation() {
		return "Mapped by path + method + not presence of query parameter!";
	}

	@RequestMapping(value="/mapping/produces", method=RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody JavaBean byProducesJson() {
		return new JavaBean();
	}

	@RequestMapping(value="/mapping/produces", method=RequestMethod.GET, produces=MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody JavaBean byProducesXml() {
		return new JavaBean();
	}

}
