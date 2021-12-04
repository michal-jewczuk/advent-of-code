package aoc2021

import java.beans.BeanInfo

object Day04_1 extends App {

  val allE = loadData("d04e.txt").get
  val markingsE = allE.head.split(",").map(_.toInt).toList
  val bingosE = extractBingos(allE)
  val all = loadData("d04.txt").get
  val markings = all.head.split(",").map(_.toInt).toList
  val bingos = extractBingos(all)

  val (lastNumberE, winningBoardE) = findWinningBoard(markingsE, bingosE)
  println(lastNumberE * winningBoardE.sumUnmarked)
  val (lastNumber, winningBoard) = findWinningBoard(markings, bingos)
  println(lastNumber * winningBoard.sumUnmarked)

  def extractBingos(all: List[String]): List[Bingo] = {
    (2 to all.length - 5).by(6).map(n => new Bingo(all.slice(n, n + 5))).toList
  }

  def findWinningBoard(markers: List[Int], bingos: List[Bingo]): (Int, Bingo) = {
    markers.foreach(n => {
      bingos.foreach(_.mark(n))
      val winners = bingos.filter(_.isBingo)
      if (winners.size == 1) return (n, winners.head)
    })
    (-1, null)
  }
}
