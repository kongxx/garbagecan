package my.mybatisstudy.host;

import my.mybatisstudy.host.model.Host;
import my.mybatisstudy.host.service.HostService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class Test {
	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("/spring.xml");
		HostService hostService = (HostService) ctx.getBean("hostService");

		Host host = new Host();
		host.setHostname("myhost");
		host.setIp("0.0.0.0");
		hostService.add(host);
		System.out.println(host);
		int id = host.getId();

		host = hostService.find(id);
		System.out.println(host);

		host.setHostname("test");
		host.setIp("");
		hostService.update(host);
		host = hostService.find(id);
		System.out.println(host);

		hostService.delete(host.getId());
		host = hostService.find(id);
		System.out.println(host);

		List<Host> hosts = hostService.list();
		System.out.println(hosts);
	}
}
