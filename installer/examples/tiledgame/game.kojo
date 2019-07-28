clearOutput()
cleari()

println("To win the game, drink the red potion on the bottom/right and return to the starting point to drink the green potion.")
println(s"\nTo modify the game layout, change $installDir/examples/tiledgame/level1.tmx using the Tiled editor from www.mapeditor.org")

scroll(-canvasBounds.x, canvasBounds.y)

// Tiled map layer of tiles that you collide with
val collisionLayer = 1
val foodLayer = 2

class Player(tx: Int, ty: Int, world: TileWorld) {
    val playerPos = world.tileToKojo(TileXY(tx, ty))
    val sheet = SpriteSheet("player.png", 30, 42)

    val stillRight = picBatch(Picture.image(sheet.imageAt(0, 0)))
    val stillLeft = picBatch(Picture.image(sheet.imageAt(0, 1)))

    val runningRight = picBatch(List(
        sheet.imageAt(0, 2),
        sheet.imageAt(1, 2),
        sheet.imageAt(2, 2),
        sheet.imageAt(3, 2),
        sheet.imageAt(4, 2)
    ).map(Picture.image))

    val runningLeft = picBatch(List(
        sheet.imageAt(0, 3),
        sheet.imageAt(1, 3),
        sheet.imageAt(2, 3),
        sheet.imageAt(3, 3),
        sheet.imageAt(4, 3)
    ).map(Picture.image))

    val jumpingRight = picBatch(List(
        sheet.imageAt(0, 0),
        sheet.imageAt(1, 0),
        sheet.imageAt(2, 0),
        sheet.imageAt(3, 0)
    ).map(Picture.image))

    val jumpingLeft = picBatch(List(
        sheet.imageAt(0, 1),
        sheet.imageAt(1, 1),
        sheet.imageAt(2, 1),
        sheet.imageAt(3, 1)
    ).map(Picture.image))

    var currentPic = stillRight
    currentPic.setPosition(playerPos)

    var facingRight = true
    val gravity = -0.1
    val speedX = 3.0
    var speedY = -1.0
    var inJump = false

    def step() {
        stepCollisions()
        stepFood()
    }

    var goalEnabled = false
    def stepFood() {
        if (currentPic.collidesWith(halfwayGoal)) {
            halfwayGoal.erase()
            goal.setOpacity(1)
            goalEnabled = true
        }
        if (goalEnabled) {
            if (currentPic.collidesWith(goal)) {
                stopAnimation()
                drawCenteredMessage("You Win!", white, 20)
            }
        }
    }

    def stepCollisions() {
        if (isKeyPressed(Kc.VK_RIGHT)) {
            facingRight = true
            updateImage(runningRight)
            currentPic.translate(speedX, 0)
            if (world.hasTileAtRight(currentPic, collisionLayer)) {
                world.moveToTileLeft(currentPic)
            }
        }
        else if (isKeyPressed(Kc.VK_LEFT)) {
            facingRight = false
            updateImage(runningLeft)
            currentPic.translate(-speedX, 0)
            if (world.hasTileAtLeft(currentPic, collisionLayer)) {
                world.moveToTileRight(currentPic)
            }
        }
        else {
            if (facingRight) {
                updateImage(stillRight)
            }
            else {
                updateImage(stillLeft)
            }
        }

        if (isKeyPressed(Kc.VK_UP)) {
            if (!inJump) {
                speedY = 5
            }
        }

        speedY += gravity
        speedY = math.max(speedY, -10)
        currentPic.translate(0, speedY)

        if (world.hasTileBelow(currentPic, collisionLayer)) {
            inJump = false
            world.moveToTileAbove(currentPic)
            speedY = -1
        }
        else {
            inJump = true
            if (world.hasTileAbove(currentPic, collisionLayer)) {
                world.moveToTileBelow(currentPic)
                speedY = -1
            }
        }

        if (inJump) {
            if (facingRight) {
                updateImage(jumpingRight)
            }
            else {
                updateImage(jumpingLeft)
            }
            currentPic.showNext(200)
        }
        else {
            currentPic.showNext()
        }
        scrollIfNeeded()
    }

    var cb = canvasBounds
    def scrollIfNeeded() {
        val threshold = 200
        val pos = currentPic.position
        if (cb.x + cb.width - pos.x < threshold) {
            scroll(speedX, 0)
            cb = canvasBounds
        }
        else if (pos.x - cb.x < threshold) {
            scroll(-speedX, 0)
            cb = canvasBounds
        }
    }

    def updateImage(newPic: BatchPics) {
        if (newPic != currentPic) {
            currentPic.invisible()
            newPic.visible()
            newPic.setPosition(currentPic.position)
            currentPic = newPic
        }
    }

    def draw() {
        drawAndHide(stillLeft, runningRight, runningLeft, jumpingRight, jumpingLeft)
        currentPic.draw()
    }
}

class AttackerUpDown(tx: Int, ty: Int, world: TileWorld) {
    val playerPos = world.tileToKojo(TileXY(tx, ty))
    val sheet = SpriteSheet("tiles.png", 24, 24)
    var currentPic = picBatch(List(
        sheet.imageAt(0, 6),
        sheet.imageAt(1, 6)
    ).map(Picture.image))

    currentPic.setPosition(playerPos)

    val gravity = -0.03
    //    var speedX = 0.0
    var speedY = -2.0

    def step() {
        speedY += gravity
        speedY = math.max(speedY, -10)
        currentPic.translate(0, speedY)
        currentPic.showNext()
        if (world.hasTileBelow(currentPic, collisionLayer)) {
            world.moveToTileAbove(currentPic)
            speedY = 5
        }
        else if (world.hasTileAbove(currentPic, collisionLayer)) {
            world.moveToTileBelow(currentPic)
            speedY = -2
        }
    }

    def updateImage(newPic: BatchPics) {
        if (newPic != currentPic) {
            currentPic.invisible()
            newPic.visible()
            newPic.setPosition(currentPic.position)
            currentPic = newPic
        }
    }

    def draw() {
        currentPic.draw()
    }
}

val tileWorld =
    new TileWorld("level1.tmx")

// Create a player object and set the level it is in
val player = new Player(9, 5, tileWorld)
val attackers = List(
    new AttackerUpDown(14, 2, tileWorld),
    new AttackerUpDown(18, 2, tileWorld),
    new AttackerUpDown(22, 2, tileWorld),
    new AttackerUpDown(32, 2, tileWorld),
    new AttackerUpDown(35, 3, tileWorld)
)

val goal = trans(12, 12) * fillColor(cm.greenYellow) * penColor(black) -> Picture.circle(10)
goal.setPosition(tileWorld.tileToKojo(TileXY(9, 2)))
goal.setOpacity(0.2)
draw(goal)

val halfwayGoal = trans(12, 12) * fillColor(cm.red) * penColor(black) -> Picture.circle(10)
halfwayGoal.setPosition(tileWorld.tileToKojo(TileXY(41, 15)))
draw(halfwayGoal)

tileWorld.draw()
player.draw()
attackers.foreach { attacker =>
    attacker.draw()
}

animate {
    tileWorld.step()
    player.step()
    attackers.foreach { attacker =>
        attacker.step()
        if (player.currentPic.collidesWith(attacker.currentPic)) {
            player.currentPic.rotate(30)
            stopAnimation()
        }
    }
}

activateCanvas()

// game resources sourced from: https://github.com/pricheal/pygame-tiled-demo