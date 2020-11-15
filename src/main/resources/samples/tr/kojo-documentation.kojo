val sayfaBiçimi = "color:black;background-color:#aaddFF; margin:15px;font-size:small;"
val ortalamaBiçimi = "text-align:center;"

val sayfa = Page(
    name = "",
    body =
        <body style={ sayfaBiçimi + ortalamaBiçimi }>
            { for (i <- 1 to 5) yield { <br/> } }
            <h3>Kojo'la bilgisayar programlamayı öğrenmek için</h3>
            şu sayfaya bakabilirsin: <a href="http://docs.kogics.net">Kojo Kullanma Kılavuzu</a><br/>
            <p>http://docs.kogics.net</p>
        </body>
)

val öykü = Story(sayfa)
stClear()                  // öyküyü temizle
stPlayStory(öykü)          // öyküyü anlatmaya başla
