silVeSakla()
dez sarı1 = Renk(180, 111, 3)
dez sarı2 = Renk(245, 236, 74)
artalanıKurYatay(sarı1, sarı2)
yineleKere(1 |-| 600) { e =>
    atla(rastgele(600) - 300, rastgele(400) - 200)
    kalemRenginiKur(Renk(
        rastgele(180), rastgele(180), rastgele(180),
        rastgele(100) + 100))
    nokta(35)
}
