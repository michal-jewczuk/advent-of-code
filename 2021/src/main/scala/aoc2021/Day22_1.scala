package aoc2021

object Day22_1 extends App {

  val linesE1 = loadData("d22e1.txt").get
  val linesE2 = loadData("d22e2.txt").get
  val lines = loadData("d22.txt").get

  val inputsE1 = linesE1.map(process)
  val inputsE2 = linesE2.map(process)
  val inputs = lines.map(process)

  def process(line: String): (String, (Int,Int), (Int, Int), (Int,Int)) = {
    val state = line.substring(0,3).trim
    val elements = line.substring(3).trim.split(",")
      .map(_.substring(2))
      .map(_.split("\\.\\.").toList)
      .map(l => (l.head.toInt, l.tail.head.toInt))

    (state, elements(0), elements(1), elements(2))
  }

  def changeState(lines: List[(String, (Int,Int), (Int, Int), (Int,Int))]): Set[(Int, Int, Int)] = {
    var result: Set[(Int, Int, Int)] = Set()

    def generateCubes(first: (Int, Int), second: (Int, Int), third: (Int, Int)): Set[(Int, Int, Int)] = {
      (for {
        x <- first._1 to first._2 if x >= -50 && x <= 50
        y <- second._1 to second._2 if y >= -50 && y <= 50
        z <- third._1 to third._2 if z >= -50 && z <= 50
      } yield (x,y,z)).toSet
    }

    lines.foreach(line => {
      val (state, x, y, z) = line
      val elements = generateCubes(x,y,z)
      if (state.equals("on")) result = result ++ elements
      else result = result.removedAll(elements)
    })

    result
  }

  println(changeState(inputsE1).size)
  println(changeState(inputsE2).size)
  println(changeState(inputs).size)
}
