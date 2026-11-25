/*
 * Copyright (C) 2024
 *   Bulent Basaran <ben@scala.org> https://github.com/bulent2k2
 *   Lalit Pant <pant.lalit@gmail.com>
 *
 * The contents of this file are subject to the GNU General Public License
 * Version 3 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 */
package net.kogics.kojo.lite.i18n.tr

trait KeyCodesInTurkish {
  // From: ~/kojo-repo/src/main/scala/net/kogics/kojo/staging/KeyCodes.scala
  object tuşlar {
    val enter = '\n'
    val gir = enter
    val backSpace = '\b'
    val silGeri = backSpace
    val sekme = '\t'
    val cancel = 0x03
    val iptal = cancel
    val clear = 0x0c
    val temizle = clear
    val shift = 0x10
    val kaldırma = shift
    val control = 0x11
    val kontrol = control
    val alt = 0x12
    val pause = 0x13
    val dur = pause
    val büyükHarfKilidi = 0x14
    val escape = 0x1b
    val çık = escape
    val kaç = escape
    val boşluk = 0x20
    val pageUp = 0x21
    val pageDown = 0x22
    val end = 0x23
    val home = 0x24
    val sayfaYukarı = pageUp
    val sayfaAşağı = pageDown
    val satırSonu = end
    val satırBaşı = home
    val ev = home

    /** Constant for the non-numpad <b>left</b> arrow key.
      * @see
      *   #VK_KP_LEFT
      */
    val sol = 0x25

    /** Constant for the non-numpad <b>up</b> arrow key.
      * @see
      *   #VK_KP_UP
      */
    val yukarı = 0x26

    /** Constant for the non-numpad <b>right</b> arrow key.
      * @see
      *   #VK_KP_RIGHT
      */
    val sağ = 0x27

    /** Constant for the non-numpad <b>down</b> arrow key.
      * @see
      *   #VK_KP_DOWN
      */
    val aşağı = 0x28

    /** Constant for the comma key, ","
      */
    val virgül = 0x2c

    /** Constant for the minus key, "-"
      * @since 1.2
      */
    val eksi = 0x2d

    /** Constant for the period key, "."
      */
    val nokta = 0x2e

    /** Constant for the forward slash key, "/"
      */
    val bölü = 0x2f

    /** VK_0 thru VK_9 are the same as ASCII '0' thru '9' (0x30 - 0x39) */
    val n0 = 0x30
    val n1 = 0x31
    val n2 = 0x32
    val n3 = 0x33
    val n4 = 0x34
    val n5 = 0x35
    val n6 = 0x36
    val n7 = 0x37
    val n8 = 0x38
    val n9 = 0x39

    /** Constant for the semicolon key, ""
      */
    val noktalıVirgül = 0x3b

    /** Constant for the equals key, "="
      */
    val eşittir = 0x3d

    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A) */
    val a = 0x41
    val b = 0x42
    val c = 0x43
    // todo: ç ı ğ ö ü ş
    val d = 0x44
    val e = 0x45
    val f = 0x46
    val g = 0x47
    val h = 0x48
    val i = 0x49
    val j = 0x4a
    val k = 0x4b
    val l = 0x4c
    val m = 0x4d
    val n = 0x4e
    val o = 0x4f
    val p = 0x50
    val q = 0x51
    val r = 0x52
    val s = 0x53
    val t = 0x54
    val u = 0x55
    val v = 0x56
    val w = 0x57
    val x = 0x58
    val y = 0x59
    val z = 0x5a

    // --- Eskitilmiş yazımlar ------------------------------------------------
    //
    // Bu on ad snake_case idi; ikojo (canlı) camelCase kullanıyor ve bu
    // deponun kendi Türkçe katmanı da öyle. Ölçüm: `lite/i18n/tr/` +
    // `trInit.scala` altındaki snake_case tanımların 10'u tam da bu
    // dosyadaydı; geri kalan üçü tuş adı bile değil (`log2_e`, `a_kalıp`,
    // ve Scala'nın işleç kuralı gereği `unary_-`). Yani aykırı olan burası
    // idi, ikojo değil.
    //
    // (Burada eskiden "i18n altında 1341 camelCase / 16 snake_case" diye bir
    // sayı vardı; yöntemi kayıtlı olmadığı için yeniden üretilemedi ve
    // çıkarıldı. Yukarıdaki sayım komutla tekrarlanabilir.)
    //
    // Ayrışmanın bedeli somut: masaüstünde çalışan bir yazılımcık ikojo'da
    // "value sayfa_yukarı is not a member of ..." ile patlıyordu.
    //
    // Eski adlar takma ad olarak duruyor -- onları kullanan yazılımcıklar
    // kırılmasın diye. Depodaki kalıp bu (kuyruk/eslem/yazi'daki "eylemle
    // başlayan ada geçildi" takma adları).
    //
    // SON ÜÇ AD DA GEÇTİ. Önceki turda back_space/page_up/page_down bilerek
    // bırakılmıştı: AWT'nin VK_BACK_SPACE/VK_PAGE_UP/VK_PAGE_DOWN adlarının
    // birebir karşılığı oldukları ve dosyanın port edildiği kaynağa bağlı
    // oldukları için. O karar değişti -- tuş adlarının TEK yazımı camelCase
    // olsun istendi; AWT'ye bağlılık, Türkçe yüzeyde iki ayrı yazım taşımaya
    // değmiyor. Artık bu dosyada birincil snake_case ad KALMADI; on yılan
    // yazım da eskitilmiş takma ad olarak duruyor.
    //
    // Eskitmek SİLMEK DEĞİL: bu adlarla yazılmış yazılımcıklar derlenmeye
    // devam ediyor, yalnız uyarı alıyorlar.
    @deprecated("ikojo ile aynı yazıma geçildi: silGeri kullanın", "Eylül 2026")
    val sil_geri = silGeri
    @deprecated("ikojo ile aynı yazıma geçildi: büyükHarfKilidi kullanın", "Eylül 2026")
    val büyük_harf_kilitleme = büyükHarfKilidi
    @deprecated("ikojo ile aynı yazıma geçildi: sayfaYukarı kullanın", "Eylül 2026")
    val sayfa_yukarı = sayfaYukarı
    @deprecated("ikojo ile aynı yazıma geçildi: sayfaAşağı kullanın", "Eylül 2026")
    val sayfa_aşağı = sayfaAşağı
    @deprecated("ikojo ile aynı yazıma geçildi: satırSonu kullanın", "Eylül 2026")
    val satır_sonu = satırSonu
    @deprecated("ikojo ile aynı yazıma geçildi: satırBaşı kullanın", "Eylül 2026")
    val satır_başı = satırBaşı
    @deprecated("ikojo ile aynı yazıma geçildi: noktalıVirgül kullanın", "Eylül 2026")
    val noktalı_virgül = noktalıVirgül
    @deprecated("ikojo ile aynı yazıma geçildi: backSpace kullanın", "Eylül 2026")
    val back_space = backSpace
    @deprecated("ikojo ile aynı yazıma geçildi: pageUp kullanın", "Eylül 2026")
    val page_up = pageUp
    @deprecated("ikojo ile aynı yazıma geçildi: pageDown kullanın", "Eylül 2026")
    val page_down = pageDown
  }

}
