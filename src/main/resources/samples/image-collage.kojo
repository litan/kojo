def woman = PicShape.image(Costume.womanWaving)
def car = PicShape.image(Costume.car)
def pencil = PicShape.image(Costume.pencil)
def bat1 = PicShape.image(Costume.bat1)
def bat2 = PicShape.image(Costume.bat2)
def title = penColor(purple) -> Picture {
    setPenFont(Font("Serif", 18))
    write("Batmania!")
}

val pic = picStack(
    trans(-150, -150) -> woman,
    trans(-50, 91) * rot(-10) -> car,
    trans(144, 24) -> pencil,
    trans(-50, 50) * scale(0.5) -> bat1,
    trans(150, 50) * scale(0.5) -> bat2,
    trans(-72, 50) -> title
)

cleari()
draw(pic)
