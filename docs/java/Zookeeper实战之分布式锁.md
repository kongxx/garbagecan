# Zookeeper实战之分布式锁

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

## 分布式锁类DistributedLocker

``` java
package my.zookeeperstudy.lock;

import org.apache.zookeeper.*;

import java.util.Collections;
import java.util.List;

public class DistributedLocker {
	private final ZooKeeper zk;
	private final String lockBasePath;
	private final String lockName;
	private String lockPath;

	public DistributedLocker(ZooKeeper zk, String lockBasePath, String lockName) {
		this.zk = zk;
		this.lockBasePath = lockBasePath;
		this.lockName = lockName;
	}

	public void getLock() throws KeeperException, InterruptedException {
		if (zk.exists(lockBasePath, true) == null) {
			zk.create(lockBasePath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}
		lockPath = zk.create(lockBasePath + "/" + lockName, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		System.out.println("getLock path: " + lockPath);
		final Object lock = new Object();
		synchronized (lock) {
			while (true) {
				List<String> nodes = zk.getChildren(lockBasePath, new Watcher() {
					@Override
					public void process(WatchedEvent event) {
						synchronized (lock) {
							lock.notifyAll();
						}
					}
				});
				Collections.sort(nodes);
				System.out.println(nodes);
				if (lockPath.endsWith(nodes.get(0))) {
					return;
				} else {
					lock.wait();
				}
			}
		}
	}

	public void releaseLock() throws KeeperException, InterruptedException {
		zk.delete(lockPath, -1);
		lockPath = null;
	}
}
```

## 测试客户端类

这里仍然模拟有三个客户端

``` java
package my.zookeeperstudy.lock;

import org.apache.zookeeper.*;

public class ZKClient {
	private final String lockBasePath = "/mylocks";
	private final String lockName = "mylock";

	public void start(String url) throws Exception {
		final ZooKeeper zk = new ZooKeeper(url, 10000, new Watcher() {
			public void process(WatchedEvent event) {
				System.out.println("event: " + event.getType());
			}
		});

		try {
			DistributedLocker locker = new DistributedLocker(zk, lockBasePath, lockName);

			System.out.println("before get lock");
			locker.getLock();
			System.out.println("after get lock");

			System.out.println("do something");
			Thread.sleep(60 * 1000);

			System.out.println("before release lock");
			locker.releaseLock();
			System.out.println("after release lock");
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
```

``` java
package my.zookeeperstudy.lock;

public class ZKClient1 {
	public static void main(String[] args) throws Exception {
		ZKClient zkClient = new ZKClient();
		zkClient.start("localhost:2181");
	}
}
```

``` java
package my.zookeeperstudy.lock;

public class ZKClient2 {
	public static void main(String[] args) throws Exception {
		ZKClient zkClient = new ZKClient();
		zkClient.start("localhost:2182");
	}
}
```

``` java
package my.zookeeperstudy.lock;

public class ZKClient3 {
	public static void main(String[] args) throws Exception {
		ZKClient zkClient = new ZKClient();
		zkClient.start("localhost:2183");
	}
}
```

## 测试

- 首先依次启动ZKServer1，ZKServer2和ZKServer3，启动过程会有错误，可以忽略，那是因为检测别的节点的时候连接失败导致，生产环境可以针对具体情况忽略此类错误。

- 然后依次启动ZKClient1，ZKClient2和ZKClient3。

- 分别观察三个Client日志输出。

- 停止一个Client比如ZKClient1然后观察日志输出。

