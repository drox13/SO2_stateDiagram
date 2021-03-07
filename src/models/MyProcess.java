package models;

public class MyProcess {
	private static int count;
	private int id;
	private String name;
	private State state;
	private int duration;
	
	public MyProcess(int duration) {
		id = count++;
		name = "P" + id;
		this.state = State.CREATE;
		this.duration = duration;
	}
	
	public State getState() {
		return state;
	}
	
	public void setState(State state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String toString() {
		return name +  " | duracion: " + duration;
	}
}