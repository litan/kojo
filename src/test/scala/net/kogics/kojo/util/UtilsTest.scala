package net.kogics.kojo.util

import org.scalatestplus.junit.JUnitSuite
import org.junit.Test
import org.junit.Assert._
import net.kogics.kojo.lite.NoOpKojoCtx
import java.util.Locale

/**Tests some methods of object Utils.
 * @author Christoph Knabe
 * @since 2016-03-01*/
class UtilsTest extends JUnitSuite with org.scalatest.Matchers {
  
  val kojoCtx = new NoOpKojoCtx

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
