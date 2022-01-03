package dominio;

import java.awt.*;
import java.io.Serializable;
import java.util.Random;

/**
 * Clase que define la forma de las piezas en S
 */
public class SPiece extends Piece implements Serializable {

    /**
     * Constructor de las piezas en S
     */
    public SPiece(){
        super.getForm()[0][1] = true;
        super.getForm()[1][1] = true;
        super.getForm()[1][2] = true;
        super.getForm()[2][2] = true;

        Random random = new Random();
        TetrisCell type = super.getPossiblePieceTypes().get(random.nextInt(super.getPossiblePieceTypes().size()));

        try{
            super.getPieceType()[0][1] = type.getClass().newInstance();
            super.getPieceType()[0][1].setPieceColor(Color.GREEN);

            super.getPieceType()[1][1] = type.getClass().newInstance();
            super.getPieceType()[1][1].setPieceColor(Color.GREEN);

            super.getPieceType()[1][2] = type.getClass().newInstance();
            super.getPieceType()[1][2].setPieceColor(Color.GREEN);

            super.getPieceType()[2][2] = type.getClass().newInstance();
            super.getPieceType()[2][2].setPieceColor(Color.GREEN);
        } catch (InstantiationException | IllegalAccessException ignored){}

        super.setColor(Color.GREEN);
    }
}
