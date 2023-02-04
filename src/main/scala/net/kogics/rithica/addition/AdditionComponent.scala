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

import java.awt.Container
import java.awt.event.KeyEvent
import java.util.ArrayList
import java.util.List

import javax.swing.JPanel

import net.kogics.rithica.collection.Converter._

import net.kogics.rithica.core.VInt
import net.kogics.rithica.ui.CarryTextField
import net.kogics.rithica.ui.FocusPolicy
import net.kogics.rithica.ui.InputNumberFieldCtx
import net.kogics.rithica.ui.InputNumberTextField
import net.kogics.rithica.ui.NumberDisplayField
import net.kogics.rithica.ui.PanelUtils
import net.kogics.rithica.ui.ResultTextField
import net.kogics.rithica.ui.RowCol
import net.kogics.rithica.ui.StopWatchController
import net.kogics.rithica.ui.StringConstants
import net.kogics.rithica.ui.VPanel
import net.kogics.rithica.ui.VStatusBar


trait AdditionContext {
    val panel: JPanel with VPanel
    val numbers: List[VInt]
    val topComponent: Container
    val swc: StopWatchController
    val layouter: net.kogics.rithica.ui.LayoutHelper
    val carrysRowIdx: Int
}

trait AdditionComponent extends InputNumberFieldCtx with PanelUtils {
    this: AdditionContext =>

    val nums = numbers.size
    val digits = VInt.maxSize(numbers)

    val model = new GameModel(numbers)
    val numCols = layouter.numCols
    val carrysRow = new ArrayList[CarryTextField with RowCol]
    val resultRow = new ArrayList[ResultTextField with RowCol]
    val numberRows = new ArrayList[List[NumberDisplayField with RowCol]]
    lazy val focusOrder= determineFocusOrder
    lazy val fp = new FocusPolicy(focusOrder)

    initComponents

    def initComponents {

        def addCarrysRow(guiRow: Int): Int = {
            val startCol = rowStartCol(model.carrys.size)
            for (i <- startCol to numCols) {
                val tf = new CarryTextField(this, model.carrys.getFromEnd(i-startCol)) with RowCol {
                    val row = guiRow
                    val col = i
                }
                tf.setName("%d, %d" format(guiRow, i))
                carrysRow.add(tf)
                layouter.add(tf, guiRow, i)
            }
            carrysRow(0).lastField = true
            guiRow + 1
        }

        def addResultRow(guiRow: Int): Int = {
            val vint = model.result
            val startCol = rowStartCol(vint.size)
            for (i <- startCol to numCols) {
                val tf = new ResultTextField(this, vint.getFromEnd(i-startCol)) with RowCol {
                    val row = guiRow
                    val col = i
                }
                tf.setName("%d, %d" format(guiRow, i))
                resultRow.add(tf)
                layouter.add(tf, guiRow, i)
            }
            guiRow + 1
        }

        def addNumberRows(guiRow: Int): Int = {
            var gr = guiRow
            model.numbers.foreach(vint => {
                    val rowNum = new ArrayList[NumberDisplayField with RowCol]
                    val startCol = rowStartCol(vint.size)
                    for (i <- startCol to numCols) {
                        val tf = new NumberDisplayField with RowCol {
                            val row = gr
                            val col = i
                        }
                        tf.setName("%d, %d" format(gr, i))
                        tf.setText(vint.getFromEnd(i-startCol).toString)
                        layouter.add(tf, gr, i)
                        rowNum.add(tf)
                    }
                    gr += 1
                    numberRows.add(rowNum)
                })
            addPlusSign(gr - 1, numCols - model.numbers.last.size)
            gr
        }

        var nextRow = addCarrysRow(carrysRowIdx)
        nextRow = addNumberRows(nextRow)
        nextRow = addSeparator(nextRow)
        nextRow = addResultRow(nextRow)
        // nextRow = addFiller(nextRow, 75)
        nextRow = addStatusBar(nextRow, statusBar)

        panel.customizeFocusKeys
    }

    def determineFocusOrder: List[InputNumberTextField] = {
        val res = new ArrayList[InputNumberTextField]
        val rli = resultRow.listIterator(resultRow.size)
        val cli = carrysRow.listIterator(carrysRow.size)
        while (rli.hasPrevious) {
            val c = cli.previous
            val r = rli.previous
            if (c.wantsFocus) res.add(c)
            if (r.wantsFocus) res.add(r)
        }
        res
    }

    def askForFocus {
        topComponent.setFocusTraversalPolicy(fp)
        fp.getDefaultComponent(null).requestFocusInWindow
    }

    def isDone(): Boolean = {
        isSumDone()
    }

    def afterDigit(e: KeyEvent) {
        if (isSumDone) {
            if (isSumGood()) {
                val (grade, par, elapsedTime) = swc.stopWatchStop
                statusBar.setMessage(StringConstants.Congrats format (grade))
            }
            else {
                statusBar.setMessage(StringConstants.Problem)
            }
        }
    }

    def numEmptyBoxes(): Int = {
        panel.numEmptyInInputRow(focusOrder)
    }

    lazy val numBoxes: Int = {
        panel.numWantingInputInRow(focusOrder)
    }

    def isSumDone(): Boolean = {
        panel.isInputRowFull(focusOrder)
    }

    def isSumGood(): Boolean = {
        panel.isInputRowGood(focusOrder)
    }

    def isCarry(pos: Int): Boolean = model.isCarry(pos)
}
