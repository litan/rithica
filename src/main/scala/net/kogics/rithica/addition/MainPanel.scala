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

import java.awt.Container
import java.util.ArrayList
import java.util.List
import java.util.Random
import net.kogics.rithica.core.VInt
import net.kogics.rithica.ui.RoundedBorderPanel
import net.kogics.rithica.ui.StopWatchController
import net.kogics.rithica.ui.VPanel
import net.kogics.rithica.ui.VStatusBar

class MainPanel(val numbers: List[VInt], topComponent: Container, swc: StopWatchController, val statusBar: VStatusBar)
  extends RoundedBorderPanel with VPanel { mainPanel =>

  def this(nums: Int, digits: Int, c: VInt.Complexity, topComponent: Container, cp: StopWatchController, statusBar: VStatusBar) = this({
    val ns = new ArrayList[VInt]
    val rand = new Random
    for (i <- 1 to nums) {
      ns.add(VInt(digits, rand, c))
    }
    ns
  }, topComponent, cp, statusBar)

  val nums = numbers.size
  val digits = VInt.maxSize(numbers)

  val layouter = new LayoutHelper(this, nums, digits)
  val ap = new AdditionContextAdapter with AdditionComponent 

  val numberRows = ap.numberRows
  val carrysRow = ap.carrysRow
  val resultRow = ap.resultRow

  def askForFocus {
    ap.askForFocus
  }

  class AdditionContextAdapter extends addition.AdditionContext {
    val statusBar = mainPanel.statusBar
    val panel = MainPanel.this
    val layouter: net.kogics.rithica.ui.LayoutHelper = MainPanel.this.layouter
    val swc = MainPanel.this.swc
    val topComponent = MainPanel.this.topComponent
    val carrysRowIdx = 1
    val numbers = MainPanel.this.numbers

  }
}

