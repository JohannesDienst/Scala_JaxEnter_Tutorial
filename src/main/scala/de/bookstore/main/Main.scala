package de.bookstore.main

import de.bookstore.main.Lib.PublicationClassicBook

object Main {

  import Lib.findAvailableClassic
  import Lib.findAvailable

  def main(args: Array[String]): Unit = {
    // Usage of adapter pattern
    val book0 = new Book("Clean Code", "Uncle Bob", 3826655486L)
    val book1 = new Book("Code Complete 2", "Steve McConnell", 735619670L, 0)
    val book2 = new Book("The Pragmatic Programmer", "Andrew Hunt", 3446223096L, 0)

    val booksClassic = List(PublicationClassicBook(book0),
      PublicationClassicBook(book1), PublicationClassicBook(book2))
    println(findAvailableClassic(booksClassic))

    // Usage of Type Classes
    val books = List(book0, book1, book2)
    println(findAvailable(books))

    val numbers = List(27, 42)
    // ERROR: could not find implicit value for parameter ev: de.bookstore.main.Lib.Publication[Int]
    // findAvailable(numbers)

    implicit object PublicationEbook extends Lib.Publication[EBook] {
      def getQuantity(eb: EBook): Int = 1
    }

    val ebook0 = new EBook("Clean Code", "Uncle Bob", 3826655486L)
    val ebook1 = new EBook("Code Complete 2", "Steve McConnell", 735619670L, List(), 0)
    val ebook2 = new EBook("The Pragmatic Programmer", "Andrew Hunt", 3446223096L)
    val ebooks = List(ebook0, ebook1, ebook2)
    println(findAvailable(ebooks))

    // Examples for Orderings
    import scala.util.Sorting
    import scala.math.Ordering
    val pairs = Array(("a", 5, 2), ("c", 3, 1), ("b", 1, 3))

    // sort by 2nd element
    println(Sorting.quickSort(pairs)(Ordering.by[(String, Int, Int), Int](_._2)))
    println(pairs.toList)

    // sort by the 3rd element, then 1st
    Sorting.quickSort(pairs)(
      Ordering[(Int, String)].on[(String, Int, Int)]((x1) => (x1._3, x1._1)))
    println(pairs.toList)

    // Ordering provided by scala.math.Ordering
    val aList = List(27, 42, 7, 9, 3)
    println(aList.sorted)

    // Override implicit Ordering from scala.math.Ordering
    implicit object Int extends Ordering[Int] {
      def compare(x: Int, y: Int) =
        if (x < y) 1
        else if (x == y) 0
        else -1
    }
    println(aList.sorted)

    // Write your own implicit
    implicit def orderBooks[A <: Book]: Ordering[A] =
      Ordering.by(b => (b.quantity))
    println(books.sorted)
  }
}