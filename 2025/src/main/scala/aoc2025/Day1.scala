package aoc2025

import scala.annotation.tailrec
import scala.collection.immutable.List

object Day1 extends App {
  private val linesE = loadData("d01e.txt").get
  private val lines = loadData("d01.txt").get

  private def extractLists(lines: List[String]): List[(Char, Int)] = {
    @tailrec
    def extractUntil(lines: List[String], result: List[(Char, Int)]): List[(Char, Int)] = {
      if (lines.isEmpty) result
      else {
        val direction = lines.head.charAt(0);
        val count = lines.head.substring(1).toInt;
        extractUntil(lines.tail, result :+ (direction, count))
      }
    }
    extractUntil(lines, List.empty)
  }

  private def findPassword(rotations: List[(Char, Int)]): Int = {
    @tailrec
    def findUntil(left: List[(Char, Int)], pass: Int, current: Int): Int = {
      if (left.isEmpty) pass
      else {
        val rot = left.head._2 % 100;
        var newC = 0;
        var passInc = 0;
        if (left.head._1 == 'R') {
          newC = (current + rot) % 100;
        } else {
          newC = (100 + current - rot) % 100;
        }
        if (newC == 0)  {
          passInc = 1;
        }
        findUntil(left.tail, pass + passInc, newC)
      }
    }
    findUntil(rotations, 0, 50)
  }

  private def findPassword0x43(rotations: List[(Char, Int)]): Int = {
    @tailrec
    def findUntil(left: List[(Char, Int)], pass: Int, current: Int): Int = {
      if (left.isEmpty) pass
      else {
        val roll = (left.head._2 - left.head._2 % 100) / 100; // roll through 0 while rotating more than 100
        val rot = left.head._2 % 100;
        var newC = 0;
        var passInc = 0;
        if (left.head._1 == 'R') {
          newC = current + rot;
          if (newC > 100) {
            passInc = 1;
          }
        } else {
          newC = current - rot;
          if (newC < 0 && current > 0) {
            passInc = 1;
          }
          newC += 100;
        }
        if (newC % 100 == 0) {
          passInc += 1;
        }
        findUntil(left.tail, pass + passInc + roll, newC % 100)
      }
    }
    findUntil(rotations, 0, 50)
  }

  println(findPassword(extractLists(linesE)));
  println(findPassword(extractLists(lines)));
  println("===============")
  println(findPassword0x43(extractLists(linesE)));
  println(findPassword0x43(extractLists(lines)));
}