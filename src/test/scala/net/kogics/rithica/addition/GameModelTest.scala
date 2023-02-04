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

import net.kogics.rithica.core._
import net.kogics.rithica.core.Predef._
import net.kogics.collection.RichCollections._
import java.util.List

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert._


class GameModelTest {

    @Test
    def testDoSumWithCarry {
        val num1 = VInt(1,9)
        val num2 = VInt(3,4)
        
        val model = new GameModel(ListJ(num1, num2))

        val (r, c) = model.doSum
        assertEquals(Carrys(None, Some(1), None), c)
        assertEquals(VInt(5, 3), r)
    }

    @Test
    def testDoSumWithNoCarry {
        val num1 = VInt(1,3,2)
        val num2 = VInt(2,4,4)

        val model = new GameModel(ListJ(num1, num2))

        val (r, c) = model.doSum
        assertEquals(Carrys(None, None, None, None), c)
        assertEquals(VInt(3,7,6), r)
    }

    @Test
    def testDoSumWithAllCarry {
        val num1 = VInt(6,7,8,9)
        val num2 = VInt(4,3,2,1)

        val model = new GameModel(ListJ(num1, num2))

        val (r, c) = model.doSum
        assertEquals(Carrys(Some(1), Some(1), Some(1), Some(1), None), c)
        assertEquals(VInt(1,1,1,1,0), r)
    }

    @Test
    def testJaggedDoSumWithCarry {
        val num1 = VInt(1,9,3)
        val num2 =   VInt(3,4)

        val model = new GameModel(ListJ(num1, num2))

        val (r, c) = model.doSum
        assertEquals(Carrys(None, Some(1), None, None), c)
        assertEquals(VInt(2,2,7), r)
    }

    @Test
    def testDoSumWithCarryDevBugHunt {
        val num1 = VInt(2,0,1,4,8)
        val num2 = VInt(7,2,9,2,2)

        val model = new GameModel(ListJ(num1, num2))

        val (r, c) = model.doSum
        assertEquals(Carrys(None, None, Some(1), None, Some(1), None), c)
        assertEquals(VInt(9, 3, 0, 7, 0), r)
    }

    @Test
    def testAddThreeNums {
        val num1 = VInt(6,3,1,2)
        val num2 = VInt(1,3,2,1)
        val num3 = VInt(2,3,3,4)

        val model = new GameModel(ListJ(num1, num2, num3))

        val (r, c) = model.doSum
        assertEquals(Carrys(None, None, None, None, None), c)
        assertEquals(VInt(9,9,6,7), r)
    }
}
