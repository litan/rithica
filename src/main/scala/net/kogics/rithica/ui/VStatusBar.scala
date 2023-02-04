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
import java.awt.Dimension
import java.awt.Font
import java.awt.Graphics
import java.awt.RenderingHints

import javax.swing.BorderFactory
import javax.swing.JTextArea

import net.kogics.rithica.core.Predef.gToG2d

class VStatusBar extends JTextArea {

  setPreferredSize(new Dimension(300, 115))
  setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10))
  setLineWrap(true)
  setWrapStyleWord(true)
  setRequestFocusEnabled(false)
  setFont(new Font("Sans Serif", Font.BOLD, 12))
  setOpaque(false)

  override def paintComponent(g: Graphics) {
    // Enable text antialiasing
    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
      RenderingHints.VALUE_TEXT_ANTIALIAS_ON)

    val i = getInsets
    val w = 2

    g.setBackground(ColorConstants.ActiveBackground)
    g.clearRect(i.left - w - 2 + 1, i.top - w - 2 + 1,
      getWidth + 2 * w + 4 - (i.left + i.right) - 1, getHeight + 2 * w + 4 - (i.top + i.bottom) - 1)
    super.paintComponent(g)

    // Enable antialiasing
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON)

    g.setStroke(new BasicStroke(w, BasicStroke.CAP_ROUND,
      BasicStroke.JOIN_MITER))

    g.setColor(ColorConstants.StatusOutline)
    g.setStroke(new BasicStroke(w, BasicStroke.CAP_ROUND,
      BasicStroke.JOIN_MITER))
    g.drawRoundRect(i.left - w - 2, i.top - w - 2,
      getWidth + 2 * w + 4 - (i.left + i.right), getHeight + 2 * w + 4 - (i.top + i.bottom),
      5 * w, 5 * w);
  }

  def setMessage(mesg: String) {
    setText(mesg)
    repaint()
  }

  def clearMessage() {
    setText("")
    repaint()
  }

}
