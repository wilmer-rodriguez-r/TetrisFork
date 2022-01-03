package dominio;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Clase que define como se comporta el poder TimeX2 en el que se duplica la velocidad de bajada de las piezas
 */
public class TimeX2 extends Buff implements Serializable {

    /**
     * Constructor de los poderes TimeX2
     */
    public TimeX2(){
        setImage("./resources/TimeX2BuffImage.png");
        setGoodPower(false);
    }

    /**
     * Metodo que define como se comporta el poder, en este caso se duplica la velocidad de bajada de las piezas
     * @param game Tablero en el que esta actuando el buffo
     */
    @Override
    public void activate(TetrisPlayer game){
        Timer timer = new Timer();
        TimerTask useBuff = new TimerTask() {
            @Override
            public void run() {
                Timer gameTimer = game.getBoardObject().getTimer();
                gameTimer.cancel();
                doubleVelocity(game);
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
     * Metodo que arregla los timer para que la velocidad de bajada se duplique
     * @param game Tablero en el que esta actuando el buffo
     */
    private void doubleVelocity(TetrisPlayer game){
        TimerTask increasePoints = new TimerTask() {
            @Override
            public void run() {
                int doublePoints = game.getBoardObject().getPoints() + 2;
                game.getBoardObject().setPoints(doublePoints);
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
        gameTimer.scheduleAtFixedRate(pushDown, 0, 1200L/ (2L * game.getBoardObject().getFallingVelocity()));
    }
}
