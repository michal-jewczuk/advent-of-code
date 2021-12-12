package aoc2021

import scala.annotation.tailrec
import scala.collection.mutable

object Day12_1 extends App {

  val dataE1 = loadData("d12e1.txt").get
  val dataE2 = loadData("d12e2.txt").get
  val dataE3 = loadData("d12e3.txt").get
  val data = loadData("d12.txt").get

  def convertToMap(connections: List[String]): Map[String, Set[String]] = {
    val tmp: mutable.Map[String, Set[String]] = mutable.Map("start" -> Set(), "big" -> Set())

    def addToMap(elems: Array[String]): Unit = {
      val first = elems(0)
      val second = elems(1)
      if (first.equals("start")) tmp += "start" -> (tmp("start") + second)
      else if (second.equals("start")) tmp += "start" -> (tmp("start") + first)
      else {
        tmp += first -> (tmp.getOrElse(first , Set()) + second)
        tmp += second -> (tmp.getOrElse(second, Set()) + first)
        if (first(0).toInt <= 90) tmp += "big" -> (tmp("big") + first)
        if (second(0).toInt <= 90) tmp += "big" -> (tmp("big") + second)
      }
    }

    connections.foreach(con => {
      addToMap(con.split("-"))
    })

    tmp.view.toMap
  }

  def countPaths(entries: List[String]): Set[List[String]] = {
    val connections: Map[String, Set[String]] = convertToMap(entries)

    def appendElement(path: List[String], element: String): List[String] = {
      if (path.head.equals("end")) path
      else if (!path.contains(element) || connections("big").contains(element)) element +: path
      else List()
    }

    def extendPaths(paths: Set[List[String]]): Set[List[String]] = {
      var result: Set[List[String]] = Set()
      paths.foreach(path =>
        connections(path.head).foreach(cave => {
          val newPath = appendElement(path, cave)
          if (newPath.nonEmpty) result = result + newPath
        })
      )
      result
    }

    @tailrec
    def findUntilAllComplete(paths: Set[List[String]]): Set[List[String]] = {
      if (paths.forall(_.head.equals("end"))) paths
      else findUntilAllComplete(extendPaths(paths))
    }

    findUntilAllComplete(Set(List("start")))
  }

  println(countPaths(dataE1).size)
  println(countPaths(dataE2).size)
  println(countPaths(dataE3).size)
  println(countPaths(data).size)
}
