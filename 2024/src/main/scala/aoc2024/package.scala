import scala.annotation.tailrec
import scala.io.Source
import scala.util.{Try, Using}

package object aoc2024 {
  def loadData(file: String): Try[List[String]] = {
    Using(Source.fromFile(getClass.getResource("/" + file).toURI)) { reader =>
      Iterator.continually(reader.getLines()).next().toList
    }
  }

  def loadNumerics(file: String): List[Int] = {
    loadData(file).get.map(_.toInt)
  }

  def loadStrings(file: String): List[String] = {
    loadData(file).get
  }

  def binaryToLong(binary: String): Long = {
    var result = 0L
    (0 until binary.length).foreach(pos => {
      if (binary.charAt(pos).equals('1')) result += (1L << (binary.length - pos - 1))
    })

    result
  }

  // inspired by: https://stackoverflow.com/questions/40875537/fp-lcm-in-scala-in-1-line
  def calculateLCM(list: List[Long]): Long = {
    @tailrec def gdc(a: Long, b: Long): Long = {
      if (b == 0) a
      else gdc(b, a % b)
    }

    list.foldLeft(1L)((a, b) => (a / gdc(a, b)) * b)
  }

  //println(linesE.map(l => l.mkString("", "","\n")).mkString("", "",""))
}
