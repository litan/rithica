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
package ui

import java.awt.Color
import java.awt.Dimension

import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JSeparator
import javax.swing.SwingConstants

trait PanelUtils {
    this: InputNumberFieldCtx =>

    val numCols: Int
    val layouter: LayoutHelper

    def addSign(sign: String, row: Int, col: Int, color: Color) {
        val tf = new NumberDisplayField
        tf.setText(sign)
        tf.setForeground(color)
        layouter.add(tf, row, 1/*col*/)
    }

    def addMultSign(row: Int, col: Int) {
        addSign("x", row, col, Color.black)
    }

    def addGrayMultSign(row: Int, col: Int) {
        addSign("x", row, col, ColorConstants.InactiveOutline)
    }

    def addPlusSign(row: Int, col: Int) {
        addSign("+", row, col, Color.black)
    }

    def addGrayPlusSign(row: Int, col: Int) {
        addSign("+", row, col, ColorConstants.InactiveOutline)
    }

    def addMinusSign(row: Int, col: Int) {
        addSign("-", row, col, Color.black)
    }

    def addSeparator(row: Int): Int = {
        addSeparator(row, 1, numCols)
    }

    def addSeparator(row: Int, startCol: Int, endCol: Int): Int = {
        val sep = new JSeparator
        sep.setBackground(ColorConstants.InactiveOutline)
        layouter.add(sep, row, startCol, row, endCol)
        row + 1
    }

    def addVerticalSeparator(row: Int, col: Int) {
        val sep = new JSeparator(SwingConstants.VERTICAL)
        sep.setBackground(ColorConstants.InactiveOutline)
        layouter.add(sep, row-1, col, row, col)
    }

    def addFiller(row: Int, pixels: Int): Int = {
        val filler = new JLabel
        filler.setPreferredSize(new Dimension(20, pixels))
        layouter.add(filler, row, 1, row, 1)
        row + 1
    }

    def addStatusBar(row: Int, statusBar: VStatusBar): Int = addStatusBar(row, numCols, statusBar)

    def addStatusBar(row: Int, endCol: Int, statusBar: VStatusBar): Int = {
        layouter.add(statusBar, row, 1, row, endCol)
        row + 1
    }

    def addInactiveInputRow(guiRow: Int): Int = {
        val startCol = 2
        for (i <- startCol to numCols) {
            val tf = new InputNumberTextField(this)
            tf.setName("%d, %d" format(guiRow, i))
            layouter.add(tf, guiRow, i)
        }
        guiRow + 1
    }

    def rowStartCol(size: Int) = {
        numCols - size + 1
    }

    def createImageIcon(path: String , description: String): ImageIcon = {
        val imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
