package net.kogics.kojo
package lite

import java.awt.Color
import java.awt.Cursor
import java.awt.Font
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JWindow

import net.kogics.kojo.util.Utils

class SplashScreen extends JWindow {

  val container = getContentPane();
  container.setLayout(null);

  val panel = new JPanel();
  panel.setBorder(new javax.swing.border.EtchedBorder());
  panel.setBackground(new Color(255, 255, 255));
  panel.setBounds(0, 0, 430, 280);
  panel.setLayout(null);
  container.add(panel);

  val kojoIcon = new JLabel();
  kojoIcon.setIcon(Utils.loadIcon("/images/splash.png"))
  kojoIcon.setBounds(0, 0, 430, 280);

  val msg = new JLabel(Utils.loadString("S_Loading"))
  msg.setFont(new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC, 14))
  msg.setBounds(60, 150, 250, 20);

//  val versionString = s"v${Versions.KojoVersion} ${Versions.KojoRevision} "
//  val msg2 = new JLabel(versionString)
//  msg2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15))
//  msg2.setBounds(60, 165, 250, 20);

  panel.add(msg);
//  panel.add(msg2);
  panel.add(kojoIcon);
  setSize(430, 280);
  setLocationRelativeTo(null);
  setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
  setVisible(true);

  def close(): Unit = {
    Utils.schedule(0.3) {
      setVisible(false);
    }
  }
}
