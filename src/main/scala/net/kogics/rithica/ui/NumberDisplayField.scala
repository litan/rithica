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

import java.awt.Color
import java.awt.Graphics
import java.awt.RenderingHints

import net.kogics.rithica.core.Predef.gToG2d

class NumberDisplayField extends ArithTextField {
  setForeground(ColorConstants.Number)
  setBackground(new Color(0, 0, 0, 0))

  override def paintComponent(g: Graphics) {
    // Enable text antialiasing
    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
      RenderingHints.VALUE_TEXT_ANTIALIAS_ON)

    super.plainPaintComponent(g);
  }
}
