// Kojo'nun bildiği resimlerden biri el sallayan kadın (womanWaving)
// Onu birkaç kez kullanarak bileşik bir resim oluşturalım

// Picture resim demek. Image da imaj. Benzer şeyler yani!
// def resim = Picture.image("dosya-adı")
def resim = Picture.image(Costume.womanWaving)
/* şunlardan herhangi birini de deneyebilirsin:
    Costume.bat1 Costume.bat2 Costume.car Costume.pencil */

// scale: ölçek demek. Resmin büyüklüğünü değiştiriyor bu dönüşüm komutu
// H horizontal yatay, V de vertical dikey demek
def yanına(r1: Picture, r2: Picture) = scale(0.5, 1) -> HPics(r1, r2)
def üstüne(r1: Picture, r2: Picture) = scale(1, 0.5) -> VPics(r2, r1)

def desen(n: Int): Picture = {
    if (n <= 1)
        resim
    else
        yanına(resim, üstüne(desen(n - 1), desen(n - 1)))
}

silVeSakla()
draw(desen(6))  // draw İngilizce bir sözcük. Çiz demek. desen işleviyle resmi çizen komut bu.
