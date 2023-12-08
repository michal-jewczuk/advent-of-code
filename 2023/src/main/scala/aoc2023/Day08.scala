package aoc2023

import scala.annotation.tailrec

object Day08 extends App {

  private val linesE1 = loadData("d08e1.txt").get
  private val linesE2 = loadData("d08e2.txt").get
  private val linesE3 = loadData("d08e3.txt").get
  private val lines = loadData("d08.txt").get
  private val pathE1 = "RL".toList
  private val pathE2 = "LLR".toList
  private val pathE3 = "LR".toList
  private val path = "LRRLRRRLRRLRRLRRRLRRLRLLRRRLRRRLRRRLRRRLRRRLRRLLRRRLLRLRRRLRRLRRRLLRRRLRLLRRLRLLRLRLLRRLRRRLRRLLRRRLRRRLRLRRRLRRRLRRRLRRRLRLRRRLLRRRLRRLRRRLRLRRRLRRLRLLLLLRRRLRRRLRRRLRRRLRRLLRLRLRRLRRLLRRRLRRRLRRRLLLRRRLRRRLRRRLRLRRRLLRLRLRRLRRLRRRLRRLRRRLRRRLRRRLRRLLRRRLRRLRRLRLLRRRR".toList

  private def transformData(lines: List[String]): Map[String, (String, String)] = {
    lines.map(_.split(" = ")).map(l => (l(0), (l(1).substring(1,4), l(1).substring(6,9)))).toMap
  }

  private def traversePath(joints: Map[String, (String, String)], directions: List[Char], start: String, allZ: Boolean): Int = {
    val ending = if (allZ) "ZZZ" else "Z"

    @tailrec def traverseAcc(acc: Int, currentJoint: String): Int = {
      if (currentJoint.endsWith(ending)) acc
      else {
        val nextDir = directions(acc % directions.length)
        val nextJoint = if (nextDir == 'L') joints(currentJoint)._1 else joints(currentJoint)._2
        traverseAcc(acc + 1, nextJoint)
      }
    }

    traverseAcc(0, start)
  }

  private def traversePathSim(joints: Map[String, (String, String)], directions: List[Char]): Long = {
    val result = joints.keySet.filter(_.endsWith("A")).map(traversePath(joints, directions, _, false)) ++ Set(directions.size)
    calculateLCM(result.map(_.toLong).toList)
  }

  println(traversePath(transformData(linesE1), pathE1, "AAA", true))
  println(traversePath(transformData(linesE2), pathE2, "AAA", true))
  println(traversePath(transformData(lines), path, "AAA", true)) //16409
  println("===============")
  println(traversePathSim(transformData(linesE3), pathE3))
  println(traversePathSim(transformData(lines), path))  //11795205644011
}
