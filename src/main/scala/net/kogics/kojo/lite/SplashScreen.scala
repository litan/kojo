package net.kogics.kojo
package lite

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.Color
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JProgressBar
import javax.swing.JWindow
import javax.swing.Timer
import util.Utils
import java.awt.Cursor

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
  msg.setBounds(55, 180, 250, 15);
  panel.add(msg);
  panel.add(kojoIcon);
  setSize(430, 280);
  setLocationRelativeTo(null);
  setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
  setVisible(true);

  def close() {
    Utils.schedule(0.3) {
      setVisible(false);
    }
  }
}
