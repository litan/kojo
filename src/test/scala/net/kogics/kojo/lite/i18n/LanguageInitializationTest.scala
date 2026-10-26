package net.kogics.kojo.lite.i18n

import java.io.File
import java.net.URLClassLoader
import java.util.Locale
import java.util.prefs.{AbstractPreferences, Preferences}
import scala.language.existentials

import net.kogics.kojo.lite.ToolchainTestSupport.withProperty
import org.junit.runner.RunWith
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class LanguageInitializationTest extends FunSuite with Matchers {
  private def preferences(language: String): Preferences = new AbstractPreferences(null, "") {
    private val values = collection.mutable.Map("user.language" -> language)
    def putSpi(key: String, value: String): Unit = { values(key) = value }
    def getSpi(key: String): String = values.getOrElse(key, null)
    def removeSpi(key: String): Unit = { values.remove(key); () }
    def removeNodeSpi(): Unit = ()
    def keysSpi(): Array[String] = values.keys.toArray
    def childrenNamesSpi(): Array[String] = Array.empty[String]
    def childSpi(name: String): AbstractPreferences = throw new UnsupportedOperationException
    def syncSpi(): Unit = ()
    def flushSpi(): Unit = ()
  }

  for ((code, keyword, translated) <- List(
      ("en", "dez", "Int"), ("tr", "dez", "Sayı"), ("sv", "värde", "Int"), ("xx", "dez", "Int"))) {
    test(s"$code startup registers data and fixes selection before any consumer is constructed") {
      val urls = System.getProperty("java.class.path").split(File.pathSeparator).map(p => new File(p).toURI.toURL)
      val loader = new URLClassLoader(urls, null) {
        override def loadClass(name: String, resolve: Boolean): Class[_] = {
          if (name.startsWith("net.kogics.kojo.lite.i18n.TurkishAPI") ||
              name.startsWith("net.kogics.kojo.lite.i18n.SwedishAPI") ||
              (code != "tr" && name.startsWith("net.kogics.kojo.lite.i18n.tr."))) {
            throw new ClassNotFoundException("Startup unexpectedly loaded " + name)
          }
          super.loadClass(name, resolve)
        }
      }
      val oldLocale = Locale.getDefault
      try withProperty("user.language", "en") { withProperty("kojo.toolchain", "tr") {
        def module(name: String): (Class[_], AnyRef) = {
          val cls = loader.loadClass(name + "$")
          (cls, cls.getField("MODULE$").get(null))
        }
        val (initClass, init) = module("net.kogics.kojo.lite.i18n.LangInit")
        val prefs = preferences(code)
        initClass.getMethod("init", classOf[Preferences]).invoke(init, prefs) should be(code)
        val (registryClass, registry) = module("net.kogics.kojo.lite.i18n.LanguageEditorDataRegistry")
        def isKeyword = {
          val data = registryClass.getMethod("current").invoke(registry)
          val keywordSet = data.getClass.getMethod("keywordSet").invoke(data)
          keywordSet.getClass.getMethod("contains", classOf[Object]).invoke(keywordSet, keyword)
        }
        isKeyword should be(java.lang.Boolean.valueOf(code == "tr" || code == "sv"))
        val (behaviorsClass, behaviors) = module("net.kogics.kojo.lite.i18n.LanguageBehavior")
        val behavior = behaviorsClass.getMethod("current").invoke(behaviors)
        def present = behavior.getClass.getMethod("updateTypes", classOf[String]).invoke(behavior, "Int")
        present should be(translated)
        // Simulate a menu preference update and an unrelated compiler override.
        prefs.put("user.language", "de")
        System.setProperty("user.language", "de")
        System.setProperty("kojo.toolchain", "en")
        isKeyword should be(java.lang.Boolean.valueOf(code == "tr" || code == "sv"))
        present should be(translated)
        initClass.getMethod("language").invoke(init) should be(code)
      }}
      finally {
        Locale.setDefault(oldLocale)
        loader.close()
      }
    }
  }
}
