
// Set Up Styles for tutorial pages
val pageStyle = "color:black;background-color:#99CCFF; margin:15px;font-size:small;"
val centerStyle = "text-align:center;"
val headerStyle = "text-align:center;font-size:110%;color:maroon;"
val codeStyle = "font-size:90%;"
val smallNoteStyle = "color:gray;font-size:95%;"
val sublistStyle = "margin-left:60px;"

import language.implicitConversions
import language.postfixOps

import net.kogics.kojo.util.Utils

showVerboseOutput()
retainSingleLineCode()
def pgHeader(hdr: String) =
    <p style={headerStyle}>
        {new xml.Unparsed(hdr)}
        {nav}
        <hr/>
        
    </p>

def tCode(cd:String)=Para(
    <div style="background-color:CCFFFF;"> <pre style={codeStyle}> {cd} </pre> </div>,
    code = {clearOutput;stRunCode(cd);stSetScript(cd)}
)

var pages = new collection.mutable.ListBuffer[StoryPage]
var pg: StoryPage = _
var header: xml.Node = _

def link(page: String) = "http://localpage/%s" format(page)
val homeLink = <div style={smallNoteStyle+centerStyle}><a href={link("home#7")}>Start Page</a></div>

nesne MenüSeçeneği {
  den menüsüVarMı = doğru // varsayım. scala-tutorial'da kullanılıyor
}
def nav = if (MenüSeçeneği.menüsüVarMı) {<div style={smallNoteStyle}>
        <a href={link(("Menu").toString)}>Menü</a>
         </div>} else {}

// Mark up support

implicit def toSHtm(s:String):SHtm={new SHtm(escTrx(s))}
def escTrx(s:String) = s.replace("&" , "&amp;").replace(">" , "&gt;").replace("<" , "&lt;")
def escRem(s:String) = s.replace("&amp;","&"  ).replace("&gt;",">").replace("&lt;","<")
def row(c:SHtm *)={
    val r=c.map(x=>{new SHtm("<td>" + x.s + "</td>")}).reduce(_ + _)
    new SHtm("<tr>" + r.s + "</tr>")
}
def table(c:SHtm *)={
    val r=c.reduce(_ + _)
    new SHtm("""<table border="1">""" + r.s + "</table>")
}
//def toHtm(h:SHtm *)={h.reduce(_ + _)}

def tPage(title:String,h:SHtm *)={
    <body style={pageStyle}>
    <p style={headerStyle}>
        {new xml.Unparsed(title)}
        {nav}
        <hr/>
        {new xml.Unparsed(h.reduce(_ + _).s)}
    
    </p>
    </body>
    
}
val codeExamples = new Array[String](1000) // no need to translate this Array
var codeID = 0
// Mark up DSL definitions class

class SHtm (var s:String){
    def h2=new SHtm("<h2>" + s + "</h2>")
    def h3=new SHtm("<h3>" + s + "</h3>")
    def h4=new SHtm("<h4>" + s + "</h4>")
    def i=new SHtm("<i>" + s + "</i>")
    def h1=new SHtm("<h1>" + s + "</h1>")
    def p=new SHtm("<p>" + s + "</p>")
    def b=new SHtm("<b>" + s + "</b>")
    def link(url:String)=new SHtm("<a href=\"http://" + url + " \">" + s + "</a></br>")
    def c={
    codeID+=1
    codeExamples(codeID)=escRem(s)
    new SHtm("""<hr/><div style=background-color:CCFFFF;"> <pre><code><a href="http://runhandler/example/""" +
                 codeID.toString +
                 """ " style="text-decoration: none;font-size:x-small;">""" + s + """</a></code></pre><hr/></div>""")
    }
    def + (a:SHtm)={new SHtm(s + "\n" + a.s) }
}
