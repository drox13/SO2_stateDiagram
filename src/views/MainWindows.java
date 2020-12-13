package views;

import javax.swing.JFrame;

import models.MyProcess;
import structure.MyQueue;

public class MainWindows extends JFrame{

	private static final long serialVersionUID = 1L;
	private CanvasDiagram canvasDiagram;
	
	public MainWindows() {
		setTitle("Simulacion diagrama de 7 estados");
		setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		canvasDiagram = new CanvasDiagram();
		add(canvasDiagram);
		
		setVisible(true);
	}
	
	public void setWaitingReady(MyQueue<MyProcess> queueWaitingready) {
		canvasDiagram.setWaitingReady(queueWaitingready);
	}
}