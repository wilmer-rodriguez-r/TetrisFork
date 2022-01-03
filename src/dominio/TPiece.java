package dominio;

import java.awt.*;
import java.io.Serializable;
import java.util.Random;

/**
 * Clase que define la forma de las piezas en T
 */
public class TPiece extends Piece implements Serializable {

    /**
     * Constructor de las piezas en T
     */
    public TPiece(){
        super.getForm()[0][2] = true;
        super.getForm()[1][2] = true;
        super.getForm()[1][1] = true;
        super.getForm()[1][3] = true;

        Random random = new Random();
        TetrisCell type = super.getPossiblePieceTypes().get(random.nextInt(super.getPossiblePieceTypes().size()));

        try{
            super.getPieceType()[0][2] = type.getClass().newInstance();
            super.getPieceType()[0][2].setPieceColor(Color.PINK);

            super.getPieceType()[1][2] = type.getClass().newInstance();
            super.getPieceType()[1][2].setPieceColor(Color.PINK);

            super.getPieceType()[1][1] = type.getClass().newInstance();
            super.getPieceType()[1][1].setPieceColor(Color.PINK);

            super.getPieceType()[1][3] = type.getClass().newInstance();
            super.getPieceType()[1][3].setPieceColor(Color.PINK);
        } catch (InstantiationException | IllegalAccessException ignored){}

        super.setColor(Color.PINK);
    }
}
