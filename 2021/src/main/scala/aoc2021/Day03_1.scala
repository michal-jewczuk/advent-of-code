package aoc2021

object Day03_1 extends App {

  val reportExample = loadData("d03e.txt").get
  val report = loadData("d03.txt").get

  def countNumberOfZerosAtPosition(lines: List[String], pos: Int): Int = {
    lines.count(line => line.charAt(pos).equals('0'))
  }

  def calculateGammaAndEpsilon(lines: List[String]): (String, String) = {
    var gamma = ""
    var epsilon = ""
    (0 until lines.head.length).foreach(pos => {
      if (countNumberOfZerosAtPosition(lines, pos) * 2 > lines.size) {
        gamma += "0"
        epsilon += "1"
      } else {
        gamma += "1"
        epsilon += "0"
      }
    })

    (gamma, epsilon)
  }

  val reportExampleResult = calculateGammaAndEpsilon(reportExample)
  val reportResult = calculateGammaAndEpsilon(report)

  println(reportExampleResult)
  println(binaryToInt(reportExampleResult._1) * binaryToInt(reportExampleResult._2))
  println(reportResult)
  println(binaryToInt(reportResult._1) * binaryToInt(reportResult._2))

}
