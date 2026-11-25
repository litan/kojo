// Koch üçgeni diye de bilinir bu muhteşem fraktal, yani kesirli boyutsal
// Basit bir üçgen çizer gibi yapalım önce:
tanım kochTanesi(kenarUzunluğu: Sayı, kaçKere: Sayı) {
    // ama bu kaç kere girdisi de nedir acaba? Onu birazdan anlayacağız...
    // Eşkenar üçgeni çizmeye sol kenarın altından başlayalım:
    // Kaplumbağa 90 dereceye bakıyor ya en başta. Onun için:
    sağ(30)
    /* hani yaklaşık olarak şöyle olsun yani:
     *
     *       /\
     *      /  \
     *     -----
     *
     */
    yinele(3) {
        birKenarÇiz(kenarUzunluğu, kaçKere)
        // birKenarÇiz(...) yerine ileri(kenarUzunluğu) desek üçgen olurdu
        // ikinci kenarla arasındaki açı 60 derece olmalı. O zaman:
        sağ(120)
    }
}

// üçgenin bir kenarını çizelim. Ama son adıma gelmemişsek
// dörde bölerek ortasına yeni küçük bir üçgen çizelim
// Not. Bu bir özyineleme örneği. Kendi kendini çağıran bir
// işlev. Özedönen işlev mi desek?
tanım birKenarÇiz(kenarUzunluğu: Kesir, kaçKere: Sayı) {
    // durmayı bilmezsek sonsuza kadar gider ve ilk kenarın
    // en başında kalırız!
    eğer (kaçKere <= 1) {
        ileri(kenarUzunluğu)
    }
    yoksa {
        dez birEksik = kaçKere - 1
        dez küçükKenar = kenarUzunluğu / 3
        birKenarÇiz(küçükKenar, birEksik)
        sol(60)
        birKenarÇiz(küçükKenar, birEksik)
        sağ(120)
        birKenarÇiz(küçükKenar, birEksik)
        sol(60)
        birKenarÇiz(küçükKenar, birEksik)
    }
}

tanım ayarla(sola: Kesir, aşağı: Kesir, duraklamaSüresi: Sayı = 50) = {
    sil
    kalemKalınlığınıKur(1)
    kalemRenginiKur(koyuGri)
    boyamaRenginiKur(gri)
    canlandırmaHızınıKur(duraklamaSüresi)
    atla(-sola, -aşağı)
}

// kaplumbacık tuval merkezinin biraz solunda (150 adım) ve biraz aşağısından (50 adım)
// başlasın. O sayede kartanesi ekranın tam ortasını bulacak.
// Bir de çizim hızını biraz azaltalım (iki kat)
// yavaş olur.
ayarla(150, 50, 100)
// İlk üçgenin kenar uzunluğu 300 olsun.
// 5 kere tekrarlayalım üçgen doğurmayı.
kochTanesi(300, 5) // sadece 1 kere olsaydı bildiğimiz basit üçgen olacaktı!
                   // 2 ve 3 kereyi de dene!
