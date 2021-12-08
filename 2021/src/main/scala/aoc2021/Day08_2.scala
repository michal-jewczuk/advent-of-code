package aoc2021

import scala.collection.mutable

object Day08_2 extends App {

  val e0 = List(0,1,2,4,5,6)
  val e1 = List(2,5)
  val e2 = List(0,2,3,4,6)
  val e3 = List(0,2,3,5,6)
  val e4 = List(1,2,3,5)
  val e5 = List(0,1,3,5,6)
  val e6 = List(0,1,3,4,5,6)
  val e7 = List(0,2,5)
  val e8 = List(0,1,2,3,4,5,6)
  val e9 = List(0,1,2,3,5,6)

  val entriesE = loadData("d08e.txt").get
  val entries = loadData("d08.txt").get

  def decodeSignals(numbers: List[String]): List[String] = {
    def getDiffChar(e1: String, e2: String): String = {
      e1.filter(e => !e2.contains(e))
    }

    val decoded = mutable.Map[Int, String](0 -> "z", 1-> "z", 2-> "z", 3-> "z", 4-> "z", 5-> "z", 6-> "z")
    val n1 = numbers.filter(_.length == 2).head
    val n7 = numbers.filter(_.length == 3).head
    val n4 = numbers.filter(_.length == 4).head
    val n8 = numbers.filter(_.length == 7).head
    val n3 = numbers.filter(n => n.length == 5 && getDiffChar(n, n7).length == 2).head
    val n9 = numbers.filter(n => n.length == 6 && getDiffChar(n, n4).length == 2).head
    val n0 = numbers.filter(n => n.length == 6 && getDiffChar(n, n1).length == 4).filter(!_.equals(n9)).head
    val n6 = numbers.filter(_.length == 6).filter(n => !n.equals(n9) && !n.equals(n0)).head
    val n2 = numbers.filter(_.length == 5).filter(getDiffChar(n6, _).length == 2).filter(!_.equals(n3)).head

    decoded(0) = getDiffChar(n7, n1)
    decoded(1) = getDiffChar(n4, n3)
    decoded(2) = getDiffChar(n8, n6)
    decoded(3) = getDiffChar(n8, n0)
    decoded(4) = getDiffChar(n8, n9)
    decoded(5) = getDiffChar(n1, n2)
    decoded(6) = getDiffChar("abcdefg", decoded.view.values.foldRight("")(_ + _))

    decoded.values.toList
  }

  def decodeNumbers(pattern: List[String], numbers: List[String]): Int = {
    val decodedNumbers = List(e0, e1, e2, e3, e4, e5, e6, e7, e8, e9).map(_.foldRight("")(pattern(_) + _)).map(_.sorted)

    def mapToNumber(encoded: String): Int = {
      val index = decodedNumbers.filter(n => n.equals(encoded.sorted)).head
      decodedNumbers.indexOf(index)
    }

    numbers.map(mapToNumber).foldRight("")(_ + _).toInt
  }

  def processLine(line: String): Int = {
    val inputs = line.split(" ").slice(0, 10).toList
    val outputs = line.split(" ").slice(11, 15).toList
    val pattern = decodeSignals(inputs)
    decodeNumbers(pattern, outputs)
  }

  println(entriesE.map(processLine).sum)
  println(entries.map(processLine).sum)
}
