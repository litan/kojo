/*
 * Copyright (C) 2013
 *   Massimo Ghisalberti <zairik@gmail.com>
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

// Language Turtle wrapper for Kojo

package net.kogics.kojo.lite.i18n

import java.awt.Font
import java.awt.Paint
import java.awt.geom.Point2D
import java.awt.Image

import net.kogics.kojo.lite.CoreBuiltins
import net.kogics.kojo.lite.Builtins
import net.kogics.kojo.xscala.RepeatCommands
import net.kogics.kojo.core.Voice

object ItalianDirectionCases {

  trait Direction
  case object Right extends Direction
  case object Left extends Direction
  case object Top extends Direction
  case object Bottom extends Direction

  trait Direzione extends Direction
  case object Destra extends Direzione
  case object Sinistra extends Direzione
  case object Alto extends Direzione
  case object Basso extends Direzione

}

object ItalianSpeedCases {
  trait Velocità
  case object Lentissima extends Velocità
  case object Lenta extends Velocità
  case object Media extends Velocità
  case object Veloce extends Velocità
  case object Velocissima extends Velocità
}


object ItalianCustomStatements {

  import scala.language.implicitConversions

  class IfClauseExpression[T1, T2](fn1: => T1, fn2: => T2){
    lazy val pred = fn1
    lazy val compl = fn2
  }

  implicit class TernaryAssocExpression(cond: => Boolean){
    def ??[T](thenFn: => T) = new IfClauseExpression(cond, thenFn)
  }

  implicit class TernaryElseClauseExpression[T](falseFn: => T){
    def ::(clause: IfClauseExpression[Boolean, T]) =
      if(clause.pred) clause.compl else falseFn
  }

  def isNotEmpty[T](value: T): Boolean = {
    Option(value) match {
      case None => false
      case Some(v) => v match {
        case false => false
        case 0 => false
        case s: String if s.isEmpty => false
        case _ => true
      }
    }
  }

  implicit class ElvisOperator[T](thatFn: => T) {
    def ?:[A >: T](thisFn: A) = {
      if (thisFn == null) thatFn else thisFn
    }
  }

  implicit class OrOperator[T](thatFn: => T) {
    def oppure[A >: T](thisFn: A) = {
      if(isNotEmpty(thatFn)) thatFn else thisFn
    }
  }

  class IfThenClauseExpression[T](cond: => Boolean, thenFn: => T)
    extends IfClauseExpression(cond, thenFn) {
    def altrimenti[T](elseFn: => T) = {
      if(cond) thenFn else elseFn
    }
  }

  implicit def se[T](cond: => Boolean)(thenFn: => T) = {
    new IfThenClauseExpression(cond, thenFn)
  }

  implicit def seVero[T](cond: Boolean)(thenFn: => T) = {
    if (cond) thenFn else ()
  }

}

object ItalianAPI {
  import ItalianDirectionCases._
  import ItalianSpeedCases._
  import net.kogics.kojo.core.Turtle
  import java.awt.Color
  import net.kogics.kojo.util.Utils

  var builtins: net.kogics.kojo.lite.CoreBuiltins = _ //unstable reference to module

  import ItalianCustomStatements._

  trait ItalianTurtle {
    def englishTurtle: Turtle
    def pulisci() = englishTurtle.clear()
    def rimuovi() = englishTurtle.remove()
    def visibile() = englishTurtle.visible()
    def invisibile() = englishTurtle.invisible()
    def avanti(passi: Double) = englishTurtle.forward(passi)
    def avanti() = englishTurtle.forward(25)
    def indietro(passi: Double) = englishTurtle.back(passi)
    def destra(angolo: Double) = englishTurtle.right(angolo)
    def destra() = englishTurtle.right(90)
    def sinistra(angolo: Double) = englishTurtle.left(angolo)
    def sinistra() = englishTurtle.left(90)
    def saltaVerso(x: Double, y: Double) = englishTurtle.jumpTo(x, y)
    def muoviVerso(x: Double, y: Double) = englishTurtle.moveTo(x, y)
    def cambiaPosizione(x: Double, y: Double) = englishTurtle.changePosition(x, y)
    def salta(n: Double) = {
      englishTurtle.saveStyle() //to preserve pen state
      englishTurtle.hop(n) //hop change state to penDown after hop
      englishTurtle.restoreStyle()
    }
    def salta(): Unit = salta(25)
    def casa() = englishTurtle.home()
    def verso(x: Double, y: Double) = englishTurtle.towards(x, y)
    def impostaDirezione(angolo: Double) = englishTurtle.setHeading(angolo)
    def direzione = englishTurtle.heading
    def est() = englishTurtle.setHeading(0)
    def ovest() = englishTurtle.setHeading(180)
    def nord() = englishTurtle.setHeading(90)
    def sud() = englishTurtle.setHeading(-90)
    def valoreRitardo = englishTurtle.animationDelay
    def ritardo(n: Long) = englishTurtle.setAnimationDelay(n)
    def velocità(v: Velocità) = v match {
      case Lentissima => englishTurtle.setAnimationDelay(2000)
      case Lenta => englishTurtle.setAnimationDelay(1000)
      case Media => englishTurtle.setAnimationDelay(100)
      case Veloce => englishTurtle.setAnimationDelay(10)
      case Velocissima => englishTurtle.setAnimationDelay(0)
    }
    def lentezza(v: Long) = englishTurtle.setAnimationDelay(v)
    def scrivi(t: Any) = englishTurtle.write(t)
    def impostaCarattere(font: Font) = englishTurtle.setPenFont(font)
    def impostaGrandezzaCarattere(dimensione: Int) = englishTurtle.setPenFontSize(dimensione)
    def arco(raggio: Double, angolo: Double) = englishTurtle.arc(raggio, math.round(angolo).toInt)
    def arco2(raggio: Double, angolo: Double) = englishTurtle.arc2(raggio, angolo)
    def cerchio(raggio: Double) = englishTurtle.circle(raggio)
    def punto(diametro: Int) = englishTurtle.dot(diametro)
    def posizione = englishTurtle.position
    def abbassaPenna() = englishTurtle.penDown()
    def alzaPenna() = englishTurtle.penUp()
    def èLaPennaAbbassata = englishTurtle.style.down
    def colorePenna(colore: java.awt.Color) = englishTurtle.setPenColor(colore)
    def coloreRiempimento(colore: java.awt.Color) = englishTurtle.setFillColor(colore)
    def impostaSpessorePenna(n: Double) = englishTurtle.setPenThickness(n)
    def salvaStile() = englishTurtle.saveStyle()
    def ripristinaStile() = englishTurtle.restoreStyle()
    def salvaPosizioneDirezione() = englishTurtle.savePosHe()
    def ripristinaPosizioneDirezione() = englishTurtle.restorePosHe()
    def assi() = englishTurtle.beamsOn()
    def rimuoviAssi() = englishTurtle.beamsOff()

    def indossaCostume(nomeFile: String) = englishTurtle.setCostume(nomeFile)
    def indossaImmagine(immagine: Image) = englishTurtle.setCostumeImage(immagine)

    def indossaCostumi(nomiFile: Vector[String]) = englishTurtle.setCostumes(nomiFile)
    def indossaCostumi(nomiFile: String*) = englishTurtle.setCostumes(nomiFile.toVector)

    def indossaImmagini(immagini: Vector[Image]) = englishTurtle.setCostumeImages(immagini)
    def indossaImmagini(immagini: Image*) = englishTurtle.setCostumeImages(immagini.toVector)

    def prossimoCostume() = englishTurtle.nextCostume()
    def scalaCostume(fattore: Double) = englishTurtle.scaleCostume(fattore)
    def suona(voce: Voice) = englishTurtle.playSound(voce)

    def superficie = englishTurtle.area
    def perimetro = englishTurtle.perimeter

    def ultimaLinea = englishTurtle.lastLine
    def ultimaSvolta = englishTurtle.lastTurn

    def square(steps: Double = 100, direction: Direction = Right): Unit = {
      RepeatCommands.repeat(4) {
        englishTurtle.forward(steps)
        direction match {
          case Right => englishTurtle.right()
          case Left  => englishTurtle.left()
        }
      }
    }

    def quadrato(passi: Double = 100, direzione: Direzione = Destra): Unit = {
      square(passi, direzione match {
        case Destra   => Right
        case Sinistra => Left

      })
    }

    def triangle(steps: Double, direction: Direction): Unit = {
      val angles = direction match {
        case Right => (180.0, 60.0)
        case Left  => (0.0, -240.0)
      }
      englishTurtle.setHeading(angles._1)
      englishTurtle.forward(steps)
      englishTurtle.setHeading(angles._2)
      englishTurtle.forward(steps)
      englishTurtle.setHeading(-angles._2)
      englishTurtle.forward(steps)
    }

    def polygon(side: Double, sides: Int) = {
      val a = 180 / (sides.toDouble / 2)
      if(sides % 2 >= 0) englishTurtle.left(90 - a)
      RepeatCommands.repeat(sides) {
        englishTurtle.forward(side)
        englishTurtle.right(a)
      }
    }

    def poligono(lato: Double, lati: Int) = polygon(lato, lati)

    def triangolo(lato: Double, direzione: Direzione = Destra) = {
      triangle(lato, direzione match {
        case Destra   => Right
        case Sinistra => Left

      })
    }

  }

  class Tartaruga(override val englishTurtle: Turtle) extends ItalianTurtle {
    def this(startX: Double, startY: Double, costumeFileName: String) = this(builtins.TSCanvas.newTurtle(startX, startY, costumeFileName))
    def this(startX: Double, startY: Double) = this(startX, startY, "/images/turtle32.png")
    def this() = this(0, 0)

    def fai(fn: Tartaruga => Unit) = Utils.runAsyncMonitored(fn(this))
    def rifai(fn: Tartaruga => Unit): Unit = {
      builtins.TSCanvas.animate {
        fn(this)
      }
    }

  }

  def nuovaTartaruga(): Tartaruga = new Tartaruga(0, 0)
  def nuovaTartaruga(x: Double = 0, y: Double = 0, costume: String = "/images/turtle32.png") = new Tartaruga(x, y, costume)

  class Tartaruga0(t0: => Turtle) extends ItalianTurtle { //by-name construction as turtle0 is volatile }
    override def englishTurtle: Turtle = t0
  }

  object tartaruga extends Tartaruga0(builtins.TSCanvas.turtle0)

  def pulisci() = builtins.TSCanvas.clear()
  def pulisciOutput() = builtins.clearOutput()
  lazy val blu = builtins.blue
  lazy val rosso = builtins.red
  lazy val giallo = builtins.yellow
  lazy val verde = builtins.green
  lazy val porpora = builtins.purple
  lazy val rosa = builtins.pink
  lazy val marrone = builtins.brown
  lazy val nero = builtins.black
  lazy val bianco = builtins.white
  lazy val senzaColore = builtins.noColor
  def sfondo(c: Color) = builtins.setBackground(c)
  def gradiente(c1: Color, c2: Color) = builtins.TSCanvas.setBackgroundV(c1, c2)

  //loops
  def ripeti(n: Int)(block: => Unit): Unit = {
    RepeatCommands.repeat(n) { block }
  }

  def ripetizione(n: Int)(block: Int => Unit): Unit = {
    RepeatCommands.repeati(n) { i => block(i) }
  }

  def ripetiFinché(condizione: => Boolean)(block: => Unit): Unit = {
    RepeatCommands.repeatWhile(condizione) { block }
  }

  def ripetiPerOgniElementoDi[T](sequenza: Iterable[T])(block: T => Unit): Unit = {
    RepeatCommands.repeatFor(sequenza) { block }
  }

  //simple IO
  def leggiLinea(pronto: String = "") = builtins.readln(pronto)

  def scriviLinea(data: Any) = println(data) //Transferred here from sv.tw.kojo.
  def scriviLinea() = println()

  //math functions
  def arrotonda(numero: Number, cifre: Int = 0): Double = {
    val faktor = math.pow(10, cifre).toDouble
    math.round(numero.doubleValue * faktor).toLong / faktor
  }
  def numeroCasuale(limitiSuperiori: Int) = builtins.random(limitiSuperiori)
  def numeroDecimaleCasuale(limitiSuperiori: Int) = builtins.randomDouble(limitiSuperiori)

  //some type aliases in Swedish
  type Intero = Int
  type Decimale = Double
  type Stringa = String

  //speedTest
  def systemtid = BigDecimal(System.nanoTime) / BigDecimal("1000000000") //sekunder

  @annotation.nowarn def conta(n: BigInt): Unit = {
    var c: BigInt = 1
    print("*** conta da 1 fino a ... ")
    val startTid = systemtid
    while (c < n) { c = c + 1 } //tar tid om n är stort
    val stoppTid = systemtid
    println("" + n + " *** PRONTO!")
    val tid = stoppTid - startTid
    print("Ci sono voluti ")
    if (tid < 0.1)
      println((tid * 1000).round(new java.math.MathContext(2)) +
        " millisecondi.")
    else println((tid * 10).toLong / 10.0 + " secondi.")
  }
}



object ItInit {
  def init(builtins: CoreBuiltins): Unit = {
    //initialize unstable value
    ItalianAPI.builtins = builtins
    builtins match {
      case b: Builtins =>
        println("Benvenuti nella Tartaruga Italiana di Kojo!")
        if (b.isScratchPad) {
          println("Lo storico non sarà salvato nel Blocco Note di Kojo alla chiusura.")
        }
        b.setEditorTabSize(2)
        //code completion
        b.addCodeTemplates(
          "it",
          codeTemplates
        )
        //help texts
        b.addHelpContent(
          "it",
          helpContent
        )
      case _ =>
    }
  }

  val codeTemplates = Map(
    "avanti" -> "avanti(${passi})",
    "destra" -> "destra(${angolo})",
    "sinistra" -> "sinistra(${angolo})",
    "saltaVerso" -> "saltaVerso(${x},${y})",
    "muoviVerso" -> "muoviVerso(${x},${y})",
    "cambiaPosizione" -> "cambiaPosizione(${x},${y})",
    "salta" -> "salta(${passi})",
    "casa" -> "casa()",
    "verso" -> "verso(${x},${y})",
    "impostaDirezione" -> "impostaDirezione(${angolo})",
    "est" -> "est()",
    "ovest" -> "ovest()",
    "nord" -> "nord()",
    "sud" -> "sud()",
    "ritardo" -> "ritardo(${milliSecondi})",
    "scrivi" -> "scrivi(${testo})",
    "impostaCarattere" -> "impostaCarattere(${font})",
    "impostaGrandezzaCarattere" -> "impostaGrandezzaCarattere(${dimensione})",
    "arco2" -> "arco2(${raggio},${angolo})",
    "arco" -> "arco(${raggio},${angolo})",
    "cerchio" -> "cerchio(${raggio})",
    "poligono" -> "poligono(${lato},${lati})",
    "punto" -> "cerchio(${diametro})",
    "visibile" -> "visibile()",
    "invisibile" -> "invisibile()",
    "abbassaPenna" -> "abbassaPenna()",
    "alzaPenna" -> "alzaPenna()",
    "èLaPennaAbbassata" -> "èLaPennaAbbassata",
    "colorePenna" -> "colorePenna(${colore})",
    "coloreRiempimento" -> "coloreRiempimento(${colore})",
    "impostaSpessorePenna" -> "impostaSpessorePenna(${grandezza})",
    "salvaStile" -> "salvaStile()",
    "ripristinaStile" -> "ripristinaStile()",
    "salvaPosizioneDirezione" -> "salvaPosizioneDirezione()",
    "ripristinaPosizioneDirezione" -> "ripristinaPosizioneDirezione()",
    "assi" -> "assi()",
    "rimuoviAssi" -> "rimuoviAssi()",
    "pulisci" -> "pulisci()",
    "pulisciOutput" -> "pulisciOutput()",
    "sfondo" -> "sfondo(${colore})",
    "gradiente" -> "gradiente(${colore1},${colore2})",
    "ripeti" -> "ripeti(${conta}) {\n    ${cursor}\n}",
    "ripetizione" -> "ripetizione(${conta}) { i =>\n    ${cursor}\n}",
    "ripetiFinché" -> "ripetiFinché(${condizione}) {\n    ${cursor}\n}",
    "ripetiPerOgniElementoDi" -> "ripetiPerOgniElementoDi(${sequenza}) { ${e} =>\n    ${cursor}\n}",
    "scriviLinea" -> "scriviLinea(${testo})",
    "leggiLinea" -> "leggiLinea(${pronto})",
    "arrotonda" -> "arrotonda(${numero},${cifre})",
    "numeroCasuale" -> "numeroCasuale(${limitiSuperiori})",
    "numeroDecimaleCasuale" -> "numeroDecimaleCasuale(${limitiSuperiori})",
    "indossaCostume" -> "indossaCostume(${nomeDelFile})",
    "indossaCostumi" -> "indossaCostumi(${nomeDelFile1},${nomeDelFile2})",
    "indossaImmagine" -> "indossaImmagine(${immagine})",
    "indossaImmagini" -> "indossaImmagini(${immagine},${immagine})",
    "indossaImmagini" -> "indossaImmagini(${listaImmagini})",
    "prossimoCostume" -> "prossimoCostume()",
    "scalaCostume" -> "scalaCostume(fattore)",
    "se" -> "se (condizione) {blocco} altrimenti {blocco}",
    "rimuovi" -> "rimuovi()",
    "fai" -> "fai { self => codice }",
    "rifai" -> "rifai { self => codice }",
    "indietro" -> "indietro(${passi})",
    "velocità" -> "velocità(${Velocità})",
    "lentezza" -> "lentezza(${milliSecondi})"
  )

  val helpContent = Map(
    "fai" -> """
      <div>
        <p><strong>fai</strong>{ self => codice }</p>
        <p>
          Permette di definire un blocco di codice da eseguire in una unità di esecuzione separata (thread). Il blocco di codice è eseguito una volta.
          <strong>self</strong> è la tartaruga a cui si riferisce.
        </p>
      </div>
    """,
    "rifai" -> """
      <div>
        <p><strong>rifai</strong>{ self => codice }</p>
        <p>
          Permette di definire un blocco di codice da eseguire in una unità di esecuzione separata (thread). Il blocco di codice è eseguito 30 volte al secondo.
          <strong>self</strong> è la tartaruga a cui si riferisce.
        </p>
      </div>
    """,
    "avanti" -> """
      <div>
        <p><strong>avanti</strong>(passi)</p>
        <p>Sposta la tartaruga in avanti per il numero di passi dati </p>
      </div>
    """,
    "indietro" -> """
      <div>
        <p><strong>indietro</strong>(passi)</p>
        <p>Sposta la tartaruga indietro per il numero di passi dati</p>
      </div>
    """,
    "sinistra" -> """
    <div>
      <p><strong>sinistra</strong>()</p>
      <p>Gira la tartaruga di 90 gradi a sinistra (anti-orario). </p>

      <p><strong>sinistra</strong>(angolo).</p>
      <p>Gira la Tartaruga sinistra (anti-orario) per il dato angolo in gradi </p>

      <p><strong>sinistra</strong>(angolo, raggio)</p>
      <p>Gira la Tartaruga a sinistra (anti-orario) per il dato angolo in gradi, intorno al raggio </p>
    </div>
    """,
    "destra" -> """
    <div>
      <p><strong>destra</strong>()</p>
      <p>Gira la tartaruga di 90 gradi a destra (orario). </p>

      <p><strong>destra</strong>(angolo).</p>
      <p>Gira la Tartaruga destra (orario) per il dato angolo in gradi </p>

      <p><strong>destra</strong>(angolo, raggio)</p>
      <p>Gira la Tartaruga a destra (orario) per il dato angolo in gradi, intorno al raggio </p>
    </div>
    """,
    "saltaVerso" -> """
      <div>
        <p><strong>saltaVerso</strong>(x, y)</p>
        <p>Posiziona la Tartaruga alle coordinate (x, y) senza disegnare Una linea. La direzione della Tartaruga non è cambiata. </p>
      </div>
      """,
    "muoviVerso" -> """
      <div>
        <p><strong>muoviVerso</strong>(x, y)</p>
        <p>Muove la Tartaruga verso il punto di coordinate x, y. </p>
      </div>
      """,
    "cambiaPosizione" -> """
      <div>
        <p><strong>cambiaPosizione</strong>(x, y)</p>
        <p>Cambia la posizione della Tartaruga verso il punto di coordinate x, y. </p>
      </div>
      """,
    "salta" -> """
      <div>
        <p><strong>salta</strong>(passi)</p>
        <p>Sposta la tartaruga in avanti per il numero determinato di passi <em> con la penna </em>,
        senza che che nessuna linea sia disegnata. La penna viene messo giù dopo il salto. </p>
      </div>
      """,
    "casa" -> """
      <div>
        <p><strong>casa</strong>()</p>
        <p>Sposta la tartaruga nella sua posizione originale al centro dello schermo facendola puntare in alto. </p>
      </div>
      """,
    "verso" -> """
      <div>
        <p><strong>verso</strong>(x, y)</p>
        <p>Fa puntare la Tartaruga verso il punto di coordinate x, y. </p>
      </div>
      """,
    "impostaDirezione" -> """
      <div>
        <p><strong>impostaDirezione</strong>(angolo)</p>
        <p>Imposta la direzione della tartaruga all'angolo dato (0 è verso il lato destro dello schermo (<em> est </em>), 90 è su ( <em> nord </em>)). </p>
      </div>
      """,
    "direzione" -> """
      <div>
        <p><strong>direzione</strong>.
        <p>Interroga la direzione della tartaruga (0 è verso il lato destro dello schermo (<em> est </em>), 90 è alto (<em> nord </em>)). </p>
      </div>
      """,
    "est" -> """
      <div>
        <p><strong>est</strong>()</p>
        <p>Gira la Tartaruga verso est </p>
      </div>
      """,
    "ovest" -> """
      <div>
        <p><strong>ovest</strong>()</p>
        <p>Gira la Tartaruga verso ovest </p>
      </div>
      """,
    "nord" -> """
      <div>
        <p><strong>nord</strong>()</p>
        <p>Gira la Tartaruga verso nord </p>
      </div>
      """,
    "sud" -> """
      <div>
        <p><strong>sud</strong>()</p>
        <p>Gira la Tartaruga verso sud </p>
      </div>
      """,
    "ritardo" -> """
      <div>
        <p><strong>ritardo</strong>(valore)</p>
        <p>Imposta la velocità della tartaruga al valore specificato</p>
      </div>
      """,
    "valoreRitardo" -> """
      <div>
        <p><strong>valoreRitardo</strong>.
        <p>Legge la velocità della tartaruga</p>
      </div>
      """,
    "scrivi" -> """
      <div>
        <p><strong>scrivi</strong>(testo)</p>
        <p>Scrive alla posizione della tartaruga, se l'oggetto non è una <em>stringa</em> lo converte. </p>
      </div>
      """,
    "impostaCarattere" -> """
      <div>
        <p><strong>impostaCarattere</strong>(font)</p>
        <p>Imposta il carattere con cui scrive la tartaruga.</p>
      </div>
      """,
    "impostaGrandezzaCarattere" -> """
      <div>
        <p><strong>impostaGrandezzaCarattere</strong>(n)</p>
        <p>Specifica la dimensione del carattere con cui scrive la tartaruga</p>
      </div>
      """,
    "arco2" -> """
      <div>
        <p><strong>arco2</strong>(raggio, angolo)</p>
        <p>Fa fare alla tartaruga un arco con il raggio ed angolo dato.
        Per angoli positivi il giro è anti-orario. </p>
      </div>
      """,
    "arco" -> """
      <div>
        <p><strong>arco</strong>(raggio, angolo)</p>
        <p>Fa fare alla tartaruga un arco con il raggio ed angolo dato.
        Per angoli positivi il giro è anti-orario. </p>
      </div>
      """,
    "cerchio" -> """
      <div>
        <p><strong>cerchio</strong>(raggio)</p>
        <p>La tartaruga si muove in cerchio dato il raggio.
        Il comando cerchio(50) è equivalente al comando arco(50, 360). </p>
      </div>
      """,
    "poligono" -> """
      <div>
        <p><strong>poligono</strong>(lato, lati)</p>
        <p>La tartaruga disegna un poligono. </p>
      </div>
      """,
    "punto" -> """
      <div>
        <p><strong>punto</strong>(diametro)</p>
        <p>Disegna un punto dato il diametro.</p>
      </div>
      """,
    "visibile" -> """
      <div>
        <p><strong>visibile</strong>()</p>
        <p>Rende la tartaruga visibile dopo che è stato resa invisibile con il comando <strong>invisibile</strong>(). </p>
      </div>
      """,
    "invisibile" -> """
      <div>
        <p><strong>invisibile</strong>()</p>
        <p>Rende la tartaruga invisibile. Utilizzare il comando <strong>visibile</strong>() per renderla di nuovo visibile. </p>
      </div>
      """,
    "abbassaPenna" -> """
      <div>
        <p><strong>abbassaPenna</strong>()</p>
        <p>Fa disegnare una linea alla Tartaruga.
        Per impostazione predefinita la penna è abbassata. </p>
      </div>
      """,
    "alzaPenna" -> """
      <div>
        <p><strong>abbassaPenna</strong>()</p>
        <p>Alza la penna della tartaruga, in modo che essa non tracci linee al movimento. </p>
      </div>
      """,
    "èLaPennaAbbassata" -> """
      <div>
        <p><strong>èLaPennaAbbassata</strong>.
        <p>Indica se la penna della tartaruga è abbassata e potrà scrivere. </p>
      </div>
      """,
    "colorePenna" -> """
      <div>
        <p><strong>colorePenna</strong>(colore)</p>
        <p>Specifica il colore della penna con cui disegna la tartaruga. </p>
      </div>
      """,
    "coloreRiempimento" -> """
      <div>
        <p><strong>coloreRiempimento</strong>(colore)</p>
        <p>Specifica il colore di riempimento delle figure disegnate dalla tartaruga. </p>
      </div>
      """,
    "impostaSpessorePenna" -> """
      <div>
        <p><strong>impostaSpessorePenna</strong>(spessore)</p>
        <p>Specifica la larghezza della penna con cui disegna la tartaruga. </p>
      </div>
      """,
    "salvaStile" -> """
      <div>
        <p><strong>salvaStile</strong>()</p>
        <p>Salva lo stile corrente della tartaruga, in modo che possa essere facilmente ripristinato dopo con un <strong>ripristinaStile</strong>(). </p>
        <p> Lo stile della tartaruga include:
          <ul>
            <li> Colore penna </li>
            <li> Spessore penna </li>
            <li> Colore di riempimento </li>
            <li> Carattere </li>
            <li> Lo stato della penna (Su/Giù)</li>
          </ul>
        </p>
      </div>
      """,
    "ripristinaStile" -> """
      <div>
        <p><strong>ripristinaStile</strong>()</p>
        <p>Ripristina lo stile salvato della tartaruga dopo con un <strong>salvaStile</strong>(). </p>
        <p> Lo stile della tartaruga include:
          <ul>
            <li> Colore penna </li>
            <li> Spessore penna </li>
            <li> Colore di riempimento </li>
            <li> Carattere </li>
            <li> Lo stato della penna (Su/Giù)</li>
          </ul>
        </p>
      </div>
      """,
    "salvaPosizioneDirezione" -> """
      <div>
        <p><strong>salvaPosizioneDirezione</strong>()</p>
        <p>Salva la posizione corrente della tartaruga e la direzione, in modo che possano facilmente essere ripristinata
          dopo un <strong>ripristinaPosizioneDirezione</strong>().
        </p>
      </div>
      """,
    "ripristinaPosizioneDirezione" -> """
      <div>
        <p><strong>ripristinaPosizioneDirezione</strong>()</p>
        <p>Ripristina la posizione della tartaruga e la direzione dopo un <strong>salvaPosizioneDirezione</strong>().
        </p>
      </div>
      """,
    "assi" -> """
      <div>
        <p><strong>assi</strong>()</p>
        <p>Visualizza gli assi centrati sulla tartaruga.</p>
        </div>
      """,
    "rimuoviAssi" -> """
      <div>
        <p><strong>rimuoviAssi</strong></p>
        <p>Nasconde gli assi per le tartarughe su cui sono attivi dopo una chiamata a <strong>assi</strong>()</p>
      </div>
      """,
    "pulisci" -> """
      <div>
        <p><strong>pulisci</strong>()</p>
        <p>Cancella l'area di disegno e ne riporta la tartaruga al centro </p>
      </div>
      """,
    "pulisciOutput" -> """
      <div>
        <p><strong>pulisciOutput</strong>()</p>
        <p>Cancella la finestra di output. </p>
      </div>
      """,
    "sfondo" -> """
      <div>
        <p><strong>sfondo</strong>(colore)</p>
        <p>Imposta lo sfondo al colore specificato. </p>
        <p>
          È possibile usare i colori predefiniti per impostare lo sfondo,
          oppure è possibile creare colori utilizzando le funzioni
          <strong> Colore </strong>,
          <strong> ColorHSB </strong>,
          <strong> ColorG </strong>.
        </p>
      </div>
      """,
    "gradiente" -> """
      <div>
        <p><strong>gradiente</strong>(colore1, colore2)</p>
        <p>Imposta lo sfondo per un colore verticale in gradiente, definito dai due colori specificati. </p>
        <p>
          È possibile usare i colori predefiniti per impostare lo sfondo,
          oppure è possibile creare colori utilizzando le funzioni
          <strong> Colore </strong>,
          <strong> ColorHSB </strong>,
          <strong> ColorG </strong>.
        </p>
      </div>
      """,
    "ripeti" -> """
      <div>
        <p><strong>ripeti</strong>(n){ blocco }</p>
        <p>Ripete il blocco specificato di comandi (tra parentesi graffe) n numero di volte </p>
      </div>
      """,
    "ripetizione" -> """
      <div>
        <p><strong>ripetizione</strong>(n) { indice => {blocco}}</p>
        <p>Ripete il blocco specificato di comandi (tra parentesi graffe) n numero di volte.
        L'indice di ripetizione corrente è disponibile come <em> indice </em> all'interno delle parentesi graffe. </p>
      </div>
      """,
    "ripetiFinché" -> """
      <div>
        <p><strong>ripetiFinché</strong>(condizione) { blocco }</p>
        <p>Ripete il blocco specificato di comandi (tra parentesi graffe) finché la condizione data è vera. </p>
      </div>
      """,
    "ripetiPerOgniElementoDi" -> """
      <div>
        <p><strong>ripetiPerOgniElementoDi</strong>(sequenza) { el => { blocco } }</p>
        <p>Ripete il blocco specificato di comandi (tra parentesi graffe) per ogni elemento nella sequenza indicata </p>
      </div>
      """,
    "scriviLinea" -> """
      <div>
        <p><strong>scriviLinea</strong>(oggetto)</p>
        <p>Scrive una linea nella finestra di output andando a capo. </div>
      """,
    "leggiLinea" -> """
      <div>
        <p><strong>leggiLinea</strong>(stringaPrompt)</p>
        <p>Visualizza il prompt indicato nella finestra di output e legge una linea inserita dall'utente.</p>
      </div>
      """,
    "arrotonda" -> """
      <div>
        <p><strong>arrotonda</strong>(numero, cifre)</p>
        <p>Arrotonda il numero per il numero specificato di cifre dopo la virgola </p>
      </div>
      """,
    "numeroCasuale" -> """
      <div>
        <p><strong>numeroCasuale</strong>(limiteSuperiore)</p>
        <p>Restituisce un numero intero casuale compreso tra 0 (incluso) e <em>limiteSuperiore</em> (escluso) </div>
      """,
    "numeroDecimaleCasuale" -> """
      <div>
        <p><strong>numeroCasuale</strong>(limiteSuperiore)</p>
        <p>Restituisce un numero in virgola casuale compreso tra 0 (incluso) e <em>limiteSuperiore</em> (escluso) </div>
      """,
    "indossaCostume" -> """
      <div>
        <p><strong>indossaCostume</strong>(costumeFile)</p>
        <p>Cambia il costume (cioè immagine) associata con la tartaruga con l'immagine nel file specificato. </p>
      </div>
      """,
    "indossaCostumi" -> """
      <div>
        <p><strong>indossaCostumi</strong>(costumeFile1, costumeFile2,...)</p>
        <p>Specifica più costumi per la tartaruga ed imposta il costume della tartaruga al primo nella sequenza.
          È possibile scorrere i costumi chiamando <strong>prossimoCostume</strong>(). </p>
      </div>
      """,
    "indossaImmagine" -> """
      <div>
        <p><strong>indossaImmagine</strong>(immagine)</p>
        <p>Cambia il costume (cioè immagine) associata con la tartaruga con l'immagine nel file specificato. </p>
      </div>
      """,
    "indossaImmagini" -> """
      <div>
        <p><strong>indossaImmagini</strong>(immagine1, immagine2,...)</p>
        <p>Specifica più immagini per la tartaruga ed imposta l'immagine della tartaruga al primo nella sequenza.
          È possibile scorrere i costumi chiamando <strong>prossimoCostume</strong>(). </p>
      </div>
      """,
    "prossimoCostume" -> """
      <div>
        <p><strong>prossimoCostume</strong>()</p>
        <p>Scorre nella sequenza dei costumi specificato da <strong>indossaCostumi</strong>(...) </div>
      """,
    "scalaCostume" -> """
      <div>
        <p><strong>scalaCostume</strong>(fattore)</p>
        <p>Scala il costume della tartaruga.</p>
      </div>
      """,
    "se" -> """
      <div>
        <p><strong>se</strong>(condizione) { blocco} <strong>altrimenti</strong>{ blocco}</p>
        <p> Espressione di controllo. </p>
      </div>
      """,
    "seVero" -> """
      <div>
        <p><strong>seVero</strong>(condizione) { blocco} </p>
        <p> Espressione di controllo. </p>
      </div>
      """,
    "oppure" -> """
      <div>
        <p>valore1 <strong>oppure</strong> valore2</p>
        <p >Se il valore1 è valutato come vuoto verrà restituito il valore2. </p>
      </div>
      """,
    "?:" -> """
      <div>
        <p>valore1 <strong>?:</strong> valore2</p>
        <p>Groovy Elvis Operator, simile a oppure ma lavora su valori nulli.</p>
      </div>
      """,
    "?: ::" -> """
      <div>
        <p><strong>(condizione) ?: (se condizione vera) :: (se condizione falsa)</strong>
      </div>
      """,
    "lentezza" -> """
      <div>
        <p><strong>lentezza</strong>(millisecondi)</p>
        <p>mposta la velocità della tartaruga. La lentezza  specificata. </p>
      </div>
      """,
    "velocità" -> """
      <div>
        <p><strong>velocità</strong>(velocità)</p>
        <p>Imposta la velocità della tartaruga. La velocità  specificata può essere: Lentissima, Lenta, Media, Veloce, Velocissima. </p>
      </div>
      """
  )
}