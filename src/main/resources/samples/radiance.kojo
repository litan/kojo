// Copyright (C) 2020 Amatullah Kayyumi
// The contents of this file are subject to
// the GNU General Public License Version 3 (http://www.gnu.org/copyleft/gpl.html)

clear()
setSpeed(fast)
val cb = canvasBounds
val clr = cm.radialGradient(0, 0, cyan, cb.height / 3, red, true)
setBackground(clr)

setPenColor(black)

repeat(18) {
    repeat(6) {
        right(150, 50)
        right(-90)
    }
    right(20)
}
invisible()
