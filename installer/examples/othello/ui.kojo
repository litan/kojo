//#include history
//#include alphaBetaSearch
//#include ui_test

class UI(
    board:         EBoard,
    history:       History,
    computerPlays: Stone) {

    val length = 90 // size of squares on the board
    val (llx, lly) = (-board.size / 2 * length, -board.size / 2 * length)
    val (l2, l3, l4) = (length / 2, length / 3, length / 4)

    val emptySquareColor = Color(10, 111, 23) // dark greenish
    def stone2color: Stone => Color = {
        case Empty => emptySquareColor
        case White => white
        case Black => black
    }
    def room2point(room: Room, ll: Boolean = true) =
        if (ll) Point(llx + room.x * length, lly + room.y * length)
        else Point(llx + room.x * length + l2, lly + room.y * length + l2)
    val pic2room = collection.mutable.Map.empty[Picture, Room]
    val room2pic = collection.mutable.Map.empty[Room, Picture]
    def paint(r: Room, s: Stone) = room2pic(r).setFillColor(stone2color(s))
    def moves = board.moves(board.player())

    def drawTheBoard = {
        val innerCorners = ArrayBuffer.empty[Picture]
        val innerCornerColor = Color(255, 215, 85, 101) // pale yellowish color
        for (x <- board.range; y <- board.range) {
            val room = Room(y, x)
            val edgeColor =
                if (board.isInnerCorner(room)) innerCornerColor else purple
            val pic = penColor(edgeColor) *
                fillColor(stone2color(board.stone(y, x))) ->
                Picture.rectangle(length, length)
            pic.setPosition(room2point(room))
            pic.draw()
            pic2room += (pic -> room)
            room2pic += (room -> pic)
            if (board.isInnerCorner(room))
                innerCorners += pic
            defineBehavior(pic)
        }
        innerCorners.map { _.moveToFront() }
    }

    def defineBehavior(pic: Picture) = { // pic is a square on the board
        val room = pic2room(pic)
        var stonesToBeFlipped: Seq[Room] = Seq()
        pic.onMouseEnter { (_, _) =>
            board.stone(room) match {
                case Empty =>
                    val payOff = board.movePayoff(room, board.player())
                    if (payOff > 0) hiliteMoveOutcome
                    hintPics.update(payOff, room)
                case _ => hintPics.update(0, room)
            }
            hintPics.refresh(pic, room2point(room, false) - Point(l2, -l2))
        }
        pic.onMouseExit { (_, _) =>
            clearHilites
            hintPics.toggleV
            if (computerInPlay) computerToMove
        }
        pic.onMouseClick { (_, _) =>
            board.stone(room) match {
                case Empty =>
                    if (makeTheMove(room)) {
                        clearHilites
                        if (board.isGameOver) endTheGame
                        else if (computerInPlay) refreshScoreBoard
                    }
                case _ =>
            }
        }
        def roomColor = stone2color(board.stone(room))
        def playerColor = stone2color(board.player())
        def playerColorHilite = board.player() match {
            case White => Color(244, 213, 244) // light purple
            case Black => Color(98, 8, 97) // dark purple
            case _     => stone2color(Empty)
        }
        def oppColor = stone2color(board.player.opponent)
        def hiliteMoveOutcome = if (hintsOn) {
            stonesToBeFlipped = board.pretendMove(room)
            stonesToBeFlipped.map { r =>
                room2pic(r).setFillColor(playerColorHilite)
                room2pic(r).opacityMod(0.8)
            }
        }
        def clearHilites = if (hintsOn) {
            stonesToBeFlipped.map { r =>
                room2pic(r).setFillColor(oppColor)
                room2pic(r).opacityMod(1)
            }
            pic.setFillColor(roomColor) // room of pic is in stonesToBeFlipped
            pic.opacityMod(1)
            stonesToBeFlipped = Seq()
        }
    }

    def makeTheMove(room: Room, pauseDuration: Double = 0.0): Boolean = {
        if (board.pretendMove(room).size < 2) {
            println(s"${board.player()} can't move on $room.")
            false
        }
        else {
            println(s"Move ${board.moveCount()}. ${board.player()} on $room:")
            history.saveBoard(true, board.lastMove)
            board.lastMove = Some(room)
            val player = board.player() // move changes the player, in general
            board.move(room).foreach { paint(_, player) }
            refreshScoreBoard
            board.print
            showMoves
            showLastMove
            if (pauseDuration > 0) pause(pauseDuration)
            if (board.isGameOver) endTheGame
            true
        }
    }

    def newGame = if (board.moveCount() != 1) {
        board.reset("New game:")
        for (x <- board.range; y <- board.range)
            paint(Room(y, x), board.stone(y, x))
        history.reset()
        clearLastMove
        showMoves
        endedTheGame = false
        if (computerInPlay) computerToMove
    }

    var movePics: Seq[Picture] = Seq()
    def clearMoves = movePics.foreach { _.erase() }
    def showMoves = {
        clearMoves
        if (hintsOn) {
            val ordered = moves.map { r =>
                (r, board.movePayoff(r, board.player()))
            }.sortBy { p => p._2 }.reverse
            if (ordered.size > 0) {
                val maxPayOff = ordered.head._2
                movePics = ordered map {
                    case (r, payOff) =>
                        val color = if (payOff == maxPayOff) yellow else orange
                        val pic = penColor(color) * penThickness(3) * fillColor(noColor) ->
                            Picture.circle(l4)
                        pic.setPosition(room2point(r, false))
                        pic.forwardInputTo(room2pic(r))
                        pic.draw()
                        pic
                }
            }
        }
    }

    var lastMovePic: Picture = Picture.circle(l4)
    def showLastMove = {
        clearLastMove
        if (hintsOn) {
            board.lastMove match {
                case Some(room) =>
                    lastMovePic = penColor(blue) * penThickness(3) * fillColor(noColor) ->
                        Picture.circle(l4)
                    lastMovePic.setPosition(room2point(room, false))
                    lastMovePic.forwardInputTo(room2pic(room))
                    lastMovePic.draw()
                case _ =>
            }
        }
    }
    def clearLastMove = lastMovePic.erase()

    var endedTheGame = false
    def endTheGame = if (!endedTheGame) {
        endedTheGame = true
        assert(board.isGameOver == true)
        println(s"The game is over.")
        for (s <- Seq(White, Black)) println(s"${s.name.capitalize}: ${board.count(s)}")
    }

    def computerInPlay = board.player() == computerPlays
    def computerToMove: Unit = {
        def board2board(size: Int) = {
            var b = new Board(size, Vector.fill(size * size)(0))
            for (s <- List(White, Black)) b = b.place(s, board.stones(s))
            b
        }
        val sBoard = board2board(board.size)
        val state = new State(sBoard, board.player())
        if (state.isGameOver) endTheGame
        else {
            val move = ABS.move(state) match {
                case Some(room) => room
                case _          => throw new Exception("Not here?!")
            }
            makeTheMove(move)
            // if we don't have any moves, computer to replay
            if (!board.isGameOver && computerInPlay) computerToMove
        }
    }

    def defineKeys = {
        onKeyPress { k =>
            k match {
                case Kc.VK_LEFT  => mem.undo
                case Kc.VK_RIGHT => mem.redo
                case Kc.VK_UP    => computerToMove
                case _           =>
            }
        }
    }

    object mem {
        def undo = {
            history.undo
            endedTheGame = false
            repaint
        }
        def redo = {
            history.redo
            repaint
            if (board.isGameOver) endTheGame
        }
        def repaint {
            refreshScoreBoard
            for (x <- board.range; y <- board.range)
                paint(Room(y, x), board.stone(y, x))
            showLastMove
            showMoves
        }
    }

    var hintsOn = true
    def toggleHints = {
        hintsOn = if (hintsOn) false else true
        if (hintsOn) {
            showMoves
            showLastMove
            hintPics.turnOn
        }
        else {
            clearMoves
            clearLastMove
            hintPics.turnOff
        }
    }

    def defineButtons = {
        val scoreboard = scoreBoard.map(Label(_))
        draw(
            trans(llx + length * board.size + 10, lly) ->
                Picture.widget(
                    ColPanel(
                        scoreboard(0),
                        scoreboard(1),
                        scoreboard(2),
                        Label(""),
                        Label(""),
                        Button("Toggle Hints On/Off") { toggleHints },
                        Button("Suggest Move") { computerToMove },
                        Button("Undo") { mem.undo },
                        Button("Redo") { mem.redo },
                        Button("Toggle Full Screen") { toggleFullScreenCanvas() },
                        Button("New Game") { newGame })
                )
        )
        scoreboard
    }

    def scoreBoard: Seq[String] = { // make sure to have 3 elements for three lines in the scoreBoard
        def score: Seq[String] = for (s <- Seq(White, Black))
            yield s"${s.cname}: ${board.count(s)}"
        if (board.isGameOver) Seq("Game is over") ++ score
        else if (board.moveCount == 1) Seq(
            s"${board.startingPlayer.cname} to play", "", ""
        )
        else if (computerInPlay)
            Seq(s"Computer computing next move...") ++ score
        else
            Seq(s"Move ${board.moveCount()}. ${board.player().cname} to play" +
                (if (board.skippedTurn) " again" else "")) ++ score
    }
    def refreshScoreBoard =
        scoreboard.zipWithIndex.foreach {
            case (l, i) => l.setText(scoreBoard(i))
        }

    cleari()
    clearOutput()
    toggleFullScreenCanvas()
    setBackground(darkGrayClassic)
    drawTheBoard
    showMoves
    defineKeys
    val scoreboard = defineButtons
    activateCanvas
    refreshScoreBoard

    val hintPics = new HintPics(length)

    //if (computerInPlay) computerToMove // todo: we get an empty screen while the computer computes the first move!
}

class HintPics(length: Int) {
    var show = true
    def turnOn = { show = true }
    def turnOff = {
        show = false
        pics.map(_.invisible)
    }
    val hintPayoff = Picture.textu("", 20, red)
    val hintCoord = Picture.textu("", 20, pink)
    def update(payoff: Int, room: Room) = {
        hintPayoff.update(if (payoff > 0) payoff.toString else "")
        hintCoord.update(room.toString)
    }
    val pics = List(hintPayoff, hintCoord)
    def draw = pics.map(_.draw)
    def toggleV = if (show) pics.map(_.toggleV)
    def setPosition(p: Point) = {
        hintPayoff.setPosition(p)
        hintCoord.setPosition(p - Point(0, length / 2))
    }
    def moveToFront() = pics.map(_.moveToFront())
    def forwardInputTo(p: Picture) = pics.map(_.forwardInputTo(p))
    def refresh(picture: Picture, newPosition: Point) = {
        this.setPosition(newPosition)
        this.toggleV
        this.moveToFront()
        this.forwardInputTo(picture)
    }
    draw
    toggleV
}

val ui_wip = false
if (runUnitTests || ui_wip) {
    println("Here!")
    test_basic_board_drawing(0.1)
    test_game_play
    test_computer_play
}
