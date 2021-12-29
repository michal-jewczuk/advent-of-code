package aoc2021

import javax.management.InvalidAttributeValueException
import scala.annotation.tailrec
import scala.collection.mutable
import scala.util.Try

/**
 * General solution written based on
 * https://github.com/kemmel-dev/AdventOfCode2021/blob/master/day24/AoC%20Day%2024.pdf
 */
object Day24 extends App {

  val alu = loadData("d24.txt").get

  def decodeAlu(list: List[String]): List[(String, String, String)] = {
    list.map(_.split(" ")).map(e => if (e(0).equals("inp")) (e(0), e(1), "") else (e(0), e(1), e(2)))
  }

  def findConnectedValues(value: Int, isMax: Boolean): (Int, Int) = {
    if (isMax) {
      if (value < 0) (9, 9 + value)
      else (9 - value, 9)
    } else {
      if (value < 0) (1 - value, 1)
      else (1, 1 + value)
    }
  }

  def findModelNumber(list: List[String]): Unit = {
    val alu = decodeAlu(list)

    def calculateValue(isMax: Boolean): String = {
      val criticalOps = (0 to 13).map(n => (alu(5 + 18 * n)._3.toInt, alu(15 + 18 * n)._3.toInt))
      val connections: mutable.Map[Int, (Int, Int)] = mutable.Map()
      val tmpStack: mutable.Stack[(Int, (Int, Int))] = mutable.Stack()

      criticalOps.indices.foreach(n => {
        if (criticalOps(n)._1 > 0) {
          connections(n) = (n, 0)
          tmpStack.push((n, criticalOps(n)))
        } else {
          val (index, ops) = tmpStack.pop()
          connections(n) = (index, ops._2 + criticalOps(n)._1)
        }
      })
      val pattern: mutable.Map[Int, Int] = mutable.Map.from((0 to 13).map(n => n -> 0))

      connections.foreach((k,v) => {
        if (k != v._1) {
          val values = findConnectedValues(v._2, isMax)
          pattern(k) = values._2
          pattern(v._1) = values._1
        }
      })
      pattern.toList.map(_._2).foldRight("")(_+_)
    }

    val maxValue: String = calculateValue(true)
    val minValue: String = calculateValue(false)

    def runThroughAlu(modelNum: String): Boolean = {
      val values: mutable.Map[String, Long] = mutable.Map("x" -> 0l, "y" -> 0l, "z" -> 0l, "w" -> 0l)

      if (modelNum.contains('0')) return false
      var inpAt = 0
      def input(key: String): Unit = {
        values(key) = modelNum(inpAt).toString.toInt
        inpAt += 1
      }

      def operation(opType: String, key: String, by: String): Unit = {
        val byVal = if (values.view.keySet.contains(by)) values(by) else by.toLong
        opType match {
          case "add" => values(key) = values(key) + byVal
          case "mul" => values(key) = values(key) * byVal
          case "div" => if (byVal != 0) values(key) = values(key) / byVal
          case "mod" => if (values(key) >= 0 && byVal > 0 ) values(key) = values(key) % byVal
          case "eql" => values(key) = if (values(key) == byVal) 1 else 0
        }
      }

      alu.foreach(command =>
        command._1 match {
          case "inp" => input(command._2)
          case _ => operation(command._1, command._2, command._3)
        }
      )
      values("z") == 0
    }

    println((maxValue, runThroughAlu(maxValue)))
    println((minValue, runThroughAlu(minValue)))
  }

  findModelNumber(alu)
}
