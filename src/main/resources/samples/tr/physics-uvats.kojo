// Mekanik simülasyon == kinematik bir canlandırma, hepsi fizik

tümEkran()
silVeSakla()
eksenleriGöster()
gridiGöster()
yaklaş(0.75, 500, 300)

val a = 20.0 // başlangıç ivmesi
val b = 10.0 // başlangıç hızı

val araba = yeniKaplumbağa(0, 0, Costume.car)

val konumEğrisi = yeniKaplumbağa(0, 0, Costume.pencil)
konumEğrisi.kalemRenginiKur(mavi)

val hızEğrisi = yeniKaplumbağa(0, b, Costume.pencil)
hızEğrisi.kalemRenginiKur(yeşil)

val t0 = buSaniye // göreceli olarak bu anın zamanı
val geçenZaman = yeniKaplumbağa(100, -50)

def zamanıGöster(t: String) {
    geçenZaman.sil()
    geçenZaman.gizle()
    geçenZaman.kalemRenginiKur(mavi)
    geçenZaman.yazı(t)
}

canlandır {
    val t = buSaniye - t0 // program başlayalı beri kaç saniye geçmiş
    // kinematik denklemlerle arabanın hızını ve ivmesini hesaplayalım
    val h = b + a * t 
    val x = b * t + 0.5 * a * t * t 
    araba.konumuKur(x, 10)
    konumEğrisi.ilerle(t * 50, x)
    hızEğrisi.ilerle(t * 50, h)
    zamanıGöster(f"Zaman: $t%.1f saniye")
}

tuşaBasınca { t =>
    t match {
      case tuşlar.VK_ESCAPE => durdur()
      case _ =>
    }
}
