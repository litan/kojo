//#include ui

val welcome = <html> Welcome to Kojothello! </html>.toString

val ui_variant = DropDown("Traditional", "Variation")
val ui_board = DropDown(8, 4, 5, 6, 7, 8, 9, 10, 11, 12)
val ui_toPlay = DropDown("Black", "White")
val ui_level = DropDown(4, 1, 2, 3, 4, 5, 6, 7, 8)
val ui_computer = DropDown("Two Players", "Computer plays Black", "Computer plays White")

def menu = {
    cleari
    drawCentered(scale(2.0) ->
        Picture.widget(
            ColPanel(
                RowPanel(Label(welcome)),
                RowPanel(Label("Preplaced Stones"), ui_variant),
                RowPanel(Label("Board Size"), ui_board),
                RowPanel(Label("First Move"), ui_toPlay),
                RowPanel(Label("Level"), ui_level),
                RowPanel(Label("Players"), ui_computer),
                RowPanel(Button("Play!") {
                    val size = ui_board.value
                    val variant = ui_variant.value match {
                        case "Traditional" => 0
                        case _             => 1
                    }
                    if (variant != 0 && size < 7) {
                        println("We need a 7x7 or larger board for preplaced stone variation")
                    }
                    else {
                        val firstPlayer = ui_toPlay.value match {
                            case "Black" => Black
                            case _       => White
                        }
                        val computerPlays = ui_computer.value match {
                            case "Two Players"          => Empty
                            case "Computer plays Black" => Black
                            case _                      => White
                        }
                        val board = new EBoard(size, firstPlayer, variant)
                        clearOutput
                        ABS.maxDepth = ui_level.value
                        new UI(board, new History(board), computerPlays)
                    }
                }
                )
            )
        )
    )
}
menu
