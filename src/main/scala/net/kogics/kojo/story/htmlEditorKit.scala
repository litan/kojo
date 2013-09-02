/*
 * Copyright (C) 2010 Lalit Pant <pant.lalit@gmail.com>
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

package net.kogics.kojo
package story

import java.awt.Color
import java.awt.Graphics
import java.awt.Insets
import java.awt.Rectangle
import java.awt.Shape

import org.scilab.forge.jlatexmath.ParseException
import org.scilab.forge.jlatexmath.TeXConstants
import org.scilab.forge.jlatexmath.TeXFormula

import javax.swing.JLabel
import javax.swing.text.Element
import javax.swing.text.Position
import javax.swing.text.StyleConstants
import javax.swing.text.View
import javax.swing.text.html.CSS
import javax.swing.text.html.HTML
import javax.swing.text.html.HTMLEditorKit

object CustomHtmlEditorKit {
  val latexPrefix = "latex://"
  util.Utils.runAsync {
    // warm up latex subsystem
    val formula = new TeXFormula("\text{y = m x + c}")
    formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 18)
  }
  // force usage of companion object - to warm up the latex subsystem
  def apply() = new CustomHtmlEditorKit()
}

class CustomHtmlEditorKit private extends HTMLEditorKit {
  override def getViewFactory() = new CustomHtmlFactory()
}

class CustomHtmlFactory extends HTMLEditorKit.HTMLFactory {
  
  override def create(elem: Element) = {
    def hasLatexAttr(e: Element) = e.getAttributes().getAttribute(HTML.Attribute.SRC) match {
      case src: String if (src.startsWith(CustomHtmlEditorKit.latexPrefix)) => true
      case _ => false
    }

    val o = elem.getAttributes().getAttribute(StyleConstants.NameAttribute)
    val src = elem.getAttributes().getAttribute(HTML.Attribute.SRC)
    o match {
      case kind: HTML.Tag if(kind == HTML.Tag.IMG && hasLatexAttr(elem)) => new LatexView(elem)
      case _ => super.create(elem)
    }
  }
}

class LatexView(elem: Element) extends View(elem) {
  val srcAttr = elem.getAttributes().getAttribute(HTML.Attribute.SRC).asInstanceOf[String]
  val size = elem.getAttributes().getAttribute(HTML.Attribute.HEIGHT).asInstanceOf[String].toInt
  val latex = srcAttr.substring(CustomHtmlEditorKit.latexPrefix.length, srcAttr.length) // strip off latex prefix
  val defColor = new Color(30, 30, 30)

  def colorAttr(e: Element) = e.getAttributes().getAttribute(CSS.Attribute.COLOR)
  val cae = colorAttr(elem)

  val color = if (cae != null) {
    ColorValue.parseCssValue(cae.toString).getValue
  }
  else {
    val cape = colorAttr(elem.getParentElement)
    if (cape != null) {
      ColorValue.parseCssValue(cape.toString).getValue
    }
    else {
      defColor
    }
  }

  val formula = try {
    new TeXFormula(latex)
  }
  catch {
    case pe: ParseException =>
      output("Incorrect formula - %s.\nProblem: %s" format(latex, pe.getMessage))
      new TeXFormula("\\text{Incorrect Formula. See output for details}")
  }
  val icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, size)
  icon.setInsets(new Insets(2, 2, 2, 2))
  val jl = new JLabel();
  jl.setForeground(color);

  def output(msg: String) {
    println(msg)
  }

  override def getPreferredSpan(axis: Int) = {
    axis match {
      case View.X_AXIS => icon.getIconWidth
      case View.Y_AXIS => icon.getIconHeight
    }
  }

  override def getMinimumSpan(axis: Int) = getPreferredSpan(axis)
  override def getMaximumSpan(axis: Int) = getPreferredSpan(axis)

  override def viewToModel(x: Float, y: Float, a: Shape, biasReturn: Array[Position.Bias]): Int = {
//    println("viewToModel(%f, %f, %s, %s)" format(x, y, a.toString, biasReturn.toString))
    val alloc = a.asInstanceOf[Rectangle];
    if (x < alloc.x + alloc.width) {
      biasReturn(0) = Position.Bias.Forward
      getStartOffset()
    }
    else {
      biasReturn(0) = Position.Bias.Backward
      getEndOffset()
    }
  }

  override def modelToView(pos: Int, a: Shape, b: Position.Bias): Shape = {
//    println("modelToView(%d, %s, %s)" format(pos, a.toString, b.toString))
    new Rectangle(0, 0, icon.getIconWidth, icon.getIconHeight)
  }

  override def paint(g: Graphics, a: Shape) {
    icon.paintIcon(jl, g, a.getBounds.x, a.getBounds.y);
  }
}
