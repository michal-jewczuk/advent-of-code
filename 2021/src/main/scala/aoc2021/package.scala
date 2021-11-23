import scala.io.Source
import scala.util.{Try, Using}

package object aoc2021 {

  def loadData(file: String): Try[List[String]] = {
    Using(Source.fromFile(getClass.getResource("/"+file).toURI)){reader =>
      Iterator.continually(reader.getLines()).next().toList}
  }

}
