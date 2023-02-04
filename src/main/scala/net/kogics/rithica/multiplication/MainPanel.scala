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

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Container
import java.awt.Graphics
import java.awt.RenderingHints
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.util.ArrayList
import java.util.List
import java.util.Set

import javax.swing.Timer

import net.kogics.rithica.collection.Converter._

import net.kogics.rithica.collection.ListJ.ListJ
import net.kogics.rithica.ui.CarryTextField
import net.kogics.rithica.ui.HotRowColChanger
import net.kogics.rithica.ui.NumberDisplayField
import net.kogics.rithica.ui.ResultTextField
import net.kogics.rithica.ui.RowCol

import core.Predef.gToG2d
import core.VInt
import ui.ColorConstants
import ui.FieldMarker
import ui.FieldRowListener
import ui.FocusPolicy
import ui.HotRowColDecider
import ui.InputNumberFieldCtx
import ui.InputNumberTextField
import ui.PanelUtils
import ui.RoundedBorderPanel
import ui.RowAwareField
import ui.StopWatchController
import ui.StringConstants
import ui.VPanel
import ui.VStatusBar

object MainPanel {
  val CarrysRowIdx = 5
  def apply(mpcand: VInt, mplier: VInt, topComponent: Container, cp: StopWatchController, statusBar: VStatusBar) = new MainPanel(mpcand, mplier, topComponent, cp, statusBar)
  def apply(digitsMpcand: Int, digitsMplier: Int, c: VInt.Complexity, topComponent: Container, cp: StopWatchController, statusBar: VStatusBar) = {
    val nums = GameModel.createNums(digitsMpcand, digitsMplier, c)
    new MainPanel(nums._1, nums._2, topComponent, cp, statusBar)
  }
}

class MainPanel(val mpcand: VInt, val mplier: VInt, val topComponent: Container, val swc: StopWatchController, val statusBar: VStatusBar)
  extends RoundedBorderPanel with VPanel with FieldRowListener
  with InputNumberFieldCtx with HotRowColDecider with PanelUtils {

  if (mpcand.size < mplier.size) throw new IllegalArgumentException("Multiplicand cannot have lesser number of digits than multiplier")

  val layouter = new LayoutHelper(this, mpcand, mplier)

  val numCols = layouter.numCols

  val model = GameModel(mpcand, mplier)

  val carrysRows = new ArrayList[List[CarryTextField with HotRowColChanger]]
  val multResultRows = new ArrayList[List[ResultTextField with RowCol]]
  val numberRows = new ArrayList[List[NumberDisplayField with RowCol]]

  var focusOrder: List[InputNumberTextField] = null
  var fp: FocusPolicy = null
  var strategy: Strategy = if (mplier.size == 1) new OneDigitMultStrategy else new NDigitMultStrategy

  val hrcCalci = new HotRowColCalculator(strategy.resultStartRow, numCols)

  var carryRowSwitcher: CarryRowSwitcher = null

  initComponents

  def addMultCarrysRows(guiRow: Int): Int = {
    carryRowSwitcher = new CarryRowSwitcher(guiRow)
    model.carrysSeq.foreachWithIdx((carrys, idx) => {
      val carrysRow = new ArrayList[CarryTextField with HotRowColChanger]
      val startCol = rowStartCol(carrys.size)
      for (i <- startCol to numCols) {
        val tf =
          new CarryTextField(this, carrys.getFromEnd(i - startCol)) with HotRowColChanger {
            val row = guiRow
            val col = i
            val carryOffset = Some(idx)
            val hotRowColDecider = MainPanel.this
          }
        tf.setName("%d, %d" format (guiRow, i))
        carrysRow.add(tf)
      }
      carrysRow(0).lastField = true
      carrysRows.add(carrysRow)
    })
    carryRowSwitcher.showRow(0)
    guiRow + 1
  }

  def addNumberRows(guiRow: Int): Int = {
    var gr = guiRow
    model.numbers.foreach(vint => {
      val rowNum = new ArrayList[NumberDisplayField with RowCol]
      val startCol = rowStartCol(vint.size)
      for (i <- startCol to numCols) {
        val tf = new NumberDisplayField with FieldMarker {
          val ctx = MainPanel.this
          val row = gr
          val col = i
        }
        tf.setName("%d, %d" format (gr, i))
        tf.setText(vint.getFromEnd(i - startCol).toString)
        strategy.numberRowField(tf)
        layouter.add(tf, gr, i)
        rowNum.add(tf)
      }
      gr += 1
      numberRows.add(rowNum)
    })
    strategy.multSign(gr - 1, numCols - model.numbers.last.size)
    gr
  }

  def addMultResultRows(guiRow: Int): Int = {
    var gr = guiRow
    for (rr <- 0 until mplier.size) {
      val multResultRow = new ArrayList[ResultTextField with RowCol]
      val vint = model.mults(rr)
      val startCol = rowStartCol(vint.size) - rr
      for (idx <- startCol to numCols - rr) {
        val tf = new ResultTextField(this, vint.getFromEnd(idx - startCol)) with RowAwareField with HotRowColChanger {
          val resultRow = rr
          val rowEventListener = MainPanel.this
          val row = gr
          val col = idx
          val carryOffset = None
          val hotRowColDecider = MainPanel.this
        }
        tf.setName("%d, %d" format (gr, idx))
        layouter.add(tf, gr, idx)
        multResultRow.add(tf)
      }
      multResultRows.add(multResultRow)
      gr += 1
    }
    strategy.plusSign(gr - 1, numCols - model.mults.last.size - (mplier.size - 1))
    gr
  }

  def createAdditionPanel = {
    new AdditionContextAdapter with addition.AdditionComponent
  }

  def addAdditionSubPanel(row: Int) {
    val ap = createAdditionPanel
    topComponent.requestFocus
    topComponent.requestFocusInWindow
    ap.askForFocus
  }

  def initComponents {
    customizeFocusKeys
    strategy.initComponents
  }

  def askForFocus {
    topComponent.setFocusTraversalPolicy(fp)
    fp.getDefaultComponent(null).requestFocusInWindow
  }

  def activateCustomFocus {
    setCustomFocusPolicy
    askForFocus
  }

  def setCustomFocusPolicy {
    val res = new ArrayList[InputNumberTextField]
    val row = multResultRows(carryRowSwitcher.currRow)
    val rli = row.listIterator(row.size)
    val currCarrysRow = carrysRows(carryRowSwitcher.currRow)
    val cli = currCarrysRow.listIterator(currCarrysRow.size)
    while (rli.hasPrevious) {
      val c = cli.previous
      val r = rli.previous
      if (c.wantsFocus) res.add(c)
      if (r.wantsFocus) res.add(r)
    }

    val nextRow = multResultRows((carryRowSwitcher.currRow + 1) % multResultRows.size)
    val nextRowTf = nextRow.last

    val pIdx = if (carryRowSwitcher.currRow == 0) multResultRows.size - 1 else carryRowSwitcher.currRow - 1
    val prevRow = multResultRows(pIdx)
    val prevRowTf = prevRow.find { tf => tf.wantsFocus }
    res.add(nextRowTf)
    if (prevRowTf.isDefined) res.add(prevRowTf.get)

    // res.foreach {jc => println(jc.getName)}
    focusOrder = res
    fp = new FocusPolicy(focusOrder)
    topComponent.setFocusTraversalPolicy(fp)
  }

  def onRowEvent(row: Int, tf: InputNumberTextField) {
    strategy.onRowEvent(row, tf)
  }

  def fillHotRowCols(hrcs: Set[(Int, Int)], row: Int, col: Int, carryOffset: Option[Int], tf: InputNumberTextField) {
    strategy.fillHotRowCols(hrcs, row, col, carryOffset, tf)
  }

  def isDone(): Boolean = {
    isPhase1Done()
  }

  def schedule(secs: Double)(f: () => Unit) {
    lazy val t: Timer = new Timer((secs * 1000).toInt, new ActionListener {
      def actionPerformed(e: ActionEvent) {
        t.stop
        f()
      }
    })
    t.start
  }

  def afterDigit(e: KeyEvent) {
    if (isPhase1Done()) {
      if (!isPhase1Good()) {
        statusBar.setMessage(StringConstants.Problem)
      }
      else {
        strategy.phase1Done
      }
    }
  }

  def isPhase1Good(): Boolean = {
    carrysRows.foldLeft(true)((b, row) => b && isInputRowGood(row)) &&
      multResultRows.foldLeft(true)((b, row) => b && isInputRowGood(row))
  }

  def isPhase1Done(): Boolean = {
    carrysRows.foldLeft(true)((b, row) => b && isInputRowFull(row)) &&
      multResultRows.foldLeft(true)((b, row) => b && isInputRowFull(row))
  }

  def numEmptyBoxes(): Int = {
    carrysRows.foldLeft(0)((num, row) => num + numEmptyInInputRow(row)) +
      multResultRows.foldLeft(0)((num, row) => num + numEmptyInInputRow(row))
  }

  lazy val numBoxes: Int = {
    carrysRows.foldLeft(0)((num, row) => num + numWantingInputInRow(row)) +
      multResultRows.foldLeft(0)((num, row) => num + numWantingInputInRow(row))
  }

  override def paintComponent(g: Graphics) {
    g.setBackground(Color.WHITE);
    g.clearRect(0, 0, getWidth, getHeight)
    super.paintComponent(g);

    val gTemp = g.create()
    // Enable antialiasing
    gTemp.setRenderingHint(
      RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON
    )

    val w = 2
    gTemp.setColor(ColorConstants.Border)
    gTemp.setStroke(new BasicStroke(w, BasicStroke.CAP_ROUND,
      BasicStroke.JOIN_MITER))
    // gTemp.drawRect(w, w, getWidth-2*w, getHeight-2*w);
    gTemp.drawRoundRect(w, w, getWidth - 2 * w, getHeight - 2 * w, 4 * w, 4 * w);
    gTemp.dispose
  }

  class AdditionContextAdapter extends addition.AdditionContext {

    val statusBar = new VStatusBar
    val panel = MainPanel.this
    val layouter: net.kogics.rithica.ui.LayoutHelper = MainPanel.this.layouter
    val swc = MainPanel.this.swc
    val topComponent = MainPanel.this.topComponent
    val carrysRowIdx = MainPanel.CarrysRowIdx
    val numbers = doNumbers

    def doNumbers: List[VInt] = {
      //      var i = -1
      //      model.mults.map(vi => { i += 1; vi.shift(i) })
      // 2.12 compilation refactor
      model.mults.zipWithIndex.map { case (vi, idx) => vi.shift(idx) }
    }

  }

  class CarryRowSwitcher(guiRow: Int) {
    val startCol = numCols - mpcand.size
    var currRow: Int = 0

    def rowChange(newRow: Int): Boolean = {
      newRow != currRow
    }

    def switchRow(newRow: Int) {
      val carrysRow = carrysRows(currRow)
      carrysRow.foreachWithIdx((tf, i) => layouter.remove(tf))
      showRow(newRow)
    }

    def showRow(newRow: Int) {
      val currCarrysRow = carrysRows(newRow)
      currCarrysRow.foreachWithIdx((tf, i) => layouter.add(tf, guiRow, startCol + i))
      currRow = newRow
    }
  }

  abstract class Mode()
  case object Multiply extends Mode
  case object Add extends Mode

  abstract class Strategy {
    def numberRowField(tf: NumberDisplayField)
    def multSign(row: Int, col: Int)
    def plusSign(row: Int, col: Int)
    def onRowEvent(row: Int, tf: InputNumberTextField)
    def fillHotRowCols(hrcs: Set[(Int, Int)], row: Int, col: Int, carryOffset: Option[Int], tf: InputNumberTextField)
    def initComponents
    def phase1Done
    def resultStartRow: Int
  }

  abstract class MultStrategy extends Strategy {
    def numberRowField(tf: NumberDisplayField) {}

    def multSign(row: Int, col: Int) {
      addMultSign(row, col)
    }

    def onRowEvent(row: Int, tf: InputNumberTextField) {
      if (carryRowSwitcher.rowChange(row)) {
        carryRowSwitcher.switchRow(row)
        setCustomFocusPolicy
        tf.requestFocusInWindow
        revalidate()
        repaint()
      }
    }

    def fillHotRowCols(hrcs: Set[(Int, Int)], row: Int, col: Int, carryOffset: Option[Int], tf: InputNumberTextField) {
      hrcCalci.calculate(row, col, carryOffset).foreach { e => hrcs.add(e) }
      repaint()
    }
  }

  class AddStrategy extends Strategy {
    def numberRowField(tf: NumberDisplayField) {
      tf.setForeground(ColorConstants.InactiveOutline)
    }
    def multSign(row: Int, col: Int) {
      addGrayMultSign(row, col)
    }
    def plusSign(row: Int, col: Int) { throw new UnsupportedOperationException }

    def onRowEvent(row: Int, tf: InputNumberTextField) {}
    def fillHotRowCols(hrcs: Set[(Int, Int)], row: Int, col: Int, carryOffset: Option[Int], tf: InputNumberTextField) {
      // we get one spurious event here from the first field
      // so we don't want to throw this exception
      // need to dig into that event
      // throw new UnsupportedOperationException
    }
    def phase1Done { throw new UnsupportedOperationException }
    def resultStartRow: Int = throw new UnsupportedOperationException
    def initComponents {
      hotRowCols.clear
      layouter.removeAll
      strategy = this
      var nextRow = addInactiveInputRow(1)
      nextRow = addNumberRows(nextRow)
      nextRow = addSeparator(nextRow)
      addAdditionSubPanel(nextRow)
      revalidate()
      repaint()
      schedule(0.3) { () =>
        statusBar.setMessage("Welcome to the Addition Phase.\nAdd the numbers above to get the final Multiplication result.")
      }
    }
  }
  class OneDigitMultStrategy extends MultStrategy {

    def initComponents {
      var nextRow = addMultCarrysRows(1)
      nextRow = addNumberRows(nextRow)
      nextRow = addSeparator(nextRow)
      nextRow = addMultResultRows(nextRow)
      nextRow = addStatusBar(nextRow, statusBar)
      activateCustomFocus
    }

    def phase1Done {
      val (grade, par, elapsedTime) = swc.stopWatchStop
      statusBar.setMessage(StringConstants.Congrats format (grade))
    }

    def plusSign(row: Int, col: Int) {}

    def resultStartRow = 5
  }

  class NDigitMultStrategy extends MultStrategy {
    def initComponents {
      var nextRow = addMultCarrysRows(1)
      nextRow = addNumberRows(nextRow)
      nextRow = addSeparator(nextRow)
      nextRow = addInactiveInputRow(nextRow)
      nextRow = addMultResultRows(nextRow)
      nextRow = addSeparator(nextRow)
      nextRow = addInactiveInputRow(nextRow)
      nextRow = addStatusBar(nextRow, statusBar)
      activateCustomFocus
    }

    def phase1Done {
      statusBar.setMessage("You're done with this Phase!\nSwitching to the Addition Phase...")
      val lstrategy = new AddStrategy
      schedule(3)(lstrategy.initComponents _)
    }

    def plusSign(row: Int, col: Int) {
      addGrayPlusSign(row, col)
    }

    def resultStartRow = 6
  }
}

