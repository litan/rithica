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
package multiplication

import java.util.List
import java.util.Random

import collection.ListJ
import core.Carrys
import core.VInt

object GameModel {

    def apply(mpcand: VInt, mplier: VInt) = new GameModel(mpcand, mplier)

    def apply(digitsMpcand: Int, digitsMplier: Int, c: VInt.Complexity) = {
        val nums = createNums(digitsMpcand, digitsMplier, c)
        new GameModel(nums._1, nums._2)
    }

    def createNums(digitsMpcand: Int, digitsMplier: Int, c: VInt.Complexity): (VInt, VInt) = {
        val rand = new Random
        val mpcand = VInt(digitsMpcand, rand, c)
        val mplier = VInt(digitsMplier, rand, c)
        (mpcand, mplier)
    }
}


/**
 * Primary ctor
 * @param numbers The list of numbers to be added
 */
class GameModel private (val mpcand: VInt, val mplier: VInt) {

    val numbers = ListJ(mpcand, mplier)

    val size = mpcand.size

    val (mults, carrysSeq) = doMult1

    /**
     * Phase 1 of multiplication
     */
    def doMult1: (List[VInt], List[Carrys]) = {
        mpcand * mplier
    }
}
