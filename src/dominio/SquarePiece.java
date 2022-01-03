package dominio;

import java.awt.*;
import java.io.Serializable;
import java.util.Random;

/**
 * Clase que define la forma de las piezas cuadradas
 */
public class SquarePiece extends Piece implements Serializable {

    /**
     * Constructor de las piezas cuadradas
     */
    public SquarePiece(){
        super.getForm()[1][1] = true;
        super.getForm()[1][2] = true;
        super.getForm()[2][1] = true;
        super.getForm()[2][2] = true;

        Random random = new Random();
        TetrisCell type = super.getPossiblePieceTypes().get(random.nextInt(super.getPossiblePieceTypes().size()));

        try{
            super.getPieceType()[1][1] = type.getClass().newInstance();
            super.getPieceType()[1][1].setPieceColor(Color.YELLOW);

            super.getPieceType()[1][2] = type.getClass().newInstance();
            super.getPieceType()[1][2].setPieceColor(Color.YELLOW);

            super.getPieceType()[2][1] = type.getClass().newInstance();
            super.getPieceType()[2][1].setPieceColor(Color.YELLOW);

            super.getPieceType()[2][2] = type.getClass().newInstance();
            super.getPieceType()[2][2].setPieceColor(Color.YELLOW);
        } catch (InstantiationException | IllegalAccessException ignored){}

        super.setColor(Color.YELLOW);
    }

    // Este metodo se usa unicamente para las pruebas y poder tener un control de las aserciones
    public SquarePiece(int i){
        super.getForm()[1][1] = true;
        super.getForm()[1][2] = true;
        super.getForm()[2][1] = true;
        super.getForm()[2][2] = true;

        TetrisCell type = super.getPossiblePieceTypes().get(i);

        try{
            super.getPieceType()[1][1] = type.getClass().newInstance();
            super.getPieceType()[1][1].setPieceColor(Color.YELLOW);

            super.getPieceType()[1][2] = type.getClass().newInstance();
            super.getPieceType()[1][2].setPieceColor(Color.YELLOW);

            super.getPieceType()[2][1] = type.getClass().newInstance();
            super.getPieceType()[2][1].setPieceColor(Color.YELLOW);

            super.getPieceType()[2][2] = type.getClass().newInstance();
            super.getPieceType()[2][2].setPieceColor(Color.YELLOW);

        } catch (InstantiationException | IllegalAccessException ignored){}

        super.setColor(Color.YELLOW);
    }
}
