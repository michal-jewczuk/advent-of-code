package aoc2021

import scala.util.Try

object Day20 extends App {

  val dataE = loadData("d20e.txt").get
  val data = loadData("d20.txt").get

  val ieaE = dataE.head
  val inputE = convertToArrayStrings(dataE.slice(2,dataE.length))
  val iea = data.head
  val input = convertToArrayStrings(data.slice(2,data.length))

  def applyImageEnhancement(input: Array[Array[String]], _iea: String, times: Int): Array[Array[String]] = {

    def applyEnh(in: Array[Array[String]], extended: String): Array[Array[String]] = {
      val out: Array[Array[String]] = Array.ofDim[String](in.length + 2, in(0).length + 2)
      out.indices.foreach(row => out(row).indices.foreach(col => out(row)(col) = "."))

      out.indices.foreach(row => out(row).indices.foreach(col => {
        val bin = List((row-1, col-1),(row-1, col),(row-1,col+1),(row, col-1),(row, col),(row,col+1),(row+1, col-1),(row+1, col),(row+1,col+1))
          .map(p => Try(in(p._1 - 1)(p._2 - 1))).map(t => if (t.isSuccess) t.get else extended).reduceRight(_+_)

        val position = binaryToInt(bin.replace('.','0').replace('#','1'))
        out(row)(col) = _iea(position).toString
      }))
      out
    }

    var e1 = applyEnh(input, ".")
    (2 to times).foreach(n => {
      if (n % 2 == 0) e1 = applyEnh(e1, _iea(0).toString)
      else e1 = applyEnh(e1, if (_iea(0).equals('.')) "." else _iea(511).toString)
    })
    e1
  }

  def countHashes(in: Array[Array[String]]): Int = {
    in.indices.map(row => in(row).count(_.equals("#"))).sum
  }

  val enhanced2E = applyImageEnhancement(inputE, ieaE, 2)
  val enhanced2 = applyImageEnhancement(input, iea, 2)

  println(countHashes(enhanced2E))
  println(countHashes(enhanced2))

  val enhanced50E = applyImageEnhancement(inputE, ieaE, 50)
  val enhanced50 = applyImageEnhancement(input, iea, 50)

  println(countHashes(enhanced50E))
  println(countHashes(enhanced50))

//  println(enhanced2E.map(_.mkString(" ")).mkString("\n"))
//  println(enhanced2.map(_.mkString(" ")).mkString("\n"))
}
