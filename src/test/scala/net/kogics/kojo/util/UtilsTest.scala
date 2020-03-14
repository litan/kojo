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