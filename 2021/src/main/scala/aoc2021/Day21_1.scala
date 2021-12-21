package aoc2021

import scala.annotation.tailrec

object Day21_1 extends App {

  def playDeterministic(starting1: Int, starting2: Int): (Int, Int, Int, Int, Int, Int) = {
    @tailrec
    def playUntil(pos1: Int, pos2: Int, score1: Int, score2: Int, dice: Int, diceRolls: Int): (Int, Int, Int, Int, Int, Int) = {
      if (score1 >= 1000 || score2 >= 1000) (pos1, pos2, score1, score2, dice, diceRolls)
      else {
        val newDice1 = if (dice == 100) 1 else dice + 1
        val newDice2 = if (newDice1 == 100) 1 else newDice1 + 1
        val newDice3 = if (newDice2 == 100) 1 else newDice2 + 1
        val move = newDice1 + newDice2 + newDice3
        if (diceRolls % 2 == 0) {
          val p1 = (pos1 - 1 + move) % 10 + 1
          playUntil(p1, pos2, score1 + p1, score2, newDice3, diceRolls + 3)
        }
        else {
          val p2 = (pos2 - 1 + move) % 10 + 1
          playUntil(pos1, p2, score1, score2 + p2, newDice3, diceRolls + 3)
        }
      }
    }

    playUntil(starting1, starting2, 0, 0, 0, 0)
  }

  println(playDeterministic(4,8))
  println(playDeterministic(6,7))
  val (pos1, pos2, score1, score2, dice, diceRolls) = playDeterministic(6,7)
  println(Math.min(score1, score2) * diceRolls)
}
