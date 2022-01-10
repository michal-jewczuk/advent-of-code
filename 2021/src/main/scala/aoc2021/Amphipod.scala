package aoc2021

import scala.annotation.tailrec
import scala.collection.mutable
import scala.util.Random

class Amphipod {
  private val _board = Array.ofDim[String](5,11)
  private var _history: List[(String, (Int, Int), (Int, Int), Int)] = List()

  def this(initial: Set[(String, Int, Int)]) = {
    this()
    (0 to 10).foreach(n => _board(0)(n) = "_")
    (1 to 4).foreach(n => Set(0,1,3,5,7,9,10).foreach(col => _board(n)(col) = "#"))
    initial.foreach(v => _board(v._2)(v._3) = v._1)
  }

  def copy: Amphipod = {
    val copy = new Amphipod()
    copy._history = List.from(this._history)
    this._board.indices.foreach(row => this._board(0).indices.foreach(col => copy._board(row)(col) = this._board(row)(col)))
    copy
  }

  def getCost: Int = {
    _history.map(_._4).sum
  }

  def history: List[(String, (Int, Int), (Int, Int), Int)] = {
    _history
  }

  private val costs: Map[String, Int] = Map("A" -> 1, "B" -> 10, "C" -> 100, "D" -> 1000)

  def move(from: (Int, Int), to: (Int, Int)): Boolean = {
    if (!isMoveValid(from, to)) return false

    val who = _board(from._1)(from._2)
    _board(from._1)(from._2) = "_"
    _board(to._1)(to._2) = who
    _history = _history :+ (who, from, to, costs(who) * (Math.abs(from._1 - 0) + Math.abs(0 - to._1) + Math.abs(from._2 - to._2)))
    true
  }

  def isMoveValid(from: (Int, Int), to: (Int, Int)): Boolean = {

    def isDMovingBack: Boolean = {
      if (!_board(from._1)(from._2).equals("D")) return true

      if (from._1 == 0) true
      else from._2 - to._2 <= 5 // for example board 1 is most efficient
    }

    def isPathFree: Boolean = {
      (Math.min(from._2, to._2) to Math.max(from._2, to._2)).map((0,_)).filter(_ != from).count(p => !_board(p._1)(p._2).equals("_")) == 0 &&
        (1 to from._1).map((_,from._2)).filter(_ != from).count(p => !_board(p._1)(p._2).equals("_")) == 0 &&
        (1 to to._1).map((_,to._2)).filter(_ != from).count(p => !_board(p._1)(p._2).equals("_")) == 0
    }

    def isHomeRight: Boolean = {
      if (from._1 > 0) return isInEndPos

      val homeColumns = Map("A" -> 2, "B" -> 4, "C" -> 6, "D" -> 8)
      val letter = _board(from._1)(from._2)
      val targetCol = homeColumns(letter)
      if (targetCol != to._2) return false
      if ((1 to 4).map((_,targetCol)).count(l => !_board(l._1)(l._2).equals(letter) && !_board(l._1)(l._2).equals("_")) != 0) return false

      (1 to 4).map((_,targetCol)).count(l => _board(l._1)(l._2).equals(letter)) == 4 - to._1
    }

    def isInEndPos: Boolean = {
      val letter = _board(from._1)(from._2)
      val homeColumns = Map(2 -> "A", 4 -> "B", 6 -> "C", 8 -> "D")
      (from._1 to 4).count(row => _board(row)(from._2).equals(homeColumns(from._2))) != 5 - from._1
    }
    
    isDMovingBack && isPathFree && isHomeRight
  }

  private val topRowValidPos: Set[(Int, Int)] = Set((0,0), (0,1), (0,3), (0,5), (0,7), (0,9), (0,10))
  private val movesFromTopRow: Map[String, Set[(Int, Int)]] = Map(
    "A" -> Set((1,2), (2,2), (3,2), (4,2)),
    "B" -> Set((1,4), (2,4), (3,4), (4,4)),
    "C" -> Set((1,6), (2,6), (3,6), (4,6)),
    "D" -> Set((1,8), (2,8), (3,8), (4,8))
  )

  def getAllValidMoves: Set[((Int, Int), (Int, Int))] = {
    @tailrec
    def findFirstNonEmptyInCol(colNum: Int, row: Int): (Int, Int) = {
      if (row > 4) (-1,-1)
      else if (!_board(row)(colNum).equals("_")) (row, colNum)
      else findFirstNonEmptyInCol(colNum, row + 1)
    }

    val moves: mutable.Set[((Int,Int), (Int,Int))] = mutable.Set()

    topRowValidPos.foreach(pos => if (!_board(pos._1)(pos._2).equals("_")) {
      movesFromTopRow(_board(pos._1)(pos._2)).foreach(to => moves.addOne((pos, to)))
    })

    Set(2,4,6,8).foreach(col => {
      val from = findFirstNonEmptyInCol(col, 1)
      if (from != (-1,-1)) {
        topRowValidPos.foreach(to => moves.addOne((from, to)))
      }
    })

    moves.filter(m => isMoveValid(m._1, m._2)).toSet
  }

  def isFinished: Boolean = {
    _board(1)(2).equals("A") && _board(2)(2).equals("A") && _board(3)(2).equals("A") && _board(4)(2).equals("A") &&
    _board(1)(4).equals("B") && _board(2)(4).equals("B") && _board(3)(4).equals("B") && _board(4)(4).equals("B") &&
    _board(1)(6).equals("C") && _board(2)(6).equals("C") && _board(3)(6).equals("C") && _board(4)(6).equals("C") &&
    _board(1)(8).equals("D") && _board(2)(8).equals("D") && _board(3)(8).equals("D") && _board(4)(8).equals("D")
  }

  override def toString: String = {
    "\n" + _board.map(_.mkString(" ")).mkString("\n")
    + "\nHistory:\n" + _history
  }
}
