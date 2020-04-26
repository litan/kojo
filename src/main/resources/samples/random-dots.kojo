cleari()
setBackgroundH(cm.rgb(180, 180, 0), cm.rgb(245, 245, 0))
repeatFor(1 to 600) { e =>
    setPosition(random(600) - 300, random(400) - 200)
    setPenColor(cm.rgba(random(180), random(180), random(180), random(100) + 100))
    dot(35)
}
