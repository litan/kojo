val sayfaBiçimi = "color:black;background-color:#aaddFF; margin:15px;font-size:small;"
val ortalamaBiçimi = "text-align:center;"

val sayfa = Page(
  name = "",
  body =
    <body style={ sayfaBiçimi + ortalamaBiçimi }>
      { for (i <- 1 to 5) yield { <br/> } }
      <h3>Kojo'yla bilgisayar programlamayı öğrenmek için</h3>
      şu sayfaya bakabilirsin: <a href="http://docs.kogics.net">Kojo Kullanma Kılavuzu</a><br/>
      <p>http://docs.kogics.net</p>
        <p>Ne yazık ki henüz İngilizce'ye çevrilmedi İnternet'teki bu büyük kılavuz.</p>
      <p>Ama <b>Örnekler</b>, <b>Sergi</b> ve <b>Araçlar</b> menülerindeki yazılımlara göz atarak epey çok şey öğrenmek mümkün.</p>
      <p>Bir de <b>Yardım</b> menüsündeki <b>Scala'ya Giriş</b> kılavuzunu okuyarak bilgini epey artırabilirsin.</p>
      <p><b>Sevgiler</b> ve <b>başarılar</b>!</p>
      </body>
)

val öykü = Story(sayfa)
stClear() // öyküyü temizle
stPlayStory(öykü) // öyküyü anlatmaya başla
