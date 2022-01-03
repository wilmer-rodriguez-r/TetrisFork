package dominio;

import java.io.Serializable;

/**
 * Clase que representa a los jugadores de pc en general
 */
public abstract class PcPlayer extends TetrisPlayer implements Serializable {

    /**
     * Metodo que dice si un jugador de pc marco un record
     * @return Siemore se retornara falso ya que la computadora no puede marcar records
     */
    @Override
    public boolean isBestScore() {
        return false;
    }
}
