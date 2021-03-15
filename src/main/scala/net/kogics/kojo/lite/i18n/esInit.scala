/*
 * Copyright (C) 2013-2021
 *   Bjorn Regnell <bjorn.regnell@cs.lth.se>,
 *   Lalit Pant <pant.lalit@gmail.com>
 *   Alberto R.R. Manzanares
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

// Spanish Turtle wrapper for Kojo

package net.kogics.kojo.lite.i18n

import net.kogics.kojo.lite.CoreBuiltins
import net.kogics.kojo.lite.Builtins
import net.kogics.kojo.xscala.RepeatCommands

object SpanishAPI {
  import net.kogics.kojo.core.Turtle
  import java.awt.Color
  var builtins: net.kogics.kojo.lite.CoreBuiltins = _ //unstable reference to module

  trait SpanishTurtle {
    def englishTurtle: Turtle
    def limpiar() = englishTurtle.clear()
    def visible() = englishTurtle.visible()
    def invisible() = englishTurtle.invisible()
    def adelante(pasos: Double) = englishTurtle.forward(pasos)
    def adelante() = englishTurtle.forward(25)
    def derecha(angulo: Double) = englishTurtle.right(angulo)
    def derecha() = englishTurtle.right(90)
    def izquierda(angulo: Double) = englishTurtle.left(angulo)
    def izquierda() = englishTurtle.left(90)
    def saltoA(x: Double, y: Double) = englishTurtle.jumpTo(x, y)
    def moverA(x: Double, y: Double) = englishTurtle.moveTo(x, y)
    def salto(n: Double) = {
      englishTurtle.saveStyle() //to preserve pen state
      englishTurtle.hop(n) //hop change state to penDown after hop
      englishTurtle.restoreStyle()
    }
    def salto(): Unit = salto(25)
    def inicio() = englishTurtle.home()
    def hacia(x: Double, y: Double) = englishTurtle.towards(x, y)
    def establecerRumbo(angulo: Double) = englishTurtle.setHeading(angulo)
    def rumbo = englishTurtle.heading
    def este() = englishTurtle.setHeading(0)
    def oeste() = englishTurtle.setHeading(180)
    def norte() = englishTurtle.setHeading(90)
    def sur() = englishTurtle.setHeading(-90)
    def establecerRetardoAnimacion(n: Long) = englishTurtle.setAnimationDelay(n)
    def escribir(t: Any) = englishTurtle.write(t)
    def establecerTamañoFuentePluma(tamaño: Int) = englishTurtle.setPenFontSize(tamaño)
    def arco(radio: Double, angulo: Double) = englishTurtle.arc(radio, math.round(angulo).toInt)
    def circulo(radio: Double) = englishTurtle.circle(radio)
    def posicion = englishTurtle.position
    def plumaHaciaAbajo() = englishTurtle.penDown()
    def plumaHaciaArriba() = englishTurtle.penUp()
    def estadoPluma = englishTurtle.style.down
    def establecerColorPluma(color: java.awt.Color) = englishTurtle.setPenColor(color)
    def establecerColorRelleno(color: java.awt.Color) = englishTurtle.setFillColor(color)
    def establecerGrosorPluma(n: Double) = englishTurtle.setPenThickness(n)
    def guardarEstilo() = englishTurtle.saveStyle()
    def restaurarEstilo() = englishTurtle.restoreStyle()
    def guardarPosRum() = englishTurtle.savePosHe()
    def restaurarPosRum() = englishTurtle.restorePosHe()
    def cruzOn() = englishTurtle.beamsOn()
    def cruzOff() = englishTurtle.beamsOff()
    def establecerDisfraz(nombreFichero: String) = englishTurtle.setCostume(nombreFichero)
    def establecerDisfraces(nombreFichero: String*) = englishTurtle.setCostumes(nombreFichero: _*)
    def siguienteDisfraz() = englishTurtle.nextCostume()
  }
  class Tortuga(override val englishTurtle: Turtle) extends SpanishTurtle {
    def this(startX: Double, startY: Double, costumeFileName: String) = this(builtins.TSCanvas.newTurtle(startX, startY, costumeFileName))
    def this(startX: Double, startY: Double) = this(startX, startY, "/images/turtle32.png")
    def this() = this(0, 0)
  }
  class Tortuga0(t0: => Turtle) extends SpanishTurtle { //by-name construction as turtle0 is volatile }
    override def englishTurtle: Turtle = t0
  }
  object tortuga extends Tortuga0(builtins.TSCanvas.turtle0)
  def limpiar() = builtins.TSCanvas.clear()
  def limpiarSalida() = builtins.clearOutput()
  lazy val azul = builtins.blue
  lazy val rojo = builtins.red
  lazy val amarillo = builtins.yellow
  lazy val verde = builtins.green
  lazy val morado = builtins.purple
  lazy val rosa = builtins.pink
  lazy val marrón = builtins.brown
  lazy val negro = builtins.black
  lazy val blanco = builtins.white
  lazy val sinColor = builtins.noColor
  def establecerFondo(c: Color) = builtins.setBackground(c)
  def establecerFondoV(c1: Color, c2: Color) = builtins.TSCanvas.setBackgroundV(c1, c2)

  //  object KcSwe { //Key codes for Swedish keys
  //    lazy val VK_Å = 197
  //    lazy val VK_Ä = 196
  //    lazy val VK_Ö = 214
  //  }

  //loops
  def repetir(n: Int)(block: => Unit): Unit = {
    RepeatCommands.repeat(n) { block }
  }

  def repetiri(n: Int)(block: Int => Unit): Unit = {
    RepeatCommands.repeati(n) { i => block(i) }
  }

  def repetirMientras(condición: => Boolean)(block: => Unit): Unit = {
    RepeatCommands.repeatWhile(condición) { block }
  }

  def repetirDurante[T](secuencia: Iterable[T])(block: T => Unit): Unit = {
    RepeatCommands.repeatFor(secuencia) { block }
  }

  //simple IO
  def leerln(prompt: String = "") = builtins.readln(prompt)

  def imprimirln(data: Any) = println(data) //Transferred here from sv.tw.kojo.
  def imprimirln() = println()

  //math functions
  def redondear(numero: Number, digito: Int = 0): Double = {
    val faktor = math.pow(10, digito).toDouble
    math.round(numero.doubleValue * faktor).toLong / faktor
  }
  def aleatorio(cotaSuperior: Int) = builtins.random(cotaSuperior)
  def aleatorioDouble(cotaSuperior: Int) = builtins.randomDouble(cotaSuperior)
}

object SpanishInit {
  def init(builtins: CoreBuiltins): Unit = {
    //initialize unstable value
    SpanishAPI.builtins = builtins
    builtins match {
      case b: Builtins =>
        println("¡Bienvenido a Kojo con la Tortuga Sueca!")
        if (b.isScratchPad) {
          println("El historial no se guardará cuando cierres el cuaderno de Kojo.")
        }

        //        b.setEditorTabSize(2)

        //code completion
        b.addCodeTemplates(
          "es",
          codeTemplates
        )
        //help texts
        b.addHelpContent(
          "es",
          helpContent
        )

      case _ =>
    }
  }

  val codeTemplates = Map(
    "adelante" -> "adelante(${pasos})",
    "derecha" -> "derecha(${angulo})",
    "izquierda" -> "izquierda(${angulo})",
    "saltoA" -> "saltoA(${x},${y})",
    "moverA" -> "moverA(${x},${y})",
    "salto" -> "salto(${pasos})",
    "inicio" -> "inicio()",
    "hacia" -> "hacia(${x},${y})",
    "establecerRumbo" -> "establecerRumbo(${angulo})",
    "este" -> "este()",
    "oeste" -> "oeste()",
    "norte" -> "norte()",
    "sur" -> "sur()",
    "establecerRetardoAnimacion" -> "establecerRetardoAnimacion(${miliSegundos})",
    "escribir" -> "escribir(${texto})",
    "establecerTamañoFuentePluma" -> "establecerTamañoFuentePluma(${tamaño})",
    "arco" -> "arco(${radio},${angulo})",
    "circulo" -> "circulo(${radio})",
    "visible" -> "visible()",
    "invisible" -> "invisible()",
    "plumaHaciaAbajo" -> "plumaHaciaAbajo()",
    "plumaHaciaArriba" -> "plumaHaciaArriba()",
    "estadoPluma" -> "estadoPluma",
    "establecerColorPluma" -> "establecerColorPluma(${color})",
    "establecerColorRelleno" -> "establecerColorRelleno(${color})",
    "establecerGrosorPluma" -> "establecerGrosorPluma(${ancho})",
    "guardarEstilo" -> "guardarEstilo()",
    "restaurarEstilo" -> "restaurarEstilo()",
    "guardarPosRum" -> "guardarPosRum()",
    "restaurarPosRum" -> "restaurarPosRum()",
    "cruzOn" -> "cruzOn()",
    "cruzOff" -> "cruzOff()",
    "limpiar" -> "limpiar()",
    "limpiarSalida" -> "limpiarSalida()",
    "establecerFondo" -> "establecerFondo(${color})",
    "establecerFondoV" -> "establecerFondoV(${color1},${color2})",
    "repetir" -> "repetir(${contador}) {\n    ${cursor}\n}",
    "repetiri" -> "repetiri(${contador}) { i =>\n    ${cursor}\n}",
    "repetirMientras" -> "repetirMientras(${condición}) {\n    ${cursor}\n}",
    "repetirDurante" -> "repetirDurante(${secuencia}) { ${e} =>\n    ${cursor}\n}",
    "imprimirln" -> "imprimirln(${texto})",
    "leerln" -> "leerln(${prompt})",
    "redondear" -> "redondear(${numero},${digito})",
    "aleatorio" -> "aleatorio(${cotaSuperior})",
    "aleatorioDouble" -> "aleatorioDouble(${cotaSuperior})",
    "establecerDisfraz" -> "establecerDisfraz(${nombreFichero})",
    "establecerDisfraces" -> "establecerDisfraces(${nombreFichero1},${nombreFichero2})",
    "siguienteDisfraz" -> "siguienteDisfraz()"
  )

  val helpContent = Map(
    "adelante" -> <div><strong>adelante</strong>(numPasos) - Mueve la tortuga adelante el número de pasos introducido.</div>.toString,
    "izquierda" -> <div> <strong>izquierda</strong>() - Gira la tortuga 90 grados a la izquierda (sentido antihorario). <br/> <strong>izquierda</strong>(angulo) - Gira la tortuga a la izquierda (sentido antihorario) hasta el ángulo dado en grados.<br/> <strong>izquierda</strong>(angulo, radio) - Gira la tortuga a la izquierda (sentido antihorario) hasta el ángulo dado en grados, alrededor del radio de giro dado.<br/> </div>.toString,
    "derecha" -> <div> <strong>derecha</strong>() - Gira la tortuga 90 grados a la derecha (sentido antihorario). <br/> <strong>derecha</strong>(angulo) - Gira la tortuga a la derecha (sentido antihorario) hasta el ángulo dado en grados.<br/> <strong>derecha</strong>(angulo, radio) - Gira la tortuga a la derecha (sentido antihorario) hasta el ángulo dado en grados, alrededor del radio de giro dado.<br/> </div>.toString,
    "saltoA" -> <div> <strong>saltarA</strong>(x, y) - Coloca la tortuga en el punto (x, y) sin dibujar una línea. Places the turtle at the point (x, y) without drawing a line. La dirección de la tortuga no cambia. <br/> </div>.toString,
    "moverA" -> <div><strong>moverA</strong>(x, y) - Gira la tortuga hacia (x, y) y la mueve a ese punto.</div>.toString,
    "salto" -> <div> <strong>salto</strong>(numPasos) - Mueve la tortuga adelante el número de pasos dado <em>con la pluma hacia arriba</em>, de manera que no se dibuja ninguna línea. La pluma se deja después del salto. <br/> </div>.toString,
    "inicio" -> <div><strong>inicio</strong>() - Mueve la tortuga a su ubicación original en el centro de la pantalla y mirando hacia el norte.</div>.toString,
    "hacia" -> <div><strong>hacia</strong>(x, y) - Gira la tortuga hacia el punto (x, y).</div>.toString,
    "establecerRumbo" -> <div><strong>establecerDireccion</strong>(angulo) - Establece la dirección de la tortuga hacia el ángulo dado (0 sería hacia la parte derecha de la pantalla (<em>este</em>), 90 sería hacia la parte de arriba (<em>norte</em>)).</div>.toString,
    "rumbo" -> <div><strong>direccion</strong> - Consulta la dirección de la tortuga (0 sería hacia la parte derecha de la pantalla (<em>este</em>), 90 sería hacia la parte de arriba (<em>norte</em>)).</div>.toString,
    "este" -> <div><strong>este</strong>() - Gira la tortuga hacia el este.</div>.toString,
    "oeste" -> <div><strong>oeste</strong>() - Gira la tortuga hacia el oeste.</div>.toString,
    "norte" -> <div><strong>norte</strong>() - Gira la tortuga hacia el norte.</div>.toString,
    "sur" -> <div><strong>sur</strong>() - Gira la tortuga hacia el sur.</div>.toString,
    "establecerRetardoAnimacion" -> <div> <strong>establecerRetardoAnimacion</strong>(retardo) - Establece la velocidad de la tortuga. El retardo especificado es la cantidad de tiempo (en milisegundos) que tarda la tortuga en moverse cien pasos.<br/> El retardo por defecto son 1000 milisegundos(o 1 segundo).<br/> </div>.toString,
    "escribir" -> <div><strong>escribir</strong>(obj) - Hace que la tortuga escriba, en su ubicación actual, el objeto especificado como string.</div>.toString,
    "establecerTamañoFuentePluma" -> <div><strong>establecerTamañoFuentePluma</strong>(n) - Especifica el tamaño de la fuente de la pluma con la que la tortuga escribe.</div>.toString,
    "arco" -> <div> <strong>arco</strong>(radio, angulo) - Hace que la tortuga dibuje un arco con el radio y ángulo dados.<br/> Ángulos positivos hacen que la tortuga lo cree hacia la izquierda (sentido antihorario). Ángulos negativos hace que la tortuga lo cree hacia la derecha (sentido horario) <br/> </div>.toString,
    "circulo" -> <div> <strong>circulo</strong>(radio) - Hace que la tortuga dibuje un círculo con el radio dado. <br/> Un comando circulo(50) es equivalente a un comando arco(50, 360).<br/> </div>.toString,
    "visible" -> <div><strong>visible</strong>() - Hace a la tortuga visible después de que se haya hecho invisible con el comando invisible().</div>.toString,
    "invisible" -> <div><strong>invisible</strong>() - Hace a la tortuga invisible. Usa el comando visible() para hacerla visible de nuevo.</div>.toString,
    "plumaHaciaAbajo" -> <div> <strong>plumaHaciaAbajo</strong>() - Pone la pluma de la tortuga hacia abajo, de manera que se dibuja una línea a medida que la tortuga se mueve.<br/> Por defecto la pluma de la tortuga está hacia abajo. <br/> </div>.toString,
    "plumaHaciaArriba" -> <div><strong>plumaHaciaArriba</strong>() - Pone la pluma de la tortuga hacia arriba, de manera que no se dibuja una línea a medida que la tortuga se mueve. <br/></div>.toString,
    "estadoPluma" -> <div><strong>estadoPluma</strong> - Te dice si la pluma está hacia abajo o no.</div>.toString,
    "establecerColorPluma" -> <div><strong>establecerColorPluma</strong>(color) - Especifica el color de la pluma con la que la tortuga dibuja. <br/></div>.toString,
    "establecerColorRelleno" -> <div><strong>establecerColorRelleno</strong>(color) - Especifica el color de relleno de las figuras que dibuja la tortuga. <br/></div>.toString,
    "establecerGrosorPluma" -> <div><strong>establecerGrosorPluma</strong>(grosor) - Especifica el ancho de la pluma con la que la tortuga dibuja. <br/></div>.toString,
    "guardarEstilo" -> <div> <strong>guardarEstilo</strong>() - Guarda el estilo actual de la tortuga, para que pueda ser restaurado fácilmente más adelante con <tt>restaurarEstilo()</tt>. <br/> <p> El estilo de la tortuga incluye: <ul> <li>Color de la pluma</li> <li>Groso de la pluma</li> <li>Color de relleno</li> <li>Fuente de la pluma</li> <li>Estado de la pluma (arriba/abajo)</li> </ul> </p> </div>.toString,
    "restaurarEstilo" -> <div> <strong>restaurarEstilo</strong>() - Restaura el estilo de la tortuga guardado anteriormente con <tt>guardarEstilo()</tt>. <br/> <p> El estilo de la tortuga incluye: <ul> <li>Color de la pluma</li> <li>Groso de la pluma</li> <li>Color de relleno</li> <li>Fuente de la pluma</li> <li>Estado de la pluma (arriba/abajo)</li> </ul> </p> </div>.toString,
    "guardarPosRum" -> <div> <strong>guardarPosRum</strong>() - Guarda la posición y rumbo actuales de la tortuga, de manera que pueda ser fácilmente restaurado con <tt>restaurarPosRum()</tt>. <br/> </div>.toString,
    "restaurarPosRum" -> <div> <strong>restaurarPosRum</strong>() - Restaura la posición y el rumbo de la tortuga guardado anteriormente con <tt>savePosHe()</tt>. <br/> </div>.toString,
    "cruzOn" -> <div><strong>cruzOn</strong>() - Muestra una cruz centrada en la tortuga que ayuda a pensar sobre el rumbo/orientación de la tortuga.</div>.toString,
    "cruzOff" -> <div><strong>cruzOff</strong>() - Esconde la cruz de la tortuga que se mostró con  <tt>cruzOn()</tt>.</div>.toString,
    "limpiar" -> <div><strong>limpiar</strong>() - Limpia el lienzo de la tortuga y la coloca en el centro del mismo.</div>.toString,
    "limpiarSalida" -> <div><strong>limpiarSalida</strong>() - Limpia la ventana de salida.</div>.toString,
    "establecerFondo" -> <div> <strong>establecerFondo</strong>(color) - Establece el fondo del lienzo al color especificado. Puedes usar colores predefinidos para el fondo o puedes crear los tuyos propios usando las funciones <tt>Color</tt>, <tt>ColorHSB</tt>, y <tt>ColorG</tt>. </div>.toString,
    "establecerFondoV" -> <div><strong>establecerFondoV</strong>(color1, color2) - Establece el fondo del lienzo a un color vertical degradado definido por los dos colores especificados. </div>.toString,
    "repetir" -> <div><strong>repetir</strong>(n){{ }} - Repite el bloque de comandos especificado (entre llaves) n veces.<br/></div>.toString,
    "repetiri" -> <div><strong>repetiri</strong>(n) {{i => }} - Repite el bloque de comandos especificado (entre llaves) n veces. El índice de repetición actual está disponible como <tt>i</tt> en las llaves. </div>.toString,
    "repetirMientras" -> <div><strong>repetirMientras</strong>(cond) {{ }} - Repite el bloque de comandos especificado (entre llaves) mientras la condición dada sea cierta.</div>.toString,
    "repetirDurante" -> <div><strong>repetirDurante</strong>(sec){{ }} - Repite el bloque de comandos especificado (entre llaves) para cada elemento en la secuencia dada.<br/></div>.toString,
    "imprimirln" -> <div><strong>imprimirln</strong>(obj) - Muestra el objeto pasado como una string en la ventana de salida, con un fin de línea al final.</div>.toString,
    "leerln" -> <div><strong>readln</strong>(promptString) - Muestra la entrada dada en la ventana de salida y lee una línea que el usuario introduce.</div>.toString,
    "redondear" -> <div><strong>redondear</strong>(n, digitos) - Redondea el número dado (n) con el número de decimales especificado.</div>.toString,
    "aleatorio" -> <div><strong>aleatorio</strong>(cotaSuperior) - Devuelve un entero aleatorio entre 0 (incluido) y el especificado como  cotaSuperior (no incluido).</div>.toString,
    "aleatorioDouble" -> <div><strong>aleatorioDouble</strong>(cotaSuperior) - Devuelve un número decimal en double-precision entre 0 (incluido) y el especificado como cotaSuperior (no incluido). </div>.toString,
    "establecerDisfraz" -> <div><strong>establecerDisfraz</strong>(ficheroDisfraz) - Cambia el disfraz (i.e. imagen) asociado a la tortuga con el especificado en el fichero dado.</div>.toString,
    "establecerDisfraces" -> <div><strong>establecerDisfraces</strong>(ficheroDisfraz1, ficheroDisfraz2, ...) - Especifica múltiples disfraces para la tortuga, y establece el disfraz de la tortuga actual con el primero en la secuencia. Puedes acceder a los siguientes disfraces usando <tt>siguienteDisfraz()</tt>. </div>.toString,
    "siguienteDisfraz" -> <div><strong>siguienteDisfraz</strong>() - Cambia el disfraz actual de la tortuga por el siguiente en la secuencia especificada en <tt>establecerDisfraces(...)</tt>.</div>.toString
  )
}
