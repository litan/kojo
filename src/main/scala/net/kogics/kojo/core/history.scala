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
  def itemAdded: Unit
  def selectionChanged(n: Int): Unit
  def ensureVisible(n: Int): Unit
  def historyReady(): Unit  
}

trait HistorySaver {
  def save(code: String, file: Option[String]): HistoryItem
  def readAll: Seq[HistoryItem]
  def readSome(filter: String): Seq[HistoryItem]
  def updateStar(hi: HistoryItem): Unit
  def updateTags(hi: HistoryItem): Unit
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
