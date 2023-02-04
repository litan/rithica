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

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert._

class ScoreTest {

    @Test
    def testAPlusDecrease {
        assertEquals(Score.APlus, Score.APlus.decrease(0))
        assertEquals(Score.A, Score.APlus.decrease(1))
        assertEquals(Score.B, Score.APlus.decrease(2))
        assertEquals(Score.C, Score.APlus.decrease(3))
    }

    @Test
    def testADecrease {
        assertEquals(Score.A, Score.A.decrease(0))
        assertEquals(Score.B, Score.A.decrease(1))
        assertEquals(Score.C, Score.A.decrease(2))
        assertEquals(Score.C, Score.A.decrease(3))
    }

    @Test
    def testBDecrease {
        assertEquals(Score.B, Score.B.decrease(0))
        assertEquals(Score.C, Score.B.decrease(1))
        assertEquals(Score.C, Score.B.decrease(2))
        assertEquals(Score.C, Score.B.decrease(3))
    }

    @Test
    def testCDecrease {
        assertEquals(Score.C, Score.C.decrease(0))
        assertEquals(Score.C, Score.C.decrease(1))
        assertEquals(Score.C, Score.C.decrease(2))
        assertEquals(Score.C, Score.C.decrease(3))
    }

}
