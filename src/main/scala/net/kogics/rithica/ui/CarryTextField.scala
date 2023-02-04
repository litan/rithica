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

class CarryTextField(parentPanel: InputNumberFieldCtx, val expectedVal: Option[Int])
extends InputNumberTextField(parentPanel) {

    private var _lastField: Boolean = false
    
    def lastField = _lastField
    def lastField_=(b: Boolean) {
      _lastField = b
      this.setVisible(false)
    }

    override def wantsFocus: Boolean = expectedVal match {
        case None => false
        case Some(_) if lastField => false
        case _ => true
    }
    
    override def matchesExpected(cs: String): Boolean = expectedVal match {
        case None => cs.length == 0
        case Some(_) if lastField => true
        case Some(n) => cs.length > 0 && cs.toInt == n
    }
}
