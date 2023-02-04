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

import java.awt.Color
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.StringTokenizer

import javax.swing.Timer

import collection.ListJ
import core.Score

object StopWatch {
    val NoLimit = "No Limit"
    val Min = 15 // seconds
    val Max = 5 * 60 // seconds
    val ValidTimes = ListJ("No Limit", "00:15", "00:30", "00:45",
                           "01:00", "01:15", "01:30", "01:45",
                           "02:00", "02:15", "02:30", "02:45",
                           "03:00", "03:15", "03:30", "03:45",
                           "04:00", "04:15", "04:30", "04:45",
                           "05:00")

    def nearestValidTime(time: Long): String = {
        val t = ensureValid(roundup(time, 15))
        val mins = t / 60
        val secs = t % 60
        "%02d:%02d" format(mins, secs)
    }

    def ensureValid(time: Long): Long = {
        if (time > Max) Max
        else if (time < Min) Min
        else time
    }

    def roundup(time: Long, mod: Int) = {
        val inc = if (time % mod == 0) 0 else 1
        (time/mod + inc) * mod
    }

    def minsSecsToMillis(time: String): Long = {

        if (time == NoLimit) return 0

        val st = new StringTokenizer(time, ":")
        val mins = st.nextToken.toInt
        val secs = st.nextToken.toInt
        (mins * 60 + secs) * 1000
    }
}

class StopWatch(parInMinsSecs: String) extends VLabel {
    
    def this() = this(StopWatch.NoLimit)

    val startTime = System.currentTimeMillis
    var stopTime: Long = 0
    val parTime = StopWatch.minsSecsToMillis(parInMinsSecs)
    val timer = start
    var currErrors = 0
    val lateColor = new Color(0xb13a0c)
    val latishColor = new Color(0xff9600)
    val earlyColor = new Color(0xd5c907)
    val parColor = new Color(0xd5a207)
    borderColor = if (parTime == 0) parColor else earlyColor

    def minsToMs(mins: Double): Long = (mins * 60 * 1000).toLong

    def stop: (Score.Grade, Long, Long) =  {
        if (stopTime == 0) {
            stopTime = System.currentTimeMillis
            val elapsed = stopTime - startTime
            val grade = calculateGradeRange(elapsed)
            Score.recordGrade(grade)
            timer.stop
            (grade, parTime, elapsed)
        } else {
            val elapsed = stopTime - startTime
            val grade = calculateGradeRange(elapsed)
            (grade, parTime, elapsed)
        }
    }

    def start: Timer = {
        val t = new Timer(1000, new ActionListener {
                def actionPerformed(e: ActionEvent) {
                    updateTime
                }
            })

        adjustBorderColor(0)
        setText("%02d:%02d" format(0, 0))

        t.start
        t
    }

    def updateTime {
        if (parTime != 0) {
            val elapsed = System.currentTimeMillis - startTime
            adjustBorderColor(elapsed)
            val time = decompose(elapsed)
            setText("%02d:%02d" format(time._1, time._2))
        }
        else {
            adjustBorderColor(0)
            repaint()
        }
    }

    def adjustGradeForErrors(g: Score.Grade): Score.Grade = {
        val dec = Score.problemErrorCount / 3
        val rdec = if (dec > Score.MaxGradeDelta) Score.MaxGradeDelta else dec
        g.decrease(rdec)
    }

    def calculateGradeRange(elapsed: Long): Score.Grade = {
        adjustGradeForErrors(calculateTimeGradeRange(elapsed))
    }

    def calculateTimeGradeRange(elapsed: Long): Score.Grade = {
        if (parTime == 0) Score.A
        else if (elapsed < parTime - 5000) Score.APlus
        else if (elapsed <= parTime) Score.A
        else if (elapsed > parTime + 10000) Score.C
        else Score.B
    }

    def adjustBorderColor(elapsed: Long) {
        val grade = calculateGradeRange(elapsed)
        borderColor = grade match {
            case Score.APlus =>  earlyColor
            case Score.A =>  parColor
            case Score.B =>  latishColor
            case Score.C =>  lateColor
        }
    }

    def decompose(t: Long): (Int, Int) = {
        val secs = (t / 1000.0).toInt
        val mins = secs / 60
        val rsecs = secs % 60
        (mins, rsecs)
    }
}
