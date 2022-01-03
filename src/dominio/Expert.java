package dominio;

import java.io.Serializable;
import java.util.*;

/**
 * Clase que representa  el modo experto para jugar contra la maquina
 */
public class Expert extends PcPlayer implements Serializable {

    private final Board board;
    private int bottomLine;
    private HashMap<Integer, Integer> possiblePositions = new HashMap<>();
    private transient Timer timer = new Timer();
    private final Map<Integer, Integer> rotations = new TreeMap<>();

    /**
     * Constructor de las maquinas Expertas
     * @param type Tipo de tablero que tendra el jugador
     */
    public Expert(String type){
        setBoard(type);
        board = getBoardObject();
    }

    /**
     * Metodo para preparar el diccionario con los valores de las areas de superficie de los 4 lados de la figura
     */
    private void setUpRotations(){
        rotations.clear();
        rotations.put(0, bottomSurfaceArea());
        rotations.put(1, rightSurfaceArea());
        rotations.put(2, topSurfaceArea());
        rotations.put(3, leftSurfaceArea());
    }

    /**
     * Metodo para escojer la mejor rotacion con la que puede car la ficha
     * @param fallingCoordinates Coordenadas donde caera la pieza
     * @return retorna una lista de enteros en donde la primera entrada es el area de superficie de de la pieza con la rotacion adecuada
     * y la segunda entrada sera el numero de rotaciones necesarias para que el posicionamiento de la pieza sea optima
     */
    private int[] pickBestRotation(int[] fallingCoordinates){
        int max = rotations.values().stream().max(Integer::compare).get();
        for(int key : rotations.keySet()){
            if(rotations.get(key).equals(possiblePositions.get(fallingCoordinates[1]))){
                return new int[]{rotations.get(key), key};
            }
        }
        int maxKey = 0;
        for(Integer key : rotations.keySet() ){
            if(rotations.get(key).equals(max)){
                maxKey = key;
            }
        }
        return new int[]{max, maxKey};
    }

    /**
     * Metodo para encontrar el numero de bloques que tiene una pieza en el nivel mas alto de esta
     * @return Numero de bloques del area de arriba de la pieza
     */
    private int topSurfaceArea(){
        Piece piece = board.getMoving();
        int topArea = 0;
        for(int i = 0; i < piece.getForm().length; i++){
            for (int j = 0; j < piece.getForm()[0].length; j++){
                if(piece.getForm()[i][j]){
                    topArea++;
                }
            }
            if(topArea != 0) break;
        }
        return topArea;
    }

    /**
     * Metodo para encontrar el numero de bloques que tiene una pieza en la capa mas a la derecha de esta
     * @return Numero de bloques del area de la derecha de la pieza
     */
    private int rightSurfaceArea(){
        Piece piece = board.getMoving();
        int rightArea = 0;
        for(int i = piece.getForm().length - 1; i >= 0; i--){
            for (int j = 0; j < piece.getForm()[0].length; j++){
                if(piece.getForm()[j][i]){
                    rightArea++;
                }
            }
            if(rightArea != 0) break;
        }
        return rightArea;
    }

    /**
     * Metodo para encontrar el numero de bloques que tiene una pieza en la capa mas a la izquierda de esta
     * @return Numero de bloques del area de la izquierda de la pieza
     */
    private int leftSurfaceArea(){
        Piece piece = board.getMoving();
        int leftArea = 0;
        for(int i = 0; i < piece.getForm().length; i++){
            for (int j = 0; j < piece.getForm()[0].length; j++){
                if(piece.getForm()[j][i]){
                    leftArea++;
                }
            }
            if(leftArea != 0) break;
        }
        return leftArea;
    }

    /**
     * Metodo para encontrar el numero de bloques que tiene una pieza en el nivel mas bajo de esta
     * @return Numero de bloques del area de abajo de la pieza
     */
    private int bottomSurfaceArea(){
        Piece piece = board.getMoving();
        int bottomArea = 0;
        for(int i = piece.getForm().length - 1; i >= 0; i--){
            for (int j = 0; j < piece.getForm()[0].length; j++){
                if(piece.getForm()[i][j]){
                    bottomArea++;
                }
            }
            if(bottomArea != 0) break;
        }
        return bottomArea;
    }

    /**
     * Metodo para mover la pieza a la mejor posicion posible en el tablero con la mejor rotacion posible
     * @throws InterruptedException Se lanza cuando los Threads se interrumpen inesperadamente
     */
    private void moveToBestPosition() throws InterruptedException {
        int [] bestPosCoordinates = decideBestPosition();
        while (bestPosCoordinates == null){
            bottomLine--;
            findSpaces();
            bestPosCoordinates = decideBestPosition();
        }
        int[] rotations = pickBestRotation(bestPosCoordinates);
        for(int i = 0; i < rotations[1]; i++){
            rotate();
        }
        int[] bottomBlockCoordinates = getBottomTrue();
        while (bottomBlockCoordinates[1] < bestPosCoordinates[1]){
            moveRight();
            bottomBlockCoordinates[1]++;
            if(bottomBlockCoordinates[1] ==  bestPosCoordinates[1]) break;
            Thread.sleep(10);
        }
        while (bottomBlockCoordinates[1] > bestPosCoordinates[1]){
            moveLeft();
            bottomBlockCoordinates[1]--;
            if(bottomBlockCoordinates[1] ==  bestPosCoordinates[1]) break;
            Thread.sleep(10);
        }
        while (bottomBlockCoordinates[0] < bestPosCoordinates[0]){
            moveDown();
            bottomBlockCoordinates[0]++;
            if(bottomBlockCoordinates[0] ==  bestPosCoordinates[0]) break;
            Thread.sleep(1000);
        }
    }

    /**
     * Metodo para obtener las coordenadas del bloque mas abajo hacia la izquiera que componga a la pieza
     * @return Una lista de enteros en donde la primera entrada es la fila y la segunda la columna del bloque en el tablero
     */
    private int[] getBottomTrue(){
        Piece piece = board.getMoving();
        for(int i = piece.getForm().length - 1; i >= 0; i--){
            for (int j = 0; j < piece.getForm()[0].length; j++){
                if(piece.getForm()[i][j]){
                    return new int[]{i + piece.getXPos(), j + piece.getYPos()};
                }
            }
        }
        return null;
    }

    /**
     * Metodo para encontrar la mejor posicion en la que se puede poner un bloque en el tablero
     * @return Una lista de enteros en donde la primera entrada es la fila y la segunda la columna de la mejor posicion del tablero
     */
    private int[] decideBestPosition(){
        for(int j : rotations.keySet()){
            for(int i : possiblePositions.keySet()){
                if (rotations.get(j) <= possiblePositions.get(i)){
                    return new int[]{bottomLine, i};
                }
            }
        }
        return null;
    }

    /**
     * Metodo para encontrar el numero de agujeros en el la fila mas baja en la que se pueda poner una pieza
     */
    private void findSpaces(){
        this.possiblePositions.clear();
        HashMap<Integer, Integer> possiblePositions = new HashMap<>();
        for(int k = 0; k < board.getBoard()[0].length; k++){
            if(board.getSpecialPiecesBoard()[bottomLine][k] == null){
                if(!possiblePositions.containsKey(k) && checkWell(bottomLine, k)){
                    possiblePositions.put(k, countFakes(k));
                }
            }
        }
        this.possiblePositions = possiblePositions;
    }

    /**
     * Metodo para contar el numero de agujeros en una fila del tablero a partir de una posicion dada
     * @param k columna desde la que se empezara a acontar
     * @return Numero de bloques libres seguidos desde la columna k
     */
    private int countFakes(int k){
        int count = 0;
        for (int j = k; j < board.getBoard()[0].length; j++){
            if(board.getSpecialPiecesBoard()[bottomLine][j] == null){
                count++;
            } else {
                return count;
            }
        }
        return count;
    }

    /**
     * Metodo para obtener la fila mas baja en la que se pueda poner un bloque
     * @return Numero de la fila mas baja en la que se puede poner un bloque
     */
    private int bottomLine(){
        for(int i = board.getBoard().length - 1; i >= 0; i--){
            for(int j = 0; j < board.getBoard()[0].length; j++){
                if(board.getSpecialPiecesBoard()[i][j] == null && checkWell(i, j)){
                    bottomLine = i;
                    return i;
                }
            }
        }
        return 0;
    }

    /**
     * Metodo para mirar si un agujero en la pantalla esta disponible para poner una pieza
     * @param i Fila del agujero del tablero
     * @param j Columna del agujero del tablero
     * @return true si se puede poner una ficha, false de lo contrario
     */
    private boolean checkWell(int i, int j) {
        for(int k = i - 1; k >= 0; k--){
            if(board.getSpecialPiecesBoard()[k][j] != null){
                return false;
            }
        }
        return true;
    }

    /**
     * Metodo para calcular la mejor posicion de una pieza repetidamente en la partida
     */
    private void setUpTimer(){
        TimerTask moveTo = new TimerTask() {
            @Override
            public void run() {
                bottomLine = bottomLine();
                findSpaces();
                setUpRotations();
                try {
                    moveToBestPosition();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.scheduleAtFixedRate(moveTo, 0, 1);
    }

    /**
     * Metodo para pausar la partida de los Expertos
     */
    @Override
    public void pause(){
        super.pause();
        timer.cancel();
    }

    /**
     * Metodo para resumir la partida de los Expertos
     */
    @Override
    public void resume(){
        super.resume();
        timer = new Timer();
        setUpTimer();
    }

}
