package aoc2022

object Day02 extends App {

  val linesE = loadStrings("d02e.txt")
  val lines = loadStrings("d02.txt")

  val scoreForPart1: Map[String, Int] = Map(
    "A Y" -> 8, "A X" -> 4, "A Z" -> 3,
    "B Z" -> 9, "B Y" -> 5, "B X" -> 1,
    "C X" -> 7, "C Z" -> 6, "C Y" -> 2
  )

  val scoreForPart2: Map[String, Int] = Map(
    "A Y" -> 4, "A X" -> 3, "A Z" -> 8,
    "B Z" -> 9, "B Y" -> 5, "B X" -> 1,
    "C X" -> 2, "C Z" -> 7, "C Y" -> 6
  )

  def calculateScore(input: List[String], scores: Map[String, Int]): List[Int] = {
    input.map(scores.get(_).get)
  }

  println(calculateScore(linesE, scoreForPart1).sum)
  println(calculateScore(lines, scoreForPart1).sum)
  println("================")
  println(calculateScore(linesE, scoreForPart2).sum)
  println(calculateScore(lines, scoreForPart2).sum)
}
