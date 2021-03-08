package models;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import structure.MyQueue;
/**
 * 
 * @author Dario Baron, Brayan Cardenas
 *
 */
public class Manager extends MyThread{
	private static final int TIME_INTERVAL_BETWEEN_PROCESSES = 2000;
	private static final int MIN_NUMBER = 1;
	private static final int MAX_NUMBER = 4;
	private static final long LIMIT_NUMBER = 1;

	private static final int MIN_NUMBER_EVEN = 8;
	private static final int MAX_NUMBER_EVEN = 21;
	private static final long LIMIT_NUMBER_EVEN = 1;
	private static final int MIN_NUM = 0;
	private static final int MAX_NUM = 2;
	private static final long LIMIT = 1;
	private MyQueue<MyProcess> queueWaitingReady;
	private MyQueue<MyProcess> queueWaitingCPU;
	private MyQueue<MyProcess> queueWaitingIO;
	private CopyOnWriteArrayList<MyProcess> processList;
	private int nextProcess;
	private boolean isReady;

	public Manager() {
		super(TIME_INTERVAL_BETWEEN_PROCESSES);
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
	
	/**
	 * mueve el proceso hacia la cola del READY
	 * @param process
	 */
	private void moveProccesToQueueReady(MyProcess process) {
		if(process != null) {
			process.setState(State.QUEUE_READY);
			queueWaitingReady.putToQueue(process);
		}
	}

	/**
	 * pone en la cola de ready el proceso
	 * @param process proceso a encolar
	 */
	private void changeStateCreateToQReady() {
		try {
			Thread.sleep(TimeUnit.SECONDS.toMillis(1));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for (MyProcess myProcess : processList) {
			if(myProcess.getState().equals(State.CREATE)){
				myProcess.setState(State.QUEUE_READY);
				queueWaitingReady.putToQueue(myProcess);
			}
		}
	}
	
	/**
	 * mueve el proceso del WAITING I/O hacia RECEIVING I/O
	 * @param process
	 */
	private void changeWaitingIOtoReceivingIO(MyProcess process) {
		if(process != null) {
			process.setState(State.RECEIVING_I_O);
		}
	}
	
	/**
	 * cambia el estado del proceso que este en READY a EXECUTING
	 */
	private void changeStateReadytoExecuting() {
		if(findByState(State.READY)!= null) {
			findByState(State.READY).setState(State.EXECUTING);
			isReady = false;
		}
	}
	
	/**
	 * genera un nuero aleatorio en un intervalo definido
	 * @return
	 */
	private static int generateRandom() {
		Random random = new Random();
		return random.ints(MIN_NUMBER, MAX_NUMBER).limit(LIMIT_NUMBER).findFirst().getAsInt();
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
				validateQueueReadyIsEmpty();
			}else {
				process.setState(State.TERMINATED);
			}
		}
	}
	/**
	 * valida que la cola del READY este vacia y toma acciones dependiendo de esto
	 */
	private void validateQueueReadyIsEmpty() {
		if (queueWaitingReady.isEmpty()) {
			MyProcess processInReceivingIOAux = findByState(State.RECEIVING_I_O);
			MyProcess processInWaitingCPUAux = findByState(State.QUEUE_WAITING_CPU);
			if (processInReceivingIOAux != null && processInWaitingCPUAux.equals(null)) {
				processInReceivingIOAux.setState(State.EXECUTING);
			}else if (processInWaitingCPUAux != null && processInReceivingIOAux.equals(null)) {
				processInWaitingCPUAux.setState(State.EXECUTING);
			}else if (processInReceivingIOAux != null && processInWaitingCPUAux != null) {
				if (faceOrSeal()) {
					processInReceivingIOAux.setState(State.EXECUTING);
				}else {
					processInWaitingCPUAux.setState(State.EXECUTING);
				}
			}
		}
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
			checkWaitingCPUSstatus(process);
		}else if (optionNextProcess == 2) {
			checkWaitingIOstatus(process);
		}else if (optionNextProcess == 3) {
			process.setState(State.QUEUE_READY);
			queueWaitingReady.putToQueue(process);
		}
	}
	/**
	 * verifica si existe un proceso en Waiting IO
	 * si existe lo encola sino lo pone en estado WaitingI/O
	 * @param process proceso a cambiar de estado
	 */
	private void checkWaitingIOstatus(MyProcess process) {
		if (findByState(State.WAITING_I_O) != null) {
			process.setState(State.QUEUE_WAITING_I_O);
			queueWaitingIO.putToQueue(process);
		}

		if (queueWaitingIO.isEmpty()) {
			process.setState(State.WAITING_I_O);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
	}
	/**
	 * verifica si existe un estado en Waiting CPU
	 * si existe lo exncola sino lo pone en estado WaitingCPU
	 * @param process proceso a cambiar de estado
	 */
	private void checkWaitingCPUSstatus(MyProcess process) {
		if (findByState(State.WAITING_CPU) != null) {
			process.setState(State.QUEUE_WAITING_CPU);
			queueWaitingCPU.putToQueue(process);
		}

		if (queueWaitingCPU.isEmpty()) {
			process.setState(State.WAITING_CPU);
			return;
		}
	}

	/**
	 * ejecuta el hilo para cambiar de estados los procesos
	 */
	public void startExecute () {
		start();
	}

	/**
	 * busca un proceso por su estado
	 * @param state estado del proceso
	 * @return el estado encontrado o null segun sea el caso
	 */
	private MyProcess findByState(State state) {
		for (MyProcess myProcess : processList) {
			if(myProcess.getState().equals(state)) {
				return myProcess;
			}
		}
		return null;
	}

	@Override
	void executeTask() {
		removeProcessTerminated(findByState(State.TERMINATED));
		changeStateCreateToQReady();

		executing(findByState(State.EXECUTING));
		changeStateReadytoExecuting();
		putInReady();

		moveProccesToQueueReady(findByState(State.RECEIVING_I_O));

		if (faceOrSeal()) {
			changeWaitingIOtoReceivingIO(findByState(State.WAITING_I_O));
			putQueueWaitingIO();
		}

		if (faceOrSeal()) {
			moveProccesToQueueReady(findByState(State.WAITING_CPU));
			putQueueWaitingCPU();
		}

		if(processList.size() == 0) {
			stop();
		}
	}
	/**
	 * remueve los procesos de la cola que hayan terminado
	 * @param process
	 */
	private void removeProcessTerminated(MyProcess process) {
		if(process != null) {
			processList.remove(process);
		}
	}

	
	/**
	 * saca de la cola el proceso correspondiente
	 * lo coloca en estado READY
	 */
	private void putInReady() {
		if(!isReady) {
			try {
				queueWaitingReady.getToQueue().setState(State.READY);
				isReady = true;
			} catch (Exception e) {
			}
		}
	}
	/**
	 * saca el proceso de la COLA del WAITING CPU y lo para al ESTADO WAITING CPU
	 */
	private void putQueueWaitingCPU() {
		try {
			queueWaitingCPU.getToQueue().setState(State.WAITING_CPU);
		} catch (Exception e) {}
	}
	
	/**
	 * Saca de la cola del Waiting IO y lo pasa al ESTADO WAITING IO
	 */
	private void putQueueWaitingIO() {
		try {
			queueWaitingIO.getToQueue().setState(State.WAITING_I_O);;
		} catch (Exception e) {}
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
	/**
	 * Genera un numero aleatorio par
	 * @return el numero generado
	 */
	public int generateRandomNumberEven() {
		Random random = new Random();
		int number = random.ints(MIN_NUMBER_EVEN, MAX_NUMBER_EVEN).limit(LIMIT_NUMBER_EVEN).findFirst().getAsInt();
		while(number % 2 != 0) {
			number = random.ints(MIN_NUMBER_EVEN, MAX_NUMBER_EVEN).limit(LIMIT_NUMBER_EVEN).findFirst().getAsInt();
		}
		return number;
	}
	/**
	 * genera una opcion random para toma de desiciones
	 * @return true or false --> cara : receivedIO || cruz : waitingCPU
	 */
	public boolean faceOrSeal() {
		Random random = new Random();
		int countFace = 0;
		int countCross = 0;
		int number = 0;
		for (int i = 0; i < 7; i++) {
			number = random.ints(MIN_NUM, MAX_NUM).limit(LIMIT).findFirst().getAsInt();
			if (number == 0) {
				countFace += 1;
			}else {
				countCross += 1;
			}
		}
		return countFace > countCross;
	}
}