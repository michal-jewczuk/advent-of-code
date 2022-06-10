import scala.io.Source
import scala.util.{Try, Using}

package object aoc2021 {

  def loadData(file: String): Try[List[String]] = {
    Using(Source.fromFile(getClass.getResource("/"+file).toURI)){reader =>
      Iterator.continually(reader.getLines()).next().toList}
  }

  def binaryToInt(binary: String): Int = {
    var result = 0
    (0 until binary.length).foreach(pos => {
      if (binary.charAt(pos).equals('1')) result += (1 << (binary.length - pos - 1))
    })

    result
  }

  def binaryToLong(binary: String): Long = {
    var result = 0l
    (0 until binary.length).foreach(pos => {
      if (binary.charAt(pos).equals('1')) result += (1l << (binary.length - pos - 1))
    })

    result
  }

  def convertToArray(lines: List[String]): Array[Array[Int]] = {
    val result = Array.ofDim[Int](lines.size, lines.head.length)
    lines.indices.foreach(n => {
      result(n) = lines(n).toList.map(_.toString).map(_.toInt).toArray
    })
    result
  }

  def convertToArrayStrings(lines: List[String]): Array[Array[String]] = {
    val result = Array.ofDim[String](lines.size, lines.head.length)
    lines.indices.foreach(n => {
      result(n) = lines(n).toList.map(_.toString).toArray
    })
    result
  }

}
