package net.kogics.rithica.collection

import java.util.ArrayList

object ListJ {
  def apply[A](xs: A*): ArrayList[A] = {
    val jlist = new ArrayList[A]
    xs.foreach { x => jlist add x }
    jlist
  }

  def lToAd(l: java.util.List[Double]): Array[Double] = {
    val arr = new Array[Double](l.size)
    var i = 0
    val iter = l.iterator
    while (iter.hasNext) {
      arr(i) = iter.next
      i += 1
    }
    arr
  }

  implicit class ListJ[A](lj: java.util.List[A]) {
    def foreachWithIdx(fn: (A, Int) => Unit) {
      val iter = lj.iterator
      var idx = 0
      while (iter.hasNext) {
        fn(iter.next, idx)
        idx += 1
      }
    }
  }
}

