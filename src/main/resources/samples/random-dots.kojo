cleari()
setBackgroundH(Color(255, 255, 153), Color(255, 255, 255))
setPenColor(gray)
repeatFor(1 to 200) { e =>
    setPosition(
        random(200) - 100,
        random(200) - 100)
    setPenColor(
        Color(random(150),
            random(150), random(150),
            random(100) + 120))
    dot(14)
}
