// An example showing the use of user-interface widgets (as pictures) in the canvas
 
val reptf = TextField(5)
val linef = TextField(60)
val angletf = TextField(360 / 5)
val colordd = DropDown("blue", "green", "yellow")
val colors = Map(
    "blue" -> blue,
    "green" -> green,
    "yellow" -> yellow
)

val instructions = 
    <html>
      To run the example, specify the required <br/>   
      values in the fields below, and click on the <br/>
      <strong><em>Make + animate</em></strong> button at the bottom.
    </html>.toString
    
val panel = ColPanel(
    RowPanel(Label(instructions)),
    RowPanel(Label("Line size: "), linef),
    RowPanel(Label("Repeat count: "), reptf),
    RowPanel(Label("Angle between lines: "), angletf),
    RowPanel(Label("Fill color: "), colordd),
    RowPanel(Button("Make + animate figure") {
        val velocity = 50 // pixels per second
        val cbx = canvasBounds.x
        val figure = trans(cbx, 0) * fillColor(colors(colordd.value)) -> Picture {
            repeat(reptf.value) {
                forward(linef.value)
                right(angletf.value)
            }
        }
        draw(figure)
        val starttime = epochTime
        figure.react { self =>
            val ptime = epochTime
            val t1 = ptime - starttime
            val d = cbx + velocity * t1
            self.setPosition(d, 0)
        }
    })
)

val pic = PicShape.widget(panel)
cleari()
draw(trans(canvasBounds.x, canvasBounds.y) -> pic)
