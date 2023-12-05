package aoc2023

import scala.annotation.tailrec

object Day05 extends App {

  private val seedsE: List[Long] = List(79, 14, 55, 13)
  private val seeds2E: List[(Long, Long)] = List((79, 14), (55, 13))
  private val seeds: List[Long] = List(858905075, 56936593, 947763189, 267019426, 206349064, 252409474, 660226451, 92561087, 752930744, 24162055, 75704321, 63600948, 3866217991L, 323477533, 3356941271L, 54368890, 1755537789, 475537300, 1327269841, 427659734)
  private val seeds2: List[(Long, Long)] = List((858905075, 56936593), (947763189, 267019426), (206349064, 252409474), (660226451, 92561087), (752930744, 24162055), (75704321, 63600948), (3866217991L, 323477533), (3356941271L, 54368890), (1755537789, 475537300), (1327269841, 427659734))
  private val linesStS = loadData("d05sts.txt").get
  private val linesStF = loadData("d05stf.txt").get
  private val linesFtW = loadData("d05ftw.txt").get
  private val linesWtL = loadData("d05wtl.txt").get
  private val linesLtT = loadData("d05ltt.txt").get
  private val linesTtH = loadData("d05tth.txt").get
  private val linesHtL = loadData("d05htl.txt").get

  private val seedToSoilE: List[(Long, Long, Long)] = List((50, 98, 2), (52, 50, 48))
  private val soilToFertilizerE: List[(Long, Long, Long)] = List((0, 15, 37), (37, 52, 2), (39, 0, 15))
  private val fertilizerToWaterE: List[(Long, Long, Long)] = List((49, 53, 8), (0, 11, 42), (42, 0, 7), (57, 7, 4))
  private val waterToLightE: List[(Long, Long, Long)] = List((88, 18, 7), (18, 25, 70))
  private val lightToTempE: List[(Long, Long, Long)] = List((45, 77, 23), (81, 45, 19), (68, 64, 13))
  private val tempToHumE: List[(Long, Long, Long)] = List((0, 69, 1), (1, 0, 69))
  private val humToLocE: List[(Long, Long, Long)] = List((60, 56, 37), (56, 93, 4))

  private val seedToSoil = extractMappings(linesStS)
  private val soilToFertilizer = extractMappings(linesStF)
  private val fertilizerToWater = extractMappings(linesFtW)
  private val waterToLight = extractMappings(linesWtL)
  private val lightToTemp = extractMappings(linesLtT)
  private val tempToHum = extractMappings(linesTtH)
  private val humToLoc = extractMappings(linesHtL)

  private val mappingsE = List(seedToSoilE, soilToFertilizerE, fertilizerToWaterE, waterToLightE, lightToTempE, tempToHumE, humToLocE)
  private val mappings = List(seedToSoil, soilToFertilizer, fertilizerToWater, waterToLight, lightToTemp, tempToHum, humToLoc)

  private def extractMappings(lines: List[String]): List[(Long, Long, Long)] = {
    @tailrec def extractAcc(toProcess: List[String], acc: List[(Long, Long, Long)]): List[(Long, Long, Long)] = {
      if(toProcess.isEmpty) acc
      else {
        val elems = toProcess.head.split(" ")
        extractAcc(toProcess.tail, acc :+ (elems(0).toLong, elems(1).toLong, elems(2).toLong))
      }
    }
    extractAcc(lines, List.empty)
  }

  private def seedToLocation(source: Long, mappings: List[List[(Long, Long, Long)]], reverse: Boolean): Long = {
    @tailrec def seedToLocationAcc(toProcess: List[List[(Long, Long, Long)]], acc: Long): Long = {
      if (toProcess.isEmpty) acc
      else seedToLocationAcc(toProcess.tail, getMapping(acc, toProcess.head, reverse: Boolean))
    }

    seedToLocationAcc(mappings, source)
  }

  private def getMapping(source: Long, mapping: List[(Long, Long, Long)], reverse: Boolean): Long = {
    @tailrec def getMappingRec(toProcess: List[(Long, Long, Long)]): Long = {
      if (toProcess.isEmpty) source
      else {
        val sourceN = if (reverse) toProcess.head._1 else toProcess.head._2
        val destN = if (reverse) toProcess.head._2 else toProcess.head._1
        val diff = source - sourceN
        if (diff >= 0 && diff <= toProcess.head._3) destN + diff
        else getMappingRec(toProcess.tail)
      }
    }

    getMappingRec(mapping)
  }

  private def isValidSeed(seed: Long, seeds: List[(Long, Long)]): Boolean = {
    @tailrec def isValidRec(toProcess: List[(Long, Long)]): Boolean = {
      if (toProcess.isEmpty) false
      else {
        if (seed >= toProcess.head._1 && seed < toProcess.head._1 + toProcess.head._2) true
        else isValidRec(toProcess.tail)
      }
    }

    isValidRec(seeds)
  }

  private def calculateMinLoc(seeds: List[(Long, Long)], mappings: List[List[(Long, Long, Long)]]): Long = {
    @tailrec def calcAcc(acc: Long): Long = {
      if (isValidSeed(seedToLocation(acc, mappings, true), seeds)) acc
      else calcAcc(acc + 1)
    }

    calcAcc(1L)
  }

  println(seedsE.map(seedToLocation(_, mappingsE, false)).min)
  println(seeds.map(seedToLocation(_, mappings, false)).min) //173706076
  println("=======================")
  println(calculateMinLoc(seeds2E, mappingsE.reverse))
  println(calculateMinLoc(seeds2, mappings.reverse)) //11611182
}
