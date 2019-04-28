# 从零开始构建Flink开发项目-Scala版

今天要做一个Flink的测试，因此需要创建一个简单的Flink项目，于是找到了下面这种方式来创建一个Flink启动项目。

通过运行下面的命令来创建一个项目

``` shell
curl https://flink.apache.org/q/quickstart-scala.sh | bash
```

也可以根据 quickstart-scala.sh 文件中的内容，使用maven命令来生成自己的项目，比如：

``` shell
mvn archetype:generate                              \
  -DarchetypeGroupId=org.apache.flink               \
  -DarchetypeArtifactId=flink-quickstart-scala      \
  -DarchetypeVersion=1.8.0                          \
  -DgroupId=my.flink.quickstart                     \
  -DartifactId=quickstart                           \
  -Dversion=0.1                                     \
  -Dpackage=my.flink.quickstart                     \
  -DinteractiveMode=false
```

工程创建后，查看一下工程目录结构，如下：

``` shell
$ tree quickstart
quickstart
├── pom.xml
└── src
    └── main
        ├── resources
        │   └── log4j.properties
        └── scala
            └── my
                └── flink
                    └── quickstart
                        ├── BatchJob.scala
                        └── StreamingJob.scala
```

在开始正式编译运行之前，需要根据自己环境的scala的版本，修改一下 pom.xml 文件，我环境中的 scala 版本是 2.12.2，因此我做了下面的修改

``` shell
修改
        <scala.binary.version>2.11</scala.binary.version>
        <scala.version>2.11.12</scala.version>
为
        <scala.binary.version>2.12</scala.binary.version>
        <scala.version>2.12.2</scala.version>
```

下面使用一个例子来测试一下工程，在 src/main/scala/my/flink/quickstart 目录下创建一个 WordCount.scala 文件，内容如下：

``` shell
package my.flink.quickstart

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala._


object WordCount {

  def main(args: Array[String]) {
    if (args.length != 1) {
      println("Please give input parameter.")
      System.exit(1)
    }
    val env = ExecutionEnvironment.getExecutionEnvironment
    val text = env.readTextFile(args(0))
    val counts = text.flatMap { _.toLowerCase.split("\\W+") filter { _.nonEmpty } }
      .map { (_, 1) }
      .groupBy(0)
      .sum(1)
    counts.print()
  }
}
```

编译打包
``` shell
$ cd quickstart
$ mvn clean package
```

运行 WordCount 程序
``` shell
$ ${FLINK_HOME}/bin/flink run -c my.flink.quickstart.WordCount target/quickstart-0.1.jar ./
```
