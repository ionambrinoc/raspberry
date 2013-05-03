package DisplayStrategy;

import javax.swing.JTable;

import Visualization.StatTableModel;
import Visualization.Statistic;
import Visualization.Visualize;

public class DisplayTableRow implements DisplayStatisticStrategy{
	protected Statistic statistic;
	
	public DisplayTableRow(Statistic statistic){
		this.statistic = statistic;
	}
	
	@Override
	public void display() {
		JTable table = Visualize.display.getTable();
		StatTableModel model = (StatTableModel)table.getModel();
		int rows = model.getRowCount();
		
		int i = 0;
		String symbol = statistic.getSymbol();
		
		while(i<rows && (!((String)model.getValueAt(i,0)).equals(symbol)))
			i++;

		
		if(i>=rows){
			model.addRow(new Object[]{symbol,0,0,0,0,0,0,0});
			model = (StatTableModel)Visualize.display.getTable().getModel();
		}

		model.setValueAt(statistic.getPrice(), i, 1);
		model.setValueAt(statistic.getChange(), i, 2);
		model.setValueAt(statistic.getVolume(), i, 3);
		model.setValueAt(statistic.getOpen(), i, 4);
		model.setValueAt(statistic.getHigh(), i, 5);
		model.setValueAt(statistic.getLow(), i, 6);
	}
}
