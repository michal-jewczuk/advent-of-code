package aoc2021

object Day05_1 extends App {

  val allE = loadData("d05e.txt").get
    .map(_.split(" -> "))
    .map(a => (a(0), a(1)))
    .map(t => (t._1.split(",").toList.map(_.toInt).match{case List(a,b) => (a,b)},
               t._2.split(",").toList.map(_.toInt).match{case List(a,b) => (a,b)}))

  val all = loadData("d05.txt").get
    .map(_.split(" -> "))
    .map(a => (a(0), a(1)))
    .map(t => (t._1.split(",").toList.map(_.toInt).match{case List(a,b) => (a,b)},
      t._2.split(",").toList.map(_.toInt).match{case List(a,b) => (a,b)}))

  val straightLinesE = allE.filter(t => t._1._1 == t._2._1 || t._1._2 == t._2._2)
  val straightLines = all.filter(t => t._1._1 == t._2._1 || t._1._2 == t._2._2)

  val (xE, yE) = findMaxXAndY(straightLinesE)
  val (x, y) = findMaxXAndY(straightLines)
  val oceanFloorE = new OceanFloor(xE, yE)
  val oceanFloor = new OceanFloor(x, y)
  oceanFloorE.markAllStraightLines(straightLinesE)
  oceanFloor.markAllStraightLines(straightLines)

  println(oceanFloorE.findCountOfDangerousAreas)
  println(oceanFloor.findCountOfDangerousAreas)

  def findMaxXAndY(cords: List[((Int,Int), (Int,Int))]): (Int, Int) = {
    var x = 0
    var y = 0
    cords.foreach(t => {
      if (Math.max(t._1._1, t._2._1) > x) x = Math.max(t._1._1, t._2._1)
      if (Math.max(t._1._2, t._2._2) > y) y = Math.max(t._1._2, t._2._2)
    })
    (x,y)
  }
}
