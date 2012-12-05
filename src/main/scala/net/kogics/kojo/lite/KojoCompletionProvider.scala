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

class KojoCompletionProvider(codeSupport: CodeExecutionSupport) extends CompletionProviderBase {
  val METHOD = 10
  val VARIABLE = 9
  val CLASS = 8
  val PACKAGE = 7
  val KEYWORD = 6
  val TEMPLATE = 5

  setListCellRenderer(new CompletionCellRenderer) // needed for icons to show up
  setAutoActivationRules(false, null)

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
    def kind = {
      if (completion.isClass || completion.isType) {
        CLASS
      }
      else if (completion.isPackage || completion.isObject) {
        PACKAGE
      }
      else if (completion.isMethod) {
        METHOD
      }
      else {
        VARIABLE
      }
    }

    val valOrNoargItem =
      (completion.isValue &&
        completion.member.tpe.resultType.toString != "Unit") ||
        completion.isPackage ||
        completion.isClass ||
        completion.isType ||
        completion.isObject

    def lhs: String = {
      val fm = new StringBuilder
      fm.append(completion.name)
      val tpe = completion.member.tpe
      if (tpe.typeParams.size > 0) {
        val completionTypeParams = tpe.typeParams.map(_.nameString.replace("$", ""))
        fm.append(completionTypeParams.mkString("[", ", ", "]"))
      }

      if (tpe.params.size > 0) {
        val completionParams = tpe.params.map(_.nameString.replace("$", ""))
        fm.append(completionParams.zip(tpe.paramTypes).
          map { p => "%s: %s" format (p._1, p._2) }.
          mkString("(", ", ", ")"))
      }
      else {
        if (!valOrNoargItem) {
          // it's a no-arg command
          fm.append("()")
        }
      }
      fm.toString
    }

    def rhs: String = if (completion.member.tpe.paramss.size > 1)
      completion.member.tpe.resultType.toString
    else
      completion.member.tpe.finalResultType.toString

    val defn = "%s : %s" format (lhs, rhs)
    def template = {
      val fm = new StringBuilder
      fm.append(completion.name)
      val tpe = completion.member.tpe
      if (tpe.typeParams.size > 0) {
        val completionTypeParams = tpe.typeParams.map(_.nameString.replace("$", ""))
        fm.append(completionTypeParams map { "${%s}" format (_) } mkString ("[", ", ", "]"))
      }
      if (tpe.params.size > 0) {
        val completionParams = completion.member.tpe.params.map(_.nameString.replace("$", ""))
        fm.append(completionParams map { "${%s}" format (_) } mkString ("(", ", ", ")"))
      }
      else {
        if (!valOrNoargItem) {
          // it's a no-arg command
          fm.append("()")
        }
      }
      fm.toString
    }

    def signature = "<strong>%s</strong> : <em>%s</em>" format (lhs, rhs)

    def help = signature

    //    println(template)
    new TemplateCompletion(this, defn, defn, rtsaTemplate(template), null, help) {
      setRelevance(-completion.prio)
      override def getIcon = kindIcon(kind)
    }
  }

  def kindIcon(kind: Int) = {
    val fname = kind match {
      case VARIABLE => "/images/kindvar.png"
      case CLASS    => "/images/kindclass.png"
      case PACKAGE  => "/images/kindpackage.gif"
      case METHOD   => "/images/kindmethod.png"
      case KEYWORD  => "/images/scala16x16.png"
      case TEMPLATE => "/images/kindtemplate.png"
    }
    Utils.loadIcon(fname, "Blah blah")

  }

  def methodTemplate(completion: String) = {
    CodeCompletionUtils.methodTemplate(completion)
  }

  def addTemplateProposals(proposals: java.util.ArrayList[Completion], prefix: String, caretOffset: Int) {
    CodeTemplates.templates.filter { kv => kv._1.startsWith(prefix) }.foreach { kv =>
      val name = kv._1; val value = kv._2
      proposals.add(
        new TemplateCompletion(this, name, name, value, null, CodeTemplates.asString(name)) {
          setRelevance(TEMPLATE)
          override def getIcon = kindIcon(TEMPLATE)
        })
    }
  }

  var (objid: Option[String], prefix: Option[String]) = (None, None)

  def complete(comp: JTextComponent): List[Completion] = {
    val proposals = new java.util.ArrayList[Completion]
    val caretOffset = comp.getCaretPosition

    try {
      if (objid.isEmpty) {
        val (varCompletions, voffset) = codeSupport.varCompletions(prefix)
        varCompletions.foreach { completion =>
          proposals.add(proposal(caretOffset - voffset, completion,
            VARIABLE,
            methodTemplate(completion)))
        }

        val (memberCompletions, coffset) = codeSupport.memberCompletions(caretOffset, null, prefix)
        memberCompletions.foreach { completion =>
          try {
            proposals.add(proposal2(caretOffset - coffset, completion))
          }
          catch {
            case t: Throwable =>
              println("Completion Problem 1: " + t.getMessage())
          }
        }

        val (keywordCompletions, koffset) = codeSupport.keywordCompletions(prefix)
        keywordCompletions.foreach { completion =>
          proposals.add(proposal(caretOffset - koffset, completion,
            KEYWORD,
            CodeCompletionUtils.keywordTemplate(completion)))
        }

        addTemplateProposals(proposals, prefix.getOrElse(""), caretOffset)
      }
      else {
        val (memberCompletions, coffset) = codeSupport.memberCompletions(caretOffset, objid.get, prefix)
        memberCompletions.foreach { completion =>
          proposals.add(proposal2(caretOffset - coffset, completion))
        }
      }
    }
    catch {
      case t: Throwable =>
        println("Completion Problem 2: " + t.getMessage())
    }

    proposals
  }

  override def getAlreadyEnteredText(comp: JTextComponent) = {
    if (codeSupport.startingUp || !codeSupport.isRunningEnabled) {
      ""
    }
    else {
      val caretOffset = comp.getCaretPosition
      val (oid, pfx) = codeSupport.objidAndPrefix(caretOffset)
      objid = oid; prefix = pfx
      prefix.getOrElse("")
    }
  }

  override def getCompletionsImpl(comp: JTextComponent) = {
    if (codeSupport.startingUp) {
      val proposals = new java.util.ArrayList[Completion]
      val completion = "Please try again soon..."
      proposals.add(new TemplateCompletion(this, completion, completion, "${cursor}", null,
        "Kojo is starting up, and the Code Completion Engine is not available yet."))
      proposals
    }
    else if (codeSupport.isRunningEnabled) {
      complete(comp)
    }
    else {
      val proposals = new java.util.ArrayList[Completion]
      val completion = "Please try again soon..."
      proposals.add(new TemplateCompletion(this, completion, completion, "${cursor}", null,
        "The Code Completion Engine is currently blocked (probably because a script is running)."))
      proposals
    }
  }

  override def getParameterizedCompletions(comp: JTextComponent) = throw new UnsupportedOperationException
  override def getCompletionsAt(comp: JTextComponent, pt: Point) = throw new UnsupportedOperationException
}