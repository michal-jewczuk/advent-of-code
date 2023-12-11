package aoc2023

import scala.annotation.tailrec

object Day11 extends App {

  private val galaxiesE = loadStrings("d11e.txt").map(_.toArray).toArray
  private val galaxies = loadStrings("d11.txt").map(_.toArray).toArray

  private def findGalaxies(universe: Array[Array[Char]]): List[(Int, Int)] = {
    universe.indices.flatMap(r => universe(r).indices
      .map(c => if (universe(r)(c) == '#') (r,c) else (-1,-1))
      .filter(_ != (-1,-1))).toList
  }

  private def findEmptyRows(universe: Array[Array[Char]]): List[Int] = {
    universe.indices.map(r => if (universe(r).toList.contains('#')) -1 else r).filter(_ != -1).toList
  }

  private def findEmptyCols(universe: Array[Array[Char]]): List[Int] = {
    val colsCount = universe(0).length
    @tailrec def findEmptyAcc(idx: Int, acc: List[Int]): List[Int] = {
      if (idx == colsCount) acc
      else {
        val sumC = universe.indices.map(r => if (universe(r)(idx) == '#') 0 else 1).sum
        if (sumC == colsCount) findEmptyAcc(idx + 1, acc :+ idx)
        else findEmptyAcc(idx + 1, acc)
      }
    }

    findEmptyAcc(0, List.empty)
  }

  private def findPaths(universe: Array[Array[Char]], times: Int): List[Long] = {
    val galaxies = findGalaxies(universe)
    val emptyRows = findEmptyRows(universe)
    val emptyCols = findEmptyCols(universe)

    def findPath(a: (Int, Int), b: (Int, Int)): Long = {
      val emptyRowsBetween = emptyRows.count(c => c > Math.min(a._1, b._1) && c < Math.max(a._1, b._1))
      val emptyColsBetween = emptyCols.count(c => c > Math.min(a._2, b._2) && c < Math.max(a._2, b._2))

      Math.abs(a._1 - b._1) + Math.abs(a._2 - b._2) + emptyRowsBetween * times + emptyColsBetween * times
    }

    @tailrec def findPathsAcc(toProcess: List[(Int, Int)], acc: List[Long]): List[Long] = {
      if (toProcess.isEmpty) acc
      else {
        val current = toProcess.head
        val pathsT = toProcess.tail.map(findPath(_, current))
        findPathsAcc(toProcess.tail, acc ++ pathsT)
      }
    }

    findPathsAcc(galaxies, List.empty)
  }

  println(findPaths(galaxiesE, 1).sum)
  println(findPaths(galaxies, 1).sum) //10494813
  println("========================")
  println(findPaths(galaxiesE, 999999).sum)
  println(findPaths(galaxies, 999999).sum)  // 840988812853
}
