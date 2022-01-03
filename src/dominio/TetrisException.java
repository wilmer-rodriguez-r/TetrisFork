package dominio;

import java.io.Serializable;

/**
 * Clase de extencion de excepciones para tener un manejo mas personalizado de errores
 */
public class TetrisException extends Exception implements Serializable {

    public static final String CANT_GO_DOWN = "The piece has reached the bottom of the board";

    public static final String CANT_GO_RIGHT = "The piece has reached the extreme right of the board";

    public static final String CANT_GO_LEFT = "The piece has reached the extreme left of the board";

    public static final String ROTATION_BOUNDS_DOWN = "When rotating the piece exited the board on the down wall";

    public static final String ROTATION_BOUNDS_LEFT = "When rotating the piece exited the board on the left wall";

    public static final String ROTATION_BOUNDS_RIGHT = "When rotating the piece exited the board on the right wall";

    public static final String ROTATION_BOUNDS_UP = "When rotating the piece exited the board on the upper wall";

    /**
     * Constructor de excepciones personalizaadas del juego
     * @param message Mensaje que lanzara la excepcion
     */
    public TetrisException(String message){
        super(message);
    }

}
