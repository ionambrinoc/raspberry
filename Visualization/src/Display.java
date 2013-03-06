import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;


@SuppressWarnings("serial")
public class Display extends JPanel{
	protected final Timer timer;
	protected final JLabel label;
	int i = 0;
	
	public Display(){
		super(new GridLayout(3,1));
		label = new JLabel(String.valueOf(i)+", ");
		label.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		label.setForeground(new Color(255,131,0));
		add(label);
		timer = new Timer(1000, new ActionListener(){
			public void actionPerformed(ActionEvent e){
			i++;
			update();
			}
		});
		timer.start();
	}
	
	public void update(){
		label.setText(label.getText()+String.valueOf(i)+", ");
		/*if (time right){
			display statistic
		}*/
	}
	
	public void qTrack(){
		
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