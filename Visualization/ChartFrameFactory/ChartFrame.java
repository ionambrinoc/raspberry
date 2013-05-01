package ChartFrameFactory;

import java.util.concurrent.LinkedBlockingDeque;

import javax.swing.JInternalFrame;

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
	
	XYSeries price;
	private int[][] priceData;
	
	protected ChartFrame(String symbol, StatThread thread) {
		super("Charts for symbol "+symbol, true, true, true);
		
		//final XYDataset dataset = createDataset();
        //final JFreeChart chart = createChart(dataset);
		priceData = new int[2][1000];
		XYSeries price = new XYSeries("Price for symbol "+symbol);
		price.add(5,1);
		price.add(1,2);
		price.add(7,3);
		price.add(4,4);
		XYSeriesCollection priceDataSet = new XYSeriesCollection();
		priceDataSet.addSeries(price);
		
		JFreeChart chart = ChartFactory.createXYLineChart("Price, VWAP, SMM",	"Time", "Price", priceDataSet, PlotOrientation.VERTICAL, true, false, false);
		ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(600, 338));
        setContentPane(chartPanel);
        
		thread.addStreamListener(symbol,this);
	}

	@Override
	public void dataUpdate(LinkedBlockingDeque<Statistic> q) {
		for (Statistic s : q) {
		//	price.add(s.getTime(),s.getPrice());
			System.out.println("("+s.getTime()+","+s.getPrice()+")");
		}
	       // populateData();
/*
	        // create a fast scatter chart...
	        final Plot plot = new FastScatterPlot(this.data, new NumberAxis("X"), new NumberAxis("Y"));
	        final JFreeChart chart = new JFreeChart(
	            "Fast Scatter Plot Timing",
	            JFreeChart.DEFAULT_TITLE_FONT,
	            plot, 
	            true
	        );

	        final BufferedImage image = new BufferedImage(400, 300, BufferedImage.TYPE_INT_RGB);
	        final Graphics2D g2 = image.createGraphics();
	        final Rectangle2D chartArea = new Rectangle2D.Double(0, 0, 400, 300);

	        // set up the timer...
	        final Timer timer = new Timer(10000, this);
	        timer.setRepeats(false);
	        int count = 0;
	        timer.start();
	        while (!this.finished) {
	            chart.draw(g2, chartArea, null, null);
	            System.out.println("Charts drawn..." + count);
	            if (!this.finished) {
	                count++;
	            }
	        }
	        System.out.println("DONE");

	    }

	    /**
	     * Receives notification of action events (in this case, from the Timer).
	     *
	     * @param event  the event.
	     
	    public void actionPerformed(final ActionEvent event) {
	        this.finished = true;
	    }

	    /**
	     * Populates the data array with random values.
	    
	    private void populateData() {

	        for (Statistic s : q) {
			price.add(s.getTime(),s.getPrice());
			System.out.println("("+s.getTime()+","+s.getPrice()+")");
		}
	        for (int i = 0; i < this.data[0].length; i++) {

	            final float x = i;
	            this.data[0][i] = x;
	            this.data[1][i] = 100 + (2 * x) + (float) Math.random() * 1440;
	        }

	    }*/
		
	}

}
