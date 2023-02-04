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

import java.util.ArrayList
import java.util.List

import javax.swing.JComponent
import javax.swing.JPanel

import LayoutConstants.b
import LayoutConstants.hg
import LayoutConstants.p
import collection.ListJ.lToAd
import info.clearthought.layout.TableLayout


abstract class LayoutHelper(container: JPanel) {

    lazy val rSpec = rowSpec
    lazy val colSpec = columnSpec
    lazy val numCols = numColumns

    val layout = new TableLayout(lToAd(colSpec), lToAd(rSpec))

    container.setLayout(layout)

    def add(comp: JComponent, row: Int, column: Int) {
        container.add(comp, "%d, %d" format(column*2-1, row*2-1))
    }

    def add(comp: JComponent, row: Int, column: Int, format: String) {
        container.add(comp, format format(column*2-1, row*2-1))
    }

    def add(comp: JComponent, row: Int, column: Int, endRow: Int, endCol: Int) {
        container.add(comp, "%d, %d, %d, %d" format(column*2-1, row*2-1, endCol*2-1, endRow*2-1))
    }

    def addBefore(comp: JComponent, row: Int, column: Int, format: String) {
        container.add(comp, format format(column*2-1, row*2-2))
    }

    def remove(comp: JComponent) {
        container.remove(comp)
    }

    def removeAll() {
        container.removeAll()
    }

    protected def columnSpec: List[Double] = {
        val colSpec = new ArrayList[Double]
        colSpec.add(b)
        for (i <- 1 to numCols) {
            colSpec.add(p)
            colSpec.add(hg)
        }
        colSpec.add(p)
        colSpec.add(b)
        colSpec
    }

    protected def numColumns: Int
    protected def rowSpec: List[Double]
}
