package aoc2021

class OceanFloor(x: Int, y: Int) {
  private val _area = Array.ofDim[Int](y + 1, x + 1)

  def markAllStraightLines(cords: List[((Int,Int), (Int,Int))]): Unit = {
    cords.foreach(markStraightLine)
  }

  def markAllLines(cords: List[((Int,Int), (Int,Int))]): Unit = {
    cords.foreach(markAnyLine)
  }

  def findCountOfDangerousAreas: Int = {
    _area.map(line => line.count(_ > 1)).sum
  }

  private def markAnyLine(cord: ((Int,Int), (Int,Int))): Unit = {
    if (cord._1._1 == cord._2._1 || cord._1._2 == cord._2._2) markStraightLine(cord)
    else markDiagonalLine(cord)
  }

  private def markStraightLine(cord: ((Int,Int), (Int,Int))): Unit = {
    val points = generatePointsOnStraightLine(cord._2, cord._1)
    points.foreach(point => _area(point._2)(point._1) += 1)
  }

  private def markDiagonalLine(cord: ((Int, Int), (Int, Int))): Unit = {
    val points = generatePointsOnDiagonalLine(cord._2, cord._1)
    points.foreach(point => _area(point._2)(point._1) += 1)
  }

  private def generatePointsOnStraightLine(start: (Int, Int), end: (Int, Int)): Seq[(Int, Int)] = {
    for {
      x <- Math.min(start._1, end._1) to Math.max(start._1, end._1)
      y <- Math.min(start._2, end._2) to Math.max(start._2, end._2)
    } yield (x, y)
  }

  private def generatePointsOnDiagonalLine(start: (Int, Int), end: (Int, Int)): Seq[(Int, Int)] = {
    val steps = (if (start._1 > end._1) -1 else 1, if (start._2 > end._2) -1 else 1)
    (for {
      x <- (start._1 to end._1).by(steps._1)
      y <- (start._2 to end._2).by(steps._2)
    } yield (x, y))
      .filter(t =>
        if (steps._1 == steps._2) t._1 - t._2 == start._1 - start._2
        else t._1 + t._2 == start._1 + start._2
      )
  }

  override def toString: String = {
    "\n" + _area.map(_.mkString(" ")).mkString("\n")
  }
}
