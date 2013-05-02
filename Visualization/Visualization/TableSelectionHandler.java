package Visualization;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ChartFrameFactory.ChartFrame;
import ChartFrameFactory.ChartFrameFactory;

public class TableSelectionHandler implements ListSelectionListener{
	protected Display display;
	protected ChartFrameFactory factory = new ChartFrameFactory();
	
	public TableSelectionHandler(Display display) {
		this.display = display;
	}
	
	public void valueChanged(ListSelectionEvent e) {
		ListSelectionModel lsm = (ListSelectionModel)e.getSource();
		String symbol = null;

		if (!lsm.isSelectionEmpty()){
        // Find out which index is selected.
        int minIndex = lsm.getMinSelectionIndex();
        int maxIndex = lsm.getMaxSelectionIndex();
        for (int i = minIndex; i <= maxIndex; i++) {
            if (lsm.isSelectedIndex(i)) {
                symbol = (String)(display.getTable()).getValueAt(i,0);
            }
        }
		
		ChartFrame chartFrame = factory.createChartFrame(symbol,display.getStatThread());
		
		lsm.clearSelection();
		
		if (chartFrame == null) return;
        display.add(chartFrame);
        try {
        	chartFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {}
		}
	}
}
