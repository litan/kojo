// Contributed by Anay Kamat

cleari()

def panati =
    penWidth(2) *
        penColor(cm.black) *
        fillColor(cm.linearGradient(0, 10, cm.red, 0, -25, cm.brown)) ->
        Picture.fromPath { s =>
            s.moveTo(0, 0)
            s.arc(-100, 0, -60)
            s.arc(100, 0, 120)
            s.arc(0, 0, -60)
        }

def jyoti =
    penWidth(3) *
        penColor(cm.yellow) *
        fillColor(cm.linearGradient(0, 0, cm.red, 0, 130, cm.yellow)) ->
        Picture.fromPath { s =>
            s.moveTo(0, 0)
            s.arc(0, 139, 90)
            s.arc(0, 0, 90)
        }

case class ScaleState(frameIndex: Int) {
    def scaleFactor: Double = {
        val growShrinkValue: Double = Math.sin(Math.toRadians(frameIndex))
        val scaleDifference = 0.1 * growShrinkValue
        1.0 - scaleDifference
    }

    def nextState = ScaleState(frameIndex + 5)
}

def diya(scaleState: ScaleState): Picture =
    picStack(
        scale(scaleState.scaleFactor) -> jyoti,
        panati
    )

def updateState(state: ScaleState) = state.nextState

setBackground(cm.darkSlateBlue)
animateWithRedraw(ScaleState(0), updateState, diya)
