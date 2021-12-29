package aoc2021

import scala.annotation.tailrec

object Day18 extends App {

  val snailNumbersE = loadData("d18e.txt").get
  val snailNumbers = loadData("d18.txt").get

  def explode(current: String): String = {
    var result = current

    def identifyPair: (Int, Int, Int) = {
      var left = 0
      var right = 0
      result.indices.foreach(n => {
        if (result(n).equals('[')) left += 1
        else if (result(n).equals(']')) right += 1
        else if (result(n).equals(',') && left - 5 == right) {
          val lV = if (result(n-2).equals('[')) result.substring(n-1,n) else result.substring(n-2,n)
          val rV = if (result(n+2).equals(']')) result.substring(n+1,n+2) else result.substring(n+1,n+3)
          return (lV.toInt, rV.toInt, n)
        }
      })
      (-1, -1, -1)
    }

    val (left, right, middle) = identifyPair
    if ((left, right, middle) == (-1,-1,-1)) return current

    val rightNumber = findRightNumber(result.substring(middle + 3, result.length), middle + 3)
    if (rightNumber._1 != -1) {
      val startR = if rightNumber._2 > 9 then rightNumber._1 + 1 else rightNumber._1
      result = result.substring(0, rightNumber._1) + (rightNumber._2 + right) + result.substring(startR + 1, result.length)
    }

    val leftNumber = findLeftNumber(result.substring(0, middle - 2))
    if (leftNumber._1 != -1) {
      val endL = if leftNumber._2 > 9 then leftNumber._1 + 1 else leftNumber._1
      result = result.substring(0, leftNumber._1) + (leftNumber._2 + left) + result.substring(endL + 1, result.length)
    }

    val (leftN, rightN, middleN) = identifyPair
    result.substring(0, middleN - 1 - leftN.toString.length) + "0" + result.substring(middleN + 2 + rightN.toString.length, result.length)
  }

  def split(current: String): String = {

    def identifyNumber: Int = {
      val special = Set('[', ']', ',')
      (0 until current.length)
        .foreach(n => if (!special.contains(current(n)) && !special.contains(current(n+1))) return n)
      -1
    }

    def splitNumber(number: Int): String = {
      if (number % 2 == 0) s"[${number/2},${number/2}]"
      else s"[${number/2},${number/2 + 1}]"
    }

    val pos = identifyNumber
    if (pos == -1) return current
    val number = current.substring(pos, pos+2).toInt

    s"${current.substring(0,pos)}${splitNumber(number)}${current.substring(pos+2, current.length)}"
  }

  def findLeftNumber(input: String): (Int, Int) = {
    val special = Set('[', ']', ',')
    Range(input.length - 1, 0, -1).foreach(n => {
      if (!special.contains(input(n))) {
        if (!special.contains(input(n-1))) return (n-1, input.substring(n-1, n+1).toInt)
        else return (n, input(n).toString.toInt)
      }
    })
    (-1, 0)
  }

  def findRightNumber(input: String, start: Int): (Int, Int) = {
    val special = Set('[', ']', ',')
    input.indices.foreach(n => {
      if (!special.contains(input(n))) {
        if (!special.contains(input(n+1))) return (n + start, input.substring(n, n+2).toInt)
        else return (n + start, input(n).toString.toInt)
      }
    })
    (-1, 0)
  }

  def splitInTwo(input: String): (String, String) = {
    var left = 0
    var right = 0
    input.indices.foreach(n => {
      if (input(n).toString.equals("[")) left += 1
      else if (input(n).toString.equals("]")) right += 1
      else if (input(n).toString.equals(",") && left - 1 == right)
        return (input.substring(1,n), input.substring(n+1, input.length - 1))
    })
    ("","")
  }

  def countMagnitude(input: String): Int = {
    def isNumber(inp: String): Boolean = {
      inp.nonEmpty && !inp.contains("[")
    }

    def countUntil(left: String, right: String, leftAcc: Int, rightAcc: Int): Int = {
      if (left.isBlank && right.isBlank) leftAcc + rightAcc
      else if (isNumber(left) && isNumber(right)) {
        countUntil("", "", 3 * left.toInt, 2 * right.toInt)
      }
      else if (isNumber(left) && !isNumber(right)) {
        val (rL, rR) = splitInTwo(right)
        countUntil("", "", 3 * left.toInt, 2 * countUntil(rL, rR, 1, 2 * rightAcc))
      } else if (!isNumber(left) && isNumber(right)) {
        val (lL, lR) = splitInTwo(left)
        countUntil("", "", 3 * countUntil(lL, lR, 3 * leftAcc, 1), 2 * right.toInt)
      } else {
        val (lL, lR) = splitInTwo(left)
        val (rL, rR) = splitInTwo(right)
        countUntil("", "", 3 * countUntil(lL, lR, 3 * leftAcc, 1), 2 * countUntil(rL, rR, 1, 2 * rightAcc))
      }
    }

    val (left, right) = splitInTwo(input)
    countUntil(left, right, 1, 1)
  }


  def addTwoSnailNumbers(numOne: String, numTwo:String): String = {
    @tailrec
    def reduce(current: String, wasReduced: Boolean): String = {
      if (!wasReduced) current
      else {
        val explodeResult = explode(current)
        if (!explodeResult.equals(current)) reduce(explodeResult, true)
        else {
          val splitResult = split(current)
          if (!splitResult.equals(current)) reduce(splitResult,true)
          else reduce(current, false)
        }
      }
    }

    reduce(s"[$numOne,$numTwo]", true)
  }

  def solveHomework(lines: List[String]): Int = {
    @tailrec
    def solveUntil(elements: List[String], result: String): String = {
      if (elements.isEmpty) result
      else solveUntil(elements.tail, addTwoSnailNumbers(result, elements.head))
    }

    countMagnitude(solveUntil(lines.tail.tail, addTwoSnailNumbers(lines.head, lines.tail.head)))
  }

  def findMaxMagnitude(lines: List[String]): Int = {
    val test = for {
      x <- lines
      y <- lines
    } yield (x, y)
    test.filter(p => !p._1.equals(p._2)).map(p => addTwoSnailNumbers(p._1, p._2)).map(countMagnitude).max
  }

  println(solveHomework(snailNumbersE))
  println(solveHomework(snailNumbers))
  println("----")
  println(findMaxMagnitude(snailNumbersE))
  println(findMaxMagnitude(snailNumbers))
}
