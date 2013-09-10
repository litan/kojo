package net.kogics.kojo
package lite.trace

import java.awt.Color

import org.jmock.Expectations
import org.jmock.Expectations.returnValue
import org.jmock.Mockery
import org.jmock.lib.legacy.ClassImposteriser
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

  @Test
  def test1 {
    val code = """clear()
setPenColor(blue)
forward(100)
right()
"""

    val mockTurtle = context.mock(classOf[core.Turtle]).asInstanceOf[core.Turtle]
    context.checking(new Expectations {
      one(mockTurtle).setPenColor(Color.blue)
      one(mockTurtle).forward(100.0)
      one(mockTurtle).turn(-90.0)
      allowing(mockTurtle).lastLine; will(returnValue(None))
      allowing(mockTurtle).lastTurn; will(returnValue(None))
    })

    var startEvents = Vector[MethodEvent]()
    var endEvents = Vector[MethodEvent]()

    val traceListener = new TraceListener {
      def onStart() {}
      def onMethodEnter(me: MethodEvent) {
        startEvents :+= me
      }
      def onMethodExit(me: MethodEvent) {
        endEvents :+= me
      }
      def onEnd() {}
    }

    val sCanvas: SCanvas = new NoOpSCanvas {
      override val turtle0 = mockTurtle
      override def animateActivity(a: PActivity) {
        a.getDelegate().activityFinished(a)
      }
    }
    val TSCanvas = new DrawingCanvasAPI(sCanvas)
    val Tw = new TurtleWorldAPI(sCanvas.turtle0)

    val builtins = context.mock(classOf[Builtins]).asInstanceOf[Builtins]
    context.checking(new Expectations {
      one(builtins).random(1000); will(returnValue(300))
      allowing(builtins).TSCanvas; will(returnValue(TSCanvas))
      allowing(builtins).Tw; will(returnValue(Tw))
    })

    val tracer = new Tracing(builtins, traceListener, new NoOpRunContext)
    tracer.realTrace(code)

    println(s"Calls: $startEvents")
    println(s"Returns: $endEvents")
    startEvents.size should be(5)
    endEvents.size should be(1)
  }
}