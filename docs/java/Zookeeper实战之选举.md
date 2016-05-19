# Zookeeper实战之选举

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

## 测试客户端类

这里有三个客户端来模拟选举。

``` java
package my.zookeeperstudy.election;

import org.apache.zookeeper.*;

public class ZKClient {
	protected String id = null;
	protected String clientPort = null;
	protected String path = "/myapp_leader";

	public ZKClient(String id, String clientPort) {
		this.id = id;
		this.clientPort = clientPort;
	}

	protected void startClient() throws Exception {
		ZooKeeper zk = new ZooKeeper("localhost:" + clientPort, 10000,
				new Watcher() {
					public void process(WatchedEvent event) {
						System.out.println("event: " + event.getType());
					}
				});

		while (true) {
			byte[] leader = null;
			try {
				leader = zk.getData(path, true, null);
			} catch(Exception e) {
				System.out.println("The leader is null.");
			}
			if (leader == null) {
				try {
					zk.create(path, this.id.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
				} catch(Exception e) {
					// ignore me
				}
			} else {
				System.out.println("The leader is: " + new String(leader));
			}
			Thread.sleep(1 * 1000);
		}
	}
}
``` 

``` java
package my.zookeeperstudy.election;

public class ZKClient1 {

	public static void main(String[] args) throws Exception {
		ZKClient zkClient = new ZKClient("1", "2181");
		zkClient.startClient();
	}

}
``` 

``` java
package my.zookeeperstudy.election;

public class ZKClient2 {

	public static void main(String[] args) throws Exception {
		ZKClient zkClient = new ZKClient("2", "2182");
		zkClient.startClient();
	}

}
``` 

``` java
package my.zookeeperstudy.election;

public class ZKClient3 {

	public static void main(String[] args) throws Exception {
		ZKClient zkClient = new ZKClient("3", "2183");
		zkClient.startClient();
	}

}
``` 

## 测试

- 首先依次启动ZKServer1，ZKServer2和ZKServer3，启动过程会有错误，可以忽略，那是因为检测别的节点的时候连接失败导致，生产环境可以针对具体情况忽略此类错误。

- 然后依次启动ZKClient1，ZKClient2和ZKClient3。

- 分别观察三个Client日志输出。

- 停止当前选举到的leader，比如ZKClient1，然后观察日志输出，一段时间后会重新选举出一个leader。
