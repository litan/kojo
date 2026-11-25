// Arayüz senin bilgisayardan bilgiler alman ve ona komutlar vermeni sağlayan
// şeye verilen soyut ad.
// Arayüz hem çok önemli hem de çok sorunlu. Kojo, Java'nın ve Scala'nın
// becerilerini de kullanarak arayüz tasarlamayı ve yazmayı epey kolaylaştırıyor.
// Bir örnekle görelim..

dez açıklama = // html hyper-text-markup-language demek. İnternet sitelerinin dilidir!
    <html>
      Bu örneği çalıştırmak için, aşağıdaki değerleri istediğiniz<br/>   
      gibi değiştirdikten sonra <strong><em>Üret ve canlandır</em></strong>
      tuşuna basın.<br/>
      Düğmeye her basışta yeni bir şekil üretir ve ona istediğin hızı verir.
    </html>.yazıya
// br: break -- yeni satıra geç anlamında
// strong: daha kalın yazı
// em: emphasis -- hafifçe eğerek etki verir
// Daha da pek çok becerisi den HTML dilinin. Şuna bir bak istersen:
//     https://html.sitesi.web.tr/

dez ay_uzunluk = ay.Yazıgirdisi(60)
dez ay_kaçKenar = ay.Yazıgirdisi(5)
dez ay_açı = ay.Yazıgirdisi(360 / 5)
dez ay_renkler = ay.Salındıraç("mavi", "yeşil", "sarı")
dez ay_hız = ay.Yazıgirdisi(50)
dez renkler = Eşlem("mavi" -> mavi, "yeşil" -> yeşil, "sarı" -> sarı)

silVeSakla()
// çok becerikli bir resim çizeceğiz şimdi!
çiz(götür(tuvalAlanı.x, tuvalAlanı.y) ->
    Resim.arayüz(
        ay.Sütun(
            ay.Sıra(ay.Tanıt(açıklama)),
            ay.Sıra(ay.Tanıt("Kenar uzunluğu: "), ay_uzunluk),
            ay.Sıra(ay.Tanıt("Kenar sayısı: "), ay_kaçKenar),
            ay.Sıra(ay.Tanıt("Açı: "), ay_açı),
            ay.Sıra(ay.Tanıt("Boyama rengi: "), ay_renkler),
            ay.Sıra(ay.Tanıt("Hız: "), ay_hız, ay.Düğme("Üret ve canlandır") {
                dez hız = ay_hız.değeri // saniyede bu kadar nokta ilerlesin
                dez solKenarX = tuvalAlanı.x
                dez resim = götür(solKenarX, 0) * boyaRengi(renkler(ay_renkler.değeri)) ->
                    Resim {
                        yinele(ay_kaçKenar.değeri) { // kullanıcının girdiği değeri böyle kullanıyoruz
                            ileri(ay_uzunluk.değeri)
                            sağ(ay_açı.değeri)
                        }
                    }
                çiz(resim)
                dez başlangıç = buSaniye
                resim.canlan(_.konumuKur(solKenarX + hız * (buSaniye - başlangıç), 0))
            })
        )
    )
)
