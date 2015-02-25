/*
 * Copyright (C) 2013 
 *   Bjorn Regnell <bjorn.regnell@cs.lth.se>,
 *   Lalit Pant <pant.lalit@gmail.com> 
 *   Christoph Knabe http://public.beuth-hochschule.de/~knabe/
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

//German Turtle wrapper for Kojo

package net.kogics.kojo.lite.i18n

import net.kogics.kojo.lite.CoreBuiltins
import net.kogics.kojo.lite.Builtins
import net.kogics.kojo.xscala.RepeatCommands

object GermanAPI {
  import net.kogics.kojo.core.Turtle
  import java.awt.Color
  var builtins: net.kogics.kojo.lite.CoreBuiltins = _  //unstable reference to module
  
  trait GermanTurtle {
    def englishTurtle: Turtle
    //sudda()
    def leeren() = englishTurtle.clear()
    //synlig()
    def sichtbar() = englishTurtle.visible()
    //osynlig()
    def unsichtbar() = englishTurtle.invisible()
    //fram(steg)
    def vor(schritte: Double) = englishTurtle.forward(schritte)
    //fram()
    def vor() = englishTurtle.forward(25)
    //höger(vinkel)
    def rechts(grad: Double) = englishTurtle.right(grad)
    //höger()
    def rechts() = englishTurtle.right(90)
    //vänster(vinkel)
    def links(grad: Double) = englishTurtle.left(grad)
    //vänster()
    def links() = englishTurtle.left(90)
    //hoppaTill(x, y)
    def springen(x: Double, y: Double) = englishTurtle.jumpTo(x, y)
    //gåTill(x,y)
    def gehen(x: Double, y: Double) = englishTurtle.moveTo(x, y)
    //hoppa(steg)
    def springen(schritte: Double) = {
      englishTurtle.saveStyle() //to preserve pen state
      englishTurtle.hop(schritte) //hop changes state to penDown after hop
      englishTurtle.restoreStyle()
    }
    //hoppa()
    def springen(): Unit = springen(25)
    //hem()
    def heim() = englishTurtle.home()
    //mot(x,y)
    def schauen(x: Double, y: Double) = englishTurtle.towards(x, y)
    //sättVinkel(vinkel)
    def winkel(grad: Double) = englishTurtle.setHeading(grad)
    //vinkel
    def winkel = englishTurtle.heading
    //öster()
    def ost() = englishTurtle.setHeading(0)
    //väster()
    def west() = englishTurtle.setHeading(180)
    //norr()
    def nord() = englishTurtle.setHeading(90)
    //söder()
    def süd() = englishTurtle.setHeading(-90)
    //sakta(n)
    def langsam(n: Long) = englishTurtle.setAnimationDelay(n)
    //skriv(t)
    def schreiben(t: Any) = englishTurtle.write(t)
    //textstorlek(s)
    def schriftgröße(s: Int) = englishTurtle.setPenFontSize(s)
    //båge(radie, vinkel)
    def bogen(radius: Double, winkel: Double) = englishTurtle.arc(radius, math.round(winkel).toInt)
    //cirkel(radie)
    def kreis(radius: Double) = englishTurtle.circle(radius)
    //läge
    def ort = englishTurtle.position
    //pennaNer()
    def stiftRunter() = englishTurtle.penDown()
    //pennaUpp()
    def stiftRauf() = englishTurtle.penUp()
    //pennanÄrNere
    def stiftUnten = englishTurtle.style.down
    def stiftOben = !englishTurtle.style.down
    //färg(c)
    def stiftfarbe(c: java.awt.Color) = englishTurtle.setPenColor(c)
    //fyll(c)
    def füllfarbe(farbe: java.awt.Color) = englishTurtle.setFillColor(farbe)
    //bredd(n)
    def stiftbreite(breite: Double) = englishTurtle.setPenThickness(breite)
    //sparaStil()
    def stilSpeichern() = englishTurtle.saveStyle()
    //laddaStil()
    def stilHolen() = englishTurtle.restoreStyle()
    //sparaLägeRiktning()
    def lageSpeichern() = englishTurtle.savePosHe()
    //laddaLägeRiktning()
    def lageHolen() = englishTurtle.restorePosHe()
    //siktePå()
    def kreuzAn() = englishTurtle.beamsOn()
    //sikteAv()
    def kreuzAus() = englishTurtle.beamsOff()
    //kostym(filNamn)
    def kostüm(dateiname: String) = englishTurtle.setCostume(dateiname)
    //kostymer(filNamn)
    def kostüme(dateinamen: String*) = englishTurtle.setCostumes(dateinamen: _*)
    //nästaKostym()
    def kostümWechseln() = englishTurtle.nextCostume()
  }
  //Padda
  class Kröte(override val englishTurtle: Turtle) extends GermanTurtle {
    def this(startX: Double, startY: Double, kostymFilNamn: String) = this(builtins.TSCanvas.newTurtle(startX, startY, kostymFilNamn))
    def this(startX: Double, startY: Double) = this(startX, startY, "/images/turtle32.png")
    def this() = this(0,0)
  }
  //Padda0
  class Kröte0(t0: => Turtle) extends GermanTurtle { //by-name construction as turtle0 is volatile }
    override def englishTurtle: Turtle = t0
  }
  //padda
  object kröte extends Kröte0(builtins.TSCanvas.turtle0)
  //sudda()
  def leeren() = builtins.TSCanvas.clear()
  //suddaUtdata()
  def ausgabeLeeren() = builtins.clearOutput()
  //blå
  lazy val blau = builtins.blue
  //röd
  lazy val rot = builtins.red
  //gul
  lazy val gelb = builtins.yellow
  //grön
  lazy val grün = builtins.green 
  lazy val lila = builtins.purple
  lazy val rosa = builtins.pink
  //brun
  lazy val braun = builtins.brown 
  //svart
  lazy val schwarz = builtins.black
  //vit
  lazy val weiß = builtins.white
  //genomskinlig
  lazy val durchsichtig = builtins.noColor
  //bakgrund(färg)
  def grundfarbe(farbe: Color) = builtins.setBackground(farbe)
  //bakgrund2(färg1, färg2)
  def grundfarbeUO(farbeUnten: Color, farbeOben: Color) = builtins.TSCanvas.setBackgroundV(farbeUnten, farbeOben)
  def grundfarbeLR(farbeLinks: Color, farbeRechts: Color) = builtins.TSCanvas.setBackgroundV(farbeLinks, farbeRechts)
  object KcGer { //Key codes for German keys as in Unicode
    lazy val VK_Ä = 196
    lazy val VK_Ö = 214
    lazy val VK_Ü = 220
    lazy val VK_ß = 223
  }
  
  //Loops in German:
  //upprepa(n)
  def mehrmals(n: Int)(block: => Unit) {
    //for (i <- 1 to n) block
    RepeatCommands.repeatFor(1 to n){i => block}
  }
  //I mean too limited by fixing the start value. Christoph:
  //def räkneslinga(n: Int)(block: Int => Unit) {
  //  for (i <- 1 to n) block(i)
  //}
  def fürBereich(start: Int, ende: Int)(verarbeiten: Int => Unit){
    //(start to ende) foreach verarbeiten
    RepeatCommands.repeatFor(start to ende){verarbeiten}
  }
  //The new Kojo loops from RepeatCommands regularly call the Throttler to enable interruption of busy loops.
  //New in Kojo with name repeatFor at 2015-02-23, thus not yet in swedish API:
  //Allows:
  //fürAlle(1 to 5){}
  //fürAlle(0 until 5)[}
  //fürAlle(Seq(red, green, blue)){}
  def fürAlle[T](elemente: Iterable[T])(verarbeiten: T => Unit){
    //elemente foreach verarbeiten
    RepeatCommands.repeatFor(elemente){verarbeiten}
  }
  //sålänge(villkor: => Boolean)(block: => Unit)
  def solange(bedingung: => Boolean)(block: => Unit) {
    //while (bedingung) block
    RepeatCommands.repeatWhile(bedingung){block}
  }
  
  //simple IO
  //indata(ledtext)
  def einlesen(aufforderung: String): String =  builtins.readln(aufforderung)
  
  //Not in swedish API:
  //def ausgeben(daten: Any) = print(daten)
  //utdata(data)
  def ausgeben(daten: Any) = println(daten) //Transferred here from sv.tw.kojo. Seems to work in german API.
  def ausgeben() = println()
  
  //math functions
  //avrunda(tal, antalDecimaler)
  def aufrunden(zahl: Number, nachkommastellen: Int = 0): Double = {
    val faktor = math.pow(10, nachkommastellen).toDouble
    math.round(zahl.doubleValue * faktor).toLong / faktor
  }
  //slumptal(n)
  def zufall(n: Int) = builtins.random(n)
  def zufallBruch(n: Int) = builtins.randomDouble(n)
  
  //some type aliases in Swedish
  //Heltal
  type Ganzzahl = Int
  //Decimaltal
  type Bruchzahl = Double
  //Sträng
  type Text = String
  
  //For speed test:
  //systemtid
  def systemzeit = BigDecimal(System.nanoTime) / BigDecimal("1000000000") //sekunder
  
  //räknaTill
  def zählzeitStoppen(n: BigInt) {
    var c: BigInt = 1
    print("*** Zählen von 1 bis ... ")
    val startZeit = systemzeit
    while (c < n) { c = c + 1 } //braucht Zeit, wenn n groß ist
    val stoppZeit = systemzeit
    println(s"$n *** FERTIG!")
    val dauer = stoppZeit - startZeit
    print("Das dauerte ")
    if (dauer < 0.1)
      println((dauer * 1000).round(new java.math.MathContext(2)) +
        " Millisekunden.")
    else println((dauer * 10).toLong / 10.0 + " Sekunden.")
  }
  
}

object DeInit {
  def init(builtins: CoreBuiltins) {
    //initialize unstable value
    GermanAPI.builtins = builtins
    builtins match {
      case b: Builtins =>
        println("Willkommen bei Kojo mit deutscher Schildkröte!")
        if (b.isScratchPad) {
          println("Die Vergangenheit wird nicht gespeichert, wenn Sie dieses Kojo-Probierfenster schließen.")
        }
        b.setEditorTabSize(2)

        //code completion
        b.addCodeTemplates(
          "de",
          codeTemplates
        )
        //help texts
        b.addHelpContent(
          "de",
          helpContent
        )

      case _ =>
    }
  }
  
  val codeTemplates = Map(
    "vor" -> "vor(${schritte})",
    "rechts" -> "rechts(${grad})",
    "links" -> "links(${grad})",
    "springen" -> "springen(${x},${y})",
    "gehen" -> "gehen(${x},${y})",
    "springen" -> "springen(${schritte})",
    "fürBereich" -> "fürBereich(${start}, ${ende}){ i => \n    \n}",
    //TODO from here still swedish
    "hem" -> "hem()",
    "mot" -> "mot(${x},${y})",
    "sättVinkel" -> "sättVinkel(${vinkel})",
    "öster" -> "öster()",
    "väster" -> "väster()",
    "norr" -> "norr()",
    "söder" -> "söder()",
    "sakta" -> "sakta(${fördröjning})",
    "skriv" -> "skriv(${sträng})",
    "textstorlek" -> "textstorlek(${storlek})",
    "båge" -> "båge(${radie},${vinkel})",
    "cirkel" -> "cirkel(${radie})",
    "synlig" -> "synlig()",
    "osynlig" -> "osynlig()",
    "läge" -> "läge",
    "pennaNer" -> "pennaNer()",
    "pennaUpp" -> "pennaUpp()",
    "pennanÄrNere" -> "pennanÄrNere",
    "färg" -> "färg(${pennfärg})",
    "fyll" -> "fyll(${fyllfärg})",
    "bredd" -> "bredd(${pennbredd})",
    "sparaStil" -> "sparaStil()",
    "laddaStil" -> "laddaStil()",
    "sparaLägeRiktning" -> "sparaLägeRiktning()",
    "laddaLägeRiktning" -> "laddaLägeRiktning()",
    "siktePå" -> "siktePå()",
    "sikteAv" -> "sikteAv()",
    "engelska" -> "engelska",
    "sudda" -> "sudda()",
    "suddaUtdata" -> "suddaUtdata()",
    "bakgrund" -> "bakgrund(${färg})",
    "bakgrund2" -> "bakgrund2(${färg1},${färg2})",
    "upprepa" -> "upprepa (${antal}) {\n    \n}",
    "räkneslinga" -> "räkneslinga (${antal}) { i => \n    \n}",
    "sålänge" -> "sålänge (${villkor}) {\n    \n}",
    "utdata" -> "utdata(${sträng})",
    "indata" -> "indata(${ledtext})",
    "avrudna" -> "avrudna(${tal},${antalDecimaler})",
    "slumptal" -> "slumptal(${mindreän})",
    "slumptalMedDecimaler" -> "slumptalMedDecimaler(${mindreän})",
    "räknaTill" -> "räknaTill(${tal})",
    "systemtid" -> "systemtid",
    "kostym" -> "kostym(${filnamn})",
    "kostymer" -> "kostym(${filnamn1},${filnamn2})",
    "nästaKostym" -> "nästaKostym()"
  )

  val helpContent = Map(
    "vor" ->
      <div>
<strong>vor</strong>(schritte) - Die Schildkröte geht die angegebene Anzahl Schritte vorwärts in die Richtung, wohin ihr Kopf zeigt.
<br/>Wenn der Stift unten ist, zeichnet sie dabei.
<br/><em>Beispiel:</em> <br/><br/>
<pre>
leeren     //Der Inhalt des Zeichenbereichs wird gelöscht und die Schildkröte ist in der Mitte.    
vor(100)   //Die Schildkröte geht 100 Schritte vorwärts.
vor        //ohne Wert: Die Schildkröte geht 25 Schritte.
stiftRauf  //Die Schildkröte hebt den Stift an.
vor(200)   //Die Schildkröte geht 200 Schritte ohne zu zeichnen.
rechts(45) //Die Schildkröte dreht sich um 45 Grad nach rechts.
</pre>
      </div>.toString,
    "fürBereich" -> <div><strong>fürBereich</strong>(start, ende) {{ i => anweisungen }} - Zählschleife, die alle Ganzzahlen von <em>start</em> bis <em>ende</em> durchläuft und für jede Zahl die <em>anweisungen</em> ausführt. Den Anweisungen steht dabei die Zählvariable <strong>i</strong> zur Verfügung.
        <br/><em>Beispiel:</em> <br/><br/>
        <pre>
fürBereich(1,10) {{ i =>
    ausgeben(i)
}}
        </pre>
      </div>.toString,
    //TODO from here still swedish
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