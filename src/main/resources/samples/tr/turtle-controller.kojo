// Küçük çocukların kaplumbağayı yönetmeyi daha kolay öğrenmeleri için 
// en temel birkaç komudu çağıran onbeş tane düğmecik

// Düğmelerin işlevini birazcık değiştirmek için bu değerleri değiştirebilirsin
dez ileriAdım = 50
dez ileriAdım2 = 10
dez ileriAdım3 = 5
dez dönüşAçısı = 90
dez dönüşAçısı2 = 10
dez dönüşAçısı3 = 5
dez aaRengi = beyaz
dez aaRengiYaz = "beyaz"

sil()
çıktıyıSil()
ışınlarıAç()
dez en = tuvalAlanı.en
dez boy = tuvalAlanı.boy

artalanıKur(aaRengi)
boyamaRenginiKur(mor)

tanım eylem(komutlar: Yazı) {
    yorumla(komutlar); satıryaz(komutlar)
}

dez komutlar = Eşlek(
    "ileri1" -> s"ileri($ileriAdım)",
    "ileri2" -> s"ileri($ileriAdım2)",
    "ileri3" -> s"ileri( $ileriAdım3 )",
    "zıpla1" -> s"zıpla($ileriAdım)",
    "zıpla2" -> s"zıpla($ileriAdım2)",
    "zıpla3" -> s"zıpla( $ileriAdım3 )",
    "sağ1" -> s"sağ($dönüşAçısı)",
    "sağ2" -> s"sağ($dönüşAçısı2)",
    "sağ3" -> s"sağ( $dönüşAçısı3 )",
    "sol1" -> s"sol($dönüşAçısı)",
    "sol2" -> s"sol($dönüşAçısı2)",
    "sol3" -> s"sol( $dönüşAçısı3 )"
)

tanım silKomudu(n: Int) =
    s"biçimleriBelleğeYaz(); kalemRenginiKur($aaRengiYaz); kalemKalınlığınıKur(4); geri($n); biçimleriGeriYükle()"

tanım düğme(komutAdı: Yazı) = Resim.düğme(komutlar(komutAdı)) { eylem(komutlar(komutAdı)) }

dez düğmePanosu = götür(-en / 2, -boy / 2) * büyüt(1.4) -> Resim.diziDikey(
    Resim.diziYatay(
        düğme("sol3"),
        düğme("ileri3"),
        düğme("sağ3"),
        düğme("zıpla3"),
        Resim.düğme(s" sil($ileriAdım3) ") { eylem(silKomudu(ileriAdım3)) }
    ),
    Resim.diziYatay(
        düğme("sol2"),
        düğme("ileri2"),
        düğme("sağ2"),
        düğme("zıpla2"),
        Resim.düğme(s"sil($ileriAdım2)") { eylem(silKomudu(ileriAdım2)) }
    ),
    Resim.diziYatay(
        düğme("sol1"),
        düğme("ileri1"),
        düğme("sağ1"),
        düğme("zıpla1"),
        Resim.düğme(s"sil($ileriAdım)") { eylem(silKomudu(ileriAdım)) }
    )
)

çiz(düğmePanosu)
satıryaz("// Aşağıda oluşturduğun komut dizisini yazılım düzenleyicisine taşıyıp")
satıryaz("// yeşil üçgenle çalıştırarak resmini tekrar çizdirebilirsin")
satıryaz("sil()")
satıryaz("boyamaRenginiKur(mor)")

