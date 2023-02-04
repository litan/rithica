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

import net.kogics.rithica.core.Predef._
import java.util.{Set, HashSet}

// The FieldMarker trait, when mixed into a number field, paints green hats on the field when it is hot
// The HotRowColChanger, when mixed into a field, causes a change in the hot row-col when this field gets focus
// The HotRowColDecider decides what this new hot row-col is. 

trait FieldMarker extends ArithTextField with RowCol {

    val ctx: HotRowColDecider
    lazy val rowCol = (row, col)

    abstract override def paintComponent(g: Graphics) {
        super.paintComponent(g)
        if (ctx.hotRowCols.contains(rowCol)) {
            val d = (getWidth.toDouble / 2.7).toInt
            val dh = 5
            g.setColor(ColorConstants.Focus)
            g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND,
                                    BasicStroke.JOIN_MITER))
            g.drawLine(d, getHeight, getWidth/2, getHeight-dh)
            g.drawLine(getWidth/2, getHeight-dh, getWidth-d, getHeight)
        }
    }
}

trait HotRowColDecider {
    val hotRowCols = new HashSet[(Int, Int)]
    def fillHotRowCols(hrcs: Set[(Int, Int)], row: Int, col: Int, carryOffset: Option[Int], tf: InputNumberTextField)
    def onRowColEvent(row: Int, col: Int, carryOffset: Option[Int], tf: InputNumberTextField) {
        hotRowCols.clear
        fillHotRowCols(hotRowCols, row, col, carryOffset, tf)
    }
}

trait HotRowColChanger extends RowCol {
    this: InputNumberTextField  =>

    val hotRowColDecider: HotRowColDecider
    val carryOffset: Option[Int]

    addFocusListener(new FocusAdapter {
            override def focusGained(e: FocusEvent) {
                hotRowColDecider.onRowColEvent(row, col, carryOffset, HotRowColChanger.this)
            }
        })

}
