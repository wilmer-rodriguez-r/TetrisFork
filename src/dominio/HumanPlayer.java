package dominio;

import persistencia.IOGameData;
import presentacion.TetrisGUI;

import java.awt.*;
import java.io.Serializable;

/**
 * Clase encargada de representar a los jugadores humanos
 */
public class HumanPlayer extends TetrisPlayer implements Serializable {

    private String nickname;
    private long bestScore;

    /**
     * Constructor de juegadores humanos
     * @param type String dice el nombre del tipo de tablero que tendra el jugador
     */
    public HumanPlayer(String type) {
        setBoard(type);
        this.nickname = TetrisGUI.User;

        if(!nickname.equals("")){
            String[] userInfo = IOGameData.getUserInfo(nickname, "UsersColors.txt");
            assert userInfo != null;
            int r = Integer.parseInt(userInfo[1]);
            int g = Integer.parseInt(userInfo[2]);
            int b = Integer.parseInt(userInfo[3]);
            setBackgroundColor(new Color(r,g,b));

            userInfo = IOGameData.getUserInfo(nickname, "UsersTopScores.txt");
            assert userInfo != null;
            bestScore = Long.parseLong(userInfo[1]);
        }
    }

    /**
     * Metodo que ademas de decir si un jugador perdio, guarda los puntos de este si se marco un record
     * @return true si perdio, false de lo contrario
     */
    public boolean hasLost(){
        boolean lost = super.hasLost();
        if(lost && isBestScore()){
            IOGameData.newRecord(nickname, getPoints());
        }
        return lost;
    }

    /**
     * Metodo que dice si un el jugador marco un nuevo record
     * @return true si los puntos de la partida son mayores a su record, false de lo contrario
     */
    @Override
    public boolean isBestScore() {
        return getPoints() > bestScore && nickname != null;
    }
}
