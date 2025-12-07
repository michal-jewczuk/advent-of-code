package aoc2025

import scala.annotation.tailrec
import scala.util.Try

object Day6 extends App {
  private val linesE = loadStrings("d06e.txt")
  private val lines = loadStrings("d06.txt")

  private def extractLine(line: String): List[Int] = {
    line.split(" ").map(_.trim).filter(_.nonEmpty).map(_.toInt).toList
  }

  private def extractOperands(line: String): List[String] = {
    line.split(" ").map(_.trim).filter(_.nonEmpty).toList
  }

  private def doMathE(lines: List[String]): List[Long] = {
    val n1 = extractLine(lines(0))
    val n2 = extractLine(lines(1))
    val n3 = extractLine(lines(2))
    val operands = extractOperands(lines(3))

    @tailrec
    def countUntil(l1: List[Int], l2: List[Int], l3: List[Int], oper: List[String], results: List[Long]): List[Long] = {
      if (l1.isEmpty) results
      else {
        val cur = List(l1.head, l2.head, l3.head)
        val result = if (oper.head.equals("+")) cur.sum else cur.product
        countUntil(l1.tail, l2.tail, l3.tail, oper.tail, results :+ result)
      }
    }
    countUntil(n1, n2, n3, operands, List.empty)
  }

  private def doMath(lines: List[String]): List[Long] = {
    val n1 = extractLine(lines(0))
    val n2 = extractLine(lines(1))
    val n3 = extractLine(lines(2))
    val n4 = extractLine(lines(3))
    val operands = extractOperands(lines(4))

    @tailrec
    def countUntil(l1: List[Int], l2: List[Int], l3: List[Int], l4: List[Int], oper: List[String], results: List[Long]): List[Long] = {
      if (l1.isEmpty) results
      else {
        val cur = List(l1.head, l2.head, l3.head, l4.head).map(_.toLong)
        val result: Long = if (oper.head.equals("+")) cur.sum else cur.product
        countUntil(l1.tail, l2.tail, l3.tail, l4.tail, oper.tail, results :+ result)
      }
    }

    countUntil(n1, n2, n3, n4, operands, List.empty)
  }

  private def extractNumbersE(lines: List[String]): List[List[Long]] = {
    val operands = lines(3)
    @tailrec
    def extractUntil(cur: Int, limit: Int, numbers: List[List[Long]]): List[List[Long]] = {
      if (cur > limit) numbers
     else {
        val next = getNumbers(cur, List.empty)
        val num = next._2.map(_.trim).filter(_.nonEmpty).map(_.toLong)
        extractUntil(next._1 + 1, operands.length, numbers :+ num)
     }
    }

    @tailrec
    def getNumbers(i: Int, value: List[String]): (Int, List[String]) = {
      val res = Try(lines(0).charAt(i).toString).getOrElse("") +
        Try(lines(1).charAt(i).toString).getOrElse("") +
        Try(lines(2).charAt(i).toString).getOrElse("")
      if (Try(operands.charAt(i + 1)).isFailure || operands.charAt(i + 1) != ' ') (i, value :+ res)
      else {
        getNumbers(i + 1, value :+ res)
      }
    }

    extractUntil(0, operands.length, List.empty)
  }

  private def extractNumbers(lines: List[String]): List[List[Long]] = {
    val operands = lines(4)

    @tailrec
    def extractUntil(cur: Int, limit: Int, numbers: List[List[Long]]): List[List[Long]] = {
      if (cur > limit) numbers
      else {
        val next = getNumbers(cur, List.empty)
        val num = next._2.map(_.trim).filter(_.nonEmpty).map(_.toLong)
        extractUntil(next._1 + 1, operands.length, numbers :+ num)
      }
    }

    @tailrec
    def getNumbers(i: Int, value: List[String]): (Int, List[String]) = {
      val res = Try(lines(0).charAt(i).toString).getOrElse("") +
        Try(lines(1).charAt(i).toString).getOrElse("") +
        Try(lines(2).charAt(i).toString).getOrElse("") +
        Try(lines(3).charAt(i).toString).getOrElse("")
      if (Try(operands.charAt(i + 1)).isFailure || operands.charAt(i + 1) != ' ') (i, value :+ res)
      else {
        getNumbers(i + 1, value :+ res)
      }
    }

    extractUntil(0, operands.length, List.empty)
  }

  def doMath2(elements: List[List[Long]], operands: List[String]): List[Long] = {
    @tailrec
    def doMathUntil(el: List[List[Long]], op: List[String], res: List[Long]): List[Long] = {
      if (el.isEmpty) res
      else {
        val r = if (op.head.equals("+")) el.head.sum else el.head.product
        doMathUntil(el.tail, op.tail, res :+ r)
      }
    }

    doMathUntil(elements.filter(_.nonEmpty), operands, List.empty)
  }

  println(doMathE(linesE).sum)
  println(doMath(lines).sum)
  println("===============")
  println(doMath2(extractNumbersE(linesE), extractOperands(linesE(3))).sum);
  println(doMath2(extractNumbers(lines), extractOperands(lines(4))).sum);
}
