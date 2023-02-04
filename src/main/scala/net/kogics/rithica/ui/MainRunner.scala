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
import java.awt.Dimension

import javax.swing.JFrame
import javax.swing.UIManager

import LayoutConstants.fullH
import LayoutConstants.fullW
import info.clearthought.layout.TableLayout

trait MainRunner {

  def container(jf: JFrame): TopContainer

  def main(args: Array[String]): Unit = {
    java.awt.EventQueue.invokeLater(new Runnable {
      def run() {

        UIManager.getInstalledLookAndFeels.find { _.getName == "Nimbus" }.foreach { nim =>
          UIManager.setLookAndFeel(nim.getClassName)
        }

        val jf = new JFrame();
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jf.getContentPane.setPreferredSize(new Dimension(fullW.toInt, fullH.toInt))
        val rowSpec = Array(fullH)
        val colSpec = Array(fullW)
        val lout = new TableLayout(colSpec, rowSpec)

        jf.getContentPane.setBackground(Color.white)
        jf.getContentPane.setLayout(lout)
        val cntr = container(jf)
        jf.getContentPane.add(cntr, "%d, %d, %d, %d, C, C" format (0, 0, 0, 0))

        jf.pack
        jf.setVisible(true);

        java.awt.EventQueue.invokeLater(new Runnable {
          def run() {
            cntr.focusGained()
          }
        })
      }
    });
  }
}
