package exception;


public class MyException extends NullPointerException{

	private static final long serialVersionUID = 1L;
	private String message;
	
	public MyException(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return message;
	}
}
