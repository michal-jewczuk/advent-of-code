package aoc2022

object Day11 extends App {

  class Monkey(val id: Int, val inspect: Long => Long, val divisible: Int, val ifTrue: Int, val ifFalse: Int, var items: List[Long], val example: Boolean) {
    var inspected: Int = 0
    def grab(item: Long) = items = items :+ item
//    def reduceStress(item: Long): Long = Math.floorDiv(item, 3)  // part I

    val reducer = if (example) 23 * 19 * 17 * 13 else 7 * 2 * 19 * 3 * 13 * 11 * 5 * 17 //part II
    def reduceStress(item: Long): Long = item % reducer // part II

    def inspectItems(): Unit = {
      inspected += items.size
      items.map(inspect).map(reduceStress).foreach(i => {
        val herd = if (example) monkeysE else monkeys
        val m = if (i % divisible == 0) herd(ifTrue) else herd(ifFalse)
        m.grab(i)
      })
      items = List.empty
    }
  }

  val mE0: Monkey = Monkey(0, i => i * 19,23, 2, 3, List(79, 98), true)
  val mE1: Monkey = Monkey(1, i => i + 6, 19, 2, 0, List(54, 65, 75, 74), true)
  val mE2: Monkey = Monkey(2, i => i * i, 13, 1, 3, List(79, 60, 97), true)
  val mE3: Monkey = Monkey(3, i => i + 3, 17, 0, 1, List(74), true)

  val m0: Monkey = Monkey(0, i => i * 19, 7, 6, 2, List(59,74,65,86), false)
  val m1: Monkey = Monkey(1, i => i + 1, 2, 2, 0, List(62, 84, 72, 91, 68, 78, 51), false)
  val m2: Monkey = Monkey(2, i => i + 8, 19, 6, 5, List(78, 84, 96), false)
  val m3: Monkey = Monkey(3, i => i * i, 3, 1, 0, List(97, 86), false)
  val m4: Monkey = Monkey(4, i => i + 6, 13, 3, 1, List(50), false)
  val m5: Monkey = Monkey(5, i => i * 17, 11, 4, 7, List(73, 65, 69, 65, 51), false)
  val m6: Monkey = Monkey(6, i => i + 5, 5, 5, 7, List(69, 82, 97, 93, 82, 84, 58, 63), false)
  val m7: Monkey = Monkey(7, i => i + 3, 17, 3, 4, List(81, 78, 82, 76, 79, 80), false)

  val monkeysE = List(mE0,mE1,mE2,mE3)
  val monkeys = List(m0,m1,m2,m3,m4,m5,m6,m7)

  private def processOneRound(critters: List[Monkey]): List[Monkey] = {
    critters.foreach(m => m.inspectItems())
    critters
  }

  private def procesNRounds(critters: List[Monkey], count: Int): Long = {
    (1 to count).foreach(i => processOneRound(critters))
    val result = critters.map(m => m.inspected).sorted.reverse
    result.head.toLong * result.tail.head.toLong
  }

  // part I
//  println(procesNRounds(monkeysE, 20))
//  println(procesNRounds(monkeys, 20))

  // part II
  println(procesNRounds(monkeysE, 10000))
  println(procesNRounds(monkeys, 10000))
}
