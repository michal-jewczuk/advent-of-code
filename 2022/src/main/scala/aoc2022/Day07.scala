package aoc2022

import scala.annotation.tailrec
import scala.util.Try

object Day07 extends App {

  val linesE = loadStrings("d07e.txt")
  val lines = loadStrings("d07.txt")

  class File(val name: String, val parent: File, val dir: Boolean, var size: Int, var children: List[File]) {
    def addChild(child: File): Unit = {
      this.children = this.children :+ child
    }

    def calcualteSize(): Unit = {
      this.size = children.map(_.size).sum
    }

    //    override def toString = s"\nNode(name=$name, parent=${(if (parent == null) "" else parent.name)}, dir=$dir, size=$size, children=$children)"
    override def toString = s"Node(name=$name, parent=${(if (parent == null) "" else parent.name)}, dir=$dir, size=$size, children=${children.map(_.name)})"
  }

  private def processCommands(input: List[String]): File = {
    @tailrec
    def processUntil(commands: List[String], cwd: File): File = {
      if (commands.isEmpty) backToParent(cwd) // got up the tree to calculate the size of last visited directories
      else processUntil(commands.tail, executeCommand(commands.head, cwd))
    }

    @tailrec
    def backToParent(cwd: File): File = {
      if (cwd.parent == null) cwd
      else backToParent(executeCommand("$ cd ..", cwd))
    }

    def executeCommand(cmd: String, cwd: File): File = {
      cmd.split(" ").toList match {
        case List("$", "ls") => cwd
        case List("$", "cd", "..") => openParent(cwd)
        case List("$", "cd", child: String) => openChild(child, cwd)
        case _ => {
          cwd.addChild(createFile(cmd, cwd))
          cwd
        }
      }
    }

    def createFile(info: String, parent: File): File = {
      val elements = info.split(" ")
      File(elements(1), parent, elements(0).equals("dir"), Try(elements(0).toInt).getOrElse(0), List.empty)
    }

    def openChild(child: String, parent: File): File = {
      parent.children.filter(c => c.name.equals(child)).head
    }

    def openParent(current: File): File = {
      current.calcualteSize()
      current.parent
    }

    processUntil(input.tail, File("/", null, true, 0, List.empty))
  }

  private def findWithSizeLessThan(input: List[String]): Int = {
    @tailrec
    def findUntil(toCheck: List[File], acc: List[File]): List[File] = {
      if (toCheck.isEmpty) acc
      else {
        val cur = toCheck.head
        findUntil(
          toCheck.tail ++ cur.children.filter(_.dir),
          if (cur.size < 100000) acc :+ cur else acc
        )
      }
    }

    findUntil(processCommands(input).children.filter(_.dir), List.empty).map(_.size).sum
  }

  private def findSmallestToDelete(input: List[String]): Int = {
    val disk: File = processCommands(input)
    disk.calcualteSize()
    val freeSpace: Int = 70000000 - disk.size
    val neededSpace: Int = 30000000

    @tailrec
    def findUntil(toCheck: List[File], acc: List[File]): List[File] = {
      if (toCheck.isEmpty) acc
      else {
        val cur = toCheck.head
        findUntil(
          toCheck.tail ++ cur.children.filter(_.dir),
          if (freeSpace + cur.size > neededSpace) acc :+ cur else acc
        )
      }
    }

    findUntil(disk.children.filter(_.dir), List(disk)).map(_.size).sorted.head
  }

  println(findWithSizeLessThan(linesE))
  println(findWithSizeLessThan(lines))
  println("================")
  println(findSmallestToDelete(linesE))
  println(findSmallestToDelete(lines))
}
