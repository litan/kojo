silVeSakla()
çiz(götür(-200, -100) -> Resim.dikey(200)) // Üç duvar çizelim. Bu dik ön duvar
çiz(götür(-200, -100) -> Resim.yatay(400)) // Bu alt duvar
çiz(götür(-200,  100) -> Resim.yatay(400)) // Bu da üst duvar
dez rb = 80 // raketin boyu
dez raket = kalemRengi(mavi) -> Resim.düz(0, rb)
dez top = kalemRengi(mavi) -> Resim.daire(5)
dez skor = kalemRengi(siyah) * götür(-50, 150) -> Resim.yazı("Raketi fareyle yönet")
çiz(raket, top, skor)
den x = 0.0; den y = 0 // topun konumu
den dy = 8; den dx = -8.0 // topun hızı: d delta yani değişim ya da derivative yani türev demek
den rx = 0.0; den ry = 0.0 // raketin konumu
den ıska = 0 // top kaç kere kaçtı, saymak için
den vuruş = 0 // kaç kere raketle vurduğumuzu da sayalım
canlandır {
    raket.kondur(fareKonumu)  // raketi fareyle kontrol ediyoruz
    top.kondur(x, y) // topun yerini değiştirelim
    // top rakete çarpıyor mu?
    rx = fareKonumu.x; ry = fareKonumu.y
    dx = eğer ((dx > 0) && (mutlakDeğer(rx - x) < 10) &&
        (y > ry) && (y < ry + rb)) {vuruş += 1; -1.1*dx} yoksa dx
    // topun konumunu güncelleyelim, duvarlara bakalım
    dx = eğer (x + dx < -200) -dx yoksa dx  // ön duvardan sekti mi?
    dy = eğer ((y + dy < -100) || (y + dy > 100)) -dy yoksa dy // üst ve alt duvarlardan sekti mi?
    eğer (x + dx > 200) { x = -200; dx = 8; ıska += 1 } // ıskaladı
    x += dx; y += dy // topun gideceği noktayı hesaplayalım
    eğer (ıska > 0 || vuruş > 0) {  // skoru güncelleyelim
        dez mesaj = eğer (vuruş == 0) "Fareyi tuvale getir!" yoksa s"$vuruş kere vurdun"
        skor.güncelle(s"$mesaj\n$ıska kere ıskaladın")
    }
}
oyunSüresiniGeriyeSayarakGöster(60, "Süre bitti", yeşil) // oyun 60 saniye sürsün 
