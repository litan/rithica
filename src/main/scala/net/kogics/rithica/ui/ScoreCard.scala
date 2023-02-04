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


import java.awt._
import javax.swing._

import net.kogics.rithica.core.Predef._
import net.kogics.rithica.core._

class ScoreCard extends VStatusBar with ScoreListener {
    setPreferredSize(new Dimension(50, 125))
    setFont(new Font("Monospaced", Font.BOLD, 13))
    onScoreChange

    def onScoreChange {
        val score = new StringBuilder
        score.append("           DL1 DL2 DL3\n" format(Score.numAPlusS._1, Score.numAPlusS._2, Score.numAPlusS._3))
        score.append("   A+    - %3d %3d %3d\n" format(Score.numAPlusS._1, Score.numAPlusS._2, Score.numAPlusS._3))
        score.append("   A     - %3d %3d %3d\n" format(Score.numAs._1, Score.numAs._2, Score.numAs._3))
        score.append("   B     - %3d %3d %3d\n" format(Score.numBs._1, Score.numBs._2, Score.numBs._3))
        score.append("   C     - %3d %3d %3d\n" format(Score.numCs._1, Score.numCs._2, Score.numCs._3))
        score.append("Mistakes - %3d %3d %3d\n" format(Score.errCount._1, Score.errCount._2, Score.errCount._3))
        setMessage(score.toString)
    }
}
