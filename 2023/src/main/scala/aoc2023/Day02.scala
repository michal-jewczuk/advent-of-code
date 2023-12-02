package aoc2023

object Day02 extends App {

  val linesE = loadData("d02e.txt").get
  val lines = loadData("d02.txt").get

  private val validGames = (12, 13, 14)

  private def extractMaxValues(lines: List[String]): List[(Int, (Int, Int, Int))] = {
    lines.map(line => processLine(line))
  }

  private def processLine(line: String): (Int, (Int, Int, Int)) = {
    val part = line.substring(5).split(":")
    val game = part.head
    val rest = part.tail.flatMap(p => p.split(";"))
      .flatMap(p => p.split(",")).map(e => e.trim).toList

    (game.toInt, extractColors(rest))
  }

  private def extractColors(elements: List[String]): (Int, Int, Int) = {
    var red = 0
    var green = 0
    var blue = 0
    var current = 0
    elements.foreach(e => {
      if (e.endsWith("red")) {
        current = e.substring(0, e.length - 4).toInt
        if (current > red) red = current
      } else if (e.endsWith("green")) {
        current = e.substring(0, e.length - 6).toInt
        if (current > green) green = current
      } else {
        current = e.substring(0, e.length - 5).toInt
        if (current > blue) blue = current
      }
    })
    (red, green, blue)
  }

  private def sumValidGames(games: List[(Int, (Int, Int, Int))]): Int = {
    games.filter(g => isGameValid(g._2)).map(g => g._1).sum
  }

  private def isGameValid(game: (Int, Int, Int)): Boolean = {
    game._1 <= validGames._1 && game._2 <= validGames._2 && game._3 <= validGames._3
  }

  private def calculatePower(games: List[(Int, (Int, Int, Int))]): Int = {
    games.map(g => g._2._1 * g._2._2 * g._2._3).sum
  }

  println(sumValidGames(extractMaxValues(linesE)))
  println(sumValidGames(extractMaxValues(lines)))
  println("=============")
  println(calculatePower(extractMaxValues(linesE)))
  println(calculatePower(extractMaxValues(lines)))
}
