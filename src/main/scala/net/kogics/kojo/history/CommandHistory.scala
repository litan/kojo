/*
 * Copyright (C) 2009 Lalit Pant <pant.lalit@gmail.com>
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
package history

import scala.collection.Seq
import scala.collection.mutable
import net.kogics.kojo.core.HistoryItem
import net.kogics.kojo.core.HistoryListener
import net.kogics.kojo.core.HistorySaver
import net.kogics.kojo.util.Utils

class NoopHistorySaver extends HistorySaver {
  def save(code: String, file: Option[String]) = HistoryItem(code, file.getOrElse(""))
  def readAll(): Seq[HistoryItem] = Seq()
  def readSome(filter: String): Seq[HistoryItem] = Seq()
  def updateStar(hi: HistoryItem) {}
  def updateTags(hi: HistoryItem) {}
}

object CommandHistory {
  def apply(kojoCtx: core.KojoCtx) = {
    try {
      new CommandHistory(new DBHistorySaver)
    }
    catch {
      case t: Throwable =>
        if (!kojoCtx.subKojo) {
          println("\nProblem initializing savable history: " + t.getMessage)
          println("\nUnable to save history during this session")
        }
        new CommandHistory(new NoopHistorySaver)
    }
  }
}

class CommandHistory private[kojo] (historySaver: HistorySaver) extends core.CommandHistory {
  val history = new mutable.ArrayBuffer[HistoryItem]
  @volatile var hIndex = 0
  @volatile var listener: Option[HistoryListener] = None
  loadAll()
  //  loadInit() // async loading

  def setListener(l: HistoryListener) {
    //    if (listener.isDefined) throw new IllegalArgumentException("Listener already defined")
    listener = Some(l)
  }

  def clearListener() {
    listener = None
  }

  def internalAdd(hi: HistoryItem) {
    history += hi
    hIndex = history.size
  }

  def add(code: String): Unit = add(code, None)

  def add(code: String, file: Option[String]): Unit = {
    try {
      val hi = historySaver.save(code, file)
      internalAdd(hi)
      if (listener.isDefined) listener.get.itemAdded
    }
    catch {
      case t: Throwable => println("\nProblem adding code to history: " + t.getMessage)
    }
  }

  def hasPrevious = hIndex > 0

  def toPosition(idx: Int): Option[String] = {
    if (idx < 0 || idx > history.size) None
    else {
      if (hIndex != idx) {
        hIndex = idx
        if (listener.isDefined) listener.get.selectionChanged(hIndex)
      }
      if (hIndex == history.size) None else Some(history(hIndex).script)
    }
  }

  def previous: Option[String] = {
    if (hIndex == 0) None
    else {
      hIndex -= 1
      if (listener.isDefined) listener.get.selectionChanged(hIndex)
      Some(history(hIndex).script)
    }
  }

  def next: Option[String] = {
    if (hIndex == history.size) None
    else {
      hIndex += 1
      if (listener.isDefined) listener.get.selectionChanged(hIndex)
      if (hIndex == history.size) None else Some(history(hIndex).script)
    }
  }

  def size = history.size
  def apply(idx: Int) = history(idx)

  def ensureVisible(idx: Int) {
    listener foreach { _ ensureVisible (idx) }
  }

  def ensureLastEntryVisible() {
    ensureVisible(size)
  }

  def starHelper(hi: HistoryItem, on: Boolean) {
    try {
      hi.starred = on
      historySaver.updateStar(hi)
    }
    catch {
      case t: Throwable =>
        println("\nProblem saving star to history: " + t.getMessage)
        hi.starred = !on
    }
  }

  def star(hi: HistoryItem) {
    starHelper(hi, true)
  }

  def unstar(hi: HistoryItem) {
    starHelper(hi, false)
  }

  def saveTags(hi: HistoryItem, tags: String) {
    val oldTags = hi.tags
    try {
      hi.tags = tags
      historySaver.updateTags(hi)
    }
    catch {
      case t: Throwable =>
        println("\nProblem saving tags to history: " + t.getMessage)
        hi.tags = oldTags
    }
  }

  def loadInit() = Utils.runAsync {
    val allHistory = historySaver.readAll.reverse
    Utils.runInSwingThread {
      allHistory.foreach { hi =>
        internalAdd(hi)
      }
      listener foreach { _ historyReady () }
    }
  }

  def loadAll() {
    history.clear()
    historySaver.readAll.reverse.foreach { hi =>
      internalAdd(hi)
    }
  }

  def filter(text: String) {
    history.clear()
    historySaver.readSome(text).reverse.foreach { hi =>
      internalAdd(hi)
    }
  }
}
