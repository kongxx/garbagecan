package my.flinkstudy

import org.apache.flink.api.scala._


object BatchJob {

  def main(args: Array[String]) {
    val env = ExecutionEnvironment.getExecutionEnvironment

    val text = env.readTextFile("/etc/passwd")

    val counts = text.flatMap { w => w.split("\\s") }
      .map { w => WordWithCount(w, 1) }
      .groupBy("word")
      .sum("count")

    counts.print()
  }

  case class WordWithCount(word: String, count: Long)
}
