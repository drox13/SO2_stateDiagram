package models;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import structure.MyQueue;

public class Manager {
	private MyQueue<MyProcess> queueWaitingReady;
	private MyQueue<MyProcess> queueWaitingCPU;
	private MyQueue<MyProcess> queueWaitingIO;
	private CopyOnWriteArrayList<Process> processList;

	
	public Manager() {
		queueWaitingReady = new MyQueue<MyProcess>();
		queueWaitingCPU = new MyQueue<MyProcess>();
		queueWaitingIO = new MyQueue<MyProcess>();
		processList = new CopyOnWriteArrayList<Process>();
	}
	
	public static MyProcess createProcess(int duration) {
		return new MyProcess(duration);
	}
	
	public void pollWaitingReady(MyProcess process) {
		queueWaitingReady.putToQueue(process);
	}
	
	public MyQueue<MyProcess> getQueueWaitingReady() {
		return queueWaitingReady;
	}
	
	public void updateState(MyProcess process) {
		switch (State.valueOf(process.getState().toString())) {
		case CREATE:
			if(!queueWaitingReady.isEmtry()) {
				process.setState(State.QUEUE_READY);
				queueWaitingReady.putToQueue(process);
			}else {
				process.setState(State.READY);
			}
			break;
		case EXECUTING:
			
			break;
		case QUEUE_READY:
			break;
		case QUEUE_WAITING_CPU:
			break;
		case QUEUE_WAITING_I_O:
			break;
		case READY:
			break;
		case RECEIVING_I_O:
			break;
		case TERMINATED:
			break;
		case WAITING_CPU:
			break;
		case WAITING_I_O:
			break;
		default:
			break;

		}
	}
}