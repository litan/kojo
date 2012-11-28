// Example from http://lalitpant.blogspot.in/2012/05/recursive-drawing-with-kojo.html
val size = 100
def S = Picture {
    repeat (4) {
        forward(100)
        right()
    }
}

def stem = scale(0.13, 1) * penColor(noColor) * fillColor(black) -> S

clear()
setBackground(Color(255, 170, 29))
invisible()

def drawing(n: Int): Picture = {
    if (n == 1) 
        stem
    else 
        GPics(stem,
              trans(2, size-5) * brit(0.05) -> GPics(
                rot(25) * scale(0.72) -> drawing(n-1),
                rot(25) * trans(0, size * 0.72) * rot(-75) * scale(0.55) -> drawing(n-1)
            )
        )
}

val pic = trans(0, -100) -> drawing(10)
draw(pic)