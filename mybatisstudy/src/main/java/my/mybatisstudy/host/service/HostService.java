package my.mybatisstudy.host.service;

import my.mybatisstudy.host.mapper.HostMapper;
import my.mybatisstudy.host.model.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("hostService")
@Scope("prototype")
public class HostService {
	@Autowired
	private HostMapper hostMapper;

	public List<Host> list() {
		return hostMapper.list();
	}

	public Host find(int id) {
		return hostMapper.find(id);
	}

	public Host add(Host host) {
		hostMapper.add(host);
		return host;
	}

	public void update(Host host) {
		hostMapper.update(host);
	}

	public void delete(int id) {
		hostMapper.delete(id);
	}
}
