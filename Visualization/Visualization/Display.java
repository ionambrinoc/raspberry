package Visualization;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
import javax.swing.table.TableModel;


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
		tableSelectionModel = table.getSelectionModel();
		tableSelectionModel.addListSelectionListener(new TableSelectionHandler());
        table.setSelectionModel(tableSelectionModel);
		table.setAutoCreateRowSorter(true);
		table.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		table.setBackground(Color.black);
		table.setForeground(Color.white);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		
		JInternalFrame tableFrame = new JInternalFrame("Table");
        tableFrame.setVisible(true);
		//tableFrame.add(scrollPane);
     //   JInternalFrame.setPreferredSize(new Dimension(30,30));
      //  pack();
        add(tableFrame);
        try {
            tableFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}

		/*TRPanel = new JPanel();
		TRPanel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		TRPanel.setForeground(new Color(0,225,0));
		TRPanel.setOpaque(true);
		TRPanel.setBackground(new Color(0,225,0));
		add(TRPanel);

		BLPanel = new JPanel();
		BLPanel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		BLPanel.setForeground(new Color(255,0,225));
		BLPanel.setOpaque(true);
		BLPanel.setBackground(new Color(255,0,225));
		add(BLPanel);

		BRPanel = new JPanel();
		BRPanel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		BRPanel.setForeground(new Color(255,255,0));
		BRPanel.setOpaque(true);
		BRPanel.setBackground(new Color(255,225,0));
		add(BRPanel);*/

		timer = new Timer(1000, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				itIsTime = true;
			}
		});
	}
	
	public void start() throws InterruptedException {
		timer.start();
		while(true){
			if(itIsTime){
				System.out.println(itIsTime);
				update();
				itIsTime=false;
			}
			else {
				statThread.sleep(1);
				statThread.updateList();
			}
		}
	}
	
	public void update(){
		((statThread.getStatistic()).getDisplayStrategy()).display();
	}
	
	@Override
	public void tableChanged(TableModelEvent e) {
		int row = e.getFirstRow();
        int column = e.getColumn();
        TableModel model = (TableModel)e.getSource();
        String columnName = model.getColumnName(column);
        Object data = model.getValueAt(row, column);
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