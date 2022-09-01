// Giysiler, artalan resimleri ve seslendirmeler nasıl kullanılıyormuş görelim

// Bir kaplumbağaya giysi giydirince artık kaplumbağa olmaktan çıkar. 
// O zaman ona cin/peri ya da hayalet deriz (ingilizcesi sprite)

// Daha pek çok örnek görmek ve yazılımcıklarında kullanmak istersen
// scratch-media.zip adlı dosyayı şurada bulabilirsin:
// https://code.google.com/archive/p/kojo/downloads?page=2

sil()
artalanıKur(Renk(200, 200, 200))
// artalana bir resim koymak istersen onu bizim kaplumbağacığa giydir
kaplumbağa0.giysiKur(Background.trainTrack)  // todo

val k1 = yeniKaplumbağa(-50, -180, Costume.womanWaving)
k1.davran { kap =>
    yineleDoğruysa(kap.konum.y < 40) {
        kap.konumuDeğiştir(0.6, 0.9)
        kap.giysiyiBüyült(0.992)
        durakla(0.03)
    }
}

val k2 = yeniKaplumbağa(-250, 180)
k2.giysileriKur(Costume.bat1, Costume.bat2)
k2.giysiyiBüyült(0.5)
k2.davran { kap =>
    yineleDoğruysa(kap.konum.x < 200) {
        kap.konumuDeğiştir(10, 0)
        kap.birsonrakiGiysi()
        durakla(0.15)
    }
    durdur() // varsa çalan müziği ve bütün canlandırmaları durduralım
}
müzikMp3üÇalDöngülü(Sound.medieval1)
