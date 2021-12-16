package aoc2021

import scala.annotation.tailrec
import scala.collection.mutable
import scala.util.Try

object Day15_2 extends App {

  val caveE = convertToArray(loadData("d15e.txt").get)
  val cave = convertToArray(loadData("d15.txt").get)

  def increaseValues(area: Array[Array[Int]]): Array[Array[Int]] = {
    area.map(_.map(el => {
      val newVal = el + 1
      if (newVal > 9) 1 else newVal
    }))
  }

  def extendAreaLine(cave: Array[Array[Int]], cols: Boolean): Array[Array[Int]] = {
    val ext1 = increaseValues(cave)
    val ext2 = increaseValues(ext1)
    val ext3 = increaseValues(ext2)
    val ext4 = increaseValues(ext3)
    val ext = List(cave, ext1, ext2, ext3, ext4)
    val result = if (cols) Array.ofDim[Int](cave.length, cave(0).length * 5) else Array.ofDim[Int](cave.length * 5, cave(0).length)
    if (cols) {
      cave.indices.foreach(row => cave(row).indices.foreach(col => {
        ext.indices.foreach(n => result(row)(col + n * cave(0).length) = ext(n)(row)(col))
      }))
    } else {
      cave.indices.foreach(row => cave(row).indices.foreach(col => {
        ext.indices.foreach(n => result(row + n * cave.length)(col) = ext(n)(row)(col))
      }))
    }
    result
  }

  def extendArea(area: Array[Array[Int]]): Array[Array[Int]] = {
    val ext = extendAreaLine(area, true)
    extendAreaLine(ext, false)
  }

  def calculatePathsCosts(area: Array[Array[Int]]): Map[(Int, Int), Int] = {
    val paths: mutable.Map[(Int, Int), Int] = mutable.Map()
    paths((0,0)) = 0

    def findNextNotVisited(row: Int, col: Int, visited: Set[(Int, Int)]): Set[(Int, Int)] = {
      Set((row + 1, col), (row - 1, col), (row, col + 1), (row, col - 1))
        .filter(p => Try(area(p._1)(p._2)).isSuccess).filter(!visited.contains(_))
    }

    def findShortestNotVisited(visited: Set[(Int, Int)], paths: Set[(Int, Int)]): (Int, Int) = {
      val min = paths.filter(!visited.contains(_)).map(paths(_)).min
      paths.filter(paths(_) == min).filter(!visited.contains(_)).head
    }

    @tailrec
    def calculateUntil(visited: Set[(Int, Int)], toCheck: Set[(Int, Int)]): Set[(Int, Int)] = {
//      println((visited.size, paths.size))
      if (visited.size == area.length * area.length) visited
      else {
        val shortest = findShortestNotVisited(visited, toCheck)
        val nextNodes = findNextNotVisited(shortest._1, shortest._2, visited)
        nextNodes.foreach(p => {
          paths((p._1, p._2)) = Math.min(paths((shortest._1, shortest._2)) + area(p._1)(p._2), paths.getOrElse(p, Int.MaxValue))
        })
        calculateUntil(visited + shortest, toCheck ++ nextNodes - shortest)
      }
    }
    calculateUntil(Set(), Set((0,0)))
    paths.toMap
  }

  val extE = extendArea(caveE)
  val ext = extendArea(cave)

  println(calculatePathsCosts(caveE)((9,9)))
  println(calculatePathsCosts(cave)((99,99)))

  println(calculatePathsCosts(extE)((49,49)))
  println(calculatePathsCosts(ext)((499,499)))


}
