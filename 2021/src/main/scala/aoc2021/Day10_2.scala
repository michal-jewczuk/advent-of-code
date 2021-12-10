package aoc2021

object Day10_2 extends App {

  val navigationE = loadData("d10e.txt").get
  val navigation = loadData("d10.txt").get

  def findSequencesToEndIncomplete(line: String): List[String] = {
    val pairs = Map("(" -> ")", "[" -> "]", "{" -> "}", "<" -> ">")
    var openings = List(line.charAt(0).toString)
    (1 until line.length).foreach(n => {
      val at = line.charAt(n).toString
      if (pairs.keys.toList.contains(at)) openings = at +: openings
      else if (at.equals(pairs(openings.head))) openings = openings.tail
      else return List()
    })
    openings.map(pairs)
  }

  def countScore(line: List[String]): Long = {
    val points = Map[String, Long](")" -> 1, "]" -> 2, "}" -> 3, ">" -> 4)
    line.foldLeft(0l)((a, total) => a * 5 + points(total))
  }

  def mapToCompletionPoints(lines: List[String]): List[Long] = {
    lines.map(findSequencesToEndIncomplete).filter(_.nonEmpty).map(countScore).sorted
  }

  val pointsE = mapToCompletionPoints(navigationE)
  val points = mapToCompletionPoints(navigation)

  println(pointsE(pointsE.size / 2))
  println(points(points.size / 2))
}
