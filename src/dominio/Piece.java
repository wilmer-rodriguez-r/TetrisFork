package dominio;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Clase abstracta que representa a las piezas del Tetris
 */
public abstract class Piece implements Serializable {

    private final ArrayList<TetrisCell> possiblePieceTypes = new ArrayList<>(Arrays.asList(new Classic(), new Useless(), new Winner(), new Bomb()));

    private int xPos;
    private int yPos;
    private boolean[][] pieceForm = new boolean[4][4];
    private TetrisCell[][] pieceType = new TetrisCell[4][4];

    private Color color;

    /**
     * Metodo que mueve la pieza a la derecha
     */
    public void moveRight(){
        yPos++;
    }

    /**
     * Metodo que mueve la pieza a la izquierda
     */
    public void moveLeft(){
        yPos--;
    }

    /**
     * Metodo que mueve la pieza hacia abajo
     */
    public void moveDown(){
        xPos++;
    }

    /**
     * Metodo que mueve la pieza hacia arriba
     */
    public void moveUp(){
        xPos--;
    }

    /**
     * Metodo que hace rotar la pieza a la derecha
     */
    public void rotate(){
        pieceForm = getNextRotation();
        pieceType = getNextCellRotations();
    }

    /**
     * Metodo encargado de encontrar la sigueinte rotacion de la pieza sobre la matriz de boolianos pieceForm
     * @return nextRotation Matriz 4x4 que muestra la siguiente rotacion de la pieza
     */
    public boolean[][] getNextRotation(){
        boolean[][] nextRotation = new boolean[pieceForm.length][pieceForm.length];
        int N = pieceForm.length;
        for(int i = 0; i < N / 2; i++){
            for(int j = i; j < N - i - 1; j++){
                boolean temp = pieceForm[i][j];
                nextRotation[i][j] = pieceForm[N - 1 - j][i];
                nextRotation[N - 1 - j][i] = pieceForm[N - 1 - i][N - 1 - j];
                nextRotation[N - 1 - i][N - 1 - j] = pieceForm[j][N - 1 - i];
                nextRotation[j][N - 1 - i] = temp;
            }
        }
        return nextRotation;
    }

    /**
     * Metodo encargado de encontrar la sigueinte rotacion de la pieza sobre la matriz de ojjetos TetrisCell pieceForm
     * @return nextRotation Matriz 4x4 que muestra la siguiente rotacion de la pieza
     */
    private TetrisCell[][] getNextCellRotations(){
        TetrisCell[][] nextRotating = new TetrisCell[pieceForm.length][pieceForm.length];
        int N = pieceForm.length;
        for(int i = 0; i < N / 2; i++){
            for(int j = i; j < N - i - 1; j++){
                TetrisCell temp2 = pieceType[i][j];
                nextRotating[i][j] = pieceType[N - 1 - j][i];
                nextRotating[N - 1 - j][i] = pieceType[N - 1 - i][N - 1 - j];
                nextRotating[N - 1 - i][N - 1 - j] = pieceType[j][N - 1 - i];
                nextRotating[j][N - 1 - i] = temp2;
            }
        }
        return nextRotating;
    }

    public boolean[][] getForm(){
        return pieceForm;
    }

    public TetrisCell[][] getPieceType() { return pieceType; }

    public ArrayList<TetrisCell> getPossiblePieceTypes() {
        return possiblePieceTypes;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    public void setYPos(int yPos) {
        this.yPos = yPos;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
