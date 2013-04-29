package Visualization;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class StatTableModel extends AbstractTableModel{	
	    private String[] columnNames = {"Symbol", "Price", "% change", "Volume", "Open", "High", "Low", "Prev Close"};
	    private Object[][] data = {{"MCK",1,1,1,1,1,1,1},{"EIL",1,1,1,1,1,1,1}};

	    public int getColumnCount() {
	        return columnNames.length;
	    }

	    public int getRowCount() {
	        return data.length;
	    }

	    public String getColumnName(int col) {
	        return columnNames[col];
	    }

	    public Object getValueAt(int row, int col) {
	        return data[row][col];
	    }

	    @SuppressWarnings({ "unchecked", "rawtypes" })
		public Class getColumnClass(int c) {
	        return getValueAt(0, c).getClass();
	    }

	   public void setValueAt(Object value, int row, int col) {
	        data[row][col] = value;
	        fireTableCellUpdated(row, col);
	    }
}
