package dominio;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Clase encargada de definir como se comportan los poderes que detengan las piezas
 */
public class StopPiece extends Buff implements Serializable {

    private final Timer timer = new Timer();
    private int pieceXPos;

    /**
     * Constructor de objetos StopPiece
     */
    public StopPiece(){
        setImage("./resources/StopPieceBuffImage.png");
        setGoodPower(true);
    }

    /**
     * Metodo que define como se comporta el poder, para este se hace que los puntos se demoren
     * mas en subir y que las piezas paren hasta que se de la orden de bajar
     * @param game Tablero en el que esta actuando el buffo
     */
    @Override
    public void activate(TetrisPlayer game) {
        TimerTask useBuff = new TimerTask() {
            @Override
            public void run() {
                Timer gameTimer = game.getBoardObject().getTimer();
                gameTimer.cancel();
                pieceXPos = game.getMovingPiece().getXPos();
                stop(game);
            }
        };
        timer.schedule(useBuff, 0);
    }

    /**
     * Metodo que hace que la pieza pare y los puntos se alenten
     * @param game Tablero en el que actuara el poder
     */
    private void stop(TetrisPlayer game){
        TimerTask increasePoints = new TimerTask() {
            @Override
            public void run() {
                game.getBoardObject().setPoints(game.getBoardObject().getPoints() + 1);
            }
        };
        TimerTask checkDownMovement = new TimerTask() {
            @Override
            public void run() {
                if(game.getMovingPiece().getXPos() != pieceXPos){
                    game.pause();
                    game.resume();
                }
            }
        };
        Timer gameTimer = new Timer();
        game.getBoardObject().setTimer(gameTimer);
        gameTimer.scheduleAtFixedRate(increasePoints, 0, 5000);
        gameTimer.scheduleAtFixedRate(checkDownMovement, 0, 10);
    }
}
