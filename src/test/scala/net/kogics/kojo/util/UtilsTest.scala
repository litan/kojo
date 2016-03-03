package net.kogics.kojo.util

import org.scalatest.junit.JUnitSuite
import org.junit.Test
import org.junit.Assert._
import net.kogics.kojo.lite.NoOpKojoCtx

/**Tests some methods of object Utils.
 * @author Christoph Knabe
 * @since 2016-03-01*/
class UtilsTest extends JUnitSuite with org.scalatest.Matchers {
  
  val kojoCtx = new NoOpKojoCtx
  
  @Test def loadStringExisting(){
    //Given
    val existingKey = "S_Kojo"
    //When
    val result = Utils.loadString(existingKey)
    //Then
    assertNotNull(result, result)
    result should include ("Kojo")    
  }
  
  @Test def loadStringNull(){
    intercept[NullPointerException]{
      Utils.loadString(null)
    }
  }
  
  @Test def loadStringNotExisting(){
    val notExistingKey = "a1b2c3d4e5f6g7"
    intercept[java.util.MissingResourceException]{(
      Utils.loadString(notExistingKey))
    }
  }


}