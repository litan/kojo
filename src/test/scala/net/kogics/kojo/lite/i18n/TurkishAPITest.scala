package net.kogics.kojo.lite.i18n

import org.scalatest.{Matchers, FunSuite}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
  * Tests for TurkishAPI.
  *
  * @author Bulent Basaran   https://github.com/bulent2k2

  * Thanks to the original author (for GermanAPI):
  * Christoph Knabe  http://public.beuth-hochschule.de/~knabe/
  */
@RunWith(classOf[JUnitRunner])
class TurkishAPITest extends FunSuite with Matchers {
  
  import TurkishAPI._

  test("yinele(n){block} should repeat the block n times") {
    val sb = new StringBuilder(10)
    yinele(5){
      sb append "+-"
    }
    sb.toString should be("+-+-+-+-+-")
  }
  
  test("yineleDoğruysa(condition){block} should execute block while condition holds"){
    val sb = new StringBuilder(10)
    var i = 10
    yineleDoğruysa(i > 5){
      sb.append(i).append(' ')
      i -= 1
    }
    sb.toString should be("10 9 8 7 6 ")
  }
  
  test("yineleOlanaKadar(condition){block} should execute block while condition does not hold"){
    val sb = new StringBuilder(10)
    var i = 1
    yineleOlanaKadar(i > 5){
      sb.append(i).append(' ')
      i += 1
    }
    sb.toString should be("1 2 3 4 5 ")
  }
  
  test("yineleDizinli(n){i => fn(i)} should call fn n times with indices 1 to n"){
    val sb = new StringBuilder(10)
    yineleDizinli(5){i =>
      sb.append(i).append(' ')
    }
    sb.toString should be("1 2 3 4 5 ")
  }

  test("yineleİlktenSona(start, end){i => fn(i)} should call fn with the Int values from start to end"){
    val sb = new StringBuilder(10)
    yineleİlktenSona(1, 5){i =>
      sb.append(i).append(' ')
    }
    sb.toString should be("1 2 3 4 5 ")
  }
  
  test("yineleKere[T](iterable){e => fn(e)} should process all elements of iterable"){
    val sb = new StringBuilder(10)
    yineleKere(1 to 10 by 2){i =>
      sb.append(i).append(' ')
    }
    sb.toString should be("1 3 5 7 9 ")
  }

  test("yineleİçin[T](iterable){e => fn(e)} should process all elements of iterable"){
    val sb = new StringBuilder(10)
    yineleİçin(1 to 10 by 3){i =>
      sb.append(i).append(' ')
    }
    sb.toString should be("1 4 7 10 ")
  }

  test("İkil (Boolean in Turkish) should work") {
    val test0: İkil = yanlış
    val test1: İkil = doğru

    test0 || false should be(yanlış)
    test1 && true  should be(doğru)
  }
  
}
