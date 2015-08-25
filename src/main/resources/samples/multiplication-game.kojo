toggleFullScreenCanvas()
val max1 = 9

val max2 = 10
val bgColor = Color(208, 144, 73)

def number(max: Int) = 2 + random(max - 1)
def snumber(n: Int) = s" $n "

var number1 = 0
var number2 = 0
var answer = 0
var answerLen = 0
var done = false
val oper = "x"

def differentNumber(n: Int, m: Int) = {
    var n2 = 0
    do {
        n2 = number(m)
    } while (n2 == n)
    n2
}

import javax.swing.SwingConstants
import javax.swing.BorderFactory
val font = Font("Sans Serif", 60)
val answerF = new TextField(0) {
    setFont(font)
    setColumns(3)
    setHorizontalAlignment(SwingConstants.CENTER)
    setBackground(bgColor)
    setBorder(BorderFactory.createLineBorder(black))
}

def numPanel = new ColPanel(
    new RowPanel(
        new Label(snumber(number1)) {
            setFont(font)
            setHorizontalAlignment(SwingConstants.CENTER)
        },
        new Label(oper) {
            setFont(font)
            setHorizontalAlignment(SwingConstants.CENTER)
        },
        new Label(snumber(number2)) {
            setFont(font)
            setHorizontalAlignment(SwingConstants.CENTER)
        }
    ) {
        setBackground(bgColor)
    },
    answerF
) {
    setBackground(bgColor)
    setBorder(BorderFactory.createEmptyBorder)
}

def newUi() {
    ui.erase()
    ui = trans(-150, 0) -> PicShape.widget(numPanel)
    draw(ui)
    answerF.takeFocus()
}

def newQuestion() {
    number1 = differentNumber(number1, max1)
    number2 = differentNumber(number2, max2)
    answer = number1 * number2
    answerF.setText("")
    answerLen = answer.toString.length
}

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
    val pic = penColor(c) * trans(cb.x + (cb.width - te.width) / 2, 0) -> PicShape.text(m, 30)
    draw(pic)
}

def manageGameTime() {
    def score(c: Int, w: Int) = c - w
    var gameTime = 0
    val timeLabel = trans(cb.x + 10, cb.y + 50) -> PicShape.textu(gameTime, 20, blue)
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
