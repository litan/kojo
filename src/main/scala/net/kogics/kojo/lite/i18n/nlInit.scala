/*
 * Copyright (C) 2013 
 *   Lalit Pant <pant.lalit@gmail.com>,
 *   Eric Zoerner <eric.zoerner@gmail.com>
 *   Jacco Huysmans <jhuysmans@rhinofly.nl>
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
    def start() = engels.home()
    def naartoe(x: Double, y: Double) = engels.towards(x, y)
    def zetRichting(hoek: Double) = engels.setHeading(hoek)
    def richting = engels.heading
    def oost() = engels.setHeading(0)
    def west() = engels.setHeading(180)
    def noord() = engels.setHeading(90)
    def zuid() = engels.setHeading(-90)
    def vertraging(n: Long) = engels.setAnimationDelay(n)
    def schrijf(t: Any) = engels.write(t)
    def tekstGrootte(s: Int) = engels.setPenFontSize(s)
    def boog(radius: Double, hoek: Double) = engels.arc(radius, math.round(hoek).toInt)
    def cirkel(radius: Double) = engels.circle(radius)
    def positie = engels.position
    def penOpCanvas() = engels.penDown()
    def penVanCanvas() = engels.penUp()
    def isPenOpCanvas = engels.style.down
    def penKleur(c: java.awt.Color) = engels.setPenColor(c)
    def vulkleur(c: java.awt.Color) = engels.setFillColor(c)
    def lijnDikte(n: Double) = engels.setPenThickness(n)
    def bewaarStijl() = engels.saveStyle()
    def zetStijlTerug() = engels.restoreStyle()
    def bewaarPositieEnRichting() = engels.savePosHe()
    def zetPositieEnRichtingTerug() = engels.restorePosHe()
    def vizierAan() = engels.beamsOn()
    def vizierUit() = engels.beamsOff()
    def kostuum(bestandsNaam: String) = engels.setCostume(bestandsNaam)
    def kostuums(bestandsNaam: String*) = engels.setCostumes(bestandsNaam: _*)
    def volgendKostuum() = engels.nextCostume()
  }
  class Schildpad(override val engels: Turtle) extends DutchTurtle {
    def this(startX: Double, startY: Double, kostuumBestandsNaam: String) =
      this(builtins.TSCanvas.newTurtle(startX, startY, kostuumBestandsNaam))
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
  def willekeurig(n: Int) = builtins.random(n)
  def willekeurigDecimaal(n: Int) = builtins.randomDouble(n)
  def kleur(r: Int, g: Int, b: Int) = builtins.Color(r, g, b)

  //some type aliases in Dutch
  type GeheelGetal = Int
  type Decimaal = Double
  type Tekst = String

  //speedTest
  def systeemTijd() = BigDecimal(System.nanoTime()) / BigDecimal("1000000000") //seconds

  @annotation.nowarn def telTot(n: BigInt): Unit = {
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
    "start" -> "start()",
    "naartoe" -> "naartoe(${x},${y})",
    "richting" -> "richting",
    "zetRichting" -> "zetRichting(${hoek})",
    "oost" -> "oost()",
    "west" -> "west()",
    "noord" -> "noord()",
    "zuid" -> "zuid()",
    "vertraging" -> "vertraging(${vertraging})",
    "schrijf" -> "schrijf(${snaar})",
    "tekstGrootte" -> "tekstGrootte(${maat})",
    "boog" -> "boog(${radius},${hoek})",
    "cirkel" -> "cirkel(${radius})",
    "vertoon" -> "vertoon()",
    "verberg" -> "verberg()",
    "positie" -> "positie",
    "penOpCanvas" -> "penOpCanvas()",
    "penVanCanvas" -> "penVanCanvas()",
    "isPenOpCanvas" -> "isPenOpCanvas",
    "penKleur" -> "penKleur(${penKleur})",
    "vulkleur" -> "vulkleur(${kleur})",
    "lijnDikte" -> "lijnDikte(${lijnDikte})",
    "bewaarStijl" -> "bewaarStijl()",
    "zetStijlTerug" -> "zetStijlTerug()",
    "bewaarPositieEnRichting" -> "bewaarPositieEnRichting()",
    "zetPositieEnRichtingTerug" -> "zetPositieEnRichtingTerug()",
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
    "willekeurig" -> "willekeurig(${bovengrens})",
    "willikeurigDecimaal" -> "willikeurigDecimaal(${bovengrens})",
    "telTot" -> "telTot(${getal})",
    "systeemTijd" -> "systeemTijd",
    "kostuum" -> "kostuum(${bestandsNaam})",
    "kostuums" -> "kostuums(${bestandsNaam},${bestandsNaam})",
    "volgendKostuum" -> "volgendKostuum()"
  )

  val helpContent = Map(
    "vooruit" ->
      <div>
        <strong>vooruit</strong>(stap) - De schildpad gaat het aantal stappen naar voren die je opgeeft, in de richting waarheen zijn neus wijst.
        <br/>Als de pen op het canvas is, tekent de schildpad terwijl hij naar voren loopt.
        <br/><em>Voorbeeld:</em> <br/><br/>
        <pre>
          wis()           //tekencanvas wordt gewist en de schildpad is naar het midden verplaatst
          vooruit(100)    //de schildpad gaat 100 stappen rechtdoor
          vooruit()       //de schildpad gaat 25 stappen rechtdoor
          penVanCanvas()  //de schildpad tilt de pen van het canvas
          vooruit(200)    //de schildpad gaat 200 stappen vooruit zonder te tekenen
          rechts(45)      //de schildpad draait 45 graden naar rechts
        </pre>
      </div>.toString,
    "links" -> <div><strong>links</strong>(hoek)<br/>De schildpad draait 90 graden naar links.</div>.toString,
    "rechts" -> <div><strong>rechts</strong>(hoek)<br/>De schildpad draait 90 graden naar rechts.</div>.toString,
    "springNaar" -> <div><strong>springNaar</strong>(x, y)<br/>De schildpad springt naar positie (x, y) zonder te tekenen, en zonder van richting te veranderen.</div>.toString,
    "gaNaar" -> <div><strong>gaNaar</strong>(x, y)<br/>De schildpad draait in de richting van de positie (x,y) en loopt er heen.<br/>Als de pen op het canvas is, tekent de schildpad terwijl hij loopt.</div>.toString,
    "spring" -> <div><strong>spring</strong>(stap)<br/>De schildpad springt het aantal stappen en er wordt niets getekend, zelfs niet als de pen op het canvas zit.</div>.toString,
    "start" -> <div><strong>start</strong>()<br/>De schildpad gaat terug naar start positie (0,0) en draait zodat zijn neus naar het noorden wijst. <br/>Als de pen op het canvas is, tekent de schildpad terwijl hij loopt.</div>.toString,
    "naartoe" -> <div><strong>naartoe</strong>(x, y)<br/>De schildpad draait zodat zijn neus naar de locatie (x, y) wijst.</div>.toString,
    "zetRichting" -> <div><strong>zetRichting</strong>(hoek)<br/>De schildpad draait zodat zijn neus in de gespecificeerde hoek komt te staan.</div>.toString,
    "richting" -> <div><strong>richting</strong><br/>Geeft de waarde van de hoek waarin de neus van de schildpad staat.</div>.toString,
    "oost" -> <div><strong>oost</strong>()<br/>De schildpad draait zodat zijn neus naar het oosten wijst (rechts).</div>.toString,
    "west" -> <div><strong>west</strong>()<br/>De schildpad draait zodat zijn neus naar het westen wijst (links).</div>.toString,
    "noord" -> <div><strong>noord</strong>()<br/>De schildpad draait zodat zijn neus naar het noorden wijst (naar boven).</div>.toString,
    "zuid" -> <div><strong>zuid</strong>()<br/>De schildpad draait zodat zijn neus naar het zuiden wijst (naar beneden).</div>.toString,
    "vertraging" -> <div><strong>vertraging</strong>(vertraging)<br/>Hoe groter de vertraging hoe langzamer de schildpad gaat. <br/> De minimale vertraging is 0 (geen vertraging)<br/>En een vertraging van 1000 betekent vrij langsaam.</div>.toString,
    "schrijf" -> <div><strong>schrijf</strong>(tekst)<br/>De schildpad schrijft een tekst op het canvas bij de huidige positie.<br/>Een tekst begint en eindigt met dubbele aanhalingstekens. Voorbeeld: schrijf("hallo")</div>.toString,
    "tekstGrootte" -> <div><strong>tekstGrootte</strong>(maat)<br/>Hiermee wijzigt je de grootte van de tekst die de schildpad schrijft.</div>.toString,
    "boog" -> <div><strong>boog</strong>(radius, hoek)<br/>De schildpad tekent een boog met de gegeven radius en hoek.</div>.toString,
    "cirkel" -> <div><strong>cirkel</strong>(radius)<br/>De schildpad tekent een cirkel met de gegeven radius.</div>.toString,
    "vertoon" -> <div><strong>vertoon</strong>()<br/> Hiermee wordt de schildpad weer zichtbaar als hij onzichtbaar was.</div>.toString,
    "verberg" -> <div><strong>verberg</strong>()<br/>Heermee wordt de schildbaar onzichtbaar als hij zichtbaar was.</div>.toString,
    "positie" -> <div><strong>positie</strong><br/>Geeft de positie als een waarde (x, y)
      <br/><em>Voorbeeld:</em> <br/><br/>
      <pre>
        output(positie)     //schrijf de positie van de schildpad in het output venster
        output(positie.x)   //schrijf de x-positie van de schildpad in het output venster
        output(positie.y)   //schrijf de y-positie van de schildpad in het output venster

        var x = positie.x   //slaat de x-positie van de schildpad op in de variabele x
        var y = positie.y   //slaat de y-positie van de schildpad op in de variabele y
        x = x - 100         //wijzigt de variabele x naar de oude waarde min 100
        springNaar(x, y)    //springt naar een nieuwe positie met bijgewerkte x
      </pre>
    </div>.toString,
    "penOpCanvas" -> <div><strong>penOpCanvas</strong>()<br/>Zet de pen op het canvas, zodat de schildpad tekent als hij loopt.</div>.toString,
    "penVanCanvas" -> <div><strong>penVanCanvas</strong>()<br/>Tilt de pen op van het canvas, zodat de schildpad niet meer tekent als hij loopt.</div>.toString,
    "isPenOpCanvas" -> <div><strong>isPenOpCanvas</strong><br/>Controleert of de pen op het canvas is. Geeft <strong>true</strong> (waar) als de pen op het canvas is en <strong>false</strong> (fout) als hij van canvas is.</div>.toString,
    "penKleur" -> <div><strong>penKleur</strong>(penKleur)<br/>Laat de schildpad tekenen met de gekozen kleur.
      <br/>Je kunt deze pre-gemengde kleuren gebruiken:
      <br/>blauw, rood, geel, groen, paars, roze, bruin, zwart, wit, geenKleur.
      <br/>Je kunt je eigen kleur mixen met kleur
      <br/><em>Voorbeeld:</em> <br/><br/>
      <pre>
        penKleur(blauw)                //zet de pen kleur naar blauw
        vooruit()                      //de schildpad loopt vooruit
        penKleur(kleur(220,30,40,250)) //mengt nieuwe kleur (licht paars)
        //roodwaarde=220, groenwaarde=30, blauwwarde=40, dekking=250
        //kleurwaarden kunnen tussen 0 en 255 variëren
        vooruit(200)
      </pre>
    </div>.toString,
    "vulkleur" -> <div><strong>vulkleur</strong>(vulkleur)<br/>De schildpad gebruikt de gekozen kleur om te vullen.
      <br/>Je kunt deze vooraf gemengde kleuren gebruiken:
      <br/>blauw, rood, geel, groen, paars, roze, bruin, zwart, wit, geenKleur.
      <br/>Je kunt je eigen kleur mixen met kleur
      <br/><em>Voorbeeld:</em> <br/><br/>
      <pre>
        wis(); vertraging(0)
        vulkleur(blauw)         //zet de vulkleur naar blauw
        cirkel(100)             //de schildpad tekent een blauwe cirkel met radius 100
        spring(100)             //de schildpad springt 100 stappen vooruit
        vulkleur(geenKleur)     //zet de vulkleur
        cirkel(100)             //de schildpad tekent een cirkel met geen vulkleur
      </pre>
    </div>.toString,
    "lijnDikte" -> <div><strong>lijnDikte</strong>(lijnDikte)<br/>Wijzigt de breedte van de pennenstreep. <br/> Hoe hoger het getal, hoe dikker hij schrijft.</div>.toString,
    "bewaarStijl" -> <div><strong>bewaarStijl</strong>()<br/>Slaat de pen kleur, vulkleur, lijnDikte en de lettergrootte op.<br/>Je herstelt de opgeslagen stijl met zetStijlTerug().</div>.toString,
    "zetStijlTerug" -> <div><strong>zetStijlTerug</strong>()<br/>Zet de opgeslagen pen kleur, vulkleur, lijnDikte en de lettergrootte terug. Je kunt de stijl opslaan met bewaarStijl().</div>.toString,
    "bewaarPositieEnRichting" -> <div><strong>bewaarPositieEnRichting</strong>()<br/>Slaat de huidige positie en richting op.<br/>Je kunt de opgeslagen positie en richting van zetPositieEnRichtingTerug().</div>.toString,
    "zetPositieEnRichtingTerug" -> <div><strong>zetPositieEnRichtingTerug</strong>()<br/>Zet the opgeslagen positie en hoek terug.<br/>Je kunt de positie en hoek opslaan met bewaarPositieEnRichting().</div>.toString,
    "vizierAan" -> <div><strong>vizierAan</strong>()<br/>Laat zien welke weg de schildpad op gaat met behulp van een vizier.</div>.toString,
    "vizierUit" -> <div><strong>vizierUit</strong>()<br/>Verberg het vizier van de schildpad.</div>.toString,
    "engels" -> <div><strong>engels</strong><br/>Geeft een Engelse schildpad.<br/>Als je typt:<br/><tt>schildpad.engels.</tt><br/>wordt alles in de Engelse tekst weer gegeven.</div>.toString,
    "wis" -> <div><strong>wis</strong>()<br/>Wist alles op het canvas, en brengt de schildpad naar het midden van het canvas</div>.toString,
    "wisOutput" -> <div><strong>wisOutput</strong>()<br/>Wist alles in het output venster.</div>.toString,
    "achtergrond" -> <div><strong>achtergrond</strong>(achtergrondKleur)<br/>Wijzig de achtergrond kleur.
      <br/>Je kunt deze vooraf gemengde kleuren gebruiken:
      <br/>blauw, rood, geel, groen, paars, roze, bruin, zwart, wit, geenKleur.
      <br/>Je kunt je eigen kleur mixen met kleur.</div>.toString,
    "achtergrondV" -> <div><strong>achtergrondV</strong>(kleur1,kleur2)<br/>Stel de achtergrond in op een overgang van kleur1 naar kleur2.
      <br/>Je kunt deze vooraf gemengde kleuren gebruiken:
      <br/>blauw, rood, geel, groen, paars, roze, bruin, zwaart, wit, geenKleur.
      <br/>Je kunt je eigen kleur mixen met kleur.</div>.toString,
    "herhaal" -> <div><strong>herhaal</strong>(aantal) {{ opdrachten }} - herhaal <em>opdrachten</em> het gegeven aantal keren.
      <br/><em>Voorbeeld:</em> <br/><br/>
      <pre>
        herhaal(4) {{
          vooruit()
          links()
        }}
      </pre>
    </div>.toString,
    "herhaalTel" -> <div><strong>herhaalTel</strong>(antal) {{ i => opdrachten }} - herhaal <em>opdrachten</em> het gegeven aantal keren en tel elke ronde van de herhaling. De teller is de waarde in <strong>i</strong>.
      <br/><em>Voorbeeld:</em> <br/><br/>
      <pre>
        herhaalTel(10) {{ i =>
        output(i)
        }}
      </pre>
    </div>.toString,
    "zolangAls" -> <div><strong>zolangAls</strong>(voorwaarde) {{  opdrachten }} - herhaal <em>opdrachten</em> zolang als <em>voorwaarde</em> waar is.
      <br/><em>Voorbeeld:</em> <br/><br/>
      <pre>var i = 0
        zolangAls(i{ "<" }10) {{
        output(i)
        i = i + 1
        }}
      </pre>
    </div>.toString,
    "output" -> <div><strong>output</strong>(tekst)<br/>Schrijft tekst in <em>tekst</em> in het output venster <br/> Een tekst moet dubbele aanhalingstekens hebben aan het begin en het einde. Voorbeeld: output("hallo")</div>.toString,
    "input" -> <div><strong>input</strong>(tekst)<br/>Schrijft de tekst in het output venster en wacht op input van tekst totdat je op Enter toetst.<br/>
      <br/><em>Voorbeeld:</em> <br/><br/>
      <pre>val x = input("Typ je naam ")
        output("Hallo " + x + "!")
      </pre>
    </div>.toString,
    "rondAf" -> <div><strong>rondAf</strong>(getal, aantalDecimalen)<br/>Rondt een getal af op het aantal cijfers achter de komma (decimalen)<br/>
      <br/><em>Voorbeeld:</em> <br/><br/>
      <pre>val t1 = rondAf(3.991,2) //op 2 decimalen afronden, geeft 3.99
        output(t1)
        val t2 = rondAf(3.999)   //op 0 decimalen afronden, geeft 4
        output(t2)
      </pre>
    </div>.toString,
    "systeemTijd" -> <div><strong>systeemTijd</strong><br/>Geeft de systeemtijd (van de computer) in seconden. Je kunt de systeemTijd gebruiken om te meten hoe lang iets duurt.<br/>
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
    "telTot" -> <div><strong>telTot</strong>(getal)<br/>Laat zien hoe lang het duurt om de computer tot het getal te laten tellen. Probeer het maar eens met een heel groot getal, bijvoorbeeld 1000000<br/>
      <br/><em>Voorbeeld:</em> <br/><br/>
      <pre>
        telTot(5000)
      </pre>
    </div>.toString,
    "willekeurig" -> <div><strong>willekeurig</strong>(bovengrens)<br/>Geeft een willekeurig getal tussen 0 en de bovengrens.
      <br/><em>Voorbeeld:</em>
      <br/><pre>  def w = willekeurig(20) + 1 </pre>
      <br/>Geeft een willekeurig getal tussen de 1 en 20</div>.toString,
    "willikeurigDecimaal" -> <div><strong>willikeurigDecimaal</strong>(bovengrens)<br/>Geeft een willekeurig decimaal getal tussen 0 en de bovengrens.
      <br/><em>Voorbeeld:</em>
      <br/><pre> def w = willikeurigDecimaal(20) + 1.0</pre>
      <br/>Geeft een willekeurig getal tussen 1.0 en 20.0</div>.toString,
    "kostuum" -> <div><strong>kostuum</strong>(bestandsNaam)
      <br/>Gebruik de afbeelding in het bestand om het uiterlijk van de schildpad te wijzigen.
      <br/><em>Voorbeeld:</em>
      <br/><pre>
      wis()
      kostuum("achtergrond.jpg") //het uiterlijk van de schildpad wordt achtergrond.jpg
      vooruit(100)               //de schildpad loopt
      val aardbei = new Schildpad(100,100,"aardbei.jpg") //Een nieuwe schildpad wordt gecreërt op de positie (100,100) met de afbeelding aardbei.jpg
      aardbei.vooruit(100)
    </pre><br/></div>.toString
  )
}