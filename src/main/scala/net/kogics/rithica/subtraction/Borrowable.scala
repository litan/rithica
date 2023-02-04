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

package net.kogics.rithica
package subtraction

import ui._

import java.awt._
import javax.swing._
import java.awt.event._

import net.kogics.rithica.core.Predef._

trait Borrowable extends NumberDisplayField with ActionListener {
    val nextBorrowable: Option[NumberDisplayField with Borrowable]
    val needsBorrow: Boolean
    val aboveField: InputNumberTextField
    var borrowedFrom = false

    val popupB = new JPopupMenu
    val menuItem = new JMenuItem("Borrow from here");
    menuItem.addActionListener(this);
    popupB.add(menuItem);

    val popupNb = new JPopupMenu
    val menuItem2 = new JMenuItem("Borrowing not allowed");
    menuItem2.setEnabled(false)
    menuItem2.addActionListener(this);
    popupNb.add(menuItem2);


    addMouseListener(new MouseAdapter {
            override def mousePressed(e: MouseEvent) {
                if(nextBorrowable.isDefined
                   && nextBorrowable.get.needsBorrow
                   && !borrowedFrom
                   && getText.toInt > 0) {
                    popupB.show(e.getComponent(),
                                e.getX(), e.getY())
                }
                else {
                    popupNb.show(e.getComponent(),
                                 e.getX(), e.getY())
                }
            }
        })

    override def actionPerformed(e: ActionEvent) {
        val num = getText
        aboveField.setText((num.toInt - 1).toString)
        borrowedFrom = true
        nextBorrowable.get.acceptBorrow()
        repaint()
    }

    def acceptBorrow() {
        val tf = if (borrowedFrom) aboveField else this
        val num = tf.getText
        tf.setText((num.toInt + 10).toString)
        tf.repaint()
    }

    abstract override def paintComponent(g: Graphics) {
        setSelectionStart(0)
        setSelectionEnd(0)
        super.paintComponent(g)
        if (borrowedFrom) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                               RenderingHints.VALUE_ANTIALIAS_ON)
            g.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND,
                                        BasicStroke.JOIN_MITER))
            g.setColor(Color.gray)
            g.drawLine(0, getHeight, getWidth, 0)
        }
    }
}
