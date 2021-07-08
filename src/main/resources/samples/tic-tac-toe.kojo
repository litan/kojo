// Unbeatable tic tac toe - with minimax + alpha-beta pruning

val Computer = 1 // computer plays O
val Human = 2 // human plays X
val Blank = 0 // blank space on board

cleari()
val cb = canvasBounds
setBackground(black)
//disablePanAndZoom()
val len = 100

val boardSize = len * 3
val bx = cb.x + (cb.width - boardSize) / 2
val by = cb.y + (cb.height - boardSize) / 2

val margin = 20
val len2 = len - 2 * margin
val lineWidth = 8

def background() {
    setPenColor(noColor)
    setFillColor(black)
    val mgn = lineWidth / 2
    setPosition(mgn, mgn)
    repeat(4) {
        forward(len - 2 * mgn)
        right(90)
    }
}

def cross = Picture {
    background()
    setPenThickness(lineWidth)
    setPenColor(ColorMaker.hsl(200, 1.00, 0.50))
    setPosition(margin, margin)
    lineTo(len - margin, len - margin)
    setPosition(len - margin, margin)
    lineTo(margin, len - margin)
}

def o = Picture {
    background()
    setPenThickness(lineWidth)
    setPenColor(ColorMaker.hsl(120, 0.86, 0.64))
    setPosition(len / 2, margin)
    setHeading(0)
    left(360, len2 / 2)
}

def blank = Picture {
    background()
}

val lines = Picture {
    setPenThickness(lineWidth)
    repeatFor(1 to 2) { n =>
        setPosition(len * n, 0)
        lineTo(len * n, 3 * len)
    }
    repeatFor(1 to 2) { n =>
        setPosition(0, len * n)
        lineTo(3 * len, len * n)
    }
}

def noPic: Picture = Picture {}

val pics = ArrayBuffer(
    ArrayBuffer(noPic, noPic, noPic),
    ArrayBuffer(noPic, noPic, noPic),
    ArrayBuffer(noPic, noPic, noPic)
)
val boardState = ArrayBuffer(
    ArrayBuffer(Blank, Blank, Blank),
    ArrayBuffer(Blank, Blank, Blank),
    ArrayBuffer(Blank, Blank, Blank)
)

var nextCross = true
var done = false

def evaluate: Int = {
    if (checkWinFor(Human)) {
        -10
    }
    else if (checkWinFor(Computer)) {
        10
    }
    else {
        0
    }
}

def gameDrawn: Boolean = {
    var filled = true
    repeatFor(0 until 3) { x =>
        repeatFor(0 until 3) { y =>
            if (boardState(x)(y) == Blank) {
                filled = false
            }
        }
    }
    filled
}

val AlphaMin = -1000
val BetaMax = 1000

def minimax(curDepth: Int, computerTurn: Boolean, alpha: Int, beta: Int): Int = {
    val score = evaluate
    if (score == 10 || score == -10) {
        return score
    }

    if (gameDrawn) {
        return 0
    }

    if (computerTurn) {
        var value = AlphaMin
        var newAlpha = alpha
        repeatFor(0 until 3) { x =>
            repeatFor(0 until 3) { y =>
                if (boardState(x)(y) == Blank) {
                    boardState(x)(y) = Computer
                    value = math.max(value, minimax(curDepth + 1, !computerTurn, newAlpha, beta))
                    boardState(x)(y) = Blank
                    if (value >= beta) {
                        return value
                    }
                    newAlpha = math.max(newAlpha, value)
                }
            }
        }
        return value
    }
    else {
        var value = BetaMax
        var newBeta = beta
        repeatFor(0 until 3) { x =>
            repeatFor(0 until 3) { y =>
                if (boardState(x)(y) == Blank) {
                    boardState(x)(y) = Human
                    value = math.min(value, minimax(curDepth + 1, !computerTurn, alpha, newBeta))
                    boardState(x)(y) = Blank
                    if (value <= alpha) {
                        return value
                    }
                    newBeta = math.min(newBeta, value)
                }
            }
        }
        return value
    }
}

case class Move(x: Int, y: Int)

def findBestMove: Move = {
    var bestVal = -1000;
    var bestMove = Move(-1, -1)
    repeatFor(0 until 3) { x =>
        repeatFor(0 until 3) { y =>
            if (boardState(x)(y) == Blank) {
                boardState(x)(y) = Computer
                val moveVal = minimax(0, false, AlphaMin, BetaMax)
                boardState(x)(y) = Blank
                if (moveVal > bestVal) {
                    bestVal = moveVal
                    bestMove = Move(x, y)
                }
            }
        }
    }
    bestMove
}

def doMove(x: Int, y: Int, move: Int, newPic: Picture) {
    newPic.setPosition(bx + x * len, by + y * len)
    boardState(x)(y) = move
    nextCross = !nextCross
    pics(x)(y) = newPic
    draw(newPic)
    checkWin()
    if (!done) {
        checkDraw()
    }

}

def doComputerMove(pic: Picture) {
    val move = findBestMove
    val newPic = o
    doMove(move.x, move.y, Computer, newPic)
}

def drawBoard() {
    lines.setPosition(bx, by)
    draw(lines)
    repeatFor(0 until 3) { x =>
        repeatFor(0 until 3) { y =>
            val pic = blank
            pic.setPosition(bx + x * len, by + y * len)
            draw(pic)
            pic.onMousePress { (_, _) =>
                if (!done) {
                    if (nextCross) {
                        val newPic = cross
                        pic.erase()
                        doMove(x, y, Human, newPic)
                        if (!done) {
                            schedule(0.5) {
                                doComputerMove(pic)
                            }
                        }
                    }
                }
            }
            pics(x)(y) = pic
            boardState(x)(y) = 0
        }
    }
}

def column(x: Int) = boardState(x)
def row(y: Int) = ArrayBuffer(boardState(0)(y), boardState(1)(y), boardState(2)(y))
def diagonal1 = ArrayBuffer(boardState(0)(0), boardState(1)(1), boardState(2)(2))
def diagonal2 = ArrayBuffer(boardState(0)(2), boardState(1)(1), boardState(2)(0))

def checkWinFor(n: Int): Boolean = {
    var win = false
    val target = ArrayBuffer(n, n, n)
    repeatFor(0 until 3) { x =>
        win = { column(x) == target }
        if (win) {
            return true
        }
    }

    repeatFor(0 until 3) { y =>
        win = { row(y) == target }
        if (win) {
            return true
        }
    }
    win = { diagonal1 == target }
    if (win) {
        return true
    }
    win = { diagonal2 == target }
    win
}

def gameOver(msg: String) {
    val pmsg = Picture {
        setPenFontSize(80)
        setPenColor(white)
        write(msg)
    }
    val pic = picColCentered(pmsg, Picture.vgap(cb.height - 100))
    drawCentered(pic)
    done = true
}

def checkWin() {
    if (checkWinFor(Computer)) {
        gameOver("O Won")
    }
    else if (checkWinFor(Human)) {
        gameOver("X Won")
    }
}

def checkDraw() {
    var filled = true
    repeatFor(0 until 3) { x =>
        repeatFor(0 until 3) { y =>
            if (boardState(x)(y) == 0) {
                filled = false
            }
        }
    }
    if (filled) {
        done = true
        gameOver("It's a Draw")
    }
}

drawBoard()
