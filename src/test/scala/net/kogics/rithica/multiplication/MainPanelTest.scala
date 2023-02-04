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

class MainPanelTest {

    val carrysStartRow = 1
    val numStartRow = 2
    val resultStartRow = 6

    @Test
    def test2x2values {
        val mpanel = MainPanel(VInt(1,2), VInt(3,4), new JPanel, null)

        var ctf = mpanel.carrysRows(0)(0)
        assertEquals(None, ctf.expectedVal)
        assertEquals(Some(0), ctf.carryOffset)
        ctf = mpanel.carrysRows(0)(1)
        assertEquals(None, ctf.expectedVal)
        assertEquals(Some(0), ctf.carryOffset)
        ctf = mpanel.carrysRows(0)(2)
        assertEquals(None, ctf.expectedVal)
        assertEquals(Some(0), ctf.carryOffset)
        ctf = mpanel.carrysRows(1)(0)
        assertEquals(None, ctf.expectedVal)
        assertEquals(Some(1), ctf.carryOffset)
        ctf = mpanel.carrysRows(1)(1)
        assertEquals(None, ctf.expectedVal)
        assertEquals(Some(1), ctf.carryOffset)
        ctf = mpanel.carrysRows(1)(2)
        assertEquals(None, ctf.expectedVal)
        assertEquals(Some(1), ctf.carryOffset)

        var ntf = mpanel.numberRows(0)(0)
        assertEquals("1", ntf.getText)
        ntf = mpanel.numberRows(0)(1)
        assertEquals("2", ntf.getText)
        ntf = mpanel.numberRows(1)(0)
        assertEquals("3", ntf.getText)
        ntf = mpanel.numberRows(1)(1)
        assertEquals("4", ntf.getText)

        var rtf = mpanel.multResultRows(0)(0)
        assertEquals(4, rtf.expectedVal)
        rtf = mpanel.multResultRows(0)(1)
        assertEquals(8, rtf.expectedVal)
        rtf = mpanel.multResultRows(1)(0)
        assertEquals(3, rtf.expectedVal)
        rtf = mpanel.multResultRows(1)(1)
        assertEquals(6, rtf.expectedVal)
    }

    @Test
    def test2x2positions {
        val mpanel = MainPanel(VInt(1,2), VInt(3,4), new JPanel, null)

        var ctf = mpanel.carrysRows(0)(0)
        assertEquals((carrysStartRow, mpanel.numCols-2), (ctf.row, ctf.col))
        ctf = mpanel.carrysRows(0)(1)
        assertEquals((carrysStartRow, mpanel.numCols-1), (ctf.row, ctf.col))
        ctf = mpanel.carrysRows(0)(2)
        assertEquals((carrysStartRow, mpanel.numCols), (ctf.row, ctf.col))

        ctf = mpanel.carrysRows(1)(0)
        assertEquals((carrysStartRow, mpanel.numCols-2), (ctf.row, ctf.col))
        ctf = mpanel.carrysRows(1)(1)
        assertEquals((carrysStartRow, mpanel.numCols-1), (ctf.row, ctf.col))
        ctf = mpanel.carrysRows(1)(2)
        assertEquals((carrysStartRow, mpanel.numCols), (ctf.row, ctf.col))

        var ntf = mpanel.numberRows(0)(0)
        assertEquals((numStartRow, mpanel.numCols-1), (ntf.row, ntf.col))
        ntf = mpanel.numberRows(0)(1)
        assertEquals((numStartRow, mpanel.numCols), (ntf.row, ntf.col))

        ntf = mpanel.numberRows(1)(0)
        assertEquals((numStartRow+1, mpanel.numCols-1), (ntf.row, ntf.col))
        ntf = mpanel.numberRows(1)(1)
        assertEquals((numStartRow+1, mpanel.numCols), (ntf.row, ntf.col))

        var rtf = mpanel.multResultRows(0)(0)
        assertEquals((resultStartRow, mpanel.numCols-1), (rtf.row, rtf.col))
        rtf = mpanel.multResultRows(0)(1)
        assertEquals((resultStartRow, mpanel.numCols), (rtf.row, rtf.col))

        rtf = mpanel.multResultRows(1)(0)
        assertEquals((resultStartRow+1, mpanel.numCols-2), (rtf.row, rtf.col))
        rtf = mpanel.multResultRows(1)(1)
        assertEquals((resultStartRow+1, mpanel.numCols-1), (rtf.row, rtf.col))
    }

    @Test
    def test2x2sizes {
        val mpanel = MainPanel(VInt(1,2), VInt(3,4), new JPanel, null)
        assertEquals(5, mpanel.numCols)

        assertEquals(2, mpanel.carrysRows.size)
        assertEquals(3, mpanel.carrysRows(0).size)
        assertEquals(3, mpanel.carrysRows(1).size)

        assertEquals(2, mpanel.numberRows.size)
        assertEquals(2, mpanel.numberRows(0).size)
        assertEquals(2, mpanel.numberRows(1).size)

        assertEquals(2, mpanel.multResultRows.size)
        assertEquals(2, mpanel.multResultRows(0).size)
        assertEquals(2, mpanel.multResultRows(1).size)
    }

    @Test
    def test4x3values {
        val mpanel = MainPanel(VInt(7,3,2,2), VInt(6,1,2), new JPanel, null)

        var ctf = mpanel.carrysRows(0)(0)
        assertEquals(Some(1), ctf.expectedVal)
        assertEquals(Some(0), ctf.carryOffset)
        ctf = mpanel.carrysRows(0)(1)
        assertEquals(None, ctf.expectedVal)
        assertEquals(Some(0), ctf.carryOffset)
        ctf = mpanel.carrysRows(0)(2)
        assertEquals(None, ctf.expectedVal)
        assertEquals(Some(0), ctf.carryOffset)
        ctf = mpanel.carrysRows(0)(3)
        assertEquals(None, ctf.expectedVal)
        assertEquals(Some(0), ctf.carryOffset)
        ctf = mpanel.carrysRows(0)(4)
        assertEquals(None, ctf.expectedVal)
        assertEquals(Some(0), ctf.carryOffset)

        ctf = mpanel.carrysRows(1)(0)
        assertEquals(None, ctf.expectedVal)
        assertEquals(Some(1), ctf.carryOffset)
        ctf = mpanel.carrysRows(1)(1)
        assertEquals(None, ctf.expectedVal)
        assertEquals(Some(1), ctf.carryOffset)
        ctf = mpanel.carrysRows(1)(2)
        assertEquals(None, ctf.expectedVal)
        assertEquals(Some(1), ctf.carryOffset)
        ctf = mpanel.carrysRows(1)(3)
        assertEquals(None, ctf.expectedVal)
        assertEquals(Some(1), ctf.carryOffset)
        ctf = mpanel.carrysRows(1)(4)
        assertEquals(None, ctf.expectedVal)
        assertEquals(Some(1), ctf.carryOffset)

        ctf = mpanel.carrysRows(2)(0)
        assertEquals(Some(4), ctf.expectedVal)
        assertEquals(Some(2), ctf.carryOffset)
        ctf = mpanel.carrysRows(2)(1)
        assertEquals(Some(1), ctf.expectedVal)
        assertEquals(Some(2), ctf.carryOffset)
        ctf = mpanel.carrysRows(2)(2)
        assertEquals(Some(1), ctf.expectedVal)
        assertEquals(Some(2), ctf.carryOffset)
        ctf = mpanel.carrysRows(2)(3)
        assertEquals(Some(1), ctf.expectedVal)
        assertEquals(Some(2), ctf.carryOffset)
        ctf = mpanel.carrysRows(2)(4)
        assertEquals(None, ctf.expectedVal)
        assertEquals(Some(2), ctf.carryOffset)

        var ntf = mpanel.numberRows(0)(0)
        assertEquals("7", ntf.getText)
        ntf = mpanel.numberRows(0)(1)
        assertEquals("3", ntf.getText)
        ntf = mpanel.numberRows(0)(2)
        assertEquals("2", ntf.getText)
        ntf = mpanel.numberRows(0)(3)
        assertEquals("2", ntf.getText)

        ntf = mpanel.numberRows(1)(0)
        assertEquals("6", ntf.getText)
        ntf = mpanel.numberRows(1)(1)
        assertEquals("1", ntf.getText)
        ntf = mpanel.numberRows(1)(2)
        assertEquals("2", ntf.getText)

        var rtf = mpanel.multResultRows(0)(0)
        assertEquals(1, rtf.expectedVal)
        rtf = mpanel.multResultRows(0)(1)
        assertEquals(4, rtf.expectedVal)
        rtf = mpanel.multResultRows(0)(2)
        assertEquals(6, rtf.expectedVal)
        rtf = mpanel.multResultRows(0)(3)
        assertEquals(4, rtf.expectedVal)
        rtf = mpanel.multResultRows(0)(4)
        assertEquals(4, rtf.expectedVal)

        rtf = mpanel.multResultRows(1)(0)
        assertEquals(7, rtf.expectedVal)
        rtf = mpanel.multResultRows(1)(1)
        assertEquals(3, rtf.expectedVal)
        rtf = mpanel.multResultRows(1)(2)
        assertEquals(2, rtf.expectedVal)
        rtf = mpanel.multResultRows(1)(3)
        assertEquals(2, rtf.expectedVal)

        rtf = mpanel.multResultRows(2)(0)
        assertEquals(4, rtf.expectedVal)
        rtf = mpanel.multResultRows(2)(1)
        assertEquals(3, rtf.expectedVal)
        rtf = mpanel.multResultRows(2)(2)
        assertEquals(9, rtf.expectedVal)
        rtf = mpanel.multResultRows(2)(3)
        assertEquals(3, rtf.expectedVal)
        rtf = mpanel.multResultRows(2)(4)
        assertEquals(2, rtf.expectedVal)
    }

    @Test
    def test4x3positions {
        val mpanel = MainPanel(VInt(7,3,2,2), VInt(6,1,2), new JPanel, null)

        var ctf = mpanel.carrysRows(0)(0)
        assertEquals((carrysStartRow, mpanel.numCols-4), (ctf.row, ctf.col))
        ctf = mpanel.carrysRows(0)(1)
        assertEquals((carrysStartRow, mpanel.numCols-3), (ctf.row, ctf.col))
        ctf = mpanel.carrysRows(0)(2)
        assertEquals((carrysStartRow, mpanel.numCols-2), (ctf.row, ctf.col))
        ctf = mpanel.carrysRows(0)(3)
        assertEquals((carrysStartRow, mpanel.numCols-1), (ctf.row, ctf.col))
        ctf = mpanel.carrysRows(0)(4)
        assertEquals((carrysStartRow, mpanel.numCols), (ctf.row, ctf.col))

        ctf = mpanel.carrysRows(1)(0)
        assertEquals((carrysStartRow, mpanel.numCols-4), (ctf.row, ctf.col))
        ctf = mpanel.carrysRows(1)(1)
        assertEquals((carrysStartRow, mpanel.numCols-3), (ctf.row, ctf.col))
        ctf = mpanel.carrysRows(1)(2)
        assertEquals((carrysStartRow, mpanel.numCols-2), (ctf.row, ctf.col))
        ctf = mpanel.carrysRows(1)(3)
        assertEquals((carrysStartRow, mpanel.numCols-1), (ctf.row, ctf.col))
        ctf = mpanel.carrysRows(1)(4)
        assertEquals((carrysStartRow, mpanel.numCols), (ctf.row, ctf.col))

        ctf = mpanel.carrysRows(2)(0)
        assertEquals((carrysStartRow, mpanel.numCols-4), (ctf.row, ctf.col))
        ctf = mpanel.carrysRows(2)(1)
        assertEquals((carrysStartRow, mpanel.numCols-3), (ctf.row, ctf.col))
        ctf = mpanel.carrysRows(2)(2)
        assertEquals((carrysStartRow, mpanel.numCols-2), (ctf.row, ctf.col))
        ctf = mpanel.carrysRows(2)(3)
        assertEquals((carrysStartRow, mpanel.numCols-1), (ctf.row, ctf.col))
        ctf = mpanel.carrysRows(2)(4)
        assertEquals((carrysStartRow, mpanel.numCols), (ctf.row, ctf.col))

        var ntf = mpanel.numberRows(0)(0)
        assertEquals((numStartRow, mpanel.numCols-3), (ntf.row, ntf.col))
        ntf = mpanel.numberRows(0)(1)
        assertEquals((numStartRow, mpanel.numCols-2), (ntf.row, ntf.col))
        ntf = mpanel.numberRows(0)(2)
        assertEquals((numStartRow, mpanel.numCols-1), (ntf.row, ntf.col))
        ntf = mpanel.numberRows(0)(3)
        assertEquals((numStartRow, mpanel.numCols), (ntf.row, ntf.col))

        ntf = mpanel.numberRows(1)(0)
        assertEquals((numStartRow+1, mpanel.numCols-2), (ntf.row, ntf.col))
        ntf = mpanel.numberRows(1)(1)
        assertEquals((numStartRow+1, mpanel.numCols-1), (ntf.row, ntf.col))
        ntf = mpanel.numberRows(1)(2)
        assertEquals((numStartRow+1, mpanel.numCols), (ntf.row, ntf.col))

        var rtf = mpanel.multResultRows(0)(0)
        assertEquals((resultStartRow, mpanel.numCols-4), (rtf.row, rtf.col))
        rtf = mpanel.multResultRows(0)(1)
        assertEquals((resultStartRow, mpanel.numCols-3), (rtf.row, rtf.col))
        rtf = mpanel.multResultRows(0)(2)
        assertEquals((resultStartRow, mpanel.numCols-2), (rtf.row, rtf.col))
        rtf = mpanel.multResultRows(0)(3)
        assertEquals((resultStartRow, mpanel.numCols-1), (rtf.row, rtf.col))
        rtf = mpanel.multResultRows(0)(4)
        assertEquals((resultStartRow, mpanel.numCols), (rtf.row, rtf.col))

        rtf = mpanel.multResultRows(1)(0)
        assertEquals((resultStartRow+1, mpanel.numCols-4), (rtf.row, rtf.col))
        rtf = mpanel.multResultRows(1)(1)
        assertEquals((resultStartRow+1, mpanel.numCols-3), (rtf.row, rtf.col))
        rtf = mpanel.multResultRows(1)(2)
        assertEquals((resultStartRow+1, mpanel.numCols-2), (rtf.row, rtf.col))
        rtf = mpanel.multResultRows(1)(3)
        assertEquals((resultStartRow+1, mpanel.numCols-1), (rtf.row, rtf.col))

        rtf = mpanel.multResultRows(2)(0)
        assertEquals((resultStartRow+2, mpanel.numCols-6), (rtf.row, rtf.col))
        rtf = mpanel.multResultRows(2)(1)
        assertEquals((resultStartRow+2, mpanel.numCols-5), (rtf.row, rtf.col))
        rtf = mpanel.multResultRows(2)(2)
        assertEquals((resultStartRow+2, mpanel.numCols-4), (rtf.row, rtf.col))
        rtf = mpanel.multResultRows(2)(3)
        assertEquals((resultStartRow+2, mpanel.numCols-3), (rtf.row, rtf.col))
        rtf = mpanel.multResultRows(2)(4)
        assertEquals((resultStartRow+2, mpanel.numCols-2), (rtf.row, rtf.col))
    }

    @Test
    def test4x3sizes {
        val mpanel = MainPanel(VInt(7,3,2,2), VInt(6,1,2), new JPanel, null)
        assertEquals(8, mpanel.numCols)

        assertEquals(3, mpanel.carrysRows.size)
        assertEquals(5, mpanel.carrysRows(0).size)
        assertEquals(5, mpanel.carrysRows(1).size)
        assertEquals(5, mpanel.carrysRows(2).size)

        assertEquals(2, mpanel.numberRows.size)
        assertEquals(4, mpanel.numberRows(0).size)
        assertEquals(3, mpanel.numberRows(1).size)

        assertEquals(3, mpanel.multResultRows.size)
        assertEquals(5, mpanel.multResultRows(0).size)
        assertEquals(4, mpanel.multResultRows(1).size)
        assertEquals(5, mpanel.multResultRows(2).size)
    }
}
