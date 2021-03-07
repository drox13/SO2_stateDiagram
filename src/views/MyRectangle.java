package views;

import java.awt.Color;
import java.awt.Rectangle;

public class MyRectangle extends Rectangle{
	private static final long serialVersionUID = 1L;
	private Color colorRectangle;
	public static final int WIDTH_RECTANGLE_STATE = 200;
	public static final int HEIGTH_RECTANGLE_STATE = 100;
	private static final int WIDTH_PROCESS_QUEUE = 180;
	private static final int HEIGTH_PROCESS_QUEUE = 40;
	
	public MyRectangle(int x, int y, String color) {
		super(x, y, WIDTH_RECTANGLE_STATE, HEIGTH_RECTANGLE_STATE);
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