package ChartFrameFactory;

import java.util.LinkedList;

import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public final class ChartFrameFactory {
	protected final LinkedList<String> currentSymbols = new LinkedList<String>();
	
	public ChartFrame createChartFrame(final String symbol) {
		if(!currentSymbols.contains(symbol)){
			ChartFrame chartFrame = new ChartFrame(symbol);
			currentSymbols.add(symbol);
			chartFrame.setSize(600,400);
			chartFrame.setVisible(true);
			
			chartFrame.addInternalFrameListener(new InternalFrameAdapter() {
				@Override
				public void internalFrameClosed(InternalFrameEvent e) {
					currentSymbols.remove(symbol);
				}
			});
			return chartFrame;
		}
		return null;
	}
}