package logica;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.nio.file.Files;
import javax.swing.AbstractButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.x.LibXUtil;

/**
 * @web http://www.jc-mouse.net/
 * @author Mouse
 */
public class Reproductor extends javax.swing.JFrame {

    private EmbeddedMediaPlayerComponent player;
    private File file;

    static {
        
            NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\VideoLAN\\VLC");
            Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
    }
   

    //bandera para controlar la reproduccion de video y el cambio en el avance de video
    private boolean band = true;

    /**
     * Creates new form Reproductor
     */
    public Reproductor() {
        initComponents();


        setTitle("VLCJ Player");//nombre de reproductor
        setLocationRelativeTo(null);//centrar en pantalla
        player = new EmbeddedMediaPlayerComponent();
        //se añade reproductor 
        jPanel2.add(player);
        player.setSize(jPanel2.getSize());
        player.setVisible(true);
        //slider control de volumen
        sldVolumen.setMinimum(0);
        sldVolumen.setMaximum(100);
        //slider control progreso
        sldProgress.setMinimum(0);
        sldProgress.setMaximum(100);
        sldProgress.setValue(0);
        sldProgress.setEnabled(false);

        //Control abrir archivo        
        btnOpenFile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Videos", "mp4", "flv", "webm", "3gp", "dat");
                fileChooser.addChoosableFileFilter(filter);
                //fileChooser.setCurrentDirectory(new java.io.File("C:\\videos\\tmp\\"));
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile();
                    btnPlay.doClick();
                }
            }
        });

        //Control captura de ventana
        btnSnapshot.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (file != null) {
                    String absolutePath = file.getAbsolutePath();
                    String newPath = absolutePath.substring(0, absolutePath.length() - 4) + "_" + System.currentTimeMillis() + ".png";
                    if (player.getMediaPlayer().saveSnapshot(new File(newPath))) {
                        JOptionPane.showMessageDialog(null, "Snapshot: " + newPath);
                    }
                }
            }
        });

        //Control de reproduccion
        btnPlay.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (file != null) {
                    player.getMediaPlayer().playMedia(file.getAbsolutePath());
                    sldVolumen.setValue(player.getMediaPlayer().getVolume());
                    sldProgress.setEnabled(true);
                    setTitle(file.getName() + " - VLCJ Player");
                }
            }
        });

        //Control de pausa
        btnPause.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                player.getMediaPlayer().setPause(player.getMediaPlayer().isPlaying() ? true : false);
            }
        });

        //Control detener reproduccion
        btnStop.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                player.getMediaPlayer().stop();
                sldProgress.setValue(0);
                sldProgress.setEnabled(false);
                setTitle("VLCJ Player");
            }
        });

        //Control silenciar 
        btnMute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
                player.getMediaPlayer().mute(abstractButton.getModel().isSelected());
            }
        });

        //Control slider cambiar volumen
        sldVolumen.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                Object source = e.getSource();
                player.getMediaPlayer().setVolume(((JSlider) source).getValue());
            }
        });

        //Listener de reproductor para mostrar el progreso en la reproduccion del video 
        player.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

            @Override
            public void positionChanged(MediaPlayer mp, float pos) {
                if (band) {
                    int value = Math.min(100, Math.round(pos * 100.0f));
                    sldProgress.setValue(value);
                }
            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {

            }

        });

        //Listener para el slider progress
        sldProgress.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                band = false;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                band = true;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

        });

        //Control para cambiar a posicion de reproduccion
        sldProgress.addChangeListener(new ChangeListener() {

            @Override
            public synchronized void stateChanged(ChangeEvent e) {
                if (!band) {
                    Object source = e.getSource();
                    float np = ((JSlider) source).getValue() / 100f;
                    player.getMediaPlayer().setPosition(np);
                }

            }
        });

    }//end: constructor

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        sldProgress = new javax.swing.JSlider();
        jPanel4 = new javax.swing.JPanel();
        btnPlay = new javax.swing.JButton();
        btnPause = new javax.swing.JButton();
        btnStop = new javax.swing.JButton();
        btnMute = new javax.swing.JToggleButton();
        sldVolumen = new javax.swing.JSlider();
        jPanel2 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btnOpenFile = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnSnapshot = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));
        jPanel3.add(sldProgress);

        jPanel1.add(jPanel3, java.awt.BorderLayout.NORTH);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        btnPlay.setText("Play");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel4.add(btnPlay, gridBagConstraints);

        btnPause.setText("Pause");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel4.add(btnPause, gridBagConstraints);

        btnStop.setText("Stop");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel4.add(btnStop, gridBagConstraints);

        btnMute.setText("Mute");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel4.add(btnMute, gridBagConstraints);

        sldVolumen.setPreferredSize(new java.awt.Dimension(100, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel4.add(sldVolumen, gridBagConstraints);

        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setMinimumSize(new java.awt.Dimension(100, 100));
        jPanel2.setPreferredSize(new java.awt.Dimension(400, 300));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));
        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnOpenFile.setText("Open File");
        btnOpenFile.setFocusable(false);
        btnOpenFile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenFile.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnOpenFile);
        jToolBar1.add(jSeparator1);

        btnSnapshot.setText("Snapshot");
        btnSnapshot.setFocusable(false);
        btnSnapshot.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSnapshot.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnSnapshot);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.NORTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Reproductor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Reproductor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Reproductor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Reproductor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Reproductor().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnMute;
    private javax.swing.JButton btnOpenFile;
    private javax.swing.JButton btnPause;
    private javax.swing.JButton btnPlay;
    private javax.swing.JButton btnSnapshot;
    private javax.swing.JButton btnStop;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JSlider sldProgress;
    private javax.swing.JSlider sldVolumen;
    // End of variables declaration//GEN-END:variables
}
