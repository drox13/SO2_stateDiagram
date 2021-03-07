package presenter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import models.Manager;
import views.MainWindows;

public class Presenter {
	private Manager manager;
	private MainWindows mainWindows;
	
	public Presenter() {
		manager = new Manager();
		mainWindows = new MainWindows();
		init();
	}
	
	public void init() {
//		manager.addToList(Manager.createProcess(8));
//		manager.addToList(Manager.createProcess(6));
//		manager.addToList(Manager.createProcess(10));
//		manager.addToList(Manager.createProcess(6));
//		manager.addToList(Manager.createProcess(8));
//		manager.addToList(Manager.createProcess(6));
//		manager.addToList(Manager.createProcess(10));
//		manager.addToList(Manager.createProcess(6));
//		manager.addToList(Manager.createProcess(8));
//		manager.addToList(Manager.createProcess(6));
//		manager.addToList(Manager.createProcess(10));
//		manager.addToList(Manager.createProcess(6));
		for (int i = 0; i < 20; i++) {
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