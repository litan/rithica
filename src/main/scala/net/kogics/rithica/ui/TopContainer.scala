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

package net.kogics.rithica.ui

import java.awt._
import java.awt.event._
import javax.swing._

import net.kogics.rithica.core._
import net.kogics.rithica.core.Predef._

trait TopContainer extends JPanel {

  type MyGameParams <: CoreGameParams
  type MyControlPanel <: ControlPanel
  type MyMainPanel <: JPanel with VPanel

  val meta1 = "Version: 090208-2               "
  val meta2 = "%s.%s.%s" format ("www", NameObfuscator.Kogics, "net")
  val meta = meta1 + meta2

  val gameName: String

  val frame: Container

  var savedCp: MyControlPanel
  var acp: MyControlPanel
  var ap: MyMainPanel = _
  val statusBar = new VStatusBar

  setBackground(Color.white)

  def printScore(user0: String) {
  }

  def addMetaBar(row: Int) {
    val vb = new VLabel
    vb.setText(meta)
    vb.setBackground(ColorConstants.CPBackground)
    add(vb, "%d, %d, %d, %d, F, C" format (1, row, 3, row))
  }

  //  def initCustomComponents(aparams: MyGameParams): VPanel

  def initComponents(aparams: MyGameParams) {
    removeAll
    acp = createCp(aparams)
    savedCp = acp
    ap = createMainPanel(aparams)
    add(acp, "%d, %d, %d, %d, F, F" format (1, 1, 1, 1))
    add(ap, "%d, %d, %d, %d, F, F" format (3, 1, 3, 1))
    //    addMetaBar(3)
    Score.newProblem(aparams.complexity)
    revalidate()
    if (aparams.parTime != StopWatch.NoLimit) {
      // 2.12 compilation refactor
      lazy val t: Timer = new Timer(100, new ActionListener {
        def actionPerformed(e: ActionEvent) {
          statusBar.setMessage("Your time has started. The Stop-watch on the left tells you how much of your allocated time you have used up.")
          t.stop
        }
      })
      t.start
    }
    frame.repaint()
  }

  def createCp(ap: MyGameParams): MyControlPanel

  def createMainPanel(ap: MyGameParams): MyMainPanel

  def onResetParams(ap: MyGameParams) = {
    remove(savedCp)
    val acp = createCp(ap)
    savedCp = acp
    add(acp, "%d, %d, %d, %d, F, F" format (1, 1, 1, 1))
    revalidate()
    repaint()
  }

  def loggedInUser = frame match {
    case applet: JApplet => applet.getParameter("user")
    case _               => ""
  }

  def focusGained() {
    frame.requestFocus
    frame.requestFocusInWindow
    ap.askForFocus
  }
}
