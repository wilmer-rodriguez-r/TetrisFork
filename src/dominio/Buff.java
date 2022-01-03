package dominio;

import java.io.Serializable;
import java.util.Random;

/**
 * Clase abstracta que representa a los poderes del Tetris en conjunto
 */
public abstract class Buff implements Serializable {

    private String image;
    private boolean isGoodPower;

    private Random rand = new Random();

    /**
     * Metodo que spawnea el buff en una posicion cualquiera del tablero dadp
     * @param board Tablero en el que se spawneara el buffo
     */
    public void spawn(Board board){
        int xPosPiece = rand.nextInt(3) + 1;
        int yPosPiece = rand.nextInt(10);

        while(!canSpawn(xPosPiece, yPosPiece, board)){
            xPosPiece = rand.nextInt(4);
            yPosPiece = rand.nextInt(10);
        }

        board.setxPosBuff(xPosPiece);
        board.setyPosBuff(yPosPiece);
    }

    /**
     * Metodo que decide si se puede o no spawnear el buffo en la posicion dada del tablero dado
     * @param xPosPiece Fila del tablero donde se quiere spawnear el buffo
     * @param yPosPiece Columna del tablero donde se quiere spawnear el buffo
     * @param board Tablero donde se quiere spawnear el buffo
     * @return
     */
    private boolean canSpawn(int xPosPiece, int yPosPiece, Board board){
        return !board.getBoard()[xPosPiece][yPosPiece] && board.getSpecialPiecesBoard()[xPosPiece][yPosPiece] == null;
    }

    /**
     * Metodo abstracto en el que se definiran como se comportan los buffos
     * @param game Tablero en el que esta actuando el buffo
     */
    public abstract void activate(TetrisPlayer game);

    /**
     * Metodo que asignara la imagen del buffo
     * @param image Camino hacia la imagen
     */
    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setGoodPower(boolean goodPower) {
        isGoodPower = goodPower;
    }

    public boolean isGoodPower() {
        return isGoodPower;
    }
}
