// Kojo ile yüklenen bazı imgelerle bir örnek resim çizelim 
def kadın = Resim.imge(Costume.womanWaving)
def araba = Resim.imge(Costume.car)
def kalem = Resim.imge(Costume.pencil)
def yarasa1 = Resim.imge(Costume.bat1)
def yarasa2 = Resim.imge(Costume.bat2)
def başlık = kalemRengi(mor) -> Resim {
    yazıYüzünüKur(Font("Serif", 28))
    kaplumbağa.yazı("Çılgın yarasalar!")
}

val resim = Resim.dizi(
    götür(-150, -150) -> kadın, 
    götür(-50, 91) * döndür(-10) * saydamlık(-0.3) -> araba,
    götür(94 , 44) * saydamlık(-0.2) -> kalem,
    götür(-50, 50) * büyüt(0.5) -> yarasa1,
    götür(150, 50) * büyüt(0.5) -> yarasa2,
    götür(-72, 50) -> başlık
)

silVeSakla()
çizMerkezde(resim)
