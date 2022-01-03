package presentacion;

import dominio.Buff;
import dominio.TetrisMatch;
import persistencia.IOGameData;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Clase que extiende a JPanel para mostrar el panel que contenga todas las operaciones de las configuraciones del juego
 */
public class SettingsPanel extends JPanel {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private Dimension frameSize = new Dimension(screenSize.width/2, screenSize.height/2);

    private ArrayList<JCheckBox> checkBoxes = new ArrayList<>();

    private GridBagConstraints constraints = new GridBagConstraints();

    private JButton backButton;
    private JButton recordsButton;
    private JButton loginButton;
    private JButton buffButton;
    private JButton openButton;

    private JButton applyBuffChanges;

    private JPanel contentPanel;

    /**
     * Constructor de la clase SettingsPanel
     */
    public SettingsPanel(){
        super();
        prepareElements();
        prepareActions();
    }

    /**
     * Metodo para preparar todos los elementos de la pantalla de configuraciones
     */
    private void prepareElements(){
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.BLACK);

        backButton = new JButton();
        backButton.setBorderPainted(false);
        backButton.setIcon(scaleImage("./resources/BackButton.png", 1f/8.5f));

        recordsButton = new JButton();
        recordsButton.setBorderPainted(false);
        recordsButton.setIcon(scaleImage("./resources/RecordsButton.png", 1f/3.5f));

        loginButton = new JButton();
        loginButton.setBorderPainted(false);
        loginButton.setIcon(scaleImage("./resources/LoginButton.png", 1f/3.5f));

        buffButton = new JButton();
        buffButton.setBorderPainted(false);
        buffButton.setIcon(scaleImage("./resources/BuffsButton.png", 1f/3.5f));

        openButton = new JButton();
        openButton.setBorderPainted(false);
        openButton.setIcon(scaleImage("./resources/OpenButton.png", 1f/3.5f));

        applyBuffChanges = new JButton();
        applyBuffChanges.setIcon(scaleImage("./resources/ApplyButton.png", 1f/4f));
        applyBuffChanges.setBorderPainted(false);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0,0,20,0);
        this.add(backButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        this.add(loginButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        this.add(buffButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        this.add(recordsButton, constraints);

        contentPanel = new JPanel();
        contentPanel.setOpaque(false);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 5;
        constraints.gridheight = 5;
        constraints.fill = GridBagConstraints.BOTH;
        this.add(contentPanel, constraints);

        prepareOpen();
    }

    /**
     * Metodo para prepara las acciones de todos los elementos de la pantalla
     */
    private void prepareActions(){
        backButton.addActionListener(e -> backToMenu());
        recordsButton.addActionListener(e -> records());
        loginButton.addActionListener(e -> login());
        openButton.addActionListener(e -> open());
        buffButton.addActionListener(e -> setUpBuffs());
        applyBuffChanges.addActionListener(e -> changeBuffsPool());
    }

    /**
     * Accion del boton open
     */
    private void open() {
        JFileChooser openWindow = new JFileChooser("./GameData/" + TetrisGUI.User);
        openWindow.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int ans = openWindow.showOpenDialog(this);
        if(ans == JFileChooser.APPROVE_OPTION){
            File game = openWindow.getSelectedFile();
            GameBackground openingMatch = IOGameData.openMatch(game);
            TetrisGUI frame = (TetrisGUI)this.getTopLevelAncestor();
            frame.setContentPane(openingMatch);
            int players = openingMatch.getGames();
            int pcPlayers = openingMatch.getPcPlayers();
            frame.setSize((int)(frameSize.height * 1.25 * (players + pcPlayers)), (int)(frameSize.width * 1.1));
            frame.setLocationRelativeTo(null);
            frame.revalidate();
            frame.repaint();
        }
    }

    /**
     * Metodo que prepara el boton open dependiendo si se esta ingresada la sesion
     */
    private void prepareOpen(){
        if(!TetrisGUI.User.equals("")){
            constraints.gridx = 0;
            constraints.gridy = 4;
            constraints.gridwidth = 1;
            constraints.gridheight = 1;
            constraints.fill = GridBagConstraints.NONE;
            this.add(openButton, constraints);
        }
    }

    /**
     * Accion del boton records que muestra el historial de puntajes
     */
    private void records(){
        contentPanel.removeAll();
        contentPanel.setLayout(new GridBagLayout());
        ArrayList<String[]> leaderboard = IOGameData.getLeaderBoard();

        leaderboard.sort((Comparator.comparing(o -> Integer.parseInt(o[1]))));
        Collections.reverse(leaderboard);

        JLabel leaderboardTag = new JLabel("LEADERBOARD");
        leaderboardTag.setForeground(Color.CYAN);
        leaderboardTag.setOpaque(false);
        leaderboardTag.setFont(new Font("ArcadeClassic", Font.PLAIN, 35));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.insets = new Insets(0,50,25,0);
        constraints.fill = GridBagConstraints.NONE;
        contentPanel.add(leaderboardTag, constraints);

        for (int i = 0; i < leaderboard.size(); i++){
            for(int j = 0; j < leaderboard.get(0).length; j++){
                JLabel element = new JLabel(leaderboard.get(i)[j]);

                element.setForeground(Color.WHITE);
                element.setFont(new Font("ArcadeClassic", Font.PLAIN, 25));

                constraints.gridx = j;
                constraints.gridy = i + 1;
                constraints.gridwidth = 1;
                constraints.gridheight = 1;
                constraints.insets = new Insets(0,50,0,0);
                constraints.fill = GridBagConstraints.HORIZONTAL;
                contentPanel.add(element, constraints);
            }
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * Accion del boton buffs que muestra el manel para exojer nuevos buffos en la partida
     */
    private void setUpBuffs(){
        contentPanel.removeAll();
        contentPanel.setLayout(new GridBagLayout());
        checkBoxes.clear();
        ArrayList<Buff> buffs = TetrisMatch.getBuffsPool();
        for(int i = 0; i < buffs.size(); i++){

            JPanel buffPanel = new JPanel(new GridBagLayout());
            buffPanel.setOpaque(false);
            JLabel buffImage = new JLabel(scaleImage(buffs.get(i).getImage(), 1f/4f));
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridwidth = 1;
            constraints.gridheight = 1;
            constraints.insets = new Insets(0,50,20,0);
            constraints.fill = GridBagConstraints.NONE;
            buffPanel.add(buffImage, constraints);

            JCheckBox checkBoxBuff = new JCheckBox();
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.gridwidth = 1;
            constraints.gridheight = 1;
            constraints.insets = new Insets(0,50,20,0);
            constraints.fill = GridBagConstraints.NONE;
            buffPanel.add(checkBoxBuff, constraints);

            constraints.gridx = i % 2 != 0 ? 0 : 1;
            constraints.gridy = i % 2 != 0 ? i : i + 1;
            constraints.gridwidth = 1;
            constraints.gridheight = 1;
            constraints.fill = GridBagConstraints.NONE;
            constraints.insets = new Insets(0,0,0,0);
            contentPanel.add(buffPanel, constraints);

            checkBoxes.add(checkBoxBuff);
        }

        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.insets = new Insets(0,50,0,0);
        contentPanel.add(applyBuffChanges, constraints);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * Accion del boton de volver
     */
    private void backToMenu() {
        TetrisGUI frame = (TetrisGUI) this.getTopLevelAncestor();
        frame.setContentPane(frame.getMainMenuBackGround());
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Metodo para cambiar los posibles bufos que apareceran en partida
     */
    private void changeBuffsPool() {
        ArrayList<Buff> newPool = new ArrayList<>();
        for(int i = 0; i < checkBoxes.size(); i++){
            if(checkBoxes.get(i).isSelected()){
                newPool.add(TetrisMatch.getBuffsPool().get(i));
            }
        }
        TetrisMatch.possibleBuffs = newPool;
    }

    /**
     * Metodo para aparecer el dialogo de login
     */
    private void login(){
        LoginDialog login = new LoginDialog((TetrisGUI)this.getTopLevelAncestor());
        login.setVisible(true);
        prepareOpen();
        this.revalidate();
        this.repaint();
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
}
