package my.flinkstudy.windowing

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{ DataStream, StreamExecutionEnvironment }


// for i in {1..10000};do random=$(( (RANDOM % 10) + 1 )) && echo  "hello_$random" && sleep 1;done >> /tmp/wordcount.log
// ${FLINK_HOME}/bin/flink run -c my.flinkstudy.windowing.WindowWordCount target/flinkstudy-0.1.jar
object WindowWordCount {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val text = env.readTextFile("/tmp/wordcount.log")

    val windowSize = 20
    val slideSize = 10

    val counts: DataStream[(String, Int)] = text
      .flatMap(_.toLowerCase.split("\\W+"))
      .filter(_.nonEmpty)
      .map((_, 1))
      .keyBy(0)
      .countWindow(windowSize, slideSize)
      .sum(1)

    counts.print()

    env.execute("WindowWordCount")
  }

}