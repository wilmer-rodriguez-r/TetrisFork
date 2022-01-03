package dominio;

import java.io.Serializable;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Clase que representa al jugador principiante de la maquina
 */
public class Noob extends PcPlayer implements Serializable {

    private transient Timer timer = new Timer();

    /**
     * Constructor de juegadores principiantes
     * @param type Nombre de la clase del tipo de tablero que tendra el jugador
     */
    public Noob(String type) {
        setBoard(type);
        autoMove();
    }

    /**
     * Metodo que hace que el jugador se mueva en una direccion aleatoria
     */
    private void randomMove(){
        Random rand = new Random();
        int move = rand.nextInt(4);
        switch (move){
            case 0:
                getBoardObject().movePieceDown();
                break;
            case 1:
                getBoardObject().movePieceLeft();
                break;
            case 2:
                getBoardObject().movePieceRight();
                break;
            case 3:
                getBoardObject().rotate();
                break;
        }
    }

    /**
     * Metodo que pausa el juego de los principiantes
     */
    @Override
    public void pause(){
        super.pause();
        timer.cancel();
    }

    /**
     * Metodo que resume el juego de los principiantes
     */
    @Override
    public void resume(){
        super.resume();
        autoMove();
    }

    /**
     * Metodo que hace que el jugador se mueva aleatoriamente cada 900 milisegundos
     */
    private void autoMove(){
        TimerTask autoMove = new TimerTask() {
            @Override
            public void run() {
                if(!getBoardObject().isPaused()){
                    randomMove();
                }
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(autoMove, 0, 900);
    }
}
