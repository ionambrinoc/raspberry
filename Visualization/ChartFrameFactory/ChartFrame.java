package ChartFrameFactory;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.LinkedBlockingDeque;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import Visualization.StatThread;
import Visualization.Statistic;
import Visualization.StreamListener;

@SuppressWarnings("serial")
public class ChartFrame extends JInternalFrame implements StreamListener{
	
	XYSeriesCollection priceDataSet;
	XYSeriesCollection candleDataSet;
	JFreeChart chart;
	JFreeChart chart2;
	String symbol;
	
	protected ChartFrame(String symbol, StatThread thread) {
		super("Charts for symbol "+symbol, false, true, false);

		this.symbol = symbol;
		priceDataSet = new XYSeriesCollection();
		candleDataSet = new XYSeriesCollection();
		
		chart = ChartFactory.createXYLineChart("Symbol "+symbol+ "'s Price, VWAP, SMM", "Time", "Price", priceDataSet, PlotOrientation.VERTICAL, true, false, false);
		chart2 = ChartFactory.createXYLineChart("Symbol "+symbol+ "'s SMA, High, Low", "Time", "Price", candleDataSet, PlotOrientation.VERTICAL, true, false, false);
		ChartPanel chartPanel = new ChartPanel(chart);
		ChartPanel chartPanel2 = new ChartPanel(chart2);
        chartPanel.setPreferredSize(new java.awt.Dimension(400, 225));
        chartPanel2.setPreferredSize(new java.awt.Dimension(400, 225));
        JPanel jPanel = new JPanel();
        jPanel.add(chartPanel);
        jPanel.add(chartPanel2);
        setContentPane(jPanel);
        
		thread.addStreamListener(symbol,this);
	}

	@Override
	public void dataUpdate(LinkedBlockingDeque<Statistic> q) {
		priceDataSet.removeAllSeries();
		candleDataSet.removeAllSeries();
		XYSeries price = new XYSeries("Price");
		XYSeries vwap = new XYSeries("VWAP");
		XYSeries smm = new XYSeries("SMM");
		XYSeries sma = new XYSeries("SMA");
		XYSeries high = new XYSeries("High");
		XYSeries low = new XYSeries("Low");

		for (Statistic s : q) {
			price.add(s.getTime(),s.getPrice());
			vwap.add(s.getTime(),s.getVWAP());
			smm.add(s.getTime(),s.getSMM());
			sma.add(s.getTime(),s.getSMA());
			high.add(s.getTime(),s.getHigh());
			low.add(s.getTime(),s.getLow());
		 }

		priceDataSet.addSeries(price);
		priceDataSet.addSeries(vwap);
		priceDataSet.addSeries(smm);
		
		candleDataSet.addSeries(sma);
		candleDataSet.addSeries(high);
		candleDataSet.addSeries(low);

		final BufferedImage image = new BufferedImage(ChartPanel.WIDTH, ChartPanel.HEIGHT, BufferedImage.TYPE_INT_RGB);
	    final Graphics2D g2 = image.createGraphics();
	    final Rectangle2D chartArea = new Rectangle2D.Double(0, 0, ChartPanel.WIDTH, ChartPanel.HEIGHT);
	        
	    chart.draw(g2, chartArea, null, null);
	    chart2.draw(g2, chartArea, null, null);     
	}
}
