package persistencia;

import dominio.TetrisMatch;
import presentacion.GameBackground;
import presentacion.TetrisGUI;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Clase usada para leer los archivos que tenemos guardados de cada perfil
 */
public class IOGameData {

    public static String name = "TetrisPOOB";

    /**
     * Metodo que registra el error en el log
     * @param e Excepcion que deseamos imprimir en el log
     */
    public static void registerError(Exception e){
        try{
            Logger logger=Logger.getLogger(name);
            logger.setUseParentHandlers(false);
            FileHandler file = new FileHandler("./ErrorsLog/" + name + ".log",true);
            file.setFormatter(new SimpleFormatter());
            logger.addHandler(file);
            logger.log(Level.SEVERE, e.toString(), e);
            file.close();
        }catch (Exception oe){
            oe.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Metodo que guarda el color de un jugador en un archivo de texto UsersColors.txt
     * @param nickname Nombre del usuario que ingreso a la aplicacion
     * @param newColor Color que se le guardara al usuario
     */
    public static void saveColor(String nickname, Color newColor){
        try{
            ArrayList<String> userColorContent = new ArrayList<>(Files.readAllLines(Path.of("./GameData/UsersColors.txt"), StandardCharsets.UTF_8));
            for(int i = 0; i < userColorContent.size(); i++){
                if (userColorContent.get(i).trim().split(" ")[0].equalsIgnoreCase(nickname)){
                    int r = newColor.getRed();
                    int g = newColor.getGreen();
                    int b = newColor.getBlue();
                    userColorContent.set(i, nickname + " " + r + " " + g + " " + b);
                }
            }
            Files.write(Path.of("./GameData/UsersColors.txt"), userColorContent, StandardCharsets.UTF_8);
        } catch (IOException ignored){}
    }

    /**
     * Metodo que guarda en los archivos de texto el nuevo record de un jugador
     * @param nickname Nombre del usuario que ingreso en la aplicacion
     * @param points Puntos de la mejor partida del jugador
     */
    public static void newRecord(String nickname, int points){
        try{
            ArrayList<String> usersPoints = new ArrayList<>(Files.readAllLines(Path.of("./GameData/UsersTopScores.txt"), StandardCharsets.UTF_8));
            ArrayList<String> leaderboard = new ArrayList<>(Files.readAllLines(Path.of("./GameData/Leaderboard.txt"), StandardCharsets.UTF_8));
            for(int i = 0; i < usersPoints.size(); i++){
                if(usersPoints.get(i).trim().split(" ")[0].equalsIgnoreCase(nickname)){
                    usersPoints.set(i, nickname + " " + points);
                }
            }
            for(int i = 0; i < leaderboard.size(); i++){
                if(leaderboard.get(i).trim().split(" ")[0].equalsIgnoreCase(nickname)){
                    leaderboard.set(i, nickname + " " + points);
                }
            }
            Files.write(Path.of("./GameData/UsersTopScores.txt"), usersPoints, StandardCharsets.UTF_8);
            Files.write(Path.of("./GameData/Leaderboard.txt"), leaderboard, StandardCharsets.UTF_8);
        } catch (IOException ignored){}
    }

    /**
     * Metodo que guarda el objeto de la partida en un archivo .dat
     * @param match Objeto TetrisMatch que queremos guardar en el archivo
     * @param nickname Nombre del jugador que ingreso para guardar la partida
     * @param matchParams Nombre del directorio en el que se guardara la partda
     */
    public static void saveMatch(TetrisMatch match, String nickname, File matchParams){
        try{
            Files.createDirectories(Path.of("./GameData/" + nickname.toLowerCase() + "/" + matchParams.getName() + "/"));
            PrintWriter printWriter = new PrintWriter(new FileOutputStream("./GameData/" + nickname.toLowerCase() + "/" + matchParams.getName() + "/" + matchParams.getName() + ".txt"));
            printWriter.println(match.getTetrisGames() + " " + match.getPcPlayers() + " " + match.getType() + " " + match.getPcExperience());
            printWriter.close();

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("./GameData/" + nickname.toLowerCase() + "/" + matchParams.getName() + "/" + matchParams.getName() + ".dat"));
            objectOutputStream.writeObject(match);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException ignored){}

    }

    /**
     * Metodo que retorna el historial de los mejores juegos del Tetris
     * @return String[][] en donde el primer elemento es el usuario y el segundo su maximo puntaje
     */
    public static ArrayList<String[]> getLeaderBoard(){
        ArrayList<String[]> leaderboard = new ArrayList<>();
        try{
            BufferedReader reader = new BufferedReader(new FileReader("./GameData/Leaderboard.txt"));
            String userPoints = reader.readLine();
            while (userPoints != null){
                userPoints = userPoints.trim();
                String[] userPointsList = userPoints.split(" ");
                leaderboard.add(userPointsList);
                userPoints = reader.readLine();
            }
        } catch (IOException ignored){}
        return leaderboard;
    }

    /**
     * Metodo que valida que los datos ingresados por el usuario sean correctos y le permita hacer el login
     * @param user Nombre ingresado por el usuario
     * @param password Constraseña ingresada por el usuario
     * @return true si los datos son correctos, false de lo contrario
     */
    public static boolean validateLogin(String user, String password){
        try{
            BufferedReader reader = new BufferedReader(new FileReader("./GameData/UsersPasswords.txt"));
            String line = reader.readLine();
            while (line != null){
                line = line.trim();
                String[] userPasswords = line.split(" ");
                if(userPasswords[0].equalsIgnoreCase(user) && userPasswords[1].equals(password)){
                    return true;
                }
                line = reader.readLine();
            }
        } catch (IOException ignored){}
        return false;
    }

    /**
     * Metodo que obtiene los datos importantes de los usuarios guardados en archivos de texto
     * @param user Nombre del usuario del que queremos retomar sus datos
     * @param path Camino al archivo del que tomaremos los datos
     * @return String[] en el que estan listados los datos del usuario actualmente ingresado, retornara una lista vacia
     * si lo no hay ningun usuario ingresado
     */
    public static String[] getUserInfo(String user, String path){
        try{
            BufferedReader reader = new BufferedReader(new FileReader("./GameData/" + path));
            String line = reader.readLine();
            while (line != null){
                line = line.trim();
                String[] userInfo = line.split(" ");
                if(userInfo[0].equalsIgnoreCase(user)){
                    return userInfo;
                }
                line = reader.readLine();
            }
        } catch (IOException ignored){}
        return null;
    }

    /**
     * Metodo que abre juegos guardados anteriormente del usuario que ingreso al juego
     * @param match Directorio que contiene los datos necesarios para abrir la partida
     * @return GameBackground Objeto que se usa para representar graficamente la partida
     */
    public static GameBackground openMatch(File match){
        GameBackground game = null;

        try{
            BufferedReader reader = new BufferedReader(new FileReader("./GameData/" + TetrisGUI.User.toLowerCase() + "/" + match.getName() + "/" + match.getName() + ".txt"));
            String line = reader.readLine();
            line = line.trim();
            String[] matchInfo = line.split(" ");
            game = new GameBackground(Integer.parseInt(matchInfo[0]), Integer.parseInt(matchInfo[1]), matchInfo[2], matchInfo[3]);
            reader.close();

            ObjectInputStream in = new ObjectInputStream(new FileInputStream("./GameData/" + TetrisGUI.User.toLowerCase() + "/" + match.getName() + "/" + match.getName() + ".dat"));
            TetrisMatch newMatch = (TetrisMatch) in.readObject();
            newMatch.resume();
            game.setMatch(newMatch);
            in.close();
        } catch (IOException | ClassNotFoundException ignored){
            ignored.printStackTrace();
        }
        return game;
    }
}
