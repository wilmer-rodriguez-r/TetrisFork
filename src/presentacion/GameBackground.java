package presentacion;

import dominio.TetrisMatch;
import dominio.TetrisPlayer;
import persistencia.IOGameData;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase que extiende JPanel para mostrar la partida creada
 */
public class GameBackground extends JPanel {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private Dimension frameSize = new Dimension(screenSize.width/2, screenSize.height/2);

    private ArrayList<TetrisPanel> gamesOnScreen = new ArrayList<>();

    private JPanel buttonsPanel;

    private JButton restartButton;
    private JButton exitButton;
    private JButton pauseButton;
    private JButton colorsButton;
    private JButton saveButton;

    private JPanel gamesPanel;

    private int games;
    private int pcPlayers;
    private String boardType;
    private String pcExperience;

    private Color boardColor = Color.BLACK;
    private Timer lostTimer;

    private GridBagConstraints constraints = new GridBagConstraints();

    private TetrisMatch match;

    /**
     * Constructos de la clase GameBackground
     * @param tetrisGames Numero de jugadores humanos en la partida
     * @param pcPlayers Numero de jugadores maquina en la partida
     * @param type Tipo de tablero que tendran los jugadores
     * @param pcExperience Experiencia de los jugadores maquina
     */
    public GameBackground(int tetrisGames, int pcPlayers, String type, String pcExperience){
        super();
        this.games = tetrisGames;
        this.pcPlayers = pcPlayers;
        this.boardType = type;
        this.pcExperience = pcExperience;
        prepareElements();
        prepareActions();
    }

    /**
     * Metodo que prepara las acciones de los diferentes elementos de la partida
     */
    private void prepareActions() {
        exitButton.addActionListener(e -> exitGame());
        restartButton.addActionListener(e -> restart());
        pauseButton.addActionListener(e -> pauseResume());
        colorsButton.addActionListener(e -> changeColors());
        saveButton.addActionListener(e -> save());
        setKeyBindings();
        lostTimer = new Timer(100, e -> gameOver());
        lostTimer.start();
    }

    /**
     * Metodo para preparar todos los elementos de la partida
     */
    private void prepareElements() {
        this.match = new TetrisMatch(games, pcPlayers, boardType, pcExperience);
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
        prepareButtons();
        prepareGames();
    }

    /**
     * Metodo para preparar los paneles que muestran el estado del juego
     */
    private void prepareGames(){
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weighty = 0.1;
        constraints.weightx = 1.0;
        constraints.insets = new Insets(0,0,0,0);
        constraints.fill = GridBagConstraints.BOTH;
        this.add(buttonsPanel, constraints);

        gamesPanel = new JPanel(new GridLayout(1, games));
        gamesPanel.setOpaque(false);

        for (int i = 0; i < match.getGames().size(); i++){
            TetrisPanel game = new TetrisPanel(match.getGames().get(i), i + 1, match);
            gamesOnScreen.add(game);
            gamesPanel.add(game);
        }

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weighty = 1.0;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        this.add(gamesPanel, constraints);
    }

    /**
     * Metodo para preparar los botones del panel de la parte superior de la partida
     */
    private void prepareButtons(){
        buttonsPanel = new JPanel(new GridBagLayout());
        buttonsPanel.setOpaque(false);

        pauseButton = new JButton();
        pauseButton.setIcon(scaleImage("./resources/PauseButton.png", 1f/8f));
        pauseButton.setBorderPainted(false);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.ipady = 0;
        constraints.ipadx = 0;
        constraints.fill = GridBagConstraints.NONE;
        constraints.insets = new Insets(0,10,0,10);
        buttonsPanel.add(pauseButton, constraints);

        restartButton = new JButton();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.insets = new Insets(0,0,0,10);
        constraints.fill = GridBagConstraints.NONE;
        restartButton.setIcon(scaleImage("./resources/RestartButton.png", 1f/4f));
        restartButton.setBorderPainted(false);
        buttonsPanel.add(restartButton, constraints);

        exitButton = new JButton();
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.insets = new Insets(0,0,0,10);
        constraints.fill = GridBagConstraints.NONE;
        exitButton.setIcon(scaleImage("./resources/ExitButton.png", 1f/4f));
        exitButton.setBorderPainted(false);
        buttonsPanel.add(exitButton, constraints);

        colorsButton = new JButton();
        constraints.gridx = 3;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.insets = new Insets(0,0,0,10);
        constraints.fill = GridBagConstraints.NONE;
        colorsButton.setIcon(scaleImage("./resources/ColorsButton.png", 1f/4f));
        colorsButton.setBorderPainted(false);
        buttonsPanel.add(colorsButton, constraints);

        saveButton = new JButton();
        constraints.gridx = 4;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.insets = new Insets(0,0,0,10);
        constraints.fill = GridBagConstraints.NONE;
        saveButton.setIcon(scaleImage("./resources/SaveButton.png", 1f/8f));
        saveButton.setBorderPainted(false);
        buttonsPanel.add(saveButton, constraints);

        buttonsPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    }

    /**
     * Metodo que reinicia las partidas existentes
     */
    private void restart(){
        pause();
        int resp = JOptionPane.showConfirmDialog(this, "Are you sure you what to restart the game? \nAll unsaved process will be lost", "Restart?", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            this.removeAll();
            prepareElements();
            prepareActions();
            this.revalidate();
            this.repaint();
        } else {
            resume();
        }
    }

    /**
     * Metodo que devuelve al menu principal del juego
     */
    private void exitGame(){
        pause();
        int resp = JOptionPane.showConfirmDialog(this, "Are you sure you what to exit the game? \nAll unsaved process will be lost", "Exit?", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            TetrisGUI frame = (TetrisGUI)this.getTopLevelAncestor();
            frame.setContentPane(frame.getMainMenuBackGround());
            frame.setSize(frameSize.width, frameSize.height);
            frame.setLocationRelativeTo(null);
            frame.revalidate();
            frame.repaint();
        } else {
            resume();
        }
    }

    /**
     * Metodo para pausar y resumir alternamente las partidas
     */
    private void pauseResume() {
        match.pauseResume();
    }

    /**
     * Metodo para pausar una partida
     */
    private void pause(){
        match.pause();
    }

    /**
     * Metodo para resumir una partida
     */
    private void resume(){
        match.resume();
    }

    /*
     * Metodo para escalar una imagen a un tamaño deseaddo en la  GUI
     */
    private ImageIcon scaleImage(String path, float reSizeFactor){
        try{
            BufferedImage img = ImageIO.read(new File(path));
            Image scaledImg = img.getScaledInstance((int)( img.getWidth() * reSizeFactor), (int)(img.getHeight() * reSizeFactor), Image.SCALE_DEFAULT);
            return new ImageIcon(scaledImg);
        } catch (IOException ignored){}
        return null;
    }

    /**
     * Metodo para cambiar los colores de los tableros en la partida
     */
    private void changeColors(){
        pause();
        Color newColor = JColorChooser.showDialog(this, "Change background color", boardColor);
        if(newColor != null) boardColor = newColor;
        for(TetrisPanel game : gamesOnScreen){
            game.setBoardColor(boardColor);
            IOGameData.saveColor(TetrisGUI.User, newColor);
        }
        resume();
    }

    /**
     * Metodo que revisa si algun juego de la partida ha perdido
     */
    private void gameOver() {
        boolean lost = false;
        boolean record = false;
        for (TetrisPlayer tetris : match.getGames()) {
            if(tetris.hasLost()){
                lost = true;
                if(tetris.isBestScore()) {
                    record = true;
                }
                break;
            }
        }
        if(lost){
            pause();
            if(record && !TetrisGUI.User.equals("")){
                JOptionPane.showConfirmDialog(this, "Congrats!!!! \nYou just made a new high score :D", "NEW RECORD!!!", JOptionPane.DEFAULT_OPTION);
            }
            int res = JOptionPane.showConfirmDialog(this, "Game Over D: \nDo you wish to try again?", "GAME OVER", JOptionPane.YES_NO_OPTION);
            if(res == JOptionPane.YES_OPTION){
                match.getGames().clear();
                this.removeAll();
                prepareElements();
                prepareActions();
                this.revalidate();
                this.repaint();
            } else {
                TetrisGUI frame = (TetrisGUI)this.getTopLevelAncestor();
                frame.setContentPane(frame.getMainMenuBackGround());
                frame.setSize(frameSize.width, frameSize.height);
                frame.setLocationRelativeTo(null);
                frame.revalidate();
                frame.repaint();
            }
            lostTimer.stop();
        }
    }

    /**
     * Metodo para guardar el estado de una partida
     */
    private void save(){
        pause();
        if (!TetrisGUI.User.equals("")){
            JFileChooser saveWindow = new JFileChooser("./GameData/" + TetrisGUI.User);
            saveWindow.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int ans = saveWindow.showSaveDialog(this);
            if(ans == JFileChooser.APPROVE_OPTION){
                IOGameData.saveMatch(match, TetrisGUI.User, saveWindow.getSelectedFile());
            }
        } else {
            JOptionPane.showConfirmDialog(this, "You haven't login yet \nTo be able to save a match you need to have an account :'( \nSorry", "Unable to save", JOptionPane.DEFAULT_OPTION);
        }
        resume();
    }

    /**
     * Metodo para asignar a las teclas del teclado una accion particular del dominio
     */
    private void setKeyBindings(){
        ActionMap actionMap = getActionMap();
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, 0), "VK_PERIOD");

        Action useBuff = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                putValue(ACTION_COMMAND_KEY, "VK_PERIOD");
                for(TetrisPlayer game : match.getGames()){
                    if(game.getBoardObject().getBuffOnBoard() != null && !game.getBoardObject().isPaused()){
                        try {
                            Clip powerUp = AudioSystem.getClip();
                            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("./resources/UsePowerUp.wav"));
                            powerUp.open(inputStream);
                            powerUp.start();
                            FloatControl volume = (FloatControl) powerUp.getControl(FloatControl.Type.MASTER_GAIN);
                            if (volume != null) {
                                volume.setValue(-10.0f);
                            }
                        }catch(LineUnavailableException | UnsupportedAudioFileException | IOException ignored){}
                        match.useBuff();
                    }
                }
            }
        };

        actionMap.put("VK_PERIOD", useBuff);
    }

    /**
     * Metodo para asignar una nueva partida para mostrar
     * @param newMatch Nueva partidda a asignar al fondo del juego
     */
    public void setMatch(TetrisMatch newMatch) {
        match = newMatch;
        gamesOnScreen.clear();
        gamesPanel.removeAll();
        for (int i = 0; i < match.getGames().size(); i++){
            TetrisPanel game = new TetrisPanel(match.getGames().get(i), i + 1, match);
            gamesOnScreen.add(game);
            gamesPanel.add(game);
        }
    }

    public int getPcPlayers() {
        return pcPlayers;
    }

    public int getGames() {
        return games;
    }
}
