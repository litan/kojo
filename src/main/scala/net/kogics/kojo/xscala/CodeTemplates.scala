package net.kogics.kojo.xscala

object CodeTemplates {
  val templates = Map(
    "pre" -> """<pre>
    ${cursor}
</pre>
""",
    "incr" -> """IncrPage(
    name = "",
    style = "",
    body = List(
        Para(
            <p>
                ${cursor}
            </p>
        ),
        Para(
            <p>
                
            </p>
        )
    )
)
""")

  def apply(name: String) = templates(name)
  def asString(name: String) = xml.Utility.escape(templates(name).replace("${cursor}", "|"))
  def beforeCursor(name: String) = templates(name).split("""\$\{cursor\}""")(0)
  def afterCursor(name: String) = templates(name).split("""\$\{cursor\}""")(1)
}