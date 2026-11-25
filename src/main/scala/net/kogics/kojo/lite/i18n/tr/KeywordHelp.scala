package net.kogics.kojo.lite.i18n.tr

/** Keep English topic keys: Turkish explanations replace their English help in turtle mode. */
object KeywordHelp {
  private val topics = List(
    "for", "def", "if", "val", "abstract", "case", "catch", "class", "do", "else",
    "extends", "false", "final", "finally", "forSome", "implicit", "import", "lazy", "match", "new",
    "null", "object", "override", "package", "private", "protected", "return", "sealed", "super", "this", "throw",
    "trait", "true", "try", "type", "var", "while", "with", "yield"
  )

  val content: Map[String, String] = topics.filter(dict.keywordTranslation.contains).map { keyword =>
    keyword -> s"Türkçesi '${dict.keywordTranslation(keyword)}' olan anahtar sözcük: '$keyword'."
  }.toMap
}
