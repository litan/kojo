package net.kogics.kojo.lite

import org.scalatest.junit.JUnitSuite
import org.junit.Test
import org.junit.Assert._

/**Tests some methods of object CodeExecutionSupport.
 * @author Christoph Knabe
 * @since 2016-03-01*/
class CodeExecutionSupportTest extends JUnitSuite with org.scalatest.Matchers {
  
  import CodeExecutionSupport._
  
  val kojoCtx = new NoOpKojoCtx
  
  
  @Test def tabulateNormal(){
    //Given
    val head = "Welcome to Kojo"
    val instructions = List(
      "Press mouse right -> Show context menu", //18 chars left of "->"
      "Type key -> Insert it into the script editor" //9 chars left of "->"
    )
    //When
    val result = makeTabulatedWelcomeMessage(head, instructions)
    //Then
    assertNotNull(result, result)
    result shouldBe "Welcome to Kojo" + EOL +
      "* Press mouse right -> Show context menu" + EOL + //"* " + 18 chars left of "->"
      "* Type key          -> Insert it into the script editor" + EOL //"* " + 18 chars left of "->"
  }
  
  @Test def tabulateExtraneousWhiteSpace(){
    //Given
    val head = "Welcome to Kojo"
    val instructions = List(
      "  Press mouse right      -> Show context menu  ",
      "Type key   ->   Insert it into the script editor"
    )
    //When
    val result = makeTabulatedWelcomeMessage(head, instructions)
    //Then
    assertNotNull(result, result)
    result shouldBe "Welcome to Kojo" + EOL +
      "* Press mouse right -> Show context menu" + EOL + //17 chars left of " -> "
      "* Type key          -> Insert it into the script editor" + EOL
  }
  
  @Test def tabulateWithoutArrow(){
    //Given
    val head = "Welcome to Kojo"
    val instructions = List(
      "Type key: Insert it into the script editor"
    )
    //When
    val result = makeTabulatedWelcomeMessage(head, instructions)
    //Then
    assertNotNull(result, result)
    result shouldBe "Welcome to Kojo" + EOL +
      "* Type key: Insert it into the script editor" + EOL
  }
  
  @Test def tabulateEmpties(){
    //Given
    val head = ""
    val instructions = List(
    )
    //When
    val result = makeTabulatedWelcomeMessage(head, instructions)
    //Then
    assertNotNull(result, result)
    result shouldBe EOL
  }
  
  @Test def tabulateNulls(){
    intercept[NullPointerException]{
      makeTabulatedWelcomeMessage(null, null)
    }
  }


}