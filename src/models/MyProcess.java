package models;

public class MyProcess {
	private static int count;
	private int id;
	private String name;
	private State state;
	private int duration;
	
	public MyProcess(int duration) {
		id = count++;
		name = "proceso" + id;
		this.state = State.CREATE;
		this.duration = duration;
	}
	
	public State getState() {
		return state;
	}
	
	public void setState(State state) {
		this.state = state;
	}

	public String toString() {
		return "Process [id=" + id + ", name=" + name + 
				", state=" + state + ", duration=" + duration + "]";
	}
}
