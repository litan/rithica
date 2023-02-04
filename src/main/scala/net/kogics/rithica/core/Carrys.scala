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

import net.kogics.rithica.core.Predef._
import net.kogics.rithica.collection.Converter._

import java.util.{List, ArrayList}

object Carrys {

    type Remainders = Carrys

    def apply(revCarrys: List[Option[Int]]): Carrys = new Carrys(revCarrys)

    def apply(revCarrys: Option[Int]*): Carrys = {
        val revC = new ArrayList[Option[Int]]
        revCarrys.foreach {c => revC.add(c)}
        new Carrys(revC)
    }
}

/**
 * Represents carry-overs for an addition
 */
class Carrys private (revCarrys: List[Option[Int]]) {

    val carrys = revCarrys.reverse

    def apply(i: Int): Option[Int] = if (i >= 0 && i < carrys.size) carrys(i) else None

    def getFromEnd(i: Int): Option[Int] = apply(size - 1 - i)

    def size: Int = carrys.size

    def last: Option[Int] = apply(size - 1)

    override def equals(other: Any): Boolean = {
        other match {
            case other:Carrys => carrys == other.carrys
            case _ => false
        }
    }

    override def toString: String = carrys.toString
}
