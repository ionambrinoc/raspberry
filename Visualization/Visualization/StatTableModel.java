package Visualization;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class StatTableModel extends AbstractTableModel{	
	    private String[] columnNames = {"Symbol", "Price", "% change", "Volume", "Open", "High", "Low", "Prev Close"};
	    private ArrayList<Object[]> data = new ArrayList<Object[]>();
	    
	    public int getColumnCount() {
	    	return columnNames.length;
	    }

	    public int getRowCount() {
	    	return data.size();
	    }

	    public String getColumnName(int col) {
	        return columnNames[col];
	    }

	    public Object getValueAt(int row, int col) {
	        return data.get(row)[col];
	    }

	    @SuppressWarnings({ "unchecked", "rawtypes" })
		public Class getColumnClass(int c) {
	        return getValueAt(0, c).getClass();
	    }

	   public void setValueAt(Object value, int row, int col) {
		    data.get(row)[col] = value;
		    fireTableDataChanged();
	    }
	   
		public void addRow(Object[] statistic) {
			data.add(statistic);
			fireTableDataChanged();
		}
		
}
