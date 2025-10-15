package pms.view;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import pms.controller.ReservaController;
import pms.controller.ClienteController;
import pms.model.Cliente;
import pms.model.Habitacion;
import pms.model.Reserva;

public class ReservaView extends JFrame {
    private String rolUsuario;
    private ReservaController reservaController;
    private ClienteController clienteController;
    private JComboBox<Cliente> cmbClientes;
    private JComboBox<Habitacion> cmbHabitaciones;
    private JTextField txtFechaCheckIn, txtFechaCheckOut;
    private JButton btnCrear;

    public ReservaView(String rol) {
        this.rolUsuario = rol;
        setTitle("Crear Reserva - PMS Educativo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null);

        reservaController = new ReservaController();
        clienteController = new ClienteController();

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Cliente
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1;
        cmbClientes = new JComboBox<>();
        // Configurar renderizado personalizado para Cliente
        cmbClientes.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Cliente) {
                    Cliente cliente = (Cliente) value;
                    setText(cliente.getNombre() + " - " + cliente.getDocumento());
                }
                return this;
            }
        });
        // Cargar clientes
        for (Cliente cliente : clienteController.listarClientes()) {
            System.out.println("Cargando cliente: " + cliente.getNombre()); // Debug
            cmbClientes.addItem(cliente);
        }
        panel.add(cmbClientes, gbc);

        // Habitación
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Habitación:"), gbc);
        gbc.gridx = 1;
        cmbHabitaciones = new JComboBox<>();
        // Configurar renderizado personalizado para Habitacion
        cmbHabitaciones.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Habitacion) {
                    Habitacion habitacion = (Habitacion) value;
                    setText("Hab " + habitacion.getIdHabitacion() + " (" + habitacion.getTipo() + ")");
                }
                return this;
            }
        });
        panel.add(cmbHabitaciones, gbc);

        // Fechas
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Check-In (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        txtFechaCheckIn = new JTextField(12);
        panel.add(txtFechaCheckIn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Check-Out (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        txtFechaCheckOut = new JTextField(12);
        panel.add(txtFechaCheckOut, gbc);

        // Botón
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        btnCrear = new JButton("Crear Reserva");
        panel.add(btnCrear, gbc);

        // Listeners para actualizar habitaciones disponibles
        txtFechaCheckIn.addActionListener(e -> updateHabitacionesDisponibles());
        txtFechaCheckOut.addActionListener(e -> updateHabitacionesDisponibles());

        // Acción del botón crear
        btnCrear.addActionListener(e -> crearReserva());

        // Deshabilitar si no tiene permisos
        if (!"Administrador".equals(rol) && !"Reservas".equals(rol)) {
            btnCrear.setEnabled(false);
            JOptionPane.showMessageDialog(this, "No tienes permiso para crear reservas.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }

        // Valores por defecto para testing
        txtFechaCheckIn.setText("2025-10-25");
        txtFechaCheckOut.setText("2025-10-27");
        updateHabitacionesDisponibles();

        add(panel);
        pack();
    }

    private void crearReserva() {
        try {
            Cliente cliente = (Cliente) cmbClientes.getSelectedItem();
            Habitacion habitacion = (Habitacion) cmbHabitaciones.getSelectedItem();
            
            if (cliente == null || habitacion == null) {
                JOptionPane.showMessageDialog(this, "Seleccione cliente y habitación.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            Date fechaCheckIn = new Date(sdf.parse(txtFechaCheckIn.getText()).getTime());
            Date fechaCheckOut = new Date(sdf.parse(txtFechaCheckOut.getText()).getTime());

            if (fechaCheckIn.after(fechaCheckOut)) {
                JOptionPane.showMessageDialog(this, "Check-Out debe ser después de Check-In.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Reserva reserva = new Reserva(0, cliente.getIdCliente(), habitacion.getIdHabitacion(), fechaCheckIn, fechaCheckOut);
            if (reservaController.crearReserva(reserva, rolUsuario)) {
                JOptionPane.showMessageDialog(this, "Reserva creada exitosamente para " + cliente.getNombre() + 
                    " en habitación " + habitacion.getIdHabitacion(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear reserva o permiso insuficiente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use YYYY-MM-DD (ej: 2025-10-25).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateHabitacionesDisponibles() {
        try {
            if (txtFechaCheckIn.getText().isEmpty() || txtFechaCheckOut.getText().isEmpty()) {
                return;
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaCheckIn = new Date(sdf.parse(txtFechaCheckIn.getText()).getTime());
            Date fechaCheckOut = new Date(sdf.parse(txtFechaCheckOut.getText()).getTime());
            
            cmbHabitaciones.removeAllItems();
            List<Habitacion> disponibles = reservaController.getHabitacionesDisponibles(fechaCheckIn, fechaCheckOut);
            
            System.out.println("Habitaciones disponibles para " + fechaCheckIn + " a " + fechaCheckOut + ": " + disponibles.size());
            
            if (disponibles.isEmpty()) {
                cmbHabitaciones.addItem(new Habitacion(0, "Ninguna disponible", ""));
                JOptionPane.showMessageDialog(this, "No hay habitaciones disponibles para las fechas seleccionadas.", "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (Habitacion h : disponibles) {
                    System.out.println("Agregando habitación: " + h.toString());
                    cmbHabitaciones.addItem(h);
                }
            }
        } catch (ParseException e) {
            System.out.println("Error parsing fechas para disponibilidad: " + e.getMessage());
        }
    }
}