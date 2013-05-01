package ChartFrameFactory;

import javax.swing.JInternalFrame;

@SuppressWarnings("serial")
public class ChartFrame extends JInternalFrame{
	protected ChartFrame(String symbol) {
		super("Charts for "+symbol, true, true, true);
	}
}
