package dominio;

import java.awt.*;
import java.io.Serializable;

/**
 * Clase abstracta que representa a los bloques que conforman a las piezas y al tablero
 */
public abstract class TetrisCell implements Serializable {
    private Color pieceColor;
    private Color borderColor;
    private boolean isActivated = false;

    /**
     * Metodo que define como se comporta cada bloque del tablero
     * @param board Tablero en el que se efectuaran los cambios dependiendo de la pieza
     * @param rowInBoard Fila donde esta posicionado el bloque en el tablero
     * @param columnInBoard Columna donde esta posicionado el bloque en el tablero
     */
    public abstract void setActive(Board board, int rowInBoard, int columnInBoard);

    public Color getPieceColor() {
        return pieceColor;
    }

    public void setPieceColor(Color pieceColor) {
        this.pieceColor = pieceColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }
}
