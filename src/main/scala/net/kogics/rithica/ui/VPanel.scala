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

import java.awt.AWTKeyStroke
import java.awt.Color
import java.awt.KeyboardFocusManager
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.util.HashSet
import java.util.List

import javax.swing.JPanel
import javax.swing.KeyStroke

import net.kogics.rithica.collection.Converter._

trait VPanel {
  this: JPanel =>

  setBackground(ColorConstants.GameBackground)
  val gradientStart: Color = new Color(0xfeffe3)

  def askForFocus

  def customizeFocusKeys {
    val set = getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS)
    val set2 = new HashSet[AWTKeyStroke](set)
    set2.add(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0))
    set2.add(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0))
    setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, set2)

    val bset = getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS)
    val bset2 = new HashSet[AWTKeyStroke](bset)
    bset2.add(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.SHIFT_DOWN_MASK))
    bset2.add(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.SHIFT_DOWN_MASK))
    setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, bset2)
  }

  def isInputRowFull[T <: InputNumberTextField](row: List[T]): Boolean = {
    row.foldLeft(true) { (b, tf) => b && !(tf.wantsFocus && tf.getText.length == 0) }
  }

  def numEmptyInInputRow[T <: InputNumberTextField](row: List[T]): Int = {
    row.foldLeft(0) { (num, tf) => if (tf.wantsFocus && tf.getText.length == 0) num + 1 else num }
  }

  def numWantingInputInRow[T <: InputNumberTextField](row: List[T]): Int = {
    row.foldLeft(0) { (num, tf) => if (tf.wantsFocus) num + 1 else num }
  }

  def isInputRowGood[T <: InputNumberTextField](row: List[T]): Boolean = {
    row.foldLeft(true) { (b, tf) => b && tf.isResultGood }
  }

}
