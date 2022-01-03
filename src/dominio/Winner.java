package dominio;

import java.awt.*;
import java.io.Serializable;

/**
 * Clase que representa a los bloques que se ajustan dependiendo de donde se pongan
 */
public class Winner extends TetrisCell implements Serializable {

    /**
     * Constructor de la clase Winner
     */
    public Winner(){
        // Dorado se confunde mucho
        //setBorderColor(new Color(218, 165, 32));
        setBorderColor(Color.MAGENTA);
    }

    /**
     * Metodo que hace que los bloques de la pieza caigan lo que mas puedan ajustandose al estado del tablero
     * @param board Tablero en el que se efectuaran los cambios dependiendo de la pieza
     * @param rowInBoard Fila donde esta posicionado el bloque en el tablero
     * @param columnInBoard Columna donde esta posicionado el bloque en el tablero
     */
    @Override
    public void setActive(Board board, int rowInBoard, int columnInBoard) {
        if(!isActivated()){
            TetrisCell[][] piecesBoard = board.getSpecialPiecesBoard();
            int bottomCoordinate = getBottomCoordinate(piecesBoard, rowInBoard, columnInBoard);
            if(piecesBoard[rowInBoard][columnInBoard] != null){
                if(rowInBoard != bottomCoordinate){
                    board.getSpecialPiecesBoard()[rowInBoard][columnInBoard] = null;
                    board.getBoard()[rowInBoard][columnInBoard] = false;
                }
                board.getSpecialPiecesBoard()[bottomCoordinate][columnInBoard] = this;
                board.getBoard()[bottomCoordinate][columnInBoard] = true;
            }
            setActivated(true);
        }
    }

    private int getBottomCoordinate(TetrisCell[][] piecesBoard, int row, int column){
        int bottomCoordinate = row;
        while(bottomCoordinate + 1 < 20 && piecesBoard[bottomCoordinate + 1][column] == null){
            bottomCoordinate++;
        }
        return bottomCoordinate;
    }
}
