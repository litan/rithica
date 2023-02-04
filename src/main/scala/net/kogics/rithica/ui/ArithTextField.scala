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

import net.kogics.rithica.core.Predef._

import java.awt._
import java.awt.event._
import javax.swing._

class ArithTextField extends JTextField(3) {

    setFont(new Font("Sans Serif", Font.BOLD, 17))
    setHorizontalAlignment(SwingConstants.CENTER)
    setRequestFocusEnabled(false)
    setOpaque(false)
    setBackground(ColorConstants.GameBackground)

    override def paint(g: Graphics) {
        paintComponent(g)
    }
    
    def plainPaintComponent(g: Graphics) {
        super.paintComponent(g)
    }

    override def paintComponent(g: Graphics) {
        // Enable text antialiasing
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                           RenderingHints.VALUE_TEXT_ANTIALIAS_ON)

        val w = 2
        drawBackground(w, g)

        super.paintComponent(g);

        // Enable antialiasing
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                           RenderingHints.VALUE_ANTIALIAS_ON)
        val cs = getText

        customizeGraphics(g)

        g.setStroke(new BasicStroke(w, BasicStroke.CAP_ROUND,
                                    BasicStroke.JOIN_MITER))

        g.drawRoundRect(w, w, getWidth-2*w, getHeight-2*w, 4*w, 4*w);
    }

    def drawBackground(w: Int, g: Graphics) {
        g.setBackground(getBackground);
        g.clearRect(w+1, w+1, getWidth-2*w-1, getHeight-2*w-1)
    }

    def customizeGraphics(g: Graphics) {
        g.setColor(ColorConstants.InactiveOutline)
    }
}
