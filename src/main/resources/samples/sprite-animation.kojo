clear()
setBackground(Color(200, 200, 200))
turtle0.setCostume(Background.trainTrack)

val t1 = newTurtle(-50, -180, Costume.womanWaving)
t1.act { self =>
    repeatWhile(self.position.y < 40) {
        self.changePosition(0.6, 1)
        self.scaleCostume(0.995)
        pause(0.03)
    }
}

val t2 = newTurtle(-250, 200)
t2.setCostumes(Costume.bat1, Costume.bat2)
t2.scaleCostume(0.5)
t2.act { self =>
    repeatWhile(self.position.x < 200) {
        self.changePosition(10, 0)
        self.nextCostume()
        pause(0.15)
    }
}
