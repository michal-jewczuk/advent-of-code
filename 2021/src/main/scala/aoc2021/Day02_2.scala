package aoc2021

object Day02_2 extends App {

  val directionsExample = loadData("d02e.txt").get
  val directions= loadData("d02.txt").get

  def extractValue(dir: String, line: String): Int = {
    line.substring(dir.length + 1).toInt
  }

  def calculatePosition(directions: List[String]): (Int, Int) = {
    var forward = 0
    var depth = 0
    var aim = 0
    directions.foreach(d => {
      if (d.startsWith("down")) aim = aim + extractValue("down", d)
      else if (d.startsWith("up")) aim = aim - extractValue("up", d)
      else {
        val fVal = extractValue("forward", d)
        forward = forward + fVal
        depth = depth + fVal * aim
      }
    })

    (forward, depth)
  }

  println(calculatePosition(directionsExample))
  
  val position = calculatePosition(directions)
  println(position._1 * position._2)
}
