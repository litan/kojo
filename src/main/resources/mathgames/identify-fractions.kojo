// Copyright (C) 2016 Anusha Pant <anusha.pant@gmail.com>
// The contents of this file are subject to 
// the GNU General Public License Version 3 (http://www.gnu.org/copyleft/gpl.html)

val font = Font("Sans Serif", 40)
import javax.swing.SwingConstants
val text1 = new TextField("") {
    setFont(font)
    setHorizontalAlignment(SwingConstants.CENTER)
    setColumns(2)
    setBackground(Color(255, 232, 181))
}

val text2 = new TextField("") {
    setFont(font)
    setHorizontalAlignment(SwingConstants.CENTER)
    setColumns(2)
    setBackground(Color(255, 232, 181))
}

def clearOnFocus(t: TextField[String]) {
    import java.awt.event.{ FocusEvent, FocusAdapter }
    t.addFocusListener(new FocusAdapter {
        override def focusGained(e: FocusEvent) {
            t.setText("")
        }
    })
}
clearOnFocus(text1); clearOnFocus(text2)

var n = random(6) + 2
var d = random(10) + 2
if (n == d) {
    n = d - 1
}
if (n > d) {
    n = d
    d = n + 2
}
def fracirc(a: Int, b: Int) = Picture {
    var n = a
    var d = b
    def makeD() {
        // make denominator parts
        repeat(d) {
            forward(110)
            hop(-110)
            right(360.0 / d)
        }
    }
    def makeN() {
        setFillColor(Color(90, 199, 255))
        forward(110)
        right()
        right((360.0 / d) * n, 110)
        right()
        forward(110)
    }

    right(90)
    hop(110)
    left(90)
    setPenColor(black)
    left(360, 110)
    left()
    hop(110)
    right()
    savePosHe()
    makeN()
    restorePosHe()
    makeD()
    setFillColor(noColor)
    hop(-110)
    right()
    hop(160)
    left()
    repeat(2) {
        forward(220)
        right()
        forward(50)
        right()
    }
    setFillColor(Color(90, 199, 255))
    repeat(2) {
        forward(220.0 * n / d)
        right()
        forward(50)
        right()
    }
    setFillColor(noColor)
    repeat(d - 1) {
        forward(220.0 / d)
        right()
        forward(50)
        hop(-50)
        left()
    }
}

var fracpic = fracirc(n, d)
var label = Label("")
var lab = PicShape.widget(label)
draw(lab)
var pc = black

def drawMessage(m: String, c: Color) {
    val te = textExtent(m, 30)
    val pic = penColor(c) * trans(cb.x + (cb.width - te.width) / 2, 0) ->
PicShape.text(m, 30)
    draw(pic)
}

val button = Button("Check The Answer") {
    lab.erase()
    if (n > d) {
        if (text1.value.toInt == d && text2.value.toInt == n) {
            label = Label("Your answer is correct.")
            pc = Color(0, 143, 0)
        }
        else {
            label = Label("Your answer is wrong.")
            pc = red
        }
    }
    else {
        if (text1.value.toInt == n && text2.value.toInt == d) {
            label = Label("Your answer is correct.")
            pc = Color(0, 143, 0)
        }
        else {
            label = Label("Your answer is wrong.")
            pc = red
        }
    }
    label.setFont(Font("Serif", 20))
    label.setForeground(pc)
    lab = PicShape.widget(label)
    draw(trans(cb.x, -cb.y - 40) -> lab)
}

val button2 = Button("Next") {
    fracpic.erase()
    text1.value = ""
    text2.value = ""
    lab.erase
    n = random(4) + 2
    d = random(8) + 2
    if (n == d) {
        n = d - 1
    }
    if (n > d) {
        n = d
        d = n + 2
    }
    fracpic = fracirc(n, d)
    draw(fracpic)
}

import javax.swing.JSeparator
val widgets = RowPanel(
    ColPanel(
        text1,
        new JSeparator {
            setOpaque(true)
            setBackground(black)
        },
        text2
    ),
    button,
    button2
)

cleari()
val cb = canvasBounds
val panel = trans(cb.x, cb.y) -> PicShape.widget(widgets)
draw(panel)
draw(fracpic)
text1.takeFocus()
