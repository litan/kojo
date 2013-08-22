//Swedish Turtle wrapper for Kojo
//Contributed by Bjorn Regnell 

println("Välkommen till Kojo med svensk padda!")
if (isScratchPad) {
  println("Historiken kommer inte att sparas när du stänger Kojo Scratchpad.") 
}
setEditorTabSize(2)

trait SwedishTurtle { 
	def englishTurtle: Turtle 
  def sudda() = englishTurtle.clear()
  def synlig() = englishTurtle.visible()
  def osynlig() = englishTurtle.invisible()
  def fram(steg:Double) = englishTurtle.forward(steg)
  def fram() = englishTurtle.forward(25)
  def höger(vinkel:Double) = englishTurtle.right(vinkel)
  def höger() = englishTurtle.right(90)
  def vänster(vinkel:Double) = englishTurtle.left(vinkel) 
  def vänster() = englishTurtle.left(90) 
  def hoppaTill(x:Double, y:Double) = englishTurtle.jumpTo(x, y)
  def gåTill(x:Double, y:Double) = englishTurtle.moveTo(x, y)
  def hoppa(steg:Double) = {
    englishTurtle.saveStyle() //to preserve pen state
    englishTurtle.hop(steg)   //hop change state to penDown after hop
    englishTurtle.restoreStyle()    
  }
  def hoppa(): Unit = hoppa(25) 
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
  def läge = englishTurtle.position
  def pennaNer() = englishTurtle.penDown()
  def pennaUpp() = englishTurtle.penUp()  
  def pennanÄrNere = style.down
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
  def kostymer(filNamn: String *) = englishTurtle.setCostumes(filNamn:_*)
  def nästaKostym() = englishTurtle.nextCostume()
}
class Padda(override val englishTurtle: Turtle) extends SwedishTurtle {
  def this() = this(newTurtle)
  def this(startX: Double, startY: Double) = this (newTurtle(startX, startY))
  def this(startX: Double, startY: Double, kostymFilNamn: String) = this (newTurtle(startX, startY, kostymFilNamn))
}
class Padda0(t0: => Turtle) extends SwedishTurtle {  //by-name construction as turtle0 is volatile }
    override def englishTurtle: Turtle = t0 
}
object padda extends Padda0(turtle0) 
def suddaUtdata() = clearOutput()
val blå=blue; val röd=red; val gul=yellow; val grön=green; val lila=purple;
val rosa=pink; val brun=brown; val svart=black; val vit=white; 
val genomskinlig = noColor
def bakgrund(färg:Color) = setBackground(färg)
def bakgrund2(färg1:Color, färg2:Color) = setBackgroundV(färg1, färg2)
object KcSwe { //Key codes for Swedish keys
    val VK_Å = 197
    val VK_Ä = 196
    val VK_Ö = 214
}
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
import net.kogics.kojo.lite.i18n.SvInit._
//code completion
addCodeTemplates(
    "sv",
    codeTemplates
)
//help texts
addHelpContent(
    "sv", 
    helpContent
)
import padda.{sudda => _, _}
def sudda() = clear()