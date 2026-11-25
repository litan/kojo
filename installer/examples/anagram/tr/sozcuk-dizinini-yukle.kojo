getir java.io.File

tanım sözcükDizininiYükle: Dizin[Yazı] = {
    dez istream = yeni java.io.FileInputStream(getFile)
    dene {
        dez s = scala.io.Source.fromInputStream(istream)(scala.io.Codec.UTF8)
        s.getLines.toList
    }
    yakala {
        durum e: Exception =>
            println("Sözcük dizinini yükleyemedik: " + e)
            bildir e
    }
    sonunda {
        istream.close
    }
}

// looking için the file under installDir:
//   ./examples/anagram/tr/sozcukler.txt or
//   ./installer/examples/anagram/tr/sozcukler.txt
tanım getFile = {
    dez s = File.separatorChar
    dez path = installDir + s + "examples" + s + "anagram" + s + "tr" + s + "sozcukler.txt"
    dez f1 = yeni java.io.File(path)
    eğer (f1.exists) {
        f1
    }
    yoksa {
        dez path = installDir + s + "installer" + s + "examples" + s + "anagram" + s + "tr" + s + "sozcukler.txt"
        dez f2 = yeni java.io.File(path)
        eğer (f2.exists) {
            f2
        }
        yoksa {
            bildir yeni KuralDışı("Sözcük dizin dosyasını şurada bulamadık: " + path)
        }
    }
}

tanım yüklemeyiDene() = {
    dez dict = sözcükDizininiYükle
    println(s"Dizinde ${dict.length} sözcük var.")
    println(s"İlk onu: ${dict take 10 mkString (", ")} ve son onu: ${dict takeRight 10 mkString (", ")}")
    dict
}
// yüklemeyiDene
