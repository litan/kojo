val pageStyle = "color:black;background-color:#aaddFF; margin:15px;font-size:small;"
val centerStyle = "text-align:center;"

val pg = Page(
    name = "",
    body =
        <body style={ pageStyle + centerStyle }>
            { for (i <- 1 to 5) yield { <br/> } }
            <h3>To learn how to write programs in Kojo</h3>
            Visit the <a href="http://docs.kogics.net">Kojo Docs</a> site<br/>
            <p>http://docs.kogics.net</p>
        </body>
)

val story = Story(pg)
stClear()
stPlayStory(story)
