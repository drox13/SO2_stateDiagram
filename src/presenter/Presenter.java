package presenter;

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
		manager.pollWaitingReady(Manager.createProcess(10));
		manager.pollWaitingReady(Manager.createProcess(10));
		manager.pollWaitingReady(Manager.createProcess(10));
		manager.pollWaitingReady(Manager.createProcess(10));
		mainWindows.setWaitingReady(manager.getQueueWaitingReady());
	}
}