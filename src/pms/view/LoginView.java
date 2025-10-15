package pms.view;

import javax.swing.*;
import java.awt.*;
import pms.controller.LoginController;
import pms.model.Usuario;

public class LoginView extends JFrame {
    private JTextField userField;
    private JPasswordField passField;

    public LoginView() {
        setTitle("Login - PMS Educativo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel userLabel = new JLabel("Usuario:");
        userField = new JTextField(15);
        JLabel passLabel = new JLabel("Contraseña:");
        passField = new JPasswordField(15);
        JButton loginButton = new JButton("Iniciar Sesión");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userLabel, gbc);
        gbc.gridy = 1;
        panel.add(passLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(userField, gbc);
        gbc.gridy = 1;
        panel.add(passField, gbc);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            LoginController controller = new LoginController();
            Usuario usuario = controller.validateLogin(username, password);
            if (usuario != null) {
                dispose();
                MainMenuView mainMenuView = new MainMenuView(usuario.getRol());
                mainMenuView.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(panel);
    }
}