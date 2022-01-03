package dominio;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Clase encargada de definir como se comportan los poderes que detengan el tiempo del oponente
 */
public class StopTime extends Buff implements Serializable {

    /**
     * Constructor de objetos StopTime
     */
    public StopTime(){
        setImage("./resources/StopTimeBuffImage.png");
        setGoodPower(false);
    }

    /**
     * Metodo que define como se comporta el poder, para este se hace que los puntos del oponente
     * dejen de incrementarse por 3 segundos
     * @param game Tablero en el que esta actuando el buffo
     */
    @Override
    public void activate(TetrisPlayer game) {
        Timer timer = new Timer();
        TimerTask useBuff = new TimerTask() {
            @Override
            public void run() {
                Timer gameTimer = game.getBoardObject().getTimer();
                gameTimer.cancel();
            }
        };
        timer.schedule(useBuff, 0);
        timer.schedule(allNormal(game), 2999);
    }

    /**
     * Metodo que hace que todo en el tablero vuelva a la normalidad
     * @param game Juego en el que todo el tablero volvera a la normalidad
     * @return Tarea para que el timer programe la llamada del metodo
     */
    private TimerTask allNormal(TetrisPlayer game){
        return new TimerTask() {
            @Override
            public void run() {
                game.pause();
                game.resume();
            }
        };
    }
}
