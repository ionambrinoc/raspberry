package Visualization;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;


@SuppressWarnings("serial")
public class Visualize extends JFrame {
	protected final Display display;
	
	public Visualize(){
		super("Visualisation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Toolkit toolkit = Toolkit.getDefaultToolkit(); // Working out and setting required size of panel
		Dimension dim = toolkit.getScreenSize();
		int height = (int)(dim.getHeight()*0.75);
		int width = (int)(dim.getWidth()*0.75);
		//panel = new JPanel(new GridLayout(10,6));
		display = new Display();
		display.setPreferredSize(new Dimension(width,height));
		
		setExtendedState(MAXIMIZED_BOTH);
		setContentPane(display);
		pack();
		// http://www.coderanch.com/t/334083/GUI/java/start-JFrame-maximized-mode has useful ideas on setting panel size
	}
	
	public void start() throws InterruptedException{
		display.start();
	}
	
	public static void main(String[] args) throws InterruptedException {
		Visualize visualise = new Visualize();
		visualise.setLocation(40,30);
		visualise.setVisible(true);
		visualise.start();
	}
}