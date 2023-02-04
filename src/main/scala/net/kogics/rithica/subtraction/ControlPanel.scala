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
package subtraction

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.SpinnerListModel
import javax.swing.SpinnerNumberModel
import core.VInt
import info.clearthought.layout.TableLayout
import ui.LayoutConstants.b
import ui.LayoutConstants.hg
import ui.LayoutConstants.p
import ui.LayoutConstants.vg
import ui.StopWatch
import net.kogics.rithica.ui.VStatusBar

class ControlPanel(val container: TopContainer, val ap: GameParams, val statusBar: VStatusBar)
extends ui.RoundedBorderPanel with ui.StopWatchController with ui.ControlPanel {

    def this() = this(null, new GameParams {override val digits = 3
                                            override val complexity = VInt.Easy
                                            override val parTime = StopWatch.NoLimit
                                            override val baseTime = 15L
                                            override val baseD = 2
                                            override val baseC = VInt.Middling}, new VStatusBar)

    var baseD = ap.baseD

    val numRows = 3
    val numCols = 2
    val rowSpec = Array(b, p, vg, p, vg, p, vg, p, vg, p, vg, p, vg, p, vg, p, vg, p, vg, p, b)
    val colSpec = Array(b, p, hg, p, b)

    val lout = new TableLayout(colSpec, rowSpec)
    setLayout(lout)

    var nextRow = 1

    val dsm = new SpinnerNumberModel(ap.digits, 1, 7, 1)
    val digitsSpinner = addLabelAndSpinner(nextRow, "Digits per Number:", dsm)
    digitsSpinner.addChangeListener(spinnerCl)

    nextRow += 2
    val csm = new SpinnerNumberModel(ap.complexity.toInt, 1, 3, 1)
    val cSpinner = addLabelAndSpinner(nextRow, "Difficulty Level:", csm)
    cSpinner.addChangeListener(spinnerCl)

    nextRow += 2
    val ptsm = new SpinnerListModel(StopWatch.ValidTimes)
    changeParTime(ap.parTime)
    val ptSpinner = addLabelAndSpinner(nextRow, "Time Limit:", ptsm)
    ptSpinner.addChangeListener(parTimeCl)

    nextRow += 2
    addButton(nextRow)

    nextRow += 2
    val resetParamsButton = addResetParamsButton(nextRow)
    onPossibleParamsChange

    nextRow += 2
    val stopWatch = addStopWatch(nextRow)

//    nextRow += 2
//    addScoreCard(nextRow)
//
//    nextRow += 2
//    addClearScoreButton(nextRow)
//
//    nextRow += 2
//    addPrintScoreButtonAndField(nextRow)

    private def addButton(row: Int) {
        val but = newQuestionButton
        but.addActionListener(new ActionListener {
                override def actionPerformed(e: ActionEvent) {
                    container.onSumChange(new GameParams {override val digits = digitsSpinner.getValue.asInstanceOf[Int]
                                                          override val complexity = VInt.complexityFromInt(cSpinner.getValue.asInstanceOf[Int])
                                                          override val parTime = ptSpinner.getValue.asInstanceOf[String]
                                                          override val baseTime = ControlPanel.this.baseTime
                                                          override val baseD = ControlPanel.this.baseD
                                                          override val baseC = ControlPanel.this.baseC})
                }
            })
        add(but, "%d, %d, %d, %d, C, C" format(1, row, 3, row))
    }

    def setAppropriateTime {
        val d = digitsSpinner.getValue.asInstanceOf[Int]
        val c = VInt.complexityFromInt(cSpinner.getValue.asInstanceOf[Int])
        changeParTime(determineAppropriateTime(d, c))
    }

    def determineAppropriateTime(d: Int, c: VInt.Complexity): String = {
        val scale = 8
        val incr = (d * c.toInt) - (baseD * baseC.toInt)
        val apt = baseTime + incr * scale
        StopWatch.nearestValidTime(apt)
    }

    def updateCustomBaseParams() {
        baseD = digitsSpinner.getValue.asInstanceOf[Int]
    }

    def resetCustomBaseParams() {
        baseD = ap.baseD
    }

    def paramsChanged: Boolean = {
        val digits = digitsSpinner.getValue.asInstanceOf[Int]
        val complexity = VInt.complexityFromInt(cSpinner.getValue.asInstanceOf[Int])
        val parTime = ptSpinner.getValue.asInstanceOf[String]

        (digits != ap.digits
         || complexity != ap.complexity
         || parTime != ap.parTime)
    }
}
