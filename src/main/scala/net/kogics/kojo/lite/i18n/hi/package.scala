package net.kogics.kojo.lite.i18n

/** Hindi editor data registered by HindiInit.preInit for highlighting, completion and snippets. */
package object hi {
  // val, var and def aliases; keep in sync with the Hindi Scala and Scalariform toolchains.
  val keywordList: List[String] = List("मान", "चर", "परिभाषा")

  val keywordTemplateMap: Map[String, String] = Map.empty
  val codeTemplateMap: Map[String, String] = Map(
    "maan" -> "मान ${cursor} = ",
    "char" -> "चर ${cursor} = ",
    "paribhasha" -> "परिभाषा ${cursor}() = {\n    \n}",
    "aage" -> "आगे(${cursor})",
    "poncho" -> "पोंछो(${cursor})",
    "dayen" -> "दाएँ(${cursor})",
    "bayen" -> "बाएँ(${cursor})",
    "dohrao" -> "दोहराओ(${cursor}) {\n    \n}",
    "kudo" -> "कूदो(${cursor})",
    "kalam_niche" -> "कलम_नीचे(${cursor})",
    "kalam_upar" -> "कलम_ऊपर(${cursor})",
    "kalam_niche_hai" -> "कलम_नीचे_है${cursor} ",
    "kalam_ka_rang" -> "कलम_का_रंग(${cursor})",
    "kalam_ki_motai" -> "कलम_की_मोटाई(${cursor})",
    "bharne_ka_rang" -> "भरने_का_रंग(${cursor})"
  )
}
