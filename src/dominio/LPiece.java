package dominio;

import java.awt.*;
import java.io.Serializable;
import java.util.Random;

/**
 * Clase que define la forma de las piezas en L
 */
public class LPiece extends Piece implements Serializable {

    /**
     * Constructor de las piezas en L
     */
    public LPiece(){
        super.getForm()[0][1] = true;
        super.getForm()[1][1] = true;
        super.getForm()[2][1] = true;
        super.getForm()[2][2] = true;

        Random random = new Random();
        TetrisCell type = super.getPossiblePieceTypes().get(random.nextInt(super.getPossiblePieceTypes().size()));

        try{
            super.getPieceType()[0][1] = type.getClass().newInstance();
            super.getPieceType()[0][1].setPieceColor(Color.ORANGE);

            super.getPieceType()[1][1] = type.getClass().newInstance();
            super.getPieceType()[1][1].setPieceColor(Color.ORANGE);

            super.getPieceType()[2][1] = type.getClass().newInstance();
            super.getPieceType()[2][1].setPieceColor(Color.ORANGE);

            super.getPieceType()[2][2] = type.getClass().newInstance();
            super.getPieceType()[2][2].setPieceColor(Color.ORANGE);

        } catch (InstantiationException | IllegalAccessException ignored){}

        super.setColor(Color.ORANGE);
    }

    // Este metodo se usa unicamente para las pruebas y poder tener un control de las aserciones
    public LPiece(int i){
        super.getForm()[0][1] = true;
        super.getForm()[1][1] = true;
        super.getForm()[2][1] = true;
        super.getForm()[2][2] = true;

        TetrisCell type = super.getPossiblePieceTypes().get(0);

        try{
            super.getPieceType()[0][1] = type.getClass().newInstance();
            super.getPieceType()[0][1].setPieceColor(Color.ORANGE);

            super.getPieceType()[1][1] = type.getClass().newInstance();
            super.getPieceType()[1][1].setPieceColor(Color.ORANGE);

            super.getPieceType()[2][1] = type.getClass().newInstance();
            super.getPieceType()[2][1].setPieceColor(Color.ORANGE);

            super.getPieceType()[2][2] = type.getClass().newInstance();
            super.getPieceType()[2][2].setPieceColor(Color.ORANGE);

        } catch (InstantiationException | IllegalAccessException ignored){}

        super.setColor(Color.ORANGE);
    }

}
