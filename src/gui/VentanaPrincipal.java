package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import logica.Analisis;
import logica.Constantes;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

public class VentanaPrincipal extends JFrame implements ActionListener, MouseListener, Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JMenuBar jMenuBar;
	private JMenu jMenuArchivo;
	private JMenuItem jMenuItemAbrir;

	private JPanel jPanelCenter, jPanelNorth;

	private JButton jButtonReproducir, jButtonPausar, jButtonParar, jButtonSilencio, jButtonCaptura;

	private JSlider jSliderProgreso;

	private JLabel jLabelLog;

	private JTextField jTextFieldPuntos, jTextFieldTiempoDeMuestra;
	
	private JScrollPane jScrollPane;
	private JTextArea jTextAreaConsola;

	private EmbeddedMediaPlayerComponent embeddedMediaPlayerComponent;

	private Thread threadCapturas;

	private Analisis analisis;

	/* Controla la reproduccion del video adelantar o devolver. */
	private boolean bandera;

	public VentanaPrincipal() {

		super();

		this.bandera = true;

		this.setTitle("Montecarlo en video  ");
		this.setSize(720, 480);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setResizable(true);

		/* menuBar */
		this.jMenuBar = new JMenuBar();
		this.jMenuArchivo = new JMenu("Archivo");
		this.jMenuArchivo.add(this.jMenuItemAbrir = new JMenuItem("Abrir"));
		this.jMenuItemAbrir.addActionListener(this);
		this.jMenuBar.add(this.jMenuArchivo);
		this.setJMenuBar(this.jMenuBar);
		/* /menuBar */

		/* Controles */
		JPanel jPanelControles = new JPanel(new GridLayout(1, 5));
		jPanelControles.add(this.jButtonReproducir = new JButton("Reproducir"));
		this.jButtonReproducir.addActionListener(this);
		jPanelControles.add(this.jButtonPausar = new JButton("Pausar"));
		this.jButtonPausar.addActionListener(this);
		jPanelControles.add(this.jButtonParar = new JButton("Parar"));
		this.jButtonParar.addActionListener(this);
		jPanelControles.add(this.jButtonSilencio = new JButton("Silencio"));
		this.jButtonSilencio.addActionListener(this);
		jPanelControles.add(this.jButtonCaptura = new JButton("Captura"));
		this.jButtonCaptura.addActionListener(this);
		/* /Controles */

		/* Reproductor */
		this.jPanelCenter = new JPanel(new BorderLayout());
		JPanel jPanelAuxiliar = new JPanel(new GridLayout(3, 1));
		/* Progreso */
		jPanelAuxiliar.add(this.jSliderProgreso = new JSlider());
		this.jSliderProgreso.setMinimum(0);
		this.jSliderProgreso.setMaximum(100);
		this.jSliderProgreso.setValue(0);
		this.jSliderProgreso.setEnabled(false);

		// Listener para jSliderProgreso
		this.jSliderProgreso.addMouseListener(this);

		// Listener para cambiar a posicion de la reproduccion
		this.jSliderProgreso.addChangeListener(new ChangeListener() {
			@Override
			public synchronized void stateChanged(ChangeEvent e) {
				if (!bandera) {
					Object source = e.getSource();
					float np = ((JSlider) source).getValue() / 100f;
					embeddedMediaPlayerComponent.getMediaPlayer().setPosition(np);
				}

			}
		});

		/* /Progreso */
		jPanelAuxiliar.add(jPanelControles);
		jPanelAuxiliar.add(this.jLabelLog = new JLabel("Waiting ", JLabel.RIGHT));
		this.jLabelLog.setForeground(Constantes.COLOR_DE_MOVIMIENTO);
		this.jPanelCenter.add(jPanelAuxiliar, BorderLayout.SOUTH);

		this.jPanelCenter.add(this.embeddedMediaPlayerComponent = new EmbeddedMediaPlayerComponent(),
				BorderLayout.CENTER);

		/* Listener para el control de progreso */
		this.embeddedMediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			@Override
			public void positionChanged(MediaPlayer mp, float pos) {
				if (bandera) {
					int value = Math.min(100, Math.round(pos * 100.0f));
					jSliderProgreso.setValue(value);
				}
			}
		});
		/* /Listener para el control de progreso */

		this.add(this.jPanelCenter, BorderLayout.CENTER);
		/* /Reproductor */

		/* Reproductor clase */
		this.analisis = new Analisis(this);
		/* /Reproductor clase */

		/* Panel East */
		this.jPanelNorth = new JPanel(new BorderLayout());
		JPanel jPanelAuxiliar2 = new JPanel(new GridLayout(1, 4));
		jPanelAuxiliar2.add(new JLabel("# puntos: ", JLabel.CENTER));
		jPanelAuxiliar2.add(this.jTextFieldPuntos = new JTextField());
		this.jTextFieldPuntos.setText(String.valueOf(this.analisis.getNumeroDePuntos()));
		jPanelAuxiliar2.add(new JLabel("Intervalos (ml): ", JLabel.CENTER));
		jPanelAuxiliar2.add(this.jTextFieldTiempoDeMuestra = new JTextField());
		this.jTextFieldTiempoDeMuestra.setText(String.valueOf(this.analisis.getTiempoEntreMuestra()));

		/* Chochan los paneles */
		this.jPanelNorth.add(jPanelAuxiliar2, BorderLayout.NORTH);
		this.add(this.jPanelNorth, BorderLayout.NORTH);
		/* /Panel North */
		
		/* Panel east */
		this.jTextAreaConsola = new JTextArea("========== Consola =========");
		this.jScrollPane = new JScrollPane(this.jTextAreaConsola);
		this.add(this.jScrollPane, BorderLayout.EAST);
		/*/Panel east */

		/* Hilo para controlar las capturas del video */
		this.threadCapturas = new Thread(this);
		
		
	}

	private boolean abrir() {
		JFileChooser jFileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Videos", "mp4", "flv", "webm", "3gp", "dat");
		jFileChooser.addChoosableFileFilter(filter);
		if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			this.analisis.setFile(jFileChooser.getSelectedFile());
			// this.jButtonReproducir.doClick();
			return true;
		} else {
			return false;
		}
	}

	private boolean reproducir() {
		if (this.analisis.getFile() != null) {
			this.embeddedMediaPlayerComponent.getMediaPlayer().playMedia(this.analisis.getFile().getAbsolutePath());
			this.jSliderProgreso.setEnabled(true);
			this.setTitle(this.getTitle() + "//" + this.analisis.getFile().getName());

			this.analisis.setNumeroDePuntos(Integer.parseInt(this.jTextFieldPuntos.getText()));
			this.analisis.setTiempoEntreMuestra(Integer.parseInt(this.jTextFieldTiempoDeMuestra.getText()));

			this.threadCapturas.start();
			
			return true;
		} else {
			return false;
		}
	}

	private void pausar() {
		this.embeddedMediaPlayerComponent.getMediaPlayer()
				.setPause(this.embeddedMediaPlayerComponent.getMediaPlayer().isPlaying() ? true : false);
	}

	private void parar() {
		this.embeddedMediaPlayerComponent.getMediaPlayer().stop();
		this.jSliderProgreso.setValue(0);
		this.jSliderProgreso.setEnabled(false);
		this.setTitle("Montecarlo en video ");
	}

	/* Aun no sirve ..... */
	private void silenciar(ActionEvent e) {
		AbstractButton abstractButton = (AbstractButton) e.getSource();
		this.embeddedMediaPlayerComponent.getMediaPlayer().mute(abstractButton.getModel().isSelected());
	}

	private void captura() {
		if (this.analisis.getFile() != null) {
			String nuevoPath = this.analisis.getCapturasPath() + System.currentTimeMillis() + ".png";
			if (this.embeddedMediaPlayerComponent.getMediaPlayer().saveSnapshot(new File(nuevoPath))) {
				this.jLabelLog.setText("Captura guardada " + nuevoPath + " ");
				this.analisis.getArrayListRoutes().add(nuevoPath);
				if (this.analisis.isBanderaDetectar() == false) { 
					this.analisis.setBanderaDetectar(true);
					this.analisis.getThreadDetectarMovimiento().start();
				}
			} else {
				this.jLabelLog.setText("No se puedo guardar captura ");
			}
		}
	}

	@Override
	public void run() {
		// Cambiar esto por una bandera
		while (true) {
			this.captura();
			try {
				Thread.sleep(this.analisis.getTiempoEntreMuestra());
			} catch (InterruptedException e) {
				this.jLabelLog.setText(e.getMessage());
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.jMenuItemAbrir)) {
			if (this.abrir()) {
				this.jLabelLog.setText("Abierto ");
			} else {
				this.jLabelLog.setText("No se puede abrir el archivo ");
			}
		}
		if (e.getSource().equals(this.jButtonReproducir)) {
			if (this.reproducir()) {
				this.jLabelLog.setText("Reproduciendo ");
			} else {
				this.jLabelLog.setText("No se puede abrir el archivo ");
			}
		}
		if (e.getSource().equals(this.jButtonPausar)) {
			this.pausar();
			this.jLabelLog.setText("Pausado ");
		}
		if (e.getSource().equals(this.jButtonParar)) {
			this.parar();
		}
		if (e.getSource().equals(this.jButtonSilencio)) {
			this.silenciar(e);
		}
		if (e.getSource().equals(this.jButtonCaptura)) {
			this.captura();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.bandera = false;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.bandera = true;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
		ventanaPrincipal.setVisible(true);
	}

	public JMenuBar getjMenuBar() {
		return jMenuBar;
	}

	public void setjMenuBar(JMenuBar jMenuBar) {
		this.jMenuBar = jMenuBar;
	}

	public JMenu getjMenuArchivo() {
		return jMenuArchivo;
	}

	public void setjMenuArchivo(JMenu jMenuArchivo) {
		this.jMenuArchivo = jMenuArchivo;
	}

	public JMenuItem getjMenuItemAbrir() {
		return jMenuItemAbrir;
	}

	public void setjMenuItemAbrir(JMenuItem jMenuItemAbrir) {
		this.jMenuItemAbrir = jMenuItemAbrir;
	}

	public JPanel getjPanelCenter() {
		return jPanelCenter;
	}

	public void setjPanelCenter(JPanel jPanelCenter) {
		this.jPanelCenter = jPanelCenter;
	}

	public JPanel getjPanelNorth() {
		return jPanelNorth;
	}

	public void setjPanelNorth(JPanel jPanelNorth) {
		this.jPanelNorth = jPanelNorth;
	}

	public JButton getjButtonReproducir() {
		return jButtonReproducir;
	}

	public void setjButtonReproducir(JButton jButtonReproducir) {
		this.jButtonReproducir = jButtonReproducir;
	}

	public JButton getjButtonPausar() {
		return jButtonPausar;
	}

	public void setjButtonPausar(JButton jButtonPausar) {
		this.jButtonPausar = jButtonPausar;
	}

	public JButton getjButtonParar() {
		return jButtonParar;
	}

	public void setjButtonParar(JButton jButtonParar) {
		this.jButtonParar = jButtonParar;
	}

	public JButton getjButtonSilencio() {
		return jButtonSilencio;
	}

	public void setjButtonSilencio(JButton jButtonSilencio) {
		this.jButtonSilencio = jButtonSilencio;
	}

	public JButton getjButtonCaptura() {
		return jButtonCaptura;
	}

	public void setjButtonCaptura(JButton jButtonCaptura) {
		this.jButtonCaptura = jButtonCaptura;
	}

	public JSlider getjSliderProgreso() {
		return jSliderProgreso;
	}

	public void setjSliderProgreso(JSlider jSliderProgreso) {
		this.jSliderProgreso = jSliderProgreso;
	}

	public JLabel getjLabelLog() {
		return jLabelLog;
	}

	public void setjLabelLog(JLabel jLabelLog) {
		this.jLabelLog = jLabelLog;
	}

	public JTextField getjTextFieldPuntos() {
		return jTextFieldPuntos;
	}

	public void setjTextFieldPuntos(JTextField jTextFieldPuntos) {
		this.jTextFieldPuntos = jTextFieldPuntos;
	}

	public JTextField getjTextFieldTiempoDeMuestra() {
		return jTextFieldTiempoDeMuestra;
	}

	public void setjTextFieldTiempoDeMuestra(JTextField jTextFieldTiempoDeMuestra) {
		this.jTextFieldTiempoDeMuestra = jTextFieldTiempoDeMuestra;
	}

	public JScrollPane getjScrollPane() {
		return jScrollPane;
	}

	public void setjScrollPane(JScrollPane jScrollPane) {
		this.jScrollPane = jScrollPane;
	}

	public JTextArea getjTextAreaConsola() {
		return jTextAreaConsola;
	}

	public void setjTextAreaConsola(JTextArea jTextAreaConsola) {
		this.jTextAreaConsola = jTextAreaConsola;
	}

	public EmbeddedMediaPlayerComponent getEmbeddedMediaPlayerComponent() {
		return embeddedMediaPlayerComponent;
	}

	public void setEmbeddedMediaPlayerComponent(EmbeddedMediaPlayerComponent embeddedMediaPlayerComponent) {
		this.embeddedMediaPlayerComponent = embeddedMediaPlayerComponent;
	}

	public Thread getThreadCapturas() {
		return threadCapturas;
	}

	public void setThreadCapturas(Thread threadCapturas) {
		this.threadCapturas = threadCapturas;
	}

	public Analisis getAnalisis() {
		return analisis;
	}

	public void setAnalisis(Analisis analisis) {
		this.analisis = analisis;
	}

	public boolean isBandera() {
		return bandera;
	}

	public void setBandera(boolean bandera) {
		this.bandera = bandera;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
