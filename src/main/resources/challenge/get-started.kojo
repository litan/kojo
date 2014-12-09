// This is a Story that runs the code provided below in 'challenge' mode for young kids

// Config params
val ChallengePenWidth = 6 // pen size of the challenge figure
val BlockNextLevel = false // block next level till current level is completed
val NumCmdChoices = 4 // number of options to show in the command dropdown

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
    """repeat(3) {
    forward(100)
    right(120)
}
""",
    """repeat(5) {
    forward(100)
    right(72)
}
""",
    """repeat(6) {
    repeat(2) {
        forward(40)
        right(90)
        forward(80)
        right(90)
    }
    hop(40)
    right(90)
    hop(60)
    left(90)
}
"""
)

lazy val levelsHelp = Map(
    1 -> <div>
    Learning Opportunities:
        <ul>
            <li>The idea of unit length and distances</li>
            <li>Length measurement</li>
            <li>Right angles</li>
            <li>Angle measurement</li>
            <li><tt>forward</tt>, <tt>right</tt>, and <tt>left</tt> commands</li>
            <li>Efficient search among options (without measurement)</li>
            <li>Mental Sequencing of commands</li>
            <li>Logical thinking</li>
        </ul>
      Control Buttons:
      <ul>
          <li><em>Grid</em> – Turns grid on/off. Useful for approximate measurement.</li>
          <li><em>Tools</em> – Turns scale and protractor on/off. 
            Either tool can be moved by dragging and rotated by Shift-Dragging.
            Useful for precise measurement.</li>
          <li><em>Reset Zoom</em> – Resets pan and zoom in the Canvas. 
          Useful for resetting the canvas after panning/zooming during measurement.</li>
      </ul>
      
    </div>,
    2 -> <div>
    Learning Opportunities:
        <ul>
            <li>Length measurement</li>
            <li>Right angles</li>
            <li>Angle measurement</li>
            <li>Rectangles</li>
            <li>Efficient search among options (without measurement)</li>
            <li>Mental Sequencing of commands</li>
            <li>Logical thinking</li>
            <li>Practice of learnt ideas</li>
        </ul>
    </div>,
    3 -> <div>
    Learning Opportunities:
        <ul>
            <li>Length measurement</li>
            <li>45 degree angles</li>
            <li>Angle measurement</li>
            <li>Efficient search among options (without measurement)</li>
            <li>Mental Sequencing of commands</li>
            <li>Logical thinking</li>
            <li>Practice of learnt ideas</li>
        </ul>
    </div>,
    4 -> <div>
    Objective:
        <ul>
            <li>Practice - applying learnt ideas to make a hut.</li>
        </ul>
    </div>,
    5 -> <div>
    Objective:
        <ul>
            <li>Practice - applying learnt ideas to make a mini-castle.</li>
        </ul>
    </div>,
    6 -> <div>
    Learning Opportunities:
        <ul>
            <li><tt>hop</tt> command</li>
            <li>Using Arithmetic to determine dimensions (without guessing or measurement)</li>
            <li>Practice of learnt ideas within a larger program</li>
        </ul>
    </div>,
    7 -> <div>
    Learning Opportunities:
        <ul>
            <li>Starting to identify patterns</li>
            <li>Squares</li>
            <li>Practice of learnt ideas</li>
        </ul>
    </div>,
    8 -> <div>
    Learning Opportunities:
        <ul>
            <li>Using an identified pattern to write a shorter program to accomplish something</li>
            <li><tt>repeat</tt> command</li>
            <li>Analytical thinking</li>
            <li>Practice of learnt ideas</li>
        </ul>
    </div>,
    9 -> <div>
    Learning Opportunities:
        <ul>
            <li>Using the <tt>repeat</tt> command</li>
            <li>Triangles</li>
            <li>Practice of learnt ideas</li>
        </ul>
    </div>,
    10 -> <div>
    Learning Opportunities:
        <ul>
            <li>Using the <tt>repeat</tt> command</li>
            <li>Pentagons</li>
            <li>Practice of learnt ideas</li>
        </ul>
    </div>,
    11 -> <div>
    Learning Opportunities:
        <ul>
            <li>Identifying a two-level pattern</li>
            <li>nested <tt>repeat</tt> commands</li>
            <li>Analytical thinking</li>
            <li>Practice of learnt ideas</li>
        </ul>
    </div>
)
