package net.kogics.kojo.history

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.BorderLayout
import java.awt.Color
import java.text.DateFormat
import javax.swing.border.BevelBorder
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener
import javax.swing.table.AbstractTableModel
import javax.swing.table.DefaultTableCellRenderer
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

import net.kogics.kojo.core.CodeExecutionSupport
import net.kogics.kojo.core.HistoryListener
import net.kogics.kojo.lite.Theme
import net.kogics.kojo.util.Utils
import sun.swing.table.DefaultTableCellHeaderRenderer

class HistoryPanel(execSupport: CodeExecutionSupport) extends JPanel { hpanel =>
  val cmdh = execSupport.commandHistory
  val commaSeparatedColNames = Utils.loadString("S_HistoryColumnNames")
  val splitted: List[String] = commaSeparatedColNames.split(',').toList.map(_.trim)
  val colNames = "\u263c" :: splitted
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
    override def getValueAt(row: Int, col: Int): Object = {
      if (row == cmdh.size) {
        col match {
          case 0 => java.lang.Boolean.valueOf(false)
          case _ => ""
        }
      }
      else {
        val hi = cmdh(row)
        col match {
          case 0 => java.lang.Boolean.valueOf(hi.starred)
          case 1 => hi.script.replace("\n", " | ")
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
    override def setValueAt(value: Object, row: Int, col: Int): Unit = {
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

  //  table.setBackground(Color.white)
  table.setShowGrid(false)
  table.setRowHeight(table.getRowHeight + 8)
  table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
  table.setRowSelectionInterval(cmdh.size, cmdh.size)
  table.getTableHeader.getDefaultRenderer
    .asInstanceOf[DefaultTableCellHeaderRenderer]
    .setHorizontalAlignment(SwingConstants.CENTER)
  table.setDefaultRenderer(
    classOf[AnyRef],
    new DefaultTableCellRenderer {
      override def getTableCellRendererComponent(
          table: JTable,
          value: Object,
          isSelected: Boolean,
          hasFocus: Boolean,
          row: Int,
          column: Int
      ) = {
        val component =
          super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column).asInstanceOf[JComponent]

        val outsideBorder = BorderFactory.createLineBorder(new Color(240, 240, 240))
        val insideBorder = new EmptyBorder(0, 3, 0, 2)
        val border = new CompoundBorder(outsideBorder, insideBorder)

        component.setBorder(border)
        component
      }
    }
  )
  table.getSelectionModel.addListSelectionListener(new ListSelectionListener {
    override def valueChanged(event: ListSelectionEvent): Unit = {
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
  searchPane.setBackground(Theme.currentTheme.defaultBg)
  searchPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED))

  val searchField = new JTextField(20)
  searchPane.add(searchField)
  val searchButtonName = Utils.loadString("S_HistorySearch")
  val searchBut = new JButton(searchButtonName)
  searchPane.add(searchBut)

  var allowSearch = true

  def scrollToEnd(): Unit = {
    table.scrollRectToVisible(table.getCellRect(cmdh.size, 0, true))
  }

  def clearSearch(): Unit = {
    if (!allowSearch) {
      execSupport.kojoCtx.showAppWaitCursor()
      cmdh.loadAll()
      execSupport.kojoCtx.hideAppWaitCursor()
      tableModel.fireTableDataChanged()
      table.setRowSelectionInterval(cmdh.size, cmdh.size)
      allowSearch = true
      searchBut.setText(searchButtonName)
      searchField.setText("")
      scrollToEnd()
    }
  }

  val searcher = new ActionListener {
    def actionPerformed(e: ActionEvent): Unit = {
      if (allowSearch) {
        val searchText = searchField.getText
        execSupport.kojoCtx.showAppWaitCursor()
        cmdh.filter(searchText)
        execSupport.kojoCtx.hideAppWaitCursor()
        tableModel.fireTableDataChanged()
        table.setRowSelectionInterval(cmdh.size, cmdh.size)
        allowSearch = false
        searchBut.setText("Clear Search")
        // searchBut.requestFocusInWindow()
      }
      else {
        clearSearch()
        searchField.requestFocusInWindow()
      }
    }
  }
  searchBut.addActionListener(searcher)
  //  searchField.addActionListener(searcher)
  searchField.addKeyListener(new KeyAdapter {
    override def keyTyped(e: KeyEvent): Unit = {
      if (e.getKeyChar == KeyEvent.VK_ENTER && searchField.getText.length > 0) {
        searcher.actionPerformed(null)
      }
      else {
        allowSearch = true
        searchBut.setText(searchButtonName)
      }
    }
  })

  add(searchPane, BorderLayout.SOUTH)

  cmdh.setListener(new HistoryListener {
    def itemAdded: Unit = {
      tableModel.fireTableRowsInserted(cmdh.size - 1, cmdh.size - 1)
      table.setRowSelectionInterval(cmdh.size, cmdh.size)
    }

    def selectionChanged(n: Int): Unit = {
      table.setRowSelectionInterval(n, n)
    }

    def ensureVisible(n: Int): Unit = {
      table.scrollRectToVisible(table.getCellRect(n, 0, true))
    }

    def historyReady(): Unit = {
      tableModel.fireTableDataChanged()
      table.setRowSelectionInterval(cmdh.size, cmdh.size)
      ensureVisible(cmdh.size)
    }
  })
  scrollToEnd()
}
