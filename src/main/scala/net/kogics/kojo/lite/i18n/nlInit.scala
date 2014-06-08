/*
 * Copyright (C) 2013 
 *   Lalit Pant <pant.lalit@gmail.com>,
 *   Eric Zoerner <eric.zoerner@gmail.com>
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

//Dutch Turtle wrapper for Kojo

package net.kogics.kojo.lite.i18n

import net.kogics.kojo.lite.CoreBuiltins
import net.kogics.kojo.lite.Builtins
import net.kogics.kojo.xscala.RepeatCommands

object DutchAPI {
  import net.kogics.kojo.core.Turtle
  import java.awt.Color
  var builtins: net.kogics.kojo.lite.CoreBuiltins = _  //unstable reference to module

  trait DutchTurtle {
    def engels: Turtle
    def wis() = engels.clear()
    def vertoon() = engels.visible()
    def verberg() = engels.invisible()
    def vooruit(n: Double) = engels.forward(n)
    def vooruit() = engels.forward()
    def rechts(hoek: Double) = engels.right(hoek)
    def rechts() = engels.right()
    def links(hoek: Double) = engels.left(hoek)
    def links() = engels.left()
    def springNaar(x: Double, y: Double) = engels.jumpTo(x, y)
    def gaNaar(x: Double, y: Double) = engels.moveTo(x, y)
    def spring(n: Double) = {
      engels.saveStyle() //to preserve pen state
      engels.hop(n) //hop change state to penDown after hop
      engels.restoreStyle()
    }
    def spring(): Unit = {
      engels.saveStyle() //to preserve pen state
      engels.hop() //hop change state to penDown after hop
      engels.restoreStyle()
    }
    def thuis() /* of naarHuis?*/= engels.home()
    def naartoe(x: Double, y: Double) = engels.towards(x, y)
    def zetHoek(hoek: Double) = engels.setHeading(hoek)
    def hoek = engels.heading
    def oost() = engels.setHeading(0)
    def west() = engels.setHeading(180)
    def noord() = engels.setHeading(90)
    def zuid() = engels.setHeading(-90)
    def vertraging(n: Long) = engels.setAnimationDelay(n)
    def schrijf(t: Any) = engels.write(t)
    def tekstGroote(s: Int) = engels.setPenFontSize(s)
    def boog(radius: Double, hoek: Double) = engels.arc(radius, math.round(hoek).toInt)
    def cirkel(radius: Double) = engels.circle(radius)
    def positie = engels.position
    def penOpCanvas() = engels.penDown()
    def penVanCanvas() = engels.penUp()
    def isPenOpCanvas = engels.style.down
    def penKleur(c: java.awt.Color) = engels.setPenColor(c)
    def vulkleur(c: java.awt.Color) = engels.setFillColor(c)
    def penBreedte(n: Double) = engels.setPenThickness(n)
    def bewaarStijl() = engels.saveStyle()
    def zetStijlTerug() = engels.restoreStyle()
    def bewaarPositieEnHoek() = engels.savePosHe()
    def zetPositieEnHoekTerug() = engels.restorePosHe()
    def vizierAan() = engels.beamsOn()
    def vizierUit() = engels.beamsOff()
    def kostuum(bestandNaam: String) = engels.setCostume(bestandNaam)
    def kostuums(bestandNaam: String*) = engels.setCostumes(bestandNaam: _*)
    def volgendKostuum() = engels.nextCostume()
  }
  class Schildpad(override val engels: Turtle) extends DutchTurtle {
    def this(startX: Double, startY: Double, kostuumBestandNaam: String) =
      this(builtins.TSCanvas.newTurtle(startX, startY, kostuumBestandNaam))
    def this(startX: Double, startY: Double) = this(startX, startY, "/images/turtle32.png")
    def this() = this(0,0)
  }
  class Schildpad0(t0: => Turtle) extends DutchTurtle { //by-name construction as turtle0 is volatile }
    override def engels: Turtle = t0
  }
  object schildpad extends Schildpad0(builtins.TSCanvas.turtle0)
  def wis() = builtins.TSCanvas.clear()
  def wisOutput() = builtins.clearOutput()
  lazy val blauw = builtins.blue
  lazy val rood = builtins.red
  lazy val geel = builtins.yellow
  lazy val groen = builtins.green
  lazy val paars = builtins.purple
  lazy val roze = builtins.pink
  lazy val bruin = builtins.brown
  lazy val zwart = builtins.black
  lazy val wit = builtins.white
  lazy val cyaan = builtins.cyan
  lazy val geenKleur = builtins.noColor
  def achterGrond(kleur: Color) = builtins.setBackground(kleur)
  def achterGrondV(kleur1: Color, kleur2: Color) = builtins.TSCanvas.setBackgroundV(kleur1, kleur2)

  //loops in Dutch
  def herhaal(n: Int)(bloc: => Unit): Unit = {
    RepeatCommands.repeat(n)(bloc)
  }
  def herhaalTel (n: Int)(bloc: Int => Unit): Unit = {
    RepeatCommands.repeati(n)(bloc)
  }

  def zolangAls(conditie: => Boolean)(bloc: => Unit): Unit = {
    RepeatCommands.repeatWhile(conditie)(bloc)
  }

  //simple IO
  def input(leidTekst: String = "") =  builtins.readln(leidTekst)

  //math functions
  def rondAf(getal: Number, aantalDecimalen: Int = 0): Double = {
    val factor = math.pow(10, aantalDecimalen)
    math.round(getal.doubleValue * factor) / factor
  }
  def toeval(n: Int) = builtins.random(n)
  def toevalDubbel(n: Int) = builtins.randomDouble(n)

  //some type aliases in Dutch
  type Geheel = Int
  type Dubbel = Double
  type Snaar = String

  //speedTest
  def systeemTijd() = BigDecimal(System.nanoTime()) / BigDecimal("1000000000") //seconds

  def telTot(n: BigInt): Unit = {
    var c: BigInt = 1
    print("*** Tel van 1 tot ... ")
    val startTime = systeemTijd()
    while (c < n) {
      c = c + 1
    } //takes time if n is large
    val stopTime = systeemTijd()
    println("" + n + " *** KLAAR!")
    val time = stopTime - startTime
    print("Het duurde ")
    if (time < 0.1)
      println((time * 1000).round(new java.math.MathContext(2)) +
                " millseconden.")
    else println((time * 10).toLong / 10.0 + " seconden.")
  }
}

object NlInit {
  def init(builtins: CoreBuiltins): Unit = {
    //initialize unstable value
    net.kogics.kojo.lite.i18n.DutchAPI.builtins = builtins
    builtins match {
      case b: Builtins =>
        println("Welkom in Kojo met Nederlandse schildpad!")
        if (b.isScratchPad) {
          //History for work you do in the Scratchpad will not be saved.
          println("De geschiedenis wordt niet opgeslagen bij het sluiten van Kojo kladblok.")
        }
        b.setEditorTabSize(2)

        //code completion
        b.addCodeTemplates(
          "nl",
          codeTemplates
        )
        //help texts
        b.addHelpContent(
          "nl",
          helpContent
        )

      case _ =>
    }
  }

  val codeTemplates = Map(
    "vooruit" -> "vooruit(${stap})",
    "rechts" -> "rechts(${hoek})",
    "links" -> "links(${hoek})",
    "springNaar" -> "springNaar(${x},${y})",
    "gaNaar" -> "gaNaar(${x},${y})",
    "spring" -> "spring(${stap})",
    "thuis" -> "thuis()",
    "naartoe" -> "naartoe(${x},${y})",
    "hoek" -> "hoek",
    "zetHoek" -> "zetHoek(${hoek})",
    "oost" -> "oost()",
    "west" -> "west()",
    "noord" -> "noord()",
    "zuid" -> "zuid()",
    "vertraging" -> "vertraging(${vertraging})",
    "schrijf" -> "schrijf(${snaar})",
    "tekstGroote" -> "tekstGroote(${maat})",
    "boog" -> "boog(${radius},${hoek})",
    "cirkel" -> "cirkel(${radius})",
    "vertoon" -> "vertoon()",
    "verberg" -> "verberg()",
    "positie" -> "positie",
    "penOpCanvas" -> "penOpCanvas()",
    "penVanCanvas" -> "penVanCanvas()",
    "isPenOpCanvas" -> "isPenOpCanvas",
    "kleur" -> "kleur(${penKleur})",
    "vulkleur" -> "vulkleur(${kleur})",
    "penBreedte" -> "penBreedte(${penBreedte})",
    "bewaarStijl" -> "bewaarStijl()",
    "zetStijlTerug" -> "zetStijlTerug()",
    "bewaarPositieEnHoek" -> "bewaarPositieEnHoek()",
    "zetPositieEnHoekTerug" -> "zetPositieEnHoekTerug()",
    "vizierAan" -> "vizierAan()",
    "vizierUit" -> "vizierUit()",
    "engels" -> "engels",
    "wis" -> "wis()",
    "wisOutput" -> "wisOutput()",
    "achterGrond" -> "achterGrond(${kleur})",
    "achterGrondV" -> "achterGrondV(${kleur1},${kleur2})",
    "herhaal" -> "herhaal (${aantal}) {\n    \n}",
    "herhaalTel" -> "herhaalTel (${aantal}) { i => \n    \n}",
    "zolangAls" -> "zolangAls (${conditie}) {\n    \n}",
    "output" -> "output(${snaar})",
    "input" -> "input(${leidTekst})",
    "rondAf" -> "rondAf(${getal},${aantalDecimalen})",
    "toeval" -> "toeval(${bovengrens})",
    "toevalDubbel" -> "toevalDubbel(${bovengrens})",
    "telTot" -> "telTot(${getal})",
    "systeemTijd" -> "systeemTijd",
    "kostuum" -> "kostuum(${bestandNaam})",
    "kostuums" -> "kostuums(${bestandNaam},${bestandNaam})",
    "volgendKostuum" -> "volgendKostuum()"
  )

  val helpContent = Map(
    "vooruit" ->
      <div>
        <strong>vooruit</strong>(stap) - De schildpad gaat naar voren het aantal stappen die je opgeeft in de richting die zijn neus wijst.
        <br/>Als de pen op canvas is, tekent de schildpad terwijl hij naar voren loopt.
        <br/><em>Voorbeeld:</em> <br/><br/>
        <pre>
          wis()           //tekencanvas wordt gewist en de schildpad is naar het midden verplaatst
          vooruit(100)    //de schildpad gaat rechtdoor 100 stappen
          vooruit()       //de schildpad gaat rechtdoor 25 stappen
          penVanCanvas()  //de schildpad tilt de pen van de canvas
          vooruit(200)    //de schildpad gaat 200 stappen zonder tekenen
          rechts(45)      //de schildpad roteert 45 graden naar rechts
        </pre>
      </div>.toString,
    "links" -> <div><strong>links</strong>(hoek)<br/>De schildpad roteert 90 graden naar links.</div>.toString,
    "rechts" -> <div><strong>rechts</strong>(hoek)<br/>De schildpad roteert 90 graden naar rechts.</div>.toString,
    "springNaar" -> <div><strong>springNaar</strong>(x, y)<br/>De schildpad springt naar positie (x, y) zonder tekenen, en zonder wijziging van de hoek.</div>.toString,
    "gaNaar" -> <div><strong>gaNaar</strong>(x, y)<br/>De schildpad roteert naar de positie (x,y) en daarheen loopt.<br/>Als de pen beneden zit, tekent de schildpad terwijl hij loopt.</div>.toString,
    "spring" -> <div><strong>spring</strong>(stap)<br/>De schildpad springt in de richting waarin zijn neus wijst naar het aantal stappen, zonder tekenen, zelfs als de pen op canvas zit.</div>.toString,
    "thuis" -> <div><strong>thuis</strong>()<br/>De schildpad loopt terug naar positie (0,0) en roteert zodat zijn neus naar het noorden wijst. <br/>Als de pen op canvas zit, tekent de schildpad terwijl hij loopt.</div>.toString,
    "naartoe" -> <div><strong>naartoe</strong>(x, y)<br/>De schildpad roteert zodat zijn neus naar de locatie (x, y) wijst.</div>.toString,
    "zetHoek" -> <div><strong>zetHoek</strong>(hoek)<br/>De schildpad roteert zodat zijn neus in de gespecificeerde hoek wijst.</div>.toString,
    "hoek" -> <div><strong>hoek</strong><br/>Geeft de waarde van de hoek waarin zijn neus van de schildpad wijst.</div>.toString,
    "oost" -> <div><strong>oost</strong>()<br/>De schildpad roteert zodat zijn neus naar het oosten wijst (rechts).</div>.toString,
    "west" -> <div><strong>west</strong>()<br/>De schildpad roteert zodat zijn neus naar het westen wijst (links).</div>.toString,
    "noord" -> <div><strong>noord</strong>()<br/>De schildpad roteert zodat zijn neus naar het noorden wijst (naar boven).</div>.toString,
    "zuid" -> <div><strong>zuid</strong>()<br/>De schildpad roteert zodat zijn neus naar het zuiden wijst (naar beneden).</div>.toString,
    "vertraging" -> <div><strong>vertraging</strong>(vertraging)<br/>Hoe meer de vertraging hoe langzamer de schildpad. <br/> Minimale vertraging is 0 <br/> vertraging (1000) is vrij langsaam.</div>.toString,
    "schrijf" -> <div><strong>schrijf</strong>(snaar)<br/>De schildpad schrijft een snaar in het tekencanvas bij de huidige positie.<br/>Een snaar begint en eindigt met dubbele aanhalingstekens. Voorbeeld: schrijf("hallo")</div>.toString,
    "tekstGroote" -> <div><strong>tekstGroote</strong>(maat)<br/>Hiermee wijzigt je de grootte van de tekst die de schildpad schrijft.</div>.toString,
    "boog" -> <div><strong>boog</strong>(radius, hoek)<br/>De schildpad tekent een boog met de gegeven radius en hoek.</div>.toString,
    "cirkel" -> <div><strong>cirkel</strong>(radius)<br/>De schildpad tekent een cirkel met de gegeven radius.</div>.toString,
    "vertoon" -> <div><strong>vertoon</strong>()<br/> Hiermee wordt de schildpad weer zichtbaar als hij onzichtbaar was.</div>.toString,
    "verberg" -> <div><strong>verberg</strong>()<br/>Heermee wordt de schildbaar onzichtbaar als hij zichtbaar was</div>.toString,
    "positie" -> <div><strong>positie</strong><br/>Geeft de schildpad een positie als een puntenwaarde (x, y)
      <br/><em>Voorbeeld:</em> <br/><br/>
      <pre>
        output(positie)     //schrijf de positie van de schildpad in het output venster
        output(positie.x)   //schrijf de x-positie van de shildpad in het output venster
        output(positie.y)   //schrijf de y-positie van de schildpad in het output venster

        var x = positie.x   //slaat de x-positie van de schildpad op in de variabele x
        var y = positie.y   //slaat de y-positie van de schildpad op in de variabele y
        x = x - 100         //wijzigt de variabele x naar de oude waarde min 100
        springNaar(x, y)    //springt naar een nieuwe positie met bijgewerkte x
      </pre>
    </div>.toString,
    "penOpCanvas" -> <div><strong>penOpCanvas</strong>()<br/>Zet de pen op de canvas, zodat de schildpad tekent als hij loopt.</div>.toString,
    "penVanCanvas" -> <div><strong>penVanCanvas</strong>()<br/>Tilt de pen op van de canvas, zodat de schildpad niet tekent als hij loopt.</div>.toString,
    "isPenOpCanvas" -> <div><strong>isPenOpCanvas</strong><br/>Controleert of de pen op de canvas zit. Geeft <strong>true</strong> (waar) als de pen op canvas zit en <strong>false</strong> (fout) als hij van canvas is.</div>.toString,
    "penKleur" -> <div><strong>penKleur</strong>(penKleur)<br/>Maakt de schildpad tekenen met de gegeven pen kleur.
      <br/>Je kunt deze pre-gemengde kleuren gebruiken:
      <br/>blauw, rood, geel, groen, paars, roze, bruin, zwaart, wit, geenKleur.
      <br/>Je kunt je eigen kleur mixen met Color
      <br/><em>Voorbeeld:</em> <br/><br/>
      <pre>
        kleur(blauw)                //zet de pen kleur naar blauw
        vooruit()                   //de schildpad loopt vooruit
        kleur(Color(220,30,40,250)) //mengt nieuwe kleur (licht paars)
        //roodwaarde=220, groenwaarde=30, blauwwarde=40, dekking=250
        //kleurwaarden kunnen tussen 0 en 255 variëren
        vooruit(200)
      </pre>
    </div>.toString,
    "vulkleur" -> <div><strong>vulkleur</strong>(vulkleur)<br/>Hiermee gebruikt de schildpad de gegeven vulkleur als het tekent.
      <br/>Je kunt deze pre-gemengde kleuren gebruiken:
      <br/>blauw, rood, geel, groen, paars, roze, bruin, zwaart, wit, geenKleur.
      <br/>Je kunt je eigen kleur mixen met Color
      <br/><em>Voorbeeld:</em> <br/><br/>
      <pre>
        wis(); vertraging(0)
        vulkleur(blauw)         //zet de vulkleur naar blauw
        cirkel(100)             //de schildpad tekent een blauwe cirkel met radius 100
        spring(100)             //de schildpad springt vooruit 100 stappen
        vulkleur(geenKleur)     //zet de vulkleur
        cirkel(100)             //de schildpad tekent een cirkel met geen vulkleur
      </pre>
    </div>.toString,
    "penBreedte" -> <div><strong>penBreedte</strong>(penBreedte)<br/>Wijzigt de pen breedte. <br/> Hoe hoger de pen breedte, hoe dikker de strepen.</div>.toString,
    "bewaarStijl" -> <div><strong>bewaarStijl</strong>()<br/>Slaat de pen kleur, vulkleur, breedte en de lettergrootte op.<br/>Je herstelt de opgeslagen stijl met zetStijlTerug().</div>.toString,
    "zetStijlTerug" -> <div><strong>zetStijlTerug</strong>()<br/>Zet de opgeslagen pen kleur, vulkleur, breedte en de lettergrootte terug. Je kunt de stijl opslaan met bewaarStijl().</div>.toString,
    "bewaarPositieEnHoek" -> <div><strong>bewaarPositieEnHoek</strong>()<br/>Slaat de huidige positie en hoek op.<br/>Je kunt weer de opgeslagen positie en hoek van zetPositieEnHoekTerug().</div>.toString,
    "zetPositieEnHoekTerug" -> <div><strong>zetPositieEnHoekTerug</strong>()<br/>Zet the opgeslagen positie en hoek terug.<br/>Je kunt de positie en hoek opslaan met bewaarPositieEnHoek().</div>.toString,
    "vizierAan" -> <div><strong>vizierAan</strong>()<br/>Laat zien welke weg de schildpad wijst met een vizier.</div>.toString,
    "vizierUit" -> <div><strong>vizierUit</strong>()<br/>Verberg de vizier van de schildpad.</div>.toString,
    "engels" -> <div><strong>engelska</strong><br/>Geeft de Engelse schildpad.<br/>Als je typt:<br/><tt>schildpad.engels.</tt><br/>kun je zien alles wat een schildpad in Engels kan doen.</div>.toString,
    "wis" -> <div><strong>wis</strong>()<br/>Wist alles in het tekencanvas, en brengt de schildpad naar het midden van het canvas</div>.toString,
    "wisOutput" -> <div><strong>wisOutput</strong>()<br/>Wist alles in het output venster.</div>.toString,
    "achtergrond" -> <div><strong>achtergrond</strong>(achtergrondKleur)<br/>Wijzig de achtergrond kleur.
      <br/>Je kunt deze pre-gemengde kleuren gebruiken:
      <br/>blauw, rood, geel, groen, paars, roze, bruin, zwaart, wit, geenKleur.
      <br/>Je kunt je eigen kleur mixen met Color.</div>.toString,
    "achtergrondV" -> <div><strong>achtergrondV</strong>(kleur1,kleur2)<br/>Zet de achtergrond kleur naar een overgang van kleur 1 tot kleur2.
      <br/>Je kunt deze pre-gemengde kleuren gebruiken:
      <br/>blauw, rood, geel, groen, paars, roze, bruin, zwaart, wit, geenKleur.
      <br/>Je kunt je eigen kleur mixen met Color.</div>.toString,
    "herhaal" -> <div><strong>herhaal</strong>(aantal) {{ statements }} - herhaal <em> statements</em> het gegeven aantal keren.
      <br/><em>Voorbeeld:</em> <br/><br/>
      <pre>
        herhaal(4) {{
          vooruit()
          links()
        }}
      </pre>
    </div>.toString,
    "herhaalTel" -> <div><strong>herhaalTel</strong>(antal) {{ i => statements }} - herhaal <em>statements</em> het gegeven aantal keren en tel elke ronde van de herhaling. De teller is de waarde in <strong>i</strong>.
      <br/><em>Voorbeeld:</em> <br/><br/>
      <pre>
        herhaalTel(10) {{ i =>
        output(i)
        }}
      </pre>
    </div>.toString,
    "zolangAls" -> <div><strong>zolangAls</strong>(conditie) {{  statements }} - herhaal <em>statements</em> zolang als <em>conditie</em> waar is.
      <br/><em>Voorbeeld:</em> <br/><br/>
      <pre>var i = 0
        zolangAls(i{ "<" }10) {{
        output(i)
        i = i + 1
        }}
      </pre>
    </div>.toString,
    "output" -> <div><strong>output</strong>(tekstSnaar)<br/>Schrijft tekst in <em>tekstSnaar</em> in het output venster <br/> Een snaar moet dubbele aanhalingstekens hebben aan het begin en einde. Voorbeeld: output("hallo")</div>.toString,
    "input" -> <div><strong>input</strong>(leidTekst)<br/>Schrijft de leidtekst in het output venster en wacht op input van tekst totdat je op Enter toetst.<br/>
      <br/><em>Voorbeeld:</em> <br/><br/>
      <pre>val x = input("Typ je naam ")
        output("Hallo " + x + "!")
      </pre>
    </div>.toString,
    "rondAf" -> <div><strong>rondAf</strong>(getal, aantalDecimalen)<br/>Rondt een getal af op een gegeven aantal decimalen<br/>
      <br/><em>Voorbeeld:</em> <br/><br/>
      <pre>val t1 = rondAf(3.991,2) //op 2 decimalen afronden
        output(t1)
        val t2 = rondAf(3.999) //op 0 decimalen afronden
        output(t2)
      </pre>
    </div>.toString,
    "systeemTijd" -> <div><strong>systeemTijd</strong><br/>Geeft de systeemklok in seconden. Je kunt de systeemTijd gebruiken om te meten hoe lang iets duurt.<br/>
      <br/><em>Voorbeeld:</em> <br/><br/>
      <pre>
        val start = systeemTijd
        output("Even geduld!")
        telTot(100000000)
        val stop = systeemTijd
        val s = stop - start
        output("Je had " + rondAf(s,1) + " seconden geduld.")
      </pre>
    </div>.toString,
    "telTot" -> <div><strong>telTot</strong>(getal)<br/>Bekijkt hoe lang het duurt voordat de computer tot een gegeven getal telt. Jij kunt met een vrij groot getal proberen.<br/>
      <br/><em>Voorbeeld:</em> <br/><br/>
      <pre>
        telTot(5000)
      </pre>
    </div>.toString,
    "toeval" -> <div><strong>toeval</strong>(bovengrens)<br/>Geeft een willekeurig getal tussen 0 en bovengrens.
      <br/><em>Voorbeeld:</em>
      <br/><pre>  def toeval = toeval(20) + 1 </pre>
      <br/>Geeft een willekeurig getal van 1 tot en met 20</div>.toString,
    "toevalDubbel" -> <div><strong>toevalDubbel</strong>(bovengrens)<br/>Geeft een willekeurig decimaal getal tussen 0 en bovengrens.
      <br/><em>Voorbeeld:</em>
      <br/><pre> def toeval = toevalDubbel(20) + 1.0</pre>
      <br/>Geeft een willekeurig getal van 1.0 tot en met 20.0</div>.toString,
    "kostuum" -> <div><strong>kostuum</strong>(bestandNaam)
      <br/>Gebruik de afbeelding in het bestand om de uiterlijk van de schildpad te wijzigen.
      <br/><em>Voorbeeld:</em>
      <br/><pre>
      wis()
      kostuum("achtergrond.jpg") //de uiterlijk van de gewoone schildpad wordt achtergrond.jpg
      vooruit(100)               //de achtergrond loopt
      val aardbei = new Schildpad(100,100,"aardbei.jpg") //Een nieuwe schildpad wordt gecreërt op de positie (100,100) met de afbeelding aardbei.jpg
      aardbei.vooruit(100)
    </pre><br/></div>.toString
  )
}