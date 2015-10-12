// Copyright (C) 2015 Anusha Pant <anusha.pant@gmail.com>
// The contents of this file are subject to 
// the GNU General Public License Version 3 (http://www.gnu.org/copyleft/gpl.html)

toggleFullScreenCanvas()

val hutsize = 50
def hut() {
    setPenColor(black)
    setFillColor(Color(204, 102, 0))
    repeat(4) {
        forward(hutsize)
        right()
    }
    hop(hutsize)
    left()
    hop(5)
    setFillColor(red)
    right(120)
    repeat(3) {
        forward(hutsize + 10)
        right(120)
    }
    right(60)
    hop(5)
    left()
    hop(-hutsize)
}

def drawhut(h: Int, v: Int, e: Int) = Picture {
    repeat(h) {
        repeat(v) {
            hut()
            right()
            hop(75)
            left()
        }
        left()
        hop(v * 75)
        left()
        hop(120)
        left(180)
    }
    repeat(e) {
        hut()
        right()
        hop(75)
        left()
    }
}


val max1 = 4

val max2 = 5
val bgColor = Color(208, 144, 73)

def number(max: Int) = 2 + random(max - 1)
def snumber(n: Int) = s" $n "

var number1 = 0
var number2 = 0
var number3 = 0
var answer = 0
var answerLen = 0
var done = false
val oper = "x"
val oper2 = "+"

def differentNumber(n: Int, m: Int) = {
    var n2 = 0
    do {
        n2 = number(m)
        println(n2)
    } while (n2 == n)
    n2
}

import javax.swing.SwingConstants
import javax.swing.BorderFactory
val font = Font("Sans Serif", 60)
val answerF = new TextField(0) {
    setFont(font)
    setColumns(5)
    setHorizontalAlignment(SwingConstants.CENTER)
    setBackground(bgColor)
    setBorder(BorderFactory.createLineBorder(black))
}

def newUi() {
    ui.erase()
    ui = hutPanel
    draw(ui)
    answerF.takeFocus()
}

def newQuestion() {
    number1 = differentNumber(number1, max1)
    number2 = differentNumber(number2, max2)
    number3 = randomFrom(1 to number2 - 1)
    answer = number1 * number2 + number3
    answerF.setText("")
    answerLen = answer.toString.length
}

def hutPanel = picCol(
     trans(-cb.x - 300, cb.y + 60) ->  PicShape.widget(answerF),
     trans(cb.x + 50, -cb.y ) -> drawhut(number1, number2, number3),
     trans(cb.x + 20, -cb.y - 240) * penColor(black) -> PicShape.text("How
many huts are there?")
)

var corrects = 0
var wrongs = 0

var lastQtime = epochTimeMillis
def enoughQTime = {
    val delta = epochTimeMillis - lastQtime
    if (delta > 100) {
        lastQtime = epochTimeMillis
        true
    }
    else false
}

import java.awt.event.{ KeyAdapter, KeyEvent }
answerF.addKeyListener(new KeyAdapter {
    def checkAnswer(x: Int) {
        if (x == answer) {
            answerF.setForeground(green)
            corrects += 1
            if (!done && enoughQTime) {
                schedule(0.3) {
                    newQuestion()
                    newUi()
                    answerF.setForeground(black)
                }
            }
        }
        else {
            answerF.setForeground(red)
            wrongs += 1
            if (!done) {
                newUi()
            }
        }
    }

    def checkingTime(e: KeyEvent) = {
        answerF.getText.length >= answerLen
    }

    override def keyTyped(e: KeyEvent) {
        if (!e.getKeyChar.isDigit) {
            e.consume()
        }
    }

    override def keyReleased(e: KeyEvent) {
        if (checkingTime(e)) {
            val x = answerF.value
            checkAnswer(x)
        }
        else {
            answerF.setForeground(black)
        }
    }
})

def drawMessage(m: String, c: Color) {
    val te = textExtent(m, 30)
    val pic = penColor(c) * trans(cb.x + (cb.width - te.width) / 2, 0) ->
PicShape.text(m, 30)
    draw(pic)
}

def manageGameTime() {
    def score(c: Int, w: Int) = c - w
    var gameTime = 0
    val timeLabel = trans(cb.x + 10, cb.y + 50) -> PicShape.textu(gameTime,
20, blue)
    draw(timeLabel)
    timeLabel.forwardInputTo(stageArea)

    timer(1000) {
        gameTime += 1
        timeLabel.update(gameTime)

        if (gameTime == 60) {
            done = true
            val msg = s"""      Game over!
            |Correct answers: $corrects
            |Wrong answers: $wrongs
            |Score: ${score(corrects, wrongs)}
            """
            ui.erase()
            drawMessage(msg.stripMargin, black)
            stopAnimation()
        }
    }
}

cleari()
drawStage(bgColor)
val cb = canvasBounds
var ui: Picture = PicShape.hline(1)
newQuestion()
newUi()
manageGameTime()
