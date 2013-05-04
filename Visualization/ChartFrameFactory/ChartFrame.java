package ChartFrameFactory;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.Date;
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
	
	XYSeries price = new XYSeries("Price");
	XYSeries vwap = new XYSeries("VWAP");
	XYSeries smm = new XYSeries("SMM");
	XYSeries sma = new XYSeries("SMA");
	XYSeries high = new XYSeries("High");
	XYSeries low = new XYSeries("Low");
	
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
        
		priceDataSet.addSeries(price);
		priceDataSet.addSeries(vwap);
		priceDataSet.addSeries(smm);
		
		candleDataSet.addSeries(sma);
		candleDataSet.addSeries(high);
		candleDataSet.addSeries(low);
        
		thread.addStreamListener(symbol,this);
	}

	@Override
	public void dataUpdate(Statistic s) {
		int n = 2000;
		Date date = new Date((long)s.getTime());
		double time = date.getHours()+(date.getMinutes()/10);
		time = s.getTime();
		if(price.getItemCount() == n) price.remove(0);
		price.add(time,s.getPrice());
		if(vwap.getItemCount() == n) vwap.remove(0);
		vwap.add(time,s.getVWAP());
		if(smm.getItemCount() == n) smm.remove(0);
		smm.add(time,s.getSMM());
		if(sma.getItemCount() == n) sma.remove(0);
		sma.add(time,s.getSMA());
		if(high.getItemCount() == n) high.remove(0);
		high.add(time,s.getHigh());
		if(low.getItemCount() == n) low.remove(0);
		low.add(time,s.getLow());

		final BufferedImage image = new BufferedImage(ChartPanel.WIDTH, ChartPanel.HEIGHT, BufferedImage.TYPE_INT_RGB);
	    final Graphics2D g2 = image.createGraphics();
	    final Rectangle2D chartArea = new Rectangle2D.Double(0, 0, ChartPanel.WIDTH, ChartPanel.HEIGHT);
	        
	    chart.draw(g2, chartArea, null, null);
	    chart2.draw(g2, chartArea, null, null);     
	}
}
