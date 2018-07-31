def room(w: Double, h: Double) = {
    val c = ColorMaker.darkOliveGreen
    fillColor(c) * penColor(c) -> Picture.rectangle(w, h)
}
val roomWest = room(50, 500)
val roomNorth = room(800, 50)
val roomEast = room(50, 500)
val roomSouth = room(800, 50)
val room1 = room(50, 150)
val room1m = room(50, 150)
val room2 = room(200, 100)
val room3 = room(100, 200)
val room4 = room(200, 50)
val room4m = room(200, 50)
val room5 = room(50, 400)
val room6 = room(100, 150)
val room6m = room(100, 150)
val room7 = room(250, 100)

roomWest.setPosition(-450, -250)
roomNorth.setPosition(-400, 250)
roomEast.setPosition(400, -250)
roomSouth.setPosition(-400, -300)
room1.setPosition(-350, -250)
room1m.setPosition(-350, 100)
room2.setPosition(-400, -50)
room3.setPosition(-150, -100)
room4.setPosition(-250, -200)
room4m.setPosition(-250, 150)
room5.setPosition(50, -200)
room6.setPosition(150, 100)
room6m.setPosition(150, -250)
room7.setPosition(100, -50)

val allRooms = picStack(roomWest, roomNorth, roomEast, roomSouth, room1, room2,
    room3, room4, room1m, room4m, room5, room6, room6m, room7)

