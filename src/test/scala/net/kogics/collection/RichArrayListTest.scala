/*
 * RichArrayListTest.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.kogics.collection

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert._

import java.util.ArrayList
import java.util.Iterator
import RichCollections._

class RichArrayListTest {

    val plainList = new ArrayList[Int]

    @Before
    def setupSet: Unit = {
        plainList.add(1); plainList.add(2); plainList.add(3)
    }

    @Test
    def testFoldForSet = {
        assertEquals(6, plainList.foldLeft(0)((x,y) => x+y))
    }

    @Test
    def testOriginalCollectionOpForSet = {
        assertTrue(plainList.map(e => 2*e).contains(4))
    }

    @Test
    def testChainedRichOpForSet = {
        assertTrue(plainList.map(e => 2*e).map(e => 2*e).contains(8))
    }

    @Test
    def testEqualityForSet = {
        val expected = new ArrayList[Int]
        expected.add(2); expected.add(4); expected.add(6)
        assertEquals(expected, plainList.map(e => 2*e))
    }
}
