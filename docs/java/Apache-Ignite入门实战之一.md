Apache-Ignite入门实战之一

## 简介
Apache Ignite 内存数据组织框架是一个高性能、集成化和分布式的内存计算和事务平台，用于大规模的数据集处理，比传统的基于磁盘或闪存的技术具有更高的性能，同时他还为应用和不同的数据源之间提供高性能、分布式内存中数据组织管理的功能。

## 安装

从 https://ignite.apache.org/download.cgi#binaries 下载最新的安装包，这里我下载的是 apache-ignite-fabric-2.3.0-bin.zip 包。下载后解压就可以直接使用了。

## 运行

进入到 ${IGNITE_HOME}/bin 目录，然后运行

``` shell
./ignite.sh
...
[00:24:04] To start Console Management & Monitoring run ignitevisorcmd.{sh|bat}
[00:24:04]
[00:24:04] Ignite node started OK (id=01af1a02)
[00:24:04] Topology snapshot [ver=1, servers=1, clients=0, CPUs=2, heap=1.0GB]
...
```
其中：
- servers=1 表示当前 Ignite 集群中只有一个节点。
- clients=0 表示当前没有客户端连接到此集群。

此时，我们可以在另外一台机器上运行同样的命令来再启动一个 Ignite，此时我们就可以看到

``` shell
...
[00:41:21] Topology snapshot [ver=2, servers=2, clients=0, CPUs=2, ...
...
```

可以看到 servers=2，说明有一个新节点加入了集群。

## 测试

Ignite 集群已经有了，下面我们来看看怎样使用 Ignite 作为分布式缓存系统使用。

首先建立一个 Maven 工程，pom.xml 文件内容如下:

``` xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>my.ignitestudy</groupId>
	<artifactId>ignitestudy</artifactId>
	<packaging>jar</packaging>
	<version>1.0-SNAPSHOT</version>
	<name>ignitestudy</name>
	<url>http://maven.apache.org</url>
	<properties>
		<ignite.version>2.3.0</ignite.version>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.apache.commons</groupId>
			<artifactId>commons-lang3</artifactId>
			<version>3.1</version>
		</dependency>

		<dependency>
			<groupId>net.sf.json-lib</groupId>
			<artifactId>json-lib</artifactId>
			<version>2.4</version>
			<classifier>jdk15</classifier>
		</dependency>

		<dependency>
			<groupId>org.apache.ignite</groupId>
			<artifactId>ignite-core</artifactId>
			<version>${ignite.version}</version>
		</dependency>

		<dependency>
			<groupId>org.apache.ignite</groupId>
			<artifactId>ignite-spring</artifactId>
			<version>${ignite.version}</version>
		</dependency>

		<dependency>
			<groupId>org.apache.ignite</groupId>
			<artifactId>ignite-indexing</artifactId>
			<version>${ignite.version}</version>
		</dependency>

		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>3.8.1</version>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>2.3.2</version>
				<configuration>
					<source>1.8</source>
					<target>1.8</target>
					<encoding>UTF-8</encoding>
					<compilerArguments>
						<extdirs>${project.basedir}/lib</extdirs>
					</compilerArguments>
				</configuration>
			</plugin>

			<plugin>
				<artifactId>maven-assembly-plugin</artifactId>
				<configuration>
					<descriptorRefs>
						<descriptorRef>jar-with-dependencies</descriptorRef>
					</descriptorRefs>
				</configuration>
				<executions>
					<execution>
						<id>make-assembly</id>
						<phase>package</phase>
						<goals>
							<goal>single</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>
</project>
```

下面是 Java 中怎样使用 Cache 的例子

``` java
package my.ignitestudy.datagrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.apache.ignite.transactions.Transaction;

import java.util.Arrays;

public class Test {
	public static void main( String[] args ) throws Exception {
		Ignite ignite = getIgnite();
		testGetPut(ignite);
		testAtomOperation(ignite);
	}

	private static Ignite getIgnite() {
		TcpDiscoverySpi spi = new TcpDiscoverySpi();
		TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
		ipFinder.setAddresses(Arrays.asList("192.168.0.192:47500..47509"));
		spi.setIpFinder(ipFinder);
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setDiscoverySpi(spi);
		cfg.setClientMode(true);

		Ignite ignite = Ignition.start(cfg);
		return ignite;
	}

	private static void testGetPut(Ignite ignite) {
		IgniteCache<String, String> cache = ignite.getOrCreateCache("myCache");

		for (int i = 0; i < 10; i++) {
			cache.put("mykey_" + i, "myvalue_" + i);
		}

		for (int i = 0; i < 10; i++) {
			String key = "mykey_" + i;
			System.out.println("Got [key=" + key + ", val=" + cache.get(key) + ']');
		}
	}

	private static void testAtomOperation(Ignite ignite) {
		IgniteCache<String, Integer> cache = ignite.getOrCreateCache("myCache");

		Integer oldValue = cache.getAndPutIfAbsent("MyKey", 11);
		System.out.println("MyKey: " + oldValue);

		boolean success = cache.putIfAbsent("MyKey", 22);
		System.out.println("MyKey: " + success);

		oldValue = cache.getAndReplace("MyKey", 11);
		System.out.println("MyKey replace: " + oldValue);

		success = cache.replace("MyKey", 22);
		System.out.println("MyKey replace: " + success);

		success = cache.replace("MyKey", 2, 22);
		System.out.println("MyKey replace: " + success);

		success = cache.remove("MyKey", 1);
		System.out.println("MyKey remove: " + success);
	}
}
```

上面 Java 代码中我们是使用编程的方式来连接集群，如下：

``` java
TcpDiscoverySpi spi = new TcpDiscoverySpi();
TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
ipFinder.setAddresses(Arrays.asList("192.168.0.192:47500..47509"));
spi.setIpFinder(ipFinder);
IgniteConfiguration cfg = new IgniteConfiguration();
cfg.setDiscoverySpi(spi);
cfg.setClientMode(true);

Ignite ignite = Ignition.start(cfg);
```

也可以使用指定的配置文件来获取到集群的连接，比如：

``` java
Ignite ignite = Ignition.start("... config file path ...");
```
