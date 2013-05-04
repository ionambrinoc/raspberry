package Visualization;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.List;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import DisplayStrategy.DisplayTableRow;
import MapCreator.MapCreator;


@SuppressWarnings("serial")
public class Display extends JDesktopPane implements TableModelListener{
	protected final JTable table;
	ListSelectionModel tableSelectionModel;
	StatThread statThread;
	protected boolean itIsTime = false;
	
	public Display(){
		super();
		statThread = new StatThread();

		table = new JTable(new StatTableModel());
		table.getModel().addTableModelListener(this);
		(table.getSelectionModel()).setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableSelectionModel = table.getSelectionModel();		
		tableSelectionModel.addListSelectionListener(new TableSelectionHandler(this));
        table.setSelectionModel(tableSelectionModel);
		table.setAutoCreateRowSorter(false);
		table.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		table.setBackground(Color.black);
		table.setForeground(Color.white);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		
		JInternalFrame tableFrame = new JInternalFrame("Table",true);
		tableFrame.setSize(600,700);
        tableFrame.setVisible(true);
		tableFrame.add(scrollPane);
        this.add(tableFrame);
        try {
            tableFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}
	}
	
	public StatThread getStatThread(){
		return statThread;
	}
	
	public void start() throws InterruptedException {
		int tableUpdateFreq = 100;
		int chartUpdateFreq = 300;
		long nextTableUpdate = System.currentTimeMillis()+tableUpdateFreq;
		long nextChartUpdate = System.currentTimeMillis()+chartUpdateFreq;
		while(true){
				boolean tableUpdate = nextTableUpdate<System.currentTimeMillis();
				boolean chartUpdate = nextChartUpdate<System.currentTimeMillis();
				if(tableUpdate){
					nextTableUpdate = System.currentTimeMillis()+tableUpdateFreq;
					update();
				}
				if(chartUpdate) {
					nextChartUpdate = System.currentTimeMillis()+chartUpdateFreq;
				}
				statThread.updateList(chartUpdate);
		}
	}

	public void update(){
		/*Test:
		 * byte[] bs = {
				0,0,0,(byte)0,
				0,0,0, (byte)(100*(Math.random())),
				0,(byte)(100*(Math.random())),0,0,
				(byte)(100*(Math.random())),0,0,0,
				0,0,0,(byte)(100*(Math.random())),
				0,0,0,(byte)(100*(Math.random())),
				0,0,0,(byte)(100*(Math.random())),
				0,0,0,(byte)(100*(Math.random())),
				0,0,0,(byte)(100*(Math.random())),
				0,0,0,(byte)(100*(Math.random())),
				0,0,0,(byte)(100*(Math.random())),
				0,0,0,(byte)(100*(Math.random())),
				0,0,0,(byte)(100*(Math.random())),
				0,0,0,(byte)(100*(Math.random()))};
		Statistic s = new Statistic(bs);
		s.getDisplayStrategy().display();*/
		List<Statistic> list = statThread.peekStatistic();
		for (Statistic stat : list){
			new DisplayTableRow(stat).display();
		}
	}
	
	public JTable getTable() {
		return table;
	}
	
	@Override
	public void tableChanged(TableModelEvent e) {
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		Object oldAntialiasing = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
		Color oldColor = g.getColor();
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());		
		g.setColor(oldColor);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntialiasing);
	}

}