package aoc2022

import scala.annotation.tailrec
import scala.collection.immutable.Map

object Day10 extends App {

  val linesE = loadStrings("d10e.txt")
  val lines = loadStrings("d10.txt")
  private val crtE = (0 to 5).map(_ => Array.fill(40) {'.'}).toArray
  private val crt = (0 to 5).map(_ => Array.fill(40) {'.'}).toArray

  private def findSignalStrength(input: List[String]): Map[Int, Int] = {
    @tailrec
    def findUntil(instructions: List[String], tick: Int, register: Int, values: Map[Int, Int]): Map[Int, Int] = {
      if (instructions.isEmpty || tick > 220) values
      else {
        val (nTick, nRegister, nValues) = processInstruction(instructions.head, tick, register, values)
        findUntil(instructions.tail, nTick, nRegister, nValues)
      }
    }

    def processInstruction(instr: String, t: Int, r: Int, values: Map[Int, Int]): (Int, Int, Map[Int, Int]) = {
      var nValues = values
      if (List(20, 60, 100, 140, 180, 220).contains(t) && !values.contains(t)) nValues ++= Map(t -> r)

      if (instr.equals("noop")) (t + 1, r, nValues)
      else {
        if (List(19, 59, 99, 139, 179, 219).contains(t)) nValues ++= Map((t+1) -> r)
        if (List(18, 58, 98, 138, 178, 218).contains(t)) nValues ++= Map((t+2) -> r)
        (t + 2, r + instr.split(" ")(1).toInt, nValues)
      }
    }

    findUntil(input, 0, 1, Map.empty)
  }

  private def draw(input: List[String], screen: Array[Array[Char]]): Array[Array[Char]] = {
    @tailrec
    def drawUntil(instructions: List[String], tick: Int, pos: Int, isSecond: Boolean): Array[Array[Char]]  = {
      if (instructions.isEmpty || tick == 240) screen
      else {
        val instr = instructions.head
        val rest = if (isSecond) instructions.tail else instructions
        val col = tick % 40
        screen(Math.floorDiv(tick, 40))(col) = getDrawValue(col, pos)

        val nPos = if (isAtSecondTickOfAdd(instr, isSecond)) pos + instr.split(" ")(1).toInt else pos
        drawUntil(rest, tick + 1, nPos, !isAtSecondTickOfAdd(instr, isSecond))
      }
    }

    def isAtSecondTickOfAdd(instr: String, second: Boolean): Boolean = {
      !instr.equals("noop") && second
    }

    def getDrawValue(col: Int, pos: Int): Char = {
      if (List(pos - 1, pos, pos + 1).contains(col)) '#'
      else '.'
    }

    drawUntil(input, 0, 1, false)
  }

  println(findSignalStrength(linesE).map(e => e._1 * e._2).sum)
  println(findSignalStrength(lines).map(e => e._1 * e._2).sum)
  println("------------------------------------------")
  println(draw(linesE, crtE).map(_.mkString("")).mkString("","\n",""))
  println("------------------------------------------")
  println(draw(lines, crt).map(_.mkString("")).mkString("","\n",""))
}
