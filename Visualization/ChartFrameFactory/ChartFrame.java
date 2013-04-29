package ChartFrameFactory;

import javax.swing.JInternalFrame;

@SuppressWarnings("serial")
public class ChartFrame extends JInternalFrame{
	protected ChartFrame(String symbol) {
		super(symbol, true, true, true);
	}
}
