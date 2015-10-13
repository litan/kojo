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

import net.kogics.kojo.lite.CoreBuiltins
import net.kogics.kojo.lite.Builtins
import net.kogics.kojo.xscala.RepeatCommands
import net.kogics.kojo.core.Voice

object ItalianAPI {
  import net.kogics.kojo.core.Turtle
  import java.awt.Color
  var builtins: net.kogics.kojo.lite.CoreBuiltins = _ //unstable reference to module

  trait ItalianTurtle {
    def englishTurtle: Turtle
    def pulisci() = englishTurtle.clear()
    def visibile() = englishTurtle.visible()
    def invisibile() = englishTurtle.invisible()
    def avanti(passi: Double) = englishTurtle.forward(passi)
    def avanti() = englishTurtle.forward(25)
    def destra(angolo: Double) = englishTurtle.right(angolo)
    def destra() = englishTurtle.right(90)
    def sinistra(angolo: Double) = englishTurtle.left(angolo)
    def sinistra() = englishTurtle.left(90)
    def saltaVerso(x: Double, y: Double) = englishTurtle.jumpTo(x, y)
    def muoviVerso(x: Double, y: Double) = englishTurtle.moveTo(x, y)
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
    def ritardo(n: Long) = englishTurtle.setAnimationDelay(n)
    def scrivi(t: Any) = englishTurtle.write(t)
    def impostaGrandezzaCarattere(dimensione: Int) = englishTurtle.setPenFontSize(dimensione)
    def arco(raggio: Double, angolo: Double) = englishTurtle.arc(raggio, math.round(angolo).toInt)
    def cerchio(raggio: Double) = englishTurtle.circle(raggio)
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
    def indossaCostume(nomeDelFile: String) = englishTurtle.setCostume(nomeDelFile)
    def indossaCostumi(nomeDelFile: String*) = englishTurtle.setCostumes(nomeDelFile: _*)
    def prossimoCostume() = englishTurtle.nextCostume()
    def suona(voce: Voice) = englishTurtle.playSound(voce)

    def superficie = englishTurtle.area
    def perimetro = englishTurtle.perimeter
  }
  class Tartaruga(override val englishTurtle: Turtle) extends ItalianTurtle {
    def this(startX: Double, startY: Double, costumeFileName: String) = this(builtins.TSCanvas.newTurtle(startX, startY, costumeFileName))
    def this(startX: Double, startY: Double) = this(startX, startY, "/images/turtle32.png")
    def this() = this(0, 0)
  }
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
  def ripeti(n: Int)(block: => Unit) {
    RepeatCommands.repeat(n) { block }
  }

  def ripetizione(n: Int)(block: Int => Unit) {
    RepeatCommands.repeati(n) { i => block(i) }
  }

  def ripetiFinché(condizione: => Boolean)(block: => Unit) {
    RepeatCommands.repeatWhile(condizione) { block }
  }

  def ripetiPerOgniElementoDi[T](sequenza: Iterable[T])(block: T => Unit) {
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
}

object ItInit {
  def init(builtins: CoreBuiltins) {
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
    "impostaGrandezzaCarattere" -> "impostaGrandezzaCarattere(${dimensione})",
    "arco" -> "arco(${raggio},${angolo})",
    "cerchio" -> "cerchio(${raggio})",
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
    "prossimoCostume" -> "prossimoCostume()"
  )

  val helpContent = Map(
    "avanti" -> (<div> <strong> Avanti </strong> (passi). Sposta la tartaruga in avanti per il numero di passi dati </div>).toString,
    "sinistra" -> (<div> <strong> sinistra </strong> sinistra(). Gira la tartaruga di 90 gradi a sinistra (anti-orario). <br/> <strong> sinistra </strong> sinistra(angolo). Gira la Tartaruga sinistra (anti-orario) per il date angolo in gradi <br/> <strong> sinistra </strong> sinistra(angolo, Raggio). Gira la Tartaruga a sinistra (anti-orario) per il date angolo in gradi, Intorno al Raggio <br/> </div>).toString,
    "destra" -> (<div> <strong> destra </strong> destra(). Gira La Tartaruga 90 gradi a destra (orario). <br/> <strong> destra </strong> destra(angolo). Gira la Tartaruga a destra (orario) per il date angolo in gradi <br/> <strong> destra </strong> destra(angolo, Raggio). Gira La Tartaruga destra (orario) per il date angolo in gradi, Intorno al Raggio <br/></div>).toString,
    "saltaVerso" -> (<div> <strong> saltaVerso </strong> saltaVerso(x, y). posiziona la Tartaruga alle coordinate (x, y) senza disegnare Una linea. La direzione della Tartaruga non è cambiata. <br/> </div>).toString,
    "muoviVerso" -> (<div> <strong> muoviVerso </strong> muoviVerso(x, y). Gira la Tartaruga verso (x, y) e si muove la tartaruga a quel punto </div>).toString,
    "salta" -> (<div> <strong> salta </strong> salta(passi). Sposta la tartaruga in avanti per il numero determinato di passi <em> con la penna </em>, senza che che nessuna linea sia disegnata. La penna viene messo giù dopo il salto. <br/> </div>).toString,
    "casa" -> (<div> <strong> casa </strong> casa(). Sposta la tartaruga nella sua posizione originale al centro dello schermo e fa puntare a nord </div>).toString,
    "verso" -> (<div> <strong> verso </strong> verso(x, y). Gira la Tartaruga verso il punto (x, y) </div>).toString,
    "impostaDirezione" -> (<div> <strong> impostaDirezione </strong> impostaDirezione(angolo). Imposta la direzione della tartaruga al angolo dato (0 è verso il lato destra dello schermo (<em> est </em>), 90 è su ( <em> nord </em>)). </div>).toString,
    "direzione" -> (<div> <strong> direzione </strong>. Interroga la direzione della tartaruga (0 è verso il lato destra dello schermo (<em> est </em>), 90 è alto (<em> nord </em >)). </div>).toString,
    "est" -> (<div> <strong> est </strong> est(). Gira la Tartaruga verso est </div>).toString,
    "ovest" -> (<div> <strong> ovest </strong> ovest(). Gira la Tartaruga verso ovest </div>).toString,
    "nord" -> (<div> <strong> nord </strong> nord(). Gira la Tartaruga verso nord </div>).toString,
    "sud" -> (<div> <strong> sud </strong> sud(). Gira la Tartaruga verso sud </div>).toString,
    "ritardo" -> (<div> <strong> ritardo </strong> (ritardo). Imposta la velocità della tartaruga. Il ritardo  specificato</div>).toString,
    "scrivi" -> (<div> <strong> scrivi </strong> scrivi(oggetto). Scrive alla posizione della tartaruga, se l'oggetto non è una stringa lo converte. </div>).toString,
    "impostaGrandezzaCarattere" -> (<div> <strong> impostaGrandezzaPenna </strong> (n). Specifica la dimensione del carattere con cui scrive la tartaruga</div>).toString,
    "arco" -> (<div> <strong> arco </strong> arco(raggio, angolo). Fa fare alla tartaruga un arco con il raggio ed angolo dato <br/> per angoli positivi il giro è anti-orario. <br/> </div>).toString,
    "cerchio" -> (<div> <strong> cerchio </strong> cerchio(raggio). La tartaruga si muove in cerchio dato il raggio. <br/> Il comando cerchio(50) è equivalente al comando arco(50, 360). <br/> </div>).toString,
    "visibile" -> (<div> <strong> visibile </strong> visibile(). Rende la tartaruga visibile dopo che è stato resa invisibile con il comando invisibile() </div>).toString,
    "invisibile" -> (<div> <strong> invisibile </strong> invisibile(). rende la tartaruga invisibile. Utilizzare il comando visibile() per renderla di nuovo visibile. </div>).toString,
    "abbassaPenna" -> (<div> <strong> abbassaPenna </strong> abbassaPenna(). Fa disegnare una linea alla Tartaruga <br/> Per impostazione predefinita la penna è abbassata. <br/> </div>).toString,
    "alzaPenna" -> (<div> <strong> abbassaPenna </strong> abbassaPenna(). Alza la penna della tartaruga, in modo che essa non tracci linee al movimento. <br/> </div>).toString,
    "èLaPennaAbbassata" -> (<div> <strong> èLaPennaAbbassata </strong>. Indica se la penna della tartaruga è giù </div>).toString,
    "colorePenna" -> (<div> <strong> colorePenna </strong> (colore). Specifica il colore della penna che la tartaruga disegna con. <br/> </div>).toString,
    "coloreRiempimento" -> (<div> <strong> coloreRiempimento </strong> (colore). Specifica il colore di riempimento delle figure disegnate dalla tartaruga. <br/> </div>).toString,
    "impostaSpessorePenna" -> (<div> <strong> impostaSpessorePenna </strong> (spessore). Specifica la larghezza della penna che la tartaruga disegna con. <br/> </div>).toString,
    "salvaStile" -> (<div> <strong> salvaStile </strong> salvaStile(). Salva lo stile corrente della tartaruga, in modo che possa facilmente essere ripristinato dopo con un <tt> restoreStyle() </tt>. <br/> <p> Lo stile della tartaruga include: <ul> <li> Colore penna </li> <li> Spessore penna </li> <li> Colore di riempimento </li> <li> Carattere </li> <li> Penna Su / Giù stato </li> </ul> </p> </div>).toString,
    "ripristinaStile" -> (<div> <strong> ripristinaStile </strong> ripristinaStile(). Ripristina stile della tartaruga sulla base di una precedente <tt> saveStyle() </tt>. <br/> <p> Lo stile della tartaruga include: <ul> <li> Colore penna </li> <li> Spessore penna </li> <li> Colore di riempimento </li> <li> Carattere </li> <li> Penna Su / Giù stato </li> </ul> </p> </div>).toString,
    "salvaPosizioneDirezione" -> (<div> <strong> salvaPosizioneDirezione </strong> salvaPosizioneDirezione(). Salva la posizione corrente della tartaruga e la direzione, in modo che possano facilmente essere ripristinata dopo con un <tt> ripristinaPosizioneDirezione() </tt>. <br/> </div>).toString,
    "ripristinaPosizioneDirezione" -> (<div> <strong> ripristinaPosizioneDirezione </strong> ripristinaPosizioneDirezione(). ripristina la posizione corrente della tartaruga e la direzionesulla base di una precedente chiamata a <tt> salvaPosizioneDirezione() </tt>. <br/> </div>).toString,
    "assi" -> (<div> <strong> assi </strong> assi(). Indica assi centrati sulla tartaruga.  </div>).toString,
    "rimuoviAssi" -> (<div> <strong> rimuoviAssi </strong> (rimuoviAssi). Nasconde gli assi per le tartarughe su cui sono attivi dopo una chiamata a <tt> assi() </tt> </div>).toString,
    "pulisci" -> (<div> <strong> pulisci </strong> pulisci(). Cancella la tela tartaruga, e porta la tartaruga al centro della tela </div>).toString,
    "pulisciOutput" -> (<div> <strong> pulisciOutput </strong> pulisciOutput(). Cancella la finestra di output </div>).toString,
    "sfondo" -> (<div> <strong> sfondo </strong> sfondo(colore). Imposta lo sfondo al colore specificato. È possibile usare i colori predefiniti per impostare lo sfondo, oppure è possibile creare colori utilizzando le funzioni <tt> Colore </tt>, <tt> ColorHSB </tt>, <tt> ColorG </tt>. </div>).toString,
    "gradiente" -> (<div> <strong> gradiente </strong> gradiente(color1, color2). Imposta lo sfondo per un colore verticale in gradiente, definito dai due colori specificati. </div>).toString,
    "ripeti" -> (<div> <strong> ripeti </strong> ripeti(n) {{}}. Ripete il blocco specificato di comandi (tra parentesi graffe) n numero di volte <br/> </div>).toString,
    "ripetizione" -> (<div> <strong> ripetizione </strong> ripetizione(n) {{indice =>}}. Ripete il blocco specificato di comandi (tra parentesi graffe) n numero di volte. L'indice di ripetizione corrente è disponibile come <tt> indice </tt> all'interno delle parentesi graffe. </div>).toString,
    "ripetiFinché" -> (<div> <strong> ripetiFinché </strong> ripetiFinché(cond) {{}}. Ripete il blocco specificato di comandi (tra parentesi graffe), mentre la condizione data è vera </div>).toString,
    "ripetiPerOgniElementoDi" -> (<div> <strong> ripetiPer </strong> ripetiPerOgniElementoDi(ss) {{}}. Ripete il blocco specificato di comandi (tra parentesi graffe) per ogni elemento nella sequenza indicata <br/> </div>).toString,
    "scriviLinea" -> (<div> <strong> scriviLinea </strong> (Oggetto). Consente di visualizzare la data oggettoect come una stringa nella finestra di output, con una nuova riga alla fine </div>).toString,
    "leggiLinea" -> (<div> <strong> leggiLinea </strong> (stringaPrompt). Visualizza il prompt data nella finestra di output e legge una linea che l'utente inserisce </div>).toString,
    "arrotonda" -> (<div> <strong> arrotonda </strong> (n, cifre). Arrotonda il numero specificato n per il numero specificato di cifre dopo la virgola </div>).toString,
    "numeroCasuale" -> (<div> <strong> numeroCasuale </strong> (upper bound). Restituisce un numero intero casuale compreso tra 0 (incluso) e upper bound (esclusiva) </div>).toString,
    "numeroDecimaleCasuale" -> (<div> <strong> numeroDecimaleCasuale </strong> (upper bound). Restituisce un numero casuale a doppia precisione decimale compreso tra 0 (incluso) e upper bound (esclusiva) </div>).toString,
    "indossaCostume" -> (<div> <strong> indossaCostume </strong> (costumeFile). Cambia il costume (cioè immagine) associata con la tartaruga per l'immagine nel file specificato </div>).toString,
    "indossaCostumi" -> (<div> <strong> indossaCostumi </strong> (costumeFile1, costumeFile2,...). Specifica multiple costumi per la tartaruga, e imposta costume della tartaruga al primo nella sequenza. È possibile scorrere i costumi chiamando <tt> nextCostume() </tt>. </div>).toString,
    "prossimoCostume" -> (<div> <strong> prossimoCostume </strong> prossimoCostume(). cambi di costume della tartaruga a quella successiva nella sequenza dei costumi specificato da <tt> setCostumes (..) </tt> </div>).toString
  )
}

