val Level = 1
require(Level >= 1 && Level <= 3, "Level needs to be between 1 and 3")

toggleFullScreenCanvas()
cleari()

case class Card(num: Int) {
    def decorate(p: Picture) = fillColor(CardBg) * penColor(darkGray) -> p
    val te = textExtent(num.toString, 60)
    val pFront = decorate(GPics(PicShape.rect(120, 80), trans((80 - te.width) / 2, te.height + (120 - te.height) / 2) -> PicShape.text(num, 60)))
    val pBack = decorate(PicShape.rect(120, 80))
    def drawAt(i: Int, j: Int) {
        drawAndHide(pFront)
        draw(trans(-200 + j * 100, -120 * Level + i * 140) -> pBack)
        pFront.setPosition(pBack.position)
    }
    def flip() {
        if (pBack.isVisible) {
            pBack.invisible()
            pFront.visible()
        }
        else {
            pFront.invisible()
            pBack.visible()
        }
    }
    var active = true
    def done() {
        active = false
        pFront.setFillColor(CardGlowBg)
        schedule(1) { pFront.setFillColor(CardBg) }
    }

    pBack.onMouseClick { (x, y) => if (active) click(this) }
    pFront.onMouseClick { (x, y) => if (active) click(this) }
}

case class Moves(n: Int) {
    val pLabel = penColor(black) -> PicShape.text(s"Moves: $n", 20)
    def incr() = Moves(n + 1)
}

case class World(card1: Option[Card],
                 card2: Option[Card],
                 card3: Option[Card],
                 moves: Moves)

def click(c: Card) {
    if (world.card1.isEmpty) {
        c.flip()
        world = world.copy(card1 = Some(c))
        incrMoves()
    }
    else if (world.card2.isEmpty) {
        val card1 = world.card1.get 
        if (!(c eq card1)) {
            c.flip()
            world = world.copy(card2 = Some(c))
            incrMoves()
            if (card1 == c) {
                card1.done()
                c.done()
            }
        }
    }
    else if (world.card3.isEmpty) {
        val card1 = world.card1.get; val card2 = world.card2.get 
        if (!(c eq card1) && !(c eq card2)) {
            if (card1 != card2) {
                card1.flip()
                card2.flip()
            }
            world = World(None, None, None, world.moves)
            click(c)
        }
    }
}

def incrMoves() {
    def mlabel = world.moves.pLabel
    val pos = mlabel.position
    mlabel.erase()
    world = world.copy(moves = world.moves.incr())
    mlabel.setPosition(pos)
    mlabel.draw()
}

val Num = Level * 2 * 5
def genCards(n: Int) = for (i <- 1 to n) yield Card(i)

var world = World(None, None, None, Moves(0))

val CardBg = Color(0, 255, 0, 127)
val CardGlowBg = Color(0, 0, 255, 127)
val cards = util.Random.shuffle(genCards(Num / 2) ++ genCards(Num / 2))

for (i <- 0 to Num / 5 - 1) {
    for (j <- 0 to 4) {
        cards(i * 5 + j).drawAt(i, j)
    }
}

draw(trans(-canvasBounds.width / 2 + 50, 0) -> world.moves.pLabel)
activateCanvas()
