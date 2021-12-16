package aoc2021

object Day16_1 extends App {

  val hex2Bin: Map[String, String] =
    Map("0" -> "0000", "1" -> "0001", "2" -> "0010", "3" -> "0011",
      "4" -> "0100", "5" -> "0101", "6" -> "0110", "7" -> "0111",
      "8" -> "1000", "9" -> "1001", "A" -> "1010", "B" -> "1011",
      "C" -> "1100", "D" -> "1101", "E" -> "1110", "F" -> "1111")

  val ex1 = "D2FE28"

  def mapToBin(hex: String): String = {
    hex.toCharArray.map(_.toString).map(hex2Bin).foldLeft("")(_+_)
  }
  
  def parsePacked(packet: String): (Int, Int, String) = {
    (0,0,"")
  }

  println(mapToBin(ex1))
  println("110100101111111000101000")

}
