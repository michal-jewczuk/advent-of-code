package aoc2021

object Day02_1 extends App {

  val directionsExample = loadData("d02e.txt").get
  val directions= loadData("d02.txt").get

  def sumOneDirection(direction: String, all: List[String]): Int = {
    all
      .filter(d => d.startsWith(direction))
      .map(_.substring(direction.length + 1))
      .map(_.toInt)
      .sum
  }

  def mapPosition(directions: List[String]): (Int, Int) = {
    val forwards = sumOneDirection("forward", directions)
    val downs = sumOneDirection("down", directions)
    val ups = sumOneDirection("up", directions)

    (forwards, downs - ups)
  }

  val positionExample = mapPosition(directionsExample)
  val position = mapPosition(directions)

  println(mapPosition(directionsExample))
  println(position._1 * position._2)
}
