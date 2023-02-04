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
import info.clearthought.layout._
import LayoutConstants._
import net.kogics.rithica.core._
import net.kogics.rithica.core.Predef._

trait GameAppletRunner extends JApplet {

    def container(jf: JApplet): Container

    override def init() = {
        try {
            checkCodeBase
            getContentPane.setPreferredSize(new Dimension(fullW.toInt, fullH.toInt))
            val rowSpec = Array(fullH)
            val colSpec = Array(fullW)
            val lout = new TableLayout(colSpec, rowSpec)

            getContentPane.setBackground(Color.white)
            getContentPane.setLayout(lout)
            getContentPane.add(container(this), "%d, %d, %d, %d, C, C" format(0,0,0,0))
        }
        catch {
            case e: Exception => add(new ErrorPanel("This is an unauthorized use of the Game"))
        }
    }

    def checkCodeBase {
        val cb = getCodeBase
        checkState(cb.getProtocol == "http")
        checkState(cb.getHost.contains(NameObfuscator.Kogics))
    }
}
