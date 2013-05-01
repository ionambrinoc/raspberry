package Visualization;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
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


@SuppressWarnings("serial")
public class Display extends JDesktopPane implements TableModelListener{
	protected final Timer timer;
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
		table.setAutoCreateRowSorter(true);
		table.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		table.setBackground(Color.black);
		table.setForeground(Color.white);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		
		JInternalFrame tableFrame = new JInternalFrame("Table",true);
		tableFrame.setSize(650,200);
        tableFrame.setVisible(true);
		tableFrame.add(scrollPane);
        this.add(tableFrame);
        try {
            tableFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}

		timer = new Timer(1000, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				itIsTime = true;
			}
		});
	}
	
	public StatThread getStatThread(){
		return statThread;
	}
	
	public void start() throws InterruptedException {
		timer.start();
		while(true){
			if(itIsTime){
				update();
				itIsTime=false;
			}
			else {
				statThread.updateList();
			}
		}
	}

	public void update(){
		/*byte[] bs = {
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
		/*for (GameElement gameElement : cave.getGameElements()){
			getGameElementPainter(gameElement).paint(g2d,gameElement);
			if(applicationState.getToolSelected() == ToolSelected.EDIT && applicationState.isSelected(gameElement)){
				getGameElementPainter(gameElement).paintSelection(g2d, gameElement);
			}
		}*/
		
		g.setColor(oldColor);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntialiasing);
	}

}