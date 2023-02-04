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

import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import javax.swing.SpinnerListModel
import javax.swing.SpinnerNumberModel
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener

import net.kogics.rithica.ui.VStatusBar

import core.VInt
import info.clearthought.layout.TableLayout
import ui.LayoutConstants.b
import ui.LayoutConstants.hg
import ui.LayoutConstants.p
import ui.LayoutConstants.vg
import ui.StopWatch

class ControlPanel(val container: TopContainer, val ap: GameParams, val statusBar: VStatusBar)
  extends ui.RoundedBorderPanel with ui.StopWatchController with ui.ControlPanel {

  def this() = this(null, new GameParams {
    override val digitsMpcand = 4
    override val digitsMplier = 2
    override val complexity = VInt.Easy
    override val parTime = StopWatch.NoLimit
    override val baseTime = 15L
    override val baseMpcandDs = 4
    override val baseMplierDs = 2
    override val baseC = VInt.Middling
  }, new VStatusBar)

  var baseMpcandDs = ap.baseMpcandDs
  var baseMplierDs = ap.baseMplierDs

  val numRows = 3
  val numCols = 2
  val rowSpec = Array(b, p, vg, p, vg, p, vg, p, vg, p, vg, p, vg, p, vg, p, vg, p, vg, p, vg, p, b)
  val colSpec = Array(b, p, hg, p, b)

  val lout = new TableLayout(colSpec, rowSpec)
  setLayout(lout)

  var nextRow = 1

  val mpcandCl = new ChangeListener {
    def stateChanged(e: ChangeEvent) {
      val digitsMpcand = digitsMpcandSpinner.getValue.asInstanceOf[Int]
      val digitsMplier = digitsMplierSpinner.getValue.asInstanceOf[Int]
      if (digitsMpcand < digitsMplier) digitsMpcandSpinner.setValue(digitsMplier)
    }
  }

  val mplierCl = new ChangeListener {
    def stateChanged(e: ChangeEvent) {
      val digitsMpcand = digitsMpcandSpinner.getValue.asInstanceOf[Int]
      val digitsMplier = digitsMplierSpinner.getValue.asInstanceOf[Int]
      if (digitsMplier > digitsMpcand) digitsMplierSpinner.setValue(digitsMpcand)
    }
  }

  val nsm = new SpinnerNumberModel(ap.digitsMpcand, 2, 5, 1)
  val digitsMpcandSpinner = addLabelAndSpinner(nextRow, "Digits in First Number:", nsm)
  digitsMpcandSpinner.addChangeListener(mpcandCl)
  digitsMpcandSpinner.addChangeListener(spinnerCl)

  nextRow += 2
  val dsm = new SpinnerNumberModel(ap.digitsMplier, 1, 4, 1)
  val digitsMplierSpinner = addLabelAndSpinner(nextRow, "Digits in Multiplier:", dsm)
  digitsMplierSpinner.addChangeListener(mplierCl)
  digitsMplierSpinner.addChangeListener(spinnerCl)

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
  addNewQButton(nextRow)

  nextRow += 2
  val resetParamsButton = addResetParamsButton(nextRow)
  onPossibleParamsChange

  nextRow += 2
  val stopWatch = addStopWatch(nextRow)

  //  nextRow += 2
  //  addScoreCard(nextRow)
  //
  //  nextRow += 2
  //  addClearScoreButton(nextRow)
  //
  //    nextRow += 2
  //    addPrintScoreButtonAndField(nextRow)

  private def addNewQButton(row: Int) {
    val but = newQuestionButton
    but.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent) {
        container.onSumChange(new GameParams {
          override val digitsMpcand = digitsMpcandSpinner.getValue.asInstanceOf[Int]
          override val digitsMplier = digitsMplierSpinner.getValue.asInstanceOf[Int]
          override val complexity = VInt.complexityFromInt(cSpinner.getValue.asInstanceOf[Int])
          override val parTime = ptSpinner.getValue.asInstanceOf[String]
          override val baseTime = ControlPanel.this.baseTime
          override val baseMpcandDs = ControlPanel.this.baseMpcandDs
          override val baseMplierDs = ControlPanel.this.baseMplierDs
          override val baseC = ControlPanel.this.baseC
        })
      }
    })
    add(but, "%d, %d, %d, %d, C, C" format (1, row, 3, row))
  }

  def setAppropriateTime {
    val n = digitsMpcandSpinner.getValue.asInstanceOf[Int]
    val d = digitsMplierSpinner.getValue.asInstanceOf[Int]
    val c = VInt.complexityFromInt(cSpinner.getValue.asInstanceOf[Int])
    changeParTime(determineAppropriateTime(n, d, c))
  }

  def determineAppropriateTime(n: Int, d: Int, c: VInt.Complexity): String = {
    val scaleFactor = 2
    val delta = 2
    val incr = (n * d * (c.toInt + delta)) - (baseMpcandDs * baseMplierDs * (baseC.toInt + delta))
    val apt = (baseTime + incr * scaleFactor)
    StopWatch.nearestValidTime(apt.toLong)
  }

  def updateCustomBaseParams() {
    baseMpcandDs = digitsMpcandSpinner.getValue.asInstanceOf[Int]
    baseMplierDs = digitsMplierSpinner.getValue.asInstanceOf[Int]
  }

  def resetCustomBaseParams() {
    baseMpcandDs = ap.baseMpcandDs
    baseMplierDs = ap.baseMplierDs
  }

  def paramsChanged: Boolean = {
    val digitsMpcand = digitsMpcandSpinner.getValue.asInstanceOf[Int]
    val digitsMplier = digitsMplierSpinner.getValue.asInstanceOf[Int]
    val complexity = VInt.complexityFromInt(cSpinner.getValue.asInstanceOf[Int])
    val parTime = ptSpinner.getValue.asInstanceOf[String]

    (digitsMpcand != ap.digitsMpcand
      || digitsMplier != ap.digitsMplier
      || complexity != ap.complexity
      || parTime != ap.parTime)
  }
}
