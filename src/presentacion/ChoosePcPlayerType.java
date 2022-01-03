package presentacion;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Clase que extiende JPanel para mostrar los tipos de jugadores con los que se puede jugar contra el Pc
 */
public class ChoosePcPlayerType extends JPanel {

    private JButton noobButton;
    private JButton expertButton;

    private GridBagConstraints constraints = new GridBagConstraints();

    /**
     * Constructor de la clase ChoosePcPlayerType
     */
    public ChoosePcPlayerType(){
        super();
        prepareElements();
        prepareActions();
    }

    /**
     * Metodo que asigna las acciones a los botones del panel
     */
    private void prepareActions() {
        noobButton.addActionListener(e -> noobPc());
        expertButton.addActionListener(e -> expertPc());
    }

    /**
     * Metodo que crea cambia de panel diciendo eque la partida tendra un jugador experto
     */
    private void expertPc() {
        TetrisGUI frame = (TetrisGUI)this.getTopLevelAncestor();
        frame.setContentPane(new BoardTypePanel(1, 1, "dominio.Expert"));
        //frame.setSize((int)(frameSize.height * 1.2), (int)(frameSize.width * 1.1));
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Metodo que crea cambia de panel diciendo eque la partida tendra un jugador principiante
     */
    private void noobPc() {
        TetrisGUI frame = (TetrisGUI)this.getTopLevelAncestor();
        frame.setContentPane(new BoardTypePanel(1, 1, "dominio.Noob"));
        //frame.setSize((int)(frameSize.height * 1.2), (int)(frameSize.width * 1.1));
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Metodo que prepara los elementos del panel
     */
    private void prepareElements() {
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.BLACK);

        noobButton = new JButton();
        noobButton.setIcon(scaleImage("./resources/NoobButton.png", 1f/2f));
        noobButton.setBorderPainted(false);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0,50,0,50);
        this.add(noobButton, constraints);

        expertButton = new JButton();
        expertButton.setIcon(scaleImage("./resources/ExpertButton.png", 1f/2f));
        expertButton.setBorderPainted(false);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.insets = new Insets(0,50,0,50);
        this.add(expertButton, constraints);

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
