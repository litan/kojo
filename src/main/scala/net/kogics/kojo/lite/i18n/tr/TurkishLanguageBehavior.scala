package net.kogics.kojo.lite.i18n.tr

import net.kogics.kojo.lite.i18n.LanguageBehavior

object TurkishLanguageBehavior extends LanguageBehavior {
  override def updateResult(text: String): String = translate.result(text)
  override def updateTypes(text: String): String = translate.typeInfo(text)
  override def recognizesOwner(owner: String): Boolean = trDestesiMi(owner)
  override def allowsPlainNameFallback(owner: String): Boolean = trDestesiMi(owner)
}
