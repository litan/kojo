// Copyright (C) 2012 Lalit Pant <pant.lalit@gmail.com>
// The contents of this file are subject to 
// the GNU General Public License Version 3 (http://www.gnu.org/copyleft/gpl.html)

val pageStyle = "background-color:#99CCFF; margin:15px;font-size:small;"
val centerStyle = "text-align:center;"
val headerStyle = "text-align:center;font-size:110%;color:maroon;"
val codeStyle = "font-size:90%;"
val smallNoteStyle = "color:dark-gray;font-size:95%;"
val sublistStyle = "margin-left:60px;"

def pgHeader(hdr: String) =
    <p style={ headerStyle }>
        { hdr }
        <hr/>
        <br/> 
    </p>

def term(t: Any) = <em><tt> { t.toString } </tt></em>
def term(t: Symbol) = <em><tt> { t.name } </tt></em>

var pages = Vector.empty[StoryPage]
var pg: StoryPage = _

pg = Page(
    name = "toc",
    body =
        <body style={ pageStyle }>
            { pgHeader("Linear Equations in one variable") }
            <ul>
                <li><a href="http://localpage/intro1">Introduction</a></li>
                <li><a href="http://localpage/practice">Practice</a></li>
            </ul>
        </body>,
    code = {}
)

pages = pages :+ pg

pg = IncrPage(
    name = "intro1",
    style = pageStyle,
    body = List(
        Para(
            <div>
            { pgHeader("Linear Equations in one variable") }
                This activity will help you learn how to solve linear equations in one variable.
            </div>
        ),
        Para(
            <p>
                Such equations are made out of two kinds of terms - terms that contain the variable
                <em><tt>x</tt></em> (raised to the power 1), and terms that are constants.
            </p>
        ),
        Para(
            <p>
                Here's an example of such an equation: { stFormula(demoEqn) }
            </p>
        ),
        Para(
            <p>
                This equation makes a mathematical statement about some number { term('x) }. It states that
               if you multiply { term('x) } by { term(7) }, and then add { term(10) } to the result, 
               you get { term(31) }.
            </p>
        ),
        Para(
            <p>
                How can you determine the value of x for which this equation is true (this value
               of x is said to satisfy the equation)?
            </p>
        ),
        Para(
            <p>
               By solving the equation. Read on...
            </p>
        )
    )
)

pages = pages :+ pg
val demoEqn = "7x + 10 = 31"
val demoEqn2 = "7x = 21"

pg = IncrPage(
    name = "intro2",
    style = pageStyle,
    body = List(
        Para(
            <div>
            { pgHeader("Solving an equation") }
                We will make use of the CAS (Computer Algebra System) view of Mathworld to play with,
                and solve, equations. <br/> 
                Can you see the CAS view within the Mathworld window?
            </div>,
            code = {
                Mw.clear()
                Mw.hideAlgebraView()
                Mw.showCASView()
            }
        ),
        Para(
            <p>
                First, let us load the equation from the previous page into the CAS view. Can you see the equation there?
            </p>,
            code = {
                Mw.casView.keep(demoEqn)
            }
        ),
        Para(
            <p>
            To solve an equation, you need to follow one basic rule. Whatever operation you do on the 
            left hand side (LHS) of the equation, you also need to do on the right hand side (RHS) of the equation. 
            This rule is based on the priciple that the same thing done to equal things results in equal things.
            </p>
        ),
        Para(
            <p>
                Using this rule, you can keep transforming the given equation till the value of the unknown 
                variable is known. The basic idea is to get all the terms containing 
                { term('x) } to the LHS of the equation, and all the terms not containing { term('x) } 
                to the RHS of the equation. Then you divide both sides of the equation by the 
                coefficient of { term('x) } to get the value of { term('x) }.
            </p>
        ),
        Para(
            <p>
                Let us try that here. The first step is to try to get rid of the number { term(10) } from the LHS 
                of the equation. <br/><br/> 
                How can you do this? 
            </p>
        ),
        Para(
            <p>
                By adding { term(-10) } to both sides of the equation. You can see the result of doing this within 
                the CAS view.
                <br/>
                <em>Note</em> how, within the CAS view, you can do an operation on 
                both sides of an equation by putting the equation within round brackets and
                then specifying (once) the operation that needs to performed on both sides. 
            </p>,
            code = {
                Mw.asString(Mw.casView.evaluate("(%s) - 10" format (demoEqn)).get)
            }
        ),
        Para(
            <p>
                Now you can divide both sides of the equation by { term(7) }, the coefficient of x,
                to get the value of x.
            </p>,
            code = {
                Mw.asString(Mw.casView.evaluate("(%s) / 7" format (demoEqn2)).get)
            }
        )
    )
)

pages = pages :+ pg
import scala.language.reflectiveCalls

pg = Page(
    name = "practice",
    body =
        <body style={ pageStyle }>
            { pgHeader("Equation solving practice") }
            It's time for some practice! <br/><br/>

              Solve the equation given at the bottom of this page, step by step, using the CAS view - by following these steps:
              <ul>
                  <li>Click on the first row of the CAS view, type in the equation, and hit Enter. This will take you to the 
                  second row (with the equation you want to solve in the first row).</li>
                  <li>Press the ')' key. The equation from the first row, enclosed in round brackets, will get copied into the second row.</li>
                  <li>Now add, right after the equation in round brackets, the operation that you want to do on both sides of the equation. 
                  Hit Enter to do the operation.</li>
                  <li>Keep going till you solve the equation.</li>
              </ul>
              <p style = { smallNoteStyle }>
                  Keep reading to find out how the Explain button (below) can help you see these steps in action.
              </p>
              <br/> 
              Once you have a solution, put it in the space provided (below) and click the <em>Check Answer</em> button to make sure you 
              got the answer right. <br/><br/>

              If you get the answer wrong (or even if you get it right), you can click on the <em>Explain</em> button to have Kojo 
              show you, within the CAS view, the step by step procedure for solving the equation.<br/><br/>
              
              After finishing one equation, you can generate a new equation to solve by clicking the 
              <em>New Question</em> button. <br/><br/>

              After solving some equations with the help of the CAS view, solve a few equations using pen and paper. Check your answers (as before)
              by putting them into the space provided (below) and clicking the <em>Check Answer</em> button.
        </body>,
    code = {
        Mw.clear()
        Mw.hideAlgebraView()
        Mw.showCASView()
        runInGuiThread(
            stAddUiComponent(ui.peer)
        )
        runInGuiThread(
            ui.onReady()
        )
    }
)

pages = pages :+ pg

val story = Story(pages: _*)
stClear()
stPlayStory(story)

import swing._
import eu.flierl.grouppanel._

trait Question {
    def text: String
    def next: Question
}

object EqnQuestion {
    var numLimit = 10
    var numstart = -5
    var fracs = false
}

class EqnQuestion extends Question {
    import EqnQuestion._
    // eqn in the form of ax + b = cx + d or a/ad x + b = cx + d/dd
    def gencoeff: Int = {
        val coeff = numstart + random(numLimit)
        if (coeff >= -1 && coeff <= 1) gencoeff else coeff
    }
    def genc: Int = {
        val nc = numstart + random(numLimit)
        def eql2a = {
            if (fracs) nc == a/ad else nc == a
            
        }
        if (nc == -1 || nc == 1 || eql2a) genc else nc
    }
    def genden(num: Int): Int = {
        val den = gencoeff
        if (den.abs == num.abs) genden(num) else den
    }
    val a = gencoeff
    val ad = genden(a)
    val b = gencoeff
    val c = genc
    val d = gencoeff
    val dd = genden(d)

    def text = {
        val sb = new StringBuilder
        if (fracs) {
            sb.append("%d/%d x " format (a, ad))
        }
        else {
            sb.append("%dx " format (a))
        }
        if (b > 0) {
            sb.append("+ %d = " format (b))
        }
        else {
            sb.append("- %d = " format (b.abs))
        }
        if (c != 0) {
            sb.append("%dx " format (c))
            if (d > 0) {
                sb.append("+ %d" format (d))
            }
            else {
                sb.append("- %d" format (d.abs))
            }
        }
        else {
            if (d > 0) {
                sb.append("%d" format (d))
            }
            else {
                sb.append("- %d" format (d.abs))
            }
        }
        if (fracs) {
            sb.append("/%d" format (dd))
        }
        sb.toString
    }
    def next = new EqnQuestion
}

def borderWithMargin(m: Int) = {
    import javax.swing.border._
    import javax.swing.BorderFactory
    val outsideBorder = BorderFactory.createLineBorder(color(128, 128, 128))
    val insideBorder = new EmptyBorder(m, m, m, m)
    new CompoundBorder(outsideBorder, insideBorder)
}

lazy val ui = new GroupPanel {
    import java.awt.Font
    val kfont = Font.decode("Serif-PLAIN-20")
    var question = new EqnQuestion

    def onReady() {
        answerf.requestFocusInWindow
    }

    def correctResult(suffix: String = "") {
        resultf.text = "Your answer is CORRECT" + suffix
        resultf.foreground = green
        explainb.enabled = true
    }

    def wrongResult() {
        resultf.text = "Your answer is WRONG"
        resultf.foreground = red
        explainb.enabled = true
    }

    def checkingProblem() {
        resultf.text = "Unable to process answer. Try again."
        resultf.foreground = C.purple
    }

    def nextQuestion() {
        Mw.clear()
        resultf.text = ""
        answerf.text = "x = ?"
        question = question.next
        questionf.text = qhtml(question.text)
        explainb.enabled = false
        answerf.requestFocusInWindow
    }

    def explainAnswer() = runInBackground {
        Mw.clear()
        var ires = Mw.casView.keep(question.text).get
        if (question.c != 0) {
            if (EqnQuestion.fracs) {
                if (question.c > 0) {
                    ires = Mw.casView.evaluate("Simplify[ (%s) - %dx ]" format (question.text, question.c)).get
                }
                else {
                    ires = Mw.casView.evaluate("Simplify[ (%s) + %dx ]" format (question.text, question.c.abs)).get
                }
            }
            else {
                if (question.c > 0) {
                    ires = Mw.casView.evaluate("(%s) - %dx" format (question.text, question.c)).get
                }
                else {
                    ires = Mw.casView.evaluate("(%s) + %dx" format (question.text, question.c.abs)).get
                }
            }
        }
        if (question.b > 0) {
            ires = Mw.casView.evaluate("(%s) - %d" format (Mw.asString(ires), question.b)).get
        }
        else {
            ires = Mw.casView.evaluate("(%s) + %d" format (Mw.asString(ires), question.b.abs)).get
        }
        if (EqnQuestion.fracs) {
            Mw.casView.evaluate("Simplify[ (%s) / (%s) ]" format (Mw.asString(ires), Mw.casEval("%d/%d - %d" format (question.a, question.ad, question.c))))
        }
        else {
            if (question.a - question.c != 1) {
                Mw.casView.evaluate("(%s) / %d" format (Mw.asString(ires), question.a - question.c))
            }
        }
    }

    val xregex = """\s*x?\s*=?\s*(.*)""".r
    def xval(s: String) = s match {
        case xregex(x) => x
    }

    val xsolregex = """\{\s*x\s*=\s*(.*?)}""".r

    def xsolval(s: String) = s match {
        case xsolregex(x) => x
    }

    def checkAnswer() {
        try {
            val uanswer = xval(answerf.text)
            val answer = xsolval(Mw.casEval("Solve[%s]" format (question.text)))
            if (Mw.isEqualExpr(uanswer, answer)) {
                correctResult()
            }
            else {
                val uanswer2 = Mw.casEval("Simplify[%s]" format (uanswer))
                if (Mw.isEqualExpr(uanswer2, answer)) {
                    correctResult(", but needs Simplification")
                }
                else {
                    wrongResult()
                }
            }
        }
        catch {
            case t: Throwable =>
                println(t.getMessage)
                checkingProblem()
        }
    }

    def qhtml(s: String) = {
        <body>
            <div style="text-align:center;font-size:120%;font-family:serif;margin:10px">
                { s }
            </div>
        </body>.toString
    }

    // UI Controls
    val levelLabel = new scala.swing.Label("Difficulty Level: ") {
        font = kfont
    }
    val level = new scala.swing.ComboBox(Seq(" Easy ", " Medium (Fractions)", " Medium ", " Hard ")) {
        font = kfont
        listenTo(selection)
        reactions += {
            case _ =>
                selection.item.trim match {
                    case "Easy" =>
                        EqnQuestion.numLimit = 10; EqnQuestion.numstart = -5; EqnQuestion.fracs = false
                    case "Medium" =>
                        EqnQuestion.numLimit = 100; EqnQuestion.numstart = -50; EqnQuestion.fracs = false
                    case "Medium (Fractions)" =>
                        EqnQuestion.numLimit = 20; EqnQuestion.numstart = -10; EqnQuestion.fracs = true
                    case "Hard" =>
                        EqnQuestion.numLimit = 1000; EqnQuestion.numstart = -500; EqnQuestion.fracs = false
                }
                nextQuestion()
        }
    }
    val levelPanel = new scala.swing.FlowPanel {
        background = white
        contents += levelLabel
        contents += level
    }

    val questionf = new EditorPane {
        contentType = "text/html"
        border = Swing.LineBorder(color(128, 128, 128))
        editable = false
    }

    val answerf = new TextField(30) {
        border = borderWithMargin(10)
        editable = true
        horizontalAlignment = Alignment.Center
        font = kfont
        background = white
    }

    val nextb = new Button() {
        font = kfont
        action = Action("Next Question") {
            nextQuestion()
        }
    }

    val explainb = new Button() {
        font = kfont
        action = Action("Explain") {
            explainAnswer()
        }
        enabled = false
    }

    val checkb = new Button() {
        font = kfont
        action = Action("Check Answer") {
            checkAnswer()
        }
    }

    val resultf = new TextField(30) {
        border = Swing.LineBorder(color(255, 255, 255))
        editable = false
        horizontalAlignment = Alignment.Center
        font = kfont
        background = white
    }

    nextQuestion()

    // UI Layout
    background = white
    border = Swing.LineBorder(color(240, 240, 240))
    theHorizontalLayout is Parallel(Center)(
        levelPanel,
        questionf,
        answerf,
        resultf,
        Sequential(checkb, explainb, nextb)
    )
    theVerticalLayout is Sequential(
        levelPanel,
        Gap(25),
        questionf,
        Gap(25),
        answerf,
        Gap(25),
        resultf,
        Gap(25),
        Parallel(checkb, explainb, nextb)
    )
}

