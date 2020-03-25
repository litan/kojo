// Contributed by Saurabh Kapoor
//
// =================================================================
//
// Archimedes' estimation of PI
//

switchToDefaultPerspective()
val S = Staging
val unit = 100

def rads(degree: Double) = degree * math.Pi / 180.0
val headerStyle = "text-align:center;font-size:110%;color:maroon;"

var page: StoryPage = _
var header: xml.Node = _

var pages = new collection.mutable.ListBuffer[StoryPage]

def pgHeader(hdr: String) =
    <p style={headerStyle}>
        {new xml.Unparsed(hdr)}
        <hr/>
    </p>


header = pgHeader("Archmedes' Pi")

page = Page(
    name = "Archimedes's estimation of PI",
    body =
        <body style="margin:15px;">
            <h1>Archimedes' estimation of PI</h1>
            <p>
                We're going to explore the method Archimedes used for estimating the
                value of the number <em>PI</em>, represented by the symbol:
                <br/>
                <br/>
                {stFormula("""\pi""", 30)}.
            </p>
        </body>,
    code = {}
)

pages += page

page = Page(
    name = "Pi - Definition",
    body =
        <body style="margin:15px;">
            <h1>Definition of PI</h1>
            <p> PI is defined as the ratio of a circle's circumference (or perimeter) to its diameter.
                See the circle on the right, whose radius can be controlled using the slider.
                <br/>
                <br/>
                Note that the ratio of the circle's circumference to its diameter, as shown
                to the right of the circle, remains constant as you change the radius of the
                circle.
            </p>
            <p style="color:gray;font-size:95%;">
                The Greek letter π was adopted to represent the number PI because it
                is the first letter of the Greek word for perimeter - "περίμετρος".
            </p>
        </body>,
    code = {
        Mw.clear()
        Mw.hideAlgebraView()
        Mw.showAxes()
        Mw.variable("r", 4, 0, 10, 0.5, 50, 50)
        Mw.evaluate("c = Circle[(0, 0), r]")

        val t1 = """
"Diameter is: " + 2 r + "
Circumference is: " + Perimeter[c] + "
Ratio of Circumference to Diameter is: " + Perimeter[c] / (2 r) + " (3.14...)"
"""
        Mw.text(t1, 4, 4).show()
    }
)

pages += page

//  A method to return the points of a regular polygon of N points
//  inscribed in the circle with the supplied radius
// center: The center of the circle
// radius: The radius of the circle in which we want to inscribe the polygon
// nSides: The number of sides of the polygon
// initialAngle: The angle by which we want to rotate the polygon counter-clockwise
def getPolygonPoints(center: Point,
                     radius: Double,
                     nSides: Int,
                     initialAngle: Double) : List[Point] =
{
    // We will first generate the inital set of points on a circle with radius 100, along origin 0,0
    // with an initalAngle of (0,0).
    // Once we have those points, we will transform according to the args

    var initialPt = new Point(0, radius)

    def createPoint(count: Int) : Point = {
        var theta = rads(360.0/nSides * count + initialAngle);
        var cosTheta = math.cos(theta)
        var sinTheta = math.sin(theta)
        var newX = initialPt.x * cosTheta - initialPt.y * sinTheta
        var newY = initialPt.x * sinTheta + initialPt.y * cosTheta
        new Point(newX + center.x, newY + center.y)
    }

    for(i <- List.range(0, nSides)) yield createPoint(i)
}

// Draw the inner and outer polygons and trap a circle within them
def drawCircleAndPoly(radius: Double, nSides: Int) =
{
    val initialAngle = 180.0 / nSides
    val radiusOuter = radius / math.cos(rads(initialAngle))
    gridOff()
    axesOff()

    val innerPerimeter = math.sin(rads(initialAngle)) * nSides
    val outerPerimeter = math.tan(rads(initialAngle)) * nSides

    S.text("%1.10f < pi < %1.10f" format(innerPerimeter, outerPerimeter), -110, 160)
    S.text("pi ~ %1.10f" format((innerPerimeter + outerPerimeter)/2), -110, 140)
    val innerPoly = getPolygonPoints(S.O, radius, nSides, 0)
    val outerPoly = getPolygonPoints(S.O, radiusOuter, nSides, initialAngle)

    // Mark the origin
    S.dot(S.O)

    // Now draw the outer polygon
    S.setPenColor(blue)
    S.setFillColor(yellow)
    S.polygon(outerPoly)

    // Draw the circle
    S.setPenColor(green)
    S.setFillColor(new Color(255, 0, 255))
    S.circle(S.O, radius)

    // Draw the inner polygon
    S.setPenColor(red)
    S.setFillColor(new Color(255, 255, 255))
    S.polygon(innerPoly)
}

page = Page(
    name = "Polygons - inscribed and circumscribed",
    body =
        <body style="margin:15px;">
            <h1>Trapping the circle</h1>
            <p>
                Archimedes approximated the value of PI by trapping a circle between
                two polygons. Let's see this in action...
            </p>
            <p>The circle to the right is inscribed and circumscribed by a regular Pentagon (5 sided polygon).</p>
            <p>
                The perimeters of the two polygons provide upper and lower bounds on the circumference
                of the circle (and hence the value of PI).
            </p>
            <p>Try changing the number of sides below to see how the circumference of the circle (and
                the value of PI) gets <em>trapped</em> by the two Polygons.</p>
        </body>,
    code = {
        clear()
        drawCircleAndPoly(100, 5)
        stAddField("Sides", 5)
        stAddButton ("Make Polygons") {
            val size = stFieldValue("Sides", 5)
            S.reset()
            drawCircleAndPoly(100, size)
        }
    }
)

pages += page

val story = Story(pages.toSeq: _*)

stClear()
stPlayStory(story)
