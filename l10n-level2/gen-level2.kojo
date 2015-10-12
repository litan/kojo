import java.io.{ File, FileReader }
import java.util.regex.Pattern
import java.util.Properties
import net.kogics.kojo.util.RichFile.enrichFile

clearOutput()

val kojoDir = "/home/lalit/kojo-2.11"
val templateFile = s"$kojoDir/l10n-level2/langInit.scala.template"
val replacementsFile = s"$kojoDir/l10n-level2/level2_it.properties"

val varPattern = """\$\$(.*?)\$"""
val pattern = Pattern.compile(varPattern)

val template = new File(templateFile).readAsString
val replacements = new Properties
replacements.load(new FileReader(replacementsFile))

val matcher = pattern.matcher(template)

val result = new StringBuffer
while (matcher.find) {
    val varName = matcher.group(1)
    val replacement = replacements.get(varName).asInstanceOf[String]
    if ((replacement == null) || (replacement == "")) {
        println(s"property $varName is missing in props file")
    }
    else {
        matcher.appendReplacement(result, replacement)
    }
}
matcher.appendTail(result)
println(result.toString())
