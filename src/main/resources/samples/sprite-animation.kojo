// This sample shows you how to use costumes, backgrounds, and sounds

// When we give a turtle a costume that looks nothing like a turtle, we informally 
// call it a sprite  

// To access and play with many different costumes, backgrounds, and sounds, 
// you can download (and unzip) the following media zipfile:
// code.google.com/p/kojo/downloads/detail?name=scratch-media.zip
clear()
setBackground(Color(200, 200, 200))
// if you want a background image, make it the original turtle's costume
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
    stopActivity() // stops any ongoing animation or music
}
// play background music in a loop
playMp3Loop(Sound.medieval1)
