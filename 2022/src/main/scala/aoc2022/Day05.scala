package aoc2022

import scala.collection.mutable

object Day05 extends App {

  val linesE = loadStrings("d05e.txt")
  val lines = loadStrings("d05.txt")

  val cratesE = Map(
    1 -> List("N", "Z"),
    2 -> List("D", "C", "M"),
    3 -> List("P")
  )

  val crates = Map(
    1 -> List("T", "Q", "V", "C", "D", "S", "N"),
    2 -> List("V", "F", "M"),
    3 -> List("M", "H", "N", "P", "D", "W", "Q", "F"),
    4 -> List("F", "T", "R", "Q", "D"),
    5 -> List("B", "V", "H", "Q", "N", "M", "F", "R"),
    6 -> List("Q", "W", "P", "N", "G", "F", "C"),
    7 -> List("T", "C", "L", "R", "F", "W"),
    8 -> List("S", "N", "Z", "T"),
    9 -> List("N", "H", "Q", "R", "J", "D", "S", "M")
  )

  private def extractMoves(input: List[String]): List[(Int, Int, Int)] = {
    def extract(line: String): (Int, Int, Int) = {
      val e1 = line.substring(5, 7).trim.toInt
      val e2 = line.split("from ")(1).substring(0, 1).toInt
      val e3 = line.split("to ")(1).trim.toInt

      (e1, e2, e3)
    }

    input.map(extract)
  }

  trait Mover {
    def moveCrates(move: (Int, Int, Int), stack: mutable.Map[Int, List[String]]): mutable.Map[Int, List[String]]
  }

  implicit object CrateMover9000 extends Mover {
    override def moveCrates(move: (Int, Int, Int), stack: mutable.Map[Int, List[String]]): mutable.Map[Int, List[String]] = {
      (0 until move._1).foreach(i => {
        stack(move._3) = stack(move._2).head +: stack(move._3)
        stack(move._2) = stack(move._2).tail
      })
      stack
    }
  }

  object CrateMover9001 extends Mover {
    override def moveCrates(move: (Int, Int, Int), stack: mutable.Map[Int, List[String]]): mutable.Map[Int, List[String]] = {
      stack(move._3) = stack(move._2).slice(0, move._1) ++ stack(move._3)
      stack(move._2) = stack(move._2).slice(move._1, stack(move._2).length)
      stack
    }
  }

  private def moveCrates(input: List[String], stock: Map[Int, List[String]])(implicit mover: Mover): String = {
    val tmp = mutable.Map[Int, List[String]]()
    tmp ++= stock
    extractMoves(input).foreach(mover.moveCrates(_, tmp))
    (1 to tmp.keySet.size).map(i => tmp(i).head).reduceRight(_ + _)
  }

  println(moveCrates(linesE, cratesE))
  println(moveCrates(lines, crates))
  println("================")
  println(moveCrates(linesE, cratesE)(CrateMover9001))
  println(moveCrates(lines, crates)(CrateMover9001))
}
