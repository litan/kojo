//Contributed by Bjorn Regnell 
//Swedish Turtle wrapper for Kojo version 5 updated 2013-03-10
class Padda(val englishTurtle: Turtle) {
  def this () = this(newTurtle)
  def this (startX: Double, startY: Double) = this (newTurtle(startX, startY))
  def this (startX: Double, startY: Double, kostymFilNamn: String) = this (newTurtle(startX, startY, kostymFilNamn))
  def fram(steg:Double) = englishTurtle.forward(steg)
  def fram() = englishTurtle.forward(25)
  def höger(vinkel:Double) = englishTurtle.right(vinkel)
  def höger() = englishTurtle.right(90)
  def vänster(vinkel:Double) = englishTurtle.left(vinkel) 
  def vänster() = englishTurtle.left(90) 
  def hoppaTill(x:Double, y:Double) = englishTurtle.jumpTo(x, y)
  def gåTill(x:Double, y:Double) = englishTurtle.moveTo(x, y)
  def hoppa(steg:Double) = saveStateAndDo { pennaUpp; fram(steg) }
  def hoppa() = saveStateAndDo { pennaUpp; fram() }
  def hem() = englishTurtle.home()
  def mot(x:Double, y:Double) = englishTurtle.towards(x, y)
  def sättVinkel(vinkel:Double) = englishTurtle.setHeading(vinkel)
  def vinkel = englishTurtle.heading
  def öster() = englishTurtle.setHeading(0)
  def väster() = englishTurtle.setHeading(180)
  def norr() = englishTurtle.setHeading(90)
  def söder() = englishTurtle.setHeading(-90)  
  def sakta(n: Long) = englishTurtle.setAnimationDelay(n) 
  def skriv(t : Any) = englishTurtle.write(t)
  def textstorlek(s:Int) = englishTurtle.setPenFontSize(s)
  def båge(radie:Double, vinkel:Double) = englishTurtle.arc(radie, math.round(vinkel).toInt)
  def cirkel(radie:Double) = englishTurtle.circle(radie)
  def synlig() = englishTurtle.visible()
  def osynlig() = englishTurtle.invisible()
  def läge = englishTurtle.position
  def pennaNer() = {penIsDown = true ; englishTurtle.penDown()}
  def pennaUpp() = {penIsDown = false ; englishTurtle.penUp()}  
  def pennanÄrNere = penIsDown
  def färg(c:java.awt.Color) = englishTurtle.setPenColor(c)
  def fyll(c:java.awt.Color) = englishTurtle.setFillColor(c)
  def bredd(n:Double) = englishTurtle.setPenThickness(n)
  def sparaStil() = englishTurtle.saveStyle()
  def laddaStil() = englishTurtle.restoreStyle()
  def sparaLägeRiktning() = englishTurtle.savePosHe()
  def laddaLägeRiktning() = englishTurtle.restorePosHe()
  def siktePå() = englishTurtle.beamsOn()
  def sikteAv() = englishTurtle.beamsOff()
  def kostym(filNamn: String) = englishTurtle.setCostume(filNamn)
  private var penIsDown = true
  private def saveStateAndDo(doThis: => Unit) {
    val wasDown = penIsDown
    doThis
    if (wasDown) pennaNer else pennaUpp
  }
}
object padda extends Padda(turtle0) 
import padda._  
def sudda() = clear()
def suddaUtdata() = clearOutput()
val blå=blue; val röd=red; val gul=yellow; val grön=green; val lila=purple;
val rosa=pink; val brun=brown; val svart=black; val vit=white; 
val genomskinlig = Color(0, 0, 0, 0)
def bakgrund(färg:Color) = setBackground(färg)
def bakgrund2(färg1:Color, färg2:Color) = setBackgroundV(färg1, färg2)
//loops in Swedish
def upprepa(n:Int)(block : => Unit){
    for (i <- 1 to n) block
}
def räkneslinga(n:Int)(block : Int => Unit){
    for (i <- 1 to n) block(i)
}
def sålänge(villkor : => Boolean)(block : => Unit){
  while (villkor) block
}
//simple IO
def utdata(data:Any) = println(data)
def indata(ledtext:String="") = readln(ledtext)
//math functions
def avrunda(tal:Number, antalDecimaler:Int=0):Double = {
  val faktor = math.pow(10, antalDecimaler).toDouble
  math.round(tal.doubleValue * faktor).toLong / faktor
}
def slumptal(n:Int) = random(n)
def slumptalMedDecimaler(n:Int) = randomDouble(n)
//some type aliases in Swedish
  type Heltal = Int
  type Decimaltal = Double
  type Sträng = String
//speedTest
def systemtid = BigDecimal(System.nanoTime) / BigDecimal("1000000000") //sekunder
def räknaTill(n:BigInt) { 
    var c:BigInt = 1
    print("*** Räknar från 1 till ... ")
    val startTid = systemtid
    while (c < n) {c = c + 1} //tar tid om n är stort
    val stoppTid = systemtid
    println("" + n + " *** KLAR!")
    val tid = stoppTid - startTid
    print("Det tog ")
    if (tid<0.1) 
        println((tid*1000).round(new java.math.MathContext(2)) + 
                " millisekunder.")
    else println((tid*10).toLong/10.0 + " sekunder.")
}
//code completion
addCodeTemplates(
    "sv", 
    Map(
        "fram" -> "fram(${steg})",
        "höger" -> "höger(${vinkel})",
        "vänster" -> "vänster(${vinkel})",
        "hoppaTill" -> "hoppaTill(${x},${y})",
        "gåTill" -> "gåTill(${x},${y})",
        "hoppa" -> "hoppa(${steg})",
        "hem" -> "hem()",
        "mot" -> "mot(${x},${y})",
        "sättVinkel" -> "sättVinkel(${vinkel})",
        "öster" -> "öster()",
        "väster" -> "väster()",
        "norr" -> "norr()",
        "söder" -> "söder()",
        "sakta" -> "sakta(${fördröjning})",
        "skriv" -> "skriv(${sträng})",
        "textstorlek" -> "textstorlek(${storlek})",
        "båge" -> "båge(${radie},${vinkel})",
        "cirkel" -> "cirkel(${radie})",
        "synlig" -> "synlig()",
        "osynlig" -> "osynlig()",
        "läge" -> "läge",
        "pennaNer" -> "pennaNer()",
        "pennaUpp" -> "pennaUpp()",
        "pennanÄrNere" -> "pennanÄrNere",
        "färg" -> "färg(${pennfärg})",
        "fyll" -> "fyll(${fyllfärg})",
        "bredd" -> "bredd(${pennbredd})",
        "sparaStil" -> "sparaStil()",
        "laddaStil" -> "laddaStil()",
        "sparaLägeRiktning" -> "sparaLägeRiktning()",
        "laddaLägeRiktning" -> "laddaLägeRiktning()",
        "siktePå" -> "siktePå()",
        "sikteAv" -> "sikteAv()",
        "engelska" -> "engelska",
        "sudda" -> "sudda()",
        "suddaUtdata" -> "suddaUtdata()",
        "bakgrund" -> "bakgrund(${färg})",
        "bakgrund2" -> "bakgrund2(${färg1},${färg2})",
        "upprepa" -> "upprepa (${antal}) {\n    \n}",
        "räkneslinga" -> "räkneslinga (${antal}) { i => \n    \n}",
        "sålänge" -> "sålänge (${villkor}) {\n    \n}",
        "utdata" -> "utdata(${sträng})",
        "indata" -> "indata(${ledtext})",
        "avrudna" -> "avrudna(${tal},${antalDecimaler})",
        "slumptal" -> "slumptal(${mindreän})", 
        "slumptalMedDecimaler" -> "slumptalMedDecimaler(${mindreän})", 
        "räknaTill" -> "räknaTill(${tal})", 
        "systemtid" -> "systemtid",
        "kostym" -> "kostym(${filnamn})"
  )
)
//help texts
addHelpContent(
    "sv", 
    Map(
      "fram" -> 
      <div>
<strong>fram</strong>(steg) - Paddan går frammåt det antal steg du anger i riktningen dit nosen pekar.
<br/>Om pennan är nere så ritar paddan när den går frammåt.
<br/><em>Exempel:</em> <br/><br/>
<pre>
sudda()    //ritfönstret suddas och paddan ställs i mitten     
fram(100)  //paddan går 100 steg
fram()     //inget värde: paddan går 25 steg
pennaUpp   //paddan lyfter pennan
fram(200)  //paddan går 200 steg utan att rita
höger(45)  //paddan vrider sig 45 grader åt höger
</pre>
      </div>.toString,
      "vänster" -> <div><strong>vänster</strong>(vinkel)<br/>Paddan vrider åt vänster.</div>.toString,
      "höger" -> <div><strong>höger</strong>(vinkel)<br/>Paddan vrider sig åt höger.</div>.toString,
      "hoppaTill" -> <div><strong>hoppaTill</strong>(x, y)<br/>Paddan hoppar till läge (x,y) utan att rita och utan att ändra riktning.</div>.toString,
      "gåTill" -> <div><strong>gåTill</strong>(x, y)<br/>Paddan vrider sig mot läge (x,y) och går dit.<br/>Om pennan är nere så ritar paddan när den går.</div>.toString,
      "hoppa" -> <div><strong>hoppa</strong>(steg)<br/>Paddan hoppar i riktningen dit nosen pekar det antal steg som anges utan att rita även om pennan är nere.</div>.toString,
      "hem" ->  <div><strong>hem</strong>()<br/>Paddan går tillbaka till läge origo (0,0) och vrider sig så att nosen pekar norr.<br/>Om pennan är nere så ritar paddan när den går.</div>.toString,
      "mot" -> <div><strong>mot</strong>(x, y)<br/>Paddan vrider sig så att nosen pekar mot läge (x,y)</div>.toString,
      "sättVinkel" -> <div><strong>sättVinkel</strong>(vinkel)<br/>Paddan vrider sig så att nosen får den vinkel som anges.</div>.toString,
      "vinkel" -> <div><strong>vinkel</strong>(vinkel)<br/>Ger värdet på vinkeln dit paddans nos pekar.</div>.toString,
      "öster" -> <div><strong>öster</strong>()<br/>Paddan vrider sig så att nosen pekar mot öster (höger).</div>.toString,
      "väster" -> <div><strong>väster</strong>()<br/>Paddan vrider sig så att nosen pekar mot väster (vänster).</div>.toString,
      "norr" -> <div><strong>norr</strong>()<br/>Paddan vrider sig så att nosen pekar mot norr (upp).</div>.toString,
      "söder" -> <div><strong>söder</strong>()<br/>Paddan vrider sig så att nosen pekar mot söder (ner).</div>.toString,
      "sakta" -> <div><strong>sakta</strong>(fördröjning)<br/>Ju mer fördröjning desto långsammare padda.<br/>Minsta fördröjning är 0<br/>dröj(1000) är ganska långsamt.</div>.toString,
      "skriv" -> <div><strong>skriv</strong>(sträng)<br/>Paddan skriver en sträng i ritfönstret<br/>En sträng måste ha dubbelfnuttar i början och slutet. Exempel: skriv("hej")</div>.toString,
      "textstorlek" -> <div><strong>textstorlek</strong>(storlek)<br/>Ändrar storleken på texten som paddan skriver.</div>.toString,
      "båge" -> <div><strong>båge</strong>(radie, vinkel)<br/>Paddan ritar ett cirkelsegment med angiven radie och vinkel.</div>.toString,
      "cirkel" -> <div><strong>cirkel</strong>(radie)<br/>Paddan ritar en cirkel med angiven radie.</div>.toString,
      "synlig" -> <div><strong>synlig</strong>()<br/> Gör så att paddan syns igen om den är osynlig.</div>.toString,
      "osynlig" -> <div><strong>osynlig</strong>()<br/>Gör paddan osynlig.</div>.toString,
      "läge" -> <div><strong>läge</strong><br/>Ger paddans läge som ett punktvärde Point(x,y)
<br/><em>Exempel:</em> <br/><br/>
<pre>
utdata(läge)     //skriver ut paddans läge i utdatafönstret     
utdata(läge.x)     //skriver ut paddans x-läge i utdatafönstret     
utdata(läge.y)     //skriver ut paddans y-läge i utdatafönstret     

var x = läge.x   //lagrar paddans x-läge i variablen x
var y = läge.y   //lagrar paddans y-läge i variablen y
x = x - 100      //ändrar variabeln x till gamla värdet minus 100
hoppaTill(x, y)  //hoppar till nytt läge med ändrat x 
</pre>
      </div>.toString, 
      "pennaNer" -> <div><strong>pennaNer</strong>()<br/>Sätter ner paddans penna så att den ritar när paddan går.</div>.toString,
      "pennaUpp" -> <div><strong>pennaUpp</strong>()<br/>Lyfter upp paddans penna så att den inte ritar när paddan går.</div>.toString,
      "pennanÄrNere" -> <div><strong>pennanÄrNere</strong><br/>Kollar om paddans penna är nere. Ger <strong>true</strong> om pennan är nere och <strong>false</strong> om pennan är uppe.</div>.toString,
      "färg" -> <div><strong>färg</strong>(pennfärg)<br/>Gör så att paddans penna ritar med angiven pennfärg.
<br/>Du kan anväda dessa färdigblandade färger:<br/>blå, röd, gul, grön, lila, rosa, brun, svart, vit, genomskinlig.
<br/>Du kan blanda egna färger med Color 
<br/><em>Exempel:</em> <br/><br/>
<pre>
färg(blå)                  //sätter pennans färg till blå
fram()                     //paddan går frammåt
färg(Color(220,30,40,250)) //blandar ny färg (ljuslila) 
//rödvärde=220, grönvärde=30, blåvärde=40, genomskinlighet=250
//färgvärden till Color mellan 0 och 255
fram(200)
</pre>
       </div>.toString,
      "fyll" -> <div><strong>fyll</strong>(fyllfärg)<br/>Gör så att paddan fyller i med angiven fyllfärg när den ritar.
<br/>Du kan anväda dessa färdigblandade färger:
<br/>blå, röd, gul, grön, lila, rosa, brun, svart, vit, genomskinlig.
<br/>Du kan blanda egna färger med Color 
<br/><em>Exempel:</em> <br/><br/>
<pre>
sudda();sakta(0)
fyll(blå)          //sätter fyllfärg till blå
cirkel(100)        //paddan ritar blå cirkel med radie 100
hoppa(100)         //paddan hoppar fram 100 steg
fyll(genomskinlig) //sätter fyllfärg till genomskinlig
cirkel(100)        //paddan ritar genomskinlig cirkel
</pre>
      </div>.toString,
      "bredd" -> <div><strong>bredd</strong>(pennbredd)<br/>Ändrar pennbredden på paddans penna.<br/>Ju högre pennbredd desto tjockare streck.</div>.toString,
      "sparaStil" -> <div><strong>sparaStil</strong>()<br/>Sparar undan pennans färg, fyllfärg, bredd och textstorlek.<br/>Du kan få tillbaka den sparade stilen med laddaStil</div>.toString,
      "laddaStil" -> <div><strong>laddaStil</strong>()<br/>Hämtar sparad pennstil och sätter tillbaka pennans färg, fyllfärg, bredd och textstorlek.<br/>Du spara en pennstil med sparaStil</div>.toString,
      "sparaLägeRiktning" -> <div><strong>sparaLägeRiktning</strong>()<br/>Sparar undan pennans läge och riktning.<br/>Du kan få tillbaka den sparade pennans position och riktning med laddaLägeRiktning</div>.toString,
      "laddaLägeRiktning" -> <div><strong>laddaLägeRiktning</strong>()<br/>Hämtar sparad riktning och läge.<br/>Du sparar en pennstil med sparaLägeRiktning</div>.toString,
      "siktePå" -> <div><strong>siktePå</strong>()<br/>Visar vilket håll paddan siktar mot med ett hårkors-sikte.</div>.toString,
      "sikteAv" -> <div><strong>sikteAv</strong>()<br/>Gömmer paddans hårkors-sikte.</div>.toString,
      "engelska" -> <div><strong>engelska</strong><br/>Ger den engelska paddan.<br/>Om du skriver:<br/>padda.eneglska.<br/>kan du se allt som en padda kan göra på engelska.</div>.toString,
      "sudda" -> <div><strong>sudda</strong>()<br/>Suddar allt som ritats i ritfönstret.</div>.toString,
      "suddaUtdata" -> <div><strong>suddaUtdata</strong>()<br/>Suddar allt som skrivits i utdatafönstret.</div>.toString,
      "bakgrund" -> <div><strong>bakgrund</strong>(bakgrundsfärg)<br/>Gör så att bakgrundsfärgen ändras.<br/>Du kan anväda dessa färdigblandade färger:<br/>blå, röd, gul, grön, lila, rosa, brun, svart, vit, genomskinlig.<br/>Du kan blanda egna färger med Color </div>.toString,
      "bakgrund2" -> <div><strong>bakgrund</strong>(färg1,färg2)<br/>Gör så att bakgrundsfärgen blir en övergång från färg1 till färg2.<br/>Du kan anväda dessa färdigblandade färger:<br/>blå, röd, gul, grön, lila, rosa, brun, svart, vit, genomskinlig.<br/>Du kan blanda egna färger med Color </div>.toString,
      "upprepa" -> <div><strong>upprepa</strong>(antal) {{ satser }} - upprepar <em>satser</em> det antal gånger som anges.
        <br/><em>Exempel:</em> <br/><br/>
        <pre>
upprepa(4) {{ 
      fram
      vänster
}}
        </pre>
      </div>.toString,
      "räkneslinga" -> <div><strong>räkneslinga</strong>(antal) {{ i => satser }} - upprepar <em>satser</em> det antal gånger som anges och räknar varje runda i slingan. Räknarens värde finns i värdet <strong>i</strong>
        <br/><em>Exempel:</em> <br/><br/>
        <pre>
räkneslinga(10) {{ i =>
      utdata(i)
}}
        </pre>
      </div>.toString,
      "sålänge" -> <div><strong>sålänge</strong>(villkor) {{  satser }} - upprepar <em>satser</em> så länge <em>villkor</em> är sant. 
        <br/><em>Exempel:</em> <br/><br/>
        <pre>var i = 0
sålänge(i{"<"}10) {{ 
      utdata(i)
      i = i + 1
}}
        </pre>
      </div>.toString,
      "utdata" -> <div><strong>utdata</strong>(textsträng)<br/>Skriver texten i <em>textsträng</em> i utdatafönstret<br/>En textsträng måste ha dubbelfnuttar i början och slutet. Exempel: utdata("hej")</div>.toString,
      "indata" -> <div><strong>indata</strong>(ledtext)<br/>Skriver ut ledtext i utdatafönstret och väntar på inmatning av text tills du trycker Enter.<br/>
        <br/><em>Exempel:</em> <br/><br/>
        <pre>val x = indata("Skriv ditt namn ")
utdata("Hej " + x + "!")
        </pre>
      </div>.toString,
      "avrunda" -> <div><strong>avrunda</strong>(decimaltal, antalDecimaler)<br/>Avrundar decimaltal till angivet antal decimaler<br/>
        <br/><em>Exempel:</em> <br/><br/>
        <pre>val t1 = avrunda(3.991,2) //Avrundar till 2 decimaler
utdata(t1)
val t2 = avrunda(3.999) //Avrundar till 0 decimaler
utdata(t2)
        </pre>
      </div>.toString,
      "systemtid" -> <div><strong>systemtid</strong><br/>Ger systemklockans tid i sekunder. Du kan använda systemtid för att mäta hur lång tid något tar.<br/>
        <br/><em>Exempel:</em> <br/><br/>
<pre>
val start = systemtid
utdata("Ha tålamod!")
räknaTill(100000000)
val stopp = systemtid
val s = stopp - start
utdata("Du hade tålamod i " + avrunda(s,1) + " sekunder.")
        </pre>
      </div>.toString,
      "räknaTill" -> <div><strong>räknaTill</strong>(tal)<br/>Kollar hur lång tid det tar för datorn att räkna till ett visst tal. DU kan prova med ganska stora tal<br/>
        <br/><em>Exempel:</em> <br/><br/>
        <pre>
räknaTill(5000)
        </pre>
      </div>.toString,
      "slumptal" -> <div><strong>slumptal</strong>(mindreÄn)<br/>Ger ett slumptal mellan 0 och mindreÄn.<br/><em>Exempel:</em><br/><pre>  def slump = slumptal(20) + 1 </pre><br/>Ger slumptal från 1 till och med 20</div>.toString,
      "slumptalMedDecimaler" -> <div><strong>slumptalMedDecimaler</strong>(mindreÄn)<br/>Ger ett slumptal med decimaler mellan 0 och mindreÄn.<br/><em>Exempel:</em><br/><pre> def slump = slumptalMedDecimaler(20) + 1.0</pre><br/>Ger slumptal med decimaler från 1.0 till och med 20.0</div>.toString,
      "kostym" -> <div><strong>kostym</strong>(filNamn)<br/>Ändrar paddans utseende efter bild i fil.<br/><em>Exempel:</em><br/><pre>  
sudda       
kostym("bakgrund.jpg") //den vanliga paddan blir bakgrund.jpg 
fram(100) //bakgrunden flyttas
val gubbe = new Padda(100,100,"gubbe.jpg") //ny padda skapas på plats (100, 100) med bilden gubbe.jpg 
gubbe.fram(100) 
</pre><br/></div>.toString
      )
)

utdata("Välkommen till Kojo med svensk padda!")
if (isScratchPad) {
  utdata("Historiken kommer inte att sparas när du stänger Kojo Scratchpad.") 
}

