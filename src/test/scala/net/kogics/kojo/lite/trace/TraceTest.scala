package net.kogics.kojo
package lite.trace

import java.awt.Color
import java.awt.Font

import org.jmock.Expectations
import org.jmock.Expectations.any
import org.jmock.Expectations.returnValue
import org.jmock.Mockery
import org.jmock.lib.legacy.ClassImposteriser
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.scalatest.junit.ShouldMatchersForJUnit

import net.kogics.kojo.core.SCanvas
import net.kogics.kojo.core.Turtle
import net.kogics.kojo.lite.Builtins
import net.kogics.kojo.lite.DrawingCanvasAPI
import net.kogics.kojo.lite.NoOpRunContext
import net.kogics.kojo.lite.NoOpSCanvas
import net.kogics.kojo.turtle.TurtleWorldAPI

import edu.umd.cs.piccolo.activities.PActivity

@RunWith(classOf[org.jmock.integration.junit4.JMock])
class TraceTest extends ShouldMatchersForJUnit {

  val context: Mockery = new Mockery() {
    {
      setImposteriser(ClassImposteriser.INSTANCE)
    }
  }
  var nameCounter = 0
  def getMockTurtleName: String = "Turtle #" + nameCounter
  def incCounter = { nameCounter = nameCounter + 1 }
  def newMockTurtle = { context.mock(classOf[core.Turtle], getMockTurtleName).asInstanceOf[core.Turtle] }
  var mockTurtle: Turtle = _

  var startEvents: Vector[MethodEvent] = _
  var endEvents: Vector[MethodEvent] = _
  var traceListener: TraceListener = _
  var sCanvas: SCanvas = _
  var TSCanvas: DrawingCanvasAPI = _
  var Tw: TurtleWorldAPI = _
  val builtins = context.mock(classOf[Builtins]).asInstanceOf[Builtins]
  var tracer: Tracing = _

  @Before
  def initTracer {
    incCounter
    mockTurtle = newMockTurtle
    startEvents = Vector[MethodEvent]()
    endEvents = Vector[MethodEvent]()

    traceListener = new TraceListener {
      def onStart() {}
      def onMethodEnter(me: MethodEvent) {
        startEvents :+= me
      }
      def onMethodExit(me: MethodEvent) {
        endEvents :+= me
      }
      def onEnd() {}
    }

    sCanvas = new NoOpSCanvas {
      override val turtle0 = mockTurtle
      override def animateActivity(a: PActivity) {
        a.getDelegate().activityFinished(a)
      }
    }
    TSCanvas = new DrawingCanvasAPI(sCanvas)
    Tw = new TurtleWorldAPI(sCanvas.turtle0)

    tracer = new Tracing(builtins, traceListener, new NoOpRunContext)
  }

  @Test
  def movTest1 {
    //Rich Turtle Command functions
    val code = """clear()
turn(10)
forward(100)
right(40)
back(100)
left(40)
back()
left()
right()      
"""
    context.checking(new Expectations {
      oneOf(mockTurtle).turn(10.0)
      oneOf(mockTurtle).forward(100.0)
      oneOf(mockTurtle).turn(-40.0)
      oneOf(mockTurtle).forward(-100.0)
      oneOf(mockTurtle).turn(40.0)
      oneOf(mockTurtle).forward(-25.0)
      oneOf(mockTurtle).turn(90.0)
      oneOf(mockTurtle).turn(-90.0)

      allowing(mockTurtle).lastLine; will(returnValue(None))
      allowing(mockTurtle).lastTurn; will(returnValue(None))
    })

    context.checking(new Expectations {
      oneOf(builtins).random(1000); will(returnValue(300))
      allowing(builtins).TSCanvas; will(returnValue(TSCanvas))
      allowing(builtins).Tw; will(returnValue(Tw))
    })

    tracer.realTrace(code)
    startEvents.size should be(9)
    endEvents.size should be(0)

    context.assertIsSatisfied()
  }

  @Test
  def movTest2 {
    //TurtleMover movement functions
    val code = """clear()
forward()
towards(new Point(10,10))
setPosition(0, 20)
setPosition(new Point(30, 20))
changePosition(10, 10)
hop()
hop(10)
jumpTo(new Point(0, 0))
moveTo(40, 0)
moveTo(new Point(40, 10))
towards(10,10)
setHeading(270)
jumpTo(0, 30)
home()
arc(100,40)
circle(10)
"""
    /*
 * 
 */

    //val getHeading = mockTurtle.heading

    context.checking(new Expectations {
      oneOf(mockTurtle).forward(25.0)
      oneOf(mockTurtle).towards(10.0, 10.0)
      oneOf(mockTurtle).jumpTo(0.0, 20.0)
      oneOf(mockTurtle).jumpTo(30.0, 20.0)
      oneOf(mockTurtle).changePosition(10.0, 10.0)

      //hop()
      oneOf(mockTurtle).penUp()
      oneOf(mockTurtle).forward(25.0)
      oneOf(mockTurtle).penDown()

      //hop(10)
      oneOf(mockTurtle).penUp()
      oneOf(mockTurtle).forward(10.0)
      oneOf(mockTurtle).penDown()

      oneOf(mockTurtle).jumpTo(0.0, 0.0)

      //moveTo(40, 0)
      oneOf(mockTurtle).towards(40.0, 0.0)
      oneOf(mockTurtle).forward(40.0)

      //moveTo(new Point(40, 10))
      oneOf(mockTurtle).towards(40.0, 10.0)
      oneOf(mockTurtle).forward(10.0)

      oneOf(mockTurtle).towards(10.0, 10.0)

      //setHeading
      oneOf(mockTurtle).setHeading(270.0)
      allowing(mockTurtle).turn(90.0)

      oneOf(mockTurtle).jumpTo(0.0, 30.0)

      //home
      oneOf(mockTurtle).home()
      allowing(mockTurtle).towards(0.0, 0.0)
      allowing(mockTurtle).forward(30.0)
      allowing(mockTurtle).setHeading(90.0)
      allowing(mockTurtle).turn(-180.0)

      //arc & circle
      allowing(mockTurtle).forward(`with`(any(classOf[Double])))
      allowing(mockTurtle).turn(`with`(any(classOf[Double])))

      allowing(mockTurtle).lastLine; will(returnValue(None))
      allowing(mockTurtle).lastTurn; will(returnValue(None))
    })

    context.checking(new Expectations {
      one(builtins).random(1000); will(returnValue(300))
      allowing(builtins).TSCanvas; will(returnValue(TSCanvas))
      allowing(builtins).Tw; will(returnValue(Tw))
    })

    tracer.realTrace(code)
    startEvents.size should be(17)
    endEvents.size should be(0)

    context.assertIsSatisfied()
  }

  @Test
  def styleTest1 {
    //non-movement TurtleMover functions
    val code = """clear()
setPenColor(black)
setPenColor(new Color(10,10,10))
setPenThickness(10)
setFillColor(black)
setFillColor(new Color(10,10,10))
saveStyle()
restoreStyle()
savePosHe()
restorePosHe()
setAnimationDelay(10)
beamsOn()
beamsOff()
write("Hello World")
visible()
invisible()
write(black)
setPenFontSize(12)
setPenFont(new Font("Arial", 0, 18))
"""
    val testFont = new Font("Arial", 0, 18)
    val (x, y, z) = (testFont.getFontName, testFont.getStyle, testFont.getSize)

    context.checking(new Expectations {
      oneOf(mockTurtle).setPenColor(Color.black)
      oneOf(mockTurtle).setPenColor(new Color(10, 10, 10))
      oneOf(mockTurtle).setPenThickness(10.0)
      oneOf(mockTurtle).setFillColor(Color.black)
      oneOf(mockTurtle).setFillColor(new Color(10, 10, 10))
      oneOf(mockTurtle).saveStyle
      oneOf(mockTurtle).restoreStyle
      oneOf(mockTurtle).savePosHe
      oneOf(mockTurtle).restorePosHe
      oneOf(mockTurtle).setAnimationDelay(10)
      oneOf(mockTurtle).beamsOn()
      oneOf(mockTurtle).beamsOff()
      oneOf(mockTurtle).write("Hello World")
      oneOf(mockTurtle).visible()
      oneOf(mockTurtle).invisible()
      oneOf(mockTurtle).write(Color.black.toString)
      allowing(mockTurtle).write(`with`(any(classOf[String])))
      oneOf(mockTurtle).setPenFontSize(12)

      oneOf(mockTurtle).setPenFont(new Font(x, y, z))

      allowing(mockTurtle).lastLine; will(returnValue(None))
      allowing(mockTurtle).lastTurn; will(returnValue(None))
    })

    context.checking(new Expectations {
      oneOf(builtins).random(1000); will(returnValue(300))
      allowing(builtins).TSCanvas; will(returnValue(TSCanvas))
      allowing(builtins).Tw; will(returnValue(Tw))
    })

    tracer.realTrace(code)
    startEvents.size should be(22)
    endEvents.size should be(3)

    context.assertIsSatisfied()
  }

  @Test
  def costumeTest {
    //Rich Turtle Command functions
    val code = """clear()
setCostume(Background.trainTrack)
setCostumes(Costume.bat1, Costume.bat2)
nextCostume()
scaleCostume(0.5)
"""
    context.checking(new Expectations {
      oneOf(mockTurtle).setCostume("/media/backgrounds/train-tracks3.gif")
      oneOf(mockTurtle).setCostumes(Vector("/media/costumes/bat1-a.png", "/media/costumes/bat1-b.png"))
      oneOf(mockTurtle).nextCostume()
      oneOf(mockTurtle).scaleCostume(0.5)
      allowing(mockTurtle).lastLine; will(returnValue(None))
      allowing(mockTurtle).lastTurn; will(returnValue(None))
    })

    context.checking(new Expectations {
      oneOf(builtins).random(1000); will(returnValue(300))
      allowing(builtins).TSCanvas; will(returnValue(TSCanvas))
      allowing(builtins).Tw; will(returnValue(Tw))
    })

    tracer.realTrace(code)
    startEvents.size should be(11)
    endEvents.size should be(6)

    context.assertIsSatisfied()
  }
}