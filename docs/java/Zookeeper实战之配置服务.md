# Zookeeper实战之配置服务

最近整理资料的时候发现了两年前自己写的一些Zookeeper的例子，今天整理了一下，放到这里，也许以后用的着。

* Server 用来启动一个Zookeeper服务。

``` java
package my.zookeeperstudy.config;

import org.apache.zookeeper.server.ServerCnxnFactory;
import org.apache.zookeeper.server.ZooKeeperServer;

import java.io.File;
import java.net.InetSocketAddress;

public class Server {

	public static void main(String[] args) throws Exception {
		int tickTime = 2000;
		int maxClientCnxns = 60;
		File dir = new File(System.getProperty("java.io.tmpdir"), "zookeeper").getAbsoluteFile();
		ZooKeeperServer zkServer = new ZooKeeperServer(dir, dir, tickTime);
		ServerCnxnFactory standaloneServerFactory = ServerCnxnFactory.createFactory(new InetSocketAddress(2181), maxClientCnxns);
		standaloneServerFactory.startup(zkServer);
	}

}
```

* Config 配置类，包含要操作的配置项和方法等。
``` java
package my.zookeeperstudy.config;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Config implements Watcher {

	private ZooKeeper zk;

	private String basePath = "/config";

	public Config() {

	}

	public void connect(String hosts) throws IOException, KeeperException, InterruptedException {
		this.zk = new ZooKeeper(hosts, 1000, this);
		if (zk.exists(basePath, this) == null) {
			zk.create(basePath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}
	}

	public void set(String key, String value) throws InterruptedException, KeeperException {
		String path = basePath + "/" + key;
		if (zk.exists(path, this) == null) {
			zk.create(path, value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} else {
			zk.setData(path, value.getBytes(), -1);
		}
	}

	public String get(String key) throws InterruptedException, KeeperException {
		String path = basePath + "/" + key;
		if (zk.exists(path, this) != null) {
			byte[] data = zk.getData(path, this, null);
			return new String(data);
		}
		return null;
	}

	public void printConfig() throws InterruptedException, KeeperException {
		System.out.println("----- begin -----");
		List<String> children = zk.getChildren(basePath, false);
		Collections.sort(children);
		for (String child : children) {
			System.out.println(child + ": " + get(child));
		}
		System.out.println("----- end -----");
	}

	public void clear() throws KeeperException, InterruptedException {
		if (zk.exists(basePath, false) != null) {
			List<String> children = zk.getChildren(basePath, false);
			for (String child : children) {
				zk.delete(basePath + "/" + child, -1);
				System.out.println("Deleted " + basePath + "/" + child);
			}
			zk.delete(basePath, -1);
		}
	}

	@Override
	public void process(WatchedEvent event) {
		System.out.println("event: " + event);
		if (event.getType() == Event.EventType.NodeDataChanged ||
				event.getType() == Event.EventType.NodeChildrenChanged) {
			try {
				printConfig();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (KeeperException e) {
				e.printStackTrace();
			}
		}
	}
}

```

* ClientA和ClientB - 两个客户端，模拟配置修改和同步

ClientA.java
``` java
package my.zookeeperstudy.config;

public class ClientA {
	public static void main(String[] args) throws Exception {
		Config config = new Config();
		config.connect("localhost:2181");

		while (true) {
			Thread.sleep(1000);
			config.get("mykey");
		}
	}
}

```

ClientB.java
``` java
package my.zookeeperstudy.config;

public class ClientB {
	public static void main(String[] args) throws Exception {
		Config config = new Config();
		config.connect("localhost:2181");

		for (int i = 0; i < 10000; i++) {
			config.set("mykey", "myvalue_" + i);
			Thread.sleep(5 * 1000);
		}

		config.clear();
	}
}
```

* 测试
首先启动Server类，然后启动ClientA和ClientB，然后观察ClientA和ClientB的输出。

``` java
```

``` java
```

``` java
```

``` java
```

``` java
```

``` java
```
