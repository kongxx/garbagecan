使用Maven自动部署插件

Java程序员常常有这样的困惑，每天开发项目的时候都要重复着“写代码 -> 打包 -> 停止服务 -> 部署代码 -> 启动服务 -> 测试”。尤其是当项目的历史比较长，目录结构比较混乱的时候更是如此。这其中有很多时间都花在了手动启停服务，部署包上面了。今天就说说怎样使用Maven的插件来实现自动“打包 -> 停止服务 -> 部署代码 -> 启动服务”。

修改Maven的配置文件pom.xml，加入类似如下的配置。

``` xml
    <build>
        ....
        <extensions>
            <extension>
                <groupId>org.apache.maven.wagon</groupId>
                <artifactId>wagon-ssh</artifactId>
                <version>2.8</version>
            </extension>
        </extensions>
        
        <plugins>
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>wagon-maven-plugin</artifactId>
                <version>1.0</version>
                <executions>
                    <execution>
                        <id>stop</id>
                        <phase>package</phase>
                        <goals>
                            <goal>sshexec</goal>
                        </goals>
                        <configuration>
                            <url>scp://<username>:<password>@<host>/</url>
                            <commands>
                                <command>/usr/share/tomcat/bin/tomcat stop</command>
                            </commands>
                        </configuration>
                    </execution>
                    <execution>
                        <id>upload</id>
                        <phase>package</phase>
                        <goals>
                            <goal>upload</goal>
                        </goals>
                        <configuration>
                            <url>scp://<username>:<password>@<host>/</url>
                            <fromDir>${project.basedir}/target</fromDir>
                            <includes>*.jar</includes>
                            <toDir>...</toDir>
                        </configuration>
                    </execution>
                    <execution>
                        <id>start</id>
                        <phase>package</phase>
                        <goals>
                            <goal>sshexec</goal>
                        </goals>
                        <configuration>
                            <url>scp://<username>:<password>@<host>/</url>
                            <commands>
                                <command>/usr/share/tomcat/bin/tomcat start</command>
                            </commands>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

此时，“打包，停止服务，部署，启动服务”只需要运行下面的命令即可完成。
``` shell
mvn clean package
```

插件的具体使用方法可以参考：http://www.mojohaus.org/wagon-maven-plugin/

