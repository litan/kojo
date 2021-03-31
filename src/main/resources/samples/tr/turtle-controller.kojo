// Küçük çocukların kaplumbağayı yönetmeyi daha kolay öğrenmeleri için 
// en temel birkaç komudu çağıran onbeş tane düğmecik

// Düğmelerin işlevini birazcık değiştirmek için bu değerleri değiştirebilirsin
val ileriAdım = 50
val ileriAdım2 = 10
val ileriAdım3 = 5
val dönüşAçısı = 90
val dönüşAçısı2 = 10
val dönüşAçısı3 = 5
val aaRengi = beyaz
val aaRengiYaz = "beyaz"

sil()
çıktıyıSil()
ışınlarıAç()
val en = tuvalAlanı.en
val boy = tuvalAlanı.boy

artalanıKur(aaRengi)
boyamaRenginiKur(mor)

def eylem(komutlar: Yazı) {
    yorumla(komutlar); satıryaz(komutlar)
}

val komutlar = Map(
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

def silKomudu(n: Int) =
    s"biçimleriBelleğeYaz(); kalemRenginiKur($aaRengiYaz); kalemKalınlığınıKur(4); geri($n); biçimleriGeriYükle()"

def düğme(komutAdı: String) = Resim.düğme(komutlar(komutAdı)) { eylem(komutlar(komutAdı)) }

val düğmePanosu = götür(-en / 2, -boy / 2) * büyüt(1.4) -> Resim.diziDikey(
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

