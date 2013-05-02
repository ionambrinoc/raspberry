package ChartFrameFactory;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import com.steema.teechart.Chart;
import com.steema.teechart.Panel;
import com.steema.teechart.TChart;
import com.steema.teechart.editors.ChartEditor;
import com.steema.teechart.styles.Bar;
import com.steema.teechart.styles.Series;

import Visualization.StatThread;
import Visualization.Statistic;
import Visualization.StreamListener;

@SuppressWarnings("serial")
public class ChartFrame extends JInternalFrame implements StreamListener{
	protected ChartFrame(String symbol, StatThread thread) {
		super("Charts for "+symbol, true, true, true);
		thread.addStreamListener(symbol,this);
		
		JPanel jpanel = new JPanel();
		jpanel.setLayout(null);
		TChart priceChart = new TChart();
		
		priceChart.setSize(100, 200);
		Chart chart = new Chart(priceChart);
		Series bar = new Bar(priceChart.getChart());
		priceChart.getAxes().getBottom().setIncrement(1);
		bar.add(400, "pears");
		bar.add(500, "apples");
		chart.addSeries(bar);
		Panel panel = new Panel();
		panel.setVisible(true);
		//jpanel.add(bar);
		//JButton button = new JButton("HI");
		this.add(jpanel);
		//this.add(button);
	}

	@Override
	public void dataUpdate(Statistic s) {
		// TODO Auto-generated method stub
		//add(s.getPrice(),time????);
	}

}
