//#include cboard

// this board is used only in alpha-beta search
class Board(val size: Int, var board: Vector[Int]) extends CoreBoard {
    def s2n(s: Stone) = s match {
        case Black => 2
        case White => 1
        case _     => 0
    }
    def n2s(n: Int) = n match {
        case 2 => Black
        case 1 => White
        case _ => Empty
    }
    def stone(r: Room) = n2s(board(r.y * size + r.x))
    def move(turn: Stone, room: Room): Board = place(turn, moveCore(turn, room))
    def place(s: Stone, r: Room) = new Board(
        size, board.updated(r.y * size + r.x, s2n(s)))
    def place(s: Stone, rooms: Seq[Room]) = {
        var newBoard = board
        for (r <- rooms) newBoard = newBoard.updated(r.y * size + r.x, s2n(s))
        new Board(size, newBoard)
    }
    def print(msg: String = "", lineHeader: String = "") = {
        for (y <- range.reverse) {
            val row = for (x <- range) yield stone(Room(y, x))
            println(row.mkString(lineHeader, " ", ""))
        }
        if (msg.size > 0) println(lineHeader + msg)
        for (p <- List(White, Black))
            println(s"$lineHeader ${p.name.capitalize}: ${count(p)}")
    }

    def placeSeq(rooms: Seq[(Int, Int)])(stone: Stone): Unit = {
        val newBoard = place(stone, rooms.map(p => Room(p._1, p._2)))
        board = newBoard.board
    }

}

def newBoard(size: Int, variant: Int = 0): Board = {
    var b = new Board(size, Vector.fill(size * size)(0))
    CoreBoard.newBoard(b, variant)
    b
}

def test_board = {
    val size = 6
    var b = new Board(size, Vector.fill(size * size)(0))
    assert(b.board == Vector(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), "1")
    val foo = b.place(White, Room(1, 1))
    assert(b.board == Vector(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), "2")
    b = b.place(White, Seq(Room(2, 2), Room(3, 3)))
    b = b.place(Black, Seq(Room(2, 3), Room(3, 2)))
    val newBoard = b
    // newBoard.print("t1")
    assert(newBoard.board == Vector(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), "3")
    var turn: Stone = Black
    for (
        (i, moves, finalBoard) <- List(
            (0, List(Room(2, 1), Room(3, 1), Room(4, 1)), Vector(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 2, 2, 1, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)),
            (1, List(Room(1, 2), Room(1, 3), Room(1, 4)), Vector(0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 2, 2, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)),
            (2, List(Room(4, 3), Room(4, 2), Room(4, 1)), Vector(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 0, 0, 0, 2, 2, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0)),
            (3, List(Room(3, 4), Room(2, 4), Room(1, 4)), Vector(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 1, 2, 2, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
        )
    ) {
        val payoffs = ArrayBuffer.empty[Int]
        for (move <- moves) {
            payoffs += b.movePayoff(move, turn)
            b = b.move(turn, move)
            turn = if (turn == Black) White else Black
        }
        //println(payoffs)
        assert(payoffs == ArrayBuffer(1, 1, 2), "payoffs $i")
        //println(b.board)
        //b.print()
        assert(b.board == finalBoard, s"board $i")
        b = newBoard
        turn = Black
    }
    // println("Board is functional.")
}
if (runUnitTests)
    test_board

def test_newBoard = {
    for (
        (n, expected) <- List(
            (4, Vector(0, 0, 0, 0, 0, 1, 2, 0, 0, 2, 1, 0, 0, 0, 0, 0)),
            (5, Vector(0, 0, 1, 0, 0, 0, 1, 2, 2, 0, 2, 1, 0, 1, 2, 0, 2, 2, 1, 0, 0, 0, 1, 0, 0)),
            (6, Vector(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)),
            (7, Vector(0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 1, 2, 0, 0, 2, 1, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 1, 2, 0, 0, 2, 1, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0))
        )
    ) {
        val b = if (n < 7) newBoard(n) else newBoard(n, 1)
        assert(b.board == expected, s"newBoard $n")
    }
    // println("New board test passed.")
}
if (runUnitTests)
    test_newBoard

