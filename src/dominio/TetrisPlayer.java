package dominio;

import java.awt.*;
import java.io.Serializable;

/**
 * Clase que agrupa diferentes metodos de diferentes clases para poder representar a un jugador
 */
public abstract class TetrisPlayer implements Serializable {

    private Color backgroundColor = Color.BLACK;
    private Board board;

    /**
     * Metodo que retorna los puntos del jugador
     * @return int - puntos del jugador
     */
    public int getPoints(){
        return board.getPoints();
    }

    /**
     * Metodo que retorna el objeto del tablero de juego
     * @return Board - Tablero de juego
     */
    public Board getBoardObject() {return board;}

    /**
     * Metodo que retorna la matriz de booleanos que representa el tablero
     * @return boolean[][] Matriz del tablero de juego
     */
    public boolean[][] getBoard(){
        return board.getBoard();
    }

    /**
     * Metodo que retorna la pieza que esta actualmente en movimiento
     * @return Piece - Pieza que estamos controlando actualmente
     */
    public Piece getMovingPiece(){
        return board.getMoving();
    }

    /**
     * Metodo para mover la pieza a la izquierda
     */
    public void moveLeft(){
        board.movePieceLeft();
    }

    /**
     * Metodo para mover la pieza a la derecha
     */
    public void moveRight(){
        board.movePieceRight();
    }

    /**
     * Metodo para rotar la pieza a la derecha
     */
    public void rotate(){
        board.rotate();
    }

    /**
     * Metodo para mover la pieza hacia abajo
     */
    public void moveDown() {
        board.movePieceDown();
    }

    /**
     * Metodo para pausar el tablero del jugador
     */
    public void pause(){
        board.pause();
    }

    /**
     * Metodo para resumir el tablero del jugador
     */
    public void resume(){
        board.resume();
    }

    /**
     * Metodo para asignar el tablero al jugador
     * @param type Tipo de tablero que se le asignara al jugador
     */
    public void setBoard(String type) {
        try{
            this.board = (Board)Class.forName(type).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ignored){}
    }

    /**
     * Metodo que retorna la matriz de objetos TetrisCell que representa el tablero
     * @return TetrisCell[][] Matriz del tablero de juego
     */
    public TetrisCell[][] getSpecialPiecesBoard(){
        return board.getSpecialPiecesBoard();
    }

    /**
     * Metodo que retorna el color del tablero del juego
     * @return Color del fondo del tablero de juego
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Metodo que asigna un nuevo color al tablero de juego
     * @param backgroundColor Nuevo color a ponerle al fondo del tablero de juego
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Betodo que dice si un jugador perdio la partida
     * @return true si perdio, false de lo contrario
     */
    public boolean hasLost() {
        return board.hasLost();
    }

    /**
     * Metodo abstracto que dice si el jugador marco un record o no
     * @return true si se marco record, false de lo contrario
     */
    public abstract boolean isBestScore();
}
