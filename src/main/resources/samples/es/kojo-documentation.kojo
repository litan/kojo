val estiloPag = "color:black;background-color:#aaddFF; margin:15px;font-size:small;"
val estiloCentrado = "text-align:center;"

val pag = Page(
    name = "",
    body =
        <body style={ estiloPag + estiloCentrado }>
            { for (i <- 1 to 5) yield { <br/> } }
            <h3>Para aprender a escribir programas en Kojo</h3>
            visita la página de <a href="http://docs.kogics.net">Kojo Docs</a><br/>
            <p>http://docs.kogics.net</p>
        </body>
)

val historia = Story(pag)
stClear()
stPlayStory(historia)
