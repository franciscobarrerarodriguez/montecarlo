package logica;

import org.apache.commons.io.FileUtils;
//import com.sun.jna.Native;
//import com.sun.jna.NativeLibrary;

import java.io.File;
import java.io.IOException;

//import uk.co.caprica.vlcj.binding.LibVlc;
//import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class Analisis extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private File file;

	private String capturasPath;

	private int numeroDePuntos;

	private int tiempoEntreMuestra;

	// static {
	// NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),
	// "C:\\Program Files\\VideoLAN\\VLC");
	// Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
	// }

	public Analisis() {

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
}
