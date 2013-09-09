package net.kogics.kojo.core

import java.util.Date

case class HistoryItem(
  script: String,
  file: String = "",
  var id: Long = 0,
  var starred: Boolean = false,
  var tags: String = "",
  at: Date = new Date)

trait HistoryListener {
  def itemAdded
  def selectionChanged(n: Int)
  def ensureVisible(n: Int)
  def historyReady()  
}

trait HistorySaver {
  def save(code: String, file: Option[String]): HistoryItem
  def readAll(): Seq[HistoryItem]
  def readSome(filter: String): Seq[HistoryItem]
  def updateStar(hi: HistoryItem)
  def updateTags(hi: HistoryItem)
}

trait CommandHistory {
  def apply(idx: Int): HistoryItem
  def size: Int
  def setListener(l: HistoryListener): Unit
  def loadAll(): Unit
  def filter(text: String): Unit
  def saveTags(hi: HistoryItem, tags: String): Unit
  def star(hi: HistoryItem): Unit
  def unstar(hi: HistoryItem): Unit
}
