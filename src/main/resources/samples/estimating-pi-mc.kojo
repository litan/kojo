/*
  π calculation with Monte Carlo method. Contributed by Massimo Maria Ghisalberti.
  For more details, take a look at: https://academo.org/demos/estimating-pi-monte-carlo/
*/
def π(totalPoints: Int): Double = {
    def len(x: Double, y: Double) = math.sqrt(math.pow(x, 2) + math.pow(y, 2))
    var pointsInsideCircle = 0
    repeat(totalPoints) {
        val newPointX = randomDouble(1)
        val newPointY = randomDouble(1)
        val newPointInsideCircle =
            len(newPointX - 0.5, newPointY - 0.5) <= 0.5
        if (newPointInsideCircle) {
            pointsInsideCircle += 1
        }
    }
    pointsInsideCircle * 4.0 / totalPoints
}

println(s"100 points: ${π(100)}")
println(s"1000 points: ${π(1000)}")
println(s"10000 points: ${π(10000)}")
println(s"100000 points: ${π(100000)}")
println(s"1000000 points: ${π(1000000)}")
// println(s"10000000 points: ${π(10000000)}")
