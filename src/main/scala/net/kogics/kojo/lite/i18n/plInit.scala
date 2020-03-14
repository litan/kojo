/*
 * Copyright (C) 2013 
 *   Bjorn Regnell <bjorn.regnell@cs.lth.se>,
 *   Lalit Pant <pant.lalit@gmail.com>,
 *   Mikołaj Sochacki <mbsochacki@wp.pl>
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

//Polish Turtle wrapper for Kojo

package net.kogics.kojo.lite.i18n

import net.kogics.kojo.lite.CoreBuiltins
import net.kogics.kojo.lite.Builtins

object PolishAPI {
  import net.kogics.kojo.core.Turtle
  import java.awt.Color
  var builtins: net.kogics.kojo.lite.CoreBuiltins = _  //unstable reference to module
  
  trait PolishTurtle {
    def englishTurtle: Turtle
    def czyść() = englishTurtle.clear() 
    def pokaż() = englishTurtle.visible() 
    def ukryj() = englishTurtle.invisible() 
    def naprzód(kroki: Double) = englishTurtle.forward(kroki) 
    def naprzód() = englishTurtle.forward(25) 
    def tył() = englishTurtle.back()
    def tył(kroki:Double) = englishTurtle.back(kroki)
    def prawo(kąt: Double) = englishTurtle.right(kąt) 
    def prawo() = englishTurtle.right(90) 
    def lewo(kąt: Double) = englishTurtle.left(kąt) 
    def lewo() = englishTurtle.left(90) 
    def skoczDo(x: Double, y: Double) = englishTurtle.jumpTo(x, y) 
    def idźDo(x: Double, y: Double) = englishTurtle.moveTo(x, y) 
    def skocz(kroki: Double) = {
      englishTurtle.saveStyle() 
      englishTurtle.hop(kroki) 
      englishTurtle.restoreStyle()
    }
    def skocz(): Unit = skocz(25)
    def dom() = englishTurtle.home() 
    def kierunek(x: Double, y: Double) = englishTurtle.towards(x, y) 
    def ustawKąt(kąt: Double) = englishTurtle.setHeading(kąt) 
    def kąt = englishTurtle.heading 
    def wschód() = englishTurtle.setHeading(0) 
    def zachód() = englishTurtle.setHeading(180)
    def północ() = englishTurtle.setHeading(90) 
    def południe() = englishTurtle.setHeading(-90)
    def spowolnij(n: Long) = englishTurtle.setAnimationDelay(n) 
    def pisz(t: Any) = englishTurtle.write(t)
    def rozmiarCzcionki(s: Int) = englishTurtle.setPenFontSize(s)
    def łuk(promień: Double, kąt: Double) = englishTurtle.arc(promień, math.round(kąt).toInt)
    def okrąg(promień: Double) = englishTurtle.circle(promień)
    def położenie = englishTurtle.position
    def opuśćPisak() = englishTurtle.penDown()
    def podnieśPisak() = englishTurtle.penUp()
    def czyśćStylPisaka = englishTurtle.style.down
    def kolor(c: java.awt.Color) = englishTurtle.setPenColor(c)
    def kolorLosowy = builtins.Color(builtins.random(256), 
	builtins.random(256), builtins.random(256))
    def utwórzKolor(czerwony:Int, zielony:Int, niebieski:Int) = 
	builtins.Color(czerwony, zielony, niebieski)
    def wypełnienie(c: java.awt.Color) = englishTurtle.setFillColor(c)
    def grubość(n: Double) = englishTurtle.setPenThickness(n)
    def zapiszStyl() = englishTurtle.saveStyle()
    def przywróćStyl() = englishTurtle.restoreStyle()
    def zapamiętajKierunek() = englishTurtle.savePosHe()
    def odtwórzKierunek() = englishTurtle.restorePosHe()
    def pokażOś() = englishTurtle.beamsOn()
    def ukryjOś() = englishTurtle.beamsOff()
    def kostium(nazwaPliku: String) = englishTurtle.setCostume(nazwaPliku)
    def kostiumy(nazwaPliku: String*) = englishTurtle.setCostumes(nazwaPliku: _*)
    def następnyKostium() = englishTurtle.nextCostume()
  }
  class Żółw(override val englishTurtle: Turtle) extends PolishTurtle {
    def this(startX: Double, startY: Double, nazwaPlikuKostiumu: String) = 
        this(builtins.TSCanvas.newTurtle(startX, startY, nazwaPlikuKostiumu))
    def this(startX: Double, startY: Double) = this(startX, startY, "/images/turtle32.png")
    def this() = this(0,0)
  }
  class Żółw0(t0: => Turtle) extends PolishTurtle { //by-name construction as turtle0 is volatile }
    override def englishTurtle: Turtle = t0
  }
  object żółw extends Żółw0(builtins.TSCanvas.turtle0)
  def czyść() = builtins.TSCanvas.clear()
  def czyśćWyjście() = builtins.clearOutput()
  lazy val niebieski = builtins.blue //bla
  lazy val czerwony = builtins.red 
  lazy val żółty = builtins.yellow 
  lazy val zielony = builtins.green 
  lazy val fioletowy = builtins.purple
  lazy val różowy = builtins.pink 
  lazy val brązowy = builtins.brown 
  lazy val czarny = builtins.black 
  lazy val biały = builtins.white
  lazy val przezroczysty = builtins.noColor
  lazy val szary = builtins.gray
  lazy val pomarańczowy = builtins.orange
  def tło(kolor: Color) = builtins.setBackground(kolor)
  def tłoGradientPion(kolor1: Color, kolor2: Color) = builtins.TSCanvas.setBackgroundV(kolor1, kolor2)
  def tłoGradientPoz(kolor1: Color, kolor2: Color) = builtins.TSCanvas.setBackgroundH(kolor1, kolor2)
  object KcPL { //Key codes Polish
    lazy val VK_Ą = 260 
    lazy val VK_Ę = 280
    lazy val VK_Ó = 211
    lazy val VK_Ś = 346
    lazy val VK_Ł = 321
    lazy val VK_Ż = 379
    lazy val VK_Ź = 377
    lazy val VK_Ć = 262
    lazy val VK_Ń = 323
  }
  
  def powtarzaj(n: Int)(blok: => Unit): Unit = {
    for (i <- 1 to n) blok
  }
  def powtarzajZLicznikem(n: Int)(blok: Int => Unit): Unit = {
    for (i <- 1 to n) blok(i)
  }
  def dopóki(warunek: => Boolean)(blok: => Unit): Unit = {
    while (warunek) blok
  }
  
  //simple IO
  def wejście(tekst: String = "") =  builtins.readln(tekst)
  
  //math functions
  def zaokrągl(liczba: Number, miejscPoPrzecinku: Int = 0): Double = {
    val faktor = math.pow(10, miejscPoPrzecinku).toDouble
    math.round(liczba.doubleValue * faktor).toLong / faktor
  }
  def liczbaLosowa(n: Int) = builtins.random(n)
  def liczbaLosowaRzeczywista(n: Int) = builtins.randomDouble(n)
  
  //some type aliases in Polish
  type Całkowita = Int
  type Rzeczywista = Double
  type Napis = String
  
  //speedTest
  def czasSystemowy = System.nanoTime / 1000000000L //sekunder
}

object PlInit {
  def init(builtins: CoreBuiltins): Unit = {
    //initialize unstable value
    net.kogics.kojo.lite.i18n.PolishAPI.builtins = builtins
    builtins match {
      case b: Builtins =>
        println("Witamy w Kojo w polskiej wersji!")
        if (b.isScratchPad) {
          println("Jesteś w brudnopisie, historia zmian nie zostanie zapisana po jego zamknięciu") 
        }
        b.setEditorTabSize(2)

        //code completion
        b.addCodeTemplates(
          "pl",
          codeTemplates
        )
        //help texts
        b.addHelpContent(
          "pl",
          helpContent
        )

      case _ =>
    }
  }
  
  val codeTemplates = Map(
    "naprzód" -> "naprzód(${kroki})",
    "tył" -> "tył(${kroki})",
    "prawo" -> "prawo(${kąt})",
    "lewo" -> "lewo(${kąt})",
    "skoczDo" -> "skoczDo(${x},${y})",
    "idźDo" -> "idźDo(${x},${y})",
    "skocz" -> "skocz(${kroki})",
    "dom" -> "dom()",
    "kierunek" -> "kierunek(${x},${y})",
    "ustawKąt" -> "ustawKąt(${kąt})",
    "wschód" -> "wschód()",
    "zachód" -> "zachód()",
    "północ" -> "północ()",
    "południe" -> "południe()",
    "spowolnij" -> "spowolnij(${opóźnienie})",
    "pisz" -> "pisz(${tekst})",
    "rozmiarCzcionki" -> "rozmiarCzcionki(${rozmiar})",
    "łuk" -> "łuk(${promień},${kąt})",
    "okrąg" -> "ogrąg(${promień})",
    "pokaż" -> "pokaż()",
    "ukryj" -> "ukryj()",
    "położenie" -> "położenie",
    "opuśćPisak" -> "opuśćPisak()",
    "podnieśPisak" -> "podnieśPisak()",
    "czyśćStylPisaka" -> "czyśćStylPisaka",
    "kolor" -> "kolor(${kolorPisaka})",
    "wypełnienie" -> "wypełnienie(${kolorWypełnienia})",
    "grubość" -> "grubość(${grubośćPisaka})",
    "zapiszStyl" -> "zapiszStyl()",
    "przywróćStyl" -> "przywróćStyl()",
    "zapamiętajKierunek" -> "zapamiętajKierunek()",
    "odtwórzKierunek" -> "odtwórzKierunek()",
    "pokażOś" -> "pokażOś()",
    "ukryjOś" -> "ukryjOś()",
    "angielski" -> "angielski",
    "czyść" -> "czyść()",
    "czyśćWyjście" -> "czyśćWyjście()",
    "tło" -> "tło(${kolor})",
    "tłoGradientPion" -> "tłoGradientPion(${kolor1},${kolor2})",
    "powtarzaj" -> "powtarzaj (${ilość}) {\n    \n}",
    "powtarzajZLicznikem" -> "powtarzajZLicznikem (${ilość}) { i => \n    \n}",
    "dopóki" -> "dopóki (${ilość}) {\n    \n}",
    "drukuj" -> "drukuj(${tekst})",
    "wejście" -> "wejście(${tekst})",
    "zaokrągl" -> "zaokrągl(${tal},${miejscPoPrzecinku})",
    "libczbaLosowa" -> "liczbaLosowa(${wartośćMaksymalna})",
    "liczbaLosowaRzeczywista" -> "liczbaLosowaRzeczywista(${wartośćMaksymalna})",
    "czasSystemowy" -> "czasSystemowy",
    "kostium" -> "kostium(${nazwaPliku})",
    "kostiumy" -> "kostium(${nazwaPliku1},${nazwaPliku2})",
    "następnyKostium" -> "następnyKostium()"
  )

  val helpContent = Map(
    "naprzód" ->
      <div>
<strong>naprzód</strong>(kroki) - porusza żółwia w kierunku głowy o zadaną ilość kroków, lub gdy nie podamy parametru o 25 kroków. (piksele)
<br/><em>Przykład:</em> <br/><br/>
<pre>
czyść()    //czyść płótno
naprzód(100)  //przesuń żółwia do przodu o 100 kroków
naprzód()     // przesuń żółwia o 25 kroków
podnieśPisak   //podnieś pisak - żółw idąc nie rysuje
naprzód(200)  //przesuń żółwia o 200 kroków
prawo(45)  //obróć żółwia w prawo o 45 stopni
</pre>
      </div>.toString,
    "lewo" -> <div><strong>tył</strong>(kroki)<br/>porusza żółwiem do tyłu</div>.toString,
    "lewo" -> <div><strong>lewo</strong>(kąt)<br/>obraca żółwia w lewo o zadany kąt, domyślnie 90 stopni</div>.toString,
    "prawo" -> <div><strong>prawo</strong>(kąt)<br/>obraca żółwia w prawo o zadany kąt, domyślnie 90 stopni</div>.toString,
    "skoczDo" -> <div><strong>skoczDo</strong>(x, y)<br/>umieszcza żółwia na pozycji (x,y) nie rysując po płótnie</div>.toString,
    "idźDo" -> <div><strong>idźDo</strong>(x, y)<br/>przesuwa po linii prostej żółwia do punktu(x,y) <br/> Żółw zostawia na trasie ruchu linię</div>.toString,
    "skocz" -> <div><strong>skocz</strong>(kroki)<br/> </div>.toString,
    "dom" -> <div><strong>dom</strong>()<br/>żółw wraca do położenia (0,0) i ustawia się w kierunku północnym.</div>.toString,
    "kierunek" -> <div><strong>kierunek</strong>(x, y)<br/>ustawia żółwia w kierunku punktu o współrzędnych (x,y)</div>.toString,
    "ustawKąt" -> <div><strong>ustawKąt</strong>(kąt)<br/>ustawa kierunek żółwia o danym kącie gdzie 0 to wschód (w prawo).</div>.toString,
    "kąt" -> <div><strong>kąt</strong><br/>pobiera wartość aktualnego kąta ustawienia</div>.toString,
    "wschód" -> <div><strong>wschód</strong>()<br/>ustawia żółwia w kierunku wschodnim (w prawo).</div>.toString,
    "zachód" -> <div><strong>zachód</strong>()<br/> ustawia żółwia w kierunku zachodnim (w lewo).</div>.toString,
    "północ" -> <div><strong>północ</strong>()<br/>ustawia żółwia w kierunku  północnym (w górę).</div>.toString,
    "południe" -> <div><strong>południe</strong>()<br/>ustawia żółwia w kierunku południowym (w dół).</div>.toString,
    "spowolnij" -> <div><strong>spowolnij</strong>(opóźnienie)<br/>ustawia wartość opóźnienia animacji<br/>Domyślnie ustawiona wartość to<br/>spowolnij(1000). Im mniejsza liczba tym szybciej porusza się żółw</div>.toString,
    "pisz" -> <div><strong>pisz</strong>(tekst)<br/>Żółł pisze po płótnie (w prawo od kierunku ustawienia) <br/> Przykład: pisz("hej")</div>.toString,
    "rozmiarCzcionki" -> <div><strong>rozmiarCzcionki</strong>(rozmiar)<br/> Ustawia wielkość czcionki do pisania.</div>.toString,
    "łuk" -> <div><strong>łuk</strong>(promień, kąt)<br/>rysuje fragment okręgu</div>.toString,
    "okrąg" -> <div><strong>okrąg</strong>(promień)<br/>rysuje okrąg o zadanym promieniu</div>.toString,
    "pokaż" -> <div><strong>pokaż</strong>()<br/> pokazuje postać żółwia</div>.toString,
    "ukryj" -> <div><strong>ukryj</strong>()<br/> ukrywa postać żółwia</div>.toString,
    "położenie" -> <div><strong>położenie</strong><br/>pobiera położenie żółwia w danym momencie jako punkt Point(x,y)
<br/><em>Przykład:</em> <br/><br/>
<pre>
drukuj(położenie)
drukuj(położenie.x) 
drukuj(położenie.y) 

var x = położenie.x 
var y = położenie.y 
x = x - 300     
skoczDo(x, y)
</pre>
      </div>.toString,
    "opuśćPisak" -> <div><strong>opuśćPisak</strong>()<br/> umożliwia rysowanie gdy żółw się porusza</div>.toString,
    "podnieśPisak" -> <div><strong>podnieśPisak</strong>()<br/> po uruchomieniu polecenia poruszający się żółw nie zostawia śladu</div>.toString,
    "czyśćStylPisaka" -> <div><strong>czyśćStylPisaka</strong><br/> anuluje ustawienia koloru i grubości linii  Zwraca prawdę lub fałsz informujący czy pisak jest opuszczony <strong>true</strong> czy nie <strong>false</strong> </div>.toString,
    "kolor" -> <div><strong>kolor</strong>(kolorpisaka)<br/>Ustawia kolor pisaka
<br/>Zdefiniowane kolory: <br/>niebieski, czerwony, żółty, zielony, fioletowy, różowy, brązowy, czarny, biały, pomarańczowy, szary, przezroczysty.
<br/>Można też ustawić kolor korzystając z obiektu Color 
<br/><em>Przykład:</em> <br/><br/>
<pre>
kolor(niebieski)
naprzód()
kolor(Color(220,30,40,250)) //(fioletowy) maksymalna wartość to 255, czerwony, zielony, niebieski, przezroczystość
naprzód(200)
</pre> 
lub za pomocą ustawKolor(cz,n,z)
       </div>.toString,
"kolorLosowy" -> <div><strong>kolorLosowy</strong>()<br/> losuje kolor</div>.toString,
"utwórzKolor" -> <div><strong>utwórzKolor</strong>(czerwony:Int, zielony:Int, niebieski:Int)<br/> tworzy kolor ze zmiesznia podanych kolorów</div>.toString,
"wypełnienie" -> <div><strong>wypełnienie</strong>(kolor)<br/> Ustawia kolor wypełnienia. Aby wypełnić musimy wykonać figurę zamkniętą</div>.toString,
    "grubość" -> <div><strong>grubość</strong>(grubość)<br/>Ustawia grubość pisaka</div>.toString,
    "zapiszStyl" -> <div><strong>zapiszStyl</strong>()<br/> Zapamiętuje styl (kolor, grubość pędzla, wypełnienie kolorem)<br/>>Odczyt stylu odbywa się za pomocą przywróćStyl</div>.toString,
    "przywróćStyl" -> <div><strong>przywróćStyl</strong>()<br/>Służy do odczytu zapamiętanego stylu  funkcją przwróćStyl</div>.toString,
    "zapamiętajKierunek" -> <div><strong>zapamiętajKierunek</strong>()<br/>zapamiętuje obecny kierunek w celu odtworzenia go funkcją odtwórzKierunek</div>.toString,
    "odtwórzKierunek" -> <div><strong>odtwórzKierunek</strong>()<br/>odtwarza kierunek żółwia wcześniej zapamiętany funkcją zapamiętajKierunek</div>.toString,
    "pokażOś" -> <div><strong>pokażOś</strong>()<br/> pokazuje oś wskazującą kierunek ustawienia żółwia</div>.toString,
    "ukryjOś" -> <div><strong>ukryjOś</strong>()<br/> ukrywa oś wskazującą kierunek ustawienia żółwia</div>.toString,
    "angielski" -> <div><strong>angielski</strong><br/>odwołanie do poleceń dla żółwia w języku angielskim<br/> np. żółw.angielski</div>.toString,
    "czyść" -> <div><strong>czyść</strong>()<br/>czyści zawartość płótna</div>.toString,
    "czyśćWyjście" -> <div><strong>czyśćWyjście</strong>()<br/>czyści okno wyjścia (komunikatów)</div>.toString,
    "tło" -> <div><strong>tło</strong>(kolorTła)<br/>ustawia kolor tła, zdefiniowane kolory:<br/>niebieski, czerwony, żółty, zielony, fioletowy, różowy, brązowy, czarny, biały, przezroczysty.<br/>Możemy też użyć obiektu Color </div>.toString,
    "tłoGradientPion" -> <div><strong>tłoGradientPion</strong>(kolor1,kolor2)<br/> ustawia gradient zmieniający się w pionie od koloru kolor1 aż do koloru kolor2 <br/>Dostępe zdefiniowane kolory:<br/>niebieski, czerwony, żółty, zielony, fioletowy, różowy, brązowy, czarny, pomarańczowy, szary, biały, przezroczysty.<br/>Można też użyć obiektu Color </div>.toString,
"tłoGradientPoz" -> <div><strong>tłoGradientPion</strong>(kolor1,kolor2)<br/> ustawia gradient zmieniający się w poziomie od koloru kolor1 aż do koloru kolor2 <br/>Dostępe zdefiniowane kolory:<br/>niebieski, czerwony, żółty, zielony, fioletowy, różowy, brązowy, czarny, biały, pomarańczowy, szary, przezroczysty.<br/>Można też użyć obiektu Color </div>.toString,
    "powtarzaj" -> <div><strong>powtarzaj</strong>(ilość) {{ polecenia }} - powtarza <em>polecenia w nawiasie</em> zadaną ilość razy.
        <br/><em>Przykład:</em> <br/><br/>
        <pre>
powtarzaj(4) {{ 
      naprzód
      lewo
}}
        </pre>
      </div>.toString,
    "powtarzajZLicznikem" -> <div><strong>powtarzajZLicznikem</strong>(ilość) {{ i => polecenia }} - powtarza <em>polecenia</em>  możemy użyć zmiennej oznaczające nr pętli<strong>i</strong>
        <br/><em>Przykład:</em> <br/><br/>
        <pre>
powtarzajZLicznikem(10) {{ i =>
      drukuj(i)
}}
        </pre>
      </div>.toString,
    "dopóki" -> <div><strong>dopóki</strong>(warunek) {{  polecenia }} - powtarza  <em>polecenia</em> dopóki warunek logiczny <em>warunek</em> jest prawdziwy
        <br/><em>Przykład:</em> <br/><br/>
        <pre>var i = 0
dopóki(i{ "<" }10) {{ 
      drukuj(i)
      i = i + 1
}}
        </pre>
      </div>.toString,
    "drukuj" -> <div><strong>drukuj</strong>(tekst)<br/>drukuje <em>napis</em> do okna wyjścia <br/> Przykład: drukuj("hej")</div>.toString,
    "wejście" -> <div><strong>wejście</strong>(tekstKomunikatu)<br/> wyświetla na dole okna wyjścia pole w którym użytkownik wpisuje tekst wczytywany do zmiennej w skrypcie. tekstKomunikatu służy do inforamcji co użytkownik ma wpisać.<br/>
        <br/><em>Przykład:</em> <br/><br/>
        <pre>val x = wejście("Podaj swoje imię")
drukuj("Hej " + x + "!")
        </pre>
      </div>.toString,
    "zaokrągl" -> <div><strong>zaokrągl</strong>(rzeczywista, miejscPoPrzecinku)<br/>zaokrągla liczbę z przecinkiem do podanej ilości miejsc po przecinku<br/>
        <br/><em>Przykład:</em> <br/><br/>
        <pre>val t1 = zaokrągl(3.991,2) 
drukuj(t1)
val t2 = zaokrągl(3.999) 
drukuj(t2)
        </pre>
      </div>.toString,
    "czasSystemowy" -> <div><strong>czasSystemowy</strong><br/> pobiera aktualny czas w sekundach<br/>
        <br/><em>Przykład:</em> <br/><br/>
<pre>
val start = czasSystemowy
drukuj("Odlczanie!")
okrąg(34)
val stop = czasSystemowy
val s = stop - start
drukuj("Ułynęło " + zaokrągl(s,1) + " sekund.")
        </pre>
      </div>.toString,
    "liczbaLosowa" -> <div><strong>liczbaLosowa</strong>(maksimum)<br/>losuje liczbę z zakresu od 0 do (maksimum - 1) <br/><em>Przykład:</em><br/><pre>  def rzut_kostką = liczbaLosowa(5) + 1 </pre><br/> losuje liczbę od 1 do 6</div>.toString,
    "liczbaLosowaRzeczywista" -> <div><strong>liczbaLosowaRzeczywista</strong>(maksimum)<br/>losuje liczbę z zakresu od 0 do maksimum (przedział lewostronnie otwarty)<br/><em>Przykład:</em><br/><pre> def losowa = liczbaLosowaRzeczywista(20) + 1.0</pre><br/>losuje liczbę od  1.0 do poniżej 21.0</div>.toString,
    "kostium" -> <div><strong>kostium</strong>(Costume)<br/>ustawia obrazek żółwia, zdefiniowany w obiekcie Costume<br/><em>Przykład:</em><br/><pre>  
czyść       
kostium(Costume.bat1) 
naprzód(100) 
val nietoperz = new Żółw(100,100,"ścieżka_do_pliku")
nietoperz.naprzód(100) 
</pre><br/></div>.toString
  )
}
