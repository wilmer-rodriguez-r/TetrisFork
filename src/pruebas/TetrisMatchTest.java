package pruebas;

import dominio.*;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import presentacion.TetrisGUI;

import static org.junit.Assert.*;

import java.awt.*;
import java.io.Serializable;

public class TetrisMatchTest implements Serializable{

    TetrisMatch matchHumanVsPc = new TetrisMatch(1, 1, "dominio.SlowBoard", "dominio.Noob");
    TetrisMatch matchHumanVsHuman = new TetrisMatch(2, 0, "dominio.SlowBoard", "dominio.Noob");
    TetrisMatch matchHumanVsExpertPc = new TetrisMatch(1, 1, "dominio.SlowBoard", "dominio.Expert");

    @BeforeEach
    public void prepareTests(){
        matchHumanVsPc = new TetrisMatch(1, 1, "dominio.SlowBoard", "dominio.Noob");
        matchHumanVsHuman = new TetrisMatch(2, 0, "dominio.SlowBoard", "dominio.Noob");
        matchHumanVsExpertPc = new TetrisMatch(1, 1, "dominio.SlowBoard", "dominio.Expert");
    }

    @Test
    public void deberianCrearseBienLasPartidas(){
        assertEquals(2, matchHumanVsPc.getGames().size());
        assertEquals(2, matchHumanVsHuman.getGames().size());
        assertEquals(2, matchHumanVsExpertPc.getGames().size());
        assertTrue(matchHumanVsPc.getGames().get(0) instanceof HumanPlayer);
        assertTrue(matchHumanVsPc.getGames().get(1) instanceof Noob);
        assertTrue(matchHumanVsHuman.getGames().get(0) instanceof HumanPlayer);
        assertTrue(matchHumanVsHuman.getGames().get(1) instanceof HumanPlayer);
        assertNotNull(matchHumanVsPc.getGames().get(0).getBoardObject());
        assertNotNull(matchHumanVsPc.getGames().get(1).getBoardObject());
        assertNotNull(matchHumanVsHuman.getGames().get(0).getBoardObject());
        assertNotNull(matchHumanVsHuman.getGames().get(1).getBoardObject());
        assertTrue(matchHumanVsExpertPc.getGames().get(0) instanceof HumanPlayer);
        assertTrue(matchHumanVsExpertPc.getGames().get(1) instanceof Expert);
        assertNotNull(matchHumanVsExpertPc.getGames().get(0).getBoardObject());
        assertNotNull(matchHumanVsExpertPc.getGames().get(1).getBoardObject());
    }

    @Test
    public void deberiaRetornarCorrectamenteLaInformacionDeUnaPartida() {
        assertEquals(matchHumanVsHuman.getPcPlayers(), 0);
        assertEquals(matchHumanVsHuman.getType(), "dominio.SlowBoard");
        assertEquals(matchHumanVsHuman.getTetrisGames(), 2);
        assertEquals(matchHumanVsHuman.getPcExperience(), "dominio.Noob");
        assertEquals(matchHumanVsPc.getPcPlayers(), 1);
        assertEquals(matchHumanVsPc.getType(), "dominio.SlowBoard");
        assertEquals(matchHumanVsPc.getTetrisGames(), 1);
        assertEquals(matchHumanVsPc.getPcExperience(), "dominio.Noob");
        assertEquals(matchHumanVsExpertPc.getPcPlayers(), 1);
        assertEquals(matchHumanVsExpertPc.getType(), "dominio.SlowBoard");
        assertEquals(matchHumanVsExpertPc.getTetrisGames(), 1);
        assertEquals(matchHumanVsExpertPc.getPcExperience(), "dominio.Expert");
    }

    @Test
    public void deberiaPausarTodosLosJuegos() {
        matchHumanVsHuman.pause();
        for(TetrisPlayer game : matchHumanVsHuman.getGames()){
            assertTrue(game.getBoardObject().isPaused());
        }
        matchHumanVsPc.pause();
        for(TetrisPlayer game : matchHumanVsPc.getGames()){
            assertTrue(game.getBoardObject().isPaused());
        }
    }

    @Test
    public void deberiaRenaudarTodosLosJuegos() {
        matchHumanVsHuman.pause();
        for(TetrisPlayer game : matchHumanVsHuman.getGames()){
            assertTrue(game.getBoardObject().isPaused());
        }
        matchHumanVsHuman.resume();
        for(TetrisPlayer game : matchHumanVsHuman.getGames()){
            assertFalse(game.getBoardObject().isPaused());
        }
        matchHumanVsPc.pause();
        for(TetrisPlayer game : matchHumanVsPc.getGames()){
            assertTrue(game.getBoardObject().isPaused());
        }
        matchHumanVsPc.resume();
        for(TetrisPlayer game : matchHumanVsPc.getGames()){
            assertFalse(game.getBoardObject().isPaused());
        }
    }

    @Test
    public void deberiaIntercalarLaPausaYResumen() {
        matchHumanVsHuman.pauseResume();
        for(TetrisPlayer game : matchHumanVsHuman.getGames()){
            assertTrue(game.getBoardObject().isPaused());
        }
        matchHumanVsHuman.pauseResume();
        for(TetrisPlayer game : matchHumanVsHuman.getGames()){
            assertFalse(game.getBoardObject().isPaused());
        }
        matchHumanVsPc.pauseResume();
        for(TetrisPlayer game : matchHumanVsPc.getGames()){
            assertTrue(game.getBoardObject().isPaused());
        }
        matchHumanVsPc.pauseResume();
        for(TetrisPlayer game : matchHumanVsPc.getGames()){
            assertFalse(game.getBoardObject().isPaused());
        }
    }

    @Test
    public void deberiaUsarCorrectamenteLosBuffos() {
        matchHumanVsPc = new TetrisMatch(1, 1, "dominio.SlowBoard", "dominio.Noob");
        matchHumanVsHuman = new TetrisMatch(2, 0, "dominio.SlowBoard", "dominio.Noob");
        matchHumanVsHuman.getGames().get(0).getBoardObject().setBuffOnBoard(matchHumanVsHuman.getActualBuff());
        matchHumanVsHuman.useBuff();
        assertNull(matchHumanVsHuman.getActualBuff());
    }

    @Test
    public void deberiaCrearBienLosJugadoresIniciadaLaSesion() {
        TetrisGUI.User = "GirlyGirlGina";
        assertEquals(2, matchHumanVsPc.getGames().size());
        assertEquals(2, matchHumanVsHuman.getGames().size());
        assertTrue(matchHumanVsPc.getGames().get(0) instanceof HumanPlayer);
        assertTrue(matchHumanVsPc.getGames().get(1) instanceof Noob);
        assertTrue(matchHumanVsHuman.getGames().get(0) instanceof HumanPlayer);
        assertTrue(matchHumanVsHuman.getGames().get(1) instanceof HumanPlayer);
        assertNotNull(matchHumanVsPc.getGames().get(0).getBoardObject());
        assertNotNull(matchHumanVsPc.getGames().get(1).getBoardObject());
        assertNotNull(matchHumanVsHuman.getGames().get(0).getBoardObject());
        assertNotNull(matchHumanVsHuman.getGames().get(1).getBoardObject());
    }

    @Test
    public void deberiaComportrseBienElSlow() throws InterruptedException {
        Buff buff = new Slow();
        TetrisPlayer player = matchHumanVsHuman.getGames().get(0);
        matchHumanVsHuman.setActualBuff(buff);
        int xAntes = player.getMovingPiece().getXPos();
        buff.activate(player);
        Thread.sleep(10);
        assertEquals(player.getMovingPiece().getXPos(), xAntes + 1);
        Thread.sleep(1500);
        assertEquals(player.getMovingPiece().getXPos(), xAntes + 2);
    }

    @Test
    public void deberiaComportarseBienElStopTime() throws InterruptedException {
        Buff buff = new StopTime();
        TetrisPlayer player = matchHumanVsHuman.getGames().get(0);
        matchHumanVsHuman.setActualBuff(buff);
        int points = player.getPoints();
        buff.activate(player);
        assertEquals(points, player.getPoints());
        Thread.sleep(1000);
        assertEquals(points, player.getPoints());
        Thread.sleep(1000);
        assertEquals(points, player.getPoints());
    }

    @Test
    public void deberiaComportarseBienElStopPiece() throws InterruptedException {
        Buff buff = new StopPiece();
        TetrisPlayer player = matchHumanVsHuman.getGames().get(0);
        matchHumanVsHuman.setActualBuff(buff);
        int points = player.getPoints();
        int xAntes = player.getMovingPiece().getXPos();
        buff.activate(player);
        Thread.sleep(10);
        assertEquals(points + 1, player.getPoints());
        assertEquals(player.getMovingPiece().getXPos(), xAntes);
        Thread.sleep(5100);
        assertEquals(points + 2, player.getPoints());
        assertEquals(player.getMovingPiece().getXPos(), xAntes);
    }

    @Test
    public void deberiaDuplicarElTiempoElTimeX2() throws InterruptedException {
        Buff buff = new TimeX2();
        TetrisPlayer player = matchHumanVsHuman.getGames().get(0);
        matchHumanVsHuman.setActualBuff(buff);
        int xAntes = player.getMovingPiece().getXPos();
        buff.activate(player);
        Thread.sleep(50);
        assertEquals(xAntes + 1, player.getMovingPiece().getXPos());
        Thread.sleep(600);
        assertEquals(xAntes + 2, player.getMovingPiece().getXPos());
    }

    @Test
    public void deberiaDecirBienLosPuntosDeUnJuego() throws InterruptedException {
        TetrisPlayer player = matchHumanVsHuman.getGames().get(0);
        assertEquals(player.getPoints(), 1);
        Thread.sleep(1000);
        assertEquals(player.getPoints(), 2);
    }

    @Test
    public void deberiaDecirBienElTablero() {
        TetrisPlayer player = matchHumanVsHuman.getGames().get(0);
        assertArrayEquals(player.getBoard(), new boolean[20][10]);
        assertArrayEquals(player.getSpecialPiecesBoard(), new TetrisCell[20][10]);
        player.getBoard()[0][0] = true;
        TetrisCell buff = new Classic();
        player.getSpecialPiecesBoard()[0][0] = buff;
        boolean[][] boardState = new boolean[20][10];
        TetrisCell[][] pieceBoardState = new TetrisCell[20][10];
        pieceBoardState[0][0] = buff;
        boardState[0][0] = true;
        assertArrayEquals(boardState, player.getBoard());
        assertArrayEquals(pieceBoardState, player.getSpecialPiecesBoard());
    }

    @Test
    public void deberiaGuardarBienLosColores() {
        TetrisGUI.User = "";
        TetrisPlayer player = new HumanPlayer("dominio.SlowBoard");
        assertEquals(player.getBackgroundColor(), Color.BLACK);
        TetrisGUI.User = "Gerber";
        player = new HumanPlayer("dominio.SlowBoard");
        assertEquals(player.getBackgroundColor(), new Color(153, 0, 0));
        TetrisGUI.User = "SuperJose";
        player = new HumanPlayer("dominio.SlowBoard");
        assertEquals(player.getBackgroundColor(), new Color(0, 0, 153));
    }

    @AfterEach
    public void prepareNext(){
        matchHumanVsPc = new TetrisMatch(1, 1, "dominio.SlowBoard", "dominio.Noob");
        matchHumanVsHuman = new TetrisMatch(2, 0, "dominio.SlowBoard", "dominio.Noob");
        matchHumanVsExpertPc = new TetrisMatch(1, 1, "dominio.SlowBoard", "dominio.Expert");
    }
}
