package logica;

import java.util.ArrayList;
import java.util.Random;

public class Montecarlo {

	private int puntos;
	private Random random;

	public Montecarlo(int puntos) {

		this.puntos = puntos;
		this.random = new Random();
	}

	public ArrayList<Datos> circulo(int ancho, int alto) {
		// centro de la imagen
		ArrayList<Datos> arrayListDatos = new ArrayList<>();
		int centro = 0;
		int diametro = 0;
		if (ancho < alto)
			centro = ancho / 2;
		else
			centro = alto / 2;

		diametro = centro * 2;
		// /centro

		int contadordentro = 0, contadorFuera = 0;
		for (int i = 0; i < this.puntos; i++) {
			int x = (int) ((this.random.nextDouble() * ancho) + 1);
			int y = (int) ((this.random.nextDouble() * alto) + 1);
			if (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) <= diametro) {
				arrayListDatos.add(new Datos(x, y));
			}
		}
		
		return arrayListDatos;
	}

	public static void main(String[] args) {
		Montecarlo montecarlo = new Montecarlo(1500);
		montecarlo.circulo(2000, 2000);
	}
}
