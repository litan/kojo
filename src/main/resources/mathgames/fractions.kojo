// Copyright (C) 2015 Anusha Pant <anusha.pant@gmail.com>
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

var fracPic = PicShape.hline(0)
val button = Button("Draw fraction") {
    try {
        fracPic.erase()
        dnp.erase()
        fracPic = fracirc(text1.value.toInt, text2.value.toInt)
        draw(fracPic)
        button2.setEnabled(true)
    }
    catch {
        case e: RuntimeException =>
        //            println("Please enter numbers in the numerator and denominator")
    }
}

def decper(n: Double, d: Double) = {
    var deci = n / d
    var per = deci * 100
    picStack(
        trans(cb.x + 10, -cb.y - 50) -> PicShape.widget(Label(f"The decimal value is $deci%.2f")),
        trans(cb.x + 10, -cb.y - 70) -> PicShape.widget(Label(f"The percentage is $per%.2f"))
    )
}

var dnp: Picture = PicShape.hline(0)
val button2: Button = Button("See decimal/percentage representation") {
    try {
        dnp.erase()
        dnp = decper(text1.value.toDouble, text2.value.toDouble)
        draw(dnp)
        button2.setEnabled(false)
        button.requestFocusInWindow()
    }
    catch {
        case e: RuntimeException =>
        //            println("Please enter numbers in the numerator and denominator")
    }
}

def fracirc(n: Int, d: Int) = Picture {
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
    if (n > d) {
        setPosition(-350, 0)
        write("The numerator should be less than the denominator")
    }
    else {
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
    }
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
button2.setEnabled(false)
draw(panel)
text1.takeFocus()
