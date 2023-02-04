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

import java.awt.Container
import javax.swing.JPanel
import net.kogics.rithica.core.VInt
import info.clearthought.layout.TableLayout
import ui.LayoutConstants.b
import ui.LayoutConstants.p
import ui.LayoutConstants.smallG
import ui.StopWatch
import ui.VPanel
import net.kogics.rithica.ui.VStatusBar

class TopContainer(val frame: Container) extends JPanel with ui.TopContainer {

  type MyGameParams = GameParams
  type MyControlPanel = ControlPanel
  type MyMainPanel = MainPanel

  val gameName = "The Kogics Division Game"

  val rowSpec = Array(b, p, smallG, p, b)
  val colSpec = Array(b, p, smallG, p, b)

  val lout = new TableLayout(colSpec, rowSpec)
  setLayout(lout)

  var savedCp: MyControlPanel = null
  var acp: MyControlPanel = null

  initComponents(new GameParams {
    override val digits = 4
    override val complexity = VInt.Middling
    override val parTime = StopWatch.NoLimit
    override val baseTime = 30L
    override val baseD = 2
    override val baseC = VInt.Middling
  })

  def onSumChange(gp: GameParams) = {
    initComponents(gp)
    ap.askForFocus
  }

  def createMainPanel(ap: MyGameParams): MyMainPanel = {
    MainPanel(ap.digits, ap.complexity, frame, acp, statusBar)
    // MainPanel(VInt(2,1,6,0,2,4), VInt(4), frame, acp)
  }

  def createCp(ap: MyGameParams): MyControlPanel = new ControlPanel(this, ap, statusBar)
}
