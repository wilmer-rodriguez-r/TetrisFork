package dominio;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Clase que representa el poder que hace que la pieza baje mas lento Slow
 */
public class Slow extends Buff implements Serializable {

    /**
     * Constructor de poderes Slow
     */
    public Slow(){
        setImage("./resources/SlowBuffImage.png");
        setGoodPower(true);
    }

    /**
     * Metodo que define como se comporta Slow, cuando el poder se activa la caida de los bloque sera mas lenta por 3 segundos
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
                slowFall(game);
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

    /**
     * Metodo que cambia las acciones del juego para lograr el efecto de que la pieza caiga mas lento
     * @param game Tablero en el que actuara el buffo
     */
    private void slowFall(TetrisPlayer game){
        TimerTask increasePoints = new TimerTask() {
            @Override
            public void run() {
                game.getBoardObject().setPoints(game.getBoardObject().getPoints() + 1);
            }
        };
        TimerTask pushDown = new TimerTask(){
            @Override
            public void run() {
                try{
                    game.moveDown();
                } catch (Exception ignored){}
            }
        };
        Timer gameTimer = new Timer();
        game.getBoardObject().setTimer(gameTimer);
        gameTimer.scheduleAtFixedRate(increasePoints, 0, 1000);
        gameTimer.scheduleAtFixedRate(pushDown, 0, 1500L);
    }
}
