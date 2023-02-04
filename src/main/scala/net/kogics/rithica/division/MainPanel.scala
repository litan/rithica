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

package net.kogics.rithica.division

import java.awt.Container
import java.awt.Dimension
import java.awt.Font
import java.awt.event.KeyEvent
import java.util.ArrayList
import java.util.List

import net.kogics.rithica.collection.Converter._

import net.kogics.rithica.core.VInt
import net.kogics.rithica.ui.CarryTextField
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

  val numCols = layouter.numCols - 1
  val carrysRow = new ArrayList[CarryTextField]
  val resultRow = new ArrayList[ResultTextField with RowCol]
  val numberRows = new ArrayList[List[NumberDisplayField with RowCol]]
  lazy val focusOrder = determineFocusOrder
  lazy val fp = new FocusPolicy(focusOrder)

  initComponents

  def initComponents {

    def addRemainderRow(guiRow: Int): Int = {
      val vint = model.result
      val startColT = rowStartCol(model.remainders.size) + 1
      val startCol = if (vint.digits.last == 0) startColT + 1 else startColT
      for (i <- startCol + 1 to numCols + 1) {
        val tf = new CarryTextField(this, model.remainders(numCols + 1 - i)) with RowCol {
          val row = guiRow
          val col = i
        }
        tf.setName("%d, %d" format (guiRow, i))
        tf.setPreferredSize(new Dimension(15, 23))
        tf.setColumns(2)
        tf.setFont(new Font("Sans Serif", Font.BOLD, 11))
        carrysRow.add(tf)
        layouter.addBefore(tf, guiRow, i, "%d, %d, L, C")
      }
      guiRow
    }

    def addResultRow(guiRow: Int): Int = {
      val vint = model.result
      val startColT = rowStartCol(vint.size)
      val startCol = if (vint.digits.last == 0) startColT + 1 else startColT
      for (i <- startCol to numCols) {
        val tf = new ResultTextField(this, vint(numCols - i)) with RowCol {
          val row = guiRow
          val col = i
        }
        tf.setName("%d, %d" format (guiRow, i))
        resultRow.add(tf)
        layouter.add(tf, guiRow, i)
      }
      guiRow + 1
    }

    def addNumberRow(guiRow: Int): Int = {

      val colx = 1
      val tf = new NumberDisplayField with RowCol {
        val row = guiRow
        val col = colx
      }
      tf.setName("%d, %d" format (guiRow, colx))
      tf.setText(num2(0).toString)
      tf.setColumns(1)
      layouter.add(tf, guiRow, colx, "%d, %d, R, C")

      addVerticalSeparator(guiRow, colx + 1)

      val rowNum = new ArrayList[NumberDisplayField with RowCol]
      val startCol = rowStartCol(num1.size)
      for (i <- startCol to numCols) {
        val tf = new NumberDisplayField with RowCol {
          val row = guiRow
          val col = i
        }
        tf.setName("%d, %d" format (guiRow, i))
        tf.setText(num1.getFromEnd(i - startCol).toString)
        layouter.add(tf, guiRow, i)
        rowNum.add(tf)
      }
      numberRows.add(rowNum)
      guiRow + 1
    }

    var nextRow = addResultRow(2)
    nextRow = addSeparator(nextRow, 2, numCols)
    nextRow = addRemainderRow(nextRow)
    nextRow = addNumberRow(nextRow)
    nextRow = addStatusBar(nextRow, numCols + 1, statusBar)

    customizeFocusKeys
  }

  def determineFocusOrder: List[InputNumberTextField] = {
    val res = new ArrayList[InputNumberTextField]
    val resi = resultRow.iterator
    val remi = carrysRow.iterator

    while (resi.hasNext) {
      val r = resi.next
      val rem = remi.next
      if (r.wantsFocus) res.add(r)
      if (rem.wantsFocus) res.add(rem)
    }

    res
  }

  def askForFocus {
    topComponent.setFocusTraversalPolicy(fp)
    fp.getDefaultComponent(null).requestFocusInWindow
  }

  def isDone(): Boolean = {
    isDivDone()
  }

  def afterDigit(e: KeyEvent) {
    if (isDivDone) {
      if (isDivGood()) {
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

  def isDivDone(): Boolean = {
    isInputRowFull(focusOrder)
  }

  def isDivGood(): Boolean = {
    isInputRowGood(focusOrder)
  }
}

