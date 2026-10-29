package net.kogics.kojo.lite.i18n

/** Hindi editor data registered by HindiInit.preInit for highlighting, completion and snippets. */
package object hi {
  // val, var and def aliases; keep in sync with the Hindi Scala and Scalariform toolchains.
  val keywordList: List[String] = List("मान", "चर", "परिभाषा")

  val keywordTemplateMap: Map[String, String] = Map.empty
  val codeTemplateMap: Map[String, String] = Map(
    "aage" -> "आगे(${cursor})"
  )
}
