package aoc2021

object Day10_1 extends App {

  val navigationE = loadData("d10e.txt").get
  val navigation = loadData("d10.txt").get

  def findFirstCorruptedSign(line: String): String = {
    val pairs = Map("(" -> ")", "[" -> "]", "{" -> "}", "<" -> ">")
    var openings = List("")
    (0 until line.length).foreach(n => {
      val at = line.charAt(n).toString
      if (pairs.keys.toList.contains(at)) openings = at +: openings
      else if (at.equals(pairs(openings.head))) openings = openings.tail
      else return at
    })
    "incomplete"
  }

  def mapCorruptedToPoints(lines: List[String]): Long = {
    val points = Map(")" -> 3, "]" -> 57, "}" -> 1197, ">" -> 25137)
    lines.map(findFirstCorruptedSign).filter(!_.equals("incomplete")).map(points).sum
  }

  println(mapCorruptedToPoints(navigationE))
  println(mapCorruptedToPoints(navigation))
}
