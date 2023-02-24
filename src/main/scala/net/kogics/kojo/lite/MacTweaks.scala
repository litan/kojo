/*
 * Copyright (C) 2018 Lalit Pant <pant.lalit@gmail.com>
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
package net.kogics.kojo.lite

import javax.swing.JFrame

import net.kogics.kojo.util.Utils

class MacTweaks {
  def tweak(frame: JFrame): Unit = {
    import com.apple.eawt.{ AboutHandler, Application }; import com.apple.eawt.AppEvent.AboutEvent;
    import javax.swing.JOptionPane
    val app = Application.getApplication
    app.setDockIconImage(Utils.loadImage("/images/kojo48.png"))
    app.setAboutHandler(new AboutHandler {
      def handleAbout(e: AboutEvent): Unit = {
        JOptionPane.showMessageDialog(
          frame,
          "The Kojo Learning Environment\nSee 'Help -> About' for more information",
          "",
          JOptionPane.PLAIN_MESSAGE
        )
      }
    })
  }
}
