/*
 * Copyright (C) 2011 Lalit Pant <pant.lalit@gmail.com>
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

package net.kogics.kojo.xscala

import org.junit._
import Assert._

import org.junit.runner.RunWith

import org.jmock.integration.junit4.{JMock, JUnit4Mockery=>Mockery}
import org.jmock.Expectations
import org.jmock.Expectations._
import org.jmock.lib.legacy.ClassImposteriser

import net.kogics.kojo.core.RunContext

@RunWith(classOf[JMock])
class InterpOutputHandlerTest {

  val context: Mockery = new Mockery() {
    {
      setImposteriser(ClassImposteriser.INSTANCE)
    }
  }

  @Test
  def testSimpleOutput = {
    val output = "plain text\n"
    val runCtx = (context.mock(classOf[RunContext])).asInstanceOf[RunContext]
    context.checking (new Expectations {
        one(runCtx).reportOutput(output)
      })

    val outputHandler = new InterpOutputHandler(runCtx)
    outputHandler.reportInterpOutput(output)
  }

  @Test
  def testSimpleOutputTwice = {
    val output = "plain text\n"
    val output2 = "plain text2\n"
    val runCtx = (context.mock(classOf[RunContext])).asInstanceOf[RunContext]
    context.checking (new Expectations {
        one(runCtx).reportOutput(output)
        one(runCtx).reportOutput(output2)
      })

    val outputHandler = new InterpOutputHandler(runCtx)
    outputHandler.reportInterpOutput(output)
    outputHandler.reportInterpOutput(output2)
  }

  @Test
  def testErrorPattern1 = {
    val output1 = "<console>:1: error: Invalid literal number\n"
    val output11 = "error: Invalid literal number\n"
    val output2 = "       12x\n"
    val output3 = "       ^\n"

    val runCtx = (context.mock(classOf[RunContext])).asInstanceOf[RunContext]
    context.checking (new Expectations {
        one(runCtx).reportErrorMsg(output11)
        one(runCtx).reportErrorText(output2)
        one(runCtx).reportErrorMsg(output3)
      })

    val outputHandler = new InterpOutputHandler(runCtx)
    outputHandler.reportInterpOutput(output1)
    outputHandler.reportInterpOutput(output2)
    outputHandler.reportInterpOutput(output3)
  }

  @Test
  def testErrorPattern2 = {
    val output1 = "<console>:8: error: not enough arguments for constructor Range: (start: Int,end: Int,step: Int)scala.collection.immutable.Range.\nUnspecified value parameters start, end, step.\n"
    val output11 = "error: not enough arguments for constructor Range: (start: Int,end: Int,step: Int)scala.collection.immutable.Range.\nUnspecified value parameters start, end, step.\n"
    val output2 = "val l = new Range\n"
    val output3 = "        ^\n"

    val runCtx = (context.mock(classOf[RunContext])).asInstanceOf[RunContext]
    context.checking (new Expectations {
        one(runCtx).reportErrorMsg(output11)
        one(runCtx).reportErrorText(output2)
        one(runCtx).reportErrorMsg(output3)
      })

    val outputHandler = new InterpOutputHandler(runCtx)
    outputHandler.reportInterpOutput(output1)
    outputHandler.reportInterpOutput(output2)
    outputHandler.reportInterpOutput(output3)
  }

  @Test
  def testErrorPattern3 = {
    val output1 = "<console>:12: error: Invalid literal number\n"
    val output11 = "error: Invalid literal number\n"
    val output2 = "       12x\n"
    val output3 = "       ^\n"

    val runCtx = (context.mock(classOf[RunContext])).asInstanceOf[RunContext]
    context.checking (new Expectations {
        one(runCtx).reportErrorMsg(output11)
        one(runCtx).reportErrorText(output2)
        one(runCtx).reportErrorMsg(output3)
      })

    val outputHandler = new InterpOutputHandler(runCtx)
    outputHandler.reportInterpOutput(output1)
    outputHandler.reportInterpOutput(output2)
    outputHandler.reportInterpOutput(output3)
  }

  @Test
  def testErrorPattern4 = {
    val output1 = "<console>:1257: error: Invalid literal number\n"
    val output11 = "error: Invalid literal number\n"
    val output2 = "       12x\n"
    val output3 = "       ^\n"

    val runCtx = (context.mock(classOf[RunContext])).asInstanceOf[RunContext]
    context.checking (new Expectations {
        one(runCtx).reportErrorMsg(output11)
        one(runCtx).reportErrorText(output2)
        one(runCtx).reportErrorMsg(output3)
      })

    val outputHandler = new InterpOutputHandler(runCtx)
    outputHandler.reportInterpOutput(output1)
    outputHandler.reportInterpOutput(output2)
    outputHandler.reportInterpOutput(output3)
  }

  @Test
  def testExceptionOutput1 = {
    val output =
      """java.lang.RuntimeException
        at .<init>(<console>:9)
        at .<clinit>(<console>)
        at RequestResult$.<init>(<console>:4)
        at RequestResult$.<clinit>(<console>)
        at RequestResult$scala_repl_result(<console>)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
        at java.lang.reflect.Method.invoke(Method.java:597)
        at scala.tools.nsc.Interpreter$Request$$anonfun$loadAndRun$1$$anonfun$apply$13.apply(Interpreter.scala)
        at scala.tools.nsc.Interpreter$Request$$anonfun$loadAndRun$1$$anonfun$apply$13.apply(Interpreter.scala)
        at scala.util.control.Exception$Catch.apply(Exception.scala)
        at scala.tools.nsc.Interpreter$Request$$anonfun$loadAndRun$1.apply(Interpreter.scala)"""

    val output5 =
      """java.lang.RuntimeException
        at .<init>(<console>:9)
        at .<clinit>(<console>)
        at RequestResult$.<init>(<console>:4)
        at RequestResult$.<clinit>(<console>)......""" + "\n"

    val runCtx = (context.mock(classOf[RunContext])).asInstanceOf[RunContext]
    context.checking (new Expectations {
        one(runCtx).kprintln(output5)
      })

    val outputHandler = new InterpOutputHandler(runCtx)
    outputHandler.reportInterpOutput(output)
  }

  @Test
  def testExceptionOutput2 = {
    val output =
      """java.lang.some.other.RuntimeException
        at .<init>(<console>:9)
        at .<clinit>(<console>)
        at RequestResult$.<init>(<console>:4)
        at RequestResult$.<clinit>(<console>)
        at RequestResult$scala_repl_result(<console>)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
        at java.lang.reflect.Method.invoke(Method.java:597)
        at scala.tools.nsc.Interpreter$Request$$anonfun$loadAndRun$1$$anonfun$apply$13.apply(Interpreter.scala)
        at scala.tools.nsc.Interpreter$Request$$anonfun$loadAndRun$1$$anonfun$apply$13.apply(Interpreter.scala)
        at scala.util.control.Exception$Catch.apply(Exception.scala)
        at scala.tools.nsc.Interpreter$Request$$anonfun$loadAndRun$1.apply(Interpreter.scala)"""

    val output5 =
      """java.lang.some.other.RuntimeException
        at .<init>(<console>:9)
        at .<clinit>(<console>)
        at RequestResult$.<init>(<console>:4)
        at RequestResult$.<clinit>(<console>)......""" + "\n"

    val runCtx = (context.mock(classOf[RunContext])).asInstanceOf[RunContext]
    context.checking (new Expectations {
        one(runCtx).kprintln(output5)
      })

    val outputHandler = new InterpOutputHandler(runCtx)
    outputHandler.reportInterpOutput(output)
  }

  @Test
  def testExceptionOutput3 = {
    val output =
      """java.lang.RuntimeException
        at .<init>(<console>:9)
        at .<clinit>(<console>)"""

    val runCtx = (context.mock(classOf[RunContext])).asInstanceOf[RunContext]
    context.checking (new Expectations {
        one(runCtx).kprintln(output)
      })

    val outputHandler = new InterpOutputHandler(runCtx)
    outputHandler.reportInterpOutput(output)
  }

  @Test
  def testExceptionOutput4 = {
    val output =
      """java.lang.RuntimeException"""

    val runCtx = (context.mock(classOf[RunContext])).asInstanceOf[RunContext]
    context.checking (new Expectations {
        one(runCtx).kprintln(output)
      })

    val outputHandler = new InterpOutputHandler(runCtx)
    outputHandler.reportInterpOutput(output)
  }

  @Test
  def testErrorExceptionOutput1 = {
    val output =
      """java.lang.NoClassDefFoundError: org/apache/log4j/Logger
        at edu.jas.poly.GenPolynomialTokenizer.<clinit>(GenPolynomialTokenizer.java:53)
        at .<init>(<console>:14)
        at .<clinit>(<console>)
        at RequestResult$.<init>(<console>:9)
        at RequestResult$.<clinit>(<console>)
        at RequestResult$scala_repl_result(<console>)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
        at java.lang.reflect.Method.invoke(Method.java:597)
        at scala.tools.nsc.Interpreter$Request$$anonfun$loadAndRun$1$$anonfun$apply$18.apply(Interpreter.scala:984)
        at scala.tools.nsc.Interpreter$Request$$anonfun$loadAndRun$1$$anonfun$apply$18.apply(..."""

    val output5 =
      """java.lang.NoClassDefFoundError: org/apache/log4j/Logger
        at edu.jas.poly.GenPolynomialTokenizer.<clinit>(GenPolynomialTokenizer.java:53)
        at .<init>(<console>:14)
        at .<clinit>(<console>)
        at RequestResult$.<init>(<console>:9)......""" + "\n"

    val runCtx = (context.mock(classOf[RunContext])).asInstanceOf[RunContext]
    context.checking (new Expectations {
        one(runCtx).kprintln(output5)
      })

    val outputHandler = new InterpOutputHandler(runCtx)
    outputHandler.reportInterpOutput(output)
  }


  @Test
  def testNotExceptionOutput1 = {
    val output =
      """ java.lang.RuntimeException
        There's an extra space above!
        at .<init>(<console>:9)
        at .<clinit>(<console>)
        at RequestResult$.<init>(<console>:4)
        at RequestResult$.<clinit>(<console>)
        at RequestResult$scala_repl_result(<console>)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
        at java.lang.reflect.Method.invoke(Method.java:597)
        at scala.tools.nsc.Interpreter$Request$$anonfun$loadAndRun$1$$anonfun$apply$13.apply(Interpreter.scala)
        at scala.tools.nsc.Interpreter$Request$$anonfun$loadAndRun$1$$anonfun$apply$13.apply(Interpreter.scala)
        at scala.util.control.Exception$Catch.apply(Exception.scala)
        at scala.tools.nsc.Interpreter$Request$$anonfun$loadAndRun$1.apply(Interpreter.scala)"""

    val runCtx = (context.mock(classOf[RunContext])).asInstanceOf[RunContext]
    context.checking (new Expectations {
        one(runCtx).reportOutput(output)
      })

    val outputHandler = new InterpOutputHandler(runCtx)
    outputHandler.reportInterpOutput(output)
  }

  @Test
  def testNotExceptionOutput2 = {
    val output =
      """RuntimeException
        No dot before the RuntimeException above
        at .<init>(<console>:9)
        at .<clinit>(<console>)
        at RequestResult$.<init>(<console>:4)
        at RequestResult$.<clinit>(<console>)
        at RequestResult$scala_repl_result(<console>)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
        at java.lang.reflect.Method.invoke(Method.java:597)
        at scala.tools.nsc.Interpreter$Request$$anonfun$loadAndRun$1$$anonfun$apply$13.apply(Interpreter.scala)
        at scala.tools.nsc.Interpreter$Request$$anonfun$loadAndRun$1$$anonfun$apply$13.apply(Interpreter.scala)
        at scala.util.control.Exception$Catch.apply(Exception.scala)
        at scala.tools.nsc.Interpreter$Request$$anonfun$loadAndRun$1.apply(Interpreter.scala)"""

    val runCtx = (context.mock(classOf[RunContext])).asInstanceOf[RunContext]
    context.checking (new Expectations {
        one(runCtx).reportOutput(output)
      })

    val outputHandler = new InterpOutputHandler(runCtx)
    outputHandler.reportInterpOutput(output)
  }

  @Test
  def testNotErrorExceptionOutput = {
    val output =
      """The Error word in the output"""

    val runCtx = (context.mock(classOf[RunContext])).asInstanceOf[RunContext]
    context.checking (new Expectations {
        one(runCtx).reportOutput(output)
      })

    val outputHandler = new InterpOutputHandler(runCtx)
    outputHandler.reportInterpOutput(output)
  }
}
