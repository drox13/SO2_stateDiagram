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
		initi();
	}
	
	public void initi() {
		manager.addToList(Manager.createProcess(8));
		manager.addToList(Manager.createProcess(6));
		manager.addToList(Manager.createProcess(10));
		manager.addToList(Manager.createProcess(6));
		Timer loop = new Timer(1500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindows.setWaitingReady(manager.getQueueWaitingReady());
				mainWindows.setWaitingCPU(manager.getQueueWaitingCPU());
				mainWindows.setWaitingIO(manager.getQueueWaitingIO());
			}
		});
		loop.start();
		manager.start();
		
//		manager.enqueueWaitingReady(Manager.createProcess(10));
//		manager.enqueueWaitingReady(Manager.createProcess(10));
//		manager.enqueueWaitingReady(Manager.createProcess(10));
//		manager.enqueueWaitingReady(Manager.createProcess(10));
//		manager.enqueueWaitingReady(Manager.createProcess(10));
//		manager.enqueueWaitingReady(Manager.createProcess(10));
//		manager.enqueueWaitingReady(Manager.createProcess(10));
//		manager.enqueueWaitingCPU(Manager.createProcess(9));
//		manager.enqueueWaitingCPU(Manager.createProcess(9));
//		manager.enqueueWaitingCPU(Manager.createProcess(9));
//		manager.enqueueWaitingCPU(Manager.createProcess(9));
//		manager.enqueueWaitingIO(Manager.createProcess(8));
//		manager.enqueueWaitingIO(Manager.createProcess(8));
//		manager.enqueueWaitingIO(Manager.createProcess(8));
//		manager.enqueueWaitingIO(Manager.createProcess(8));
//		mainWindows.setWaitingReady(manager.getQueueWaitingReady());
//		mainWindows.setWaitingCPU(manager.getQueueWaitingCPU());
//		mainWindows.setWaitingIO(manager.getQueueWaitingIO());
	}
}