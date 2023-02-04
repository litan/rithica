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
    def testRowSpec2numRows {
        val ap = new MainPanel(2, 5, VInt.Middling, new JPanel, null)
        val expectedSpec = ListJ(b, p, vg, p, vg, p, vg, p, vg, p, vg, p, vg, p, b)
        assertEquals(expectedSpec, ap.layouter.rSpec)
    }

    @Test
    def testRowSpec4numRows {
        val ap = new MainPanel(4, 5, VInt.Middling, new JPanel, null)
        val expectedSpec = ListJ(b, p, vg, p, vg, p, vg, p, vg, p, vg, p, vg, p, vg, p, vg, p, b)
        assertEquals(expectedSpec, ap.layouter.rSpec)
    }

    @Test
    def testColSpec2Digits {
        val ap = new MainPanel(3, 2, VInt.Middling, new JPanel, null)
        val expectedSpec = ListJ(b, p, hg, p, hg, p, hg, p, b) // note: an extra p, hg for carryovers from the last digit
        assertEquals(expectedSpec, ap.layouter.colSpec)
    }

    @Test
    def testColSpec6Digits {
        val ap = new MainPanel(2, 6, VInt.Middling, new JPanel, null)
        val expectedSpec = ListJ(b, p, hg, p, hg, p, hg, p, hg, p, hg, p, hg, p, hg, p, b)
        assertEquals(expectedSpec, ap.layouter.colSpec)
    }

    @Test
    def testRows2Nums3Digits {
        val ap = new MainPanel(ListJ(VInt(1,2,3), VInt(8,7,9)), new JPanel, null)
        assertEquals("1", ap.numberRows(0)(0).getText)
        assertEquals("2", ap.numberRows(0)(1).getText)
        assertEquals("3", ap.numberRows(0)(2).getText)

        assertEquals("8", ap.numberRows(1)(0).getText)
        assertEquals("7", ap.numberRows(1)(1).getText)
        assertEquals("9", ap.numberRows(1)(2).getText)

        assertEquals(Some(1), ap.carrysRow(0).expectedVal)
        assertEquals(Some(1), ap.carrysRow(1).expectedVal)
        assertEquals(Some(1), ap.carrysRow(2).expectedVal)
        assertEquals(None, ap.carrysRow(3).expectedVal)

        assertEquals(1, ap.resultRow(0).expectedVal)
        assertEquals(0, ap.resultRow(1).expectedVal)
        assertEquals(0, ap.resultRow(2).expectedVal)
        assertEquals(2, ap.resultRow(3).expectedVal)
    }

    @Test
    def testRows4Nums5Digits {
        val ap = new MainPanel(ListJ(VInt(1,2,3,8,7), VInt(8,7,9,2), VInt(4,0,6),
                                    VInt(5,1,9,0,3)), new JPanel, null)

        assertTrue(ap.numberRows(0).size == 5)
        assertEquals("1", ap.numberRows(0)(0).getText)
        assertEquals("2", ap.numberRows(0)(1).getText)
        assertEquals("3", ap.numberRows(0)(2).getText)
        assertEquals("8", ap.numberRows(0)(3).getText)
        assertEquals("7", ap.numberRows(0)(4).getText)

        ap.numberRows(1).foreach {tf => println("t: " + tf.getText)}
        assertTrue(ap.numberRows(1).size == 4)
        assertEquals("8", ap.numberRows(1)(0).getText)
        assertEquals("7", ap.numberRows(1)(1).getText)
        assertEquals("9", ap.numberRows(1)(2).getText)
        assertEquals("2", ap.numberRows(1)(3).getText)

        assertTrue(ap.numberRows(2).size == 3)
        assertEquals("4", ap.numberRows(2)(0).getText)
        assertEquals("0", ap.numberRows(2)(1).getText)
        assertEquals("6", ap.numberRows(2)(2).getText)

        assertTrue(ap.numberRows(3).size == 5)
        assertEquals("5", ap.numberRows(3)(0).getText)
        assertEquals("1", ap.numberRows(3)(1).getText)
        assertEquals("9", ap.numberRows(3)(2).getText)
        assertEquals("0", ap.numberRows(3)(3).getText)
        assertEquals("3", ap.numberRows(3)(4).getText)

        assertTrue(ap.carrysRow.size == 6)
        assertEquals(None, ap.carrysRow(0).expectedVal)
        assertEquals(Some(1), ap.carrysRow(1).expectedVal)
        assertEquals(Some(2), ap.carrysRow(2).expectedVal)
        assertEquals(Some(1), ap.carrysRow(3).expectedVal)
        assertEquals(Some(1), ap.carrysRow(4).expectedVal)
        assertEquals(None, ap.carrysRow(5).expectedVal)

        assertTrue(ap.resultRow.size == 5)
        assertEquals(7, ap.resultRow(0).expectedVal)
        assertEquals(3, ap.resultRow(1).expectedVal)
        assertEquals(4, ap.resultRow(2).expectedVal)
        assertEquals(8, ap.resultRow(3).expectedVal)
        assertEquals(8, ap.resultRow(4).expectedVal)
    }
}
