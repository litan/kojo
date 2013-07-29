val pageStyle = "background-color:#99CCFF; margin:15px;font-size:small;"
val centerStyle = "text-align:center;"
val headerStyle = "text-align:center;font-size:110%;color:maroon;"
val codeStyle = "font-size:90%;"
val smallNoteStyle = "color:dark-gray;font-size:95%;"
val sublistStyle = "margin-left:60px;"

val examples = List("""
// Turn left and right
D3.cameraTurn(30)
D3.cameraTurn(-30)
// You can also write D3.cameraLeft(30) and D3.cameraRight(30)

// Turn up and down
D3.cameraPitch(-30)
D3.cameraPitch(30)

// Roll clockwise and counterclockwise
D3.cameraRoll(-30)
D3.cameraRoll(30)

// Move forward and backward
D3.cameraForward(0.5)
D3.cameraForward(-0.5)
// You can also write D3.cameraBack(0.5)
""","""
D3.cameraStrafeUp(1)
D3.cameraStrafeDown(1)
D3.cameraStrafeLeft(1)
D3.cameraStrafeRight(1)
""","""
// Changes the focus of the camera lens. This is
// similar to using binoculars or a telescope,
// so that you can see objects very far away as
// if they were closer.
D3.cameraAngle(45)
D3.cameraAngle(90)

// Moves the camera to the point where you want it
// to be. Just give it the X, Y and Z coordinates.
D3.cameraMoveTo(1, 2, 3)

// Rotates the camera, so that it looks directly
// at a given point. Useful, if you want your
// camera to follow the Turtle, for example.
D3.cameraLookAt(0, 0, 1)
""","""
// First, let's clear the world and place the camera,
// So that we can clearly see everything
D3.clear()
D3.cameraMoveTo(2, 2, 2)

// We can move forward and turn just like in 2D
D3.forward(1)
D3.turn(90)

// We can tell the turtle to look up or down
D3.forward(1)
D3.pitch(-90)

// We can tell the turtle to roll
D3.forward(1)
D3.roll(90)

// Finally, we can tell the turtle to strafe
D3.strafeLeft(1)
D3.strafeUp(1)
""","""
D3.clear()
D3.lineWidth(0.5)
D3.color(brown)
D3.forward(3)
""","""
D3.clear()
D3.cameraMoveTo(1, 5, 6)
D3.cameraLookAt(0, 0, 3)
D3.cameraRoll(-15)
D3.axesOff()

def moveWithoutTrail(distance : Double) {
	D3.trailOff()
	D3.forward(distance)
	D3.trailOn()
}

def leaf() {
    	// We are drawing a short green line - a leaf
	D3.lineWidth(0.1)
	D3.color(green)
	D3.forward(1.5)
	// return back to the branch
	moveWithoutTrail(-1.5)
}

def branch() {
	// we are drawing a short brown line - a branch
	D3.lineWidth(0.25)
	D3.color(brown)
	D3.forward(1.5)
	// a branch has five leaves
	repeat(5) {
		D3.pitch(45)
		leaf()
		D3.pitch(-45)
		D3.roll(72)
	}
	// return back to the trunk
	moveWithoutTrail(-1.5)
}

def trunk() {
	// We are drawing a long brown line - a trunk
	D3.lineWidth(0.5)
	D3.color(brown)
	D3.forward(3)
	// a trunk has three branches
	repeat(3) {
		D3.pitch(45)
		branch()
		D3.pitch(-45)
		D3.roll(120)
	}
	// return back to ground
	moveWithoutTrail(-3)
}

trunk()
""","""
D3.clear()
D3.cameraMoveTo(3, 3, 3)
D3.color(red)
D3.sphere(2)
""","""
D3.clear()
D3.cameraMoveTo(1, 5, 6)
D3.cameraLookAt(0, 0, 3)
D3.cameraRoll(-15)
D3.axesOff()

def moveWithoutTrail(distance : Double) {
	D3.trailOff()
	D3.forward(distance)
	D3.trailOn()
}

def leaf() {
    	// We are drawing a short green line - a leaf
	D3.lineWidth(0.1)
	D3.color(green)
	D3.forward(1.5)
	// return back to the branch
	moveWithoutTrail(-1.5)
}

def branch() {
	// we are drawing a short brown line - a branch
	D3.lineWidth(0.25)
	D3.color(brown)
	D3.forward(1.5)
	// a branch has five leaves
	repeat(5) {
		D3.pitch(45)
		leaf()
		D3.pitch(-45)
		D3.roll(72)
	}
	// and an orange fruit
	D3.pitch(90)
	moveWithoutTrail(0.5)
	D3.color(orange)
	D3.sphere(0.3)
	moveWithoutTrail(-0.5)
	D3.pitch(-90)
	// return back to the trunk
	moveWithoutTrail(-1.5)
}

def trunk() {
	// We are drawing a long brown line - a trunk
	D3.lineWidth(0.5)
	D3.color(brown)
	D3.forward(3)
	// a trunk has three branches
	repeat(3) {
		D3.pitch(45)
		branch()
		D3.pitch(-45)
		D3.roll(120)
	}
	// return back to ground
	moveWithoutTrail(-3)
}

trunk()
moveWithoutTrail(-10)
D3.color(green)
D3.cube(20)
"""
)

def pgHeader(hdr: String) =
    <p style={headerStyle}>
        {hdr}
        <hr/>
    </p>

def runLink(n: Int) = "http://runhandler/example/" + n
def code(n: Int) = {
    <div style="background-color:CCFFFF;margin-top:10px"> 
        <hr/>
        <pre><code><a href={runLink(n)} style="text-decoration: none;font-size:x-small;">
                    {examples(n)}</a></code></pre>
        <hr/>
    </div>
}

var pages = new collection.mutable.ListBuffer[StoryPage]

def link(page: String) = "http://localpage/%s" format(page)
val homeLink = <div style={smallNoteStyle+centerStyle}><a href={link("home#7")}>Start Page</a></div>

pages += Page(
    name = "",
    body =
        <body style={pageStyle+centerStyle}>
            { for (i <- 1 to 5) yield {
                    <br/>
                }
            }
            <h1>Introduction to 3D graphics</h1>
            <em>Meet the 3D Turtle</em>
            { for (i <- 1 to 7) yield {
                    <br/>
                }
            }
            <p style={smallNoteStyle}>
                For best learning experience, make sure this StoryTeller board, the Turtle Canvas and the Script Editor aren't too small.
                Feel free to resize them so that you can comfortably read, write and see the results.
                When you are ready, press the "Begin" button.
                
            </p>
            {
	       stAddButton ("Begin") {
	       		D3.clear()
	       		D3.cameraMoveTo(1, 1, 1)
	       	}
	       }
        </body>
)

pages += IncrPage(
    name = "welcome",
    style=pageStyle,
    body =  List(
        Para(
            {pgHeader("Welcome to the 3D world")}
        ),
        Para(
            <p>
                What you see right now is the 3D canvas, on which the 3D Turtle can draw lines and shapes such as spheres and cubes.
                3D means three dimensions - width, height and depth. You already know the 2D Turtle, which could move up, down, left and right.
                The 3D Turtle can also come closer to you or move away from you. The computer screen is two dimensional (flat), so you won't actually
                see the 3D Turtle jumping out of your screen, but when he is close to you he'll be quite big, just like a person standing next to
                you seems bigger than someone far away. Similarly, when the 3D Turtle is far away, he will be smaller.
            <br/>
            <br/>
                Right now, you can only see the Turtle and the axes. This world has three dimensions, so we have three axes.
                The X axis is red, the Y axis is green and the Z axis is blue. Soon, we will tell the Turtle to draw some lines and shapes,
                but first let's learn how to move around in the 3D world.
            </p>
        )
    )
)

pages += IncrPage(
    name = "camera",
    style=pageStyle,
    body = List(
        Para(
            {pgHeader("3D camera")}
        ),
        Para(
            <p>
                Do you remember how you could drag the 2D canvas, so that you can see something that was drawn outside of the screen?
                In 3D, you can control a virtual camera, representing your point of view, to achieve a similar effect.
                If you have trouble understanding what this camera is, try to imagine yourself standing inside of the Turtle's world.
                Your eyes see this world from a certain point, and if you move around, this point will change determining what
                you see and how you see it.
            <br/>
            <br/>
                But, since you are not really inside the Turtle's world, you instead control a camera that makes pictures of this world and
                displays them on your screen. You can control this camera in two ways:
                <ul>
                	<li>Commands written in the Script Editor</li>
                	<li>Dragging the 3D Canvas with your mouse</li>
                </ul>
            </p>
        )
    )
)

pages += IncrPage(
    name = "mouseControl",
    style=pageStyle,
    body = List(
        Para(
            {pgHeader("Controlling the camera with your mouse")}
        ),
        Para(
            <p>
                If you want to control the camera with your mouse, I recommend that you decrease the quality first, using the slider
                to the right of the canvas. With lower quality, the camera will be able to take pictures more often, meaning it will
                be easier to navigate the 3D world. In high quality, the pictures will look better, but every time you move the camera,
                you'll have to wait a while until you see the effects of your action.
            <br/>
            <br/>
                First, place your mouse cursor inside the picture. Press the left mouse button and without releasing it move slightly to
                the left or right. You'll notice that the camera rotates, as if you were turning your head left or righ.
                If you drag the mouse up or down, it'll be similar to looking up or down. Don't move your cursor too much, or you won't see
                the turtle anymore! It is easy to get lost if you perform big movements.
            <br/>
            <br/>
                Now, try dragging the cursor but this time press the right button. When you drag the cursor left or right, the camera rotates
                in place. This is called "rolling". It is as if you were tilting your head towards your shoulders. If you drag the cursor
                up or down, the camera will move forward or backward. Now, you can try to position the camera anywhere you want.
            </p>
        )
    )
)

pages += IncrPage(
    name = "mouseControl2",
    style=pageStyle,
    body = List(
        Para(
            {pgHeader("Controlling the camera with your mouse")}
        ),
        Para(
            <p>
                Instead of moving forward and backward, you can also move your camera to the sides. This is done by dragging the cursor
                with both your left mouse button pressed and the <b>Shift</b> button pressed on your keyboard. Try it now.
            <br/>
            <br/>
                You probably noticed that it is hard to move the cursor perfectly horizontally, without moving a little bit vertically.
                If you want to rotate the camera left or right, but you don't want to accidentally rotate up or down, even a tiny bit,
                you can restrict the camera's rotation to horizontal by keeping the <b>Ctrl</b> button pressed on your keyboard. This
                also works when you try to roll with the right mouse button pressed.
            <br/>
            <br/>
                You can also restrict the camera to vertical rotation by keeping the <b>Alt</b> key pressed. This also works when you
                try to move forward or backward with your right mouse button pressed.
            <br/>
            <br/>
            	That's it! Now you know all you need to control the camera with your mouse.
            </p>
        )
    )
)

pages += IncrPage(
    name = "cameraCommands",
    style=pageStyle,
    body = List(
        Para(
            {pgHeader("Controlling the camera with commands")}
        ),
        Para(
            <p>
                Let's say you want to make an animation, where the camera rotates around the Turtle.
                To make such an animation, you need to control the camera with a Kojo script, similar to the way
                you can control the 2D Turtle with commands such as <i>forward(10)</i> and <i>rotate(90)</i>.
            <br/>
            <br/>
            	I have good news for you! All you could do by dragging the mouse, can be easily done with commands
            	that are very similar to the ones you already know.
            <br/>
            <br/>
            	Set the quality back to unlimited, then try running this script in your Script Editor, and you can
            	probably guess which command does what just by looking at their names and descriptions.
            <br/>
            <br/>
                {code(0)}
            </p>
        )
    )
)

pages += IncrPage(
    name = "cameraCommands",
    style=pageStyle,
    body = List(
        Para(
            {pgHeader("Controlling the camera with commands")}
        ),
        Para(
            <p>
                You can also move the camera up, down, left or right, similar to the movement
                you did when dragging the canvas with the <b>Shift</b> button pressed.
                This movement is known as panning or strafing. The first name comes from photography,
                and the second one from computer games.
            <br/>
            <br/>
                {code(1)}
            <br/>
            <br/>
            	There are three more commands that you may find useful, since you can't get their effects
            	by dragging the cursor.
            <br/>
            <br/>
                {code(2)}
            </p>
        )
    )
)

pages += IncrPage(
    name = "turtleCommands",
    style=pageStyle,
    body = List(
        Para(
            {pgHeader("Controlling the turtle")}
        ),
        Para(
            <p>
            	Now that you know how to move around, let's tell the turtle to draw something!
            	Don't worry, the commands to move the 3D Turtle are quite similar to the ones you
            	used to control the camera, so you shouldn't have trouble remembering them. The
            	3D Turtle draws lines as he moves just like his 2D brother.
            <br/>
            <br/>
                {code(3)}
            </p>
        )
    )
)

pages += IncrPage(
    name = "turtleCommands2",
    style=pageStyle,
    body = List(
        Para(
            {pgHeader("Controlling the turtle")}
        ),
        Para(
            <p>
            	Our friend, the 3D Turtle, would like to draw an orange tree. To begin our picture,
            	we need a tree trunk. In 2D, you'd probably draw a thick brown line. In 3D, we
            	will draw a cylinder - a shape you get when you drag a circle along a straight line segment
            	perpendicular to the circle. In fact, in 3D lines are just very thin cylinders.
            	Here's a script that tells the turtle to draw a thick, brown cylinder:
            <br/>
            <br/>
                {code(4)}
            <br/>
            <br/>
            	You probably noticed that distances in 3D are different than distances in 2D. When working
            	with the 2D Turtle, you probably used distances like 100, 200, and similar large numbers.
            	This is because the 2D Turtle uses pixels as a unit of distance. Pixels are the smallest
            	points visible on the computer screen. In 3D, we cannot use pixels as a unit of distance,
            	since object's length on the screen depends on how far it is from the camera. A single
            	pixel could represent a very short distance when working with nearby objects, or a very
            	long distance when referring to distant objects. Instead, we use the axes to measure distance.
            	For example, the first tick on the red X axis has coordinates x=1, y=0, z=0. The point where
            	the three axes meet has coordinates x=0, y=0, z=0. Thus, fractional distances simply mean a
            	fraction of one unit of length.
            </p>
        )
    )
)

pages += IncrPage(
    name = "turtleCommands3",
    style=pageStyle,
    body = List(
        Para(
            {pgHeader("Controlling the turtle")}
        ),
        Para(
            <p>
            	Here's a list of all the commands you can use to control the 3D Turtle:
                <ul>
                	<li><i>forward(distance)</i> and <i>back(distance)</i> - to move the Turtle</li>
                	<li><i>turn(angle)</i>, <i>pitch(angle)</i>, <i>roll(angle)</i> - to rotate the Turtle</li>
                	<li><i>left(angle)</i> and <i>right(angle)</i> - the alternative ways to turn the turtle
                	if you prefer to work with positive numbers</li>
                	<li><i>strafeLeft(distance)</i>, <i>strafeRight(distance)</i>,
                	<i>strafeUp(distance)</i>, <i>strafeDown(distance)</i></li>
                	<li><i>moveTo(x, y, z)</i> and <i>lookAt(x, y, z)</i> - convenient commands to move and rotate
                	the Turtle exactly the way you want</li>
                	<li><i>lineWidth(width)</i> - to change the thickness of lines drawn by the Turtle</li>
                	<li><i>trailOff()</i> and <i>trailOn()</i> - to tell the turtle to stop or resume drawing lines</li>
                	<li><i>color(color's name)</i> - to change the color of lines drawn</li>
                	<li><i>color(red, green, blue)</i> - another way to change the color. Specify the three component
                	colors either with integers between 0 and 255, or with fractions between 0 and 1, whichever is easier
                	for you</li>
                </ul>
            </p>
        )
    )
)

pages += IncrPage(
    name = "tree",
    style=pageStyle,
    body = List(
        Para(
            {pgHeader("Almost complete fruit tree")}
        ),
        Para(
            <p>
            	Here's a slightly longer script, but don't worry, it's nothing complex.
            	First, we clear the canvas and place the camera. Then, for convenience,
            	we declare a method that allows us to move the turtle without creating
            	unnecessary lines. Then, we define leaves as short, green lines.
            	We define branches as short, brown lines that end with five leaves.
            	Finally, we define a trunk as a long, thick brown line that ends with three branches.
            	The last command tells the turtle to draw the whole tree.
            <br/>
            <br/>
                {code(5)}
            </p>
        )
    )
)

pages += IncrPage(
    name = "shapes",
    style=pageStyle,
    body = List(
        Para(
            {pgHeader("Shapes")}
        ),
        Para(
            <p>
            	There's one thing missing on the tree. We need to add the fruits!
            	Oranges are spherical, and the problem is that drawing a sphere with
            	only lines at your disposal is very hard, much harder than drawing a circle
            	in 2D. Luckily, the 3D Turtle knows how to draw other shapes right away,
            	such as spheres and cubes. Here's an example:
            <br/>
            <br/>
                {code(6)}
            </p>
        )
    )
)

pages += IncrPage(
    name = "fruitTree",
    style=pageStyle,
    body = List(
        Para(
            {pgHeader("Finished fruit tree")}
        ),
        Para(
            <p>
            	Here's an updated version of the previous script. Finally, we have an
            	orange tree with fruits, and at the end the Turtle adds a green
            	cube serving as ground upon which the tree grows. Feel free to experiment
            	with the script, then try to create something new with the commands
            	you have learned. The 3D Turtle will surely enjoy playing with you!
            <br/>
            <br/>
                {code(7)}
            </p>
        )
    )
)

val story = Story(pages: _*)
stClear()
stAddLinkHandler("example", story) {idx: Int =>
    stSetScript(examples(idx))
    stClickRunButton()
}
stPlayStory(story)

