/*
 * Copyright (C) 2022
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

trait PartialFunctionMethodsInTurkish {
  type Bölümselİşlev[D,R] = PartialFunction[D,R]
  // todo: implicit class doesn't work when f's type uses the alias above
  implicit class PartialFunctionMethodsInTurkish[D,R](f: PartialFunction[D,R]) {
    def tanımlıMı(d: D) = f.isDefinedAt(d)
  }
}
