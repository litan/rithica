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

package net.kogics
package rithica
package multiplication

import java.util.List

import collection.ListJ

class HotRowColCalculator(startRow: Int, numCols: Int) {

    def calculate(row: Int, col: Int, carryOffset: Option[Int]): List[(Int, Int)] = {
        if (carryOffset.isDefined) {
            ListJ((3, numCols - carryOffset.get), (2, col+1))
        }
        else {
            ListJ((3, numCols - (row-startRow)), (2, col + (row-startRow)))
        }
    }
}
