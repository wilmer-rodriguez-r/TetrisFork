package dominio;

import java.awt.*;
import java.io.Serializable;

/**
 * Clase que representa cada bloque de la pieza que explota
 */
public class Bomb extends TetrisCell implements Serializable {

    /**
     * Constructor del bloque que representa el bloque que explota
     */
    public Bomb(){
        setBorderColor(Color.RED);
    }

    /**
     * Metodo que define como se comporta la pieza, al activarla se destruyen los bloques cercanos y este
     * @param board Tablero en el que se esta guardando la pieza
     * @param rowInBoard Fila donde esta posicionado el bloque en el tablero
     * @param columnInBoard Columna donde esta posicionado el bloque en el tablero
     */
    @Override
    public void setActive(Board board, int rowInBoard, int columnInBoard) {
        if(!isActivated()){
            boolean[][] boardState = board.getBoard();
            TetrisCell[][] boardPiecesState = board.getSpecialPiecesBoard();
            for(int i = 1; i >= -1; i--){
                for(int j = 1; j >= -1; j--){
                    try{
                        if(i == 0 || j == 0){
                            boardState[rowInBoard + i][columnInBoard + j] = false;
                            boardPiecesState[rowInBoard + i][columnInBoard + j] = null;
                        }
                    } catch(ArrayIndexOutOfBoundsException ignored){}
                }
            }
            board.setCollidedDown(true);
            setActivated(true);
        }
    }
}
