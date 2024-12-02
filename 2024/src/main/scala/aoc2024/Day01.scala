package aoc2024

object Day01 extends App {
  private val linesE = loadData("d01e.txt").get
  private val lines = loadData("d01.txt").get

  println(lines.filter(l => l.nonEmpty))
}
