package models;
/**
 * 
 * @author Dario Baron
 *
 */
public enum State {
	CREATE, READY, EXECUTING, TERMINATED, WAITING_CPU,
	WAITING_I_O, RECEIVING_I_O,
	QUEUE_READY, QUEUE_WAITING_I_O, QUEUE_WAITING_CPU
}