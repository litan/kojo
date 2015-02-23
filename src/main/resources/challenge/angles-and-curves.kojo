// This is a Story that runs the code provided below in 'challenge' mode for young kids

// Config params
val ChallengePenWidth = 6
val BlockNextLevel = false

// The included runner runs the story
// #include /challenge/runner.kojo

// The challenge code is specified below -- you can modify it to suit your needs.
lazy val challengeLevels = Seq("""right(360, 50)
right(360, 100)
right(360, 200)
""",
    """right(90, 100)
right(90)
right(90, 100)
""",
    """right(45, 150)
right(90)
right(45, 150)
right(90)
forward(88)
"""
)

lazy val levelsHelp = Map(
    1 -> <div>
        <strong>Objective: </strong> Learning about circles - angles and radius.
    </div>,
    3 -> <div>
        <strong>Objective: </strong> Learning about circles - angles and radius.
    </div>
)

