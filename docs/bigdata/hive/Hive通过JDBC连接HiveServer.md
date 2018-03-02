# Hive通过JDBC连接HiveServer

## 修改 hadoop 配置

首先需要修改hadoop的配置文件etc/hadoop/core-site.xml，添加如下配置项，其中 <user> 为连接用户，根据具体用户替换。

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
    。。。
    <property>
        <name>hadoop.proxyuser.<user>.hosts</name> 
        <value>*</value>
    </property>
    <property>
        <name>hadoop.proxyuser.<user>.groups</name>
        <value>*</value>
    </property>
</configuration>
```

修改配置后需要重启hadoop集群

``` shell
$ sbin/stop-dfs.sh
$ sbin/start-dfs.sh
```

## 启动 hiveserver2 服务

使用下面的命令启动，默认端口为10000.

``` shell
$ bin/hive --service hiveserver2
```

## JDBC测试

这里假定使用的是maven创建的Java工程，添加下面的依赖库

``` xml
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-common</artifactId>
            <version>3.0.0</version>
        </dependency>

        <dependency>
            <groupId>org.apache.hive</groupId>
            <artifactId>hive-jdbc</artifactId>
            <version>2.3.2</version>
        </dependency>
```

下面是一个测试代码，可根据自己情况修改其中的连接信息，比如url，username和passworkd。

``` java
package my.hivestudy;

import java.sql.*;

public class JDBCExample {
    public static void main(String[] args) throws Exception {
        Class.forName("org.apache.hive.jdbc.HiveDriver");
        Connection conn = DriverManager.getConnection("jdbc:hive2://localhost:10000/default", "admin", "admin");

        createTable(conn);
        insertTable(conn);
        queryTable(conn);

        conn.close();
    }

    private static void createTable(Connection conn) throws Exception {
        PreparedStatement stmt = null;

        stmt = conn.prepareStatement("drop table if exists users");
        stmt.execute();

        stmt = conn.prepareStatement("CREATE TABLE users(id int, username string, password string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ','");
        stmt.execute();

        ResultSet rs = stmt.executeQuery("show tables");
        if (rs.next()) {
            System.out.println("Table:" + rs.getString(1));
        }

        stmt.close();
    }

    private static void insertTable(Connection conn) throws Exception {
        // /tmp/users.dat
        // 1,user1,password1
        // 2,user2,password2
        // 3,user3,password3
        // 4,user4,password4
        // 5,user5,password5
        PreparedStatement stmt = conn.prepareStatement("load data local inpath '/tmp/users.dat' into table users");
        stmt.executeUpdate();
        stmt.close();
    }

    private static void queryTable(Connection conn) throws Exception {
        PreparedStatement stmt = conn.prepareStatement("select * from users");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            System.out.println("id: " + rs.getString(1));
            System.out.println("username: " + rs.getString(2));
            System.out.println("password: " + rs.getString(3));

        }
        stmt.close();
    }
}
```
