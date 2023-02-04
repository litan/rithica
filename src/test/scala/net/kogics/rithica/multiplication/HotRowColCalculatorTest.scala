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

package net.kogics
package rithica
package multiplication

import collection.RichCollections._
import core.Predef._

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert._


class HotRowColCalculatorTest {

    @Test
    def testSpot1 {
        val calci = new HotRowColCalculator(6, 4)
        assertEquals(ListJ((3,4), (2,4)), calci.calculate(6, 4, None))
        assertEquals(ListJ((3,4), (2,4)), calci.calculate(1, 3, Some(0)))
    }

    @Test
    def testSpot2 {
        val calci = new HotRowColCalculator(6, 4)
        assertEquals(ListJ((3,4), (2,3)), calci.calculate(6, 3, None))
        assertEquals(ListJ((3,4), (2,3)), calci.calculate(1, 2, Some(0)))
    }

    @Test
    def testSpot3 {
        val calci = new HotRowColCalculator(6, 4)
        assertEquals(ListJ((3,3), (2,4)), calci.calculate(7, 3, None))
        assertEquals(ListJ((3,3), (2,4)), calci.calculate(1, 3, Some(1)))
    }

    @Test
    def testSpot4 {
        val calci = new HotRowColCalculator(6, 4)
        assertEquals(ListJ((3,3), (2,3)), calci.calculate(7, 2, None))
        assertEquals(ListJ((3,3), (2,3)), calci.calculate(1, 2, Some(1)))
    }

    @Test
    def test7ColsSpot1 {
        val calci = new HotRowColCalculator(6, 7)
        assertEquals(ListJ((3,7), (2,7)), calci.calculate(6, 7, None))
        assertEquals(ListJ((3,7), (2,7)), calci.calculate(1, 6, Some(0)))
    }

    @Test
    def test7ColsSpot2 {
        val calci = new HotRowColCalculator(6, 7)
        assertEquals(ListJ((3,7), (2,6)), calci.calculate(6, 6, None))
        assertEquals(ListJ((3,7), (2,6)), calci.calculate(1, 5, Some(0)))
    }

    @Test
    def test7ColsSpot3 {
        val calci = new HotRowColCalculator(6, 7)
        assertEquals(ListJ((3,6), (2,7)), calci.calculate(7, 6, None))
        assertEquals(ListJ((3,6), (2,7)), calci.calculate(1, 6, Some(1)))
    }

    @Test
    def test7ColsSpot4 {
        val calci = new HotRowColCalculator(6, 7)
        assertEquals(ListJ((3,6), (2,6)), calci.calculate(7, 5, None))
        assertEquals(ListJ((3,6), (2,6)), calci.calculate(1, 5, Some(1)))
    }

    @Test
    def test6ColsSpot1 {
        // Test corresponding to one digit multiply
        val calci = new HotRowColCalculator(5, 6)
        assertEquals(ListJ((3,6), (2,6)), calci.calculate(5, 6, None))
        assertEquals(ListJ((3,6), (2,6)), calci.calculate(1, 5, Some(0)))
    }

}
