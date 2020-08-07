package net.kogics.kojo.lite

import net.kogics.kojo.picture.TextPic

object LoadProgress {
  @volatile private var loadProgress: LoadProgress = _

  def loading = loadProgress != null && loadProgress.isActive

  def init(builtins: Builtins): Unit = {
    loadProgress = new LoadProgress(builtins)
  }

  def showLoading(): Unit = {
    require(loadProgress != null, "Init LoadProgress before using it.")
    loadProgress.show()
  }

  def hideLoading(): Unit = {
    require(loadProgress != null, "Init LoadProgress before using it.")
    loadProgress.hide()
  }

}

class LoadProgress(val builtins: Builtins) {
  import builtins._
  @volatile private var loadingPic: Picture = _
  def makeLoadingPic(): Picture = {
    val b = textExtent("Loading...", 30)
    val bg = Picture.rectangle(b.width + 10, b.height + 10)
    val bgColor = cm.rgb(20, 20, 20)
    bg.setPenColor(cm.black)
    bg.setFillColor(bgColor)
    val fg = new TextPic("Loading...", 30, cm.lightSteelBlue)
    val pic = picStackCentered(Seq(bg, fg))
    pic.translate(-b.width / 2 - 5, -b.height / 2 - 5)
    pic
  }

  def show(): Unit = {
    loadingPic = makeLoadingPic()
    loadingPic.draw()
  }

  def hide(): Unit = {
    loadingPic.erase()
    loadingPic = null
  }

  def isActive = loadingPic != null
}
