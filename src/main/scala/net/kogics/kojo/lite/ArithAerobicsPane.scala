package net.kogics.kojo.lite

import java.awt.Color
import javax.swing.JFrame
import javax.swing.JTabbedPane
import javax.swing.SwingConstants
import net.kogics.rithica.ui.{ TopContainer => ArithContainer }
import net.kogics.rithica.addition.{ TopContainer => Adder }
import net.kogics.rithica.division.{ TopContainer => Divider }
import net.kogics.rithica.multiplication.{ TopContainer => Multiplier }
import net.kogics.rithica.subtraction.{ TopContainer => Subtractor }
import javax.swing.event.ChangeListener
import javax.swing.event.ChangeEvent
import net.kogics.kojo.util.Utils

class ArithAerobicsPane(frame: JFrame) extends JTabbedPane {

  lazy val adder = new Adder(this)
  lazy val subtractor = new Subtractor(this)
  lazy val multiplier = new Multiplier(this)
  lazy val divider = new Divider(this)

  lazy val init = {
    setFocusCycleRoot(true)
    setTabPlacement(SwingConstants.LEFT)
    setBackground(Color.white)
    addTab(Utils.loadString("S_Addition"), adder)
    addTab(Utils.loadString("S_Subtraction"), subtractor)
    addTab(Utils.loadString("S_Multiplication"), multiplier)
    addTab(Utils.loadString("S_Division"), divider)
    addChangeListener(new ChangeListener {
      def stateChanged(e: ChangeEvent) {
        handleTabChange()
      }
    })
  }

  def handleTabChange() {
    val container = getSelectedComponent().asInstanceOf[ArithContainer]
    Utils.schedule(0.6) {
      container.focusGained()
    }
  }

  def activated() {
    init
    requestFocusInWindow()
    handleTabChange()
  }
}
