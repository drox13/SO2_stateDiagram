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

	private static final int SIZE_OVAL = 20;
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
		paintArrow(g2);
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
	
	private void paintArrow(Graphics2D g2) {
		g2.drawLine(create.x + MyRectangle.WIDTH_RECTANGLE_STATE, create.y, 
				ready.x, ready.y + MyRectangle.HEIGTH_RECTANGLE_STATE);
		g2.fillOval(ready.x - 13, ready.y + MyRectangle.HEIGTH_RECTANGLE_STATE, SIZE_OVAL, SIZE_OVAL);
		
		g2.drawLine(ready.x + MyRectangle.WIDTH_RECTANGLE_STATE, ready.y + MyRectangle.HEIGTH_RECTANGLE_STATE,
				executing.x, executing.y);
		g2.fillOval(executing.x - 15, executing.y - 15, SIZE_OVAL, SIZE_OVAL);
		g2.fillOval(ready.x + MyRectangle.WIDTH_RECTANGLE_STATE, ready.y + MyRectangle.HEIGTH_RECTANGLE_STATE - 10,
				SIZE_OVAL, SIZE_OVAL);
		
		g2.drawLine(executing.x + MyRectangle.WIDTH_RECTANGLE_STATE, executing.y,
				waitingCpu.x + MyRectangle.WIDTH_RECTANGLE_STATE,
				waitingCpu.y + MyRectangle.HEIGTH_RECTANGLE_STATE);
		g2.fillOval(waitingCpu.x + MyRectangle.WIDTH_RECTANGLE_STATE - 6,
				waitingCpu.y + MyRectangle.HEIGTH_RECTANGLE_STATE, SIZE_OVAL, SIZE_OVAL);
		
		g2.drawLine(executing.x + MyRectangle.WIDTH_RECTANGLE_STATE, executing.y + MyRectangle.HEIGTH_RECTANGLE_STATE,
				terminated.x, terminated.y);
		g2.fillOval(terminated.x - 10, terminated.y - 10, SIZE_OVAL, SIZE_OVAL);
		
		g2.drawLine(executing.x, executing.y + MyRectangle.HEIGTH_RECTANGLE_STATE,
				waitingIO.x + (MyRectangle.WIDTH_RECTANGLE_STATE / 2), waitingIO.y);
		g2.fillOval(waitingIO.x + (MyRectangle.WIDTH_RECTANGLE_STATE / 2) - 10, waitingIO.y - 15, SIZE_OVAL, SIZE_OVAL);
		
		g2.drawLine(waitingIO.x, waitingIO.y + (MyRectangle.HEIGTH_RECTANGLE_STATE / 2),
				receivingIO.x + MyRectangle.WIDTH_RECTANGLE_STATE, waitingIO.y + (MyRectangle.HEIGTH_RECTANGLE_STATE / 2));
		g2.fillOval(receivingIO.x + MyRectangle.WIDTH_RECTANGLE_STATE, waitingIO.y + (MyRectangle.HEIGTH_RECTANGLE_STATE / 2) - 10, SIZE_OVAL, SIZE_OVAL);
		
		g2.drawLine(receivingIO.x, receivingIO.y,
				ready.x, ready.y + MyRectangle.HEIGTH_RECTANGLE_STATE);
	}
	
	private void paintProccess(Graphics2D g2) {
	if(processListGUI != null) {
			for (MyProcess process : processListGUI) {
				switch (State.valueOf(process.getState().toString())) {
				case CREATE:
					g2.setColor(create.getColorRectangle());
					g2.fill(create);
					g2.setColor(Color.WHITE);
					g2.drawString("# procesos: " + processListGUI.size(), (int)(create.getX() + 30),
							(int) (create.getY() + create.getHeight()/2) + 10);
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
		rectangle.setColorRectangle(process.getColor());
		g2.setColor(rectangle.getColorRectangle());
		g2.fill(rectangle);
		g2.setColor(Color.WHITE);
		g2.drawString(process.toString(), (int)(rectangle.getX() + 30),
				(int) (rectangle.getY() + rectangle.getHeight()/2 + 10));
	}
	
	private void paintQueueReady(Graphics2D g2) {
		int posY = 40;
		g2.drawString("Queue Ready", 25, 20);
		if(waitingReadyGUI != null) {
			Iterator<MyProcess> iterator = waitingReadyGUI.iterator();
			while (iterator.hasNext()) {
				MyRectangle processInQueue = new MyRectangle(20, posY);
				g2.setColor(processInQueue.getColorRectangle());
				g2.draw(processInQueue);
				posY += 50;
				g2.drawString(iterator.toString(), 25, posY - 20);
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
				g2.drawString(iterator.toString(), waitingCpu.x + 225, posY - 20);
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
				g2.drawString(iterator.toString(), waitingIO.x + 225, posY - 20);
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