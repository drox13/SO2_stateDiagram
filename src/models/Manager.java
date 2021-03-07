package models;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import structure.MyQueue;

public class Manager extends MyThread{
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
				System.out.println("line 106 cola Ready vacia");
			}
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
				printStatusProcess();
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
				validateQueueReadyIsEmpty();
			}else {
				process.setState(State.TERMINATED);
			}
		}
	}

	private void validateQueueReadyIsEmpty() {
		if (queueWaitingReady.isEmtry()) {
			MyProcess processInReceivingIOAux = fildByState(State.RECEIVING_I_O);
			MyProcess processInWaitingCPUAux = fildByState(State.QUEUE_WAITING_CPU);
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
			}else if (processInReceivingIOAux.equals(null) && processInWaitingCPUAux.equals(null)) {
				System.out.println("fsdfs");
			}
		}
	}

	private static int generateRandom() {
		Random random = new Random();
		return random.ints(MIN_NUMBER, MAX_NUMBER).limit(LIMIT_NUMBER).findFirst().getAsInt();
	}

	/**
	 * Cambia el estado del proceso dependiendo del optionNextProcess
	 * @param optionNextProcess es un n�mero aleatorio generado de 1-3. Si sale 1 el proceso cambiara de estado
	 * 			a cola de espera del CPU; Si es 2 el estado ser� la cola de espera de IO; Si es 3 el estado
	 * 			cambiar� a cola del ready.
	 * @param process Proceso al que se le cambiar� el estado.
	 */
	private void assignState(int optionNextProcess, MyProcess process) {
		if (optionNextProcess == 1) {
			if (fildByState(State.WAITING_CPU) != null) {
				process.setState(State.QUEUE_WAITING_CPU);
				queueWaitingCPU.putToQueue(process);
			}

			if (queueWaitingCPU.isEmtry()) {
				process.setState(State.WAITING_CPU);
				return;
			}
		}else if (optionNextProcess == 2) {
			if (fildByState(State.WAITING_I_O) != null) {
				process.setState(State.QUEUE_WAITING_I_O);
				queueWaitingIO.putToQueue(process);
			}

			if (queueWaitingIO.isEmtry()) {
				process.setState(State.WAITING_I_O);
				return;
			}
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
		removeProcessTerminated(fildByState(State.TERMINATED));
		changeStateCreateToQReady();

		executing(fildByState(State.EXECUTING));
		changeStateReadytoExecuting();
		putInReady();

		moveProccesToQueueReady(fildByState(State.RECEIVING_I_O));

		if (faceOrSeal()) {
			changeWaitingIOtoReceivingIO(fildByState(State.WAITING_I_O));
			putQueueWaitingIO();
		}

		if (faceOrSeal()) {
			moveProccesToQueueReady(fildByState(State.WAITING_CPU));
			putQueueWaitingCPU();
		}

		printStatusProcess();
		if(processList.size() == 0) {
			stop();
		}
	}

	private void removeProcessTerminated(MyProcess process) {
		if(process != null) {
			processList.remove(process);
		}
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
	 * saca el proceso de la COLA del WAITING CPU y lo para al ESTADO WAITING CPU
	 */
	private void putQueueWaitingCPU() {
		try {
			queueWaitingCPU.getToQueue().setState(State.WAITING_CPU);;
		} catch (Exception e) {
			System.out.println("line 221  cola CPU vacio");
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
	 * Saca de la cola del Waiting IO y lo pasa al ESTADO WAITING IO
	 */
	private void putQueueWaitingIO() {
		try {
			queueWaitingIO.getToQueue().setState(State.WAITING_I_O);;
		} catch (Exception e) {
			System.out.println("line 221  cola I/0 vacio");
		}
	}

	/**
	 * cambia el estado del proceso que este en READY a EXECUTING
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
			System.out.println(number);
			if (number == 0) {
				countFace += 1;
			}else {
				countCross += 1;
			}
		}
		return countFace > countCross;
	}


}