object DiamondPattern {
  def main(args: Array[String]): Unit = {
    val size = 5 // Size of the diamond

    printDiamond(size)
  }

  private def printDiamond(size: Int): Unit = {
    printUpperHalf(size)
    printLowerHalf(size)
  }

  private def printUpperHalf(size: Int): Unit = {
    for (i <- 1 to size) {
      val spaces = " " * (size - i)
      val stars = "*" * (2 * i - 1)
      println(spaces + stars)
    }
  }

  private def printLowerHalf(size: Int): Unit = {
    for (i <- (size - 1) to 1 by -1) {
      val spaces = " " * (size - i)
      val stars = "*" * (2 * i - 1)
      println(spaces + stars)
    }
  }
}
