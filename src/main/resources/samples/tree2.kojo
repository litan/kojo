// Contributed by Anay Kamat

cleari()

def singleBranch = penColor(black) -> Picture.fromVertexShape { s =>
    import s._
    beginShape()
    vertex(0, 0)
    vertex(0, 100)
    endShape()
}

def branches(penThickness: Double, step: Int): Picture =
    if (step == 0) {
        penWidth(penThickness) -> singleBranch
    }
    else {
        picStack(
            penWidth(penThickness) -> singleBranch,
            trans(0, 100) * scale(0.5) -> branches(penThickness * 2, step - 1),
            trans(0, 50) * rot(30) * scale(0.5) -> branches(penThickness * 2, step - 1),
            trans(0, 50) * rot(-30) * scale(0.5) -> branches(penThickness * 2, step - 1)
        )
    }

def lightBlue = cm.rgb(172, 212, 250)

setBackground(cm.linearGradient(0, -100, brown, 0, 100, lightBlue))
drawCentered(branches(1, 8))
