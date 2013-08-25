val Level = 1
require(Level >= 1 && Level <= 3, "Level needs to be between 1 and 3")

toggleFullScreenCanvas()
cleari()

case class GameState(card1: Option[Card], card2: Option[Card], card3: Option[Card]) {
    def click(c: Card) {
        if (card1.isEmpty) {
            c.flip()
            gState = gState.copy(card1 = Some(c))
            incrMoves()
        }
        else if (card2.isEmpty) {
            if (!(c eq card1.get)) {
                c.flip()
                gState = gState.copy(card2 = Some(c))
                incrMoves()
                if (card1.get == c) {
                    card1.get.done()
                    c.done()
                }
            }
        }
        else if (card3.isEmpty) {
            if (!(c eq card1.get) && !(c eq card2.get)) {
                if (card1.get != card2.get) {
                    card1.get.flip()
                    card2.get.flip()
                }
                gState = GameState(None, None, None)
                gState.click(c)
            }
        }
    }
}

case class Moves(n: Int) {
    val pLabel = penColor(black) -> PicShape.text(s"Moves: $n", 20)
    def incr() = Moves(n + 1)
}

var gState = GameState(None, None, None)
var moves = Moves(0)

def incrMoves() {
    val pos = moves.pLabel.position
    moves.pLabel.erase()
    moves = moves.incr()
    moves.pLabel.setPosition(pos)
    moves.pLabel.draw()
}

val CardBg = Color(0, 255, 0, 127)
val CardGlowBg = Color(0, 0, 255, 127)

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

    pBack.onMouseClick { (x, y) => if (active) gState.click(this) }
    pFront.onMouseClick { (x, y) => if (active) gState.click(this) }
}

val Num = Level * 2 * 5
def genCards(n: Int) = for (i <- 1 to n) yield Card(i)
val cards = util.Random.shuffle(genCards(Num / 2) ++ genCards(Num / 2))

for (i <- 0 to Num / 5 - 1) {
    for (j <- 0 to 4) {
        cards(i * 5 + j).drawAt(i, j)
    }
}

draw(trans(-canvasBounds.width / 2 + 50, 0) -> moves.pLabel)
