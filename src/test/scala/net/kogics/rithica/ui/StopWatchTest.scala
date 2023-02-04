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

package net.kogics.rithica.ui

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert._

class StopWatchTest {
    val sw = new StopWatch

    @Test
    def testTimeConversion {
        assertEquals(0L, StopWatch.minsSecsToMillis("00:00"))
        assertEquals(15 * 1000L, StopWatch.minsSecsToMillis("00:15"))
        assertEquals(30 * 1000L, StopWatch.minsSecsToMillis("00:30"))
        assertEquals(45 * 1000L, StopWatch.minsSecsToMillis("00:45"))
        assertEquals(60 * 1000L, StopWatch.minsSecsToMillis("01:00"))
        assertEquals(120 * 1000L, StopWatch.minsSecsToMillis("02:00"))
        assertEquals(180 * 1000L, StopWatch.minsSecsToMillis("03:00"))
    }

    @Test
    def testNearestValidTime {
        assertEquals("00:15", StopWatch.nearestValidTime(10))
        assertEquals("00:30", StopWatch.nearestValidTime(20))
        assertEquals("00:30", StopWatch.nearestValidTime(30))
        assertEquals("00:45", StopWatch.nearestValidTime(40))
        assertEquals("01:45", StopWatch.nearestValidTime(91))
        assertEquals("01:45", StopWatch.nearestValidTime(105))
        assertEquals("02:00", StopWatch.nearestValidTime(106))
    }


}
