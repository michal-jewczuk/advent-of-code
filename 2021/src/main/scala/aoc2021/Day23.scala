package aoc2021

object Day23 extends App {

  val gameE = new Amphipod(Set(("B",1,2), ("D",2,2), ("D",3,2), ("A",4,2),
                                ("C",1,4), ("C",2,4), ("B",3,4), ("D",4,4),
                                ("B",1,6), ("B",2,6), ("A",3,6), ("C",4,6),
                                ("D",1,8), ("A",2,8), ("C",3,8), ("A",4,8)))

  val game = new Amphipod(Set(("C",1,2), ("D",2,2), ("D",3,2), ("C",4,2),
                              ("B",1,4), ("C",2,4), ("B",3,4), ("D",4,4),
                              ("A",1,6), ("B",2,6), ("A",3,6), ("A",4,6),
                              ("D",1,8), ("A",2,8), ("C",3,8), ("B",4,8)))

  def findMinCost(g: Amphipod): Int = {
    def playUntil(current: Amphipod): Int = {
      if (current.isFinished) current.getCost
      else {
        val moves = current.getAllValidMoves.toList
        if (moves.isEmpty) Int.MaxValue
        else {
          moves.map(move => {
            val copy = current.copy
            if (!copy.move(move._1, move._2)) Int.MaxValue
            else playUntil(copy)
          }).min
        }
      }
    }

    playUntil(g)
  }

//  println(findMinCost(gameE))
  println(findMinCost(game))
}
