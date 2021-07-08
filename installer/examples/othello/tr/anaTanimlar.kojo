// temel türleri tanımlayalım burada
trait Taş {
    val adı = "taş"
}
case object Beyaz extends Taş {
    override val adı = "beyaz"
    override def toString() = "B"
}
case object Siyah extends Taş {
    override val adı = "siyah"
    override def toString() = "S"
}
case object Yok extends Taş {
    override val adı = "boş"
    override def toString() = "."
}
case class Oda(str: Sayı, stn: Sayı) {
    val y = str
    val x = stn
    override def toString() = s"${stn + 1}x${str + 1}" // satır ve sütün sırası yazılımda ters!
}
trait Yön
case object K extends Yön; case object KD extends Yön
case object D extends Yön; case object GD extends Yön
case object G extends Yön; case object GB extends Yön
case object B extends Yön; case object KB extends Yön
case class Komşu(yön: Yön, oda: Oda)
trait Ustalık
case object ErdenAz extends Ustalık
case object Er extends Ustalık
case object Çırak extends Ustalık
case object Kalfa extends Ustalık
case object Usta extends Ustalık
case object Doktor extends Ustalık
case object Aheste extends Ustalık
case object Deha extends Ustalık
case object DehadanÇok extends Ustalık
case object ÇokSabır extends Ustalık

class HamleSayısı {
    // bir sonraki hamle kaçıncı hamle olacak? 1, 2, 3, ...
    def apply() = say
    def başaAl() = say = 1
    def artır() = say += 1
    def azalt() = say -= 1
    private var say: Sayı = _
    başaAl()
}
class Oyuncu(val kimBaşlar: Taş) {
    def apply() = oyuncu
    def karşı: Taş = if (oyuncu == Beyaz) Siyah else Beyaz
    def başaAl() = oyuncu = kimBaşlar
    def değiştir() = oyuncu = karşı
    def kur(o: Taş) = oyuncu = o
    private var oyuncu: Taş = _
    başaAl()
}
