silVeSakla()
başlangıçNoktasıAltSolKöşeOlsun()
// artalanıKur(siyah)

notaÇalgısınıKur(Çalgı.AkustikBas)
notaÇal(50, 150) // nota, süre
notaÇal(45, 200)

dez yerçekimi = Yöney2B(0, -0.1)
dez tuvalEni = tuvalAlanı.eni.sayıya
dez tuvalBoyu = tuvalAlanı.boyu.sayıya

sınıf Parçacık(x0: Kesir, y0: Kesir, arıRenk: Kesir, tohum: İkil) {
    den yer = Yöney2B(x0, y0)
    gizli den hız = {
        eğer (tohum) Yöney2B(0, rastgele(5, 12))
        yoksa rastgelePatlamaYöneyi
    }
    gizli den ivme = Yöney2B(0, 0)
    gizli den yaşamSüresi = 255.0
    gizli den patladıMı = yanlış

    tanım rastgelePatlamaYöneyi: Yöney2B =
        Yöney2B(rastgeleNormalKesir, rastgeleNormalKesir) * rastgeleKesir(2, 4)

    tanım kuvvetiUygula(kuvvet: Yöney2B) = { ivme += kuvvet }

    tanım patlasınMı: İkil = !patladıMı && yaşamSüresi <= 0

    tanım ölüMü: İkil = eğer (tohum) patladıMı yoksa yaşamSüresi <= 0

    tanım patla() {
        notaÇal(15, 30, 127) // nota, süre, ses
        patladıMı = doğru
    }

    tanım adım() {
        hız += ivme
        yer += hız
        eğer (!tohum) {
            yaşamSüresi -= 5
            hız *= 0.95
        }
        ivme *= 0
        eğer (tohum && hız.y < 0) {
            yaşamSüresi = 0
        }
    }

    // gizli dez nokta = Resim.daire(1)

    tanım göster(tuval: TuvalÇizim) {
        tuval.fırça(Renk.adas(arıRenk, 1, 0.5, yaşamSüresi / 255))
        tuval.fırçaAğırlığı(eğer(tohum) 4 yoksa 2)
        tuval.nokta(yer.x, yer.y)
    }
}

sınıf HavaiFişek() {
    gizli dez arıRenk = rastgele(360)
    gizli dez havaiFişek = yeni Parçacık (rastgele(tuvalEni), 0, arıRenk, doğru)
    gizli dez parçacıklar = EsnekDizim.boş[Parçacık]

    tanım bittiMi: İkil = {
        eğer (havaiFişek.ölüMü & parçacıklar.boşMu) doğru yoksa yanlış
    }

    tanım adım() {
        parçacıklar.eleYerinde(p => !p.ölüMü)

        eğer (!havaiFişek.ölüMü) {
            havaiFişek.kuvvetiUygula(yerçekimi)
            havaiFişek.adım()

            eğer (havaiFişek.patlasınMı) {
                yinele(100) {
                    parçacıklar.ekle(
                        yeni Parçacık (havaiFişek.yer.x, havaiFişek.yer.y, arıRenk, yanlış)
                    )
                }
                havaiFişek.patla()
            }
        }
        parçacıklar.herbiriİçin { p =>
            p.kuvvetiUygula(yerçekimi)
            p.adım()
        }
    }

    tanım göster(tuval: TuvalÇizim) {
        havaiFişek.göster(tuval)
        parçacıklar.herbiriİçin { p =>
            p.göster(tuval)
        }
    }
}

dez havaifişek = EsnekDizim.boş[HavaiFişek]

tanım evreyiGüncelle() {
    havaifişek.eleYerinde(f => !f.bittiMi)
    eğer (rastgeleKesir(1) < 0.08) {
        havaifişek.ekle(yeni HavaiFişek ())
    }
    havaifişek.herbiriİçin { _.adım() }
}

tanım evreyiGöster(tuval: TuvalÇizim) {
    tuval.boya(0, 0, 0, 30)
    tuval.fırçasız()
    tuval.dikdörtgen(0, 0, tuvalEni, tuvalBoyu)
    havaifişek.herbiriİçin { _.göster(tuval) }
}

canlandırTuvalÇizimle { tuval =>
    evreyiGüncelle()
    evreyiGöster(tuval)
}

// Esin kaynağı:
//   https://github.com/CodingTrain/Coding-Challenges/tree/main/027_FireWorks/Processing/CC_027_FireWorks_2D

// Ayrıntılı bilgiler:
//   https://github.com/litan/kojo-examples/tree/main/fireworks
