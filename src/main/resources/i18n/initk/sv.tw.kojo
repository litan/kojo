//Swedish Turtle wrapper for Kojo
//Contributed by Bjorn Regnell 

println("Välkommen till Kojo med svensk padda!")
if (isScratchPad) {
  println("Historiken kommer inte att sparas när du stänger Kojo Scratchpad.") 
}
setEditorTabSize(2)

//initialize unstable value
net.kogics.kojo.lite.i18n.SwedishAPI.builtins = net.kogics.kojo.lite.Builtins.instance

//make swedish names visible
import net.kogics.kojo.lite.i18n.SvInit
import net.kogics.kojo.lite.i18n.SwedishAPI._
import padda.{sudda => _, _}


//code completion
addCodeTemplates(
    "sv",
    SvInit.codeTemplates
)
//help texts
addHelpContent(
    "sv", 
    SvInit.helpContent
)