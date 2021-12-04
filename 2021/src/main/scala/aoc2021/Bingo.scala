package aoc2021

class Bingo {
  private val _base = Array.ofDim[Int](5,5)
  private val _current = Array.ofDim[Int](5,5)

  def base: Array[Array[Int]] = _base
  def current: Array[Array[Int]] = _current

  def this(values: List[String]) = {
    this()
    def processLine(line: String, row: Int): Unit = {
      (0 to 4).foreach(n => {
        val offset = n * 3
        _base(row)(n) = line.substring(offset, offset + 2).trim.toInt
        _current(row)(n) = line.substring(offset, offset + 2).trim.toInt
      })
    }

    (0 to 4).foreach(n => processLine(values(n), n))
  }

  def mark(number: Int): Unit = {
    for (row <- _current) {
      (0 to 4).foreach(n => {
        if (row(n) == number) row(n) = -1
      })
    }
  }

  def isBingo: Boolean = {
    for (row <- _current) {
      if (row sameElements Array(-1, -1, -1, -1, -1)) return true
    }
    (0 to 4).foreach(col => {
      val column = Array(_current(0)(col), _current(1)(col), _current(2)(col), _current(3)(col), _current(4)(col))
      if (column sameElements Array(-1, -1, -1, -1, -1)) return true
    })
    false
  }

  def sumUnmarked: Int = {
    _current.map(row => row.filter(_ != -1).sum).sum
  }

  override def toString: String = {
    "\n" + _current.map(_.mkString(" ")).mkString("\n")
  }
}
