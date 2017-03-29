package logica;

import java.util.ArrayList;
import java.util.Random;

public class Montecarlo {

	// public ArrayList<Datos> circulo(int ancho, int alto) {
	// // centro de la imagen
	// ArrayList<Datos> arrayListDatos = new ArrayList<>();
	// int centro = 0;
	// int diametro = 0;
	// if (ancho < alto)
	// centro = ancho / 2;
	// else
	// centro = alto / 2;
	//
	// diametro = centro * 2;
	// // /centro
	//
	// int contadordentro = 0, contadorFuera = 0;
	// for (int i = 0; i < this.puntos; i++) {
	// int x = (int) ((this.random.nextDouble() * ancho) + 1);
	// int y = (int) ((this.random.nextDouble() * alto) + 1);
	// if (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) <= diametro) {
	// arrayListDatos.add(new Datos(x, y));
	// }
	// }
	//
	// return arrayListDatos;
	// }

	/**
	 * Genera puntos dentro de
	 * 
	 * @param ancontador++;ho
	 * @param alto
	 * @return
	 */
	public static ArrayList<Datos> cuadrado(int ancho, int alto, int puntos) {
		Random random = new Random();
		ArrayList<Datos> arrayListDatos = new ArrayList<>();
		for (int i = 0; i < puntos; i++) {
//			int x = (int) ((random.nextDouble() * ancho - 1) - 1);
//			int y = (int) ((random.nextDouble() * alto - 1) - 1);
			int x = (int) (Math.random()*((ancho - 1) -2) +1);
			int y = (int) (Math.random()*((alto - 1) -2) +1);
			arrayListDatos.add(new Datos(x, y));
		}
		return arrayListDatos;
	}

	public static void main(String[] args) {
		ArrayList<Datos> arrayList = Montecarlo.cuadrado(1920, 1080, 5000);
		int contador = 0;
		for (int i = 0; i < arrayList.size(); i++) {
			if ((arrayList.get(i).getX() > 0) && (arrayList.get(i).getX() < 1920) && (arrayList.get(i).getY() > 0)
					&& (arrayList.get(i).getY() < 1080)) {
				contador++;
				System.out.println("hola");
			} else {
				System.out.println("x " + arrayList.get(i).getX() + " y " + arrayList.get(i).getY());
			}
		}
		System.out.println(contador);
	}
}
