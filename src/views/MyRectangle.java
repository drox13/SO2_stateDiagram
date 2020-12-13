package views;

import java.awt.Color;
import java.awt.Rectangle;

public class MyRectangle extends Rectangle{
	private static final long serialVersionUID = 1L;
	private Color colorRectangle;
	private static final int WIDTH = 200;
	private static final int HEIGTH = 100;
	
	public MyRectangle(int x, int y, String color) {
		super(x, y, WIDTH, HEIGTH);
		colorRectangle = Color.decode(color);
	}
	
	public Color getColorRectangle() {
		return colorRectangle;
	}
	
	public void setColorRectangle(Color colorRectangle) {
		this.colorRectangle = colorRectangle;
	}
	
}