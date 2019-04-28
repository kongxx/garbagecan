package my.flinkstudy

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
