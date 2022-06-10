package aoc2021

import scala.annotation.tailrec

object Day16_2 extends App {

  val hex2Bin: Map[String, String] =
    Map("0" -> "0000", "1" -> "0001", "2" -> "0010", "3" -> "0011",
      "4" -> "0100", "5" -> "0101", "6" -> "0110", "7" -> "0111",
      "8" -> "1000", "9" -> "1001", "A" -> "1010", "B" -> "1011",
      "C" -> "1100", "D" -> "1101", "E" -> "1110", "F" -> "1111")

  val ex1 = "C200B40A82"
  val ex2 = "04005AC33890"
  val ex3 = "880086C3E88112"
  val ex4 = "CE00C43D881120"
  val ex5 = "D8005AC2A8F0"
  val ex6 = "F600BC2D8F"
  val ex7 = "9C005AC2F8F0"
  val ex8 = "9C0141080250320F1802104A08"
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
    @tailrec def readUntil(at: Int): String = {
      if (bits(at).equals('0')) bits.substring(0, at + 5)
      else readUntil(at + 5)
    }
    readUntil(0)
  }

  def getValueForLiteralPacket(bits: String): Long = {
    @tailrec def parseValue(cur: Int, acc: String): String = {
      if (cur >= bits.length) acc
      else parseValue(cur + 5, acc + bits.substring(cur + 1, cur + 5))
    }
    binaryToLong(parseValue(0, ""))
  }

  def parseOperator(packet: String, level: Int): (List[(Int, Int, Long, Int)], Int) = {
    packet(6) match {
      case '0' => parseOperator15(packet, level)
      case '1' => parseOperator11(packet, level)
    }
  }

  def parseOperator15(packet: String, packetLevel: Int): (List[(Int, Int, Long, Int)], Int) = {
    val (version, id, rest) = getVersionAndId(packet)
    val length = binaryToInt(rest.substring(1,16))

    @tailrec
    def parseUntil(toParse: String, accList: List[(Int, Int, Long, Int)], accLen: Int, level: Int, accValues: List[Long]): (List[(Int, Int, Long, Int)], Int) = {
      if (accLen >= length) ((version, id, computeValue(id, accValues), level) +: accList, 22 + accLen)
      else {
        val (version, id, rest) = getVersionAndId(toParse)
        if (id == 4) {
          val bits = getBitsForLiteralPacket(rest)
          val valueLP = getValueForLiteralPacket(bits)
          parseUntil(toParse.substring(6 + bits.length), accList :+ (version, id, valueLP, level + 1), accLen + 6 + bits.length, level, accValues :+ valueLP)
        } else {
          val (elements, len) = parseOperator(toParse, level + 1)
          parseUntil(toParse.substring(len), accList ++ elements, accLen + len, level, accValues :+ elements.head._3)
        }
      }
    }

    parseUntil(rest.substring(16), List(), 0, packetLevel, List())
  }

  def parseOperator11(packet: String, packetLevel: Int): (List[(Int, Int, Long, Int)], Int) = {
    val (version, id, rest) = getVersionAndId(packet)
    val count = binaryToInt(rest.substring(1,12))

    @tailrec
    def parseUntil(toParse: String, accList: List[(Int, Int, Long, Int)], accCount: Int, accLen: Int, level: Int, accValues: List[Long]): (List[(Int, Int, Long, Int)], Int) = {
      if (accCount == count) ((version, id, computeValue(id, accValues), level) +: accList, 18 + accLen)
      else {
        val (version, id, rest) = getVersionAndId(toParse)
        if (id == 4) {
          val bits = getBitsForLiteralPacket(rest)
          val valueLP = getValueForLiteralPacket(bits)
          parseUntil(toParse.substring(6 + bits.length), accList :+ (version, id, valueLP, level + 1), accCount + 1, accLen + 6 + bits.length, level, accValues :+ valueLP)
        } else {
          val (elements, len) = parseOperator(toParse, level + 1)
          parseUntil(toParse.substring(len), accList ++ elements, accCount + 1, accLen + len, level, accValues :+ elements.head._3)
        }
      }
    }

    parseUntil(rest.substring(12), List(), 0, 0, packetLevel, List())
  }

  def computeValue(id: Int, values: List[Long]): Long = {
    id match {
      case 0 => values.sum
      case 1 => values.product
      case 2 => values.min
      case 3 => values.max
      case 5 => if (values.head > values.tail.head) 1l else 0l
      case 6 => if (values.head < values.tail.head) 1l else 0l
      case 7 => if (values.head == values.tail.head) 1l else 0l
      case _ => 0l
    }
  }

  def buildHierarchy(hex: String): List[(Int, Int, Long, Int)] = {
    val packet = mapToBin(hex)
    val (version, id, rest) = getVersionAndId(packet)

    id match {
      case 4 => List((version, id, getValueForLiteralPacket(rest), 0))
      case _ => parseOperator(packet, 0)._1
    }
  }

  def tree(elements: List[(Int, Int, Long, Int)]): String = {
    @tailrec def getTabs(count: Int, limit: Int, acc: String): String = {
      if (count > limit) acc
      else getTabs(count + 1, limit, acc + " > ")
    }
    elements.map(x => s"\n${getTabs(1, x._4, "")}$x").foldLeft("")(_+_)
  }

  println(tree(buildHierarchy(input)))
}
