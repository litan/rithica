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
import javax.swing._


class ImageButton(icon: ImageIcon) extends JButton(icon) {

    init()

    def this(img: String) = this(new ImageIcon(getClass().getResource(img)))

    def init() {
        setSize(icon.getImage().getWidth(null),
				icon.getImage().getHeight(null))
        setIcon(icon)
        setMargin(new Insets(0,0,0,0));
        setIconTextGap(2)
        // setBorderPainted(false)
        // setBorder(null);
        setText(null);
    }
}