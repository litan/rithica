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

import java.awt.BasicStroke
import java.awt.Font
import java.awt.Graphics
import java.awt.RenderingHints

import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.SwingConstants

import net.kogics.rithica.core.Predef.gToG2d

class VLabel extends JLabel {

    setBorder(BorderFactory.createEmptyBorder(6,2,6,2))
    setHorizontalAlignment(SwingConstants.CENTER)
    setVerticalAlignment(SwingConstants.CENTER)
    setFont(new Font("Monospaced", Font.BOLD, 15))
    setOpaque(false)
    setBackground(ColorConstants.ActiveBackground)

    var borderColor = ColorConstants.Border
    
    override def paintComponent(g: Graphics) {
        // Enable text antialiasing
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                           RenderingHints.VALUE_TEXT_ANTIALIAS_ON)

        val w = 2
        g.setBackground(getBackground)
        g.clearRect(w+1, w+1, getWidth-2*w-1, getHeight-2*w-1)

		super.paintComponent(g);

        // Enable antialiasing
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                           RenderingHints.VALUE_ANTIALIAS_ON)

        g.setStroke(new BasicStroke(w, BasicStroke.CAP_ROUND,
                                    BasicStroke.JOIN_MITER))

        g.setColor(borderColor)
        g.setStroke(new BasicStroke(w, BasicStroke.CAP_ROUND,
                                    BasicStroke.JOIN_MITER))
		// g.drawRect(w, w, getWidth-2*w, getHeight-2*w);
        g.drawRoundRect(w, w, getWidth-2*w, getHeight-2*w, 4*w, 4*w);
	}
}
