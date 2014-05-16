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
import util.Utils

trait Viewable {
  def hasNextView: Boolean
  def forward(): Unit
  def view: xml.Node
  def hasPrevView: Boolean
  def back(): Unit
  def numViews: Int
  def currView: Int // 1 based
  val name: String
  def scrollToEnd: Boolean
}

object Page {
  def apply(name: String, body: => xml.Node, code: => Unit = {}) = new Page(name, body, code)
}

class Page(val name: String, body: => xml.Node, code: => Unit) extends Viewable {
  def hasNextView = false
  def hasPrevView = false
  def view = {
    Utils.runAsyncMonitored {
      code
    }
    body
  }
  def forward() = new IllegalStateException("Can't go forward on a Static page")
  def back() = new IllegalStateException("Can't go back on a Static page")
  def numViews = 1
  def currView = 1
  def scrollToEnd = false
}

object Para {
  def apply(body: => xml.Node, code: => Unit = {}) = new Para(body, code)
}
class Para(body0: => xml.Node, code0: => Unit) {
  def code = code0
  def body = body0
}

object IncrPage {
  def apply(name: String, style: String, body: List[Para]) = new IncrPage(name, style, body)
}

class IncrPage(val name: String, style: String, body: List[Para]) extends Viewable {
  @volatile var currPara = 1
  def paras = body.size

  private def viewParas(n: Int) = {
    <body style={style}>
      {body.take(n).map {para => para.body}}
    </body>
  }
  
  private def runCode(n: Int) {
    Utils.runAsyncMonitored {
      body(n-1).code
    }
  }

  def hasNextView = currPara < paras
  def hasPrevView = currPara > 1

  def forward() {
    currPara += 1
    if (currPara > paras) throw new IllegalStateException("Gone past view range")
  }

  def back() {
    currPara -= 1
    if (currPara < 1) throw new IllegalStateException("Gone past view range")
  }

  def view = {
    runCode(currPara)
    viewParas(currPara)
  }

  def numViews = paras
  def currView = currPara
  def scrollToEnd = true
}

case class Story(pages: Viewable*) extends Viewable {
  var currPage = 0

  def hasNextView: Boolean = {
    val b1 = pages(currPage).hasNextView
    if (b1) {
      true
    }
    else {
      if (currPage + 1 < pages.size) true else false
    }
  }

  def hasPrevView: Boolean = {
    val b1 = pages(currPage).hasPrevView
    if (b1) {
      true
    }
    else {
      if (currPage > 0) true else false
    }
  }

  def forward() {
    if (pages(currPage).hasNextView) {
      pages(currPage).forward()
    }
    else {
      currPage += 1
    }
  }

  def back() {
    if (pages(currPage).hasPrevView) {
      pages(currPage).back()
    }
    else {
      currPage -= 1
    }
  }

  def view = {
    pages(currPage).view
  }

  def hasView(pg: Int, para: Int) = {
    if (pg > 0 && pg <= pages.size && para > 0 && para <= pages(pg-1).numViews) 
      true
    else 
      false
  }

  def goto(pg: Int, para: Int) {
    // currpage - 0 based
    // pg - 1 based
    val targetPage = pg - 1
    if (currPage < targetPage) {
      while (currPage != targetPage) {
        while(pages(currPage).hasNextView) {
          pages(currPage).forward()
        }
        currPage += 1
      }
      for (idx <- 1 until para) {
        forward()
      }
    }
    else if(currPage > targetPage) {
      while (currPage != targetPage) {
        while(pages(currPage).hasPrevView) {
          pages(currPage).back()
        }
        currPage -= 1
      }
      for (idx <- 1 until pages(currPage).numViews - para) {
        back()
      }
    }
    else {
      // currPage == targetPage
      while(pages(currPage).hasPrevView) {
        pages(currPage).back()
      }
      for (idx <- 1 until para) {
        forward()
      }
    }
  }

  def location = (currPage+1, pages(currPage).currView)

  def pageNumber(name: String): Option[Int] = {
    val idx = pages.indexWhere {e => name == e.name}
    if (idx == -1) None else Some(idx+1)
  }

  def numViews = throw new UnsupportedOperationException
  def currView = throw new UnsupportedOperationException
  val name = ""
  def scrollToEnd = pages(currPage).scrollToEnd

  val noOpHandler = new StringHandlerHolder({ e => })
  val handlers = collection.mutable.Map[String, HandlerHolder[Any]]() withDefaultValue(noOpHandler)
  val linkEnterHandlers = collection.mutable.Map[String, HandlerHolder[Any]]() withDefaultValue(noOpHandler) 
  val linkExitHandlers = collection.mutable.Map[String, HandlerHolder[Any]]() withDefaultValue(noOpHandler)

  def addLinkHandler[T](name: String)(hm: HandlerHolder[T]) = Utils.runInSwingThread {
    handlers(name) = hm
  }
  
  def handleLink(name: String, data: String) {
    handlers(name).handle(data)
  }

  def addLinkEnterHandler[T](name: String)(hm: HandlerHolder[T]) = Utils.runInSwingThread {
    linkEnterHandlers(name) = hm
  }
  
  def handleLinkEnter(name: String, data: String) {
    linkEnterHandlers(name).handle(data)
  }

  def addLinkExitHandler[T](name: String)(hm: HandlerHolder[T]) = Utils.runInSwingThread {
    linkExitHandlers(name) = hm
  }
  
  def handleLinkExit(name: String, data: String) {
    linkExitHandlers(name).handle(data)
  }
  
  @volatile var stopFn: Option[() => Unit] = None
  def onStop(fn: => Unit) {
    stopFn = Some(fn _)
  }
  
  def stop() {
    stopFn.foreach(_.apply)
  }
  
}
