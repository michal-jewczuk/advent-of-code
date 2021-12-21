package aoc2021

import scala.annotation.tailrec

object Day21_2 extends App {

  def playDirac(starting1: Int, starting2: Int): (Long, Long) = {
    val moves = Map(0-> 1, 3->1, 4->3, 5->6, 6->7, 7->6, 8->3, 9->1)

    def playUniverse(pos1: Int, pos2: Int, score1: Int, score2: Int, lastMove: Int, roll: Int, wins1: Long, wins2: Long): (Long, Long) = {
      if (score1 >= 21) (wins1 + moves(lastMove), wins2)
      else if (score2 >= 21) (wins1, wins2 + moves(lastMove))
      else {
        (3 to 9).map(n => {
          if (roll % 2 == 0) {
            val p1 = (pos1 - 1 + n) % 10 + 1
            playUniverse(p1, pos2, score1 + p1, score2, n, roll + 1, wins1, wins2)
          } else {
            val p2 = (pos2 - 1 + n) % 10 + 1
            playUniverse(pos1, p2, score1, score2 + p2, n, roll + 1, wins1, wins2)
          }
        }).map(w => (w._1 * moves(lastMove), w._2 * moves(lastMove)))
          .reduce((a, b) => (a._1 + b._1, a._2 + b._2))
      }
    }

    playUniverse(starting1, starting2, 0, 0, 0, 0, 0l, 0l)
  }

  println(playDirac(4,8))
  println(playDirac(6,7))
}
