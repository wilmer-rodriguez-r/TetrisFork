package dominio;

import java.io.Serializable;

/**
 * Clase que representa el tablero en donde las piezas caen a velocidad constante
 */
public class SlowBoard extends Board implements Serializable {

    /**
     * Constructor de los tableros lentos
     */
    public SlowBoard(){
        spawnPiece();
    }

    // Este metodo se usa unicamente para las pruebas
    public SlowBoard(int i){
        if(i == 0){
            setUpBoardTest1();
        } else if(i == 1){
            setUpBoardTest2();
        } else if(i == 2){
            setUpBoardTest3();
        } else {
            setUpBoardTest4();
        }

    }
}
