// Yeni başlayanlar için alıştırmalar. Kojo'nun Öykü (Story) özelliğini kullanıyoruz.

// Düzenleme parametrelerimiz (aşağıda yüklediğimiz runner yazılımı tarafından kullanırlar)
dez YanlışlarıGöster = yanlış // yapılan yanlışları sayar ve gösterir
dez AlıştırmaKalemKalınlığı = 6 // alıştırmanın hedefini bu kalınlıkta bir kalemle çizeriz
dez Çözmedenİlerleme = yanlış // doğru yanıt bulunmazsa bir sonraki alıştırmaya geçmeye izin vermez
dez SeçenekSayısı = 4 // komut salındıraçında (dropdown) kaç seçenek olduğunu belirler

// Yüklediğimiz yazılım, öykü anlatma özelliğini kurar ve çalıştırır:
// #yükle /challenge/tr/oykucu.kojo

// Alıştırmaları ve açıklamalarını burada tanımlıyoruz. İstediğin gibi değiştirebilirsin.
// Bu iki miskin değişmez (lazy val) yukarıda yüklediğimiz runner.kojo dosyasında tanımlanmış
// yöntemler içinde kullanılıyor.
miskin dez alıştırmalar = Dizi(
    """ileri(50)
sağ(90)
ileri(100)
sol(90)
ileri(50)
""",
    """ileri(100)
sağ(90)
ileri(60)
sağ(90)
ileri(100)
sağ(90)
ileri(60)
""",
    """ileri(50)
sağ(45)
ileri(50)
sağ(45)
ileri(50)
sol(45)
ileri(50)
sol(45)
ileri(50)
""",
    """ileri(100)
sağ(45)
ileri(71)
sağ(90)
ileri(71)
sağ(45)
ileri(100)
sağ(90)
ileri(100)
""",
    """ileri(50)
sağ(45)
ileri(50)
sağ(90)
ileri(50)
sol(45)
ileri(50)
sol(45)
ileri(50)
sağ(90)
ileri(50)
sağ(45)
ileri(50)
sağ(90)
ileri(191)
""",
    """ileri(200)
sağ(90)
ileri(200)
sağ(90)
ileri(200)
sağ(90)
ileri(200)
sağ(90)
zıpla(160)
sağ(90)
zıpla(40)
ileri(35)
sağ(90)
ileri(35)
sağ(90)
ileri(35)
sağ(90)
ileri(35)
sağ(90)
zıpla(85)
ileri(35)
sağ(90)
ileri(35)
sağ(90)
ileri(35)
sağ(90)
ileri(35)
zıpla(-100)
sol(90)
ileri(50)
sağ(45)
ileri(10)
""",
    """ileri(100)
sağ(90)
ileri(100)
sağ(90)
ileri(100)
sağ(90)
ileri(100)
sağ(90)
""",
    """yinele(4) {
    ileri(100)
    sağ(90)
}
""",
    """yinele(3) {
    ileri(100)
    sağ(120)
}
""",
    """yinele(5) {
    ileri(100)
    sağ(72)
}
""",
    """yinele(6) {
    yinele(2) {
        ileri(40)
        sağ(90)
        ileri(80)
        sağ(90)
    }
    zıpla(40)
    sağ(90)
    zıpla(60)
    sol(90)
}
""",
    """sağ(360, 50)
sağ(360, 100)
sağ(360, 200)
""",
    """sağ(90, 100)
sağ(90)
sağ(90, 100)
""",
    """sağ(45, 150)
sağ(90)
sağ(45, 150)
sağ(90)
ileri(88)
"""
)

miskin dez açıklamalar = Eşlem(
    1 -> <div>
    Neler öğreniyoruz:
        <ul>
            <li>Birim uzunluk ve uzaklık kavramları</li>
            <li>Uzunluk ölçümü</li>
            <li>Dik açı</li>
            <li>Açı ölçümü</li>
            <li><tt>ileri</tt>, <tt>sağ</tt>, ve <tt>sol</tt> komutları</li>
            <li>Seçenekler arasından seçim yapma becerisi (ölçüm yapmadan)</li>
            <li>Komutların akılda sıraya sokulması</li>
            <li>Mantıksal düşünüş</li>
        </ul>
      Faydalı tudez komutları:
      <ul>
          <li><em>Eksenler</em> – Eksenleri aç/kapat. Yaklaşık ölçüm yapmak için faydalı olur.</li>
          <li><em>Araçlar</em> – Açıölçer ve cetveli aç/kapat. 
            Her iki aracı da kaydırıp çevirebilirsin. Fareyle tıklayarak sürü. Döndürmek için Üst Karakter (Shift) tuşunu basılı tutup sür.
            Tam ölçüm yapmak için faydalı olur.</li>
          <li><em>Sıfırla</em> – Kaydırma ve büyütmeyi sıfırlar. 
          Kaydırma, büyütme ve küçültmeyle değişen tuvali başlangıç haline getirir.</li>
      </ul>
      
    </div>,
    2 -> <div>
    Neler öğreniyoruz:
        <ul>
            <li>Uzunluk ölçümü</li>
            <li>Dik açı</li>
            <li>Açı ölçümü</li>
            <li>Dikdörtgen</li>
            <li>Seçenekler arasından seçim yapma becerisi (ölçüm yapmadan)</li>
            <li>Komutların akılda sıraya sokulması</li>
            <li>Mantıksal düşünüş</li>
            <li>Yeni öğrenilen kavramların pekiştirilmesi</li>
        </ul>
    </div>,
    3 -> <div>
    Neler öğreniyoruz:
        <ul>
            <li>Uzunluk ölçümü</li>
            <li>45 degree angles</li>
            <li>Açı ölçümü</li>
            <li>Seçenekler arasından seçim yapma becerisi (ölçüm yapmadan)</li>
            <li>Komutların akılda sıraya sokulması</li>
            <li>Mantıksal düşünüş</li>
            <li>Yeni öğrenilen kavramların pekiştirilmesi</li>
        </ul>
    </div>,
    4 -> <div>
    Hedef:
        <ul>
            <li>Alıştırma - Yeni öğrenilen kavramlar ve komutlarla bir ev çizelim </li>
        </ul>
    </div>,
    5 -> <div>
    Hedef:
        <ul>
            <li>Alıştırma - Yeni öğrenilen kavramlar ve komutlarla bir şato çizelim </li>
        </ul>
    </div>,
    6 -> <div>
    Neler öğreniyoruz:
        <ul>
            <li><tt>zıpla</tt> komudu</li>
            <li>Matematik kullanarak boyutları hesaplama (tahmin etmeden ve ölçmeden)</li>
            <li>Yeni öğrenilen kavramların daha büyük bir yazılımcıkla pekiştirilmesi</li>
        </ul>
    </div>,
    7 -> <div>
    Neler öğreniyoruz:
        <ul>
            <li>Örüntüleri görmeye giriş (İngilice "pattern" yani desen, kalıp ya da moteğer de diyebiliriz) </li>
            <li>Kare</li>
            <li>Yeni öğrenilen kavramların pekiştirilmesi</li>
        </ul>
    </div>,
    8 -> <div>
    Neler öğreniyoruz:
        <ul>
            <li>Daha önce gördüğümüz bir örüntüyü kullanarak daha kısa ve öz bir yazılımcıkla aynı çözüme ulaşmak</li>
            <li><tt>yinele</tt> komutu </li>
            <li>Çözümsel düşünüş</li>
            <li>Yeni öğrenilen kavramların pekiştirilmesi</li>
        </ul>
    </div>,
    9 -> <div>
    Neler öğreniyoruz:
        <ul>
            <li><tt>yinele</tt> komutlarını kullanma becerisini geliştirmek</li>
            <li>Üçgen</li>
            <li>Yeni öğrenilen kavramların pekiştirilmesi</li>
        </ul>
    </div>,
    10 -> <div>
    Neler öğreniyoruz:
        <ul>
            <li><tt>yinele</tt> komudunu kullanmak</li>
            <li>Beşgen</li>
            <li>Yeni öğrenilen kavramların pekiştirilmesi</li>
        </ul>
    </div>,
    11 -> <div>
    Neler öğreniyoruz:
        <ul>
            <li>İçiçe örüntüleri görmek</li>
            <li>İçiçe geçmiş <tt>yinele</tt> komutları</li>
            <li>Çözümsel düşünüş</li>
            <li>Yeni öğrenilen kavramların pekiştirilmesi</li>
        </ul>
    </div>,
    12 -> <div>
        <strong>Hedef: </strong> Daire, açı ve yarıçap kavramlarını daha iyi öğrenelim
    </div>,
    13 -> <div>
        <strong>Hedef: </strong> Daire, açı ve yarıçap kavramlarını daha iyi öğrenelim
    </div>,
    14 -> <div>
        <strong>Hedef: </strong> Daire, açı ve yarıçap kavramlarını daha iyi öğrenelim
    </div>
)
