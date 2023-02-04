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

package net.kogics.rithica.addition

import java.util.ArrayList
import java.util.List
import java.util.Random

import net.kogics.rithica.core.Carrys
import net.kogics.rithica.core.VInt

/**
 * Primary ctor
 * @param numbers The list of numbers to be added
 */
class GameModel(val numbers: List[VInt]) {

    /**
     * Secendory ctor
     * @param n: number of numbers in the model
     * @param size: max number size
     */
    def this(n: Int, size: Int, c: VInt.Complexity) = this({
            val nums = new ArrayList[VInt]
            val rand = new Random
            for (i <- 1 to n) {
                nums.add(VInt(size, rand, c))
            }
            nums
        })

    val (result, carrys) = doSum

    def doSum: (VInt, Carrys) = VInt.addAll(numbers)

    def isCarry(pos: Int): Boolean = {
        if (carrys(pos).getOrElse(0) > 0) true else false
    }
}
