package views;

import java.awt.BorderLayout;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import models.MyProcess;
import structure.MyQueue;
/**
 * 
 * @author Dario Baron, Brayan Cardernas
 *
 */
public class MainWindows extends JFrame{

	private static final String MESSAGE_SUGGESTION = "Se aconseja ingresar un numerO >10 para lograr ver las colas WAITING CPU y WAITING I/O";
	private static final String MESSAGE_ERROR = "Ingrese un numero";
	private static final String MESSAGE_NUMBER_OF_PROCESS = "Ingrese el numero de procesos";
	private static final String PATH_ICON_SIMULATION = "/img/automation.png";
	private static final String TITLE_APP = "Simulacion diagrama de 7 estados";
	private static final long serialVersionUID = 1L;
	private CanvasDiagram canvasDiagram;
	
	public MainWindows() {
		setTitle(TITLE_APP);
		setIconImage(new ImageIcon(getClass().getResource(PATH_ICON_SIMULATION)).getImage());
		setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		canvasDiagram = new CanvasDiagram();
		add(canvasDiagram, BorderLayout.CENTER);
		setVisible(true);
	}
	
	public void setWaitingReady(MyQueue<MyProcess> queueWaitingReady) {
		canvasDiagram.setWaitingReady(queueWaitingReady);
	}
	
	public void setWaitingCPU(MyQueue<MyProcess> queueWaitingCPU) {
		canvasDiagram.setWaitingCPU(queueWaitingCPU);
	}
	
	public void setWaitingIO(MyQueue<MyProcess> queueWaitingIO) {
		canvasDiagram.setWaitingIO(queueWaitingIO);
	}
	
	public void updateProcessListGUI(CopyOnWriteArrayList<MyProcess> processList) {
		canvasDiagram.updateProcessListGUI(processList);
	}

	public int getNumberProcess() {
		int numberProces = 0;
		try {
			JOptionPane.showMessageDialog(null, MESSAGE_SUGGESTION);
			 numberProces = Integer.parseInt(JOptionPane.showInputDialog(MESSAGE_NUMBER_OF_PROCESS));
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, MESSAGE_ERROR);
			
		}
		return numberProces;
	}
}