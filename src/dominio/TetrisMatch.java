package dominio;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Clase que representa a la partida de tetris que se lleva en el momento
 */
public class TetrisMatch implements Serializable {

    private static final long serialVersionUID = 6529685098267757690L;

    public static ArrayList<Buff> possibleBuffs = new ArrayList<>(Arrays.asList(new TimeX2(), new Slow(), new StopTime(), new StopPiece()));

    private static final ArrayList<Buff> buffsPool = new ArrayList<>(Arrays.asList(new TimeX2(), new Slow(), new StopTime(), new StopPiece()));

    private final ArrayList<TetrisPlayer> games = new ArrayList<>();
    private final int tetrisGames;
    private final int pcPlayers;
    private final String type;
    private final String pcExperience;
    private Buff actualBuff;

    private final Random rand = new Random();

    private transient Timer timer = new Timer();

    private boolean paused = false;

    /**
     * Constructor de las partidas de Tetris
     * @param tetrisGames Numero de jugadores humanos que estan en la partida actual
     * @param pcPlayers Numpero de jugadores de computadora presentes en la partida actual
     * @param type Tipo del tablero que tendran los jugadores lento o acelerado, los posibles valores son "dominio.SlowBoard" o "dominio.AcceleratedBoard"
     * @param pcExperience Tipo de jugador de pc con el que se estara enfrentando el jugador, los posibles valores son "dominio.Noob" o "dominio.Expert"
     */
    public TetrisMatch(int tetrisGames, int pcPlayers, String type, String pcExperience) {
        this.tetrisGames = tetrisGames;
        this.pcPlayers = pcPlayers;
        this.type = type;
        this.pcExperience = pcExperience;
        for (int board = 0; board < tetrisGames; board++) {
            games.add(new HumanPlayer(type));
        }
        for (int pcBoard = 0; pcBoard < pcPlayers; pcBoard++) {
            try {
                Constructor constructPlayer = Class.forName(pcExperience).getConstructor(String.class);
                games.add((TetrisPlayer) constructPlayer.newInstance(type));
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ignored) {}
        }
        resume();
    }

    /**
     * Metodo que spawnea un nuevo buffo en los tableros de la partida
     * @return El buffo que se spawneo
     */
    private Buff spawnRandomBuff(){
        int spawning = rand.nextInt(possibleBuffs.size());
        try{
            Buff newBuff = possibleBuffs.get(spawning).getClass().newInstance();
            for (TetrisPlayer game : games){
                newBuff.spawn(game.getBoardObject());
            }
            return newBuff;
        } catch (IllegalAccessException | InstantiationException ignored){}
        return null;
    }

    /**
     * Metodo que intenta spawnear un nuevo buffo si ya se uso el anterior
     */
    private void setNewSpawn(){
        timer = new Timer();
        TimerTask spawnRandomly = new TimerTask() {
            @Override
            public void run() {
                TimerTask spawn = new TimerTask() {
                    @Override
                    public void run() {
                        if(actualBuff == null){
                            actualBuff = spawnRandomBuff();
                        }
                    }
                };
                int interval = rand.nextInt(5);
                timer.schedule(spawn, interval * 1000);
            }
        };
        timer.scheduleAtFixedRate(spawnRandomly, 0, 10000);
    }

    /**
     * Metodo que que se activen los buffos en los tableros del juego
     */
    public void useBuff(){
        for(TetrisPlayer game : games){
            Buff buff = game.getBoardObject().getBuffOnBoard();
            if(buff != null){
                if(buff.isGoodPower()){
                    useInMe(game);
                } else {
                    useInEnemies(game);
                }
                game.getBoardObject().setBuffOnBoard(null);
                actualBuff = null;
                break;
            }
        }
    }

    /**
     * Metodo en que se usa el buffo en todos los enemigos eceptuando el jugador que lo llamo
     * @param game Tablero que tiene el control del buffo
     */
    private void useInEnemies(TetrisPlayer game) {
        for (TetrisPlayer board : games){
            if (board != game){
                actualBuff.activate(board);
            }
        }
    }

    /**
     * Metodo que usa el buffo en el tablero que tiene control de el
     * @param game Tablero que tiene el control del buffo
     */
    private void useInMe(TetrisPlayer game) {
        for (TetrisPlayer board : games){
            if (board == game){
                actualBuff.activate(board);
            }
        }
    }

    /**
     * Metodo que le otorga el control al jugador que consiguio el buffo y
     * desespawnea el buffo de los otros tableros
     */
    private void assignBuff(){
        TimerTask checkAssignment = new TimerTask() {
            @Override
            public void run() {
                for (TetrisPlayer game : games){
                    if(game.getBoardObject().checkCollisionBuff()){
                        takeBuffsFromBoards(game.getBoardObject());
                        break;
                    }
                }
            }
        };
        timer.scheduleAtFixedRate(checkAssignment, 0, 10);
    }

    /**
     * Metodo que elimina el del tablero el buffo
     * @param board Tablero del que se eliminara el buffo
     */
    private void takeBuffsFromBoards(Board board) {
        for(TetrisPlayer game : games){
            game.getBoardObject().setBuffOnBoard(null);
            game.getBoardObject().setxPosBuff(Integer.MAX_VALUE);
            game.getBoardObject().setyPosBuff(Integer.MAX_VALUE);
            if(game.getBoardObject() == board){
                game.getBoardObject().setBuffOnBoard(actualBuff);
            }
        }
    }

    /**
     * Metodo que pausa o resume una partida dependiendo de la variable paused
     */
    public void pauseResume(){
        if(paused){
            resume();
        } else {
            pause();
        }
    }

    /**
     * Metodo que pausa la partida
     */
    public void pause(){
        for (TetrisPlayer game : games) {
            game.pause();
        }
        timer.cancel();
        paused = true;
    }

    /**
     * Metodo que resume la partida
     */
    public void resume(){
        for (TetrisPlayer game : games) {
            game.resume();
        }
        timer = new Timer();
        setNewSpawn();
        assignBuff();
        paused = false;
    }

    public ArrayList<TetrisPlayer> getGames() {
        return games;
    }

    public int getTetrisGames() {
        return tetrisGames;
    }

    public int getPcPlayers() {
        return pcPlayers;
    }

    public String getPcExperience() {
        return pcExperience;
    }

    public String getType() {
        return type;
    }

    public Buff getActualBuff() {
        return actualBuff;
    }

    public static ArrayList<Buff> getBuffsPool() {
        return buffsPool;
    }

    // Metodo unicamente para las pruebas
    public void setActualBuff(Buff actualBuff) {
        this.actualBuff = actualBuff;
    }
}

