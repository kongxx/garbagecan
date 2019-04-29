package my.flinkstudy

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time


object StreamingJob {
  def main(args: Array[String]) {
    if (args.length != 1) {
      println("Please give a port.")
      System.exit(1)
    }

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val text = env.socketTextStream("localhost", args(0).toInt, '\n')

    val counts = text.flatMap { _.toLowerCase.split("\\W+") filter { _.nonEmpty } }
      .map { (_, 1) }
      .keyBy(0)
      .timeWindow(Time.seconds(5))
      .sum(1)

    // val counts = text.flatMap { w => w.split("\\s") }
    //   .map { w => WordWithCount(w, 1) }
    //   .keyBy("word")
    //   .timeWindow(Time.seconds(5))
    //   .sum("count")

    counts.print()

    env.execute("Window Stream WordCount")
  }

  case class WordWithCount(word: String, count: Long)
}
