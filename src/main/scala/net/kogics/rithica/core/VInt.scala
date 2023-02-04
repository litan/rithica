/*
 * Copyright (C) 2008 Lalit Pant <pant.lalit@gmail.com>
 *
 * The contents of this file are subject to the GNU General Public License
 * Version 3 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 */

package net.kogics.rithica.core

import java.util

import net.kogics.rithica.core.Predef._
import net.kogics.rithica.collection.ListJ._
import java.util.{ArrayList, List, Random}

import net.kogics.rithica.collection.Converter._

object VInt {

  sealed abstract class Complexity() { def toInt: Int }
  case object Easy extends Complexity { def toInt = 1 }
  case object Middling extends Complexity { def toInt = 2 }
  case object Hard extends Complexity { def toInt = 3 }

  def reverse[T](l: List[T]): List[T] = {
    val revList = new util.ArrayList(l)
    util.Collections.reverse(revList)
    revList
  }

  def complexityFromInt(c: Int): Complexity = c match {
    case 1 => Easy
    case 2 => Middling
    case 3 => Hard
    case _ => throw new IllegalArgumentException("Ill defined compelixity value: " + c)
  }

  def fill(size: Int, init: Int) = {
    val num = new ArrayList[Int]
    for (i <- 1 to size) {
      num.add(init)
    }
    new VInt(num)
  }

  def apply(revDigits: List[Int]) = new VInt(revDigits)
  def apply(revDigits: Int*) = new VInt(revDigits: _*)
  //    def apply(revDigits: collection.Seq[Int]) = new VInt(revDigits)
  def apply(size: Int, rand: Random, c: Complexity) = new VInt(size, rand, c)

  def fromInt(n: Int): VInt = {
    val newDigs = new ArrayList[Int]
    var rem = 0
    var quot = 0
    var newN = n
    while (newN != 0) {
      quot = newN / 10
      rem = newN % 10
      newDigs.add(rem)
      newN = quot
    }
    VInt(VInt.reverse(newDigs))
  }

  def maxSize(numbers: List[VInt]) = {
    numbers.foldLeft(0) { (sz, vint) => if (sz > vint.size) sz else vint.size }
  }

  def addAll(numbers: List[VInt]): (VInt, Carrys) = {
    val result = new ArrayList[Int]
    val carry = new ArrayList[Option[Int]]
    carry.add(None)

    for (i <- 0 to maxSize(numbers) - 1) {
      val digitSum = numbers.foldLeft(0)((s, vint) => s + vint(i)) + carry(i).getOrElse(0)
      result.add(digitSum % 10)
      val c = digitSum / 10
      if (c == 0) carry.add(None) else carry.add(Some(c))
    }

    if (carry.last.isDefined) result.add(carry.last.get)
    (VInt(VInt.reverse(result)), Carrys(VInt.reverse(carry)))
  }
}

/**
 * An Integer (for use within VLab)
 * The Integer is represented as a List, with the least significant
 * (lesser-place-value) digit first.
 * eg. to represent 123, the list ListJ(1,2,3) comes into the ctor
 * it is stored internally (as digits) as ListJ(3,2,1)
 */
class VInt private (revDigits: List[Int]) {

  val digits: List[Int] = VInt.reverse(revDigits)

  def iterator = digits.iterator

  private def this(size: Int, rand: Random, c: VInt.Complexity) = this({
    val randomNum = new ArrayList[Int]
    for (i <- 1 to size) {
      randomNum.add(c match {
        case VInt.Easy     => rand.nextInt(5)
        case VInt.Middling => rand.nextInt(8)
        case VInt.Hard     => 4 + rand.nextInt(6)
      })
    }
    if (size != 0 && randomNum(0) == 0) randomNum(0) = 1 + rand.nextInt(4)
    randomNum
  })

  private def this(digs: Int*) = this({
    val num = new ArrayList[Int]
    digs.foreach { d => num.add(d) }
    num
  })

  def apply(i: Int): Int = if (i >= 0 && i < digits.size) digits(i) else 0

  def getFromEnd(i: Int): Int = apply(size - 1 - i)

  def size: Int = digits.size

  def toInt: Int = {
    var pos = 1
    digits.foldLeft(0)((a, d) => {
      val ret = a + d * pos
      pos *= 10
      ret
    })
  }

  /**
   * Multiply this VInt with another VInt
   * Return a pair of lists:
   * list of result VInts (for each digit in the mplier)
   * list of Carrys (for each digit in the mplier)
   */
  def *(mplier: VInt): (List[VInt], List[Carrys]) = {
    val res = new ArrayList[VInt]
    val carry = new ArrayList[Carrys]
    mplier.digits.foreach(d => {
      val prod = this * d
      res.add(prod._1)
      carry.add(prod._2)
    })
    (res, carry)
  }

  def *(n: Int): (VInt, Carrys) = {
    checkArg(n >= 0 && n <= 9, "For single digit multiplication of a VInt, the multiplier has to be between 0 and 9. Supplied multiplier: %d", n)
    val newDigs = new ArrayList[Int]
    val carrys = new ArrayList[Option[Int]]
    carrys.add(None)
    digits.foreachWithIdx((d, i) => {
      val x = d * n + carrys(i).getOrElse(0)
      val res = x % 10
      val c = x / 10
      if (c == 0) carrys.add(None) else carrys.add(Some(c))
      newDigs.add(res)
    })
    if (carrys.last.isDefined) newDigs.add(carrys.last.get)
    (VInt(VInt.reverse(newDigs)), Carrys(VInt.reverse(carrys)))
  }

  def shift(n: Int): VInt = {
    val revDs = VInt.reverse(digits)
    for (i <- 1 to n) revDs += 0
    VInt(revDs)
  }

  override def equals(other: Any): Boolean = {
    other match {
      case oint: VInt => toInt == oint.toInt
      case _          => false
    }
  }

  override def toString: String = digits.toString

  def +(other: VInt): (VInt, Carrys) = {
    val result = new ArrayList[Int]
    val carry = new ArrayList[Option[Int]]
    val msize = if (size > other.size) size else other.size
    carry.add(None)

    for (i <- 0 to msize - 1) {
      val digitSum = this(i) + other(i) + carry(i).getOrElse(0)
      result.add(digitSum % 10)
      val c = digitSum / 10
      if (c == 0) carry.add(None) else carry.add(Some(c))
    }

    if (carry.last.isDefined) result.add(carry.last.get)
    (VInt(VInt.reverse(result)), Carrys(VInt.reverse(carry)))
  }

  def -(other: VInt): (VInt, List[Boolean]) = {
    val diff = toInt - other.toInt
    checkArg(!(diff < 0), "arg to '-' (%d) needs to be less than this VInt (%d)", other.toInt, toInt)
    val res = VInt.fromInt(diff)
    val borrows = new ArrayList[Boolean]
    digits.foreachWithIdx { (d, i) =>
      if (d < other(i)) borrows.add(true)
      else {
        if (i > 0 && borrows(i - 1)) {
          if (d - 1 < other(i)) borrows.add(true) else borrows.add(false)
        }
        else borrows.add(false)
      }
    }
    (res, borrows)
  }

  def /(n: VInt): (VInt, Carrys.Remainders) = {
    checkArg(n.size == 1, "Division is supported by a single digit number only. Digits in supplied number: %d", n.size)
    val divisor = n(0)
    val newDigs = new ArrayList[Int]
    val remainders = new ArrayList[Option[Int]]
    remainders.add(None)
    revDigits.foreachWithIdx((d, i) => {
      val x = d + remainders(i).getOrElse(0) * 10
      val rem = x % divisor
      val quot = x / divisor
      if (rem == 0) remainders.add(None) else remainders.add(Some(rem))
      newDigs.add(quot)
    })
    (VInt(newDigs), Carrys(remainders))
  }
}
