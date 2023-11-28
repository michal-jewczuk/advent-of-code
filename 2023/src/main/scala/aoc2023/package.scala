import scala.io.Source
import scala.util.{Try, Using}

package object aoc2023 {
  
  def loadData(file: String): Try[List[String]] = {
    Using(Source.fromFile(getClass.getResource("/" + file).toURI)) { reader =>
      Iterator.continually(reader.getLines()).next().toList
    }
  }

  def loadNumerics(file: String): List[Int] = {
    loadData(file).get.map(_.toInt);
  }

  def loadStrings(file: String): List[String] = {
    loadData(file).get
  }

  def binaryToLong(binary: String): Long = {
    var result = 0l
    (0 until binary.length).foreach(pos => {
      if (binary.charAt(pos).equals('1')) result += (1l << (binary.length - pos - 1))
    })

    result
  }
}
