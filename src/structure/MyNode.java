package structure;

/**
 * 
 * @author Dario Baron
 *
 * @param <T> tipo de dato del nodo
 */
public class MyNode <T>{
	protected T infoNode;
	protected MyNode<T> next;

	/**
	 * crea un nodo con la informacion pasada por parametro
	 * @param infoNode informacion que contendra el nodo
	 */
	public MyNode(T infoNode) {
		this.infoNode = infoNode;
	}

	/**
	 * crea con la informacion y permite asignarle el siguiente
	 * @param infoNode informacion que contendra el nodo
	 * @param next nodo al que apuntara
	 */
	public MyNode(T infoNode, MyNode<T> next) {
		this.infoNode = infoNode;
		this.next = next;
	}

	@Override
	public String toString() {
		return infoNode + "";
	}
}