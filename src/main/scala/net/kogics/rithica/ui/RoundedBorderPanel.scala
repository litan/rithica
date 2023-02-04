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

abstract class RoundedBorderPanel extends JPanel {

    setOpaque(false)
    setBackground(Color.white)

    val gradientStart: Color

    override def paintComponent(g: Graphics) {
        // Enable text antialiasing
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                           RenderingHints.VALUE_TEXT_ANTIALIAS_ON)

        val w = 2
        // g.setBackground(getBackground)
        // g.clearRect(w+1, w+1, getWidth-2*w-1, getHeight-2*w-1)

        val p = new GradientPaint(0, 0, gradientStart,
                              0, getHeight(), getBackground);

        // Saves the state
        val oldPaint = g.getPaint();

        // Paints the background
        g.setPaint(p);
        g.fillRect(w+1, w+1, getWidth-2*w-1, getHeight-2*w-1)

        // Restores the state
        g.setPaint(oldPaint);

		super.paintComponent(g);

        val gTemp = g.create()

        // Enable antialiasing
        gTemp.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                               RenderingHints.VALUE_ANTIALIAS_ON)

        gTemp.setColor(ColorConstants.Border)
        gTemp.setStroke(new BasicStroke(w, BasicStroke.CAP_ROUND,
                                        BasicStroke.JOIN_MITER))
        gTemp.drawRoundRect(w, w, getWidth-2*w, getHeight-2*w, 4*w, 4*w);
        gTemp.dispose
	}
}
