package models;

import java.awt.Color;
import java.util.Random;

public class MyProcess {
	private static int count;
	private int id;
	private String name;
	private State state;
	private int duration;
	private Color color;

	public MyProcess(int duration) {
		id = count++;
		name = "P" + id;
		this.state = State.CREATE;
		this.duration = duration;
		color = generateRamdonColor();
	}

	public Color generateRamdonColor() {
		Random Rand = new Random();
		float r = Rand.nextFloat();
		float g = Rand.nextFloat();
		float b = Rand.nextFloat();
		Color randomColor = new Color(r, g, b);
		return randomColor;
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
	
	public Color getColor() {
		return color;
	}

	public String toString() {
		return name +  " | duracion: " + duration;
	}
}