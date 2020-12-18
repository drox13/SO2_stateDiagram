package models;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import structure.MyQueue;

public class Manager extends MyThread{
	private static final int MIN_NUMBER = 1;
	private static final int MAX_NUMBER = 4;
	private static final long LIMIT_NUMBER = 1;
	private MyQueue<MyProcess> queueWaitingReady;
	private MyQueue<MyProcess> queueWaitingCPU;
	private MyQueue<MyProcess> queueWaitingIO;
	private CopyOnWriteArrayList<MyProcess> processList;
	private int nextProcess;


	public Manager() {
		super(2000);
		queueWaitingReady = new MyQueue<MyProcess>();
		queueWaitingCPU = new MyQueue<MyProcess>();
		queueWaitingIO = new MyQueue<MyProcess>();
		processList = new CopyOnWriteArrayList<MyProcess>();
	}

	public static MyProcess createProcess(int duration) {
		return new MyProcess(duration);
	}

	public void enqueueWaitingReady(MyProcess process) {
		queueWaitingReady.putToQueue(process);
	}

	public void enqueueWaitingCPU(MyProcess process) {
		queueWaitingCPU.putToQueue(process);
	}

	public void enqueueWaitingIO(MyProcess process) {
		queueWaitingIO.putToQueue(process);
	}

	public void addToList(MyProcess process) {
		processList.add(process);
	}

	public void updateState(MyProcess process) {
		switch (State.valueOf(process.getState().toString())) {
		case CREATE:
			process.setState(State.QUEUE_READY);
			queueWaitingReady.putToQueue(process);
			break;
		case EXECUTING:
			process.setDuration(process.getDuration() - 2);
			if (process.getDuration() != 0) {
				nextProcess = generateRandom();
				assignState(nextProcess, process);
			}else {
				process.setState(State.TERMINATED);
			}
			break;
		case QUEUE_READY:
			process.setState(State.READY);
			queueWaitingReady.getToQueue();
			break;
		case QUEUE_WAITING_CPU:
			process.setState(State.WAITING_CPU);
			queueWaitingCPU.getToQueue();
			break;
		case QUEUE_WAITING_I_O:
			process.setState(State.WAITING_I_O);
			queueWaitingIO.getToQueue();
			break;
		case READY:
			process.setState(State.EXECUTING);
			break;
		case RECEIVING_I_O:
			process.setState(State.QUEUE_READY);
			queueWaitingReady.putToQueue(process);
			break;
		case TERMINATED:
			System.out.println("Proceso " + process.getName() + " terminado");
			processList.remove(process);
			if (getProcessList().size() == 0) {
				stop();
				System.out.println("Simulacion terminada!");
			}
			break;
		case WAITING_CPU:
			process.setState(State.QUEUE_READY);
			queueWaitingReady.putToQueue(process);
			break;
		case WAITING_I_O:
			process.setState(State.RECEIVING_I_O);
			break;
		default:
			break;
		}
	}

	private static int generateRandom() {
		Random random = new Random();
		return random.ints(MIN_NUMBER, MAX_NUMBER).limit(LIMIT_NUMBER).findFirst().getAsInt();
	}

	/**
	 * Cambia el estado del proceso dependiendo del optionNextProcess
	 * @param optionNextProcess es un número aleatorio generado de 1-3. Si sale 1 el proceso cambiara de estado
	 * 			a cola de espera del CPU; Si es 2 el estado será la cola de espera de IO; Si es 3 el estado
	 * 			cambiará a cola del ready.
	 * @param process Proceso al que se le cambiará el estado.
	 */
	private void assignState(int optionNextProcess, MyProcess process) {
		if (optionNextProcess == 1) {
			process.setState(State.QUEUE_WAITING_CPU);
			queueWaitingCPU.putToQueue(process);
		}else if (optionNextProcess == 2) {
			process.setState(State.QUEUE_WAITING_I_O);
			queueWaitingIO.putToQueue(process);
		}else if (optionNextProcess == 3) {
			process.setState(State.QUEUE_READY);
			queueWaitingReady.putToQueue(process);
		}
	}

	public MyQueue<MyProcess> getQueueWaitingReady() {
		return queueWaitingReady;
	}

	public MyQueue<MyProcess> getQueueWaitingCPU() {
		return queueWaitingCPU;
	}

	public MyQueue<MyProcess> getQueueWaitingIO() {
		return queueWaitingIO;
	}

	public CopyOnWriteArrayList<MyProcess> getProcessList() {
		return processList;
	}

	@Override
	void executeTask() {
		for (MyProcess process : processList) {
			updateState(process);
			System.out.println("Name -> " + process.getName() + " State -> " + process.getState().toString()
					+ " Duration -> " + process.getDuration());
			try {
				Thread.sleep(TimeUnit.SECONDS.toMillis(1));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		Manager m = new Manager();
		m.addToList(Manager.createProcess(8));
		m.addToList(Manager.createProcess(6));
		m.addToList(Manager.createProcess(10));
		m.addToList(Manager.createProcess(6));
		m.start();
	}
}