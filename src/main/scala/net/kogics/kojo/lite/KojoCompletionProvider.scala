package net.kogics.kojo
package lite

import util.Utils
import org.fife.ui.autocomplete.CompletionProviderBase
import javax.swing.text.JTextComponent
import org.fife.ui.autocomplete.Completion
import java.util.ArrayList
import java.awt.Point
import org.fife.ui.autocomplete.BasicCompletion
import java.util.List
import java.util.ArrayList
import net.kogics.kojo.xscala.CodeCompletionUtils
import net.kogics.kojo.core.CompletionInfo
import org.fife.ui.autocomplete.TemplateCompletion
import org.fife.ui.autocomplete.CompletionCellRenderer
import com.sun.xml.internal.ws.server.UnsupportedMediaException
import net.kogics.kojo.xscala.Help
import net.kogics.kojo.xscala.CodeTemplates

class KojoCompletionProvider(completionSupport: core.CodeCompletionSupport) extends CompletionProviderBase {
  val METHOD = 10
  val VARIABLE = 9
  val CLASS = 8
  val PACKAGE = 7
  val KEYWORD = 6
  val TEMPLATE = 5

  setListCellRenderer(new CompletionCellRenderer)
  setAutoActivationRules(false, ".")

  var alreadyEntered = ""

  def rtsaTemplate(t: String) = {
    val res = if (t.contains("${")) t else "%s${cursor}" format (t)
    //    println("template: " + res)
    res
  }

  def proposal(offset: Int, completion: String, kind: Int, template: String) = {
    new TemplateCompletion(this, completion, completion, rtsaTemplate(if (template == null) completion else template),
      null, Help(completion)) {
      setRelevance(kind)
      override def getIcon = kindIcon(kind)
    }
  }

  def proposal2(offset: Int, completion: CompletionInfo) = {
    //    new BasicCompletion(this, completion.name, completion.name)
    def kind = {
      if (completion.isValue) {
        VARIABLE
      } else if (completion.isClass || completion.isType) {
        CLASS
      } else if (completion.isPackage) {
        PACKAGE
      } else {
        METHOD
      }
    }

    val valOrNoargItem = completion.isValue || completion.params.size == 0 && completion.ret != "Unit"

    def lhs: String = {
      val fm = new StringBuilder
      fm.append(completion.name)
      if (valOrNoargItem) {
        // do nothing
      } else if (completion.isClass) {
        fm.append(completion.params.mkString("[", ", ", "]"))
      } else {
        fm.append(completion.params.zip(completion.paramTypes).
          map { p => "%s: %s" format (p._1, p._2) }.
          mkString("(", ", ", ")"))
      }
      fm.toString
    }
    def rhs: String = completion.ret
    def defn = "%s : %s" format (lhs, rhs)
    def template = {
      val c0 = methodTemplate(completion.name)
      if (c0 != null) {
        c0
      } else {
        if (valOrNoargItem) {
          completion.name
        } else if (completion.isClass) {
          "%s[%s]" format (completion.name, completion.params.map { "${%s}" format (_) }.mkString(", "))
        } else {
          "%s(%s)" format (completion.name, completion.params.map { "${%s}" format (_) }.mkString(", "))
        }
      }
    }

    def signature = {
      val sb = new StringBuilder
      sb.append("<strong>%s</strong>" format (completion.name))
      if (valOrNoargItem) {
        // do nothing
      } else if (completion.isClass) {
        sb.append(completion.params.mkString("[", ", ", "]"))
      } else {
        sb.append(completion.params.zip(completion.paramTypes).
          map { p => "%s: %s" format (p._1, p._2) }.
          mkString("(", ", ", ")"))
      }
      sb.append(": ")
      sb.append("<em>%s</em>" format (completion.ret))
      sb.toString
    }

    def help = {
      val hlp = Help(completion.name)
      if (hlp != null) hlp else signature
    }

    //    println(template)
    new TemplateCompletion(this, defn, defn, rtsaTemplate(template), null, help) {
      setRelevance(-completion.prio)
      override def getIcon = kindIcon(kind)
    }
  }

  def kindIcon(kind: Int) = {
    val fname = kind match {
      case VARIABLE => "/images/kindvar.png"
      case CLASS => "/images/kindclass.png"
      case PACKAGE => "/images/kindpackage.gif"
      case METHOD => "/images/kindmethod.png"
      case KEYWORD => "/images/scala16x16.png"
      case TEMPLATE => "/images/kindtemplate.png"
    }
    Utils.loadIcon(fname, "Blah blah")

  }

  def methodTemplate(completion: String) = {
    CodeCompletionUtils.methodTemplate(completion)
  }

  def addTemplateProposals(proposals: java.util.ArrayList[Completion], prefix: String, caretOffset: Int) {
    CodeTemplates.templates.filter { kv => kv._1.startsWith(prefix)}.foreach { kv =>
      val name = kv._1; val value = kv._2
      proposals.add(
        new TemplateCompletion(this, name, name, value, null, CodeTemplates.asString(name)) {
          setRelevance(TEMPLATE)
          override def getIcon = kindIcon(TEMPLATE)
        })
    }
  }

  def complete(comp: JTextComponent): List[Completion] = {
    val proposals = new java.util.ArrayList[Completion]
    val caretOffset = comp.getCaretPosition

    val (objid, prefix) = completionSupport.objidAndPrefix(caretOffset)
    alreadyEntered = prefix.getOrElse("")

    if (objid.isEmpty) {
      val (varCompletions, voffset) = completionSupport.varCompletions(prefix)
      varCompletions.foreach { completion =>
        proposals.add(proposal(caretOffset - voffset, completion,
          VARIABLE,
          methodTemplate(completion)))
      }

      val (keywordCompletions, koffset) = completionSupport.keywordCompletions(prefix)
      keywordCompletions.foreach { completion =>
        proposals.add(proposal(caretOffset - koffset, completion,
          KEYWORD,
          CodeCompletionUtils.keywordTemplate(completion)))
      }

      addTemplateProposals(proposals, prefix.getOrElse(""), caretOffset)
    }

    if (objid.isDefined) {
      val (memberCompletions, coffset) = completionSupport.memberCompletions(caretOffset, objid.get, prefix)
      memberCompletions.foreach { completion =>
        proposals.add(proposal2(caretOffset - coffset, completion))
      }
    }

    proposals
  }

  override def getCompletionsImpl(comp: JTextComponent) = {
    complete(comp)
  }

  override def getParameterizedCompletions(comp: JTextComponent) = throw new UnsupportedOperationException
  override def getCompletionsAt(comp: JTextComponent, pt: Point) = throw new UnsupportedOperationException
  override def getAlreadyEnteredText(comp: JTextComponent) = {
    //    println("already entered: " + alreadyEntered)
    alreadyEntered
  }
}