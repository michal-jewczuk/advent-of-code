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

}
