package net.kogics.kojo.history

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Cursor
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.text.DateFormat

import javax.swing.BorderFactory
import javax.swing.DefaultCellEditor
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.JTextField
import javax.swing.ListSelectionModel
import javax.swing.SwingConstants
import javax.swing.border.BevelBorder
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener
import javax.swing.table.AbstractTableModel
import javax.swing.table.DefaultTableCellRenderer

import net.kogics.kojo.core.CodeExecutionSupport
import net.kogics.kojo.core.HistoryListener

import sun.swing.table.DefaultTableCellHeaderRenderer

class HistoryPanel(execSupport: CodeExecutionSupport) extends JPanel { hpanel =>
  val cmdh = execSupport.commandHistory
  val colNames = List("\u263c", "Code", "Tags", "File", "At")
  val colWidths = List(1, 200, 40, 30, 40)
  val df = DateFormat.getDateTimeInstance
  val tableModel = new AbstractTableModel {
    override def getColumnName(col: Int) = {
      colNames(col)
    }
    override def getRowCount() = {
      cmdh.size + 1
    }
    override def getColumnCount() = {
      colNames.size
    }
    override def getValueAt(row: Int, col: Int) = {
      if (row == cmdh.size) {
        col match {
          case 0 => new java.lang.Boolean(false)
          case _ => ""
        }
      }
      else {
        val hi = cmdh(row)
        col match {
          case 0 => new java.lang.Boolean(hi.starred)
          case 1 => hi.script.replaceAll("\n", " | ")
          case 2 => hi.tags
          case 3 => hi.file
          case 4 => df.format(hi.at)
        }
      }
    }

    override def getColumnClass(c: Int) = getValueAt(0, c).getClass()

    override def isCellEditable(row: Int, col: Int) = {
      col match {
        case 0 => true
        case 2 => true
        case _ => false
      }
    }
    override def setValueAt(value: Object, row: Int, col: Int) {
      if (row < cmdh.size) {
        val hi = cmdh(row)
        col match {
          case 0 =>
            if (value.asInstanceOf[java.lang.Boolean]) {
              cmdh.star(hi)
            }
            else {
              cmdh.unstar(hi)
            }
          case 2 => cmdh.saveTags(hi, value.asInstanceOf[String])
          case _ =>
        }
        fireTableCellUpdated(row, col);
      }
    }
  }
  val table = new JTable(tableModel)

  table.setBackground(Color.white)
  //  table.setShowGrid(true)
  table.setRowHeight(table.getRowHeight + 4)
  table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
  table.setRowSelectionInterval(cmdh.size, cmdh.size)
  table.getTableHeader.getDefaultRenderer.asInstanceOf[DefaultTableCellHeaderRenderer].setHorizontalAlignment(SwingConstants.CENTER)
  table.setDefaultRenderer(classOf[AnyRef], new DefaultTableCellRenderer {
    override def getTableCellRendererComponent(table: JTable, value: Object, isSelected: Boolean,
                                               hasFocus: Boolean, row: Int, column: Int) = {
      val component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column).asInstanceOf[JComponent]

      val outsideBorder = BorderFactory.createLineBorder(new Color(240, 240, 240))
      val insideBorder = new EmptyBorder(0, 3, 0, 2)
      val border = new CompoundBorder(outsideBorder, insideBorder)

      component.setBorder(border)
      component
    }
  })
  table.getSelectionModel.addListSelectionListener(new ListSelectionListener {
    override def valueChanged(event: ListSelectionEvent) {
      if (!event.getValueIsAdjusting) {
        execSupport.loadCodeFromHistory(table.getSelectedRow)
      }
    }
  })

  for (i <- 0 until colNames.size) {
    val column = table.getColumnModel().getColumn(i)
    column.setPreferredWidth(colWidths(i))
  }
  table.getDefaultEditor(classOf[String]).asInstanceOf[DefaultCellEditor].setClickCountToStart(1)

  setLayout(new BorderLayout)
  add(new JScrollPane(table), BorderLayout.CENTER)
  val searchPane = new JPanel()
  searchPane.setBackground(Color.white)
  searchPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED))

  val searchField = new JTextField(20)
  searchPane.add(searchField)
  val searchBut = new JButton("Search")
  searchPane.add(searchBut)

  var allowSearch = true
  val searcher = new ActionListener {
    def actionPerformed(e: ActionEvent) {
      val waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
      val normalCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
      if (allowSearch) {
        val searchText = searchField.getText
        hpanel.setCursor(waitCursor); searchField.setCursor(waitCursor)
        cmdh.filter(searchText)
        hpanel.setCursor(normalCursor); searchField.setCursor(normalCursor)
        tableModel.fireTableDataChanged()
        table.setRowSelectionInterval(cmdh.size, cmdh.size)
        allowSearch = false
        searchBut.setText("Clear Search")
        // searchBut.requestFocusInWindow()
      }
      else {
        hpanel.setCursor(waitCursor)
        cmdh.loadAll
        hpanel.setCursor(normalCursor)
        tableModel.fireTableDataChanged()
        table.setRowSelectionInterval(cmdh.size, cmdh.size)
        allowSearch = true
        searchBut.setText("Search")
        searchField.setText("")
        searchField.requestFocusInWindow()
      }
    }
  }
  searchBut.addActionListener(searcher)
  searchField.addActionListener(searcher)
  searchField.addKeyListener(new KeyAdapter {
    override def keyTyped(e: KeyEvent) {
      if (!(e.getKeyChar() == KeyEvent.VK_ENTER)) {
        allowSearch = true
        searchBut.setText("Search")
      }
    }
  })

  add(searchPane, BorderLayout.SOUTH)

  cmdh.setListener(new HistoryListener {
    def itemAdded {
      tableModel.fireTableRowsInserted(cmdh.size - 1, cmdh.size - 1)
      table.setRowSelectionInterval(cmdh.size, cmdh.size)
    }

    def selectionChanged(n: Int) {
      table.setRowSelectionInterval(n, n)
    }

    def ensureVisible(n: Int) {
      table.scrollRectToVisible(table.getCellRect(n, 0, true))
    }

    def historyReady() {
      tableModel.fireTableDataChanged()
      table.setRowSelectionInterval(cmdh.size, cmdh.size)
      ensureVisible(cmdh.size)
    }
  })
  table.scrollRectToVisible(table.getCellRect(cmdh.size, 0, true))

}