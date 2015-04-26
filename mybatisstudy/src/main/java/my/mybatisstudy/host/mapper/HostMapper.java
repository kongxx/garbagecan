package my.mybatisstudy.host.mapper;

import my.mybatisstudy.host.model.Host;

import java.util.List;

public interface HostMapper {

	List<Host> list();

	Host find(int id);

	void add(Host host);

	void update(Host host);

	void delete(int id);
}
