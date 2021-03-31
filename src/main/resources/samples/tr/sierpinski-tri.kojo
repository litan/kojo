// boyu verilen bir eşkenar üçgen çizelim
def üçgen(boy: Kesir) {
    sağ(30)
    yinele(3) {
        ileri(boy)
        sağ(120)
    }
    sol(30)
}
// boyu verilen üçgenin içine üç tane minik üçgen çizelim
def içÜçgenler(boy: Kesir, kat: Sayı) {
    def birincidenİkinciyeGit() { // birinci miniği çizdikten sonra
        sağ(30) // ikinciyi çizmek için gitmemiz gereken yere gidelim
        ileri(boy / 2)
        sol(30)
    }
    def ikincidenÜçüncüyeGit() {
        sağ(120 + 30)
        ileri(boy / 2)
        sol(120 + 30)
    }
    def üçüncüdenBaşaDön() {
        sol(90)
        ileri(boy / 2)
        sağ(90)
    }

    val k = if (kat > 10) 10 else kat
    kalemKalınlığınıKur(k)
    kalemRenginiKur(Renk(k.toInt * 20, k.toInt * 25, 100))

    val minik = boy / 2
    üçgen(minik) // sol alt minik üçgen (#1)
    birincidenİkinciyeGit()
    üçgen(minik) // üst minik üçgen (#2)
    ikincidenÜçüncüyeGit()
    üçgen(minik) // alt sağ minik üçgen (#3)
    üçüncüdenBaşaDön()

    if (kat > 1) {
        val altkat = kat - 1
        // bir alt kata inelim ve minik üçgenlerin içini çizelim
        // özyineleme yapıyoruz, yani bu metodu tekrar (burada üç kere) çağırıyoruz
        içÜçgenler(minik, altkat) // alt sol
        birincidenİkinciyeGit()
        içÜçgenler(minik, altkat) // üst
        ikincidenÜçüncüyeGit()
        içÜçgenler(minik, altkat) // alt sağ
        üçüncüdenBaşaDön()
    }
}

def sierpinski(boy: Kesir, kat: Sayı) {
    biçimleriBelleğeYaz()
    boyamaRenginiKur(yellow)
    üçgen(boy)
    biçimleriGeriYükle()
    içÜçgenler(boy, kat)
}

sil()
canlandırmaHızınıKur(10)
kalemRenginiKur(mavi)
kalemKalınlığınıKur(1)
konumuKur(-200, -150)
sierpinski(400, 6) // kenar uzunluğunu ve kaç kat çizmek istediğimizi burda girelim
