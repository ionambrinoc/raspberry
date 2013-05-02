package ChartFrameFactory;

import java.util.LinkedList;

import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import Visualization.StatThread;

public final class ChartFrameFactory {
	protected final LinkedList<String> currentSymbols = new LinkedList<String>();
	
	public ChartFrame createChartFrame(final String symbol, final StatThread thread) {
		if(!currentSymbols.contains(symbol)){
			ChartFrame chartFrame = new ChartFrame(symbol);
			currentSymbols.add(symbol);
			chartFrame.setSize(600,400);
			chartFrame.setLocation((int)(100*Math.random()),(int)(200+100*Math.random()));
			chartFrame.setVisible(true);
			thread.addStreamListener(symbol,chartFrame);
			
			chartFrame.addInternalFrameListener(new InternalFrameAdapter() {
				@Override
				public void internalFrameClosed(InternalFrameEvent e) {
					currentSymbols.remove(symbol);
					thread.removeStreamListener(symbol);
				}
			});
			return chartFrame;
		}
		return null;
	}
}