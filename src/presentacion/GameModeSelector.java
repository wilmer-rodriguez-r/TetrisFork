package presentacion;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Clase que extiende el JPanel para moostrar las opciones de juego
 */
public class GameModeSelector extends JPanel {
    private JButton onePlayerButton;
    private JButton twoPlayerButton;
    private JButton playerVsPcButton;

    private GridBagConstraints constraints = new GridBagConstraints();

    /**
     * Constructor de la clase GameModeSelector
     */
    public GameModeSelector(){
        super();
        prepareElements();
        prepareActions();
    }

    /**
     * Metodo que prepara las acciones de los elementos del panel
     */
    private void prepareActions(){
        onePlayerButton.addActionListener(e -> showOnePlayerGame());
        twoPlayerButton.addActionListener(e -> showTwoPlayerGame());
        playerVsPcButton.addActionListener(e -> showPlayerVsPc());
    }

    /**
     * Metodo que prepara el siguiente panel para escoger el tipo de tablero con un solo jugador
     */
    private void showOnePlayerGame() {
        TetrisGUI frame = (TetrisGUI)this.getTopLevelAncestor();
        frame.setContentPane(new BoardTypePanel(1));
        //frame.setSize((int)(frameSize.height * 1.2), (int)(frameSize.width * 1.1));
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Metodo que prepara el siguiente panel para escoger el tipo de tablero con dos jugadores
     */
    private void showTwoPlayerGame() {
        TetrisGUI frame = (TetrisGUI)this.getTopLevelAncestor();
        frame.setContentPane(new BoardTypePanel(2));
        //frame.setSize((int)(frameSize.height * 1.2), (int)(frameSize.width * 1.1));
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Metodo que prepara el siguiente panel para escoger el tipo de tablero con un jugador humano y una maquina
     */
    private void showPlayerVsPc() {
        TetrisGUI frame = (TetrisGUI)this.getTopLevelAncestor();
        frame.setContentPane(new ChoosePcPlayerType());
        frame.revalidate();
        frame.repaint();

    }

    /**
     * Metodo para preparar todos los elementos del tablero
     */
    private void prepareElements() {
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());

        onePlayerButton = new JButton();
        onePlayerButton.setIcon(scaleImage("./resources/OnePlayerButton.png", 1f/3f));
        onePlayerButton.setBorderPainted(false);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.insets = new Insets(20, 0,20,0);
        this.add(onePlayerButton, constraints);

        twoPlayerButton = new JButton();
        twoPlayerButton.setIcon(scaleImage("./resources/TwoPlayerButton.png", 1f/3f));
        twoPlayerButton.setBorderPainted(false);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        this.add(twoPlayerButton, constraints);

        playerVsPcButton = new JButton();
        playerVsPcButton.setIcon(scaleImage("./resources/PlayerVsAIButton.png", 1f/3f));
        playerVsPcButton.setBorderPainted(false);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        this.add(playerVsPcButton, constraints);
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
}
