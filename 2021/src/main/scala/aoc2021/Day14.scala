package aoc2021

import scala.annotation.tailrec
import scala.collection.mutable

object Day14 extends App {

  val dataE = loadData("d14e.txt").get
  val data = loadData("d14.txt").get

  def extractPatternAndFormulas(lines: List[String]): (List[String], Map[String, String]) = {
    val pattern = lines.head.toCharArray.map(_.toString).toList
    val formulas = lines.slice(2, lines.length).map(_.split(" ")).map(a => (a(0), a(2))).toMap
    (pattern, formulas)
  }

  def applyOneStep(formulas: Map[String, String], pairs: Map[String, Long], letters: Map[String, Long]): (Map[String, Long], Map[String, Long])  = {
    val pairsU: mutable.Map[String, Long] = mutable.Map()
    val lettersU: mutable.Map[String, Long] = mutable.Map.from(letters)

    pairs.keys.foreach(currentPair => {
      val newLetter = formulas(currentPair)
      val newPair1 = currentPair(0) + newLetter
      val newPair2 = newLetter + currentPair(1)
      pairsU(newPair1) = pairsU.getOrElse(newPair1, 0l) + pairs(currentPair)
      pairsU(newPair2) = pairsU.getOrElse(newPair2, 0l) + pairs(currentPair)
      lettersU(newLetter) = lettersU.getOrElse(newLetter, 0l) + pairs(currentPair)
    })

    (pairsU.toMap, lettersU.toMap)
  }

  def applyNSteps(formulas: Map[String, String], pairs: Map[String, Long], letters: Map[String, Long], times: Int): (Map[String, Long], Map[String, Long]) = {
    @tailrec
    def extendUntil(pairsAcc: Map[String, Long], lettersAcc: Map[String, Long], counter: Int): (Map[String, Long], Map[String, Long]) = {
      if (counter == 0) (pairsAcc, lettersAcc)
      else {
        val (pairsU, lettersU) = applyOneStep(formulas, pairsAcc, lettersAcc)
        extendUntil(pairsU, lettersU, counter - 1)
      }
    }

    extendUntil(pairs, letters, times)
  }

  def findDiffMostLeastCommon(lines: List[String], times: Int): Long = {
    val (pattern, formulas) = extractPatternAndFormulas(lines)
    val letters = pattern.distinct.map(e => (e, pattern.count(_.equals(e)).toLong)).toMap
    val tmp = (0 until pattern.length - 1).map(n => pattern(n) + pattern(n+1))
    val pairs = tmp.distinct.map(e => (e, tmp.count(_.equals(e)).toLong)).toMap

    val (pairsCount, lettersCount) = applyNSteps(formulas, pairs, letters, times)
    lettersCount.values.max - lettersCount.values.min
  }

  println(findDiffMostLeastCommon(dataE, 10))
  println(findDiffMostLeastCommon(data, 10))

  println(findDiffMostLeastCommon(dataE, 40))
  println(findDiffMostLeastCommon(data, 40))
}
