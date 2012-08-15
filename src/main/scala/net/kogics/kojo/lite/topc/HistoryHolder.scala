package net.kogics.kojo.lite.topc

import java.awt.BorderLayout
import java.awt.Color
import java.text.DateFormat
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener
import javax.swing.table.AbstractTableModel
import javax.swing.JComponent
import javax.swing.JScrollPane
import javax.swing.JTable
import net.kogics.kojo.core.KojoCtx
import net.kogics.kojo.history.HistoryListener
import net.kogics.kojo.lite.CodeExecutionSupport
import sun.swing.table.DefaultTableCellHeaderRenderer
import javax.swing.SwingConstants
import javax.swing.ListSelectionModel
import javax.swing.DefaultCellEditor
import javax.swing.JCheckBox

class HistoryHolder(val hw: JComponent, ctx: KojoCtx, codeSupport: CodeExecutionSupport) extends BaseHolder("HW", "History Pane", hw) {
  val cmdh = codeSupport.commandHistory
  val colNames = List("\u263c", "Code", "File", "At")
  val colWidths = List(1, 200, 30, 50)
  val df = DateFormat.getDateTimeInstance
  val tableModel = new AbstractTableModel {
    override def getColumnName(col: Int) = {
      colNames(col)
    }
    override def getRowCount() = {
      cmdh.size
    }
    override def getColumnCount() = {
      colNames.size
    }
    override def getValueAt(row: Int, col: Int) = {
      val hi = cmdh(row)
      col match {
        case 0 => new java.lang.Boolean(hi.starred)
        case 1 => hi.script.replaceAll("\n", " | ")
        case 2 => hi.file
        case 3 => df.format(hi.at)
      }
    }

    override def getColumnClass(c: Int) = getValueAt(0, c).getClass()

    override def isCellEditable(row: Int, col: Int) = {
      col match {
        case 0 => true
        case _ => false
      }
    }
    override def setValueAt(value: Object, row: Int, col: Int) {
      val hi = cmdh(row)
      col match {
        case 0 =>
          if (value.asInstanceOf[java.lang.Boolean]) {
            cmdh.star(hi)
          } else {
            cmdh.unstar(hi)
          }
        case _ =>
      }
      fireTableCellUpdated(row, col);
    }
  }
  val table = new JTable(tableModel)

  table.setBackground(Color.white)
  table.setShowGrid(true)
  table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
  table.getTableHeader.getDefaultRenderer.asInstanceOf[DefaultTableCellHeaderRenderer].setHorizontalAlignment(SwingConstants.CENTER)
  table.getSelectionModel.addListSelectionListener(new ListSelectionListener {
    override def valueChanged(event: ListSelectionEvent) {
      if (!event.getValueIsAdjusting) {
        codeSupport.loadCodeFromHistory(table.getSelectedRow)
      }
    }
  })

  for (i <- 0 until colNames.size) {
    val column = table.getColumnModel().getColumn(i);
    column.setPreferredWidth(colWidths(i))
    //    if (i == 0) {
    //      column.setCellEditor(new DefaultCellEditor(new JCheckBox))
    //    }
  }

  hw.setLayout(new BorderLayout)
  hw.add(new JScrollPane(table), BorderLayout.CENTER)

  codeSupport.commandHistory.setListener(new HistoryListener {
    def itemAdded {
      tableModel.fireTableRowsInserted(cmdh.size - 1, cmdh.size - 1)
      //      table.setRowSelectionInterval(cmdh.size, cmdh.size)
    }

    def selectionChanged(n: Int) {
      table.setRowSelectionInterval(n, n)
    }

    def ensureVisible(n: Int) {
      //      table
      //      myList.ensureIndexIsVisible(n)
    }
  })
}
