Mw.hideAlgebraView()
Mw.hideAxes()
Mw.clear()
val t = Mw.turtle(-1, 0)
t.showExternalAngles()
t.forward(-2)
t.forward(3)
val msg = """
In the figure below, think of the green dot as the nose of the turtle.
Initially, it is pointed upwards.

Move the green dot around to experiment with the input angle for 
the right() command - needed to make the turtle point towards the green dot.
"""
val txt = Mw.text(msg, 1, 4)
Mw.show(txt)
Mw.show(Mw.text("right(", -2.5, -2.1))
Mw.show(Mw.text(")", 1, -2.1))
