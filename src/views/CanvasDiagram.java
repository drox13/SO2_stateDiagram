package views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.Iterator;

import javax.swing.JPanel;

import models.MyProcess;
import structure.MyQueue;

public class CanvasDiagram  extends JPanel{

	private static final long serialVersionUID = 1L;
	private static final Font font = new Font("Arial", Font.BOLD, 20);
	private MyRectangle create;
	private MyRectangle ready;
	private MyRectangle executing;
	private MyRectangle waitingCpu;
	private MyRectangle terminated;
	private MyRectangle waitingIO;
	private MyRectangle receivingIO;
	
	private MyQueue<MyProcess> waitingReadyIO;
	
	public CanvasDiagram() {
		create = new MyRectangle(
				(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/3, 200, "#00FFBF");
		ready = new MyRectangle((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/4,
				400, "#3ADF00");
		
		executing = new MyRectangle((int)ready.getX()+ 500, (int)ready.getY(), "#FF5733");
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(font);
		
		paintState(g2, create, "CREATE");

		paintState(g2, ready, "READY");
		
		paintState(g2, executing, "EXECUTING");
		
		if(waitingReadyIO != null) {
			Iterator<MyProcess> iterator = waitingReadyIO.iterator();
			while (iterator.hasNext()) {
				g2.fillRect(50 , 50, 10, 10);
				iterator.next();
			}
		}
	}

	private void paintState(Graphics2D g2, MyRectangle rectangle, String state) {
		g2.setColor(rectangle.getColorRectangle());
		g2.fill(rectangle);
		g2.setColor(Color.WHITE);
		g2.drawString(state, (int)(rectangle.getX() + rectangle.getWidth()/3),
				(int) (rectangle.getY() + rectangle.getHeight()/2));
	}
	
	public void setWaitingReady(MyQueue<MyProcess> queueWaitingready) {
		waitingReadyIO = queueWaitingready;
		repaint();
	}
}