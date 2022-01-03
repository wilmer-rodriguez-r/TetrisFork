package presentacion;

import dominio.Piece;
import dominio.TetrisMatch;
import dominio.TetrisPlayer;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Clase que extiende la clase JPanel para mostrar los elementos de un juego
 */
public class TetrisPanel extends JPanel {

    private Color boardColor;

    private TetrisPlayer tetris;
    private TetrisMatch match;

    private JPanel boardPanel;

    private JPanel pointsPanel;
    private JLabel points;

    private JPanel buffPanel;

    private int player;

    private GridBagConstraints constraints = new GridBagConstraints();

    /**
     * Constructor de la clase TetrisPanel
     * @param player Juego del cual se mostraran los detalles
     * @param playerNum Numero de jugadores de la partida
     * @param match Partida de la que se mostraran los detalles
     */
    public TetrisPanel(TetrisPlayer player, int playerNum, TetrisMatch match){
        this.tetris = player;
        this.player = playerNum;
        this.match = match;
        this.boardColor = tetris.getBackgroundColor();
        prepareElements();
        prepareActions();
    }

    /**
     * Metodo para preparar los elementos de una partida
     */
    private void prepareElements(){
        this.setBackground(Color.BLACK);
        this.setLayout(new GridBagLayout());

        prepareBord();
        preparePoints();
        prepareBuffRightPanel();
    }

    /**
     * Metodo para prepara las acciones de los elementos de la partida
     */
    private void prepareActions(){
        Timer timer = new Timer(10, e -> refresh());
        Timer timerBuff = new Timer(1, e -> {
            showImage();
            playSoundEffects();
        });
        setKeyBindings();
        timer.start();
        timerBuff.start();
    }

    /**
     * Metodo que prepara el panel que muestra el tablero de juego
     */
    private void prepareBord(){
        boardPanel = new JPanel(new GridLayout(20, 10, 2, 2));
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        boardPanel.setBackground(Color.DARK_GRAY);

        refresh();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 3;
        constraints.weighty = 1.0;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        this.add(boardPanel, constraints);
    }

    /**
     * Metodo que refrezca la imagen sobre el estado del juego
     */
    private void refresh(){
        boardPanel.removeAll();
        for(int i = 0; i < tetris.getBoard().length; i++){
            for (int j = 0; j < tetris.getBoard()[0].length; j++){
                prepareTetrisGrid(i, j);
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    /**
     * Escoje que color o elemento debe tener cada celda de un tablero
     * @param i Fila de la grilla del tetris
     * @param j Columna de la grilla del tetris
     */
    private void prepareTetrisGrid(int i, int j){
        JPanel tetrisCell = new JPanel();
        Piece moving = tetris.getMovingPiece();
        if(i - moving.getXPos() < 4 && j - moving.getYPos() < 4 && i - moving.getXPos() >= 0 && j - moving.getYPos() >= 0 && moving.getForm()[i - moving.getXPos()][j - moving.getYPos()]){
            tetrisCell.setBackground(moving.getPieceType()[i - moving.getXPos()][j - moving.getYPos()].getPieceColor());
            tetrisCell.setBorder(BorderFactory.createLineBorder(moving.getPieceType()[i - moving.getXPos()][j - moving.getYPos()].getBorderColor(), 2));
        } else if(tetris.getSpecialPiecesBoard()[i][j] != null){
            tetrisCell.setBackground(tetris.getSpecialPiecesBoard()[i][j].getPieceColor());
            tetrisCell.setBorder(BorderFactory.createLineBorder(tetris.getSpecialPiecesBoard()[i][j].getBorderColor(), 2));
        } else if(tetris.getBoardObject().getxPosBuff() == i && tetris.getBoardObject().getyPosBuff() == j && match.getActualBuff() != null){
            tetrisCell = new ImagePanel(match.getActualBuff().getImage());
            tetrisCell.setBackground(Color.BLACK);
        } else {
            tetrisCell.setBackground(boardColor);
        }
        boardPanel.add(tetrisCell);
    }

    /**
     * Metodo que prepara el panel de puntos
     */
    private void preparePoints(){
        pointsPanel = new JPanel();
        pointsPanel.setLayout(new GridBagLayout());
        pointsPanel.setOpaque(false);
        pointsPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));

        JLabel pointsTitle = new JLabel("Points");
        pointsTitle.setForeground(Color.WHITE);
        pointsTitle.setFont(new Font("ArcadeClassic", Font.PLAIN, 25));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weighty = 0.0;
        constraints.weightx = 0.0;
        constraints.ipady = 50;
        constraints.fill = GridBagConstraints.NONE;
        pointsPanel.add(pointsTitle);

        points = new JLabel("");
        points.setFont(new Font("ArcadeClassic", Font.PLAIN, 25));
        Timer timer = new Timer(100, e -> points.setText("" + tetris.getPoints()));
        points.setForeground(Color.WHITE);
        constraints.gridx = 0;
        constraints.gridy = 1;
        pointsPanel.add(points, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weighty = 0.5;
        constraints.weightx = 0.5;
        constraints.fill = GridBagConstraints.BOTH;
        this.add(pointsPanel, constraints);

        timer.start();

    }

    /**
     * Metodo que prepara los efectos de sonido del tablero
     */
    private void playSoundEffects(){
        if(tetris.getBoardObject().checkCollisionBuff()){
            try {
                Clip powerUp = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("./resources/PowerUpSound.wav"));
                powerUp.open(inputStream);
                powerUp.start();
                FloatControl volume = (FloatControl) powerUp.getControl(FloatControl.Type.MASTER_GAIN);
                if (volume != null) {
                    volume.setValue(-20.0f);
                }
            }catch(LineUnavailableException | UnsupportedAudioFileException | IOException ignored){}
        }
    }

    /**
     * Metodo que muestra la imagen el buffo que se obtuvo en el tablero
     */
    private void showImage(){
        buffPanel.removeAll();
        buffPanel.setOpaque(false);
        buffPanel.setLayout(new GridBagLayout());

        JPanel buffImagePanel = new JPanel();
        buffImagePanel.setOpaque(false);
        if(tetris.getBoardObject().getBuffOnBoard() != null){

            buffImagePanel = new ImagePanel(tetris.getBoardObject().getBuffOnBoard().getImage());
            buffImagePanel.setOpaque(false);
            buffImagePanel.setPreferredSize(new Dimension(90, 40));
            buffImagePanel.repaint();
        }

        JLabel power = new JLabel("You    picked    up");
        power.setFont(new Font("ArcadeClassic", Font.PLAIN, 15));
        power.setForeground(Color.WHITE);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weighty = 0;
        constraints.weightx = 0;
        constraints.fill = GridBagConstraints.NONE;
        buffPanel.add(power, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weighty = 0;
        constraints.weightx = 0;
        constraints.fill = GridBagConstraints.NONE;
        buffPanel.add(buffImagePanel, constraints);

        buffPanel.revalidate();
        buffPanel.repaint();
    }

    /**
     * Metodo que prepara el panel del buffo de la derecha
     */
    private void prepareBuffRightPanel(){
        buffPanel = new JPanel(new GridBagLayout());
        buffPanel.setOpaque(false);

        showImage();

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weighty = 0.5;
        constraints.weightx = 0.5;
        constraints.fill = GridBagConstraints.BOTH;
        this.add(buffPanel, constraints);
    }

    /**
     * Metodo que prepara as acciones de las teclas
     */
    private void setKeyBindings() {
        ActionMap actionMap = getActionMap();
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        if(player == 2){
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "VK_LEFT");
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "VK_RIGHT");
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "VK_UP");
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "VK_DOWN");

            Action moveLeft = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    putValue(ACTION_COMMAND_KEY, "VK_LEFT");
                    tetris.moveLeft();
                }
            };

            Action moveRight = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    putValue(ACTION_COMMAND_KEY, "VK_RIGHT");
                    tetris.moveRight();
                }
            };

            Action rotate = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    putValue(ACTION_COMMAND_KEY, "VK_UP");
                    tetris.rotate();
                }
            };

            Action moveDown = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    putValue(ACTION_COMMAND_KEY, "VK_DOWN");
                    tetris.moveDown();
                }
            };

            actionMap.put("VK_LEFT", moveLeft);
            actionMap.put("VK_RIGHT", moveRight);
            actionMap.put("VK_UP", rotate);
            actionMap.put("VK_DOWN", moveDown);

        } else if(player == 1){

            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "VK_W");
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "VK_A");
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "VK_S");
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "VK_D");

            Action moveLeft2 = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    putValue(ACTION_COMMAND_KEY, "VK_A");
                    tetris.moveLeft();
                }
            };

            Action moveRight2 = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    putValue(ACTION_COMMAND_KEY, "VK_D");
                    tetris.moveRight();
                }
            };

            Action rotate2 = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    putValue(ACTION_COMMAND_KEY, "VK_W");
                    tetris.rotate();
                }
            };

            Action moveDown2 = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    putValue(ACTION_COMMAND_KEY, "VK_S");
                    tetris.moveDown();
                }
            };

            actionMap.put("VK_A", moveLeft2);
            actionMap.put("VK_D", moveRight2);
            actionMap.put("VK_W", rotate2);
            actionMap.put("VK_S", moveDown2);
        }
    }

    /**
     * Metodo que cambia el color del tablero
     * @param boardColor Nuevo color del tablero
     */
    public void setBoardColor(Color boardColor) {
        this.boardColor = boardColor;
    }
}