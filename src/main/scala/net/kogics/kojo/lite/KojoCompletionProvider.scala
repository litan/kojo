package net.kogics.kojo
package lite

import java.awt.Point
import java.util.List
import java.util.logging.Logger

import javax.swing.text.JTextComponent

import org.fife.ui.autocomplete.Completion
import org.fife.ui.autocomplete.CompletionCellRenderer
import org.fife.ui.autocomplete.CompletionProviderBase
import org.fife.ui.autocomplete.TemplateCompletion

import net.kogics.kojo.core.CompletionInfo
import net.kogics.kojo.xscala.CodeCompletionUtils
import net.kogics.kojo.xscala.CodeTemplates
import net.kogics.kojo.xscala.Help

import core.MemberKind.Class
import core.MemberKind.Def
import core.MemberKind.Object
import core.MemberKind.Package
import core.MemberKind.PackageObject
import core.MemberKind.Trait
import core.MemberKind.Type
import core.MemberKind.Val
import core.MemberKind.Var
import util.Utils

class KojoCompletionProvider(execSupport: CodeExecutionSupport) extends CompletionProviderBase {
  val Log = Logger.getLogger(getClass.getName)

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
    import core.MemberKind._
    def kind = completion.kind match {
      case Class         => CLASS
      case Trait         => CLASS
      case Type          => CLASS
      case Object        => PACKAGE
      case Package       => PACKAGE
      case PackageObject => PACKAGE
      case Def           => METHOD
      case Val           => VARIABLE
      case Var           => VARIABLE
    }

    val display = completion.fullCompletion

    val knownNames = Set(
      "react(net.kogics.kojo.core.Picture => Unit): Unit",
      "act(net.kogics.kojo.core.Turtle => Unit): Unit",
      "react(net.kogics.kojo.core.Turtle => Unit): Unit",
      "setCostume(String): Unit",
      "setCostumes(String*): Unit",
      "nextCostume(): Unit",
      "scaleCostume(Double): Unit",
      "changePosition(Double, Double): Unit",
      "distanceTo(net.kogics.kojo.core.Turtle): Double",
      "perimeter: Double",
      "area: Double",
      "onMouseClick((Double, Double) => Unit): Unit",
      "onMousePress((Double, Double) => Unit): Unit",
      "onMouseDrag((Double, Double) => Unit): Unit",
      "animate(=> Unit): Unit"
    )

    def knownMethodTemplate: Option[String] = {
      //      println("%s signature - %s " format(completion.name, completion.signature))
      if (knownNames contains completion.signature) {
        val template = methodTemplate(completion.name)
        if (template != null) Some(template) else None
      }
      else {
        None
      }
    }

    def template =
      knownMethodTemplate.getOrElse(s"${completion.name}${completion.templateParams}")

    def knownHelp: Option[String] = {
      if (knownNames contains completion.signature) {
        val help = Help(completion.name)
        if (help != null) Some(help) else None
      }
      else {
        None
      }
    }

    def help =
      knownHelp.getOrElse(completion.fullCompletion)

    new TemplateCompletion(this, display, display, rtsaTemplate(template), null, help) {
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
    Utils.loadIconC(fname)
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
        val (varCompletions, voffset) = execSupport.varCompletions(prefix)
        varCompletions.foreach { completion =>
          proposals.add(proposal(caretOffset - voffset, completion,
            VARIABLE,
            methodTemplate(completion)))
        }

        val (memberCompletions, coffset) = execSupport.memberCompletions(caretOffset, null, prefix)
        memberCompletions.foreach { completion =>
          try {
            proposals.add(proposal2(caretOffset - coffset, completion))
          }
          catch {
            case t: Throwable =>
              /*Log.warning*/ println(s"Completion Problem for: ${completion.name} -- ${t.getMessage()}")
          }
        }

        val (keywordCompletions, koffset) = execSupport.keywordCompletions(prefix)
        keywordCompletions.foreach { completion =>
          proposals.add(proposal(caretOffset - koffset, completion,
            KEYWORD,
            CodeCompletionUtils.keywordTemplate(completion)))
        }

        addTemplateProposals(proposals, prefix.getOrElse(""), caretOffset)
      }
      else {
        val (memberCompletions, coffset) = execSupport.memberCompletions(caretOffset, objid.get, prefix)
        memberCompletions.foreach { completion =>
          proposals.add(proposal2(caretOffset - coffset, completion))
        }
      }
    }
    catch {
      case t: Throwable =>
        /*Log.warning*/ println("Completion Problem 2: " + t.getMessage())
    }

    proposals
  }

  override def getAlreadyEnteredText(comp: JTextComponent) = {
    if (execSupport.startingUp || !execSupport.isRunningEnabled) {
      ""
    }
    else {
      val caretOffset = comp.getCaretPosition
      val (oid, pfx) = execSupport.objidAndPrefix(caretOffset)
      objid = oid; prefix = pfx
      prefix.getOrElse("")
    }
  }

  override def getCompletionsImpl(comp: JTextComponent) = {
    if (execSupport.startingUp) {
      val proposals = new java.util.ArrayList[Completion]
      val completion = "Please try again soon..."
      proposals.add(new TemplateCompletion(this, completion, completion, "${cursor}", null,
        "Kojo is starting up, and the Code Completion Engine is not available yet."))
      proposals
    }
    else if (execSupport.isRunningEnabled) {
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