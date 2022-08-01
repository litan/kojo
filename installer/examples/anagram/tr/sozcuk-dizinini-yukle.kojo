import java.io.File

def sözcükDizininiYükle: Dizin[Yazı] = {
    val istream = new java.io.FileInputStream(getFile)
    try {
        val s = scala.io.Source.fromInputStream(istream)(scala.io.Codec.UTF8)
        s.getLines.toList
    }
    catch {
        case e: Exception =>
            println("Sözcük dizinini yükleyemedik: " + e)
            throw e
    }
    finally {
        istream.close
    }
}

// looking for the file under installDir:
//   ./examples/anagram/tr/sozcukler.txt or
//   ./installer/examples/anagram/tr/sozcukler.txt
def getFile = {
    val s = File.separatorChar
    val path = installDir + s + "examples" + s + "anagram" + s + "tr" + s + "sozcukler.txt"
    val f1 = new java.io.File(path)
    if (f1.exists) {
        f1
    }
    else {
        val path = installDir + s + "installer" + s + "examples" + s + "anagram" + s + "tr" + s + "sozcukler.txt"
        val f2 = new java.io.File(path)
        if (f2.exists) {
            f2
        }
        else {
            throw new Exception("Sözcük dizin dosyasını şurada bulamadık: " + path)
        }
    }
}

def yüklemeyiDene() = {
    val dict = sözcükDizininiYükle
    println(s"Dizinde ${dict.length} sözcük var.")
    println(s"İlk onu: ${dict take 10 mkString (", ")} ve son onu: ${dict takeRight 10 mkString (", ")}")
    dict
}
// yüklemeyiDene
