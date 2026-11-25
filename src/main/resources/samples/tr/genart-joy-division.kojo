// Esin kaynağı https://generativeartistry.com/tutorials/joy-division/

// Her çalıştırmada farklı bir manzarayla karşılaşırız
kojoVarsayılanİkinciBakışaçısınıKur()
silVeSakla()
artalanıKur(Renk(60, 63, 65))
dez ta = tuvalAlanı
dez n = 22
dez yatayAdım = ta.eni / (n + 1) / 2
dez dikeyAdım = ta.boyu / (n + 1)

durum sınıf Nokta(x: Kesir, y: Kesir)
// Bir sıra dağ nasıl tanımlanır. Her bir x koordinatı için y koordinatını 
// yani dağın o noktadaki değerini belirleyerek.
// EğikÇizgi sınıfıyla bu iş tamam. Her bir EğikÇizgi nesnesi bir sıradağı tanımlayacak.
durum sınıf EğikÇizgi(noktalar: Yöney[Nokta]) // Yöney sıralı bir Nokta kümesi tanımlıyor
// EğikÇizgi yerine SıraDağ adını verseydik daha iyi olur muydu?

// Sıra sıra sıra dağlar çizmek istiyoruz. Önce herbir sıradağını hesaplayıp kümeye koyalım
den çizgiler = Yöney.boş[EğikÇizgi] // bu da boş bir EğikÇizgi kümesi, yani sıra sıra sıra dağlar 
yineleİçin(Aralık.kesirden(ta.y, ta.y + ta.boyu - 5 * dikeyAdım, dikeyAdım)) { y =>
    den noktalar = Yöney.boş[Nokta] // boş Nokta kümesiyle başlayalım
    yineleİçin(Aralık.kesirdenKapalı(ta.x, ta.x + ta.eni, yatayAdım)) { x =>
        dez f = mutlakDeğer(x / ta.eni * 2) // 0 ile 1 arasına taşıdık
        dez f1 = (1 - f) * (1 - f) * 150  // x küçüldükçe f1 büyüyör
        noktalar = noktalar :+ Nokta(x, y + rastgeleKesir(f1.sayıya))
    }
    çizgiler = çizgiler :+ EğikÇizgi(noktalar)
}


tanım çizgidenResime(eç: EğikÇizgi) =
    kalemBoyu(2) * kalemRengi(beyaz) * boyaRengi(Renk(57, 57, 57)) ->
        Resim.yoldan { yol =>
            dez nler = eç.noktalar
            dez ilkNokta = nler(0)
            yol.kondur(ilkNokta.x, ilkNokta.y) // yol bu Noktayla başlıyor
            yineleİçin(1 |-| nler.boyu - 2) { i =>
                // bu noktayla bir sonraki noktanın arasındaki noktayı bulalım      
                dez araX = (nler(i).x + nler(i + 1).x) / 2
                dez araY = (nler(i).y + nler(i + 1).y) / 2
                // doğru yerine yumuşak eğri ile birleştirelim
                yol.eğriÇiz(nler(i).x, nler(i).y, araX, araY)
            }
            // son noktaya düz bir çizgiyle gidelim
            yol.doğruÇiz(nler.sonu.x, nler.sonu.y)
        }
// çizgidenResme işlevini kullanarak çizgilerden resimler türetip çizelim
çiz(çizgiler.tersi.işle(çizgidenResime))
// Neden tersi (sıra dağların sırasını tersine çevirme) metodunu kullandık?
// Onu silip tekrar çalıştırmayı dene.
// Bilirsin elbet, öndeki sıra dağlar arkadaki sıra dağların daha yüksek zirvelerini  
// görmemizi engelleyemese de, daha alçak zirveleri görmemizi engeller.
// Onun için en yukarıdan başlıyoruz çizmeye, çünkü sonra çizdiklerimiz 
// önce çizdiklerimizin üstüne çiziliyor ve onları görmemizi kısmen engelliyor.
