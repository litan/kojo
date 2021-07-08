//#include types
//#include eBoard

// save/restore api to support sequences of undo/redo
class History(board: EBoard) {
    private val oldBoards = ArrayBuffer.empty[Array[Array[Stone]]]
    private val players = ArrayBuffer.empty[Stone]
    private val moves = ArrayBuffer.empty[Option[Room]]

    var movedAfterLatestUndo = true // no moves, no undo/redo (init state)
    def undo = if (board.moveCount() > 1) {
        if (movedAfterLatestUndo) {
            movedAfterLatestUndo = false
            saveBoard(false, board.lastMove) // so that we can redo
        }
        board.moveCount.decr()
        restoreBoard(board.moveCount() - 1)
    }
    def redo = if (board.moveCount() < oldBoards.size) {
        restoreBoard(board.moveCount())
        board.moveCount.incr()
    }

    def isSkippedTurn =
        if (board.moveCount() == 1) false
        else board.player() == players(board.moveCount() - 2)

    def reset() = {
        oldBoards.clear()
        players.clear()
        moves.clear()
    }

    def restoreBoard(move: Int) = {
        val old = oldBoards(move)
        for (x <- board.range; y <- board.range)
            board.setStone(y)(x)(old(y)(x))
        board.player.set(players(move))
        board.lastMove = moves(move)
        board.skippedTurn =
            if (move > 1) board.player() == players(move - 1)
            else false
    }

    def saveBoard(isNewMove: Boolean, move: Option[Room]) = {
        if (isNewMove) { // clear the future -- its obsolete now. No longer anything to redo
            while (board.moveCount() <= oldBoards.size) {
                oldBoards.remove(oldBoards.size - 1)
                players.remove(players.size - 1)
                moves.remove(moves.size - 1)
            }
            movedAfterLatestUndo = true
        }
        val neu = Array.ofDim[Stone](board.size, board.size)
        for (x <- board.range; y <- board.range)
            neu(y)(x) = board.stone(y, x)
        oldBoards += neu
        players += board.player()
        moves += move
    }

}
