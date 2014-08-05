cleari()
setBackgroundH(Color(204, 204, 0), Color(245, 245, 0))
setPenColor(gray)
repeatFor(1 to 600) { e =>
    setPosition(
        random(600) - 300,
        random(400) - 200)
    setPenColor(
        Color(random(180),
            random(180), random(180),
            random(100) + 120))
    dot(30)
}
