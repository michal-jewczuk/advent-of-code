package aoc2023

object Day00 extends App {

  val lines = loadData("d00.txt").get

  println(lines.filter(l => l.nonEmpty))

}
