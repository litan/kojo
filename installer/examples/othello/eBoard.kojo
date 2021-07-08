//#include cboard

class MoveCount { // the count of the next move counting from 1, 2, 3...
    private var count: Int = _
    def apply() = count
    def reset() = count = 1
    def incr() = count += 1
    def decr() = count -= 1
    reset()
}

class Player(val firstToPlay: Stone) {
    require(firstToPlay != Empty, "White or Black needs to go first")
    private var player: Stone = _
    def apply() = player
    def opponent = if (player == White) Black else White
    def reset() = set(firstToPlay)
    def change() = set(opponent)
    def set(p: Stone) = player = p
    reset()
}

class EBoard(
    val size:           Int, // board is size x size
    val startingPlayer: Stone,
    val variant:        Int) extends CoreBoard {
    require(3 < size, "Board needs to be 4x4 or bigger")
    require(20 > size, "Board needs to be 19x19 or smaller")
    require(startingPlayer != Empty, "White or Black needs to go first")
    private val board = Array.fill[Stone](size, size)(Empty)
    val player = new Player(startingPlayer)
    val moveCount = new MoveCount
    var lastMove: Option[Room] = _
    var skippedTurn = false

    def stone(y: Int, x: Int) = board(y)(x)
    def stone(room: Room) = board(room.y)(room.x)
    def setStone(y: Int)(x: Int)(s: Stone) = board(y)(x) = s
    def setStone(room: Room)(s: Stone) = board(room.y)(room.x) = s
    def print = for (y <- range.reverse) println(board(y).mkString(" "))
    def pretendMove(room: Room): Seq[Room] = moveCore(player(), room)
    def move(room: Room): Seq[Room] = {
        val touched = pretendMove(room)
        touched.map { r => setStone(r)(player()) }
        if (touched.size > 0) {
            player.change()
            moveCount.incr()
            skippedTurn = false
            if (moves(player()).isEmpty && !isGameOver) {
                skippedTurn = true
                println(s"There are no moves for ${player().cname}. ${player.opponent.cname} to play again")
                player.change()
            }
        }
        touched // and need to be (re-)painted
    }

    def score(short: Boolean) = {
        val (p1, p2) = if (short) ("W", "B") else ("White", "Black")
        s"$p1: ${count(White)}\n$p2: ${count(Black)}"
    }

    def placeSeq(rooms: Seq[(Int, Int)])(stone: Stone): Unit =
        rooms.map(p => Room(p._1, p._2)).foreach { setStone(_)(stone) }

    def reset(header: String = "") = {
        CoreBoard.newBoard(this, variant)
        player.reset()
        moveCount.reset()
        lastMove = None
        if (header.size > 0) {
            println(header)
            print
        }
    }

    reset()
}

def test_eboard = {
    val board = new EBoard(8, Black, 0)
    // note: Room(y, x) is printed: (x+1)x(y+1)
    assert(board.moves(Black).toString == "Vector(4x3, 3x4, 6x5, 5x6)", "black's starting moves")
    assert(board.moves(White).toString == "Vector(5x3, 6x4, 3x5, 4x6)", "white's starting moves")
    assert(board.neighborsToFlip(Room(3, 2), Black) == List(Neighbor(E, Room(3, 3))), "neighbors")
    assert(board.moveCore(Black, Room(3, 2)) == List(Room(3, 2), Room(3, 3)), "stones to flip")
}
if (runUnitTests)
    test_eboard
