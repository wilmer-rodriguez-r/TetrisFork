package dominio;


import java.io.Serializable;
import java.util.*;

/**
 * Clase abstracta en la que se maneja la logica general de un tablero de Tetris
 */
public abstract class Board implements Serializable {

    private final ArrayList<Piece> possiblePieces = new ArrayList<>(Arrays.asList(new LPiece(), new SPiece(), new IPiece(), new TPiece(), new SquarePiece()));

    private Random rand = new Random();

    private boolean paused = false;

    private boolean[][] board = new boolean[20][10];
    private TetrisCell[][] specialPiecesBoard = new TetrisCell[20][10];
    private Piece moving;

    private int points = 0;
    private int fallingVelocity = 1;

    private boolean collidedDown;

    private transient Timer timer = new Timer();

    private Buff buffOnBoard;
    private int xPosBuff = Integer.MAX_VALUE;
    private int yPosBuff = Integer.MAX_VALUE;

    /**
     * Metodo encargado de guardar la posicion de la pieza en el tablero cuando ya toca el fondo
     */
    public void saveState(){
        int size = moving.getForm().length;
        if(!canMove('D')){
            for(int i = size - 1; i >= 0; i--){
                for(int j = 0; j < size; j++){
                    if(moving.getForm()[i][j]){
                        board[i + moving.getXPos()][j + moving.getYPos()] = true;
                        specialPiecesBoard[i + moving.getXPos()][j + moving.getYPos()] = moving.getPieceType()[i][j];
                        specialPiecesBoard[i + moving.getXPos()][j + moving.getYPos()].setActive(this, i + moving.getXPos(), j + moving.getYPos());
                    }
                }
            }
        }
        deleteRow();
        if((!canMove('D') && !hasLost())){
            spawnPiece();
        }
    }

    /**
     * Metodo encargado de eliminar la fila cuando se llena
     */
    public void deleteRow(){
        for(int i = 0; i < board.length; i++){
            boolean filledRow = true;
            for(int j = 0; j < board[0].length; j++){
                if (!board[i][j]) {
                    filledRow = false;
                    break;
                }
            }
            if(filledRow){
                shiftDown(i);
            }
        }
    }

    /**
     * Metodo que hace que caigan las piezas de arriba cuando se elimina la fila i
     * @param i int - Numero de la fila que se eliminara del tablero
     */
    private void shiftDown(int i) {
        boolean[][] newBoard = new boolean[20][10];
        TetrisCell[][] newPiecesInBord = new TetrisCell[20][10];
        for(int w = 1; w < board.length; w++){
            for(int k = 0; k < board[0].length; k++){
                if(w <= i){
                    newBoard[w][k] = board[w - 1][k];
                    newPiecesInBord[w][k] = specialPiecesBoard[w - 1][k];
                } else {
                    newBoard[w][k] = board[w][k];
                    newPiecesInBord[w][k] = specialPiecesBoard[w][k];
                }
            }
        }
        points += 10;
        board = newBoard;
        specialPiecesBoard = newPiecesInBord;
    }

    /**
     * Metodo que hace que las piezas roten a la derecha
     */
    public void rotate(){
        try{
            if(canRotate() && !paused){
                moving.rotate();
            }
        } catch (TetrisException e){
            switch (e.getMessage()) {
                case TetrisException.ROTATION_BOUNDS_DOWN:
                    while (outOfBounds(moving.getNextRotation(), moving.getXPos(), moving.getYPos())) {
                        movePieceUp();
                    }
                    moving.rotate();
                    break;
                case TetrisException.ROTATION_BOUNDS_LEFT:
                    while (outOfBounds(moving.getNextRotation(), moving.getXPos(), moving.getYPos())) {
                        movePieceRight();
                    }
                    moving.rotate();
                    break;
                case TetrisException.ROTATION_BOUNDS_RIGHT:
                    while (outOfBounds(moving.getNextRotation(), moving.getXPos(), moving.getYPos())) {
                        movePieceLeft();
                    }
                    moving.rotate();
                    break;
                case TetrisException.ROTATION_BOUNDS_UP:
                    while (outOfBounds(moving.getNextRotation(), moving.getXPos(), moving.getYPos())) {
                        movePieceDown();
                    }
                    moving.rotate();
                    break;
            }
        }

    }

    /**
     * Metodo que mueve la piesa hacia arriba
     */
    private void movePieceUp(){
        moving.moveUp();
    }

    /**
     * Metodo que mueve la pieza hacia derecha
     */
    public void movePieceRight(){
        if(canMove('R') && !paused){
            moving.moveRight();
        }
    }

    /**
     * Metodo que mueve la piesa hacia izquierda
     */
    public void movePieceLeft(){
        if(canMove('L') && !paused){
            moving.moveLeft();
        }
    }

    /**
     * Metodo que mueve la piesa hacia abajo
     */
    public void movePieceDown(){
        if(canMove('D') && !paused){
            moving.moveDown();
        } else {
            saveState();
        }
    }

    /**
     * Metodo que revisa si el jugador perdio
     * @return true si se perdio la partida false de lo contrario
     */
    public boolean hasLost(){
        for(int x = 0; x < board[0].length; x++){
            if(board[0][x] || specialPiecesBoard[0][x] != null) {
                timer.cancel();
                timer.purge();
                return true;
            }
        }
        return false;
    }

    /**
     * Metodo que respaunea una pieza en una posicion adecuada
     */
    public void spawnPiece(){
        Piece possibleNext = possiblePieces.get(rand.nextInt(possiblePieces.size()));
        int xPosPiece = rand.nextInt(2);
        int yPosPiece = rand.nextInt(6);

        while(!canSpawn(possibleNext, xPosPiece, yPosPiece)){
            possibleNext = possiblePieces.get(rand.nextInt(5));
            xPosPiece = rand.nextInt(2);
            yPosPiece = rand.nextInt(6);
        }

        try{
            moving = possibleNext.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException ignored){}
        moving.setXPos(xPosPiece);
        moving.setYPos(yPosPiece);
        collidedDown = false;
    }

    /**
     * Metodo que mira si una pieza dada se puede spawnear en la posicion dada
     * @param piece Pieza con la intencion de spwnear
     * @param xPos Numero de la fila que se tiene la intencion de spawnear la pieza
     * @param yPos Numero de la fila que se tiene la intencion de spawnear la pieza
     * @return canSpawn - true si se puede spawnwar en la posicion dada, false de lo contrario
     */
    private boolean canSpawn(Piece piece, int xPos, int yPos){
        int size = piece.getForm().length;
        boolean[][] pieceForm = piece.getForm();
        boolean canSpawn = true;
        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                if (pieceForm[i][j] && (board[i + xPos][j + yPos] || specialPiecesBoard[i + xPos][j + yPos] != null)) {
                    canSpawn = false;
                    break;
                }
            }
        }
        return canSpawn;
    }

    /**
     * Metodo que dice si se puede mover en la direccion que se le pidio
     * @param direction Direccion en que se quiere move la pieza, los posibles valores del parametro son 'D', 'L' y 'R'
     * @return true si se puede mover en la direccion dada, false de lo contrario
     */
    private boolean canMove(char direction){
        boolean[][] pieceForm = moving.getForm();
        boolean canMove = true;
        for(int i = 0; i < pieceForm.length; i++){
            for(int j = 0; j < pieceForm.length; j++){
                switch (direction){
                    case 'D':
                        canMove = canMoveDown(i, j);
                        break;
                    case 'L':
                        canMove = canMoveLeft(i, j);
                        break;
                    case 'R':
                        canMove = canMoveRight(i, j);
                        break;
                }
                if(!canMove){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Metodo que mira si se puede mover la pieza a la derecha
     * @param i posicion x de la pieza en donde se encuentra un bloque que la componga
     * @param j posicion y de la pieza en donde se encuentra un bloque que la componga
     * @return true si se puede mover a la derecha, false de lo contrario
     */
    private boolean canMoveRight(int i, int j) {
        boolean[][] pieceForm = moving.getForm();
        boolean canMove = true;
        try{
            if(pieceForm[i][j] && j + moving.getYPos() + 1 >= board[0].length) throw new TetrisException(TetrisException.CANT_GO_RIGHT);
            if(pieceForm[i][j] && (board[i + moving.getXPos()][j + moving.getYPos() + 1] || specialPiecesBoard[i + moving.getXPos()][j + moving.getYPos() + 1] != null)){
                canMove = false;
            }
        } catch (TetrisException e){
            canMove = false;
        }
        return canMove;
    }

    /**
     * Metodo que mira si se puede mover la pieza a la izquierda
     * @param i posicion x de la pieza en donde se encuentra un bloque que la componga
     * @param j posicion y de la pieza en donde se encuentra un bloque que la componga
     * @return true si se puede mover a la izquierda, false de lo contrario
     */
    private boolean canMoveLeft(int i, int j) {
        boolean[][] pieceForm = moving.getForm();
        boolean canMove = true;
        try{
            if(pieceForm[i][j] && j + moving.getYPos() - 1 < 0) throw new TetrisException(TetrisException.CANT_GO_LEFT);
            if(pieceForm[i][j] && (board[i + moving.getXPos()][j + moving.getYPos() - 1] || specialPiecesBoard[i + moving.getXPos()][j + moving.getYPos() - 1] != null)){
                canMove = false;
            }
        } catch (TetrisException e){
            canMove = false;
        }
        return canMove;
    }

    /**
     * Metodo que mira si se puede mover la pieza hacia abajo
     * @param i posicion x de la pieza en donde se encuentra un bloque que la componga
     * @param j posicion y de la pieza en donde se encuentra un bloque que la componga
     * @return true si se puede mover hacia abajo, false de lo contrario
     */
    private boolean canMoveDown(int i, int j){
        boolean[][] pieceForm = moving.getForm();
        try{
            if(pieceForm[i][j] && i + moving.getXPos() + 1 >= board.length) throw new TetrisException(TetrisException.CANT_GO_DOWN);
            if((pieceForm[i][j] && (board[i + moving.getXPos() + 1][j + moving.getYPos()] || specialPiecesBoard[i + moving.getXPos() + 1][j + moving.getYPos()] != null)) || collidedDown){
                return false;
            }
        } catch (TetrisException e){
            return false;
        }
        return true;
    }

    /**
     * Metodo que mira si puede rotar la pieza
     * @return canRotate true si se puede rotar sin que se salga del tablero o se sobreponga sobre otra pieza, false de lo contrario
     * @throws TetrisException Lanza excepcion cuando la pieza se sale del tablero con un mensaje detallando en direccion se salio
     */
    private boolean canRotate() throws TetrisException {
        boolean[][] rotated = moving.getNextRotation();
        int x = moving.getXPos();
        int y = moving.getYPos();
        boolean canRotate = true;
        for (int i = 0; i < rotated.length; i++){
            for(int j = 0; j < rotated.length; j++){
                if(rotated[i][j] && i + x >= board.length) throw new TetrisException(TetrisException.ROTATION_BOUNDS_DOWN);
                if(rotated[i][j] && i + x < 0) throw new TetrisException(TetrisException.ROTATION_BOUNDS_UP);
                if(rotated[i][j] && j + y < 0) throw new TetrisException(TetrisException.ROTATION_BOUNDS_LEFT);
                if(rotated[i][j] && j + y >= board[0].length) throw new TetrisException(TetrisException.ROTATION_BOUNDS_RIGHT);
                if (rotated[i][j] && board[i + x][j + y]) {
                    canRotate = false;
                    break;
                }
            }
        }
        return canRotate;
    }

    /**
     * Metodo que dice cuando cuando se roto la pieza si se salio del tablero
     * @param rotated Forma de la pieza rotada
     * @param x Posicion x de la pieza
     * @param y Posicion y de la pieza
     * @return pieceOutOfBounds true si se salio del tablero, false de lo contrario
     */
    private boolean outOfBounds(boolean[][] rotated, int x, int y){
        boolean pieceOutOfBounds = false;
        for (int i = 0; i < rotated.length; i++){
            for(int j = 0; j < rotated.length; j++){
                if (rotated[i][j] && (i + x >= board.length || i + x < 0 || j + y < 0 || j + y >= board[0].length)) {
                    pieceOutOfBounds = true;
                    break;
                }
            }
        }
        return pieceOutOfBounds;
    }

    /**
     * Metodo que dependiendo de la variable paused alterna entre pausar el juego o resumir
     */
    public void pauseResume(){
        if(paused){
            resume();
        } else {
            pause();
        }
    }

    /**
     * Metodo que pausar el juego
     */
    public void pause(){
        timer.cancel();
        timer.purge();
        paused = true;
    }

    /**
     * Metodo que resume el juego
     */
    public void resume(){
        TimerTask increasePoints = new TimerTask() {
            @Override
            public void run() {
                points++;
            }
        };
        TimerTask pushDown = new TimerTask(){
            @Override
            public void run() {
                try{
                    movePieceDown();
                } catch (Exception ignored){}
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(increasePoints, 0, 1000);
        timer.scheduleAtFixedRate(pushDown, 0, 1200L/fallingVelocity);
        paused = false;
    }

    /**
     * Metodo que mira si la pieza esta tocando la posicion del tablero donde esta el buffo
     * @return true si la pieza se sobrepone con el buffo, false de lo contrario
     */
    public boolean checkCollisionBuff(){
        for(int i = 0; i < moving.getForm().length; i++){
            for (int j = 0; j < moving.getForm()[0].length; j++){
                if(moving.getForm()[i][j] && i + moving.getXPos() == xPosBuff && j + moving.getYPos() == yPosBuff){
                    return true;
                }
            }
        }
        return false;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean[][] getBoard() {
        return board;
    }

    public Piece getMoving() {
        return moving;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setFallingVelocity(int fallingVelocity) {
        this.fallingVelocity = fallingVelocity;
    }

    public TetrisCell[][] getSpecialPiecesBoard() {
        return specialPiecesBoard;
    }

    public void setCollidedDown(boolean collidedDown) {
        this.collidedDown = collidedDown;
    }

    public int getFallingVelocity() {
        return fallingVelocity;
    }

    public void setBuffOnBoard(Buff buffOnBoard) {
        this.buffOnBoard = buffOnBoard;
    }

    public Buff getBuffOnBoard() {
        return buffOnBoard;
    }

    public void setxPosBuff(int xPosBuff) {
        this.xPosBuff = xPosBuff;
    }

    public void setyPosBuff(int yPosBuff) {
        this.yPosBuff = yPosBuff;
    }

    public int getxPosBuff() {
        return xPosBuff;
    }

    public int getyPosBuff() {
        return yPosBuff;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    // De aca para abajo las funciones publicas son unicamente para poder realizar las pruebas sobre el tetris

    public void setUpBoardTest1(){
        moving = new LPiece(0);
        moving.setXPos(2);
        moving.setYPos(5);
        pause();
        paused = false;
    }

    public void setUpBoardTest2(){
        moving = new SquarePiece(1);
        moving.setXPos(17);
        moving.setYPos(-1);
        pause();
        paused = false;
    }

    public void setUpBoardTest3(){
        moving = new SquarePiece(3);
        moving.setXPos(17);
        moving.setYPos(-1);
        pause();
        paused = false;
    }

    public void setUpBoardTest4(){
        moving = new IPiece(2);
        moving.setXPos(16);
        moving.setYPos(-1);
        pause();
        paused = false;
    }


}
