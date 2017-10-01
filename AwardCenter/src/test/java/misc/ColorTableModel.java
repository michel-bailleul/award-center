package misc;


import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;


class ColorTableModel extends AbstractTableModel {

  Object rowData[][] = {
      { "1", Boolean.TRUE }, { "2", Boolean.TRUE }, { "3", Boolean.FALSE },
      { "4", Boolean.TRUE }, { "5", Boolean.FALSE }, };

  String columnNames[] = { "English", "Boolean" };

  @Override
  public int getColumnCount() {
    return columnNames.length;
  }

  @Override
  public int getRowCount() {
    return rowData.length;
  }

  @Override
  public String getColumnName(int column) {
    return columnNames[column];
  }

  @Override
  public Object getValueAt(int row, int column) {
    return rowData[row][column];
  }

  @Override
  public Class getColumnClass(int column) {
    return (getValueAt(0, column).getClass());
  }

  @Override
  public void setValueAt(Object value, int row, int column) {
    rowData[row][column] = value;
  }

  @Override
  public boolean isCellEditable(int row, int column) {
    return (column != 0);
  }


  public static void main(String args[]) {
    JFrame frame = new JFrame("Editable Color Table");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    TableModel model = new ColorTableModel();
    JTable table = new JTable(model);
    // TableColumn column = table.getColumnModel().getColumn(3);
    // column.setCellRenderer(renderer);
    // column.setCellEditor(editor);

    JScrollPane scrollPane = new JScrollPane(table);
    frame.add(scrollPane, BorderLayout.CENTER);
    frame.setSize(400, 150);
    frame.setVisible(true);
  }
}