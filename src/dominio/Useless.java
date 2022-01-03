package dominio;

import java.awt.*;
import java.io.Serializable;

/**
 * Clase que representa cada bloque de la pieza que no hace nada
 */
public class Useless extends TetrisCell implements Serializable {

    /**
     * Constructor de la pieza que no hace nada
     */
    public Useless(){
        setBorderColor(Color.LIGHT_GRAY);
    }

    /**
     * Metodo que activa el comportamiento del bloque de la pieza para que la columna no se elimine
     * @param board Tablero en el que se efectuaran los cambios dependiendo de la pieza
     * @param rowInBoard Fila donde esta posicionado el bloque en el tablero
     * @param columnInBoard Columna donde esta posicionado el bloque en el tablero
     */
    @Override
    public void setActive(Board board, int rowInBoard, int columnInBoard) {
        if(!isActivated()){
            boolean[][] boardState = board.getBoard();
            boardState[rowInBoard][columnInBoard] = false;
            setActivated(true);
        }
    }
}
