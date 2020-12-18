package views;

import java.awt.Color;
import java.awt.Rectangle;

public class MyRectangle extends Rectangle{
	private static final long serialVersionUID = 1L;
	private Color colorRectangle;
	private static final int WIDTH_STATE = 200;
	private static final int HEIGTH_STATE = 100;
	private static final int WIDTH_PROCESS_QUEUE = 140;
	private static final int HEIGTH_PROCESS_QUEUE = 40;
	
	public MyRectangle(int x, int y, String color) {
		super(x, y, WIDTH_STATE, HEIGTH_STATE);
		colorRectangle = Color.decode(color);
	}
	
	public MyRectangle(int x, int y) {
		super(x, y, WIDTH_PROCESS_QUEUE, HEIGTH_PROCESS_QUEUE);
		colorRectangle = Color.decode("#74d2e7");
	}
	
	public Color getColorRectangle() {
		return colorRectangle;
	}
	
	public void setColorRectangle(Color colorRectangle) {
		this.colorRectangle = colorRectangle;
	}
	
}