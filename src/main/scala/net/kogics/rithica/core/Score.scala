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

import java.util.HashMap

object Score {
  var scoreListener: Option[ScoreListener] = None
  val gradeOrder = Array(APlus, A, B, C)
  val MaxGradeDelta = gradeOrder.size - 1
  sealed abstract class Grade() {
    def decrease(delta: Int): Grade = {
      def findIndexOf[A](arr: Array[A])(p: A => Boolean): Int = {
        var idx = 0
        var res = -1
        arr foreach { e =>
          if (p(e)) {
            res = idx
          }
          idx += 1
        }
        res
      }

      if (delta < 0 || delta > MaxGradeDelta) throw new IllegalArgumentException("Invalid Delta")
      val didx = findIndexOf(gradeOrder) { _ == this } + delta
      val nidx = if (didx > MaxGradeDelta) MaxGradeDelta else didx
      gradeOrder(nidx)
    }
  }
  case object APlus extends Grade { override def toString = "A+" }
  case object A extends Grade { override def toString = "A" }
  case object B extends Grade { override def toString = "B" }
  case object C extends Grade { override def toString = "C" }

  val gradeMap = new HashMap[Grade, (Int, Int, Int)]
  initGradeMap

  var errCount: (Int, Int, Int) = (0, 0, 0)
  var problemErrorCount = 0
  var problemComplexity: VInt.Complexity = null

  def initGradeMap {
    gradeMap.put(A, (0, 0, 0))
    gradeMap.put(APlus, (0, 0, 0))
    gradeMap.put(B, (0, 0, 0))
    gradeMap.put(C, (0, 0, 0))
  }

  def recordGrade(grade: Grade) {
    gradeMap.put(grade, incrGradeOrError(gradeMap.get(grade)))
    scoreListener.foreach { l => l.onScoreChange }
  }

  def incrGradeOrError(grades: (Int, Int, Int)): (Int, Int, Int) = problemComplexity match {
    case VInt.Easy     => (grades._1 + 1, grades._2, grades._3)
    case VInt.Middling => (grades._1, grades._2 + 1, grades._3)
    case VInt.Hard     => (grades._1, grades._2, grades._3 + 1)
  }

  def recordError {
    problemErrorCount += 1
    errCount = incrGradeOrError(errCount)
    scoreListener.foreach { l => l.onScoreChange }
  }

  def newProblem(complexity: VInt.Complexity) {
    problemErrorCount = 0
    problemComplexity = complexity
  }

  def clear {
    gradeMap.clear
    initGradeMap
    errCount = (0, 0, 0)
    problemErrorCount = 0
    scoreListener.foreach { l => l.onScoreChange }
  }

  def numAPlusS: (Int, Int, Int) = gradeMap.get(APlus)
  def numAs: (Int, Int, Int) = gradeMap.get(A)
  def numBs: (Int, Int, Int) = gradeMap.get(B)
  def numCs: (Int, Int, Int) = gradeMap.get(C)
}
