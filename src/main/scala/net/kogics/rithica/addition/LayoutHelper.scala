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
package addition

import javax.swing._
import net.kogics.rithica.core._
import java.util.{List, ArrayList}
import ui.LayoutConstants._

class LayoutHelper(container: JPanel, nums: Int, digits: Int)
extends net.kogics.rithica.ui.LayoutHelper(container) {

    override protected def numColumns: Int = digits+1

    override protected def rowSpec: List[Double] = {
        val numRows = nums + 5 // extra 5: carry, separator, result, filler, status
        val rowSpec = new ArrayList[Double]
        rowSpec.add(b)
        for (i <- 1 until numRows) {
            rowSpec.add(p)
            rowSpec.add(vg)
        }
        rowSpec.add(p)
        rowSpec.add(b)
        rowSpec
    }
}
