/*
 * Copyright (C) 2013 
 *   Bjorn Regnell <bjorn.regnell@cs.lth.se>,
 *   Lalit Pant <pant.lalit@gmail.com> 
 *
 * The contents of this file are subject to the GNU General Public License
 * Version 3 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 */

// Russian Turtle wrapper for Kojo

package net.kogics.kojo.lite.i18n

import net.kogics.kojo.lite.CoreBuiltins
import net.kogics.kojo.lite.Builtins
import net.kogics.kojo.xscala.RepeatCommands

object RussianAPI {
  import net.kogics.kojo.core.Turtle
  import java.awt.Color
  var builtins: net.kogics.kojo.lite.CoreBuiltins = _ //unstable reference to module

  trait RussianTurtle {
    def englishTurtle: Turtle
    def очистить() = englishTurtle.clear()
    def видимый() = englishTurtle.visible()
    def невидимый() = englishTurtle.invisible()
    def вперёд(шаги: Double) = englishTurtle.forward(шаги)
    def вперёд() = englishTurtle.forward(25)
    def вправо(угол: Double) = englishTurtle.right(угол)
    def вправо() = englishTurtle.right(90)
    def влево(угол: Double) = englishTurtle.left(угол)
    def влево() = englishTurtle.left(90)
    def перепрыгнуть(x: Double, y: Double) = englishTurtle.jumpTo(x, y)
    def идти(x: Double, y: Double) = englishTurtle.moveTo(x, y)
    def прыгнуть(n: Double) = {
      englishTurtle.saveStyle() //to preserve pen state
      englishTurtle.hop(n) //hop change state to penDown after hop
      englishTurtle.restoreStyle()
    }
    def прыгнуть(): Unit = прыгнуть(25)
    def дом() = englishTurtle.home()
    def к(x: Double, y: Double) = englishTurtle.towards(x, y)
    def установить_заголовок(угол: Double) = englishTurtle.setHeading(угол)
    def заголовок = englishTurtle.heading
    def восток() = englishTurtle.setHeading(0)
    def запад() = englishTurtle.setHeading(180)
    def север() = englishTurtle.setHeading(90)
    def юг() = englishTurtle.setHeading(-90)
    def задержка(n: Long) = englishTurtle.setAnimationDelay(n)
    def запись(t: Any) = englishTurtle.write(t)
    def размер_шрифта(размер: Int) = englishTurtle.setPenFontSize(размер)
    def дуга(радиус: Double, угол: Double) = englishTurtle.arc(радиус, math.round(угол).toInt)
    def круг(радиус: Double) = englishTurtle.circle(радиус)
    def положение = englishTurtle.position
    def опустить_перо() = englishTurtle.penDown()
    def поднять_перо() = englishTurtle.penUp()
    def опущено_ли_перо = englishTurtle.style.down
    def цвет(цвет: java.awt.Color) = englishTurtle.setPenColor(цвет)
    def заливка(цвет: java.awt.Color) = englishTurtle.setFillColor(цвет)
    def толщина(n: Double) = englishTurtle.setPenThickness(n)
    def сохранить_cтиль() = englishTurtle.saveStyle()
    def восстановить_cтиль() = englishTurtle.restoreStyle()
    def сохранить_позицию_заголовка() = englishTurtle.savePosHe()
    def восстановить_позицию_заголовка() = englishTurtle.restorePosHe()
    def лучи_вкл() = englishTurtle.beamsOn()
    def лучи_выкл() = englishTurtle.beamsOff()
    def костюм(имя_файла: String) = englishTurtle.setCostume(имя_файла)
    def костюмы(имя_файла: String*) = englishTurtle.setCostumes(имя_файла: _*)
    def следующий_костюм() = englishTurtle.nextCostume()
  }
  class Черепашка(override val englishTurtle: Turtle) extends RussianTurtle {
    def this(startX: Double, startY: Double, costumeFileName: String) = this(builtins.TSCanvas.newTurtle(startX, startY, costumeFileName))
    def this(startX: Double, startY: Double) = this(startX, startY, "/images/turtle32.png")
    def this() = this(0, 0)
  }
  class Черепашка0(t0: => Turtle) extends RussianTurtle { //by-name construction as turtle0 is volatile }
    override def englishTurtle: Turtle = t0
  }
  object черепашка extends Черепашка0(builtins.TSCanvas.turtle0)
  def очистить() = builtins.TSCanvas.clear()
  def очистить_вывод() = builtins.clearOutput()
  lazy val синий = builtins.blue
  lazy val красный = builtins.red
  lazy val жёлтый = builtins.yellow
  lazy val зелёный = builtins.green
  lazy val фиолетовый = builtins.purple
  lazy val розовый = builtins.pink
  lazy val коричневый = builtins.brown
  lazy val чёрный = builtins.black
  lazy val белый = builtins.white
  lazy val без_цвета = builtins.noColor
  def установить_фон(c: Color) = builtins.setBackground(c)
  def установить_фон_2(c1: Color, c2: Color) = builtins.TSCanvas.setBackgroundV(c1, c2)

  //  object KcSwe { //Key codes for Swedish keys
  //    lazy val VK_Å = 197
  //    lazy val VK_Ä = 196
  //    lazy val VK_Ö = 214
  //  }

  //loops 
  def повторить(n: Int)(block: => Unit) {
    RepeatCommands.repeat(n) { block }
  }

  def повторить_i(n: Int)(block: Int => Unit) {
    RepeatCommands.repeati(n) { i => block(i) }
  }

  def повторить_пока(условие: => Boolean)(block: => Unit) {
    RepeatCommands.repeatWhile(условие) { block }
  }

  def повторить_для[T](последовательность: Iterable[T])(block: T => Unit) {
    RepeatCommands.repeatFor(последовательность) { block }
  }

  //simple IO
  def читать(подсказка: String = "") = builtins.readln(подсказка)

  def печатать(data: Any) = println(data) //Transferred here from sv.tw.kojo.
  def печатать() = println()

  //math functions
  def округлить(число: Number, цифры: Int = 0): Double = {
    val faktor = math.pow(10, цифры).toDouble
    math.round(число.doubleValue * faktor).toLong / faktor
  }
  def случайное(верхняя_граница: Int) = builtins.random(верхняя_граница)
  def случайное_двойное(верхняя_граница: Int) = builtins.randomDouble(верхняя_граница)

  //some type aliases in Swedish
  type целое = Int
  type двойное = Double
  type строка = String
}

object RussianInit {
  def init(builtins: CoreBuiltins) {
    //initialize unstable value
    RussianAPI.builtins = builtins
    builtins match {
      case b: Builtins =>
        println("Добро пожаловать в Kojo с русской черепашкой!")
        if (b.isScratchPad) {
          println("История не будет сохранена при закрытии блокнота Kojo.")
        }
        
//        b.setEditorTabSize(2)

        //code completion
        b.addCodeTemplates(
          "ru",
          codeTemplates
        )
        //help texts
        b.addHelpContent(
          "ru",
          helpContent
        )

      case _ =>
    }
  }

  val codeTemplates = Map(
    "вперёд" -> "вперёд(${шаги})",
    "вправо" -> "вправо(${угол})",
    "влево" -> "влево(${угол})",
    "перепрыгнуть" -> "перепрыгнуть(${x},${y})",
    "идти" -> "идти(${x},${y})",
    "прыгнуть" -> "прыгнуть(${шаги})",
    "дом" -> "дом()",
    "к" -> "к(${x},${y})",
    "установить_заголовок" -> "установить_заголовок(${угол})",
    "восток" -> "восток()",
    "запад" -> "запад()",
    "север" -> "север()",
    "юг" -> "юг()",
    "задержка" -> "задержка(${мс})",
    "запись" -> "запись(${текст})",
    "размер_шрифта" -> "размер_шрифта(${размер})",
    "дуга" -> "дуга(${радиус},${угол})",
    "круг" -> "круг(${радиус})",
    "видимый" -> "видимый()",
    "невидимый" -> "невидимый()",
    "опустить_перо" -> "опустить_перо()",
    "поднять_перо" -> "поднять_перо()",
    "цвет" -> "цвет(${цвет})",
    "заливка" -> "заливка(${цвет})",
    "толщина" -> "толщина(${ширина})",
    "сохранить_cтиль" -> "сохранить_cтиль()",
    "восстановить_cтиль" -> "восстановить_cтиль()",
    "сохранить_позицию_заголовка" -> "сохранить_позицию_заголовка()",
    "восстановить_позицию_заголовка" -> "восстановить_позицию_заголовка()",
    "лучи_вкл" -> "лучи_вкл()",
    "лучи_выкл" -> "лучи_выкл()",
    "очистить" -> "очистить()",
    "очистить_вывод" -> "очистить_вывод()",
    "установить_фон" -> "установить_фон(${цвет})",
    "установить_фон_2" -> "установить_фон_2(${цвет1},${цвет2})",
    "повторить" -> "повторить(${счёт}) {\n    ${cursor}\n}",
    "повторить_i" -> "повторить_i(${счёт}) { i =>\n    ${cursor}\n}",
    "повторить_пока" -> "повторить_пока(${условие}) {\n    ${cursor}\n}",
    "повторить_для" -> "повторить_для(${последовательность}) { ${e} =>\n    ${cursor}\n}",
    "печатать" -> "печатать(${текст})",
    "читать" -> "читать(${подсказка})",
    "округлить" -> "округлить(${число},${цифры})",
    "случайное" -> "случайное(${верхняя_граница})",
    "случайное_двойное" -> "случайное_двойное(${верхняя_граница})",
    "костюм" -> "костюм(${имя_файла})",
    "костюмы" -> "костюмы(${имя_файла1},${имя_файла2})",
    "следующий_костюм" -> "следующий_костюм()"
  )

  val helpContent = Map(
    "fram" ->
      <div>
<strong>fram</strong>(steg) - Paddan går frammåt det antal steg du anger i riktningen dit nosen pekar.
<br/>Om pennan är nere så ritar paddan när den går frammåt.
<br/><em>Exempel:</em> <br/><br/>
<pre>
sudda()    //ritfönstret suddas och paddan ställs i mitten     
fram(100)  //paddan går 100 steg
fram()     //inget värde: paddan går 25 steg
pennaUpp   //paddan lyfter pennan
fram(200)  //paddan går 200 steg utan att rita
höger(45)  //paddan vrider sig 45 grader åt höger
</pre>
      </div>.toString,
    "vänster" -> <div><strong>vänster</strong>(vinkel)<br/>Paddan vrider åt vänster.</div>.toString,
    "höger" -> <div><strong>höger</strong>(vinkel)<br/>Paddan vrider sig åt höger.</div>.toString,
    "hoppaTill" -> <div><strong>hoppaTill</strong>(x, y)<br/>Paddan hoppar till läge (x,y) utan att rita och utan att ändra riktning.</div>.toString,
    "gåTill" -> <div><strong>gåTill</strong>(x, y)<br/>Paddan vrider sig mot läge (x,y) och går dit.<br/>Om pennan är nere så ritar paddan när den går.</div>.toString,
    "hoppa" -> <div><strong>hoppa</strong>(steg)<br/>Paddan hoppar i riktningen dit nosen pekar det antal steg som anges utan att rita även om pennan är nere.</div>.toString,
    "hem" -> <div><strong>hem</strong>()<br/>Paddan går tillbaka till läge origo (0,0) och vrider sig så att nosen pekar norr.<br/>Om pennan är nere så ritar paddan när den går.</div>.toString,
    "mot" -> <div><strong>mot</strong>(x, y)<br/>Paddan vrider sig så att nosen pekar mot läge (x,y)</div>.toString,
    "sättVinkel" -> <div><strong>sättVinkel</strong>(vinkel)<br/>Paddan vrider sig så att nosen får den vinkel som anges.</div>.toString,
    "vinkel" -> <div><strong>vinkel</strong>(vinkel)<br/>Ger värdet på vinkeln dit paddans nos pekar.</div>.toString,
    "öster" -> <div><strong>öster</strong>()<br/>Paddan vrider sig så att nosen pekar mot öster (höger).</div>.toString,
    "väster" -> <div><strong>väster</strong>()<br/>Paddan vrider sig så att nosen pekar mot väster (vänster).</div>.toString,
    "norr" -> <div><strong>norr</strong>()<br/>Paddan vrider sig så att nosen pekar mot norr (upp).</div>.toString,
    "söder" -> <div><strong>söder</strong>()<br/>Paddan vrider sig så att nosen pekar mot söder (ner).</div>.toString,
    "sakta" -> <div><strong>sakta</strong>(fördröjning)<br/>Ju mer fördröjning desto långsammare padda.<br/>Minsta fördröjning är 0<br/>dröj(1000) är ganska långsamt.</div>.toString,
    "skriv" -> <div><strong>skriv</strong>(sträng)<br/>Paddan skriver en sträng i ritfönstret<br/>En sträng måste ha dubbelfnuttar i början och slutet. Exempel: skriv("hej")</div>.toString,
    "textstorlek" -> <div><strong>textstorlek</strong>(storlek)<br/>Ändrar storleken på texten som paddan skriver.</div>.toString,
    "båge" -> <div><strong>båge</strong>(radie, vinkel)<br/>Paddan ritar ett cirkelsegment med angiven radie och vinkel.</div>.toString,
    "cirkel" -> <div><strong>cirkel</strong>(radie)<br/>Paddan ritar en cirkel med angiven radie.</div>.toString,
    "synlig" -> <div><strong>synlig</strong>()<br/> Gör så att paddan syns igen om den är osynlig.</div>.toString,
    "osynlig" -> <div><strong>osynlig</strong>()<br/>Gör paddan osynlig.</div>.toString,
    "läge" -> <div><strong>läge</strong><br/>Ger paddans läge som ett punktvärde Point(x,y)
<br/><em>Exempel:</em> <br/><br/>
<pre>
utdata(läge)     //skriver ut paddans läge i utdatafönstret     
utdata(läge.x)     //skriver ut paddans x-läge i utdatafönstret     
utdata(läge.y)     //skriver ut paddans y-läge i utdatafönstret     

var x = läge.x   //lagrar paddans x-läge i variablen x
var y = läge.y   //lagrar paddans y-läge i variablen y
x = x - 100      //ändrar variabeln x till gamla värdet minus 100
hoppaTill(x, y)  //hoppar till nytt läge med ändrat x 
</pre>
      </div>.toString,
    "pennaNer" -> <div><strong>pennaNer</strong>()<br/>Sätter ner paddans penna så att den ritar när paddan går.</div>.toString,
    "pennaUpp" -> <div><strong>pennaUpp</strong>()<br/>Lyfter upp paddans penna så att den inte ritar när paddan går.</div>.toString,
    "pennanÄrNere" -> <div><strong>pennanÄrNere</strong><br/>Kollar om paddans penna är nere. Ger <strong>true</strong> om pennan är nere och <strong>false</strong> om pennan är uppe.</div>.toString,
    "färg" -> <div><strong>färg</strong>(pennfärg)<br/>Gör så att paddans penna ritar med angiven pennfärg.
<br/>Du kan anväda dessa färdigblandade färger:<br/>blå, röd, gul, grön, lila, rosa, brun, svart, vit, genomskinlig.
<br/>Du kan blanda egna färger med Color 
<br/><em>Exempel:</em> <br/><br/>
<pre>
färg(blå)                  //sätter pennans färg till blå
fram()                     //paddan går frammåt
färg(Color(220,30,40,250)) //blandar ny färg (ljuslila) 
//rödvärde=220, grönvärde=30, blåvärde=40, genomskinlighet=250
//färgvärden till Color mellan 0 och 255
fram(200)
</pre>
       </div>.toString,
    "fyll" -> <div><strong>fyll</strong>(fyllfärg)<br/>Gör så att paddan fyller i med angiven fyllfärg när den ritar.
<br/>Du kan anväda dessa färdigblandade färger:
<br/>blå, röd, gul, grön, lila, rosa, brun, svart, vit, genomskinlig.
<br/>Du kan blanda egna färger med Color 
<br/><em>Exempel:</em> <br/><br/>
<pre>
sudda();sakta(0)
fyll(blå)          //sätter fyllfärg till blå
cirkel(100)        //paddan ritar blå cirkel med radie 100
hoppa(100)         //paddan hoppar fram 100 steg
fyll(genomskinlig) //sätter fyllfärg till genomskinlig
cirkel(100)        //paddan ritar genomskinlig cirkel
</pre>
      </div>.toString,
    "bredd" -> <div><strong>bredd</strong>(pennbredd)<br/>Ändrar pennbredden på paddans penna.<br/>Ju högre pennbredd desto tjockare streck.</div>.toString,
    "sparaStil" -> <div><strong>sparaStil</strong>()<br/>Sparar undan pennans färg, fyllfärg, bredd och textstorlek.<br/>Du kan få tillbaka den sparade stilen med laddaStil</div>.toString,
    "laddaStil" -> <div><strong>laddaStil</strong>()<br/>Hämtar sparad pennstil och sätter tillbaka pennans färg, fyllfärg, bredd och textstorlek.<br/>Du spara en pennstil med sparaStil</div>.toString,
    "sparaLägeRiktning" -> <div><strong>sparaLägeRiktning</strong>()<br/>Sparar undan pennans läge och riktning.<br/>Du kan få tillbaka den sparade pennans position och riktning med laddaLägeRiktning</div>.toString,
    "laddaLägeRiktning" -> <div><strong>laddaLägeRiktning</strong>()<br/>Hämtar sparad riktning och läge.<br/>Du sparar en pennstil med sparaLägeRiktning</div>.toString,
    "siktePå" -> <div><strong>siktePå</strong>()<br/>Visar vilket håll paddan siktar mot med ett hårkors-sikte.</div>.toString,
    "sikteAv" -> <div><strong>sikteAv</strong>()<br/>Gömmer paddans hårkors-sikte.</div>.toString,
    "engelska" -> <div><strong>engelska</strong><br/>Ger den engelska paddan.<br/>Om du skriver:<br/>padda.eneglska.<br/>kan du se allt som en padda kan göra på engelska.</div>.toString,
    "sudda" -> <div><strong>sudda</strong>()<br/>Suddar allt som ritats i ritfönstret.</div>.toString,
    "suddaUtdata" -> <div><strong>suddaUtdata</strong>()<br/>Suddar allt som skrivits i utdatafönstret.</div>.toString,
    "bakgrund" -> <div><strong>bakgrund</strong>(bakgrundsfärg)<br/>Gör så att bakgrundsfärgen ändras.<br/>Du kan anväda dessa färdigblandade färger:<br/>blå, röd, gul, grön, lila, rosa, brun, svart, vit, genomskinlig.<br/>Du kan blanda egna färger med Color </div>.toString,
    "bakgrund2" -> <div><strong>bakgrund</strong>(färg1,färg2)<br/>Gör så att bakgrundsfärgen blir en övergång från färg1 till färg2.<br/>Du kan anväda dessa färdigblandade färger:<br/>blå, röd, gul, grön, lila, rosa, brun, svart, vit, genomskinlig.<br/>Du kan blanda egna färger med Color </div>.toString,
    "upprepa" -> <div><strong>upprepa</strong>(antal) {{ satser }} - upprepar <em>satser</em> det antal gånger som anges.
        <br/><em>Exempel:</em> <br/><br/>
        <pre>
upprepa(4) {{ 
      fram
      vänster
}}
        </pre>
      </div>.toString,
    "räkneslinga" -> <div><strong>räkneslinga</strong>(antal) {{ i => satser }} - upprepar <em>satser</em> det antal gånger som anges och räknar varje runda i slingan. Räknarens värde finns i värdet <strong>i</strong>
        <br/><em>Exempel:</em> <br/><br/>
        <pre>
räkneslinga(10) {{ i =>
      utdata(i)
}}
        </pre>
      </div>.toString,
    "sålänge" -> <div><strong>sålänge</strong>(villkor) {{  satser }} - upprepar <em>satser</em> så länge <em>villkor</em> är sant. 
        <br/><em>Exempel:</em> <br/><br/>
        <pre>var i = 0
sålänge(i{ "<" }10) {{ 
      utdata(i)
      i = i + 1
}}
        </pre>
      </div>.toString,
    "utdata" -> <div><strong>utdata</strong>(textsträng)<br/>Skriver texten i <em>textsträng</em> i utdatafönstret<br/>En textsträng måste ha dubbelfnuttar i början och slutet. Exempel: utdata("hej")</div>.toString,
    "indata" -> <div><strong>indata</strong>(ledtext)<br/>Skriver ut ledtext i utdatafönstret och väntar på inmatning av text tills du trycker Enter.<br/>
        <br/><em>Exempel:</em> <br/><br/>
        <pre>val x = indata("Skriv ditt namn ")
utdata("Hej " + x + "!")
        </pre>
      </div>.toString,
    "avrunda" -> <div><strong>avrunda</strong>(decimaltal, antalDecimaler)<br/>Avrundar decimaltal till angivet antal decimaler<br/>
        <br/><em>Exempel:</em> <br/><br/>
        <pre>val t1 = avrunda(3.991,2) //Avrundar till 2 decimaler
utdata(t1)
val t2 = avrunda(3.999) //Avrundar till 0 decimaler
utdata(t2)
        </pre>
      </div>.toString,
    "systemtid" -> <div><strong>systemtid</strong><br/>Ger systemklockans tid i sekunder. Du kan använda systemtid för att mäta hur lång tid något tar.<br/>
        <br/><em>Exempel:</em> <br/><br/>
<pre>
val start = systemtid
utdata("Ha tålamod!")
räknaTill(100000000)
val stopp = systemtid
val s = stopp - start
utdata("Du hade tålamod i " + avrunda(s,1) + " sekunder.")
        </pre>
      </div>.toString,
    "räknaTill" -> <div><strong>räknaTill</strong>(tal)<br/>Kollar hur lång tid det tar för datorn att räkna till ett visst tal. DU kan prova med ganska stora tal<br/>
        <br/><em>Exempel:</em> <br/><br/>
        <pre>
räknaTill(5000)
        </pre>
      </div>.toString,
    "slumptal" -> <div><strong>slumptal</strong>(mindreÄn)<br/>Ger ett slumptal mellan 0 och mindreÄn.<br/><em>Exempel:</em><br/><pre>  def slump = slumptal(20) + 1 </pre><br/>Ger slumptal från 1 till och med 20</div>.toString,
    "slumptalMedDecimaler" -> <div><strong>slumptalMedDecimaler</strong>(mindreÄn)<br/>Ger ett slumptal med decimaler mellan 0 och mindreÄn.<br/><em>Exempel:</em><br/><pre> def slump = slumptalMedDecimaler(20) + 1.0</pre><br/>Ger slumptal med decimaler från 1.0 till och med 20.0</div>.toString,
    "kostym" -> <div><strong>kostym</strong>(filNamn)<br/>Ändrar paddans utseende efter bild i fil.<br/><em>Exempel:</em><br/><pre>  
sudda       
kostym("bakgrund.jpg") //den vanliga paddan blir bakgrund.jpg 
fram(100) //bakgrunden flyttas
val gubbe = new Padda(100,100,"gubbe.jpg") //ny padda skapas på plats (100, 100) med bilden gubbe.jpg 
gubbe.fram(100) 
</pre><br/></div>.toString
  )
}
