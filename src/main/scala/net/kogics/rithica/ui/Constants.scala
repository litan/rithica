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

object ColorConstants {
    val CorrectOutline = new Color(0x64ff0a) // green
    val InactiveOutline = Color.lightGray
    val Border = new Color(0x7a8a99)
    val InputInactiveOutline = new Color(0xdcdcdc) // very light gray
    val Focus = new Color(0x00a0ff) // lightish blue
    val StatusOutline = new Color(0x00a0ff) // lightish blue
    val StatusText = Color.black
    val Error = Color.red
    val Missing = new Color(0xffaf00) // Orange
    val Number = new Color(0x3c3c3c) // very dark gray
    val GameBackground = new Color(0xfbff6d)
    val CPBackground = new Color(0xc3b24e)
    val ActiveBackground = new Color(0xf3f0dd)
}

object StringConstants {
    val YourName = "Your Name Here"
    val Congrats = "Congratulations! You've got it right. Your grade (based on time taken and mistakes made) is:\n\n\t%s"
    val Problem = "Uh Oh! There seems to be a problem. Identify the Red Boxes and fix them."
}
