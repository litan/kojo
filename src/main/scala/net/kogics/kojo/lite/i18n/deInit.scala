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
    def langsam(verzögerung: Long) = englishTurtle.setAnimationDelay(verzögerung)
    //skriv(t)
    def schreiben(text: Any) = englishTurtle.write(text)
    //textstorlek(s)
    def schriftgröße(größe: Int) = englishTurtle.setPenFontSize(größe)
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
    def stiftfarbe(farbe: java.awt.Color) = englishTurtle.setPenColor(farbe)
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
    def this(startX: Double, startY: Double, kostümDateiname: String) = this(builtins.TSCanvas.newTurtle(startX, startY, kostümDateiname))
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
  //Not in swedish API
  lazy val grau = builtins.gray
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
  def mehrmals(anzahl: Int)(block: => Unit) {
    RepeatCommands.repeatFor(1 to anzahl){i => block}
  }
  //I mean too limited by fixing the start value. Christoph:
  //def räkneslinga(n: Int)(block: Int => Unit) {
  //  for (i <- 1 to n) block(i)
  //}
  def fürBereich(start: Int, ende: Int)(verarbeiten: Int => Unit){
    RepeatCommands.repeatFor(start to ende){verarbeiten}
  }
  //The new Kojo loops from RepeatCommands regularly call the Throttler to enable interruption of busy loops.
  //New in Kojo with name repeatFor at 2015-02-23, thus not yet in swedish API:
  //Allows:
  //fürAlle(1 to 5){}
  //fürAlle(0 until 5)[}
  //fürAlle(Seq(red, green, blue)){}
  def fürAlle[T](elemente: Iterable[T])(verarbeiten: T => Unit){
    RepeatCommands.repeatFor(elemente){verarbeiten}
  }
  //sålänge(villkor: => Boolean)(block: => Unit)
  def solange(bedingung: => Boolean)(block: => Unit) {
    RepeatCommands.repeatWhile(bedingung){block}
  }
  
  //simple IO
  //indata(ledtext)
  def einlesen(aufforderung: String): String =  builtins.readln(aufforderung)
  
  //utdata(data)
  def ausgeben(daten: Any) = println(daten) //Transferred here from sv.tw.kojo. Seems to work in german API.
  def ausgeben() = println()
  
  //math functions
  //avrunda(tal, antalDecimaler)
  def runden(zahl: Number, nachkommastellen: Int = 0): Double = {
    val faktor = math.pow(10, nachkommastellen).toDouble
    math.round(zahl.doubleValue * faktor).toLong / faktor
  }
  //slumptal(n)
  def zufall(wenigerAls: Int) = builtins.random(wenigerAls)
  def zufallBruch(wenigerAls: Int) = builtins.randomDouble(wenigerAls)
  
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
  def zählzeitStoppen(bisZahl: BigInt) {
    var c: BigInt = 1
    print("*** Zählen von 1 bis ... ")
    val startZeit = systemzeit
    while (c < bisZahl) { c = c + 1 } //braucht Zeit, wenn bisZahl groß ist
    val stoppZeit = systemzeit
    println(s"$bisZahl *** FERTIG!")
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
  
  val codeTemplates = Map[String,String](
    "englishTurtle" -> "englishTurtle",
    "vor" -> "vor(${schritte})",
    "rück" -> "rück(${schritte})",
    "rechts" -> "rechts(${grad})",
    "links" -> "links(${grad})",
    "springen" -> "springen(${schritte})",
    "springen" -> "springen(${x},${y})",
    "gehen" -> "gehen(${x},${y})",
    "heim" -> "heim()",    
    "schauen" -> "schauen(${x},${y})",
    "winkel" -> "winkel(${grad})",
    "ost" -> "ost()",    
    "west" -> "west()",    
    "nord" -> "nord()",    
    "söder" -> "süd()",
    "langsam" -> "langsam(${verzögerung})",    
    "schreiben" -> "schreiben(${text})",
    "schriftgröße" -> "schriftgröße(${größe})",
    "bogen" -> "bogen(${radius},${winkel})",
    "kreis" -> "kreis(${radius})",
    "sichtbar" -> "sichtbar()",
    "unsichtbar" -> "unsichtbar()",
    "ort" -> "ort",
    "stiftRunter" -> "stiftRunter()",
    "stiftRauf" -> "stiftRauf()",
    "stiftUnten" -> "stiftUnten",
    "stiftOben" -> "stiftOben",
    "stiftfarbe" -> "stiftfarbe(${farbe})",
    "füllfarbe" -> "füllfarbe(${füllfarbe})",
    "stiftbreite" -> "stiftbreite(${breite})",
    "stilSpeichern" -> "stilSpeichern()",
    "stilHolen" -> "stilHolen()",  
    "lageSpeichern" -> "lageSpeichern()",
    "lageHolen" -> "lageHolen()",
    "kreuzAn" -> "kreuzAn()",
    "kreuzAus" -> "kreuzAus()",
    "leeren" -> "leeren()",
    "ausgabeLeeren" -> "ausgabeLeeren()",
    "grundfarbe" -> "grundfarbe(${farbe})",
    "grundfarbeUO" -> "grundfarbeUO(${farbeUnten},${farbeOben})",
    "grundfarbeLR" -> "grundfarbeLR(${farbeLinks},${farbeRechts})",
    "mehrmals" -> "mehrmals(${anzahl}){\n    \n}",
    "fürBereich" -> "fürBereich (${start},${ende}) { i => \n    \n}",
    "solange" -> "solange(${bedingung}){\n    \n}",
    "ausgeben" -> "ausgeben(${daten})",
    "einlesen" -> "einlesen(${aufforderung})",
    "runden" -> "runden(${zahl},${nachkommastellen})",
    "zufall" -> "zufall(${wenigerAls})",
    "zufallBruch" -> "zufallBruch(${wenigerAls})",
    "zählzeitStoppen" -> "zählzeitStoppen(${bisZahl})",
    "systemzeit" -> "systemzeit",
    "kostüm" -> "kostüm(${dateiname})",
    "kostüme" -> "kostüme(${dateiname1},${dateiname2})",
    "kostümWechseln" -> "kostümWechseln()"
  )

  val helpContent = Map[String,String](
    "englishTurtle" -> 
      <div>
        <strong>englishTurtle</strong><br/><br/>
        Liefert die englische Schildkröte als Objekt des Typs <tt>Turtle</tt>.
        <br/>Darüber kann man sichergehen, dass man nur die englischen Befehle verwendet.
        <br/><em>Beispiel:</em> <br/>
        <pre>
val turtle = englishTurtle
repeat(4){{
  turtle.forward(100)
  turtle.right()
}}
        </pre>Dieses Beispiel zeichnet ein Quadrat mit der englischen Schildkröte.
      </div>.toString,
    "vor" ->
      <div>
        <strong>vor</strong>(schritte)<br/><br/>
        Die Schildkröte geht die angegebene Anzahl Schritte vorwärts in die Richtung, wohin ihr Kopf zeigt.
        <br/>Wenn der Stift unten ist, zeichnet sie dabei.
        <br/><em>Beispiel:</em> <br/>
        <pre>
leeren     //Der Inhalt des Zeichenbereichs wird gelöscht und die Schildkröte ist in der Mitte.    
vor(100)   //Die Schildkröte geht 100 Schritte vorwärts.
vor        //ohne Wert: Die Schildkröte geht 25 Schritte.
stiftRauf  //Die Schildkröte hebt den Stift an.
vor(200)   //Die Schildkröte geht 200 Schritte ohne zu zeichnen.
rechts(45) //Die Schildkröte dreht sich um 45 Grad nach rechts.
        </pre>
      </div>.toString,
    "rück" -> 
    <div>
      <strong>rück</strong>(schritte)<br/><br/>
      Bewegt die Schildkröte die angegebene Anzahl von Schritten rückwärts. Ihre Kopfrichtung (<tt>winkel</tt>) wird dabei nicht geändert.<br/>
      <br/>
      <em>Beispiel:</em> <br/><br/>
      <pre>
leeren()
//100 Schritt rot nach Norden
vor(100) 
stiftfarbe(grün)
//50 Schritte rückwärts grün übermalen
rück(50) 
stiftfarbe(blau)
//100 Schritte vorwärts blau übermalen bzw. zeichnen
vor(100) 
      </pre>
    </div>.toString,
    "links" -> 
      <div>
        <strong>links</strong>(grad)<br/><br/>
        Dreht die Schildkröte um die angegebene Gradzahl nach links. Ohne (grad) wird sie um 90 Grad gedreht.<br/>
        <em>Beispiel:</em> <br/>
        <pre>
links(45)  //Dreht die Schildkröte um 45 Grad nach links.     
links()  //Dreht die Schildkröte um 90 Grad nach links.
        </pre>
      </div>.toString,
    "rechts" -> 
      <div>
        <strong>rechts</strong>(grad)<br/><br/>
        Dreht die Schildkröte um die angegebene Gradzahl nach rechts. Ohne (grad) wird sie um 90 Grad gedreht.<br/>
        <em>Beispiel:</em> <br/>
        <pre>
rechts(45)  //Dreht die Schildkröte um 45 Grad nach rechts.     
rechts()  //Dreht die Schildkröte um 90 Grad nach rechts.
        </pre>
      </div>.toString,
    "springen" -> 
      <div>
        <strong>springen</strong>(x, y)<br/>Die Schildkröte springt zum Ort (x,y) ohne zu zeichnen und ohne ihren Winkel (Kopfrichtung) zu ändern.<br/><br/>
        <strong>springen</strong>(schritte)<br/>Die Schildkröte springt in Kopfrichtung die angegebene Anzahl Schritte ohne zu zeichnen.<br/><br/>
        <strong>springen</strong>()<br/>Die Schildkröte springt in Kopfrichtung 25 Schritte ohne zu zeichnen.
      </div>.toString,
    "gehen" -> 
      <div>
        <strong>gehen</strong>(x, y)<br/><br/>
        Die Schildkröte dreht sich zum Ort (x,y) und geht dorthin.<br/>
        Wenn der Stift unten ist, zeichnet sie dabei eine gerade Linie.
      </div>.toString,
    "heim" -> 
      <div>
        <strong>heim</strong>()<br/><br/>
        Moves the turtle to its original location, and makes it point north. <br/>
        <br/>
        <em>Beispiel:</em> <br/><br/>
        <pre>
leeren()
//Bewegt die Schildkröte weg vom Heim-Ort (0,0).
mehrmals(2){{
vor(100)
rechts()
}}
//Bewegt die Schildkröte vom Ort (100,100) zurück zu (0,0) und dreht sie nach Norden.
heim()
        </pre>
      </div>.toString,
    "schauen" -> <div><strong>schauen</strong>(x, y)<br/><br/>Dreht die Schildkröte so, dass sie zum Ort (x,y) schaut.</div>.toString,
    "winkel" -> <div><strong>winkel</strong>(grad)<br/><br/>Dreht die Schildkröte so, dass ihre Kopfrichtung den angegebenen Winkel zur x-Achse hat. 90=Nord, 180=West.</div>.toString,
    "ost" -> <div><strong>ost</strong>()<br/><br/>Dreht die Schildkröte in Richtung Osten (rechte Seite).</div>.toString,
    "west" -> <div><strong>west</strong>()<br/><br/>Dreht die Schildkröte in Richtung Westen (linke Seite).</div>.toString,
    "nord" -> <div><strong>nord</strong>()<br/><br/>Dreht die Schildkröte in Richtung Norden (oben).</div>.toString,
    "süd" -> <div><strong>süd</strong>()<br/><br/>Dreht die Schildkröte in Richtung Süden (unten).</div>.toString,
    "langsam" -> 
      <div><strong>langsam</strong>(verzögerung)<br/><br/>
        Legt die Langsamkeit der Schildkröte fest. Die angegebene <tt>verzögerung</tt>  
        ist die Dauer (in Millisekunden), die die Schildkröte braucht, um hundert Schritte zurückzulegen.<br/>
        Die Standardverzögerung is 1000 Millisekunden (gleich 1 Sekunde).<br/>
        <br/>
        <em>Beispiele:</em> <br/>
        <br/>
        <pre>
// Standardverzögerung
// Die Linie zu zeichnen benötigt 1 Sekunde.
vor(100)
                    
setAnimationDelay(500)
// Die Linie zu zeichnen benötigt 1/2 Sekunde.
forward(100)
                    
setAnimationDelay(100)
// Die Linie zu zeichnen benötigt 1/10 Sekunde.
forward(100)
        </pre>
      </div>.toString,
    "schreiben" -> 
      <div>
        <strong>schreiben</strong>(text)<br/><br/>
        Die Schildkröte schreibt rechts von sich den <tt>text</tt> in den Zeichenbereich. <br/>
        Ein Text muss in Anführungszeichen eingeschlossen werden. Beispiel: schreiben("Hallo")
      </div>.toString,
    "schriftgröße" -> <div><strong>schriftgröße</strong>(größe)<br/>Legt die Schriftgröße fürs Textschreiben fest.</div>.toString,
    "bogen" -> 
      <div>
        <strong>bogen</strong>(radius, winkel)<br/><br/>
        Zeichnet einen Kreisbogen mit dem <tt>radius</tt> und <tt>winkel</tt>. Positive Winkel bewegen die Schildkröte gegen, negative mit dem Uhrzeigersinn.
      </div>.toString,
    "kreis" -> <div><strong>kreis</strong>(radius)<br/><br/>Zeichnet einen kompletten Kreis mit dem <tt>radius</tt> gegen den Uhrzeigersinn.</div>.toString,
    "sichtbar" -> <div><strong>sichtbar</strong>()<br/><br/>Macht die Schildkröte sichtbar.</div>.toString,
    "unsichtbar" -> 
      <div>
        <strong>unsichtbar</strong>()<br/><br/>
        Macht die Schildkröte unsichtbar. Sie kann wieder sichtbar gemacht werden mit dem Befehl <tt>sichtbar()</tt>.
      </div>.toString,
    "ort" -> 
      <div>
        <strong>ort</strong><br/><br/>
        Liefert den aktuellen Ort der Schildkröte als Punktwert Point(x,y).
        <br/><em>Beispiel:</em> <br/>
        <pre>
ausgeben(ort)     //Gibt den Ort der Schildkröte aus.
ausgeben(ort.x)   //Gibt die x-Koordinate der Schildkröte aus.
ausgeben(ort.y)   //Gibt die y-Koordinate der Schildkröte aus.

var x = ort.x    //Speichert die x-Koordinate der Schildkröte in der Variablen x.
var y = ort.y    //Speichert die y-Koordinate der Schildkröte in der Variablen y.
x = x - 100      //Zieht 100 von der Variablen x ab.
hoppaTill(x, y)  //Springt zum neuen Ort mit der geänderten x-Koordinate. 
        </pre>
      </div>.toString,
    "stiftRunter" -> <div><strong>stiftRunter</strong>()<br/>Senkt den Stift der Schildkröte, so dass sie bei folgenden Bewegungen zeichnet.</div>.toString,
    "stiftRauf" -> <div><strong>stiftRauf</strong>()<br/>Hebt den Stift der Schildkröte an, so dass sie bei folgenden Bewegungen nicht zeichnet.</div>.toString,
    "stiftUnten" -> 
      <div>
        <strong>stiftUnten</strong><br/><br/>
        Zeigt an, ob der Stift der Schildkröte unten ist. Liefert <tt>true</tt>, wenn der Stift unten ist bzw. <tt>false</tt>, wenn er oben ist.
      </div>.toString,
    "stiftOben" -> 
      <div>
        <strong>stiftOben</strong><br/><br/>
        Zeigt an, ob der Stift der Schildkröte oben ist. Liefert <tt>true</tt>, wenn der Stift oben ist bzw. <tt>false</tt>, wenn er unten ist.
      </div>.toString,
    "stiftfarbe" -> 
      <div>
        <strong>stiftfarbe</strong>(farbe)<br/><br/>
        Stellt die Farbe des Schildkrötenstifts auf die angegebene <tt>farbe</tt> ein.
        <br/>Du kannst diese vorgemischten Farben verwenden:<br/>blau, rot, gelb, grün, lila, rosa, braun, schwarz, grau, weiß, durchsichtig.
        <br/>Du kannst eigene Farben mit <strong>Color</strong> mischen. 
        <br/><em>Beispiel:</em> <br/>
        <pre>
leeren
stiftfarbe(blau)           //Setzt die Stiftfarbe auf Blau.
vor()                      //Die Schildkröte zeichnet blau.
stiftfarbe(Color(217, 125, 245, 250)) //mische eine neue Farbe (helllila) 
//Rotwert=220, Grünwert=125, Blauwert=245, Deckungskraft=250
//Farbwerte von Color können von 0 bis 255 sein.
vor(200)
         </pre>
       </div>.toString,
    "füllfarbe" -> 
       <div>
         <strong>füllfarbe</strong>(farbe)<br/>Legt die Füllfarbe für die ab jetzt gezeichnete Figur fest auf <tt>farbe</tt>.
         <br/>Du kannst diese vorgemischten Farben verwenden:<br/>blau, rot, gelb, grün, lila, rosa, braun, schwarz, grau, weiß, durchsichtig.
         <br/>Du kannst eigene Farben mit <tt>Color</tt> mischen. Siehe bei <tt>stiftfarbe</tt>.
         <br/><em>Beispiel:</em><br/>
         <pre>
leeren()
langsam(0)
füllfarbe(blau)          //Setzt die Füllfarbe auf Blau.
kreis(100)        //Die Schidkröte zeichnet einen roten, blau gefüllten Kreis mit Radius 100.
springen(100)         //Die Schildkröte springt 100 Schritte nach vorne.
füllfarbe(durchsichtig) //Setzt die Füllfabre auf durchsichtig.
kreis(100)        //Die Schidkröte zeichnet einen roten Kreis, dessen Fläche durchsichtig ist.
        </pre>
      </div>.toString,
    "stiftbreite" -> 
      <div><tt>stiftbreite</tt>(breite)<br/>
        Legt die Breite des Stifts fest, mit dem die Schildkröte zeichnet.<br/>
        <br/>
        <em>Beispiel:</em> <br/>
        <pre>
leeren()
stiftbreite(10)
// Zeichne eine Linie, die 10 Einheiten breit ist:
vor(100)
                    
stiftbreite(15)
// Zeichne eine Linie, die 15 Einheiten breit ist:
vor(100)
        </pre>
      </div>.toString
    ,
    "stilSpeichern" -> 
      <div>
        <strong>stilSpeichern</strong>()<br/>
        Speichert den jetzigen Zeichenstil der Schildkröte, sodass er später mit <tt>stilHolen()</tt> einfach wiederhergestellt werden kann.<br/>
        <p>
          Der Zeichenstil umfasst:
          <ul>
            <li>Stiftfarbe</li>
            <li>Stiftbreite</li>
            <li>Füllfarbe</li>
            <li>Stift-Schriftart (Font)</li>
            <li>Stiftzustand (oben/unten)</li>
          </ul>
        </p>
        <br/>
        <em>Beispiel:</em> <br/>
        <br/>
        <pre>
def querstrich(n: Int) {{
    // Speichere jetzige Lage und Stiftstil:
    stilSpeichern()
    lageSpeichern()
    stiftfarbe(grau)
    rechts()
    vor(n)
    rück(n * 2)
    // Gespeicherte Lage und Stiftstil wieder holen:
    stilHolen()
    lageHolen()
}}

leeren()
stiftfarbe(grün)
rechts()
// grüne Linie:
vor(100)
// Grauer Querstrich:
querstrich(10)
// grüne Linie:
vor(100)
        </pre>
      </div>.toString,
    "stilHolen" -> 
      <div>
        <strong>stilHolen</strong>()<br/><br/>
        Stellt den Zeichenstil wieder her, der mit dem letzten Aufruf von <tt>stilSpeichern()</tt> gespeichert wurde.
        <br/>
        <p>
          Der Zeichenstil umfasst:
          <ul>
            <li>Stiftfarbe</li>
            <li>Stiftbreite</li>
            <li>Füllfarbe</li>
            <li>Stift-Schriftart (Font)</li>
            <li>Stiftzustand (oben/unten)</li>
          </ul>
        </p>
        <br/>
        <em>Beispiel:</em> <br/>
        <br/>
        <pre>
def querstrich(n: Int) {{
    // Speichere jetzige Lage und Stiftstil:
    stilSpeichern()
    lageSpeichern()
    stiftfarbe(grau)
    rechts()
    vor(n)
    rück(n * 2)
    // Gespeicherte Lage und Stiftstil wieder holen:
    stilHolen()
    lageHolen()
}}

leeren()
stiftfarbe(grün)
rechts()
// grüne Linie:
vor(100)
// Grauer Querstrich:
querstrich(10)
// grüne Linie:
vor(100)
        </pre>
      </div>.toString,
    "lageSpeichern" -> 
      <div>
        <strong>lageSpeichern</strong>()<br/><br/>
        Speichert, wo und wie die Schildkröte zur Zeit liegt (Ort und Winkel), 
        sodass diese Einstellungen später einfach mit einem Aufruf <tt>lageHolen()</tt> wiederhergestellt werden können.<br/>
        <br/>
        <em>Beispiel:</em> <br/>
        <br/>
        <pre>
leeren()
// Speichert, wo und wie die Schildkröte zur Zeit liegt (Ort und Winkel):
lageSpeichern()
            
// Bewege Dich irgendwohin:
vor(100)
rechts(45)
vor(60)
            
// Nun hole die gespeicherte Lage,
// sodass die Schildkröte dort wieder steht
// und dieselbe Kopfrichtung hat,
// wo und wie sie gestartet ist.
lageHolen()
        </pre>
      </div>.toString,
    "lageHolen" -> 
      <div>
        <strong>lageHolen</strong>()<br/><br/>
        Stellt die Lage (Ort und Winkel) der Schildkröte wieder her, die mit dem letzten Aufruf von <tt>lageSpeichern()</tt> gespeichert wurde.
        <br/>
        <em>Beispiel:</em> <br/>
        <br/>
        <pre>
leeren()
// Speichert, wo und wie die Schildkröte zur Zeit liegt (Ort und Winkel):
lageSpeichern()
            
// Bewege Dich irgendwohin:
vor(100)
rechts(45)
vor(60)
            
// Nun hole die gespeicherte Lage,
// sodass die Schildkröte dort wieder steht
// und dieselbe Kopfrichtung hat,
// wo und wie sie gestartet ist.
lageHolen()
        </pre>
      </div>.toString,
    "kreuzAn" -> <div><strong>kreuzAn</strong>()<br/><br/>Zeigt die exakte Richtung der Schildkröte durch ein Kreuz an.</div>.toString,
    "kreuzAus" -> <div><strong>kreuzAus</strong>()<br/><br/>Schaltet das Richtungskreuz der Schildkröte aus.</div>.toString,
    "leeren" -> 
      <div>
        <strong>leeren</strong>()<br/><br/>Löscht alles im Zeichenbereich und setzt die Schildkröte in Heim-Lage (x=0, y=0 und winkel=90).
        Wenn man diese Methode bei einer speziellen Schildkröte anwendet wie <tt>meineKröte.leeren()</tt>, 
        wird nur das von dieser Schildkröte Gezeichnete gelöscht.
      </div>.toString,
    "ausgabeLeeren" -> <div><strong>ausgabeLeeren</strong>()<br/><br/>Löscht den ganzen Inhalt des Ausgabebereichs.</div>.toString,
    "grundfarbe" -> 
      <div><strong>grundfarbe</strong>(farbe)<br/><br/>Legt die Hintergrundfarbe für den Zeichenbereich fest auf <tt>farbe</tt>.
        <br/>Du kannst diese vorgemischten Farben verwenden:<br/>blau, rot, gelb, grün, lila, rosa, braun, schwarz, grau, weiß, durchsichtig.
        <br/>Du kannst eigene Farben mit <tt>Color</tt> mischen. Siehe bei <tt>stiftfarbe</tt>.
      </div>.toString,
    "grundfarbeUO" -> <div><strong>grundfarbeUO</strong>(farbeUnten, farbeOben)<br/><br/>
        Legt einen Hintergrund-Farbverlauf fest mit <tt>farbeUnten</tt> unten und <tt>farbeOben</tt> oben.
        <br/>Du kannst diese vorgemischten Farben verwenden:<br/>blau, rot, gelb, grün, lila, rosa, braun, schwarz, grau, weiß, durchsichtig.
        <br/>Du kannst eigene Farben mit <tt>Color</tt> mischen. Siehe bei <tt>stiftfarbe</tt>.
      </div>.toString,
    "grundfarbeLR" -> 
      <div><strong>grundfarbeLR</strong>(farbeLinks, farbeRechts)<br/><br/>
        Legt einen Hintergrund-Farbverlauf fest mit <tt>farbeLinks</tt> links und <tt>farbeRechts</tt> rechts.
        <br/>Du kannst diese vorgemischten Farben verwenden:<br/>blau, rot, gelb, grün, lila, rosa, braun, schwarz, grau, weiß, durchsichtig.
        <br/>Du kannst eigene Farben mit <tt>Color</tt> mischen. Siehe bei <tt>stiftfarbe</tt>.
      </div>.toString,
    "mehrmals" -> 
      <div>
        <strong>mehrmals</strong>(anzahl) {{ anweisungen }}<br/><br/>
        Schleife: Wiederholt den Block von {{<em>anweisungen</em>}} in geschweiften Klammern. 
        Er wird so oft ausgeführt, wie durch <tt>anzahl</tt> angegeben ist.
        <br/><em>Beispiel:</em> (zeichnet ein Quadrat)<br/><br/>
        <pre>
mehrmals(4) {{ 
  vor
  rechts
}}
        </pre>
      </div>.toString,
    "fürBereich" -> 
      <div>
        <strong>fürBereich</strong>(start,ende) {{ i => anweisungen }}<br/><br/>
        Schleife: Wiederholt den Block von {{<em>anweisungen</em>}} in geschweiften Klammern.
        Er wird für jede Ganzzahl im Bereich <tt>start</tt> bis <tt>ende</tt> einschließlich je einmal ausgeführt.
        Die jeweilige Zahl ist unter dem Namen <tt>i</tt> (frei wählbar) ansprechbar.
        <br/><em>Beispiel:</em> (gibt die ersten 10 Quadratzahlen aus)<br/><br/>
        <pre>
fürBereich(1,10){{
  i =>
  val q = i*i
  ausgeben(s"quadrat($i) = $q")
}}
        </pre>
      </div>.toString,
    "fürAlle" -> 
    <div>
      <strong>fürAlle</strong>(folge){{ e => anweisungen}}<br/><br/>
      Schleife: Wiederholt den Block von {{<em>anweisungen</em>}} in geschweiften Klammern.
      Er wird für jedes Element der angegebenen <tt>folge</tt> einmal ausgeführt.
      Das jeweilige Element ist unter dem Namen <tt>e</tt> (frei wählbar) ansprechbar.
      <br/>
      <em>Beispiel:</em> <br/><br/>
      <pre>
leeren()
fürAlle (40 to 60 by 5) {{ e =>
  vor(e)
  rechts()
}}
      </pre>
      <em>Beispiel 2:</em> <br/>
      <br/>
      <pre>
leeren
fürAlle (Seq(blau, grün, gelb)) {{ e =>
  stiftfarbe(e)
  vor(100)
  rechts
}}
      </pre>
    </div>.toString,
    "solange" -> <div><strong>solange</strong>(bedingung) {{ anweisungen }}<br/><br/>
        Schleife: Wiederholt den Block von {{<em>anweisungen</em>}} in geschweiften Klammern. 
        Er wird solange ausgeführt, wie die angegebene <tt>bedingung</tt> wahr bleibt.
        <br/><em>Beispiel:</em> (gibt die Ganzzahlen von 0 bis 9 aus)<br/><br/>
        <pre>
var i = 0
solange(i{ "<" }10) {{ 
      ausgeben(i)
      i = i + 1
}}
        </pre>
      </div>.toString,
    "ausgeben" -> 
      <div>
        <strong>ausgeben</strong>(daten)<br/><br/>
        Gibt die <tt>daten</tt> als Text in den Ausgabebereich aus. Ein Text (Zeichenkette) muss in Anführungszeichen "..." eingeschlossen sein. 
        Wenn der übergebene Wert <tt>daten</tt> kein Text ist, wird seine Textdarstellung ausgegeben (Methode <tt>toString</tt>).
        Wenn man <tt>ausgeben()</tt> leer aufruft, wird nur ein Zeilenwechsel ausgegeben.
        <em>Beispiel:</em> <tt>ausgeben("Hallo")</tt>.
      </div>.toString,
    "einlesen" -> 
      <div>
        <strong>einlesen</strong>(aufforderung)<br/><br/>
        Zeigt die angegebene <tt>aufforderung</tt> im Ausgabebereich an und liest eine Zeile, die der Benutzer eingibt. 
        Diese muss durch die Eingabetaste beendet werden.<br/>
        <br/><em>Beispiel:</em> <br/><br/>
        <pre>
val x = einlesen("Schreibe Deinen Namen ")
ausgeben("Hallo, " + x + "!")
        </pre>
      </div>.toString,
    "runden" -> 
      <div>
        <strong>runden</strong>(zahl, nachkommastellen)<br/><br/>
        Rundet die übergebene <tt>zahl</tt> mit einer Genauigkeit der angegebenen Anzahl <tt>nachkommastellen</tt>.<br/>
        <br/><em>Beispiel:</em> <br/><br/>
        <pre>
val z1 = runden(3.981, 2) //Runden mit 2 Nachkommastellen ergibt 3.98
ausgeben(z1)
val z2 = runden(3.981) //Runden mit 0 Nachkommastellen ergibt 4.0
ausgeben(z2)
        </pre>
      </div>.toString,
    "systemzeit" -> 
      <div>
        <strong>systemzeit</strong><br/>Liefert die Systemzeit in Sekunden als Bruchzahl. 
        Du kannst die Systemzeit verwenden, um zu messen, wie lange etwas dauert.<br/>
        <br/><em>Beispiel:</em> <br/><br/>
        <pre>
val start = systemzeit
ausgeben("Geduld haben...")
var i = 0
mehrmals(100000000){{
  i += 1
}}
val stopp = systemzeit
val s = stopp - start
ausgeben(s"Du hattest $s Sekunden Geduld.")
        </pre>
      </div>.toString,
    "zählzeitStoppen" -> 
      <div>
        <strong>zählzeitStoppen</strong>(bisZahl)<br/><br/>
        Zählt von 1 bis <tt>bisZahl</tt> und stoppt die dafür benötigte Zeit.
        Damit kannst Du herausbekommen, wie schnell Dein Computer rechnet.
        Kann für große Zahlen lange dauern.<br/>
        <br/><em>Beispiel:</em> <br/><br/>
        <pre>
zählzeitStoppen(5000)
        </pre>
      </div>.toString,
    //zufall(wenigerAls: Int)
    "zufall" -> 
      <div>
        <strong>zufall</strong>(wenigerAls)<br/><br/>
        Liefert eine zufällige Ganzzahl zwischen 0 (einschließlich) und <tt>wenigerAls</tt> (ausschließlich).<br/>
        <em>Beispiel:</em><br/>
        <pre>
mehrmals(10){{
  ausgeben(zufall(20) + 1)
}}
</pre><br/>Gibt 10 zufällige Ganzzahlen im Bereich 1...20 aus.
      </div>.toString,
    "zufallBruch(wenigerAls)" -> 
      <div>
        <strong>zufallBruch</strong>(wenigerAls)<br/><br/>
        Liefert eine zufällige Bruchzahl zwischen 0 (einschließlich) und <tt>wenigerAls</tt> (ausschließlich).<br/>
        <em>Beispiel:</em><br/>
        <pre>
mehrmals(4){{
  ausgeben(zufallBruch(10))
}}</pre>
        <br/>Gibt 4 zufällige Bruchzahlen im Bereich 0.0 bis 9.999999999999999 aus, zum Beispiel 7.944168334647333 1.5762542354581088 3.078541034787967 0.7535865933723174
      </div>.toString,
    "kostüm" -> <div>
      <strong>kostüm</strong>(dateiname)<br/>Nimmt das Bild in der angegebenen Datei als Kostüm, d.h. für das Aussehen der Schildkröte.<br/>
      <em>Beispiel:</em><br/><pre>  
leeren
//Für eine blaue Schildkröte nehmen wir ein vordefiniertes Bild:
kostüm("/images/blue-turtle32.png")
vor(100) //Die blaue Kröte bewegt sich.
//neue Kröte am Ort (100, 100) erzeugen mit dem vordefinierten Bild car.png:
val auto = new Kröte(100,100,"/media/costumes/car.png")
auto.vor(100)
</pre><br/></div>.toString,
    "kostüme" -> <div>
      <strong>kostüme</strong>(dateiname1, dateiname2, ...)<br/>Nimmt die Bilder in den Dateien als mögliche Kostüme, 
      d.h. für das Aussehen der Schildkröte und legt das erste Bild als jetziges Kostüm fest. 
      Man kann die Kostüme durchlaufen mit Aufruf von <tt>kostümWechseln()</tt>.
    </div>.toString,
    "kostümWechseln" -> <div>
      <strong>kostümWechseln</strong>()<br/><br/>
        Ändert das Schildkrötenkostüm zu dem nächsten in der Reihe der Kostüme, die durch <tt>kostüme(...)</tt> festgelegt wurden.
      </div>.toString
  )
}