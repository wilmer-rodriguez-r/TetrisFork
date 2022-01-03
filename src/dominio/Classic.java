package dominio;

import java.awt.*;
import java.io.Serializable;

/**
 * Clase encargada de representar los bloques de la pieza clasica
 */
public class Classic extends TetrisCell implements Serializable {

    /**
     * Constructor de los bloques clasicos
     */
    public Classic(){
        setBorderColor(Color.BLACK);
    }

    /**
     * Metodo que define como se comporta la pieza, al activarla esta no hace nada
     * @param board Tablero en el que se esta guardando la pieza
     * @param rowInBoard Fila donde esta posicionado el bloque en el tablero
     * @param columnInBoard Columna donde esta posicionado el bloque en el tablero
     */
    @Override
    public void setActive(Board board, int rowInBoard, int columnInBoard) {
        if(!isActivated()){
            setActivated(true);
        }
    }
}
