package aoc2022

import scala.annotation.tailrec

object Day08 extends App {

  val treesE = loadStrings("d08e.txt").map(_.toArray.map(_.toString.toInt)).toArray
  val trees = loadStrings("d08.txt").map(_.toArray.map(_.toString.toInt)).toArray

  private def mapVisibility(area: Array[Array[Int]]): List[List[Int]] = {
    def mapTreeVisibility(row: Int, col: Int): Int = {
      checkLinesVisibility(List((0,-1), (0,1), (-1,0), (1,0)), row, col)
    }

    @tailrec
    def checkLinesVisibility(offsets: List[(Int, Int)], row: Int, col: Int ): Int = {
      if (offsets.isEmpty) 0
      else
        if (isVisibleFromTheLine(area(row)(col), offsets.head, row + offsets.head._1, col + offsets.head._2)) 1
        else checkLinesVisibility(offsets.tail, row, col)
    }

    @tailrec
    def isVisibleFromTheLine(value: Int, offset: (Int, Int), curRow: Int, curCol: Int): Boolean = {
      if (curRow < 0 || curRow >= area.length || curCol < 0 || curCol >= area(0).length) true
      else if (area(curRow)(curCol) >= value) false
      else isVisibleFromTheLine(value, offset, curRow + offset._1, curCol + offset._2)
    }

    (0 until area.length).map(row => (0 until area(row).length).map(col => mapTreeVisibility(row, col)).toList).toList
  }

  private def mapScienicScore(area: Array[Array[Int]]): List[List[Int]] = {
    def getTreeScore(row: Int, col: Int): Int = {
      List((0,-1), (0,1), (-1,0), (1,0))
        .map(offset => getLineScore(area(row)(col), offset, row + offset._1, col + offset._2, 0))
        .reduceRight(_ * _)
    }

    @tailrec
    def getLineScore(value: Int, offset: (Int, Int), curRow: Int, curCol: Int, score: Int): Int = {
      if (curRow < 0 || curRow >= area.length || curCol < 0 || curCol >= area(0).length) score
      else if (area(curRow)(curCol) >= value) score + 1
      else getLineScore(value, offset, curRow + offset._1, curCol + offset._2, score + 1)
    }

    (0 until area.length).map(row => (0 until area(row).length).map(col => getTreeScore(row, col)).toList).toList
  }

  println(mapVisibility(treesE).map(e => e.sum).sum)
  println(mapVisibility(trees).map(e => e.sum).sum)
  println("================")
  println(mapScienicScore(treesE).map(e => e.max).max)
  println(mapScienicScore(trees).map(e => e.max).max)
}
