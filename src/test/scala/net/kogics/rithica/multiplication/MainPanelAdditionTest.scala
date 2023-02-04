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

package net.kogics.rithica.multiplication

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert._

import net.kogics.rithica.ui.LayoutConstants._
import net.kogics.rithica.core._
import net.kogics.collection.RichCollections._

import javax.swing.JPanel

class MainPanelAdditionTest {

    val carrysStartRow = MainPanel.CarrysRowIdx
    val numStartRow = MainPanel.CarrysRowIdx + 1

    @Test
    def test3x2values {
        val mpanel = MainPanel(VInt(1,7,6), VInt(6,2), new JPanel, null)
        val apanel = mpanel.createAdditionPanel

        println("Carrys Row")
        apanel.carrysRow.foreach {tf => println(tf.expectedVal)}

        var ctf = apanel.carrysRow(0)
        assertEquals(None, ctf.expectedVal)
        ctf = apanel.carrysRow(1)
        assertEquals(None, ctf.expectedVal)
        ctf = apanel.carrysRow(2)
        assertEquals(None, ctf.expectedVal)
        ctf = apanel.carrysRow(3)
        assertEquals(Some(1), ctf.expectedVal)
        ctf = apanel.carrysRow(4)
        assertEquals(None, ctf.expectedVal)
        ctf = apanel.carrysRow(5)
        assertEquals(None, ctf.expectedVal)

        var ntf = apanel.numberRows(0)(0)
        assertEquals("3", ntf.getText)
        ntf = apanel.numberRows(0)(1)
        assertEquals("5", ntf.getText)
        ntf = apanel.numberRows(0)(2)
        assertEquals("2", ntf.getText)
        ntf = apanel.numberRows(1)(0)
        assertEquals("1", ntf.getText)
        ntf = apanel.numberRows(1)(1)
        assertEquals("0", ntf.getText)
        ntf = apanel.numberRows(1)(2)
        assertEquals("5", ntf.getText)
        ntf = apanel.numberRows(1)(3)
        assertEquals("6", ntf.getText)
        ntf = apanel.numberRows(1)(4)
        assertEquals("0", ntf.getText)

        var rtf = apanel.resultRow(0)
        assertEquals(1, rtf.expectedVal)
        rtf = apanel.resultRow(1)
        assertEquals(0, rtf.expectedVal)
        rtf = apanel.resultRow(2)
        assertEquals(9, rtf.expectedVal)
        rtf = apanel.resultRow(3)
        assertEquals(1, rtf.expectedVal)
        rtf = apanel.resultRow(4)
        assertEquals(2, rtf.expectedVal)
    }

    @Test
    def test3x2positions {
        val resultStartRow = 9

        val mpanel = MainPanel(VInt(1,7,6), VInt(6,2), new JPanel, null)
        val apanel = mpanel.createAdditionPanel

        var ctf = apanel.carrysRow(0)
        assertEquals((carrysStartRow, apanel.numCols-5), (ctf.row, ctf.col))
        ctf = apanel.carrysRow(1)
        assertEquals((carrysStartRow, apanel.numCols-4), (ctf.row, ctf.col))
        ctf = apanel.carrysRow(2)
        assertEquals((carrysStartRow, apanel.numCols-3), (ctf.row, ctf.col))
        ctf = apanel.carrysRow(3)
        assertEquals((carrysStartRow, apanel.numCols-2), (ctf.row, ctf.col))
        ctf = apanel.carrysRow(4)
        assertEquals((carrysStartRow, apanel.numCols-1), (ctf.row, ctf.col))
        ctf = apanel.carrysRow(5)
        assertEquals((carrysStartRow, apanel.numCols), (ctf.row, ctf.col))
        
        var ntf = apanel.numberRows(0)(0)
        assertEquals((numStartRow, apanel.numCols-2), (ntf.row, ntf.col))
        ntf = apanel.numberRows(0)(1)
        assertEquals((numStartRow, apanel.numCols-1), (ntf.row, ntf.col))
        ntf = apanel.numberRows(0)(2)
        assertEquals((numStartRow, apanel.numCols), (ntf.row, ntf.col))
        ntf = apanel.numberRows(1)(0)
        assertEquals((numStartRow+1, apanel.numCols-4), (ntf.row, ntf.col))
        ntf = apanel.numberRows(1)(1)
        assertEquals((numStartRow+1, apanel.numCols-3), (ntf.row, ntf.col))
        ntf = apanel.numberRows(1)(2)
        assertEquals((numStartRow+1, apanel.numCols-2), (ntf.row, ntf.col))
        ntf = apanel.numberRows(1)(3)
        assertEquals((numStartRow+1, apanel.numCols-1), (ntf.row, ntf.col))
        ntf = apanel.numberRows(1)(4)
        assertEquals((numStartRow+1, apanel.numCols), (ntf.row, ntf.col))

        var rtf = apanel.resultRow(0)
        assertEquals((resultStartRow, mpanel.numCols-4), (rtf.row, rtf.col))
        rtf = apanel.resultRow(1)
        assertEquals((resultStartRow, mpanel.numCols-3), (rtf.row, rtf.col))
        rtf = apanel.resultRow(2)
        assertEquals((resultStartRow, mpanel.numCols-2), (rtf.row, rtf.col))
        rtf = apanel.resultRow(3)
        assertEquals((resultStartRow, mpanel.numCols-1), (rtf.row, rtf.col))
        rtf = apanel.resultRow(4)
        assertEquals((resultStartRow, mpanel.numCols), (rtf.row, rtf.col))
    }

    @Test
    def test3x2sizes {
        val mpanel = MainPanel(VInt(1,7,6), VInt(6,2), new JPanel, null)
        val apanel = mpanel.createAdditionPanel

        assertEquals(6, mpanel.numCols)
        assertEquals(mpanel.numCols, apanel.numCols)

        assertEquals(6, apanel.carrysRow.size)

        assertEquals(2, apanel.numberRows.size)
        assertEquals(3, apanel.numberRows(0).size)
        assertEquals(5, apanel.numberRows(1).size)

        assertEquals(5, apanel.resultRow.size)
    }


    @Test
    def testExpt {
        val mpanel = MainPanel(VInt(4,4), VInt(1,2), new JPanel, null)
        val apanel = mpanel.createAdditionPanel
        println("****************************************************")
        println("Carrys Row")
        apanel.carrysRow.foreach {tf => println(tf.expectedVal)}
        println("Result Row")
        apanel.resultRow.foreach {tf => println(tf.expectedVal)}
        println("Result: " + apanel.model.result)

    }
}
