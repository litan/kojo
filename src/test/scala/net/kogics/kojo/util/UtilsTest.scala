package net.kogics.kojo.util

import org.scalatestplus.junit.JUnitSuite
import org.junit.Test
import org.junit.Assert._
import net.kogics.kojo.lite.NoOpKojoCtx
import net.kogics.kojo.lite.ToolchainTestSupport.withTempDir
import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.Files
import java.util.Locale

/**Tests some methods of object Utils.
 * @author Christoph Knabe
 * @since 2016-03-01*/
class UtilsTest extends JUnitSuite with org.scalatest.Matchers {
  
  val kojoCtx = new NoOpKojoCtx

  @Test def includeTemplateValidation(): Unit = withTempDir { root =>
    val path = root.toPath.resolve("test template.kojo")
    val directive = s"// #include template $path"
    val duplicate = intercept[IllegalArgumentException] {
      Utils.preProcessInclude(s"$directive\n$directive")
    }
    duplicate.getMessage should include ("Only one #include template")

    val missing = intercept[IllegalArgumentException] {
      Utils.preProcessInclude(directive)
    }
    missing.getMessage should include (path.toString)

    val marker = "${usercode}"
    for (invalid <- Seq("val empty = ()", s"val inline = $marker", s"$marker\n$marker")) {
      Files.write(path, invalid.getBytes(UTF_8))
      val error = intercept[IllegalArgumentException] {
        Utils.preProcessInclude(directive)
      }
      error.getMessage should include ("exactly one ${usercode} on its own line")
    }
  }

  @Test def templatesInNestedIncludes(): Unit = withTempDir { root =>
    val parent = root.toPath.resolve("parent.kojo")
    val first = root.toPath.resolve("first.kojo")
    val second = root.toPath.resolve("second.kojo")
    val dependency = root.toPath.resolve("dependency.kojo")
    val sharedTemplate = root.toPath.resolve("shared-template.kojo")
    val dependencyTemplate = root.toPath.resolve("dependency-template.kojo")
    val marker = "${usercode}"
    Files.write(parent, s"// #include $first\n// #include $second\nval total = first + second\n".getBytes(UTF_8))
    Files.write(first, s"// #include template $sharedTemplate\nval first = 1".getBytes(UTF_8))
    Files.write(second, s"// #include template $sharedTemplate\nval second = 2\n".getBytes(UTF_8))
    Files.write(dependency, s"// #include template $dependencyTemplate\n// #include $parent\n3\n".getBytes(UTF_8))
    Files.write(sharedTemplate, s"//  #include $dependency\n// shared prefix\n$marker\n// shared suffix\n".getBytes(UTF_8))
    Files.write(dependencyTemplate, s"val fromTemplate = {\n$marker\n}\n".getBytes(UTF_8))

    val code = s"// #include $parent\n// #include $first\nval result = total\n"
    val (expanded, _, _) = Utils.preProcessInclude(code)

    def resolved(path: java.nio.file.Path) = Utils.absolutePath(path.toString)
    val firstBegin = s"// #begin-include: ${resolved(first)}"
    val firstEnd = s"// #end-include: ${resolved(first)}"
    val sharedBegin = s"// #begin-template: ${resolved(sharedTemplate)}"
    val sharedEnd = s"// #end-template: ${resolved(sharedTemplate)}"
    val dependencyBegin = s"// #begin-include: ${resolved(dependency)}"
    val dependencyEnd = s"// #end-include: ${resolved(dependency)}"
    expanded should include (s"$firstBegin\n$sharedBegin\n$dependencyBegin")
    expanded should include (s"$dependencyEnd\n//  #Include $dependency\n// shared prefix")
    expanded should include (s"val fromTemplate = {\n// #begin-usercode\n// #Include template $dependencyTemplate\n// #Include $parent\n3\n// #end-usercode\n}")
    for ((name, value) <- Seq("first" -> 1, "second" -> 2)) {
      expanded should include (
        s"// shared prefix\n// #begin-usercode\n// #Include template $sharedTemplate\nval $name = $value\n// #end-usercode\n// shared suffix"
      )
    }
    expanded should include (s"// shared suffix\n\n$sharedEnd\n\n$firstEnd")
    for (path <- Seq(parent, first, second, dependency)) {
      expanded.linesIterator.count(_ == s"// #begin-include: ${resolved(path)}") shouldBe 1
    }
    expanded should endWith ("val result = total\n")
  }

  @Test def roundDoubleIsIndependentOfFormattingLocale(): Unit = {
    val previousLocale = Locale.getDefault(Locale.Category.FORMAT)
    val locales = List(Locale.US, new Locale("sv", "SE"), new Locale("tr", "TR"),
      new Locale("hi", "IN"), Locale.GERMANY)
    val examples = List(
      (625.0, 2, 625.0),
      (105.0, 2, 105.0),
      (0.0, 2, 0.0),
      (123.456, 2, 123.46),
      (-123.456, 2, -123.46),
      (1.234567, 4, 1.2346),
      (2.6, 0, 3.0)
    )
    try {
      for (locale <- locales) {
        Locale.setDefault(Locale.Category.FORMAT, locale)
        for ((value, precision, expected) <- examples) {
          assertEquals(s"locale=$locale, value=$value, precision=$precision",
            expected, Utils.roundDouble(value, precision), 0.0)
        }
      }
    }
    finally Locale.setDefault(Locale.Category.FORMAT, previousLocale)
  }
  
  
  @Test def loadLocalizedResourceExistingGerman(): Unit ={
    //Given
    val existingRoot = "/samples/"
    val existingGermanFile = "tree1.kojo"
    val oldLocale = Locale.getDefault
    Locale.setDefault(Locale.GERMAN)
    try{
      //When
      val result = Utils.loadLocalizedResource(existingRoot, existingGermanFile)
      //Then
      if(result eq null){
        fail(s"Root $existingRoot, File $existingGermanFile")
      }
      result should include ("rot importieren")
    }finally{
      Locale.setDefault(oldLocale)
    }
  }
  
  @Test def loadLocalizedResourceExistingOnlyInBaseLanguage(): Unit ={
    //Given
    val existingRoot = "/samples/"
    val existingBaseFile = "some-notes.kojo"
    val oldLocale = Locale.getDefault
    Locale.setDefault(Locale.GERMAN)
    try{
      //When
      val result = Utils.loadLocalizedResource(existingRoot, existingBaseFile)
      //Then
      if(result eq null){
        fail(s"Root $existingRoot, File $existingBaseFile")
      }
      result should include ("Acoustic_Bass_Drum")
    }finally{
      Locale.setDefault(oldLocale)
    }
  }
  
  @Test def loadLocalizedResourceNotExisting(): Unit ={
    //Given
    val existingRoot = "/samples/"
    val notExistingFile = "a1b2c3d4e5.kojo"
    val oldLocale = Locale.getDefault
    Locale.setDefault(Locale.GERMAN)
    try{
      //When
      Utils.loadLocalizedResource(existingRoot, notExistingFile)
      //Then
      fail("expected: IllegalArgumentException")
    }catch{
      case expected: IllegalArgumentException => expected.toString should include (notExistingFile)
    }finally{
      Locale.setDefault(oldLocale)
    }
  }
  
  @Test def loadStringExisting(): Unit ={
    //Given
    val existingKey = "S_Kojo"
    //When
    val result = Utils.loadString(existingKey)
    //Then
    assertNotNull(result, result)
    result should include ("Kojo")    
  }
  
  @Test def loadStringNull(): Unit ={
    intercept[NullPointerException]{
      Utils.loadString(null)
    }
  }
  
  @Test def loadStringNotExisting(): Unit ={
    val notExistingKey = "a1b2c3d4e5f6g7"
    intercept[java.util.MissingResourceException]{(
      Utils.loadString(notExistingKey))
    }
  }


}
