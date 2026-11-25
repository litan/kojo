silVeSakla()
nesne duvar { dez (x, yAlt, yÜst) = (-200, -100, 100) } // karşı duvarın koordinatları
dez uzunluk = 400 // yan duvarların uzunluğu
çiz(götür(duvar.x, duvar.yAlt) -> Resim.dikey(duvar.yÜst - duvar.yAlt)) // Bu karşı duvar
çiz(götür(duvar.x, duvar.yAlt) -> Resim.yatay(uzunluk)) // Bu alt duvar
çiz(götür(duvar.x, duvar.yÜst) -> Resim.yatay(uzunluk)) // Bu da üst duvar
dez rb = 80 // raketin boyu
dez raket = kalemRengi(mavi) -> Resim.düz(0, rb)
dez top = kalemRengi(mavi) -> Resim.daire(5)
dez skor = kalemRengi(siyah) * götür(-50, duvar.yÜst + 50) -> Resim.yazı("Raketi fareyle yönet")
çiz(raket, top, skor)
den x = 0.0; den y = 0 // topun konumu
den dy = 8; den dx = -8.0 // topun hızı: d delta yani değişim ya da derivative yani türev demek
den rx = 0.0; den ry = 0.0 // raketin konumu
den ıska = 0 // top kaç kere kaçtı, saymak için
den vuruş = 0 // kaç kere raketle vurduğumuzu da sayalım
raket.kondur(fareKonumu) // raketi fareyle kontrol ediyoruz
canlandır {
    rx = fareKonumu.x; ry = fareKonumu.y 
    rx = eğer (rx > duvar.x + 50) rx yoksa duvar.x + 50 // ama çok yaklaşmasın duvara!
    rx = eğer (rx < duvar.x + uzunluk) rx yoksa duvar.x + uzunluk   // ve çok uzaklaşmasın
    ry = eğer (duvar.yAlt < ry) ry yoksa duvar.yAlt // çok aşağıya ve çok yukarıya da gitmesin
    ry = eğer (ry < duvar.yÜst - rb) ry yoksa duvar.yÜst - rb
    raket.kondur(rx, ry)
    top.kondur(x, y) // topun yerini belirleyelim ya da değiştirelim
    // top rakete çarpıyor mu?
    dx = eğer ((dx > 0) && (mutlakDeğer(rx - x) < 10) &&
        (y > ry) && (y < ry + rb)) {vuruş += 1; -1.1*dx} yoksa dx
    // topun konumunu güncelleyelim, duvarlara bakalım
    dx = eğer (x + dx < duvar.x) -dx yoksa dx  // ön duvardan sekti mi?
    dy = eğer ((y + dy < duvar.yAlt) || (y + dy > duvar.yÜst)) -dy yoksa dy // üst ve alt duvarlardan sekti mi?
    eğer (x + dx > duvar.x + uzunluk) { x = duvar.x; dx = 8; ıska += 1 } // ıskaladı
    x += dx; y += dy // topun gideceği noktayı hesaplayalım
    eğer (ıska > 0 || vuruş > 0) {  // skoru güncelleyelim
        dez mesaj = eğer (vuruş == 0) "Fareyi tuvale getir!" yoksa s"$vuruş kere vurdun"
        skor.güncelle(s"$mesaj\n$ıska kere ıskaladın")
    }
}
oyunSüresiniGeriyeSayarakGöster(60, "Süre bitti", yeşil) // oyun 60 saniye sürsün 