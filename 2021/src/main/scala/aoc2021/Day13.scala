package aoc2021

object Day13 extends App {

  val pointsE = loadData("d13e.txt").get
  val points = loadData("d13.txt").get

  def createArray(points: List[String], maxY: Int, maxX: Int): Array[Array[String]] = {
    val markings = points.map(_.split(",")).map(a => (a(0).toInt, a(1).toInt))
    val result = Array.ofDim[String](maxY + 1, maxX + 1)
    result.indices.foreach(row => result(row).indices.foreach(col => result(row)(col) = ","))
    markings.foreach(m => result(m._2)(m._1) = "#")
    result
  }

  def getNewSizes(oldX: Int, oldY: Int, horizontal: Boolean): (Int, Int) = {
    if (horizontal) (oldX, oldY / 2)
    else (oldX / 2, oldY)
  }

  def foldPage(full: Array[Array[String]], line: Int, horizontal: Boolean): Array[Array[String]] = {
    val (newX, newY) = getNewSizes(full(0).length, full.length, horizontal)
    val result = Array.ofDim[String](newY, newX)

    full.indices.foreach(y => full(y).indices.foreach(x => {
      if ((horizontal && y != line) || (!horizontal && x != line)) {
        if (x > newX) {
          if (full(y)(x).equals("#")) result(y)(full(0).length - x - 1) = full(y)(x)
        }
        else if (y > newY) {
          if (full(y)(x).equals("#")) result(full.length - y - 1)(x) = full(y)(x)
        }
        else result(y)(x) = full(y)(x)
      }
    }))
    result
  }

  val folded0E = createArray(pointsE, 14, 10)
  val folded1E = foldPage(folded0E, 7,true)
  val folded2E = foldPage(folded1E, 5,false)

  val folded0 = createArray(points, 894, 1310)
  val folded1 = foldPage(folded0, 655,false)
  val folded2 = foldPage(folded1, 447,true)
  val folded3 = foldPage(folded2, 327,false)
  val folded4 = foldPage(folded3, 223,true)
  val folded5 = foldPage(folded4, 163,false)
  val folded6 = foldPage(folded5, 111,true)
  val folded7 = foldPage(folded6, 81,false)
  val folded8 = foldPage(folded7, 55,true)
  val folded9 = foldPage(folded8, 40,false)
  val folded10 = foldPage(folded9, 27,true)
  val folded11 = foldPage(folded10, 13,true)
  val folded12 = foldPage(folded11, 6,true)

  println(folded1E.map(_.count(_.equals("#"))).sum)
  println(folded1.map(_.count(_.equals("#"))).sum)

  println(folded12.map(_.mkString(" ")).mkString("\n"))

}
