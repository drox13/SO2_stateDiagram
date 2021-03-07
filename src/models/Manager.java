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
	private boolean isReady;

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
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		switch (State.valueOf(process.getState().toString())) {
		case CREATE:
			break;
		case QUEUE_READY:
			putInReady();
			break;
		case EXECUTING:
			executing(fildByState(State.EXECUTING));
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
			break;
		case RECEIVING_I_O:
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
			break;
		case WAITING_I_O:
			process.setState(State.RECEIVING_I_O);
			break;
		default:
			break;
		}
	}



	/**
	 * saca de la cola el proceso correspondiente
	 * lo coloca en estado READY
	 */
	private void putInReady() {
		if(!isReady) {
			queueWaitingReady.getToQueue().setState(State.READY);
			isReady = true;
		}
	}
	/**
	 * pone en la cola de ready el proceso
	 * @param process proceso a encolar
	 */
	private void changeStateCreateToQReady() {
		for (MyProcess myProcess : processList) {
			myProcess.setState(State.QUEUE_READY);
			queueWaitingReady.putToQueue(myProcess);
			try {
				Thread.sleep(TimeUnit.SECONDS.toMillis(1));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * realiza la ejecucion del proceso 
	 * decide por medio de un ramdom que camino toma
	 * cola ready - cola waiting i/o - cola del Waiting CPU
	 * @param process
	 */
	private void executing(MyProcess process) {
		if(process != null) {
			process.setDuration(process.getDuration() - 2);
			if (process.getDuration() != 0) {
				nextProcess = generateRandom();
				assignState(nextProcess, process);
			}else {
				process.setState(State.TERMINATED);
			}
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



	/**
	 * Permite que los procesos cambien de estado Create a la cola de ready como primer paso
	 */
	public void firstExecute () {
		printStatusProcess(); //simulcion

		try {
			Thread.sleep(TimeUnit.SECONDS.toMillis(1));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		changeStateCreateToQReady();
		printStatusProcess();
		//		putInReady();
		//		printStatusProcess();
		start();
	}

	/**
	 * busca un proceso por su estado
	 * @param state estado del proceso
	 * @return el estado encontrado o null segun sea el caso
	 */
	public MyProcess fildByState(State state) {
		for (MyProcess myProcess : processList) {
			if(myProcess.getState().equals(state)) {
				return myProcess;
			}
		}
		return null;
	}

	/**
	 * imprime la lista de estados
	 */
	private void printStatusProcess() {
		for (MyProcess process : processList) {
			System.out.println("Name -> " + process.getName() + " State -> " + process.getState().toString()
					+ " Duration -> " + process.getDuration());
		}
		System.out.println();
	}

	@Override
	void executeTask() {
		System.out.println("------------");
		executing(fildByState(State.EXECUTING));
		changeStateReadytoExecuting();
		putInReady();
		printStatusProcess();
	}
	/**
	 * cambia el estado del proceso que este en REaADY a EXECUTING
	 */
	private void changeStateReadytoExecuting() {
		if(fildByState(State.READY)!= null) {
			fildByState(State.READY).setState(State.EXECUTING);
			isReady = false;
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

	public static void main(String[] args) {
		Manager m = new Manager();

		m.addToList(Manager.createProcess(8));
		m.addToList(Manager.createProcess(6));
		m.addToList(Manager.createProcess(10));
		m.addToList(Manager.createProcess(6));
		m.firstExecute();

	}
}