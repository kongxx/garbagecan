# Zookeeper实战之分组

最近整理资料的时候发现了两年前自己写的一些Zookeeper的例子，今天整理了一下，放到这里，也许以后用的着。

首先准备一个Zookeeper集群环境，这里使用单机模拟集群环境，并使用代码方式启动服务。

## Zookeeper服务

这里假定启动三个Zookeeper服务做集群

``` java
package my.zookeeperstudy;

import org.apache.commons.io.FileUtils;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.apache.zookeeper.server.quorum.QuorumPeerMain;

import java.io.File;
import java.net.InetAddress;
import java.util.Properties;

public class ZKServer {
	protected String id = null;
	protected String dataDir = null;
	protected String clientPort = null;

	public ZKServer(String id, String dataDir, String clientPort) {
		this.id = id;
		this.dataDir = dataDir;
		this.clientPort = clientPort;
	}

	public void startServer() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Properties props = new Properties();
					props.setProperty("tickTime", "2000");
					props.setProperty("dataDir", dataDir);
					FileUtils.write(new File(props.getProperty("dataDir"), "myid"), id);
					props.setProperty("clientPort", clientPort);
					props.setProperty("initLimit", "10");
					props.setProperty("syncLimit", "5");
					String hostname = InetAddress.getLocalHost().getHostName();
					props.setProperty("server.1", hostname + ":2881:3881");
					props.setProperty("server.2", hostname + ":2882:3882");
					props.setProperty("server.3", hostname + ":2883:3883");

					QuorumPeerConfig quorumConfig = new QuorumPeerConfig();
					quorumConfig.parseProperties(props);

					QuorumPeerMain quorumPeerMain = new QuorumPeerMain();
					quorumPeerMain.runFromConfig(quorumConfig);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}

``` 


``` java
package my.zookeeperstudy;

public class ZKServer1 {

	public static void main(String[] args) throws Exception {
		ZKServer zkServer = new ZKServer("1", "/tmp/zookeeper1/data", "2181");
		zkServer.startServer();
	}

}

``` 


``` java
package my.zookeeperstudy;

public class ZKServer2 {

	public static void main(String[] args) throws Exception {
		ZKServer zkServer = new ZKServer("2", "/tmp/zookeeper2/data", "2182");
		zkServer.startServer();
	}

}

``` 


``` java
package my.zookeeperstudy;

public class ZKServer3 {

	public static void main(String[] args) throws Exception {
		ZKServer zkServer = new ZKServer("3", "/tmp/zookeeper3/data", "2183");
		zkServer.startServer();
	}

}

``` 

## ZooKeeper组


``` java
package my.zookeeperstudy.group;

import org.apache.zookeeper.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ZooKeeperGroup {

	private final ZooKeeper zk;
	private Semaphore semaphore = new Semaphore(1);

	public ZooKeeperGroup(ZooKeeper zk) {
		this.zk = zk;
	}

	public void createGroup(String groupName) throws KeeperException, InterruptedException {
		String path = "/" + groupName;
		if (zk.exists(path, false) == null) {
			String createdPath = zk.create(path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			System.out.println("Created " + createdPath);
		}
	}

	public void deleteGroup(String groupName) throws KeeperException, InterruptedException {
		String path = "/" + groupName;
		if (zk.exists(path, false) != null) {
			List<String> children = zk.getChildren(path, false);
			for (String child : children) {
				System.out.println("Deleted " + path + "/" + child);
				zk.delete(path + "/" + child, -1);
			}
			zk.delete(path, -1);
			System.out.printf("Deleted group %s at path %s\n", groupName, path);
		}
	}

	public void joinGroup(String groupName, String memberName) throws KeeperException, InterruptedException {
		String path = "/" + groupName + "/" + memberName;
		String createdPath = zk.create(path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println("Created " + createdPath);
	}

	public void listGroup(String groupName) throws KeeperException, InterruptedException {
		String path = "/" + groupName;

		while (true) {
			semaphore.acquire();
			List<String> children = zk.getChildren(path, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					if (event.getType() == Event.EventType.NodeChildrenChanged) {
						semaphore.release();
					}
				}
			});

			Collections.sort(children);
			for (String child : children) {
				System.out.println(child);
			}
		}
	}
}

```

## 测试类

``` java
package my.zookeeperstudy.group;

import org.apache.zookeeper.*;

import java.io.IOException;

public class Test {
	private static final String host1 = "localhost:2181";
	private static final String host2 = "localhost:2182";
	private static final String host3 = "localhost:2183";

	public static void main(String[] args) throws Exception {
		final ZooKeeper zk = new ZooKeeper(host1, 10000, null);

		ZooKeeperGroup group = new ZooKeeperGroup(zk);
		group.createGroup("myGroups");

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ZooKeeper zk = new ZooKeeper(host2, 10000, null);
					ZooKeeperGroup group = new ZooKeeperGroup(zk);
					group.listGroup("myGroups");
				} catch (KeeperException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ZooKeeper zk = new ZooKeeper(host3, 10000, null);
					ZooKeeperGroup group = new ZooKeeperGroup(zk);
					for (int i = 0; i < 3; i++) {
						Thread.sleep(i * 1000);
						group.joinGroup("myGroups", "member_" + i);
					}
				} catch (KeeperException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();

		Thread.sleep(10 * 1000);
		group.deleteGroup("myGroups");
	}
}


```

## 测试

- 首先依次启动ZKServer1，ZKServer2和ZKServer3，启动过程会有错误，可以忽略，那是因为检测别的节点的时候连接失败导致，生产环境可以针对具体情况忽略此类错误。

- 启动Test类。

- 查看日志输出。
