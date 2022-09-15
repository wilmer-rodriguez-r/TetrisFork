package dominio;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Clase que representa al tablero donde las piezas aceleran con el tiempo
 */
public class AcceleratedBoard extends Board implements Serializable {

    private transient Timer timer = new Timer();
    private int currentVelocity;

    /**
     * Constructor del tablero acelerado
     */
    public AcceleratedBoard(){
        currentVelocity = 0;
        spawnPiece();
        speedUp();
    }

    /**
     * Constructor del tablero acelerado, se usa unicamente para las pruebas
     * @param i el parametro es solo para diferenciar del constructor comun
     */
    public AcceleratedBoard(int i){
        setUpBoardTest1();
        testAcceleration();
    }

    /**
     * Pausa el tablero
     */
    @Override
    public void pause() {
        super.pause();
        timer.cancel();
    }

    /**
     * Resume el juego
     */
    @Override
    public void resume() {
        super.resume();
        speedUp();
    }

    /**
     * Acelera el movimiento de las piezas hacia abajo
     */
    private void speedUp(){
        TimerTask increaseVelocity = new TimerTask() {
            @Override
            public void run() {
                if(!isPaused()){
                    pause();
                    currentVelocity++;
                    setFallingVelocity(currentVelocity);
                    AcceleratedBoard.super.resume();
                }
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(increaseVelocity, 0, 10000);
    }

    // Metodo solo para las pruebas
    public void testAcceleration(){
        TimerTask increaseVelocity = new TimerTask() {
            @Override
            public void run() {
                if(!isPaused()){
                    pause();
                    currentVelocity++;
                    setFallingVelocity(currentVelocity);
                    AcceleratedBoard.super.resume();
                }
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(increaseVelocity, 0, 1);
    }
}
