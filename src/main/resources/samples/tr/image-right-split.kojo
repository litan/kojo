// Kojo'nun bildiği resimlerden biri el sallayan kadın (womanWaving)
// Onu birkaç kez kullanarak bileşik bir resim oluşturalım

def imge = Resim.imge(Costume.womanWaving)
/* şunlardan herhangi birini de deneyebilirsin:
    Costume.bat1 Costume.bat2 Costume.car Costume.pencil */

def yanına(r1: Resim, r2: Resim) = büyüt(0.5, 1) -> resimYatayDizi(r1, r2)
def üstüne(r1: Resim, r2: Resim) = büyüt(1, 0.5) -> resimDikeyDizi(r2, r1)

def resim(s: Sayı): Resim = {
    if (s <= 1)
        imge
    else
        yanına(imge, üstüne(resim(s - 1), resim(s - 1)))
}

silVeSakla()
çiz(resim(7))
