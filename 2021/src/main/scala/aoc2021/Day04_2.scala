package aoc2021

object Day04_2 extends App {

  val allE = loadData("d04e.txt").get
  val markingsE = allE.head.split(",").map(_.toInt).toList
  val bingosE = extractBingos(allE)
  val all = loadData("d04.txt").get
  val markings = all.head.split(",").map(_.toInt).toList
  val bingos = extractBingos(all)

  val (lastNumberE, lastWinningBoardE) = findLastWinningBoard(markingsE, bingosE)
  val (losingNumberE, losingBoardE) = playUntilBingo(markingsE.slice(lastNumberE, markingsE.size), lastWinningBoardE)
  println(losingNumberE * losingBoardE.sumUnmarked)

  val (lastNumber, lastWinningBoard) = findLastWinningBoard(markings, bingos)
  val (losingNumber, losingBoard) = playUntilBingo(markings.slice(lastNumber, markings.size), lastWinningBoard)
  println(losingNumber * losingBoard.sumUnmarked)

  def extractBingos(all: List[String]): List[Bingo] = {
    (2 to all.length - 5).by(6).map(n => new Bingo(all.slice(n, n + 5))).toList
  }

  def findLastWinningBoard(markers: List[Int], bingos: List[Bingo]): (Int, Bingo) = {
    markers.indices.foreach(n => {
      bingos.foreach(_.mark(markers(n)))
      val winners = bingos.filter(_.isBingo)
      if (winners.size == bingos.size - 1) return (n, bingos.filter(!winners.contains(_)).head)
    })
    (-1, null)
  }

  def playUntilBingo(markers: List[Int], bingo: Bingo): (Int, Bingo) = {
    markers.foreach(n => {
      bingo.mark(n)
      if (bingo.isBingo) return (n, bingo)
    })
    (-1, null)
  }
}
