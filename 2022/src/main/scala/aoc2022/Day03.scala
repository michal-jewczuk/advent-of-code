package aoc2022

import scala.annotation.tailrec

object Day03 extends App {

  val linesE = loadStrings("d03e.txt")
  val lines = loadStrings("d03.txt")

  private val priorities: Map[Char, Int] = Map(
    'a' -> 1, 'b' -> 2, 'c' -> 3, 'd' -> 4, 'e' -> 5, 'f' -> 6, 'g' -> 7, 'h' -> 8, 'i' -> 9, 'j' -> 10, 'k' -> 11, 'l' -> 12, 'm' -> 13,
    'n' -> 14, 'o' -> 15, 'p' -> 16, 'q' -> 17, 'r' -> 18, 's' -> 19, 't' -> 20, 'u' -> 21, 'v' -> 22, 'w' -> 23, 'x' -> 24, 'y' -> 25, 'z' -> 26,
    'A' -> 27, 'B' -> 28, 'C' -> 29, 'D' -> 30, 'E' -> 31, 'F' -> 32, 'G' -> 33, 'H' -> 34, 'I' -> 35, 'J' -> 36, 'K' -> 37, 'L' -> 38, 'M' -> 39,
    'N' -> 40, 'O' -> 41, 'P' -> 42, 'Q' -> 43, 'R' -> 44, 'S' -> 45, 'T' -> 46, 'U' -> 47, 'V' -> 48, 'W' -> 49, 'X' -> 50, 'Y' -> 51, 'Z' -> 52
  )

  private def findInvalidItem(input: List[String]): List[Char] = {
    @tailrec
    def findCommonItem(left: List[Char], right: List[Char]): Char = {
      if (right.contains(left.head)) left.head
      else findCommonItem(left.tail, right)
    }

    input
      .map(l => (l.substring(0, l.length / 2), l.substring(l.length / 2 , l.length)))
      .map(e => findCommonItem(e._1.toList, e._2.toList))
  }

  private def findGroupBadges(input: List[String]): List[Char] = {
    @tailrec
    def findCommonItem(first: List[Char], second: List[Char], third: List[Char]): Char = {
      val el = first.head
      if (second.contains(el) && third.contains(el)) el
      else findCommonItem(first.tail, second, third)
    }

    (input.indices by 3)
      .map(i => findCommonItem(input(i).toList, input(i+1).toList, input(i+2).toList)).toList
  }

  println(findInvalidItem(linesE).map(priorities).sum)
  println(findInvalidItem(lines).map(priorities).sum)
  println("================")
  println(findGroupBadges(linesE).map(priorities).sum)
  println(findGroupBadges(lines).map(priorities).sum)
}
