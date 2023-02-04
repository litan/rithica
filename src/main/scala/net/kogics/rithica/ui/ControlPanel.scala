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
package ui

import java.awt._
import java.awt.event._
import javax.swing._
import javax.swing.event._

import core._
import core.Predef._

trait ControlPanel extends JPanel {

    val ptSpinner: JSpinner
    val cSpinner: JSpinner
    val stopWatch: StopWatch
    val ap: container.MyGameParams
    def statusBar: VStatusBar

    var baseTime = ap.baseTime
    var baseC: VInt.Complexity = ap.baseC
    var autoPtChange = false

    val ptsm: SpinnerListModel
    val container: TopContainer

    def setAppropriateTime

    val resetParamsButton: JButton
    def paramsChanged: Boolean

    setBackground(ColorConstants.CPBackground);

    val gradientStart: Color = new Color(0xf3f0dd)

    val spinnerCl = new ChangeListener {
        def stateChanged(e: ChangeEvent) {
            setAppropriateTime
            onPossibleParamsChange
        }
    }

    val parTimeCl = new ChangeListener {
        def stateChanged(e: ChangeEvent) {
            updateBaseParams
            onPossibleParamsChange
        }
    }

    def onPossibleParamsChange {
        if (paramsChanged) resetParamsButton.setEnabled(true) else resetParamsButton.setEnabled(false)
    }

    protected def changeParTime(t: String) {
        autoPtChange = true
        ptsm.setValue(t)
        autoPtChange = false
    }

    def updateCustomBaseParams(): Unit
    def resetCustomBaseParams(): Unit

    protected def updateBaseParams {
        if (autoPtChange) return
        val t = StopWatch.minsSecsToMillis(ptSpinner.getValue.asInstanceOf[String]) / 1000
        if(t == 0) {
            resetBaseParams()
        }
        else {
            baseC = VInt.complexityFromInt(cSpinner.getValue.asInstanceOf[Int])
            baseTime = t
            updateCustomBaseParams()

        }
    }

    def resetBaseParams() {
        baseTime = ap.baseTime
        baseC = ap.baseC
        resetCustomBaseParams()
    }

    protected def addResetParamsButton(row: Int): JButton = {
        val but = new JButton("Reset Parameters")
        but.setMnemonic(KeyEvent.VK_R)
        but.addActionListener(new ActionListener {
                override def actionPerformed(e: ActionEvent) {
                    container.onResetParams(ap)
                }
            })
        add(but, "%d, %d, %d, %d, C, C" format(1, row, 3, row))
        but
    }

    protected def addClearScoreButton(row: Int) {
        val but = new JButton("Clear Score")
        but.setMnemonic(KeyEvent.VK_C)
        but.addActionListener(new ActionListener {
                override def actionPerformed(e: ActionEvent) {
                    Score.clear
                }
            })
        add(but, "%d, %d, %d, %d, C, C" format(1, row, 3, row))
    }

    protected def addPrintScoreButtonAndField(row: Int) {
        val but = new JButton("Print Score For :")
        but.setMnemonic(KeyEvent.VK_P)
        val tf = new JTextField(9)
        tf.setBackground(ColorConstants.ActiveBackground)
        val ynh = StringConstants.YourName
        val user = if (container.loggedInUser != "") container.loggedInUser else ynh
        tf.setText(user)
        tf.addFocusListener(new FocusListener {
                override def focusGained(e: FocusEvent) {
                    if(tf.getText.trim.equals(ynh)) tf.setText("")
                }

                override def focusLost(e: FocusEvent) {
                    if(tf.getText.trim.equals("")) tf.setText(ynh)
                }
            })
        but.addActionListener(new ActionListener {
                override def actionPerformed(e: ActionEvent) {
                    statusBar.clearMessage
                    container.printScore(tf.getText)
                }
            })
        add(but, "%d, %d" format(1, row))
        add(tf, "%d, %d" format(3, row))
    }

    protected def addLabelAndSpinner(row: Int, ltext: String, sm: SpinnerModel): JSpinner = {
        val label = new JLabel(ltext)
        val s = new JSpinner(sm)
        val tf = s.getEditor.asInstanceOf[JSpinner.DefaultEditor].getTextField
        tf.setColumns(5)
        tf.setHorizontalAlignment(SwingConstants.CENTER)
        // tf.setBackground(ColorConstants.ActiveBackground)

        add(label, "%d, %d" format(1, row))
        add(s, "%d, %d, %d, %d, C, C" format(3, row, 3, row))
        s
    }

    protected def addStopWatch(row: Int): StopWatch = {
        val sw = new StopWatch(ptSpinner.getValue.asInstanceOf[String])
        // add(sw, "%d, %d" format(1, row))
        add(sw, "%d, %d, %d, %d, F, C" format(1, row, 3, row))
        sw
    }

    protected def addScoreCard(row: Int) {
        // add(new ScoreCard, "%d, %d" format(1, row))
        val sc = new ScoreCard
        Score.scoreListener = Some(sc)
        add(sc, "%d, %d, %d, %d, F, C" format(1, row, 3, row))
    }

    def stopWatchStop: (Score.Grade, Long, Long) = {
        stopWatch.stop
    }

    def newQuestionButton: JButton = {
        val but = new JButton("New Question")
        but.setMnemonic(KeyEvent.VK_N)
        but
    }
}
