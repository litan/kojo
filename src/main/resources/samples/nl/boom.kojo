def boom(afstand: Decimaal) {
   if (afstand > 4) {
        lijnDikte(afstand/7)
        penKleur(kleur(afstand.toInt, math.abs(255-afstand*3).toInt, 125))
        vooruit(afstand)
        rechts(25)
        boom(afstand*0.8-2)
        links(45)
        boom(afstand-10)
        rechts(20)
        vooruit(-afstand)
    }
}

wis()
verberg()
vertraging(10)
penVanCanvas()
vooruit(-200)
penOpCanvas()
boom(90)
