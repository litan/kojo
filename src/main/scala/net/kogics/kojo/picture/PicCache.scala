/*
 * Copyright (C) 2017 Lalit Pant <pant.lalit@gmail.com>
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
package net.kogics.kojo.picture

import net.kogics.kojo.core.Picture

object PicCache {
  var hits = 0
  var misses = 0
  val seen = new java.util.concurrent.ConcurrentHashMap[Picture, Int]()
  def size = seen.size()

  def clear(): Unit = {
    seen.clear()
    hits = 0
    misses = 0
  }

  def freshPic(pic: Picture): Picture = {
    if (seen.containsKey(pic)) {
      val ret = pic.copy
      seen.put(ret, 0)
      hits += 1
      ret
    }
    else {
      seen.put(pic, 0)
      misses += 1
      pic
    }
  }

  def freshPics(ps: collection.Seq[Picture]): collection.Seq[Picture] = {
    ps.map(freshPic)
  }
}
