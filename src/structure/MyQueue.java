package structure;

import java.util.Comparator;
import java.util.Iterator;

import exception.MyException;

/**
 * 
 * @author Dario Baron
 *
 * @param <T> tipo de dato de la cola
 */
public class MyQueue <T> implements Iterable<T>{
	private static final String EXCEPTION_MESSAGE = "La cola no tiene mas elementos";
	private MyNode<T> head;
	private Comparator<T> comparator;

	/**
	 * crea una cola normal
	 */
	public MyQueue() {}

	/**
	 * Crea una cola de prioridad
	 * @param comparator determinara la forma en que se le dara prioridad a la cola
	 */
	public MyQueue(Comparator<T> comparator) {
		this.comparator = comparator;
	}

	/**
	 * Verifica si la cola tiene elementos
	 * @return verdadero si la cola esta sin elementos
	 */
	public boolean isEmtry() {
		return head == null;
	}

	/**
	 * Agrega una informacion a la cola
	 * @param info dato que deamos encolar
	 */
	public void putToQueue(T info) {
		if(comparator == null) {
			add(info);
		}else {
			addByOrder(info); 
		}
	}
	/**
	 * permite encolar teniendo en cuenta el orden ( prioridad)
	 * @param info que se va a encolar
	 */
	private void addByOrder(T info) {
		if(head != null){
			if(comparator.compare(info, head.infoNode) >= 0 ) { // informacion es mayor que la cabeza
				MyNode<T> reference = head;
				while(reference.next != null && comparator.compare(info, reference.next.infoNode) >= 0 ){
					reference = reference.next;
				}
				reference.next = new MyNode<>(info, reference.next);
				return;
			}
		}
		head = new MyNode<>(info, head);
	}

	/**
	 * agrega al final de la cola
	 * @param info que se va ha agregar
	 */
	private void add(T info) {
		if(head != null) {
			MyNode<T> last = head;
			while(last.next != null) {
				last = last.next;
			}
			last.next = new MyNode<>(info);;
		}else{
			this.head = new MyNode<>(info);
		}
	}

	/**
	 * remueve el primer elemento (cabeza)
	 * @return la informacion de ese elemento
	 * @throws MyException se lanza cuando no queda mas datos en la cola
	 */
	public T getToQueue() throws MyException {
		MyNode<T> element = null;
		if(this.head != null) {
			element = head;
			head = head.next;
			return element.infoNode;
		}
		throw new MyException(EXCEPTION_MESSAGE);
	}

	/**
	 * Permite colar una informacion de primeras
	 * @param info que deseamos colar
	 */
	public void addToHead(T info) {
		if(head != null) {
			head = new MyNode<>(info, head);
		}else {
			head = new MyNode<>(info);
		}
	}

	/**
	 * Nos determina el tama�o de la cola
	 * @return el numero de datos de la cola (nodos)
	 */
	public int sizeQueue() {
		int count = 0;
		MyNode<T> aux = this.head;
		while(aux != null) {
			count ++;
			aux = aux.next;
		}
		return count;
	}

	/**
	 * Nos permite visulizar la cola
	 * @return 
	 */
	public String showQueue() {
		String queue = "";
		MyNode<T> aux = this.head;
		while(aux != null) {
			queue += aux.infoNode + "<--";
			aux = aux.next;
		}
		return queue;
	}

	public MyNode<T> getHead() {
		return head;
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {

			private MyNode<T> actual = head;

			@Override
			public boolean hasNext() {
				return actual != null;
			}

			@Override
			public T next() {
				T info = actual.infoNode;
				actual = actual.next;
				return info;
			}
		};
	}
}