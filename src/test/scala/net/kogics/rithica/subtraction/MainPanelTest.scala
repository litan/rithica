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

package net.kogics.rithica.subtraction

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert._

import net.kogics.rithica.ui.LayoutConstants._
import net.kogics.rithica.core._
import net.kogics.collection.RichCollections._

import javax.swing.JPanel

class MainPanelTest {

    @Test
    def test3x3values {
        val apanel = MainPanel(VInt(1,7,6), VInt(1,6,2), new JPanel, null)

        // println("Carrys Row")
        // spanel.carrysRow.foreach {tf => println(tf.expectedVal)}

        var ntf = apanel.numberRows(0)(0)
        assertEquals("1", ntf.getText)
        ntf = apanel.numberRows(0)(1)
        assertEquals("7", ntf.getText)
        ntf = apanel.numberRows(0)(2)
        assertEquals("6", ntf.getText)
        ntf = apanel.numberRows(1)(0)
        assertEquals("1", ntf.getText)
        ntf = apanel.numberRows(1)(1)
        assertEquals("6", ntf.getText)
        ntf = apanel.numberRows(1)(2)
        assertEquals("2", ntf.getText)

        var rtf = apanel.resultRow(0)
        assertEquals(1, rtf.expectedVal)
        rtf = apanel.resultRow(1)
        assertEquals(4, rtf.expectedVal)
    }

    @Test
    def test3x2positions {
        val apanel = MainPanel(VInt(1,7,6), VInt(1,6,2), new JPanel, null)

        var ntf = apanel.numberRows(0)(0)
        assertEquals((2, apanel.numCols-2), (ntf.row, ntf.col))
        ntf = apanel.numberRows(0)(1)
        assertEquals((2, apanel.numCols-1), (ntf.row, ntf.col))
        ntf = apanel.numberRows(0)(2)
        assertEquals((2, apanel.numCols), (ntf.row, ntf.col))

        ntf = apanel.numberRows(1)(0)
        assertEquals((3, apanel.numCols-2), (ntf.row, ntf.col))
        ntf = apanel.numberRows(1)(1)
        assertEquals((3, apanel.numCols-1), (ntf.row, ntf.col))
        ntf = apanel.numberRows(1)(2)
        assertEquals((3, apanel.numCols), (ntf.row, ntf.col))

        var rtf = apanel.resultRow(0)
        assertEquals((5, apanel.numCols-1), (rtf.row, rtf.col))
        rtf = apanel.resultRow(1)
        assertEquals((5, apanel.numCols), (rtf.row, rtf.col))
        
    }

    @Test
    def test3x2sizes {
        val apanel = MainPanel(VInt(1,7,6), VInt(1,6,2), new JPanel, null)

        assertEquals(4, apanel.numCols)
        assertEquals(2, apanel.numberRows.size)
        assertEquals(3, apanel.numberRows(0).size)
        assertEquals(3, apanel.numberRows(1).size)

        assertEquals(2, apanel.resultRow.size)
    }
}
