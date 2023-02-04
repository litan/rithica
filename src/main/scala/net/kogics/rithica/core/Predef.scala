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

package net.kogics.rithica.core

import java.awt._

object Predef {
    
    implicit def gToG2d(g: Graphics): Graphics2D = g.asInstanceOf[Graphics2D]
//    implicit def strFmt(str: String): StringFormatter = new StringFormatter(str)

    def checkArg(assertion: Boolean) {
        if (!assertion)
        throw new IllegalArgumentException()
    }

    def checkArg(assertion: Boolean, msg: String, msgArgs: Any*) {
        if (!assertion)
        throw new IllegalArgumentException(msg format (msgArgs:_*))
    }

    def check(assertion: Boolean) {
        if (!assertion)
        throw new RuntimeException()
    }

    def check(assertion: Boolean, msg: String, msgArgs: Any*) {
        if (!assertion)
        throw new RuntimeException(msg format (msgArgs:_*))
    }

    def checkState(assertion: Boolean) {
        if (!assertion)
        throw new IllegalStateException()
    }

    def checkState(assertion: Boolean, msg: String, msgArgs: Any*) {
        if (!assertion)
        throw new IllegalStateException(msg format (msgArgs:_*))
    }
}
