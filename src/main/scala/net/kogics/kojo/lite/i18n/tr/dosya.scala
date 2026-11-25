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

trait DosyaÇevirisi {
  type Dosya = java.io.File
  implicit class DosyaYöntemleri(dir: Dosya) {
    def dosyalar = dir.listFiles
    def adı: Yazı = dir.getName
    def mutlakYol = dir.getAbsolutePath
    def varMı = dir.exists
    def dizinMi = dir.isDirectory
    def dosyaMı = dir.isFile
    def dizinYap() = dir.mkdir()
    def sil() = dir.delete()
    // todo more..
    // see: https://www.geeksforgeeks.org/file-class-in-java/
  }

  object Dosya {
    import java.io.File
    def apply(yol: Yazı): Dosya = new Dosya(yol)
    def geçiciDosyaOluştur(önEk: Yazı, sonEk: Yazı = ".tmp") = File.createTempFile(önEk, sonEk)
    def kökDizinler = File.listRoots
    def ayırıcı = File.separator
    def ayırıcıHarf = File.separatorChar
    def yolAyırıcı = File.pathSeparator
    def yolAyırıcıHarf = File.pathSeparatorChar
  }

}
