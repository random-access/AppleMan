package gui;

import java.awt.Color;
import java.awt.Point;

public class ColorPoint {
	private Color color;
	private Point p;

	public ColorPoint(Color color, Point p) {
		this.color = color;
		this.p = p;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getX() {
		return p.x;
	}

	public int getY() {
		return p.y;
	}
	
	
}
