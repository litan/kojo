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

import net.kogics.kojo.lite.Builtins
import net.kogics.kojo.lite.CoreBuiltins
import net.kogics.kojo.xscala.RepeatCommands

object RussianAPI {
  import java.awt.Color

  import net.kogics.kojo.core.Turtle
  var builtins: net.kogics.kojo.lite.CoreBuiltins = _ // unstable reference to module

  trait RussianTurtle {
    def englishTurtle: Turtle
    def очисти() = englishTurtle.clear()
    def покажись() = englishTurtle.visible()
    def спрячься() = englishTurtle.invisible()
    def вперёд(шаги: Double) = englishTurtle.forward(шаги)
    def вперёд() = englishTurtle.forward(25)
    def направо(угол: Double) = englishTurtle.right(угол)
    def направо() = englishTurtle.right(90)
    def налево(угол: Double) = englishTurtle.left(угол)
    def налево() = englishTurtle.left(90)
    def перепрыгни(x: Double, y: Double) = englishTurtle.jumpTo(x, y)
    def иди(x: Double, y: Double) = englishTurtle.moveTo(x, y)
    def прыгни(n: Double) = {
      englishTurtle.saveStyle() // to preserve pen state
      englishTurtle.hop(n) // hop change state to penDown after hop
      englishTurtle.restoreStyle()
    }
    def прыгни(): Unit = прыгни(25)
    def дом() = englishTurtle.home()
    def направление(x: Double, y: Double) = englishTurtle.towards(x, y)
    def курс_установи(угол: Double) = englishTurtle.setHeading(угол)
    def курс = englishTurtle.heading
    def восток() = englishTurtle.setHeading(0)
    def запад() = englishTurtle.setHeading(180)
    def север() = englishTurtle.setHeading(90)
    def юг() = englishTurtle.setHeading(-90)
    def задержка(n: Long) = englishTurtle.setAnimationDelay(n)
    def напиши(t: Any) = englishTurtle.write(t)
    def размер_шрифта(размер: Int) = englishTurtle.setPenFontSize(размер)
    def дуга(радиус: Double, угол: Double) = englishTurtle.arc(радиус, math.round(угол).toInt)
    def круг(радиус: Double) = englishTurtle.circle(радиус)
    def положение = englishTurtle.position
    def перо_опусти() = englishTurtle.penDown()
    def перо_подними() = englishTurtle.penUp()
    def опущено_ли_перо = englishTurtle.style.down
    def цвет_пера(цвет: java.awt.Color) = englishTurtle.setPenColor(цвет)
    def цвет_заливки(цвет: java.awt.Color) = englishTurtle.setFillColor(цвет)
    def толщина_пера(n: Double) = englishTurtle.setPenThickness(n)
    def cтиль_сохрани() = englishTurtle.saveStyle()
    def cтиль_восстанови() = englishTurtle.restoreStyle()
    def сохрани_курс_позицию() = englishTurtle.savePosHe()
    def восстанови_курс_позицию() = englishTurtle.restorePosHe()
    def лучи_вкл() = englishTurtle.beamsOn()
    def лучи_выкл() = englishTurtle.beamsOff()
    def костюм(имя_файла: String) = englishTurtle.setCostume(имя_файла)
    def костюмы(имя_файла: String*) = englishTurtle.setCostumes(имя_файла: _*)
    def следующий_костюм() = englishTurtle.nextCostume()
  }
  class Черепашка(override val englishTurtle: Turtle) extends RussianTurtle {
    def this(startX: Double, startY: Double, costumeFileName: String) =
      this(builtins.TSCanvas.newTurtle(startX, startY, costumeFileName))
    def this(startX: Double, startY: Double) = this(startX, startY, "/images/turtle32.png")
    def this() = this(0, 0)
  }
  class Черепашка0(t0: => Turtle) extends RussianTurtle { // by-name construction as turtle0 is volatile }
    override def englishTurtle: Turtle = t0
  }
  object черепашка extends Черепашка0(builtins.TSCanvas.turtle0)
  def очисти() = builtins.TSCanvas.clear()
  def вывод_очисти() = builtins.clearOutput()
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
  def фон_установи(c: Color) = builtins.setBackground(c)
  def фон_2_установи(c1: Color, c2: Color) = builtins.TSCanvas.setBackgroundV(c1, c2)

  //  object KcSwe { //Key codes for Swedish keys
  //    lazy val VK_Å = 197
  //    lazy val VK_Ä = 196
  //    lazy val VK_Ö = 214
  //  }

  // loops
  def повтори(n: Int)(block: => Unit): Unit = {
    RepeatCommands.repeat(n) { block }
  }

  def i_повтори(n: Int)(block: Int => Unit): Unit = {
    RepeatCommands.repeati(n) { i => block(i) }
  }

  def пока_повтори(условие: => Boolean)(block: => Unit): Unit = {
    RepeatCommands.repeatWhile(условие) { block }
  }

  def для_повтори[T](последовательность: Iterable[T])(block: T => Unit): Unit = {
    RepeatCommands.repeatFor(последовательность) { block }
  }

  // simple IO
  def прочти(подсказка: String = "") = builtins.readln(подсказка)

  def напиши(data: Any) = println(data) // Transferred here from sv.tw.kojo.
  def напиши() = println()

  // math functions
  def округли(число: Number, разряд: Int = 0): Double = {
    val faktor = math.pow(10, разряд).toDouble
    math.round(число.doubleValue * faktor).toLong / faktor
  }
  def случайное(верхняя_граница: Int) = builtins.random(верхняя_граница)
  def случайное_двойное(верхняя_граница: Int) = builtins.randomDouble(верхняя_граница)

  // some type aliases in Swedish
  type целое = Int
  type двойное = Double
  type строка = String
}

object RussianInit {
  def init(builtins: CoreBuiltins): Unit = {
    // initialize unstable value
    RussianAPI.builtins = builtins
    builtins match {
      case b: Builtins =>
        println("Добро пожаловать в Kojo с русской черепашкой!")
        if (b.isScratchPad) {
          println("История не будет сохранена при закрытии блокнота Kojo.")
        }

//        b.setEditorTabSize(2)

        // code completion
        b.addCodeTemplates(
          "ru",
          codeTemplates
        )
        // help texts
        b.addHelpContent(
          "ru",
          helpContent
        )

      case _ =>
    }
  }

  val codeTemplates = Map(
    "вперёд" -> "вперёд(${шаги})",
    "направо" -> "направо(${угол})",
    "налево" -> "налево(${угол})",
    "перепрыгни" -> "перепрыгни(${x},${y})",
    "иди" -> "иди(${x},${y})",
    "прыгни" -> "прыгни(${шаги})",
    "дом" -> "дом()",
    "направление" -> "направление(${x},${y})",
    "курс_установи" -> "курс_установи(${угол})",
    "восток" -> "восток()",
    "запад" -> "запад()",
    "север" -> "север()",
    "юг" -> "юг()",
    "задержка" -> "задержка(${мс})",
    "напиши" -> "напиши(${текст})",
    "размер_шрифта" -> "размер_шрифта(${размер})",
    "дуга" -> "дуга(${радиус},${угол})",
    "круг" -> "круг(${радиус})",
    "покажись" -> "покажись()",
    "спрячься" -> "спрячься()",
    "перо_опусти" -> "перо_опусти()",
    "перо_подними" -> "перо_подними()",
    "опущено_ли_перо" -> "опущено_ли_перо",
    "цвет_пера" -> "цвет_пера(${цвет})",
    "цвет_заливки" -> "цвет_заливки(${цвет})",
    "толщина_пера" -> "толщина_пера(${ширина})",
    "cтиль_сохрани" -> "cтиль_сохрани()",
    "cтиль_восстанови" -> "cтиль_восстанови()",
    "сохрани_курс_позицию" -> "сохрани_курс_позицию()",
    "восстанови_курс_позицию" -> "восстанови_курс_позицию()",
    "лучи_вкл" -> "лучи_вкл()",
    "лучи_выкл" -> "лучи_выкл()",
    "очисти" -> "очисти()",
    "вывод_очисти" -> "вывод_очисти()",
    "фон_установи" -> "фон_установи(${цвет})",
    "фон_2_установи" -> "фон_2_установи(${цвет1},${цвет2})",
    "повтори" -> "повтори(${счёт}) {\n    ${cursor}\n}",
    "i_повтори" -> "i_повтори(${счёт}) { i =>\n    ${cursor}\n}",
    "пока_повтори" -> "пока_повтори(${условие}) {\n    ${cursor}\n}",
    "для_повтори" -> "для_повтори(${последовательность}) { ${e} =>\n    ${cursor}\n}",
    "напиши" -> "напиши(${текст})",
    "прочти" -> "прочти(${подсказка})",
    "округли" -> "округли(${число},${разряд})",
    "случайное" -> "случайное(${верхняя_граница})",
    "случайное_двойное" -> "случайное_двойное(${верхняя_граница})",
    "костюм" -> "костюм(${имя_файла})",
    "костюмы" -> "костюмы(${имя_файла1},${имя_файла2})",
    "следующий_костюм" -> "следующий_костюм()"
  )

  val helpContent = Map(
    "вперёд" -> <div><strong>вперёд</strong>(число_шагов) - Перемещает черепашку вперёд на данное число шагов.</div>.toString,
    "налево" -> <div> <strong>налево</strong>() - Поворачивает черепашку на 90 градусов налево (против часовой стрелки). <br/> <strong>налево</strong>(угол) - Поворачивает черепашку налево (против часовой стрелки) на данный угол в градусах.<br/> <strong>налево</strong>(угол, радиус) - Поворачивает черепашку налево (против часовой стрелки) на данный угол в градусах вокруг данного для поворота радиуса.<br/> </div>.toString,
    "направо" -> <div> <strong>направо</strong>() - Поворачивает черепашку на 90 градусов направо (по часовой стрелке). <br/> <strong>направо</strong>(угол) - Поворачивает черепашку направо (по часовой стрелке) на данный угол в градусах.<br/> <strong>направо</strong>(угол, радиус) - Поворачивает черепашку направо (по часовой стрелке) на данный угол в градусах вокруг данного для поворота радиуса.<br/> </div>.toString,
    "перепрыгни" -> <div> <strong>перепрыгни</strong>(x, y) - Размещает черепашку в точке (x, y) без рисования линии. Курс черепашки не изменяется. <br/> </div>.toString,
    "иди" -> <div><strong>или</strong>(x, y) - Поворачивает черепашку в направлении (x, y) и перемещает её в эту точку.</div>.toString,
    "прыгни" -> <div> <strong>прыгни</strong>(число_шагов) - Перемещает черепашку вперёд на данное число шагов <em>с поднятым пером</em>, таким образом, линия не рисуется. Перо опускается после прыжка. <br/> </div>.toString,
    "дом" -> <div><strong>дом</strong>() - Перемещает черепашку в её изначальное положение в центр экрана и направляет её на север.</div>.toString,
    "направление" -> <div><strong>направление</strong>(x, y) - Перемещает черепашку в направлении точки (x, y).</div>.toString,
    "курс_установи" -> <div><strong>курс_установи</strong>(угол) - Устанавливает курс черепашки на данный угол (0 - это в направлении правой стороны экрана (<em>восток</em>), 90 - это вверх (<em>север</em>)).</div>.toString,
    "курс" -> <div><strong>курс</strong> - Запрашивает курс черепашки (0 - это в направлении правой стороны экрана (<em>восток</em>), 90 - это вверх (<em>север</em>)).</div>.toString,
    "восток" -> <div><strong>восток</strong>() - Поворачивает черепашку на восток.</div>.toString,
    "запад" -> <div><strong>запад</strong>() - Поворачивает черепашку на запад.</div>.toString,
    "север" -> <div><strong>север</strong>() - Поворачивает черепашку на север.</div>.toString,
    "юг" -> <div><strong>юг</strong>() - Поворачивает черепашку на юг.</div>.toString,
    "задержка" -> <div> <strong>задержка</strong>(задержка) - Устанавливает скорость черепашки. Указанная задержка - это количество времени (в миллисекундах), требующееся черепашке чтобы переместиться на расстояние, равное ста шагам.<br/> Задержка по умолчанию - это 1000 миллисекунд (или 1 секунда).<br/> </div>.toString,
    "напиши" -> <div><strong>напиши</strong>(объект) - Заставляет черепашку написать указанный объект как строку в её текущем положении.</div>.toString,
    "размер_шрифта" -> <div><strong>размер_шрифта</strong>(n) - Указывает размер шрифта пера, которым пишет черепашка.</div>.toString,
    "дуга" -> <div> <strong>дуга</strong>(радиус, угол) - Заставляет черепашку сделать дугу с данным адресом и углом.<br/> Положительные углы заставляют черепашку идти налево (против часовой стрелки). Отрицательные углы заставляют черепашку идти направо (по часовой стрелке) <br/> </div>.toString,
    "круг" -> <div> <strong>круг</strong>(радиус) - Заставляет черепашку сделать круг с данным радиусом. <br/> Команда круг(50) эквивалентна команде дуга(50, 360).<br/> </div>.toString,
    "покажись" -> <div><strong>покажись</strong>() - Делает черепашку видимой после того, как она стала невидимой командой спрячься().</div>.toString,
    "спрячься" -> <div><strong>спрячься</strong>() - Делает черепашку невидимой. Используйте команду покажись() чтобы она снова стала видимой.</div>.toString,
    "перо_опусти" -> <div> <strong>перо_опусти</strong>() - Опускает перо черепашки вниз, таким образом, линии рисуются при перемещении черепашки.<br/> Перо черепашки опущено по умолчанию. <br/> </div>.toString,
    "перо_подними" -> <div> <strong>перо_подними</strong>() - Поднимает перо черепашки вверх, таким образом, линии не рисуются при перемещении черепашки.<br/></div>.toString,
    "опущено_ли_перо" -> <div><strong>опущено_ли_перо</strong> - Сообщает вам опущено ли перо черепашки вниз.</div>.toString,
    "цвет_пера" -> <div><strong>цвет_пера</strong>(цвет) - Указывает цвет пера, которым рисует черепашка. <br/></div>.toString,
    "цвет_заливки" -> <div><strong>цвет_заливки</strong>(цвет) - Указывает цвет заливки фигур, нарисованных черепашкой. <br/></div>.toString,
    "толщина_пера" -> <div><strong>толщина_пера</strong>(толщина) - Указывает ширину пера, с которой рисует черепашка. <br/></div>.toString,
    "cтиль_сохрани" -> <div> <strong>cтиль_сохрани</strong>() - Сохраняет текущий стиль черепашки так, чтобы он был легко восстановлен позднее через <tt>cтиль_восстанови()</tt>. <br/> <p> Стили черепашки включают: <ul> <li>Цвет пера</li> <li>Толщину пера</li> <li>Цвет заливки</li> <li>Шрифт пера</li> <li>Состояние поднятого/опущенного пера</li> </ul> </p> </div>.toString,
    "cтиль_восстанови" -> <div> <strong>cтиль_восстанови</strong>() - Восстанавливает стиль черепашки, основываясь на ранее выполненной команде <tt>cтиль_сохрани()</tt>. <br/> <p> Стили черепашки включают: <ul> <li>Цвет пера</li> <li>Толщину пера</li> <li>Цвет заливки</li> <li>Шрифт пера</li> <li>Состояние поднятого/опущенного пера</li> </ul> </p> </div>.toString,
    "сохрани_курс_позицию" -> <div> <strong>сохрани_курс_позицию</strong>() - Сохраняет текущую позицию и курс черепашки так, чтобы они легко могли быть восстановлены позднее через <tt>восстанови_курс_позицию()</tt>. <br/> </div>.toString,
    "восстанови_курс_позицию" -> <div> <strong>восстанови_курс_позицию</strong>() - Восстанавливает текущую позицию и курс черепашки, основываясь на ранее выполненной команде <tt>сохрани_курс_позицию()</tt>. <br/> </div>.toString,
    "лучи_вкл" -> <div><strong>лучи_вкл</strong>() - Показывает поперечные лучи с центром на черепашке чтобы помочь понять её курс/ориентацию.</div>.toString,
    "лучи_выкл" -> <div><strong>лучи_выкл</strong>() - Скрывает поперечные лучи у черепашки, включенные через <tt>лучи_вкл()</tt>.</div>.toString,
    "очисти" -> <div><strong>очисти</strong>() - Очищает холст черепашки и переносит черепашку в центр холста.</div>.toString,
    "вывод_очисти" -> <div><strong>вывод_очисти</strong>() - Очищает окно вывода.</div>.toString,
    "фон_установи" -> <div> <strong>фон_установи</strong>(цвет) - Устанавливает фон холста на указанный цвет. Вы можете использовать предопределённые цвета для установки фона, или вы можете создать свои собственные цвета, используя функции <tt>Color</tt>, <tt>ColorHSB</tt> и <tt>ColorG</tt>. </div>.toString,
    "фон_2_установи" -> <div><strong>фон_2_установи</strong>(цвет1, цвет2) - Устанавливает фон холста на вертикальный цветной градиент, определённый двумя указанными цветами. </div>.toString,
    "повтори" -> <div><strong>повтори</strong>(n){{ }} - Повторяет указанный блок команд (между фигурными скобками) n раз.<br/></div>.toString,
    "i_повтори" -> <div><strong>i_повтори</strong>(n) {{i => }} - Повторяет указанный блок команд (между фигурными скобками) n раз. Текущий индекс повторений доступен как  <tt>i</tt> между фигурными скобками. </div>.toString,
    "пока_повтори" -> <div><strong>пока_повтори</strong>(условие) {{ }} - Повторяет указанный блок команд (между фигурными скобками) пока данное условие истинно.</div>.toString,
    "для_повтори" -> <div><strong>для_повтори</strong>(seq){{ }} - Повторяет указанный блок команд (между фигурными скобками) для каждого элемента в данной последовательности.<br/></div>.toString,
    "напиши" -> <div><strong>напиши</strong>(obj) - Отображает данный объект как строку в окне вывода с новой строкой в конце.</div>.toString,
    "прочти" -> <div><strong>прочти</strong>(строка_запроса) - Отображает данный запрос в окне вывода и читает строку, которую вводит пользователь.</div>.toString,
    "округли" -> <div><strong>округли</strong>(n, разряд) - Округляет данное число n на указанное количество разрядов после запятой.</div>.toString,
    "случайное" -> <div><strong>случайное</strong>(верхняя_граница) - Возвращает случайное целое между 0 (включительно) и верхней границей (исключая её).</div>.toString,
    "случайное_двойное" -> <div><strong>случайное_двойное</strong>(верхняя_граница) - Возвращает случайное двойной точности десятичное число между 0 (включительно) и верхней границей (исключая её).</div>.toString,
    "костюм" -> <div><strong>костюм</strong>(файл_костюма) - Изменяет костюм (то есть изображение),  связанный с черепашкой, на изображение из указанного файла.</div>.toString,
    "костюмы" -> <div><strong>костюмы</strong>(файл_костюма1, файл_костюма2, ...) - Указывает множественные костюмы для черепашки и устанавливает её костюм на первый в последовательности. Вы можете прокрутить по циклу костюмы вызывая <tt>следующий_костюм()</tt>. </div>.toString,
    "следующий_костюм" -> <div><strong>следующий_костюм</strong>() - Изменяет костюм черепашки на следующий в последовательности костюмов, указанных через <tt>костюмы(...)</tt>.</div>.toString
  )
}
