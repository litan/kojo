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

// Croatian Turtle wrapper for Kojo

package net.kogics.kojo.lite.i18n

import net.kogics.kojo.lite.Builtins
import net.kogics.kojo.lite.CoreBuiltins
import net.kogics.kojo.xscala.RepeatCommands

object CroatianAPI {
  import java.awt.Color

  import net.kogics.kojo.core.Turtle
  var builtins: net.kogics.kojo.lite.CoreBuiltins = _ // unstable reference to module

  trait CroatianTurtle {
    def englishTurtle: Turtle
    def obriši() = englishTurtle.clear()
    def vidljivo() = englishTurtle.visible()
    def nevidljivo() = englishTurtle.invisible()
    def naprijed(koraci: Double) = englishTurtle.forward(koraci)
    def naprijed() = englishTurtle.forward(25)
    def desno(kut: Double) = englishTurtle.right(kut)
    def desno() = englishTurtle.right(90)
    def lijevo(kut: Double) = englishTurtle.left(kut)
    def lijevo() = englishTurtle.left(90)
    def skočiNa(x: Double, y: Double) = englishTurtle.jumpTo(x, y)
    def idiNa(x: Double, y: Double) = englishTurtle.moveTo(x, y)
    def skoči(n: Double) = {
      englishTurtle.saveStyle() // to preserve pen state
      englishTurtle.hop(n) // hop change state to penDown after hop
      englishTurtle.restoreStyle()
    }
    def skoči(): Unit = skoči(25)
    def kuća() = englishTurtle.home()
    def prema(x: Double, y: Double) = englishTurtle.towards(x, y)
    def postaviSmjer(kut: Double) = englishTurtle.setHeading(kut)
    def smjer = englishTurtle.heading
    def istok() = englishTurtle.setHeading(0)
    def zapad() = englishTurtle.setHeading(180)
    def sjever() = englishTurtle.setHeading(90)
    def jug() = englishTurtle.setHeading(-90)
    def kašnjenje(n: Long) = englishTurtle.setAnimationDelay(n)
    def piši(t: Any) = englishTurtle.write(t)
    def postaviVeličinuFontaPera(veličina: Int) = englishTurtle.setPenFontSize(veličina)
    def luk(polumjer: Double, kut: Double) = englishTurtle.arc(polumjer, math.round(kut).toInt)
    def krug(polumjer: Double) = englishTurtle.circle(polumjer)
    def pozicija = englishTurtle.position
    def spustiPero() = englishTurtle.penDown()
    def podigniPero() = englishTurtle.penUp()
    def jeliPeroSpušteno = englishTurtle.style.down
    def postaviBojuPera(boja: java.awt.Color) = englishTurtle.setPenColor(boja)
    def postaviBojuPunjenja(boja: java.awt.Color) = englishTurtle.setFillColor(boja)
    def postaviDebljinuPera(n: Double) = englishTurtle.setPenThickness(n)
    def snimiStil() = englishTurtle.saveStyle()
    def povratiStil() = englishTurtle.restoreStyle()
    def snimiPozicijuSmjera() = englishTurtle.savePosHe()
    def povratiPozicijuSmjera() = englishTurtle.restorePosHe()
    def zrakeUpaljene() = englishTurtle.beamsOn()
    def zrakeUgašene() = englishTurtle.beamsOff()
    def postaviKostim(imeDatoteke: String) = englishTurtle.setCostume(imeDatoteke)
    def postaviKostime(imeDatoteke: String*) = englishTurtle.setCostumes(imeDatoteke: _*)
    def sljedećiKostim() = englishTurtle.nextCostume()
  }
  class Kornjača(override val englishTurtle: Turtle) extends CroatianTurtle {
    def this(startX: Double, startY: Double, costumeFileName: String) =
      this(builtins.TSCanvas.newTurtle(startX, startY, costumeFileName))
    def this(startX: Double, startY: Double) = this(startX, startY, "/images/turtle32.png")
    def this() = this(0, 0)
  }
  class Kornjača0(t0: => Turtle) extends CroatianTurtle { // by-name construction as turtle0 is volatile }
    override def englishTurtle: Turtle = t0
  }
  object kornjača extends Kornjača0(builtins.TSCanvas.turtle0)
  def obriši() = builtins.TSCanvas.clear()
  def obrišiIzlaz() = builtins.clearOutput()
  lazy val plava = builtins.blue
  lazy val crvena = builtins.red
  lazy val žuta = builtins.yellow
  lazy val zelena = builtins.green
  lazy val ljubičasta = builtins.purple
  lazy val ružičasta = builtins.pink
  lazy val smeđa = builtins.brown
  lazy val crna = builtins.black
  lazy val bijela = builtins.white
  lazy val bezBoje = builtins.noColor
  def postaviPozadinu(c: Color) = builtins.setBackground(c)
  def postaviPozadinuV(c1: Color, c2: Color) = builtins.TSCanvas.setBackgroundV(c1, c2)

  //  object KcSwe { //Key codes for Swedish keys
  //    lazy val VK_Å = 197
  //    lazy val VK_Ä = 196
  //    lazy val VK_Ö = 214
  //  }

  // loops
  def ponovi(n: Int)(block: => Unit): Unit = {
    RepeatCommands.repeat(n) { block }
  }

  def ponovii(n: Int)(block: Int => Unit): Unit = {
    RepeatCommands.repeati(n) { i => block(i) }
  }

  def ponavljajDok(uvjet: => Boolean)(block: => Unit): Unit = {
    RepeatCommands.repeatWhile(uvjet) { block }
  }

  def ponavljajZa[T](slijed: Iterable[T])(block: T => Unit): Unit = {
    RepeatCommands.repeatFor(slijed) { block }
  }

  // simple IO
  def čitajln(upit: String = "") = builtins.readln(upit)

  def pišiln(data: Any) = println(data) // Transferred here from sv.tw.kojo.
  def pišiln() = println()

  // math functions
  def zaokruži(broj: Number, znamenka: Int = 0): Double = {
    val faktor = math.pow(10, znamenka).toDouble
    math.round(broj.doubleValue * faktor).toLong / faktor
  }
  def slučajan(gornjaGranica: Int) = builtins.random(gornjaGranica)
  def slučajanDupli(gornjaGranica: Int) = builtins.randomDouble(gornjaGranica)

  // some type aliases in Swedish
  type Broj = Int
  type Dvostruki = Double
  type Niz = String
}

object hrInit {
  def init(builtins: CoreBuiltins): Unit = {
    // initialize unstable value
    CroatianAPI.builtins = builtins
    builtins match {
      case b: Builtins =>
        println("Dobrodošli u Kojo sa hrvatskom kornjačom!")
        if (b.isScratchPad) {
          println("Povijest neće biti snimljena kada zatvorite Kojo podlogu za natuknice.")
        }

//        b.setEditorTabSize(2)

        // code completion
        b.addCodeTemplates(
          "hr",
          codeTemplates
        )
        // help texts
        b.addHelpContent(
          "hr",
          helpContent
        )

      case _ =>
    }
  }

  val codeTemplates = Map(
    "naprijed" -> "naprijed(${koraci})",
    "desno" -> "desno(${kut})",
    "lijevo" -> "lijevo(${kut})",
    "skočiNa" -> "skočiNa(${x},${y})",
    "idiNa" -> "idiNa(${x},${y})",
    "skoči" -> "skoči(${koraci})",
    "kuća" -> "kuća()",
    "prema" -> "prema(${x},${y})",
    "postaviSmjer" -> "postaviSmjer(${kut})",
    "istok" -> "istok()",
    "zapad" -> "zapad()",
    "sjever" -> "sjever()",
    "jug" -> "jug()",
    "kašnjenje" -> "kašnjenje(${miliSekunde})",
    "piši" -> "piši(${tekst})",
    "postaviVeličinuFontaPera" -> "postaviVeličinuFontaPera(${veličina})",
    "luk" -> "luk(${polumjer},${kut})",
    "krug" -> "krug(${polumjer})",
    "vidljivo" -> "vidljivo()",
    "nevidljivo" -> "nevidljivo()",
    "spustiPero" -> "spustiPero()",
    "podigniPero" -> "podigniPero()",
    "jeliPeroSpušteno" -> "jeliPeroSpušteno",
    "postaviBojuPera" -> "postaviBojuPera(${boja})",
    "postaviBojuPunjenja" -> "postaviBojuPunjenja(${boja})",
    "postaviDebljinuPera" -> "postaviDebljinuPera(${širina})",
    "snimiStil" -> "snimiStil()",
    "povratiStil" -> "povratiStil()",
    "snimiPozicijuSmjera" -> "snimiPozicijuSmjera()",
    "povratiPozicijuSmjera" -> "povratiPozicijuSmjera()",
    "zrakeUpaljene" -> "zrakeUpaljene()",
    "zrakeUgašene" -> "zrakeUgašene()",
    "obriši" -> "obriši()",
    "obrišiIzlaz" -> "obrišiIzlaz()",
    "postaviPozadinu" -> "postaviPozadinu(${boja})",
    "postaviPozadinuV" -> "postaviPozadinuV(${boja1},${boja2})",
    "ponovi" -> "ponovi(${broji}) {\n    ${cursor}\n}",
    "ponovii" -> "ponovii(${broji}) { i =>\n    ${cursor}\n}",
    "ponavljajDok" -> "ponavljajDok(${uvjet}) {\n    ${cursor}\n}",
    "ponavljajZa" -> "ponavljajZa(${slijed}) { ${e} =>\n    ${cursor}\n}",
    "pišiln" -> "pišiln(${tekst})",
    "čitajln" -> "čitajln(${upit})",
    "zaokruži" -> "zaokruži(${broj},${znamenka})",
    "slučajan" -> "slučajan(${gornjaGranica})",
    "slučajanDupli" -> "slučajanDupli(${gornjaGranica})",
    "postaviKostim" -> "postaviKostim(${imeDatoteke})",
    "postaviKostime" -> "postaviKostime(${imeDatoteke1},${imeDatoteke2})",
    "sljedećiKostim" -> "sljedećiKostim()"
  )

  val helpContent = Map(
    "naprijed" -> <div><strong>naprijed</strong>(brojKoraka) - Pomiče kornjaču naprijed za zadani broj koraka.</div>.toString,
    "lijevo" -> <div> <strong>lijevo</strong>() - Okreće kornjaču 90 stupnjeva u lijevo (obrnuto od smjera kazaljke na satu). <br/> <strong>lijevo</strong>(kut) - Okreće kornjaču u lijevo (obrnuto od smjera kazaljke na satu) pod zadanim kutem.<br/> <strong>lijevo</strong>(kut, polumjer) - Okreće kornjaču u lijevo (obrnuto od smjera kazaljke na satu) pod zadanim kutem, oko zadanog polumjera.<br/> </div>.toString,
    "desno" -> <div> <strong>desno</strong>() - Okreće kornjaču 90 stupnjeva u desno (u smjeru kazaljke na satu). <br/> <strong>desno</strong>(kut) - Okreće kornjaču u desno (u smjeru kazaljke na satu) pod zadanim kutem.<br/> <strong>desno</strong>(kut, polumjer) - Okreće kornjaču u desno (u smjeru kazaljke na satu) pod zadanim kutem, oko zadanog polumjera.<br/> </div>.toString,
    "skočiNa" -> <div> <strong>skočiNa</strong>(x, y) - Postavlja kornjaču u točku(x, y) bez crtanja linije. Smjer kornjače ostaje nepromijenjen. <br/> </div>.toString,
    "idiNa" -> <div><strong>idiNa</strong>(x, y) - Okreće kornjaču prema točki (x, y) i miče ju do nje.</div>.toString,
    "skoči" -> <div> <strong>skoči</strong>(brojKoraka) - Pomiče kornjaču naprijed za zadani broj koraka <em>s podignutim perom</em>, tako da se ne crta linija. Perko se spušta nakon skoka. <br/> </div>.toString,
    "kuća" -> <div><strong>kuća</strong>() - Pomiče kornjaču na njeno prvotno mjesto u sredini ekrana i okreće ju u smjeru sjevera.</div>.toString,
    "prema" -> <div><strong>prema</strong>(x, y) - Okreće kornjaču prema točki (x, y).</div>.toString,
    "postaviSmjer" -> <div><strong>postaviSmjer</strong>(kut) - Postavlja smjer kornjače prema određenom kutu (0 je prema desnoj strani ekrana (<em>istok</em>), 90 je gore (<em>sjever</em>)).</div>.toString,
    "smjer" -> <div><strong>smjer</strong> - Traži smjer kornjače (0 je prema desnoj strani ekrana (<em>istok</em>), 90 is gore (<em>sjever</em>)).</div>.toString,
    "istok" -> <div><strong>istok</strong>() - Okreće kornjaču prema sjeveru.</div>.toString,
    "zapad" -> <div><strong>zapad</strong>() - Okreće kornjaču prema zapadu.</div>.toString,
    "sjever" -> <div><strong>sjever</strong>() - Okreće kornjaču prema sjeveru.</div>.toString,
    "jug" -> <div><strong>jug</strong>() - Okreće kornjaču prema jugu.</div>.toString,
    "kašnjenje" -> <div> <strong>postaviKašnjenjeAnimacije</strong>(kašnjenje) - Postavlja brzinu kornjače. Specificirano kašnjenje je vrijeme (u milisekundama) koje kornjači treba da prijeđe udaljenost od 100 koraka.<br/> Početna vrijednost je 1000 milisekundi (ili 1 sekunda).<br/> </div>.toString,
    "piši" -> <div><strong>piši</strong>(objekt) - Kornjača ispisuje zadani objekt kao niz na njenoj trenutnoj lokaciji.</div>.toString,
    "postaviVeličinuFontaPera" -> <div><strong>postaviVeličinuFontaPera</strong>(n) - Određuje veličinu fonta pera kojim kornjača piše.</div>.toString,
    "luk" -> <div> <strong>luk</strong>(polumjer, kut) - Kornjača radi luk sa zadanim polumjerom i kutem.<br/> Pozitivni kutevi pomiču kornjaču u lijevo (obrnuto od kazaljke na satu).Negativni kutevi pomiču kornjaču u desno (u smjeru kazaljke na satu) <br/> </div>.toString,
    "krug" -> <div> <strong>krug</strong>(polumjer) - Kornjača radi krug sa zadanim polumjerom. <br/> Krug(50) komanda je jednaka kao i luk(50, 360) komanda.<br/> </div>.toString,
    "vidljivo" -> <div><strong>vidljivo</strong>() - Čini kornjaču vidljivom nakon što je postavljena na nevidljivo sa nevidljivo() komandom.</div>.toString,
    "nevidljivo" -> <div><strong>nevidljivo</strong>() - Čini kornjalu nevidljivom. Koristite komandu vidljivo() da bi ju opet učinili vidljivom.</div>.toString,
    "spustiPero" -> <div> <strong>spustiPero</strong>() - Spušta kornjačino pero, tako da crta linije dok se kreće.<br/> Perko je spušteno u zadanim postavkama. <br/> </div>.toString,
    "podigniPero" -> <div><strong>podigniPero</strong>() - Podiže kornjačino pero, tako da ne crta linije dok se kreće. <br/></div>.toString,
    "jeliPeroSpušteno" -> <div><strong>jeliPeroSpušteno</strong> - Govori nam je li kornjačino pero spušteno.</div>.toString,
    "postaviBojuPera" -> <div><strong>postaviBojuPera</strong>(boja) - Određuje boju pera kojim kornjača crta. <br/></div>.toString,
    "postaviBojuPunjenja" -> <div><strong>postaviBojuPunjenja</strong>(boja) - Određuje boju kojom će likovi koje kornjača nacrta biti obojani. <br/></div>.toString,
    "postaviDebljinuPera" -> <div><strong>postaviDebljinuPera</strong>(debljina) - Određuje debljinu pera kojim kornjača crta. <br/></div>.toString,
    "snimiStil" -> <div> <strong>snimiStil</strong>() - Snima kornjačin trenutni stič, tako da kasnije lagano može biti povraćen sa <tt>povratiStil()</tt>. <br/> <p> Kornjačin stil sadrži: <ul> <li>Boju Pera</li> <li>Debljinu Pera</li> <li>Boju Punjenja</li> <li>Font Pera</li> <li>Stanje Pera Dignuto/Spušteno</li> </ul> </p> </div>.toString,
    "povratiStil" -> <div> <strong>povratiStil</strong>() - Vraća prijašnji stil kornjače temeljen na ranijem <tt>snimiStil()</tt>. <br/> <p> The turtle's style includes: <ul> <li>Boju Pera</li> <li>Debljinu Pera</li> <li>Boju Punjenja</li> <li>Font Pera</li> <li>Stanje Pera Dignuto/Spušteno</li> </ul> </p> </div>.toString,
    "snimiPozicijuSmjera" -> <div> <strong>SnimiPozicijuSmjera</strong>() - Snima kornjačinu trenutnu poziciju i smjer, tako da mogu kasnije biti povraćeni sa <tt>povratiPozicijuSmjera()</tt>. <br/> </div>.toString,
    "povratiPozicijuSmjera" -> <div> <strong>povratiPozicijuSmjera</strong>() - Vraća kornjačinu trenutnu poziciju i smjer temeljeno na prijašnjoj komandi <tt>snimiPozicijuSmjera()</tt>. <br/> </div>.toString,
    "zrakeUpaljene" -> <div><strong>zrakeUpaljene</strong>() - Prikazuje zrake centrirane na kornjači - da nam pomogne razmišljati o kornjačinom smjeru/orijentaciji.</div>.toString,
    "zrakeUgašene" -> <div><strong>zrakeUgašene</strong>() - Skriva kornjačine zrake koje su bile uključene sa <tt>zrakeUpaljene()</tt>.</div>.toString,
    "obriši" -> <div><strong>obriši</strong>() - Briše kornjačino platno i postavlja ju u sredinu.</div>.toString,
    "obrišiIzlaz" -> <div><strong>obrišiIzlaz</strong>() - Briše izlazni prozor.</div>.toString,
    "postaviPozadinu" -> <div> <strong>postaviPozadinu</strong>(boja) - Postavlja pozadinu platna u određenu boju. Možete koristiti predefinirane boje za bojanje pozadine, ili možete stvoriti nove boje koristeći <tt>Boja</tt>, <tt>BojaHSB</tt>, i <tt>BojaG</tt> funkcije. </div>.toString,
    "postaviPozadinuV" -> <div><strong>postaviPozadinuV</strong>(boja1, boja2) - Postavlja pozadinu platna u okomit obojani gradijent definiran s dvije boje. </div>.toString,
    "ponovi" -> <div><strong>ponovi</strong>(n){{ }} - Ponavlja određeni skup komandi (unutar vitičastih zagrada) n puta.<br/></div>.toString,
    "ponovii" -> <div><strong>ponovii</strong>(n) {{i => }} - Ponavlja određeni skup komandi (unutar vitičastih zagrada) n puta. Trenutna veličina iteratora dostupna je kao <tt>i</tt> unutar vitičastih zagrada. </div>.toString,
    "ponavljajDok" -> <div><strong>ponavljajDok</strong>(uvjet) {{ }} - Ponavlja određeni skup komandi (unutar vitičastih zagrada) dok je uvjet istinit.</div>.toString,
    "ponavljajZa" -> <div><strong>ponavljajZa</strong>(slijed){{ }} - Ponavlja određeni skup komandi (unutar vitičastih zagrada) za svaki element zadanog slijeda.<br/></div>.toString,
    "pišiln" -> <div><strong>pišiln</strong>(objekt) - Prikazuje objekt kao niz u prozoru izlaza, s novom linijom na kraju.</div>.toString,
    "čitajln" -> <div><strong>čitajln</strong>(upitniNiz) - Prikazuje zadani upit u prozoru izlaza i čita liniju koju korisnik unese.</div>.toString,
    "zaokruži" -> <div><strong>zaokruži</strong>(n, znamenka) - Zaokružuje broj n na određeni broj znamenki nakon decimalne točke.</div>.toString,
    "slučajan" -> <div><strong>slučajan</strong>(gornjaGranica) - Vraća slučajan broj između 0 (uključuje ju) i zadane gornje granice (ne uključuje ju).</div>.toString,
    "slučajanDupli" -> <div><strong>slučajanDupli</strong>(gornjaGranica) - Vraća slučajan dvostruko-precizan decimalni broj između 0 (uključuje ju) i zadane gornje granice (ne uključuje ju).</div>.toString,
    "postaviKostim" -> <div><strong>postaviKostim</strong>(DatotekaKostima) - Mijenja kostim (sliku) kornjače u sliku iz navedene datoteke.</div>.toString,
    "postaviKostime" -> <div><strong>postaviKostime</strong>(DatotekaKostima1, DatotekaKostima2, ...) - Određuje više različitih kostima za kornjaču, i postavlja kornjačin kostim na prvi u redu. Kostime možete mijenjati koristeći <tt>sljedećiKostim()</tt>. </div>.toString,
    "sljedećiKostim" -> <div><strong>sljedećiKostim</strong>() - Mijenja kornjačin kostim na sljedeći u nizu kostima određenim s <tt>postaviKostime(...)</tt>.</div>.toString
  )
}
