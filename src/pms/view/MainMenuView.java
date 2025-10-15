package pms.view;

import javax.swing.*;
import java.awt.*;

public class MainMenuView extends JFrame {
    public MainMenuView() {
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

        panel.add(btnUsuarios);
        panel.add(btnHabitaciones);
        panel.add(btnClientes);
        panel.add(btnReservas);
        panel.add(btnReportes);

        add(panel);
    }
}