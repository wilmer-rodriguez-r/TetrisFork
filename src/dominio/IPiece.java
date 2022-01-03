package dominio;

import java.awt.*;
import java.io.Serializable;
import java.util.Random;

/**
 * Clase que define la forma de las piezas en I
 */
public class IPiece extends Piece implements Serializable {

    /**
     * Constructor de las piezas en I
     */
    public IPiece(){
        super.getForm()[0][1] = true;
        super.getForm()[1][1] = true;
        super.getForm()[2][1] = true;
        super.getForm()[3][1] = true;

        Random random = new Random();
        TetrisCell type = super.getPossiblePieceTypes().get(random.nextInt(super.getPossiblePieceTypes().size()));

        try{
            super.getPieceType()[0][1] = type.getClass().newInstance();
            super.getPieceType()[0][1].setPieceColor(Color.CYAN);

            super.getPieceType()[1][1] = type.getClass().newInstance();
            super.getPieceType()[1][1].setPieceColor(Color.CYAN);

            super.getPieceType()[2][1] = type.getClass().newInstance();
            super.getPieceType()[2][1].setPieceColor(Color.CYAN);

            super.getPieceType()[3][1] = type.getClass().newInstance();
            super.getPieceType()[3][1].setPieceColor(Color.CYAN);
        } catch (InstantiationException | IllegalAccessException ignored){}

        super.setColor(Color.CYAN);
    }

    // Para las pruebas
    public IPiece(int i){
        super.getForm()[0][1] = true;
        super.getForm()[1][1] = true;
        super.getForm()[2][1] = true;
        super.getForm()[3][1] = true;

        TetrisCell type = super.getPossiblePieceTypes().get(i);

        try{
            super.getPieceType()[0][1] = type.getClass().newInstance();
            super.getPieceType()[0][1].setPieceColor(Color.CYAN);

            super.getPieceType()[1][1] = type.getClass().newInstance();
            super.getPieceType()[1][1].setPieceColor(Color.CYAN);

            super.getPieceType()[2][1] = type.getClass().newInstance();
            super.getPieceType()[2][1].setPieceColor(Color.CYAN);

            super.getPieceType()[3][1] = type.getClass().newInstance();
            super.getPieceType()[3][1].setPieceColor(Color.CYAN);
        } catch (InstantiationException | IllegalAccessException ignored){}

        super.setColor(Color.CYAN);
    }
}
