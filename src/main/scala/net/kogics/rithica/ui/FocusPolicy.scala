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
import java.util.List

class FocusPolicy(order: List[InputNumberTextField]) extends FocusTraversalPolicy {

    def getComponentAfter(focusCycleRoot: Container,
                          aComponent: Component): Component = {
        val idx = (order.indexOf(aComponent) + 1) % order.size
        order.get(idx)
    }

    def getComponentBefore(focusCycleRoot: Container ,
                           aComponent: Component): Component = {
        val idx = order.indexOf(aComponent) - 1
        val ridx = if (idx < 0) order.size - 1 else idx
        order.get(ridx)
    }

    def getDefaultComponent(focusCycleRoot: Container): Component = {
        order.get(0)
    }

    def getLastComponent(focusCycleRoot: Container): Component = {
        order.get(order.size - 1)
    }

    def getFirstComponent(focusCycleRoot: Container): Component = {
        order.get(0)
    }
}
