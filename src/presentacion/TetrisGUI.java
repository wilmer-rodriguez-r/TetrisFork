package presentacion;

import persistencia.IOGameData;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Clase principal del proyecto, esta extiende el JFrame para manejar los paneles
 */
public class TetrisGUI extends JFrame {

    public static String User = "";

    Clip backgroundMusic;

    private GameModeSelector gameModeSelector;

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private Dimension frameSize = new Dimension(screenSize.width/2, screenSize.height/2);

    private JPanel mainMenuBackGround;

    private JPanel titlePanel;
    private JLabel titleImage;

    private JPanel playButtonPanel;
    private JButton playButton;

    private JPanel settingsButtonPanel;
    private JButton settingsButton;

    private JPanel exitButtonPanel;
    private JButton exitButton;

    /**
     * Constructor de la clase principal
     */
    private TetrisGUI() {
        super("Tetris Proyecto Final");
        prepareElements();
        prepareActions();
        prepareMusic();
    }

    /**
     * Metodo que retorna el panel del menu principal
     * @return
     */
    public JPanel getMainMenuBackGround() {
        return mainMenuBackGround;
    }

    /**
     * Metodo que prepara la musica de fondo del juego
     */
    private void prepareMusic() {
        try {
            backgroundMusic = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("./resources/TetrisBackgroundMusic.wav"));
            backgroundMusic.open(inputStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            FloatControl volume = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            if (volume != null) {
                volume.setValue(-40.0f);
            }
        }catch(LineUnavailableException | UnsupportedAudioFileException | IOException ignored){}
    }

    /**
     * Metodo que prepara los elementos del menu principal
     */
    private void prepareElements() {
        mainMenuBackGround = new ImagePanel("./resources/MenuBackground.gif");
        mainMenuBackGround.setLayout(new BoxLayout(mainMenuBackGround, BoxLayout.Y_AXIS));
        mainMenuBackGround.setBackground(Color.BLACK);

        playButton = new JButton();
        playButton.setBorderPainted(false);
        playButton.setIcon(scaleImage("./resources/PlayButton.png", 1f/3f));

        exitButton = new JButton();
        exitButton.setBorderPainted(false);
        exitButton.setIcon(scaleImage("./resources/ExitButton.png", 1f/3f));

        playButtonPanel = new JPanel();
        playButtonPanel.add(playButton);
        playButtonPanel.setOpaque(false);

        prepareImages();

        exitButtonPanel = new JPanel();
        exitButtonPanel.add(exitButton);
        exitButtonPanel.setOpaque(false);

        settingsButton = new JButton();
        settingsButton.setBorderPainted(false);
        settingsButton.setIcon(scaleImage("./resources/SettingsButton.png", 1f/1.8f));

        settingsButtonPanel = new JPanel();
        settingsButtonPanel.add(settingsButton);
        settingsButtonPanel.setOpaque(false);

        mainMenuBackGround.add(titlePanel);
        mainMenuBackGround.add(playButtonPanel);
        mainMenuBackGround.add(settingsButtonPanel);
        mainMenuBackGround.add(exitButtonPanel);

        this.setContentPane(mainMenuBackGround);
        this.setSize(frameSize.width, frameSize.height);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Metodo que prepara las acciones de los elementos del menu principal
     */
    private void prepareActions() {
        exitButton.addActionListener(e -> exit());
        playButton.addActionListener(e -> play());
        settingsButton.addActionListener(e -> settings());
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    Clip powerUp = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("./resources/CoinEasterEgg.wav"));
                    powerUp.open(inputStream);
                    powerUp.start();
                    FloatControl volume = (FloatControl) powerUp.getControl(FloatControl.Type.MASTER_GAIN);
                    if (volume != null) {
                        volume.setValue(-20.0f);
                    }
                }catch(LineUnavailableException | UnsupportedAudioFileException | IOException ignored){}
            }
        });
    }

    /**
     * Metodo que prepara la imagen del menu principal
     */
    private void prepareImages(){
        titlePanel = new JPanel();
        titlePanel.setSize((int)(frameSize.width/1.5), (int)(frameSize.height/3.3));

        titleImage = new JLabel(scaleImageToComponentSize("./resources/Tetris.png", titlePanel));

        titlePanel.add(titleImage);
        titlePanel.setOpaque(false);
    }

    /**
     * Metodo que escala una imagen al tamaño del panel que la compone
     */
    private ImageIcon scaleImageToComponentSize(String path, JPanel parentPanel){
        try{
            Image img = ImageIO.read(new File(path));
            Image scaledImg = img.getScaledInstance(parentPanel.getWidth(), parentPanel.getHeight(), Image.SCALE_DEFAULT);
            return new ImageIcon(scaledImg);
        } catch (IOException ignored){}
        return null;
    }

    /*
     * Metodo para escalar una imagen a un tamaño deseaddo en la  GUI
     */
    private ImageIcon scaleImage(String path, float reSizeFactor){
        try{
            BufferedImage img = ImageIO.read(new File(path));
            Image scaledImg = img.getScaledInstance((int)( img.getWidth() * reSizeFactor), (int)(img.getHeight() * reSizeFactor), Image.SCALE_DEFAULT);
            return new ImageIcon(scaledImg);
        } catch (IOException ignored){}
        return null;
    }

    /**
     * Metodo de accion del boton exit
     */
    private void exit(){
        int resp = JOptionPane.showConfirmDialog(this, "Are you sure you what to exit the game?", "Exit?", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    /**
     * Metodo de accion del boton play
     */
    private void play(){
        gameModeSelector = new GameModeSelector();
        this.setContentPane(gameModeSelector);
        this.setLocationRelativeTo(null);
        this.revalidate();
        this.repaint();
    }

    /**
     * Metodo de la accion del boton settings
     */
    private void settings(){
        this.setContentPane(new SettingsPanel());
        this.setLocationRelativeTo(null);
        this.revalidate();
        this.repaint();
    }

    /**
     * Metodo main de la aplicacion, punto de entrada de esta
     */
    public static void main(String[] args){
        EventQueue.invokeLater(() -> {
            try{
                TetrisGUI tetrisGame = new TetrisGUI();
                tetrisGame.setVisible(true);
            } catch (Exception e){
                IOGameData.registerError(e);
                System.exit(0);
            }
        });
    }
}
