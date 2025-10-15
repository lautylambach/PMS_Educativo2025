package pms.view;

import javax.swing.*;
import java.awt.*;
import pms.view.HabitacionView;
import pms.view.ClienteView;
import pms.view.ReservaView;

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
        btnUsuarios.setEnabled("Administrador".equals(rol));
        btnHabitaciones.setEnabled("Administrador".equals(rol) || "Housekeeping".equals(rol) || "Mantenimiento".equals(rol));
        btnClientes.setEnabled("Administrador".equals(rol) || "Reservas".equals(rol));
        btnReservas.setEnabled("Administrador".equals(rol) || "Reservas".equals(rol));
        btnReportes.setEnabled("Administrador".equals(rol));

        btnUsuarios.addActionListener(e -> {
            if ("Administrador".equals(rol)) {
                UsuarioView usuarioView = new UsuarioView();
                usuarioView.setVisible(true);
            }
        });

        btnHabitaciones.addActionListener(e -> {
            if ("Administrador".equals(rol) || "Housekeeping".equals(rol) || "Mantenimiento".equals(rol)) {
                HabitacionView habitacionView = new HabitacionView(rol);
                habitacionView.setVisible(true);
            }
        });

        btnClientes.addActionListener(e -> {
            if ("Administrador".equals(rol) || "Reservas".equals(rol)) {
                ClienteView clienteView = new ClienteView(rol);
                clienteView.setVisible(true);
            }
        });

        btnReservas.addActionListener(e -> {
            if ("Administrador".equals(rol) || "Reservas".equals(rol)) {
                ReservaView reservaView = new ReservaView(rol);
                reservaView.setVisible(true);
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