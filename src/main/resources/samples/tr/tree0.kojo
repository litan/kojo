silVeSakla()
hızıKur(hızlı)
artalanıKur(beyaz)

tanım dal(boy: Kesir) {
    ileri(boy)
}

tanım ağaç(boy: Kesir) {
    konumVeYönüBelleğeYaz()
    // dalların rengini uzunluğuyla orantılı olarak değiştirelim.
    // Renk biriminin kym yani kırmızı/yeşil ve mavi adlı metodu kullanalım.
    // 'sayıya' adlı metod, kesirli sayıyı tam sayıya çeviriyor.
    // Yani boy 1.75 olursa boy.sayıya 1 oluyor
    // n % 255 işlemi ile de 255'ten küçük olmasını sağlıyoruz
    tanım renk = Renk.kym(boy.sayıya % 255, mutlakDeğer(255 - boy * 3).sayıya % 255, 125)
    kalemRenginiKur(renk)
    eğer (boy <= 4) {
        kalemKalınlığınıKur(0.5)
        dal(boy / 2)
    }
    yoksa {
        kalemKalınlığınıKur(boy / 7)
        dal(boy)
        sağ(25)
        ağaç(0.8 * boy- 2)
        sol(45)
        ağaç(boy - 10)
    }
    konumVeYönüGeriYükle()
}

konumuKur(100, -200)
ağaç(90) // 100, 120 ve 150 gibi boyları da dene!
