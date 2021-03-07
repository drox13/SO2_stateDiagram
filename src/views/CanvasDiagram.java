package views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;

import models.MyProcess;
import models.State;
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
	private CopyOnWriteArrayList<MyProcess> processListGUI;
	
	public CanvasDiagram() {
		setBackground(Color.WHITE);
		create = new MyRectangle((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/9,
				(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2 - 80, GREEN_LIGHT);
		ready = new MyRectangle((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/4,
				(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()/5, GREEN);
		executing = new MyRectangle((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2,
				(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2 - 100, RED_LIGHT);
		waitingCpu = new MyRectangle((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/3 + 200,
				(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()/10, YELLOW);
		waitingIO = new MyRectangle((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/3 + 200,
				(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 300, PURPLE);
		receivingIO = new MyRectangle((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/9 + 150,
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
		paintProccess(g2);
	}
	

	private void paintState(Graphics2D g2, MyRectangle rectangle, String state) {
		g2.setColor(rectangle.getColorRectangle());
		g2.draw(rectangle);
		g2.setColor(Color.BLACK);
		g2.drawString(state, (int)(rectangle.getX() + rectangle.getWidth()/3),
				(int) (rectangle.getY() + rectangle.getHeight() + 20));
	}
	
	private void paintProccess(Graphics2D g2) {
	if(processListGUI != null) {
			for (MyProcess process : processListGUI) {
				switch (State.valueOf(process.getState().toString())) {
				case CREATE:
					g2.setColor(create.getColorRectangle());
					g2.fill(create);
					g2.setColor(Color.WHITE);
					g2.drawString("# procesos: " + processListGUI.size(), (int)(create.getX() +create.getWidth()/3),
							(int) (create.getY() + create.getHeight()/2));
					break;
				case READY:
					fillRectangle(g2, ready, process);
					break;
				case EXECUTING:
					fillRectangle(g2, executing, process);
					break;
				case QUEUE_READY:
					break;
				case QUEUE_WAITING_CPU:
					break;
				case QUEUE_WAITING_I_O:
					break;
				case RECEIVING_I_O:
					fillRectangle(g2, receivingIO, process);
					break;
				case TERMINATED:
					fillRectangle(g2, terminated, process);
					break;
				case WAITING_CPU:
					fillRectangle(g2, waitingCpu, process);
					break;
				case WAITING_I_O:
					fillRectangle(g2, waitingIO, process);
					break;
				}
			}
		}
	}

	private void fillRectangle(Graphics2D g2, MyRectangle rectangle, MyProcess process) {
		g2.setColor(rectangle.getColorRectangle());
		g2.fill(rectangle);
		g2.setColor(Color.WHITE);
		g2.drawString(process.getName(), (int)(rectangle.getX() +rectangle.getWidth()/3),
				(int) (rectangle.getY() + rectangle.getHeight()/2));
	}
	
	private void paintQueueReady(Graphics2D g2) {
		int posY = 10;
		if(waitingReadyGUI != null) {
			Iterator<MyProcess> iterator = waitingReadyGUI.iterator();
			while (iterator.hasNext()) {
				MyRectangle processInQueue = new MyRectangle(ready.x - 200, posY);
				g2.setColor(processInQueue.getColorRectangle());
				g2.draw(processInQueue);
				posY += 50;
				g2.drawString(iterator.toString(), ready.x - 200, posY - 30);
				iterator.next();
			}
		}
	}
	
	private void paintQueueCPU(Graphics2D g2) {
		int posY = 10;
		if(waitingCPU_GUI != null) {
			Iterator<MyProcess> iterator = waitingCPU_GUI.iterator();
			while (iterator.hasNext()) {
				MyRectangle processInQueue = new MyRectangle(waitingCpu.x + 220, posY);
				g2.setColor(processInQueue.getColorRectangle());
				g2.draw(processInQueue);
				posY += 50;
				iterator.next();
			}
		}
	}
	
	private void paintQueueIO(Graphics2D g2) {
		int posY = waitingIO.y;
		if(waitingIO_GUI != null) {
			Iterator<MyProcess> iterator = waitingIO_GUI.iterator();
			while (iterator.hasNext()) {
				MyRectangle processInQueue = new MyRectangle(waitingIO.x + 220, posY);
				g2.setColor(processInQueue.getColorRectangle());
				g2.draw(processInQueue);
				posY += 50;
				iterator.next();
			}
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
	
	public void updateProcessListGUI(CopyOnWriteArrayList<MyProcess>listProcess) {
		processListGUI = listProcess;
	}
}