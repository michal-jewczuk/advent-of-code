package aoc2023

import scala.annotation.tailrec

object Day07 extends App {

  private val handsBidsE = loadData("d07e.txt").get.map(_.split(" ")).map(l => (l(0), l(1).trim.toInt)).toMap
  private val handsBids = loadData("d07.txt").get.map(_.split(" ")).map(l => (l(0), l(1).trim.toInt)).toMap
  private val cardStrength: Map[Char, Int] = Map('A' -> 13, 'K' -> 12, 'Q' -> 11, 'J' -> 10, 'T' -> 9, '9' -> 8, '8' -> 7, '7' -> 6, '6' -> 5, '5' -> 4, '4' -> 3, '3' -> 2, '2' -> 1)
  private val cardStrengthWithJ: Map[Char, Int] = Map('A' -> 13, 'K' -> 12, 'Q' -> 11, 'J' -> 0, 'T' -> 9, '9' -> 8, '8' -> 7, '7' -> 6, '6' -> 5, '5' -> 4, '4' -> 3, '3' -> 2, '2' -> 1)

  private def sortHandsByStrength(hand1: String, hand2: String): Boolean = {
    val h1S = getHandStrength(hand1)
    val h2S = getHandStrength(hand2)
    if (h1S != h2S) h1S < h2S
    else compareCards(hand1, hand2, cardStrength)
  }

  private def compareCards(hand1: String, hand2: String, strengths: Map[Char, Int]): Boolean = {
    @tailrec def compareAcc(h1: List[Char], h2: List[Char]): Boolean = {
      if (h1.head != h2.head) strengths(h1.head) < strengths(h2.head)
      else compareAcc(h1.tail, h2.tail)
    }

    compareAcc(hand1.toList, hand2.toList)
  }

  private def getHandStrength(hand: String): Int = {
    // 1 (high card) to 7 (five of a kind)
    val cards = hand.toSet
    if (cards.size == 1) 7
    else if (cards.size == 2) {
      // four of a kind or full house
      val firstCardCount = hand.toList.count(_ == cards.head)
      if (firstCardCount == 1 || firstCardCount == 4) 6
      else 5
    }
    else if (cards.size == 3) {
      // three of a kind or two pairs
      val firstCardCount = hand.toList.count(_ == cards.head)
      val secondCardCount = hand.toList.count(_ == cards.tail.head)
      val thirdCardCount = hand.toList.count(_ == cards.tail.tail.head)
      if (firstCardCount == 3 || secondCardCount == 3 || thirdCardCount == 3 ) 4
      else 3
    }
    else if (cards.size == 4) 2
    else 1
  }

  private def getHandStrengthWithJoker(hand: String): Int = {
    // 1 (high card) to 7 (five of a kind)
    val strength = getHandStrength(hand)
    val jokerCount = hand.toList.count(_ == 'J')

    if (jokerCount == 0) strength
    else if (strength == 7 || strength == 6 || strength == 5) 7
    else if (strength == 4) 6
    else if (strength == 3) {
      if (jokerCount == 2) 6
      else 5
    }
    else if (strength == 2) 3
    else 2 // hand high upgraded to one pair
  }

  private def sortHandsByStrengthWithJokers(hand1: String, hand2: String): Boolean = {
    val h1S = getHandStrengthWithJoker(hand1)
    val h2S = getHandStrengthWithJoker(hand2)
    if (h1S != h2S) h1S < h2S
    else compareCards(hand1, hand2, cardStrengthWithJ)
  }

  private def calculateTotalWinnings(hands: Map[String, Int], withJokers: Boolean): Long = {
    val result = if (withJokers) hands.keys.toList.sortWith((h1, h2) => sortHandsByStrengthWithJokers(h1,h2))
    else hands.keys.toList.sortWith((h1, h2) => sortHandsByStrength(h1,h2))
    println(result)
    @tailrec def calcWithAcc(toProcess: List[String], idx: Int, acc: Long): Long = {
      if (toProcess.isEmpty) acc
      else calcWithAcc(toProcess.tail, idx + 1, acc + hands(toProcess.head) * idx)
    }

    calcWithAcc(result, 1, 0)
  }

  println(calculateTotalWinnings(handsBidsE, false))
  println(calculateTotalWinnings(handsBids, false)) // 251058093
  println("================")
  println(calculateTotalWinnings(handsBidsE, true))
  println(calculateTotalWinnings(handsBids, true))

  // 250535258 too high
  // 250096583 too high
  // 250046784 too high
}
