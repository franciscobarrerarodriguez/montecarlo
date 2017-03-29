package logica;

import java.awt.Color;

public class Datos {

	private int x;
	private int y;
	private Color color;
	private boolean esIgual;

	public Datos(int x, int y) {

		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isEsIgual() {
		return esIgual;
	}

	public void setEsIgual(boolean esIgual) {
		this.esIgual = esIgual;
	}

}
