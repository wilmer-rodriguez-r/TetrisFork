package pruebas;

import dominio.*;
import org.junit.Test;

import java.io.Serializable;

import static org.junit.Assert.*;

public class TetrisPlayerTest implements Serializable {

    Board board = new SlowBoard(0);
    Board newBoard = new SlowBoard(1);
    Board anotherNewBoard = new SlowBoard(2);
    Board anotherOtherNewBoard = new SlowBoard(3);

    @Test
    public void deberiaMoverBienALaDerecha(){
        Piece moving = board.getMoving();
        assertEquals(moving.getXPos(), 2);
        assertEquals(moving.getYPos(), 5);
        board.movePieceRight();
        assertEquals(moving.getXPos(), 2);
        assertEquals(moving.getYPos(), 6);
    }

    @Test
    public void deberiaMoverBienIzquierda() {
        Piece moving = board.getMoving();
        assertEquals(moving.getXPos(), 2);
        assertEquals(moving.getYPos(), 5);
        board.movePieceLeft();
        assertEquals(moving.getXPos(), 2);
        assertEquals(moving.getYPos(), 4);
    }

    @Test
    public void deberiaMoverBienAbajo() {
        Piece moving = board.getMoving();
        assertEquals(moving.getXPos(), 2);
        assertEquals(moving.getYPos(), 5);
        board.movePieceDown();
        assertEquals(moving.getXPos(), 3);
        assertEquals(moving.getYPos(), 5);
    }

    @Test
    public void deberiaRotarBien() {
        Piece moving = board.getMoving();
        boolean[][] rotated = {{false, false, false, false},
                                {false, true, true, true},
                                {false, true, false, false},
                                {false, false, false, false}};
        board.rotate();
        assertArrayEquals(moving.getForm(), rotated);
    }

    @Test
    public void deberiaEliminarBienLasFilas() {
        boolean[][] boardState = board.getBoard();
        boardState[5] = new boolean[]{true, true, true, true, true, true, true, true, true, true};
        boolean[][] boardStateShouldBe = new boolean[20][10];
        boardStateShouldBe[5] = new boolean[]{true, true, true, true, true, true, true, true, true, true};
        assertArrayEquals(boardState, boardStateShouldBe);
        board.deleteRow();
        boardState = board.getBoard();
        boolean[][] newBoardState = new boolean[20][10];
        assertArrayEquals(boardState, newBoardState);
    }

    @Test
    public void deberiaGuardarBienElEstado() {
        board.getBoard()[5][6] = true;
        boolean[][] boardState = board.getBoard();
        board.movePieceDown();
        boardState = board.getBoard();
        boolean[][] shouldStateBe = new boolean[20][10];
        shouldStateBe[2][6] = true;
        shouldStateBe[3][6] = true;
        shouldStateBe[4][6] = true;
        shouldStateBe[4][7] = true;
        shouldStateBe[5][6] = true;
        assertArrayEquals(boardState, shouldStateBe);
    }

    @Test
    public void deberiaDecirSiSePerdioUnJuego() {
        TetrisPlayer board = new HumanPlayer("dominio.SlowBoard");
        assertFalse(board.hasLost());
        board.getBoard()[0][1] = true;
        assertTrue(board.hasLost());
        board.getBoard()[0][1] = false;
        assertFalse(board.hasLost());
        board.getBoard()[0][2] = true;
        assertTrue(board.hasLost());
        board.getBoard()[0][5] = true;
        assertTrue(board.hasLost());
        board.getBoard()[0][2] = false;
        assertTrue(board.hasLost());
        board.getBoard()[0][5] = false;
        assertFalse(board.hasLost());
    }

    @Test
    public void deberiaResumirCorrectamenteElJuego() throws InterruptedException {
        Piece moving = board.getMoving();
        assertEquals(moving.getXPos(), 2);
        assertEquals(moving.getYPos(), 5);
        int points = board.getPoints();
        assertEquals(points, 0);
        board.resume();
        Thread.sleep(10);
        moving = board.getMoving();
        assertNotEquals(moving.getXPos(), 2);
        assertEquals(moving.getYPos(), 5);
        points = board.getPoints();
        assertNotEquals(points, 0);
    }

    @Test
    public void deberiaCorrerCorrectamenteLasPiezasFueraDelTablero() {
        for(int i = 0; i < 20; i++){
            board.movePieceLeft();
        }
        board.rotate();
        Piece moving = board.getMoving();
        assertEquals(moving.getXPos(), 2);
        assertEquals(moving.getYPos(), -1);
    }

    @Test
    public void deberiaAcelerarseElTableroAcelerado() throws InterruptedException {
        Board accBoard = new AcceleratedBoard(0);
        int vel = accBoard.getFallingVelocity();
        assertEquals(vel, 1);
        accBoard.resume();
        Thread.sleep(2);
        vel = accBoard.getFallingVelocity();
        assertNotEquals(1, vel);
    }

    @Test
    public void deberianComportarseBienLasUseless() {
        boolean[][] boardState = newBoard.getBoard();
        assertArrayEquals(boardState, new boolean[20][10]);
        newBoard.movePieceDown();
        boardState = newBoard.getBoard();
        assertArrayEquals(boardState, new boolean[20][10]);
        TetrisCell[][] piecesState = newBoard.getSpecialPiecesBoard();
        assertNotEquals(piecesState, new TetrisCell[20][10]);
    }

    @Test
    public void noDDeberianEliminarseLasFilasConUsseless() {
        newBoard.getBoard()[19] = new boolean[]{true, true, true, true, true, true, true, true, true, true};
        newBoard.movePieceDown();
        assertArrayEquals(newBoard.getBoard()[19], new boolean[]{false, false, true, true, true, true, true, true, true, true});
        TetrisCell[][] piecesState = newBoard.getSpecialPiecesBoard();
        assertNotEquals(piecesState[19][0], null);
        assertNotEquals(piecesState[19][1], null);
        assertNull(piecesState[19][2]);
    }

    @Test
    public void deberianDesaparecerLasPiezasBombConLasPiezasDeAlRededor() {
        boolean[][] boardState = anotherNewBoard.getBoard();
        assertArrayEquals(boardState, new boolean[20][10]);
        anotherNewBoard.getBoard()[19][2] = true;
        anotherNewBoard.getSpecialPiecesBoard()[19][2] = new Classic();
        anotherNewBoard.movePieceDown();
        boardState = anotherNewBoard.getBoard();
        assertArrayEquals(boardState, new boolean[20][10]);
        TetrisCell[][] piecesState = anotherNewBoard.getSpecialPiecesBoard();
        assertArrayEquals(piecesState, new TetrisCell[20][10]);
        assertNull(piecesState[19][2]);
        assertFalse(boardState[19][2]);
    }

    @Test
    public void deberianManejarBienLasRotacionesFueraDelTablero() {
        boolean[][] rotated = {{false, false, false, false},
                {true, true, true, true},
                {false, false, false, false},
                {false, false, false, false}};
        anotherOtherNewBoard.rotate();
        Piece moving = anotherOtherNewBoard.getMoving();
        assertEquals(moving.getYPos(), 0);
        assertArrayEquals(moving.getForm(), rotated);
        assertEquals(moving.getXPos(), 16);
    }

    @Test
    public void deberiaComportarseBienLasPiezasWinner() {
        anotherOtherNewBoard.rotate();
        anotherOtherNewBoard.getBoard()[19][0] = true;
        anotherOtherNewBoard.getSpecialPiecesBoard()[19][0] = new Classic();
        anotherOtherNewBoard.movePieceDown();
        anotherOtherNewBoard.movePieceDown();
        anotherOtherNewBoard.movePieceDown();
        assertTrue(anotherOtherNewBoard.getBoard()[19][0]);
        assertTrue(anotherOtherNewBoard.getBoard()[19][1]);
        assertTrue(anotherOtherNewBoard.getBoard()[19][2]);
        assertTrue(anotherOtherNewBoard.getBoard()[19][3]);
        assertTrue(anotherOtherNewBoard.getBoard()[18][0]);
    }

    @Test
    public void deberiaAlternarseBienLaPausaYResumen() {
        board.pauseResume();
        assertTrue(board.isPaused());
        board.pauseResume();
        assertFalse(board.isPaused());
        board.pauseResume();
        assertTrue(board.isPaused());
        board.pauseResume();
        assertFalse(board.isPaused());
    }
}
