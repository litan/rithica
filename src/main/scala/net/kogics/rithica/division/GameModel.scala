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

package net.kogics.rithica
package division

import java.util.Random
import net.kogics.rithica.core.VInt
import net.kogics.rithica.collection.ListJ


object GameModel {

    def apply(num1: VInt, num2: VInt) = new GameModel(num1, num2)

    def apply(digits: Int, c: VInt.Complexity) = {
        val nums = createNums(digits, c)
        new GameModel(nums._1, nums._2)
    }

    def createNums(digits: Int, c: VInt.Complexity): (VInt, VInt) = {
        val rand = new Random
        val num1 = VInt(digits, rand, c)
        val numt = VInt(1, rand, c)
        val num2 = if (numt(0) == 1) VInt(2) else numt
        (num1, num2)
    }
}

/**
 * Primary ctor
 * @param numbers The list of numbers to be added
 */
class GameModel(num1: VInt, num2: VInt) {
    val numbers = ListJ(num1, num2)
    val (result, remainders) = num1 / num2
}
