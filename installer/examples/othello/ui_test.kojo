
def test_basic_board_drawing(dur: Double): Unit = {
    val board = new EBoard(8, Black, 0)
    val history = new History(board)
    val computerPlays = Empty
    val ui = new UI(board, history, computerPlays)
    board.print
    pause(dur)
    ui.makeTheMove(Room(3, 2)); pause(dur)
    ui.makeTheMove(Room(4, 2)); pause(dur)
    assert(ui.makeTheMove(Room(2, 2)) == false, "illegal move")
    assert(ui.makeTheMove(Room(5, 2)) == true, "good move"); pause(dur)
    assert(ui.makeTheMove(Room(4, 2)) == false, "another kind of illegal move")
    assert(ui.makeTheMove(Room(2, 2)) == true, "good move 2"); pause(dur)
    ui.newGame
    ui.makeTheMove(Room(2, 3))
    ui.newGame
}

def test_game_play = {
    val board = new EBoard(4, Black, 0)
    val history = new History(board)
    val computerPlays = Empty
    val ui = new UI(board, history, computerPlays)
    for (
        move <- List(
            Room(1, 0), Room(2, 0),
            Room(3, 0), Room(0, 2),
            Room(0, 3), Room(0, 0)
        )
    ) ui.makeTheMove(move)
    // now B to make two moves back to back:
    assert(board.player() == Black, "black back-to-back moves 1")
    ui.makeTheMove(Room(0, 1))
    assert(board.player() == Black, "black back-to-back moves 2")
    ui.makeTheMove(Room(2, 3))
    assert(board.player() == White, "finally white to move")
    for (move <- List(Room(3, 3), Room(3, 2))) ui.makeTheMove(move)
    // move W to make two back-to-back moves:
    assert(board.player() == White, "w1")
    ui.makeTheMove(Room(1, 3))
    assert(board.player() == White, "w2")
    ui.makeTheMove(Room(3, 1))
    assert(board.isGameOver == true, "the happy end")
    assert(board.score(White) == 10, "w=10")
    assert(board.score(Black) == 6, "b=6")
    ui.newGame
}
def test_computer_play = {
    val board = new EBoard(4, Black, 0)
    val history = new History(board)
    val computerPlays = Black
    val ui = new UI(board, history, computerPlays)
}
