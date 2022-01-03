package presentacion;

import persistencia.IOGameData;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Clase para Mostrar el dialogo en el que se permite hacer los login
 */
public class LoginDialog extends JDialog {

    private GridBagConstraints constraints = new GridBagConstraints();

    private JPanel textPanel;
    private JPanel buttonsPanel;

    private JLabel userLabel;
    private JLabel passwordLabel;
    private JTextField username;
    private JPasswordField password;

    private JButton login;
    private JButton cancel;

    private JFrame parent;

    /**
     * Constructor de la clase LoginDialog
     * @param parent Frame padre del dialogo
     */
    public LoginDialog(JFrame parent){
        super(parent, "Login", true);
        this.parent = parent;
        prepareElements();
        prepareActions();
    }

    /**
     * Metodo para preparar todas las acciones del dialogo
     */
    private void prepareActions() {
        cancel.addActionListener(e -> cancel());
        login.addActionListener(e -> login());
    }

    /**
     * Metodo para preparar todos los elementos del dialogo
     */
    private void prepareElements() {
        this.getContentPane().setBackground(Color.BLACK);
        prepareTextPanel();
        prepareButtonsPanel();

        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(parent);
    }

    /**
     * Metodo para preparar los botones del dialogo
     */
    private void prepareButtonsPanel() {
        buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);

        login = new JButton();
        login.setBorderPainted(false);
        login.setIcon(scaleImage("./resources/LoginButton.png", 1f/4.5f));

        cancel = new JButton();
        cancel.setBorderPainted(false);
        cancel.setIcon(scaleImage("./resources/ExitButton.png", 1f/4.5f));

        buttonsPanel.add(login);
        buttonsPanel.add(cancel);

        this.getContentPane().add(buttonsPanel, BorderLayout.PAGE_END);
    }

    /**
     * Metodo para preparar los campos de texto del dialogo
     */
    private void prepareTextPanel() {
        textPanel = new JPanel(new GridBagLayout());
        textPanel.setOpaque(false);

        userLabel = new JLabel("Nickname: ");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("ArcadeClassic", Font.PLAIN, 25));

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        textPanel.add(userLabel, constraints);

        username = new JTextField(40);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        textPanel.add(username, constraints);

        passwordLabel = new JLabel("Password: ");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("ArcadeClassic", Font.PLAIN, 25));

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        textPanel.add(passwordLabel, constraints);

        password = new JPasswordField(40);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        textPanel.add(password, constraints);

        this.getContentPane().add(textPanel, BorderLayout.CENTER);
    }

    /**
     * Metodo de la accion del boton cerrar
     */
    private void cancel(){
        this.dispose();
    }

    /**
     * Metodo de la accion del boton login
     */
    private void login(){
        String nickname = username.getText();
        String password = new String(this.password.getPassword());

        textPanel.removeAll();
        prepareTextPanel();

        if(IOGameData.validateLogin(nickname, password)){
            TetrisGUI.User = nickname;

            JLabel loginSuccessful = new JLabel("Login     Successful");
            loginSuccessful.setForeground(Color.CYAN);
            loginSuccessful.setFont(new Font("ArcadeClassic", Font.PLAIN, 25));
            constraints.gridx = 2;
            constraints.gridy = 2;
            constraints.gridwidth = 3;
            textPanel.add(loginSuccessful, constraints);

        } else {
            JLabel wrongLogin = new JLabel("Wrong     user     or     password");
            wrongLogin.setForeground(Color.RED);
            wrongLogin.setFont(new Font("ArcadeClassic", Font.PLAIN, 25));

            constraints.gridx = 2;
            constraints.gridy = 2;
            constraints.gridwidth = 3;
            textPanel.add(wrongLogin, constraints);
        }
        this.pack();
        this.setLocationRelativeTo(null);
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
