/*
 * TestRichIterableOps.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.kogics.collection

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert._

import java.util.HashSet
import java.util.Iterator
import RichCollections._

class RichHashSetTest {

// val richSet = new RichHashSet[Int]
   val plainSet = new HashSet[Int]

   @Before
   def setupSet: Unit = {
     plainSet.add(1); plainSet.add(2); plainSet.add(3)
   }

   @Test
   def testFoldForSet = {
     assertEquals(6, plainSet.foldLeft(0)((x,y) => x+y))
   }

   @Test
   def testOriginalCollectionOpForSet = {
     assertTrue(plainSet.map(e => 2*e).contains(4))
   }

   @Test
   def testChainedRichOpForSet = {
     assertTrue(plainSet.map(e => 2*e).map(e => 2*e).contains(8))
   }

   @Test
   def testEqualityForSet = {
     val expected = new HashSet[Int]
     expected.add(2); expected.add(4); expected.add(6)
     assertEquals(expected, plainSet.map(e => 2*e))
   }

}
