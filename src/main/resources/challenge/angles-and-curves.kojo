// This is a Story that runs the code provided below in 'challenge' mode for young kids

// Config params
val ShowMistakes = false // keep count of (and show) the mistakes made
val ChallengePenWidth = 6 // pen size of the challenge figure
val BlockNextLevel = false // block next level till current level is completed
val NumCmdChoices = 4 // number of options to show in the command dropdown

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

