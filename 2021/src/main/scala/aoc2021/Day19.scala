package aoc2021

import scala.annotation.tailrec

object Day19 extends App {

  val rawE = loadData("d19e.txt").get
  val raw = loadData("d19.txt").get
  val beaconsE = parseBeacons(rawE)
  val beacons = parseBeacons(raw)

  case class Scanner(id: Int, absolute: (Int,Int,Int), conv: List[(Int,Int,Int)], parent: Int)

  def parseBeacons(lines: List[String]): Map[Int, List[(Int,Int,Int)]] = {
    @tailrec def procUntil(rest: List[String],
                           parsed: Map[Int, List[(Int,Int,Int)]],
                           beacons: List[(Int,Int,Int)], count: Int): Map[Int, List[(Int,Int,Int)]] = {
      if (rest.isEmpty) parsed ++ Map(count-1 -> beacons)
      else {
        if (rest.head.startsWith("---")) {
          val newMap = if (count > 0) parsed ++ Map(count-1 -> beacons) else parsed
          procUntil(rest.tail, newMap, List.empty, count + 1)
        } else if (rest.head.isBlank) {
          procUntil(rest.tail, parsed, beacons, count)
        } else {
          val coords = rest.head.split(",").map(_.toInt).toList
          procUntil(rest.tail, parsed, beacons :+ (coords(0),coords(1),coords(2)), count)
        }
      }
    }
    procUntil(lines, Map.empty, List.empty, 0)
  }

  val allRotations = List(
    (1,2,3), (2,-1,3), (-1,-2,3), (-2,1,3), (1,-2,-3), (-2,-1,-3), (-1,2,-3), (2,1,-3),
    (1,-3,2), (-3,-1,2), (-1,3,2), (3,1,2), (1,3,-2), (3,-1,-2), (-1,-3,-2), (-3,1,-2),
    (-3,2,1), (2,3,1), (3,-2,1), (-2,-3,1), (3,2,-1), (2,-3,-1), (-3,-2,-1), (-2,3,-1)
  )

  def rotate(coords: (Int,Int,Int), conv: (Int,Int,Int)): (Int,Int,Int) = {
    val c = List(coords._1, coords._2, coords._3)
    (
      c(Math.abs(conv._1) - 1) * (conv._1) / Math.abs(conv._1),
      c(Math.abs(conv._2) - 1) * (conv._2) / Math.abs(conv._2),
      c(Math.abs(conv._3) - 1) * (conv._3) / Math.abs(conv._3)
    )
  }

  @tailrec def rotateMulti(coords: (Int,Int,Int), rotations: List[(Int,Int,Int)]): (Int,Int,Int) = {
    if (rotations.isEmpty) coords
    else rotateMulti(rotate(coords, rotations.head), rotations.tail)
  }

  def findCommonBeacons(beacons: Map[Int, List[(Int,Int,Int)]]): Map[Int, Scanner] = {
    def areOverlaping(s1: Int, s2: Int): ((Int,Int,Int),(Int,Int,Int)) = {
      allRotations.foreach(r => {
        val s2Coords = beacons(s1).flatMap(beacon => beacons(s2)
          .map(rotate(_,r))
          .map(b => (beacon._1 - b._1, beacon._2 - b._2, beacon._3 - b._3)))
        @tailrec def find12(toCheck: List[(Int,Int,Int)]): (Int,Int,Int) = {
          if (toCheck.isEmpty) (0,0,0)
          else if (s2Coords.count(_ == toCheck.head) >= 12) toCheck.head
          else find12(toCheck.tail)
        }
        val coords = find12(s2Coords)
        if (coords != (0,0,0)) return (r, coords)
      })

      ((0,0,0),(0,0,0))
    }

    @tailrec def buildTree(toCheck: List[Int], scanners: Map[Int, Scanner], leftToProcess: Set[Int]): Map[Int, Scanner] = {
      if (toCheck.isEmpty) scanners
      else {
        val curId = toCheck.head
        val result = leftToProcess.map(id => (id, areOverlaping(curId, id))).filter(_._2._2 != (0,0,0))
        val newIds = result.map(_._1)
        buildTree(toCheck.tail ++ newIds, scanners ++ result.map(r => (r._1 -> createScannerWithAbsolutePos(r, scanners(curId)))), leftToProcess -- newIds)
      }
    }

    def createScannerWithAbsolutePos(elem: (Int, ((Int,Int,Int), (Int,Int,Int))), parent: Scanner): Scanner = {
      val (id, (conv, rel)) = elem
      val (x,y,z) = rotateMulti(rel, parent.conv)
      val (xp,yp,zp) = parent.absolute
      new Scanner(id, (x + xp, y + yp, z + zp), conv +:parent.conv, parent.id)
    }

    buildTree(List(0), Map(0 -> new Scanner(0, (0,0,0), List((1,2,3)), -1)), beacons.keySet - 0)
  }

  def transformBeacons(beacons: List[(Int,Int,Int)], scanner: Scanner): List[(Int,Int,Int)] = {
    val (x,y,z) = scanner.absolute
    beacons.map(rotateMulti(_, scanner.conv)).map(c => (c._1 + x, c._2 + y, c._3 + z))
  }

  def computeMaxManhatanDistance(scanners: Map[Int, Scanner]): Int = {
    def computeDistance(s1: Scanner, s2: Scanner): Int = {
      val (x1, y1, z1) = s1.absolute
      val (x2, y2, z2) = s2.absolute
      Math.abs(x1-x2) + Math.abs(y1-y2) + Math.abs(z1-z2)
    }
    scanners.values.flatMap(current => scanners.values.map(computeDistance(current, _))).max
  }

  def solveBothParts(beacons: Map[Int, List[(Int,Int,Int)]]): (Int, Int) = {
    val scanners = findCommonBeacons(beacons)
    (beacons.flatMap((k,v) => transformBeacons(v, scanners(k))).toSet.size, computeMaxManhatanDistance(scanners))
  }

  println(solveBothParts(beaconsE))
  println(solveBothParts(beacons))

}
