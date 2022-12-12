package aoc2022

import scala.annotation.tailrec
import scala.util.Try

object Day12 extends App {

  val heightmapE = loadStrings("d12e.txt").map(_.toArray).toArray
  val heightmap = loadStrings("d12.txt").map(_.toArray).toArray

  def findPaths(area: Array[Array[Char]], start: (Int,Int)): List[List[(Int,Int)]] = {
    @tailrec
    def findUntil(left: List[List[(Int,Int)]], completed: List[List[(Int,Int)]]): List[List[(Int,Int)]] = {
      if (left.isEmpty) completed
      else {
        val (notCompletedR, completedR) = findAllNextValid(left.head)
        findUntil(left.tail ++ notCompletedR.filter(p => left.count(_.contains(p.head)) == 0), completed ++ completedR)
      }
    }

    def findAllNextValid(path: List[(Int,Int)]): (List[List[(Int,Int)]], List[List[(Int,Int)]]) = {
      val newPaths = List((1,0),(-1,0),(0,1),(0,-1))
        .map(p => (path.head._1 + p._1, path.head._2 + p._2))
        .filter(pos => pos._1 >= 0 && pos._1 < area.length && pos._2 >=0 && pos._2 < area(0).length)
        .filter(pos => !path.contains(pos))
        .filter(pos => isValid(path.head, pos))
        .map(pos => pos +: path)

      if (newPaths.count(p => area(p.head._1)(p.head._2).equals('E')) >= 1) (List.empty, newPaths.filter(p => area(p.head._1)(p.head._2).equals('E')))
      else (newPaths, List.empty)
    }

    def isValid(cur: (Int,Int), target: (Int,Int)): Boolean = {
      val curV = area(cur._1)(cur._2)
      val tarV = area(target._1)(target._2)
      if (tarV.equals('S')) false
      else if (curV.equals('z') && tarV.equals('E')) true
      else if (curV.equals('S') && tarV.equals('a')) true
      else !tarV.equals('E') && (tarV.toInt <= curV.toInt || (tarV.toInt - curV.toInt) == 1)
    }

    findUntil(List(List(start)), List.empty)
  }

  def findPathFromBestStartingPoint(area: Array[Array[Char]]): Int = {
    val areas = area.indices
      .flatMap(row => area(row).indices
          .map(col => if (area(row)(col).equals('a') || area(row)(col).equals('S')) (row,col) else (-1,-1)))
      .filter(_ != (-1,-1))

    def isOptimalStartPoint(start: (Int,Int)): Boolean = {
      start._2 == 0 // 'b' is only in column 1 and 'a' in column one at best duplicates path for corresponding row in column 0
    }

    def findPathsForStartingLoc(loc: (Int,Int)): Int = {
      val results = findPaths(area, loc)
      if (results.isEmpty) Integer.MAX_VALUE
      else results.map(_.size).min
    }

    areas.filter(isOptimalStartPoint).map(findPathsForStartingLoc).min - 1 //last element is E
  }

  println(findPaths(heightmapE, (0,0)).map(_.size - 1).min)
  println(findPaths(heightmap, (20,0)).map(_.size - 1).min)
  println("================")
  println(findPathFromBestStartingPoint(heightmapE))
  println(findPathFromBestStartingPoint(heightmap))

}
