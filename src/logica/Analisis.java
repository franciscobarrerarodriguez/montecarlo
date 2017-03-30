package logica;

import org.apache.commons.io.FileUtils;
//import com.sun.jna.Native;
//import com.sun.jna.NativeLibrary;

import gui.VentanaPrincipal;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

//import uk.co.caprica.vlcj.binding.LibVlc;
//import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class Analisis extends javax.swing.JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private File file;

	private String capturasPath;

	private int numeroDePuntos;

	private int tiempoEntreMuestra;

	private Thread threadDetectarMovimiento;

	private boolean banderaDetectar;

	private ArrayList<String> arrayListRoutes;

	private VentanaPrincipal ventanaPrincipal;

	// static {
	// NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),
	// "C:\\Program Files\\VideoLAN\\VLC");
	// Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
	// }

	public Analisis(VentanaPrincipal ventanaPrincipal) {

		this.ventanaPrincipal = ventanaPrincipal;

		this.capturasPath = Constantes.CAPTURAS_PATH;

		boolean banderaPath = false;

		/* Creacion del directorio para las capturas */
		File crearPath = new File(this.capturasPath);

		try {
			banderaPath = crearPath.mkdir();
		} catch (SecurityException securityException) {
			System.out.println("Error creando directorio:" + securityException);
		}

		if (banderaPath) {
			System.out.println("Directorio de capturas creado. ");
		} else {
			System.out.println("El directorio no se pudo crear");
		}
		/* / */

		/* Borra todos los archivos existentes en la carpeta de capturas */
		try {
			FileUtils.cleanDirectory(new File(this.capturasPath));
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		this.numeroDePuntos = Constantes.PUNTOS_POR_DEFECTO;
		this.tiempoEntreMuestra = Constantes.TIEMPO_ENTRE_MUESTRA_POR_DEFECTO;

		this.arrayListRoutes = new ArrayList<>();

		this.banderaDetectar = false;

		this.threadDetectarMovimiento = new Thread(this);

	}

	public void comparar(String ruta1, String ruta2) {

		InputStream inputStreamRuta1, inputStreamRuta2;

		try {
			inputStreamRuta1 = new FileInputStream(ruta1);
			ImageInputStream imageInputStream1 = ImageIO.createImageInputStream(inputStreamRuta1);
			BufferedImage bufferedImage1 = ImageIO.read(imageInputStream1);

			inputStreamRuta2 = new FileInputStream(ruta2);
			ImageInputStream imageInputStream2 = ImageIO.createImageInputStream(inputStreamRuta2);
			BufferedImage bufferedImage2 = ImageIO.read(imageInputStream2);

			ArrayList<Datos> arrayListDatos = Montecarlo.cuadrado(bufferedImage1.getWidth(), bufferedImage1.getHeight(),
					this.getNumeroDePuntos());
			int contador = 0;

			for (int i = 0; i < arrayListDatos.size(); i++) {

				Color color1 = new Color(
						bufferedImage1.getRGB(arrayListDatos.get(i).getX(), arrayListDatos.get(i).getY()));
				Color color2 = new Color(
						bufferedImage2.getRGB(arrayListDatos.get(i).getX(), arrayListDatos.get(i).getY()));

				if (!color1.equals(color2)) {
					contador++;
					bufferedImage1.setRGB(arrayListDatos.get(i).getX(), arrayListDatos.get(i).getY(), Color.CYAN.getRGB());
				}
			}
			
			 BufferedImage newImage = bufferedImage1;
			 ImageIO.write(newImage, "png", new File(ruta1));

		
			this.ventanaPrincipal.getjTextAreaConsola().setText(this.ventanaPrincipal.getjTextAreaConsola().getText()
					+ "\nImagen 1: " + ruta1 + "\nImagen2: " + ruta2 + "\nCambiaron " + contador + "Pixeles\n");

		} catch (IOException e1) {
			System.err.println(e1.getMessage());
		}
	
	}

	@Override
	public void run() {
		while (this.banderaDetectar) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				System.err.println(e1.getMessage());
			}
			if (this.arrayListRoutes.size() >= 2) {
				this.comparar(this.arrayListRoutes.get(0), this.arrayListRoutes.get(1));

				this.arrayListRoutes.remove(0);
			}
		}
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getCapturasPath() {
		return capturasPath;
	}

	public void setCapturasPath(String capturasPath) {
		this.capturasPath = capturasPath;
	}

	public int getNumeroDePuntos() {
		return numeroDePuntos;
	}

	public void setNumeroDePuntos(int numeroDePuntos) {
		this.numeroDePuntos = numeroDePuntos;
	}

	public int getTiempoEntreMuestra() {
		return tiempoEntreMuestra;
	}

	public void setTiempoEntreMuestra(int tiempoEntreMuestra) {
		this.tiempoEntreMuestra = tiempoEntreMuestra;
	}

	public ArrayList<String> getArrayListRoutes() {
		return arrayListRoutes;
	}

	public void setArrayListRoutes(ArrayList<String> arrayListRoutes) {
		this.arrayListRoutes = arrayListRoutes;
	}

	public Thread getThreadDetectarMovimiento() {
		return threadDetectarMovimiento;
	}

	public void setThreadDetectarMovimiento(Thread threadDetectarMovimiento) {
		this.threadDetectarMovimiento = threadDetectarMovimiento;
	}

	public boolean isBanderaDetectar() {
		return banderaDetectar;
	}

	public void setBanderaDetectar(boolean banderaDetectar) {
		this.banderaDetectar = banderaDetectar;
	}

}
