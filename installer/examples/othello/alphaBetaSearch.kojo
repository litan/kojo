//#include types
//#include board

object ABS {
    var maxDepth = 3
    def move(state: State): Option[Room] = {
        var out: Option[Room] = None
        val t0 = epochTime
        out = move_(state)
        val delta = epochTime - t0
        println(f"Alpha-beta search took $delta%.3f seconds")
        out
    }
    def move_(state: State): Option[Room] =
        if (state.moves.isEmpty) None
        else Some((for (move <- state.moves) yield move ->
            abMove(state.move(move), maxDepth)).minBy(_._2)._1)
    // todo: maxDepth must be adaptive, or better just find out how many branches we can process...
    def abMove(state: State, depth: Int): Int =
        if (state.isGameOver || depth == 0 || state.moves.isEmpty) state.score
        else minimize(state, depth, Int.MinValue, Int.MaxValue)

    def minimize(state: State, depth: Int, alpha: Int, beta: Int): Int =
        if (state.isGameOver || depth == 0 || state.moves.isEmpty) state.score
        else {
            var newBeta = beta
            state.moves.foreach { move =>
                val newState = state.move(move)
                newBeta = math.min(newBeta, maximize(newState, depth - 1, alpha, newBeta))
                if (alpha >= newBeta) return alpha
            }
            newBeta
        }
    def maximize(state: State, depth: Int, alpha: Int, beta: Int): Int =
        if (state.isGameOver || depth == 0 || state.moves.isEmpty) state.score
        else {
            var newAlpha = alpha
            state.moves.foreach { move =>
                val newState = state.move(move)
                newAlpha = math.max(newAlpha, minimize(newState, depth - 1, newAlpha, beta))
                if (newAlpha >= beta) return beta
            }
            newAlpha
        }
}

class State(val board: Board, val turn: Stone) {
    val opponent: Stone = if (turn == White) Black else White
    def score: Int = board.score(turn) - board.score(opponent)
    def isGameOver: Boolean = {
        if (moves.size > 0) false else {
            val newState = new State(board, opponent)
            newState.moves.size == 0
        }
    }
    def moves: Seq[Room] = board.moves(turn)
    def move(room: Room): State = {
        val newBoard = board.move(turn, room)
        new State(newBoard, opponent)
    }
}

class Game(size: Int) {
    val b = newBoard(size)
    val state = new State(b, Black)
    def play: State = loop(state)
    var i = 0
    import scala.annotation.tailrec
    @tailrec
    private def loop(state: State): State = {
        i += 1
        state.board.print(i.toString)
        if (state.isGameOver) state
        else {
            if (i > size * size) {
                println("Not halting, is it?")
                return state
            }
            ABS.move(state) match {
                case Some(room) =>
                    val newState = state.move(room)
                    println(s"Move $i by ${state.turn}: $room")
                    loop(newState)
                case _ =>
                    val state2 = new State(state.board, state.opponent)
                    ABS.move(state2) match {
                        case Some(room) =>
                            val newState = state2.move(room)
                            println(s"Move $i by ${state2.turn} again: $room")
                            loop(newState)
                        case _ => throw new Exception("Not here!")
                    }
            }
        }
    }
}

def test_alpha_beta = {
    clearOutput
    val g = new Game(4)
    ABS.maxDepth = 12
    val t0 = epochTime
    g.play
    println(f"Alpha-beta game with depth=${ABS.maxDepth} took ${epochTime - t0}%.3f seconds")
}
if (runUnitTests)
    test_alpha_beta

