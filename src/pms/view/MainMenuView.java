package pms.view;

import javax.swing.*;
import java.awt.*;

public class MainMenuView extends JFrame {
    public MainMenuView(String rol) {
        setTitle("Menú Principal - PMS Educativo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnUsuarios = new JButton("Gestionar Usuarios");
        JButton btnHabitaciones = new JButton("Gestionar Habitaciones");
        JButton btnClientes = new JButton("Gestionar Clientes");
        JButton btnReservas = new JButton("Gestionar Reservas");
        JButton btnReportes = new JButton("Generar Reportes");

        // Habilitar/deshabilitar según rol
        if (!"Administrador".equals(rol)) {
            btnUsuarios.setEnabled(false); // Solo Administrador gestiona usuarios
            btnReportes.setEnabled(false); // Solo Administrador genera reportes
        }

        btnUsuarios.addActionListener(e -> {
            if ("Administrador".equals(rol)) {
                UsuarioView usuarioView = new UsuarioView();
                usuarioView.setVisible(true);
            }
        });

        panel.add(btnUsuarios);
        panel.add(btnHabitaciones);
        panel.add(btnClientes);
        panel.add(btnReservas);
        panel.add(btnReportes);

        add(panel);
    }
}