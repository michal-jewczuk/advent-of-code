package aoc2022

import scala.annotation.tailrec

object Day06 extends App {

  val ex1 = "mjqjpqmgbljsphdztnvjfqwrcgsmlb"
  val ex2 = "bvwbjplbgvbhsrlpgdmjqwftvncz"
  val ex3 = "nppdvjthqldpwncqszvftbrmjlhg"
  val ex4 = "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"
  val ex5 = "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"
  val code = loadStrings("d06.txt").head

  private def findPacket(line: String, size: Int): Int = {
    @tailrec
    def findUntil(from: String, values: Set[Char], step: Int, acc: Int): Int = {
      if (values.size == step + 1) acc
      else findUntil(from, (acc to acc + step).map(from.charAt(_)).toSet, step, acc + 1)
    }

    findUntil(line, Set(), size -1, 0) + size - 1
  }

  println(findPacket(ex1, 4))
  println(findPacket(ex2, 4))
  println(findPacket(ex3, 4))
  println(findPacket(ex4, 4))
  println(findPacket(ex5, 4))
  println(findPacket(code, 4))
  println("================")
  println(findPacket(ex1, 14))
  println(findPacket(ex2, 14))
  println(findPacket(ex3, 14))
  println(findPacket(ex4, 14))
  println(findPacket(ex5, 14))
  println(findPacket(code, 14))
}
