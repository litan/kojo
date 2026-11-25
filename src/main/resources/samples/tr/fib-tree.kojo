// Bu örnek şu kaynaktan:
//     http://lalitpant.blogspot.in/2012/05/recursive-drawing-with-kojo.html
// Bu sefer Kojo'nun zengin Resim dilini (Pictures API) kullanalım

dez büyüklük = 100
tanım resim = Resim {
    yinele(4) {
        ileri(büyüklük)
        sağ()
    }
}

tanım gövde = büyüt(0.13, 1) * kalemRengi(renksiz) * boyaRengi(siyah) -> resim

sil()
artalanıKur(Renk(255, 170, 29))
gizle()

tanım çizim(n: Sayı): Resim = {
    eğer (n <= 1)
        gövde
    else
        Resim.dizi(
            gövde,
            götür(2, büyüklük - 5) * aydınlık(0.05) -> Resim.dizi(
                döndür(25) * büyüt(0.72) -> çizim(n - 1),
                döndür(25) * götür(0, büyüklük * 0.72) * döndür(-75) * büyüt(0.55) -> çizim(n - 1))
        )
}

// 10 yerine daha küçük sayıları deneyerek nasıl çalıştığını görebilirsin 
çiz(götür(0, -100) -> çizim(10))
