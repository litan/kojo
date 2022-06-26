val sayfaBiçimi = "color:black;background-color:#aaddFF; margin:15px;font-size:small;"
val ortalamaBiçimi = "text-align:center;"

val sayfa = Page(
    name = "Ana sayfa",
    body =
        <body style={ sayfaBiçimi + ortalamaBiçimi }>
      { for (i <- 1 to 5) yield { <br/> } }
      <h3>Kojo'ya hoşgeldin!</h3>
      Kojo diye yazılıyor ama Koco diye okunuyor bu kolay öğrenim ortamının adı.<br/>
      Kojo'yla bilgisayar programlamayı öğrenmek için şu sayfaya bakabilirsin:<br/>
        <a href="http://docs.kogics.net">Kojo Kullanma Kılavuzu</a><br/>
      http://docs.kogics.net
        <p>Ne yazık ki henüz İngilizce'ye çevrilmedi İnternet'teki bu büyük kılavuz. Ama google tercümanla kolaylıkla okuyabilirsin</p>
      <p>Öte yandan <b>Örnekler</b>, <b>Sergi</b> ve <b>Araçlar</b> menülerindeki yazılımlara göz atarak epey çok şey öğrenmek mümkün.</p>
      <p>Bir de <b>Yardım</b> menüsündeki <b>Scala'ya Giriş</b> kılavuzunu okuyarak bilgini epey artırabilirsin.</p>
      </body>
)

// Yeni bir sayfa oluşturmak da çok kolay
val sayfaX = Page(
    name = "BURAYA ADINI YAZ",
    body = <body style={ sayfaBiçimi + ortalamaBiçimi }>
      { for (i <- 1 to 5) yield { <br/> } }
      <h3>BURAYA ARA BAŞLIK AT </h3>
      <p>
          Bu bir paragraf.
      </p>
      <p> Bu başka bir paragraf. </p>
     </body>
)

val sayfaAra = Page(
    name = "Ara sayfa",
    body = <body style={ sayfaBiçimi + ortalamaBiçimi }>
      { for (i <- 1 to 5) yield { <br/> } }
      <h3>Yinelemekte fayda var</h3>
      <p>
      <b>Yardım</b> menüsündeki <b>Scala'ya Giriş</b> kılavuzuna
      bir bak. Orada pek çok küçücük örnek var. Onları olduğu gibi çalıştırmak,
      sonra istediğin gibi değiştirip tekrar çalıştırmak çok kolay.
      </p> <p>
      Yanlız, epey uzun bir kılavuz. Tam 18 tane sayfası var ki bazıları epey uzun.
      Ama hepsini sırayla okuman gerekmez. İlk iki sayfasını, yani girişi ve
      <em> Başlayalım </em> safyalarını okuduktan sonra, sona yakın olan
      <em> Kaplumbağacığın Kullanılışı </em> ve <em> Çizim ve Oyun </em>
      adlı sayfalara bak. Onlardan çok şey öğreneceksin.
      </p> <p> Ondan sonra sırayla hepsini oku. Bu sayede sonradan anlayacaksın ki bilgisayar
      programlamayı öğrenivermişsin. Ondan sonrası sana ve hevesine kalmış.
      </p>
      </body>
)

val sayfaSon = Page(
    name = "Son sayfa",
    body = <body style={ sayfaBiçimi + ortalamaBiçimi }>
      { for (i <- 1 to 5) yield { <br/> } }
      <h3><b>Sevgiler</b>, <b> saygılar</b> ve <b>başarılar</b></h3>
      <p>Şimdilik hoşçakal!</p>
     </body>
)

val öykü = Story(sayfa, sayfaAra, /* sayfaX, */ /* istediğin kadar sayfa ekleyebilirsin... */ sayfaSon)
stClear() // öyküyü temizle
stPlayStory(öykü) // öyküyü anlatmaya başla
