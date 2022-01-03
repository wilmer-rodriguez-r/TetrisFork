package presentacion;

import javax.swing.*;
import java.awt.*;

/**
 * Clase que extiende el JPanel para poder tener una imagen de fondo en este
 */
public class ImagePanel extends JPanel {

    private Image image;

    /**
     * Constructor de la clase ImagePanel
     */
    public ImagePanel(){
        super();
    }

    /**
     * Constructor de la clase ImagePanel que permite que este tenga una imagen de fondo
     * @param imagePath Camino a la imagen de fondo
     */
    public ImagePanel(String imagePath){
        super();
        this.image = new ImageIcon(imagePath).getImage();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if(image != null){
            g.drawImage(image, 0,0, getWidth(), getHeight(), this);
        }
    }

}
