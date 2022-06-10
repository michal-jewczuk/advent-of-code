package aoc2021

import scala.annotation.tailrec

object Day16_1 extends App {

  val hex2Bin: Map[String, String] =
    Map("0" -> "0000", "1" -> "0001", "2" -> "0010", "3" -> "0011",
      "4" -> "0100", "5" -> "0101", "6" -> "0110", "7" -> "0111",
      "8" -> "1000", "9" -> "1001", "A" -> "1010", "B" -> "1011",
      "C" -> "1100", "D" -> "1101", "E" -> "1110", "F" -> "1111")

  val ex1 = "D2FE28"
  val ex2 = "38006F45291200"
  val ex3 = "EE00D40C823060"
  val ex4 = "8A004A801A8002F478"
  val ex5 = "620080001611562C8802118E34"
  val ex6 = "C0015000016115A2E0802F182340"
  val ex7 = "A0016C880162017C3686B18A3D4780"
  val input = "A052E04CFD9DC0249694F0A11EA2044E200E9266766AB004A525F86FFCDF4B25DFC401A20043A11C61838600FC678D51B8C0198910EA1200010B3EEA40246C974EF003331006619C26844200D414859049402D9CDA64BDEF3C4E623331FBCCA3E4DFBBFC79E4004DE96FC3B1EC6DE4298D5A1C8F98E45266745B382040191D0034539682F4E5A0B527FEB018029277C88E0039937D8ACCC6256092004165D36586CC013A008625A2D7394A5B1DE16C0E3004A8035200043220C5B838200EC4B8E315A6CEE6F3C3B9FFB8100994200CC59837108401989D056280803F1EA3C41130047003530004323DC3C860200EC4182F1CA7E452C01744A0A4FF6BBAE6A533BFCD1967A26E20124BE1920A4A6A613315511007A4A32BE9AE6B5CAD19E56BA1430053803341007E24C168A6200D46384318A6AAC8401907003EF2F7D70265EFAE04CCAB3801727C9EC94802AF92F493A8012D9EABB48BA3805D1B65756559231917B93A4B4B46009C91F600481254AF67A845BA56610400414E3090055525E849BE8010397439746400BC255EE5362136F72B4A4A7B721004A510A7370CCB37C2BA0010D3038600BE802937A429BD20C90CCC564EC40144E80213E2B3E2F3D9D6DB0803F2B005A731DC6C524A16B5F1C1D98EE006339009AB401AB0803108A12C2A00043A134228AB2DBDA00801EC061B080180057A88016404DA201206A00638014E0049801EC0309800AC20025B20080C600710058A60070003080006A4F566244012C4B204A83CB234C2244120080E6562446669025CD4802DA9A45F004658527FFEC720906008C996700397319DD7710596674004BE6A161283B09C802B0D00463AC9563C2B969F0E080182972E982F9718200D2E637DB16600341292D6D8A7F496800FD490BCDC68B33976A872E008C5F9DFD566490A14"

  def mapToBin(hex: String): String = {
    hex.toCharArray.map(_.toString).map(hex2Bin).foldLeft("")(_+_)
  }

  def getVersionAndId(packet: String): (Int, Int, String) = {
    val version = binaryToInt(packet.substring(0,3))
    val id = binaryToInt(packet.substring(3,6))
    val rest = packet.substring(6)
    (version, id, rest)
  }

  def getBitsForLiteralPacket(bits: String): String = {
    @tailrec
    def readUntil(at: Int): String = {
      if (bits(at).equals('0')) bits.substring(0, at + 5)
      else readUntil(at + 5)
    }
    readUntil(0)
  }

  def parseOperator(packet: String): (List[(Int, Int, String)], Int) = {
    packet(6) match {
      case '0' => parseOperator15(packet)
      case '1' => parseOperator11(packet)
    }
  }

  def parseOperator15(packet: String): (List[(Int, Int, String)], Int) = {
    val (version, id, rest) = getVersionAndId(packet)
    val length = binaryToInt(rest.substring(1,16))

    @tailrec
    def parseUntil(toParse: String, acc: List[(Int, Int, String)], curLen: Int): (List[(Int, Int, String)], Int) = {
      if (curLen >= length) (acc, 22 + curLen)
      else {
        val (version, id, rest) = getVersionAndId(toParse)
        if (id == 4) {
          val bits = getBitsForLiteralPacket(rest)
          parseUntil(toParse.substring(6 + bits.length), acc :+ (version, id, bits), curLen + 6 + bits.length)
        } else {
          val (elements, len) = parseOperator(toParse)
          parseUntil(toParse.substring(len), acc ++ elements, curLen + len)
        }
      }
    }

    parseUntil(rest.substring(16), List((version,id,"")), 0)
  }

  def parseOperator11(packet: String): (List[(Int, Int, String)], Int) = {
    val (version, id, rest) = getVersionAndId(packet)
    val count = binaryToInt(rest.substring(1,12))

    @tailrec
    def parseUntil(toParse: String, acc: List[(Int, Int, String)], curCount: Int, curLen: Int): (List[(Int, Int, String)], Int) = {
      if (curCount == count) (acc, 18 + curLen)
      else {
        val (version, id, rest) = getVersionAndId(toParse)
        if (id == 4) {
          val bits = getBitsForLiteralPacket(rest)
          parseUntil(toParse.substring(6 + bits.length), acc :+ (version, id, bits), curCount + 1, curLen + 6 + bits.length)
        } else {
          val (elements, len) = parseOperator(toParse)
          parseUntil(toParse.substring(len), acc ++ elements, curCount + 1, curLen + len)
        }
      }
    }

    parseUntil(rest.substring(12), List((version,id,"")), 0, 0)
  }

  def buildHierarchy(packet: String): List[(Int, Int, String)] = {
    val (version, id, rest) = getVersionAndId(packet)

    if (id == 4) List((version, id, getBitsForLiteralPacket(rest)))
    else parseOperator(packet)._1
  }

//  println(buildHierarchy(mapToBin(ex1)))
//  println(buildHierarchy(mapToBin(ex2)))
//  println(buildHierarchy(mapToBin(ex3)))
//  println(buildHierarchy(mapToBin(ex4)))
//  println(buildHierarchy(mapToBin(ex5)))
//  println(buildHierarchy(mapToBin(ex6)))
//  println(buildHierarchy(mapToBin(ex7)))
  println(buildHierarchy(mapToBin(input)).map(_._1).sum)
}
