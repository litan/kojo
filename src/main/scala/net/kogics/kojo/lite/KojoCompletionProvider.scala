package net.kogics.kojo
package lite

import java.awt.Point
import java.util.logging.Logger
import java.util.List
import javax.swing.text.JTextComponent

import scala.collection.mutable

import net.kogics.kojo.core.CompletionInfo
import net.kogics.kojo.util.Utils
import net.kogics.kojo.xscala.CodeCompletionUtils
import net.kogics.kojo.xscala.CodeTemplates
import net.kogics.kojo.xscala.Help
import org.fife.ui.autocomplete.Completion
import org.fife.ui.autocomplete.CompletionCellRenderer
import org.fife.ui.autocomplete.CompletionProviderBase
import org.fife.ui.autocomplete.TemplateCompletion

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
    val res = if (t.contains("${")) t else "%s${cursor}".format(t)
    res
  }

  def proposal(offset: Int, completion: String, kind: Int, template: String) = {
    new TemplateCompletion(
      this,
      completion,
      completion,
      rtsaTemplate(if (template == null) completion else template),
      null,
      Help(completion)
    ) {
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

    val knownOwners = Set(
      "PicShape",
      "CorePicOps2",
      "Turtle",
      "TurtleMover",
      "InputAware",
      "Picture",
      "TraversableLike",
      "IterableLike",
      "VertexShapeSupport",
      "Kmath",
      "TurkishAPI"
    )

    lazy val ownerName = {
      val s = completion.owner
      if (s == null) {
        ""
      }
      else {
        val dotpos = s.lastIndexOf('.')
        if (dotpos == -1) s else s.substring(dotpos + 1)
      }
    }
    lazy val qualifiedName = s"${ownerName}.${completion.name}"

    def knownCompletion = knownOwners contains ownerName

    def specialOwner =
      ownerName.startsWith("Turtle") || ownerName.startsWith("VertexShapeSupport") || ownerName.startsWith("TurkishAPI")

    def knownMethodTemplate: Option[String] = {
      //      println(s"owner for ${completion.name} -- ${completion.owner}")
      if (knownCompletion) {
        var template = methodTemplate(qualifiedName)
        if (template == "") None
        else if (template != null) Some(template)
        else {
          if (specialOwner) {
            template = methodTemplate(completion.name)
            if (template != null) Some(template) else None
          }
          else {
            None
          }
        }
      }
      else {
        None
      }
    }

    def template =
      knownMethodTemplate.getOrElse(s"${completion.name}${completion.templateParams}")

    def knownHelp: Option[String] = {
      if (knownCompletion) {
        var help = Help(qualifiedName)
        if (help != null) Some(help)
        else {
          if (specialOwner) {
            help = Help(completion.name)
            if (help != null) Some(help) else None
          }
          else {
            None
          }
        }
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

  def addTemplateProposals(
      proposals: collection.mutable.ArrayBuffer[Completion],
      prefix: String,
      caretOffset: Int
  ): Unit = {
    CodeTemplates.templates.filter { kv => kv._1.startsWith(prefix) }.foreach { kv =>
      val name = kv._1; val value = kv._2
      proposals.append(
        new TemplateCompletion(this, name, name, value, null, CodeTemplates.asString(name)) {
          setRelevance(TEMPLATE)
          override def getIcon = kindIcon(TEMPLATE)
        }
      )
    }
  }

  var (objid: Option[String], prefix: Option[String]) = (None, None)

  def complete(comp: JTextComponent): List[Completion] = {
    val proposals = collection.mutable.ArrayBuffer.empty[Completion]
    val caretOffset = comp.getCaretPosition
    execSupport.kojoCtx.showAppWaitCursor()

    try {
      if (objid.isEmpty) {
        val (varCompletions, voffset) = execSupport.varCompletions(prefix)
        varCompletions.foreach { completion =>
          if (!completion.contains("$")) {
            proposals.append(proposal(caretOffset - voffset, completion, VARIABLE, methodTemplate(completion)))
          }
        }

        val (memberCompletions, coffset) = execSupport.memberCompletions(caretOffset, null, prefix)
        memberCompletions.foreach { completion =>
          try {
            proposals.append(proposal2(caretOffset - coffset, completion))
          }
          catch {
            case t: Throwable =>
              /*Log.warning*/
              println(s"Completion Problem for: ${completion.name} -- ${t.getMessage()}")
          }
        }

        val (keywordCompletions, koffset) = execSupport.keywordCompletions(prefix)
        keywordCompletions.foreach { completion =>
          proposals.append(
            proposal(caretOffset - koffset, completion, KEYWORD, CodeCompletionUtils.keywordTemplate(completion))
          )
        }

        addTemplateProposals(proposals, prefix.getOrElse(""), caretOffset)
      }
      else {
        val (memberCompletions, coffset) = execSupport.memberCompletions(caretOffset, objid.get, prefix)
        memberCompletions.foreach { completion =>
          proposals.append(proposal2(caretOffset - coffset, completion))
        }
      }
    }
    catch {
      case t: Throwable =>
        /*Log.warning*/
        println("Completion Problem 2: " + t.getMessage())
    }
    execSupport.kojoCtx.hideAppWaitCursor()
    val proposals2 = new java.util.ArrayList[Completion]
    val filterMap = mutable.HashMap.empty[String, TemplateCompletion]

    def bareName(c: TemplateCompletion) = {
      val name = c.getInputText
      val bn = name.split("""[\(:]""")(0)
      //      if (name == bn)
      //        name.split(""":""")(0)
      //      else
      //        bn
      bn
    }

    def hasHelp(c: TemplateCompletion) = {
      c.getSummary != null && c.getInputText != c.getSummary
    }

    def hasTemplate(c: TemplateCompletion) = c.getParamCount > 0

    def isInterpCompletion(c: TemplateCompletion) = {
      !c.getInputText.contains("(")
    }

    proposals.foreach { p =>
      val pp = p.asInstanceOf[TemplateCompletion]
      //      println("---")
      //      println(pp.getInputText)
      //      println(pp.getSummary)
      val bName = bareName(pp)

      filterMap.get(bName) match {
        case Some(c) =>
          // there is a previous proposal
          // does it have help?
          if (hasHelp(c)) {
            // don't add current proposal
          }
          else {
            // no help
            if (isInterpCompletion(c) && !hasTemplate(c)) {
              proposals2.remove(c)
            }
            proposals2.add(p)
            filterMap(bName) = pp
          }
        case None =>
          proposals2.add(p)
          filterMap(bName) = pp
      }
    }
    proposals2
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
      proposals.add(
        new TemplateCompletion(
          this,
          completion,
          completion,
          "${cursor}",
          null,
          "Kojo is starting up, and the Code Completion Engine is not available yet."
        )
      )
      proposals
    }
    else if (execSupport.isRunningEnabled) {
      complete(comp)
    }
    else {
      val proposals = new java.util.ArrayList[Completion]
      val completion = "Please try again soon..."
      proposals.add(
        new TemplateCompletion(
          this,
          completion,
          completion,
          "${cursor}",
          null,
          "The Code Completion Engine is currently blocked (probably because a script is running)."
        )
      )
      proposals
    }
  }

  override def getParameterizedCompletions(comp: JTextComponent) = throw new UnsupportedOperationException
  override def getCompletionsAt(comp: JTextComponent, pt: Point) = throw new UnsupportedOperationException
}
