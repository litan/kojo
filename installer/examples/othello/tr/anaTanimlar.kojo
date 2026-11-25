// temel türleri tanımlayalım burada
özellik Taş {
    dez adı = "taş"
}
durum nesne Beyaz yayar Taş {
    baskın dez adı = "beyaz"
    baskın tanım toString() = "B"
}
durum nesne Siyah yayar Taş {
    baskın dez adı = "siyah"
    baskın tanım toString() = "S"
}
durum nesne Yok yayar Taş {
    baskın dez adı = "boş"
    baskın tanım toString() = "."
}
durum sınıf Oda(str: Sayı, stn: Sayı) {
    dez y = str
    dez x = stn
    baskın tanım toString() = s"${stn + 1}x${str + 1}" // satır ve sütün sırası yazılımda ters!
}
özellik Yön
durum nesne K yayar Yön; durum nesne KD yayar Yön
durum nesne D yayar Yön; durum nesne GD yayar Yön
durum nesne G yayar Yön; durum nesne GB yayar Yön
durum nesne B yayar Yön; durum nesne KB yayar Yön
durum sınıf Komşu(yön: Yön, oda: Oda)
özellik Ustalık
durum nesne ErdenAz yayar Ustalık
durum nesne Er yayar Ustalık
durum nesne Çırak yayar Ustalık
durum nesne Kalfa yayar Ustalık
durum nesne Usta yayar Ustalık
durum nesne Doktor yayar Ustalık
durum nesne Aheste yayar Ustalık
durum nesne Deha yayar Ustalık
durum nesne DehadanÇok yayar Ustalık
durum nesne ÇokSabır yayar Ustalık

sınıf HamleSayısı {
    // bir sonraki hamle kaçıncı hamle olacak? 1, 2, 3, ...
    tanım apply() = say
    tanım başaAl() = say = 1
    tanım artır() = say += 1
    tanım azalt() = say -= 1
    gizli den say: Sayı = _
    başaAl()
}
sınıf Oyuncu(dez kimBaşlar: Taş) {
    tanım apply() = oyuncu
    tanım karşı: Taş = eğer (oyuncu == Beyaz) Siyah yoksa Beyaz
    tanım başaAl() = oyuncu = kimBaşlar
    tanım değiştir() = oyuncu = karşı
    tanım kur(o: Taş) = oyuncu = o
    gizli den oyuncu: Taş = _
    başaAl()
}
