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

package net.kogics.rithica.core

import net.kogics.rithica.core.Predef._
import net.kogics.collection.RichCollections._


import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert._

import org.jmock.Mockery
import org.jmock.lib.legacy.ClassImposteriser
import org.jmock.Expectations._
import org.jmock.Expectations
import org.jmock.api.Action

import java.util.Random

class VIntTest {

    val context: Mockery = new Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    @Test
    def testListCtor {
        val v = VInt(1,2,3)
        assertEquals(v.toInt, 123)
    }

    @Test
    def testSizeInitCtor {
        val v = VInt.fill(3,4)
        assertEquals(v.toInt, 444)
    }

    def returnConsecutiveValues[T] (values: List[T]) : Array[Action] =  {
        val actions = new Array[Action](values.size)
        var i = 0
        values.foreach(value => {actions(i) = Expectations.returnValue(value); i += 1})
        return actions
    }

    @Test
    def testSizeRandomCtorEasy {
        val rg = (context.mock(classOf[Random])).asInstanceOf[Random]

        context.checking(
            new Expectations() {
                {
                    atLeast(1).of(rg).nextInt(5)
                    will(onConsecutiveCalls(returnConsecutiveValues(List(1,2,3)): _*))
                }
            })

        val v = VInt(3, rg, VInt.Easy)
        assertTrue(v.toInt == 123)
    }

    @Test
    def testSizeRandomCtorMiddling {
        val rg = (context.mock(classOf[Random])).asInstanceOf[Random]

        context.checking(
            new Expectations() {
                {
                    atLeast(1).of(rg).nextInt(8)
                    will(onConsecutiveCalls(returnConsecutiveValues(List(4,5,6)): _*))
                }
            })

        val v = VInt(3, rg, VInt.Middling)
        assertTrue(v.toInt == 456)
    }

    @Test
    def testSizeRandomCtorHard {
        val rg = (context.mock(classOf[Random])).asInstanceOf[Random]

        context.checking(
            new Expectations() {
                {
                    atLeast(1).of(rg).nextInt(6)
                    will(onConsecutiveCalls(returnConsecutiveValues(List(2,3,4)): _*))
                }
            })

        val v = VInt(3, rg, VInt.Hard)
        assertTrue(v.toInt == 678)
    }

    @Test
    def testSizeRandomCtorMiddlingWithLeadingZero {
        val rg = (context.mock(classOf[Random])).asInstanceOf[Random]

        context.checking(
            new Expectations() {
                {
                    atLeast(1).of(rg).nextInt(8)
                    will(onConsecutiveCalls(returnConsecutiveValues(List(0,5,6)): _*))
                    one(rg).nextInt(4)
                    will(returnValue(2))
                }
            })

        val v = VInt(3, rg, VInt.Middling)
        assertEquals(356, v.toInt)
    }

    @Test
    def testEquals {
        val v = VInt(3,4)
        val v2 = VInt(3,4)
        val v3 = VInt(3,5)
        assertTrue(v == v2)
        assertFalse(v == v3)
    }

    @Test
    def testMultWithInt {
        val vi = VInt(1,2,3)
        assertEquals((VInt(2,4,6), Carrys(None, None, None, None)), vi * 2)
    }

    @Test
    def testMultWithIntWithCarry {
        val vi = VInt(4,7,9)
        assertEquals((VInt(1,4,3,7), Carrys(Some(1), Some(2), Some(2), None)), vi * 3)
    }

    @Test
    def testMultWithSmallVInt {
        val vi = VInt(1,2,3)
        assertEquals((ListJ(VInt(2,4,6)), ListJ(Carrys(None, None, None, None))), vi * VInt(2))
    }

    @Test
    def testMultWithSmallVIntWithCarry {
        val vi = VInt(4,7,9)
        assertEquals((ListJ(VInt(1,4,3,7)), ListJ(Carrys(Some(1), Some(2), Some(2), None))), vi * VInt(3))
    }

    @Test
    def testBuggyMultWithVIntWithCarry {
        val vi = VInt(7,9)
        assertEquals((ListJ(VInt(3,1,6)), ListJ(Carrys(Some(3), Some(3), None))), vi * VInt(4))
    }

    @Test
    def testMultWithVInt {
        val vi = VInt(1,2,3)
        assertEquals((ListJ(VInt(3,6,9), VInt(2,4,6)),
                      ListJ(Carrys(None, None, None, None), Carrys(None, None, None, None))),
                     vi * VInt(2,3))
    }

    @Test
    def testMultWithVIntWithCarry {
        val vi = VInt(4,7,9)
        assertEquals((ListJ(VInt(1,9,1,6), VInt(1,4,3,7)),
                      ListJ(Carrys(Some(1), Some(3), Some(3), None), Carrys(Some(1), Some(2), Some(2), None))),
                     vi * VInt(3,4))
    }

    @Test
    def testShift1 {
        val vi = VInt(1,2,3)
        assertEquals(VInt(1,2,3,0), vi.shift(1))
    }

    @Test
    def testShift2 {
        val vi = VInt(1,2,3)
        assertEquals(VInt(1,2,3,0,0), vi.shift(2))
    }

    @Test
    def testShift3 {
        val vi = VInt(1,2,3)
        assertEquals(VInt(1,2,3,0,0,0), vi.shift(3))
    }

    @Test
    def testMinus1 {
        assertEquals((VInt(1,2,3), ListJ(false, false, false)), VInt(6,7,8) - VInt(5,5,5))
    }

    @Test
    def testMinus2 {
        assertEquals((VInt(8,9), ListJ(false, true, true).reverse), VInt(5,6,7) - VInt(4,7,8))
    }

    @Test
    def testMinusTrickyBorrow {
        assertEquals((VInt(1,9,4,0), ListJ(false,true, true, false).reverse), VInt(4,6,1,6) - VInt(2,6,7,6))
    }

    @Test
    def testMinus3 {
        try {
            VInt(6,7,8) - VInt(7,5,5)
            fail("Subtraction that produces negative numbers is not allowed")
        }
        catch {
            case e: IllegalArgumentException => println("got expected IllegalArgumentException: %s" % e.getMessage)
        }
    }

    @Test
    def testPlus1 {
        assertEquals((VInt(6,7,8), Carrys(None, None, None, None)),  VInt(1,2,3) + VInt(5,5,5))
    }

    @Test
    def testPlus2 {
        assertEquals((VInt(5,6,7), Carrys(None, Some(1), Some(1), None)), VInt(8,9) + VInt(4,7,8))
    }

    @Test
    def testDiv1 {
        assertEquals((VInt(1,2,3), Carrys(None, None, None, None)),  VInt(2,4,6) / VInt(2))
    }

    @Test
    def testDiv2 {
        assertEquals((VInt(1,8,9), Carrys(None, Some(2), Some(2), None)),  VInt(5,6,7) / VInt(3))
    }

    @Test
    def testDiv3 {
        assertEquals((VInt(1,8,9), Carrys(None, Some(2), Some(2), Some(1))),  VInt(5,6,8) / VInt(3))
    }

    @Test
    def testDiv4 {
        assertEquals((VInt(0,4,2,8), Carrys(None, Some(3), Some(2), Some(6), Some(5))),  VInt(3,0,0,1) / VInt(7))
    }
}
