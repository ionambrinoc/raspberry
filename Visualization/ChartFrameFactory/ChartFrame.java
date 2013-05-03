package ChartFrameFactory;

import javax.swing.JInternalFrame;

import Visualization.StatThread;
import Visualization.Statistic;
import Visualization.StreamListener;

@SuppressWarnings("serial")
public class ChartFrame extends JInternalFrame implements StreamListener{
	
	protected ChartFrame(String symbol, StatThread thread) {
		super("Charts for symbol "+symbol, true, true, true);
		thread.addStreamListener(symbol,this);
		
	}

	@Override
	public void dataUpdate(Statistic s) {
		//priceChart.add((Double)(s.getPrice()),(Double)(s.getTime()+0.0));
		System.out.println("("+s.getTime()+","+s.getPrice()+")");
	}

}
