import java.io.File

def loadWordList: List[String] = {
    val istream = new java.io.FileInputStream(getFile)
    try {
        val s = scala.io.Source.fromInputStream(istream)
        s.getLines.toList
    }
    catch {
        case e: Exception =>
            println("Could not load word list" + e)
            throw e
    }
    finally {
        istream.close
    }
}

// looking for the file under installDir:
//   ./examples/anagram/words.txt or
//   ./installer/examples/anagram/words.txt
def getFile = {
    val s = File.separatorChar
    val path = installDir + s + "examples" + s + "anagram" + s + "words.txt"
    val f1 = new java.io.File(path)
    if (f1.exists) {
        f1
    }
    else {
        val path = installDir + s + "installer" + s + "examples" + s + "anagram" + s + "words.txt"
        val f2 = new java.io.File(path)
        if (f2.exists) {
            f2
        }
        else {
            throw new Exception("Could not open the file that contains the word list at " + path)
        }
    }
}

def testLoad() = {
    val dict = loadWordList
    println(s"There are ${dict.length} words in the dictionary.")
    println(s"First ten: ${dict take 10 mkString (", ")} and last ten: ${dict takeRight 10 mkString (", ")}")
    dict
}
//testLoad