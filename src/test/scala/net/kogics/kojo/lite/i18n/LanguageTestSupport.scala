package net.kogics.kojo.lite.i18n

object LanguageTestSupport {
  def withData[A](data: LanguageEditorData)(body: => A): A = {
    val previous = LanguageEditorDataRegistry.current
    LanguageEditorDataRegistry.register(data)
    try body
    finally LanguageEditorDataRegistry.register(previous)
  }

  def turkishData: LanguageEditorData = LanguageEditorData(
    tr.keywordList, tr.keywordTemplateMap, tr.codeTemplateMap,
    Map(net.kogics.kojo.core.TwMode -> tr.KeywordHelp.content)
  )
}
