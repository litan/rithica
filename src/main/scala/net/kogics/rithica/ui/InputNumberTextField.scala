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
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

import net.kogics.rithica.core.Score

trait InputNumberFieldCtx {
  def statusBar: VStatusBar
  def afterDigit(e: KeyEvent)

  def onGoodDigit(e: KeyEvent) {
    statusBar.setMessage("That's correct. Press Enter to move on...")
  }

  def onBadDigit(e: KeyEvent) {
    Score.recordError
    statusBar.setMessage("Whoops. That's not correct. Hit Backspace and Try again...")
  }

  def inProgressFocus(e: FocusEvent) {
    statusBar.clearMessage
    val tf = e.getSource.asInstanceOf[InputNumberTextField]
    if (tf.getText.size != 0) {
      if (tf.isResultGood) {
        val numMissing = numEmptyBoxes()
        val boxes = if (numMissing == 1) "Box" else "Boxes"
        val suffix = "Hit Enter to move closer to the empty %s." format (boxes)
        if (numMissing == 1) {
          statusBar.setMessage("You're almost done. There's just 1 more Orange Box to go. %s" format (suffix))
        }
        else if (numMissing <= numBoxes / 3) {
          statusBar.setMessage("You're getting close. There are just %d more Orange Boxes to go. %s" format (numMissing, suffix))
        }
        else if (numMissing <= numBoxes / 2) {
          statusBar.setMessage("You're doing well. There are just %d more Orange Boxes to go. %s" format (numMissing, suffix))
        }
      }
      else {
        statusBar.setMessage("You need to fix this one. Hit Backspace and Try again...")
      }
    }
  }

  def isDone(): Boolean
  def numEmptyBoxes(): Int
  val numBoxes: Int
}

class InputNumberTextField(parentPanel: InputNumberFieldCtx) extends ArithTextField {

  setRequestFocusEnabled(wantsFocus)
  setBackground(Color.white)

  def wantsFocus: Boolean = false

  def isResultGood: Boolean = {
    val cs = getText.trim
    return matchesExpected(cs)
  }

  def matchesExpected(cs: String): Boolean = true

  addKeyListener(new KeyAdapter {

    def isDigit(c: Char): Boolean = Character.isDigit(c)

    def isDelChar(c: Char): Boolean = {
      (c == KeyEvent.VK_BACK_SPACE) ||
        (c == KeyEvent.VK_DELETE)
    }

    def eatEvent(evt: KeyEvent) {
      if (!evt.isAltDown) getToolkit().beep(); // don't beep on mnemonics; need something more reliabel than this
      evt.consume()
    }

    override def keyReleased(evt: KeyEvent) {
      val c = evt.getKeyChar()
      if (isDigit(evt.getKeyChar)) {
        parentPanel.afterDigit(evt)
      }
    }

    override def keyTyped(evt: KeyEvent) {
      if (getText.length > 0) {
        eatEvent(evt)
      }
      else {
        val c = evt.getKeyChar()
        if (isDelChar(c)) {
          parentPanel.statusBar.clearMessage()
        }
        else if (isDigit(c)) {
          if (matchesExpected(c.toString)) {
            parentPanel.onGoodDigit(evt)
          }
          else {
            parentPanel.onBadDigit(evt)
          }
        }
        else {
          eatEvent(evt)
        }
      }
    }
  })

  addFocusListener(new FocusListener {
    override def focusGained(e: FocusEvent) {
      if (!parentPanel.isDone()) {
        parentPanel.inProgressFocus(e)
      }
      repaint()
    }

    override def focusLost(e: FocusEvent) {
      repaint()
    }
  })

  override def drawBackground(w: Int, g: Graphics) {
    if (wantsFocus) {
      super.drawBackground(w, g)
    }
  }

  override def customizeGraphics(g: Graphics) {
    val cs = getText
    if (isFocusOwner) {
      if (cs.trim.length > 0) {
        if (isResultGood) g.setColor(ColorConstants.CorrectOutline)
        else g.setColor(ColorConstants.Error)
      }
      else g.setColor(ColorConstants.Focus)
    }
    else if (wantsFocus) {
      if (cs.trim.length > 0 && !isResultGood) g.setColor(ColorConstants.Error)
      else if (cs.trim.length == 0 && !isResultGood) g.setColor(ColorConstants.Missing)
      else g.setColor(ColorConstants.CorrectOutline)
    }
    else g.setColor(ColorConstants.InputInactiveOutline)
  }
}
