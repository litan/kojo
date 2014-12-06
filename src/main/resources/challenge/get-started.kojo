// This is a Story that runs the code provided below in 'challenge' mode for young kids

// Config params
val ChallengePenWidth = 6
val BlockNextLevel = false

// The included runner runs the story
// #include /challenge/runner.kojo

// The challenge code is specified below -- you can modify it to suit your needs.
lazy val challengeLevels = Seq("""forward(50)
right(90)
forward(100)
left(90)
forward(50)
""",
    """forward(100)
right(90)
forward(60)
right(90)
forward(100)
right(90)
forward(60)
right(90)
""",
    """forward(50)
right(45)
forward(50)
right(45)
forward(50)
left(45)
forward(50)
left(45)
forward(50)
""",
    """forward(100)
right(45)
forward(71)
right(90)
forward(71)
right(45)
forward(100)
right(90)
forward(100)
""",
    """forward(50)
right(45)
forward(50)
right(90)
forward(50)
left(45)
forward(50)
left(45)
forward(50)
right(90)
forward(50)
right(45)
forward(50)
right(90)
forward(191)
""",
    """forward(200)
right(90)
forward(200)
right(90)
forward(200)
right(90)
forward(200)
right(90)
hop(160)
right(90)
hop(40)
forward(35)
right(90)
forward(35)
right(90)
forward(35)
right(90)
forward(35)
right(90)
hop(85)
forward(35)
right(90)
forward(35)
right(90)
forward(35)
right(90)
forward(35)
hop(-100)
left(90)
forward(50)
right(45)
forward(10)
""",
    """forward(100)
right(90)
forward(100)
right(90)
forward(100)
right(90)
forward(100)
right(90)
""",
    """repeat(4) {
    forward(100)
    right(90)
}
""",
    """repeat(6) {
    repeat(4) {
        forward(50)
        right(90)
    }
    hop(50)
    right(90)
    hop(40)
    left(90)
}
"""
)

lazy val levelsHelp = Map(
    6 -> <div>
    </div>
)

