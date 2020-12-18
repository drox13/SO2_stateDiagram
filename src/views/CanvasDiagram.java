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

	private static final String RED_LIGHT = "#FF5733";
	private static final String GREEN = "#3ADF00";
	private static final String GREEN_LIGHT = "#00FFBF";
	private static final String YELLOW = "#ffdd00";
	private static final String PURPLE = "#371777";
	private static final String DARK = "#222";
	private static final long serialVersionUID = 1L;
	private static final String BLUE = "#00aeff";
	private static final Font font = new Font("Arial", Font.BOLD, 20);
	private MyRectangle create;
	private MyRectangle ready;
	private MyRectangle executing;
	private MyRectangle waitingCpu;
	private MyRectangle terminated;
	private MyRectangle waitingIO;
	private MyRectangle receivingIO;
	private MyQueue<MyProcess> waitingReadyGUI;
	private MyQueue<MyProcess> waitingCPU_GUI;
	private MyQueue<MyProcess> waitingIO_GUI;
	
	public CanvasDiagram() {
		setBackground(Color.WHITE);
		create = new MyRectangle((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() -1900,
				(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2 - 100, GREEN_LIGHT);
		ready = new MyRectangle((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/4,
				(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()/5, GREEN);
		executing = new MyRectangle((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2,
				(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2 - 100, RED_LIGHT);
		waitingCpu = new MyRectangle((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/3 + 200,
				(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()/10, YELLOW);
		waitingIO = new MyRectangle((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/3 + 200,
				(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 300, PURPLE);
		receivingIO = new MyRectangle((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/9,
				(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 300, BLUE);
		terminated = new MyRectangle((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2 + 350,
				(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 500, DARK);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(font);
		paintState(g2, create, "CREATE");
		paintState(g2, ready, "READY");
		paintState(g2, executing, "EXECUTING");
		paintState(g2, waitingCpu, "WAITING CPU");
		paintState(g2, waitingIO, "WAITING I/O");
		paintState(g2, receivingIO, "RECEIVED I/O");
		paintState(g2, terminated, "TERMINATED");
		paintQueueReady(g2);
		paintQueueCPU(g2);
		paintQueueIO(g2);
	}

	private void paintState(Graphics2D g2, MyRectangle rectangle, String state) {
		g2.setColor(rectangle.getColorRectangle());
		g2.draw(rectangle);
		g2.setColor(Color.WHITE);
		g2.drawString(state, (int)(rectangle.getX() + rectangle.getWidth()/3),
				(int) (rectangle.getY() + rectangle.getHeight()/2));
	}
	
	private void paintQueueReady(Graphics2D g2) {
		int posY = 10;
		Iterator<MyProcess> iterator = waitingReadyGUI.iterator();
		while (iterator.hasNext()) {
			MyRectangle processInQueue = new MyRectangle(ready.x - 200, posY);
			g2.setColor(processInQueue.getColorRectangle());
			g2.draw(processInQueue);
			posY += 50;
			iterator.next();
		}
	}
	
	private void paintQueueCPU(Graphics2D g2) {
		int posY = 10;
		Iterator<MyProcess> iterator = waitingCPU_GUI.iterator();
		while (iterator.hasNext()) {
			MyRectangle processInQueue = new MyRectangle(waitingCpu.x + 220, posY);
			g2.setColor(processInQueue.getColorRectangle());
			g2.draw(processInQueue);
			posY += 50;
			iterator.next();
		}
	}
	
	private void paintQueueIO(Graphics2D g2) {
		int posY = waitingIO.y;
		Iterator<MyProcess> iterator = waitingIO_GUI.iterator();
		while (iterator.hasNext()) {
			MyRectangle processInQueue = new MyRectangle(waitingIO.x + 220, posY);
			g2.setColor(processInQueue.getColorRectangle());
			g2.draw(processInQueue);
			posY += 50;
			iterator.next();
		}
	}
	
	public void setWaitingReady(MyQueue<MyProcess> queueWaitingReady) {
		waitingReadyGUI = queueWaitingReady;
		repaint();
	}
	
	public void setWaitingCPU(MyQueue<MyProcess> queueWaitingCPU) {
		waitingCPU_GUI = queueWaitingCPU;
		repaint();
	}
	
	public void setWaitingIO(MyQueue<MyProcess> queueWaitingIO) {
		waitingIO_GUI = queueWaitingIO;
		repaint();
	}
}