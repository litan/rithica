/*
 * Copyright (C) 2008-2009 Lalit Pant <pant.lalit@gmail.com>
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

package net.kogics.rithica.subtraction

import java.awt.Container
import java.awt.event.KeyEvent
import java.util.ArrayList
import java.util.List

import javax.swing.JLabel

import net.kogics.rithica.collection.Converter._
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.ListBuffer

import net.kogics.rithica.core.VInt
import net.kogics.rithica.ui.FocusPolicy
import net.kogics.rithica.ui.InputNumberFieldCtx
import net.kogics.rithica.ui.InputNumberTextField
import net.kogics.rithica.ui.NumberDisplayField
import net.kogics.rithica.ui.PanelUtils
import net.kogics.rithica.ui.ResultTextField
import net.kogics.rithica.ui.RoundedBorderPanel
import net.kogics.rithica.ui.RowCol
import net.kogics.rithica.ui.StopWatchController
import net.kogics.rithica.ui.StringConstants
import net.kogics.rithica.ui.VPanel
import net.kogics.rithica.ui.VStatusBar

object MainPanel {
  def apply(num1: VInt, num2: VInt, topComponent: Container, cp: StopWatchController, statusBar: VStatusBar) =
    new MainPanel(num1, num2, topComponent, cp, statusBar)
  def apply(digits: Int, c: VInt.Complexity, topComponent: Container, cp: StopWatchController, statusBar: VStatusBar) = {
    val nums = GameModel.createNums(digits, c)
    new MainPanel(nums._1, nums._2, topComponent, cp, statusBar)
  }
}

class MainPanel(val num1: VInt, val num2: VInt, topComponent: Container, swc: StopWatchController, val statusBar: VStatusBar)
  extends RoundedBorderPanel with VPanel with PanelUtils with InputNumberFieldCtx {

  val model = new GameModel(num1, num2)
  val numbers = model.numbers
  val digits = VInt.maxSize(numbers)
  val layouter = new LayoutHelper(this, digits)

  val numCols = layouter.numCols
  val borrowRow = new ArrayList[InputNumberTextField]
  val resultRow = new ArrayList[ResultTextField with RowCol]
  val numberRows = new ArrayList[List[NumberDisplayField with RowCol]]
  lazy val focusOrder = determineFocusOrder
  lazy val fp = new FocusPolicy(focusOrder)

  initComponents()

  def initComponents() {

    def addResultRow(guiRow: Int): Int = {
      val vint = model.result
      val startCol = rowStartCol(vint.size)
      for (i <- startCol to numCols) {
        val tf = new ResultTextField(this, vint.getFromEnd(i - startCol)) with RowCol {
          val row = guiRow
          val col = i
        }
        tf.setName("%d, %d" format (guiRow, i))
        resultRow.add(tf)
        layouter.add(tf, guiRow, i)
      }
      guiRow + 1
    }

    def addNumberRows(guiRow: Int): Int = {
      val gr1 = guiRow
      val borrowIter = borrowRow.listIterator(borrowRow.size)
      val rowNums = new ArrayList[NumberDisplayField with RowCol]
      val startCol = rowStartCol(num1.size)
      type NumberFieldType = NumberDisplayField with RowCol with Borrowable
      val nextTf: NumberFieldType = new NumberDisplayField with RowCol with Borrowable {
        val row = gr1
        val col = numCols
        val nextBorrowable = None
        val aboveField = borrowIter.previous
        val needsBorrow = model.borrows(0)
      }
      nextTf.setText(num1(0).toString)
      val revRns = ListBuffer(nextTf)
      val ntf = ArrayBuffer(nextTf)

      for (i <- (startCol to numCols - 1).reverse) {
        val tf = new NumberDisplayField with RowCol with Borrowable {
          val row = gr1
          val col = i
          val nextBorrowable = Some(ntf(0))
          val aboveField = borrowIter.previous
          val needsBorrow = model.borrows(numCols - i)
        }
        tf.setName("%d, %d" format (gr1, i))
        tf.setText(num1(numCols - i).toString)
        revRns.insert(0, tf)
        ntf.update(0, tf)
      }

      revRns.foreach { tf =>
        layouter.add(tf, tf.row, tf.col)
        rowNums.add(tf)
      }

      val gr2 = gr1 + 1
      numberRows.add(rowNums)

      val rowNums2 = new ArrayList[NumberDisplayField with RowCol]
      for (i <- startCol to numCols) {
        val tf = new NumberDisplayField with RowCol {
          val row = gr2
          val col = i
        }
        tf.setName("%d, %d" format (gr2, i))
        tf.setText(num2.getFromEnd(i - startCol).toString)
        layouter.add(tf, gr2, i)
        rowNums2.add(tf)
      }

      addMinusSign(gr2, numCols - num2.size)

      numberRows.add(rowNums2)
      gr2 + 1
    }

    def addBorrowRow(guiRow: Int): Int = {
      val startCol = 2
      for (i <- startCol to numCols) {
        val tf = new InputNumberTextField(this)
        tf.setName("%d, %d" format (guiRow, i))
        layouter.add(tf, guiRow, i)
        borrowRow.add(tf)
      }
      guiRow + 1
    }

    def addClick2BorrowImage(guiRow: Int) {
      val icon = createImageIcon("/image/click-borrow.png",
        "Click to Borrow");
      val label = new JLabel(icon);
      layouter.add(label, guiRow, 1)
    }

    var nextRow = addBorrowRow(1)
    if (digits != 1) addClick2BorrowImage(nextRow)
    nextRow = addNumberRows(nextRow)
    nextRow = addSeparator(nextRow)
    nextRow = addResultRow(nextRow)
    nextRow = addStatusBar(nextRow, statusBar)

    customizeFocusKeys
  }

  def determineFocusOrder: List[InputNumberTextField] = {
    val res = new ArrayList[InputNumberTextField]
    val rli = resultRow.listIterator(resultRow.size)
    while (rli.hasPrevious) {
      val r = rli.previous
      if (r.wantsFocus) res.add(r)
    }
    res
  }

  def askForFocus {
    topComponent.setFocusTraversalPolicy(fp)
    fp.getDefaultComponent(null).requestFocusInWindow
  }

  def isDone(): Boolean = {
    isSubDone()
  }

  def afterDigit(e: KeyEvent) {
    if (isSubDone) {
      if (isSubGood()) {
        val (grade, par, elapsedTime) = swc.stopWatchStop
        statusBar.setMessage(StringConstants.Congrats format (grade))
      }
      else {
        statusBar.setMessage(StringConstants.Problem)
      }
    }
  }

  def numEmptyBoxes(): Int = {
    numEmptyInInputRow(focusOrder)
  }

  lazy val numBoxes: Int = {
    numWantingInputInRow(focusOrder)
  }

  def isSubDone(): Boolean = {
    isInputRowFull(focusOrder)
  }

  def isSubGood(): Boolean = {
    isInputRowGood(focusOrder)
  }
}

