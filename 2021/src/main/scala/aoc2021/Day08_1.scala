package aoc2021

object Day08_1 extends App {

  val entriesE = loadData("d08e.txt").get
  val entries = loadData("d08.txt").get

  def processEntry(entry: String): Int = {
    def isUnique(e: String): Boolean = {
      val len = e.length
      len == 2 || len == 3 || len == 4 || len == 7
    }
    entry.split(" ").toList.slice(11, 15).count(isUnique)
  }

  def countUnique(elements: List[String]): Int = {
    elements.map(processEntry).sum
  }

  println(countUnique(entriesE))
  println(countUnique(entries))
}
