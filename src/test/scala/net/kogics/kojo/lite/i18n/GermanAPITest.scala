package net.kogics.kojo.lite.i18n

import org.scalatest.{Matchers, FunSuite}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * Tests for GermanAPI.
 *
 * @author Christoph Knabe  http://public.beuth-hochschule.de/~knabe/
 */
@RunWith(classOf[JUnitRunner])
class GermanAPITest extends FunSuite with Matchers {
  
  import GermanAPI._

  test("mehrmals(n){block} should repeat the block n times") {
    val sb = new StringBuilder(10)
    mehrmals(5){
      sb append "+-"
    }
    sb.toString should be("+-+-+-+-+-")
  }
  
  test("fürBereich(start, end){i => fn(i)} should call fn with the Int values from start to end"){
    val sb = new StringBuilder(10)
    fürBereich(1, 5){i => 
      sb.append(i).append(' ')
    }
    sb.toString should be("1 2 3 4 5 ")
  }
  
  test("fürAlle[T](iterable){e => fn(e)} should process all elements of iterable"){
    val sb = new StringBuilder(10)
    fürAlle(1 to 10 by 2){i => 
      sb.append(i).append(' ')
    }
    sb.toString should be("1 3 5 7 9 ")    
  }
  
  test("solange(condition){block} should execute block while condition holds"){
    val sb = new StringBuilder(10)
    var i = 10
    solange(i > 5){ 
      sb.append(i).append(' ')
      i -= 1
    }
    sb.toString should be("10 9 8 7 6 ")    
  }
  
}
