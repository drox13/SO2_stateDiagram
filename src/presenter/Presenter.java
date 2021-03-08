package presenter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import models.Manager;
import views.MainWindows;
/**
 * 
 * @author Brayan Cardenas
 *
 */
public class Presenter {
	private Manager manager;
	private MainWindows mainWindows;
	
	public Presenter() {
		manager = new Manager();
		mainWindows = new MainWindows();
		init(mainWindows.getNumberProcess());
	}
	
	public void init(int numberProcess) {
		for (int i = 0; i < numberProcess; i++) {
			manager.addToList(Manager.createProcess(manager.generateRandomNumberEven()));
		}
		
		manager.firstExecute();
		
		Timer loop = new Timer(300, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindows.setWaitingReady(manager.getQueueWaitingReady());
				mainWindows.setWaitingCPU(manager.getQueueWaitingCPU());
				mainWindows.setWaitingIO(manager.getQueueWaitingIO());
				mainWindows.updateProcessListGUI(manager.getProcessList());
			}
		});
		loop.start();
	}
}