package aoc2021

import scala.annotation.tailrec

object Day22_2 extends App {

  val dataE1 = processData("d22e1.txt")
  val dataE2 = processData("d22e2.txt")
  val dataE3 = processData("d22e3.txt")
  val data = processData("d22.txt")

  case class Cube(x: (Int, Int), y: (Int, Int), z: (Int, Int)) {
    def apply(): Long = 1L * (x._1 to x._2).size * (y._1 to y._2).size * (z._1 to z._2).size
  }
  case class Command(state: String, cube: Cube) {
    def isOn: Boolean = state.equals("on")
  }
  case class Intersection(level: Int, cube: Cube) {
    def apply(): Long = {
      val sign = if (level % 2 == 0) 1 else -1
      cube() * sign
    }
    def isNotEmpty: Boolean = cube() > 0
  }

  def processData(fileName: String): List[Command] = {
    loadData(fileName).get.map(process)
  }

  def process(line: String): Command = {
    val state = line.substring(0, 3).trim
    val elements = line.substring(3).trim.split(",")
      .map(_.substring(2))
      .map(_.split("\\.\\.").toList)
      .map(l => (l.head.toInt, l.tail.head.toInt))
    new Command(state, new Cube(elements(0), elements(1), elements(2)))
  }

  def findIntersection(cube1: Cube, cube2: Cube): Intersection = {
    val empty = new Intersection(0, new Cube((-1, -2), (-1, -2), (-1, -2)))
    if (cube2.x._2 < cube1.x._1 || cube2.x._1 > cube1.x._2) return empty
    if (cube2.y._2 < cube1.y._1 || cube2.y._1 > cube1.y._2) return empty
    if (cube2.z._2 < cube1.z._1 || cube2.z._1 > cube1.z._2) return empty

    val x = (Math.max(cube1.x._1, cube2.x._1), Math.min(cube1.x._2, cube2.x._2))
    val y = (Math.max(cube1.y._1, cube2.y._1), Math.min(cube1.y._2, cube2.y._2))
    val z = (Math.max(cube1.z._1, cube2.z._1), Math.min(cube1.z._2, cube2.z._2))

    new Intersection(1, new Cube(x, y, z))
  }

  def findIntersection(intersection: Intersection, cube: Cube): Intersection = {
    new Intersection(intersection.level + 1, findIntersection(intersection.cube, cube).cube)
  }

  def findSolution(commands: List[Command]): Long = {
    @tailrec def countUntil(left: List[Command], processed: List[Cube], intersections: List[Intersection]): Long = {
      if (left.isEmpty) processed.map(_()).sum + intersections.map(_()).sum
      else {
        val cmd = left.head
        val newIntersec = processed.map(findIntersection(_, cmd.cube)) ++ intersections.map(findIntersection(_, cmd.cube))
        countUntil(left.tail, if (cmd.isOn) processed :+ cmd.cube else processed, intersections ++ newIntersec.filter(_.isNotEmpty))
      }
    }
    countUntil(commands, List.empty, List.empty)
  }

  println((findSolution(dataE1), 39))
  println((findSolution(dataE2), 590784))
  println(findSolution(dataE3))
  println(findSolution(data))
}
