package presentacion;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Clase que extiende JPanel para mostrar las opciones de elegir el tipo de tablero
 */
public class BoardTypePanel extends JPanel {

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private Dimension frameSize = new Dimension(screenSize.width/2, screenSize.height/2);

    private JButton slowBoardButton;
    private JButton acceleratedBoardButton;

    private int players;
    private int pcPlayers;
    private String pcExperience;

    private GridBagConstraints constraints = new GridBagConstraints();

    /**
     * Constructor del panel BoardTypePanel
     * @param players Numero de jugadores humanos de la partida
     * @param pcPlayers Numero de jugadores maquina de la partida
     * @param pcExperience Experiencia del jugador maquina
     */
    public BoardTypePanel(int players, int pcPlayers, String pcExperience){
        this.players = players;
        this.pcPlayers = pcPlayers;
        this.pcExperience = pcExperience;

        prepareElements();
        prepareActions();

    }

    /**
     * Constructor del panel BoardTypePanel
     * @param players Numero de jugadores humanos de la partida
     */
    public BoardTypePanel(int players){
        this.players = players;
        this.pcPlayers = 0;

        prepareElements();
        prepareActions();

    }

    /**
     * Metodo que prepara los elementos del panel
     */
    private void prepareElements() {
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);

        slowBoardButton = new JButton();
        slowBoardButton.setIcon(scaleImage("./resources/SlowButton.png", 1f/2f));
        slowBoardButton.setBorderPainted(false);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 20, 0, 20);
        this.add(slowBoardButton, constraints);

        acceleratedBoardButton = new JButton();
        acceleratedBoardButton.setIcon(scaleImage("./resources/FastButton.png", 1f/2f));
        acceleratedBoardButton.setBorderPainted(false);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 20, 0, 20);
        this.add(acceleratedBoardButton, constraints);
    }

    /**
     * Metodo que prepara las acciones de los botones del panel
     */
    private void prepareActions() {
        slowBoardButton.addActionListener(e -> startSlowGame());
        acceleratedBoardButton.addActionListener(e -> startAcceleratedGame());
    }

    /**
     * Metodo que hace que empiece el juego con un tablero lento
     */
    private void startSlowGame(){
        TetrisGUI frame = (TetrisGUI)this.getTopLevelAncestor();
        frame.setContentPane(new GameBackground(players, pcPlayers, "dominio.SlowBoard", pcExperience));
        frame.setSize((int)(frameSize.height * 1.25 * (players + pcPlayers)), (int)(frameSize.width * 1.1));
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Metodo que hace que empiece el juego con un tablero acelerado
     */
    private void startAcceleratedGame(){
        TetrisGUI frame = (TetrisGUI)this.getTopLevelAncestor();
        frame.setContentPane(new GameBackground(players, pcPlayers, "dominio.AcceleratedBoard", pcExperience));
        frame.setSize((int)(frameSize.height * 1.25 * (players + pcPlayers)), (int)(frameSize.width * 1.1));
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
    }

    /*
     * Metodo que se usa para escalar las imagenes
     */
    private ImageIcon scaleImage(String path, float reSizeFactor){
        try{
            BufferedImage img = ImageIO.read(new File(path));
            Image scaledImg = img.getScaledInstance((int)( img.getWidth() * reSizeFactor), (int)(img.getHeight() * reSizeFactor), Image.SCALE_DEFAULT);
            return new ImageIcon(scaledImg);
        } catch (IOException ignored){}
        return null;
    }

}
