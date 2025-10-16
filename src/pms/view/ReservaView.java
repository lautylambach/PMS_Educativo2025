package pms.view;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.DefaultTableModel;
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
    private JTextField txtFechaCheckIn, txtFechaCheckOut, txtNotas;
    private JTable tableReservas;
    private DefaultTableModel tableModelReservas;
    private JButton btnCrear, btnModificar, btnCancelar;

    public ReservaView(String rol) {
        this.rolUsuario = rol;
        setTitle("Gestionar Reservas - PMS Educativo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        reservaController = new ReservaController();
        clienteController = new ClienteController();

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de entrada
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1;
        cmbClientes = new JComboBox<>();
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
        for (Cliente cliente : clienteController.listarClientes()) {
            cmbClientes.addItem(cliente);
        }
        inputPanel.add(cmbClientes, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Habitación:"), gbc);
        gbc.gridx = 1;
        cmbHabitaciones = new JComboBox<>();
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
        inputPanel.add(cmbHabitaciones, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Check-In (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        txtFechaCheckIn = new JTextField(12);
        inputPanel.add(txtFechaCheckIn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Check-Out (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        txtFechaCheckOut = new JTextField(12);
        inputPanel.add(txtFechaCheckOut, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Notas:"), gbc);
        gbc.gridx = 1;
        txtNotas = new JTextField(20);
        inputPanel.add(txtNotas, gbc);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnCrear = new JButton("Crear Reserva");
        btnModificar = new JButton("Modificar Reserva");
        btnCancelar = new JButton("Cancelar Reserva");
        buttonPanel.add(btnCrear);
        buttonPanel.add(btnModificar);
        buttonPanel.add(btnCancelar);

        // Habilitar botones según permisos
        boolean hasPermission = "Administrador".equals(rol) || "Recepcionista".equals(rol) || "Reservas".equals(rol);
        btnCrear.setEnabled(hasPermission);
        btnModificar.setEnabled(hasPermission);
        btnCancelar.setEnabled(hasPermission);
        if (!hasPermission) {
            JOptionPane.showMessageDialog(this, "No tienes permiso para gestionar reservas.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }

        // Tabla de reservas
        tableModelReservas = new DefaultTableModel(new Object[]{"ID", "Cliente", "Habitación", "Check-In", "Check-Out", "Notas"}, 0);
        tableReservas = new JTable(tableModelReservas);
        JScrollPane scrollPane = new JScrollPane(tableReservas);
        updateTableReservas();

        // Listeners
        btnCrear.addActionListener(e -> crearReserva());
        btnModificar.addActionListener(e -> modificarReserva());
        btnCancelar.addActionListener(e -> cancelarReserva());
        txtFechaCheckIn.addActionListener(e -> updateHabitacionesDisponibles());
        txtFechaCheckOut.addActionListener(e -> updateHabitacionesDisponibles());

        // Layout
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);

        txtFechaCheckIn.setText("2025-10-25");
        txtFechaCheckOut.setText("2025-10-27");
        updateHabitacionesDisponibles();

        add(panel);
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
            Reserva reserva = new Reserva(0, cliente.getIdCliente(), habitacion.getIdHabitacion(), fechaCheckIn, fechaCheckOut, txtNotas.getText());
            if (reservaController.crearReserva(reserva, rolUsuario)) {
                JOptionPane.showMessageDialog(this, "Reserva creada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                updateTableReservas();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear reserva o permiso insuficiente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarReserva() {
        int selectedRow = tableReservas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una reserva para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int idReserva = (int) tableModelReservas.getValueAt(selectedRow, 0);
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
            Reserva reserva = new Reserva(idReserva, cliente.getIdCliente(), habitacion.getIdHabitacion(), fechaCheckIn, fechaCheckOut, txtNotas.getText());
            if (reservaController.modificarReserva(reserva, rolUsuario)) {
                JOptionPane.showMessageDialog(this, "Reserva modificada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                updateTableReservas();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Error al modificar reserva o permiso insuficiente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelarReserva() {
        int selectedRow = tableReservas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una reserva para cancelar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idReserva = (int) tableModelReservas.getValueAt(selectedRow, 0);
        if (reservaController.cancelarReserva(idReserva, rolUsuario)) {
            JOptionPane.showMessageDialog(this, "Reserva cancelada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            updateTableReservas();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Error al cancelar reserva o permiso insuficiente.", "Error", JOptionPane.ERROR_MESSAGE);
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
            if (disponibles.isEmpty()) {
                cmbHabitaciones.addItem(new Habitacion(0, "Ninguna disponible", ""));
                JOptionPane.showMessageDialog(this, "No hay habitaciones disponibles para las fechas seleccionadas.", "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (Habitacion h : disponibles) {
                    cmbHabitaciones.addItem(h);
                }
            }
        } catch (ParseException e) {
            System.out.println("Error parsing fechas para disponibilidad: " + e.getMessage());
        }
    }

    private void updateTableReservas() {
        tableModelReservas.setRowCount(0);
        for (Reserva reserva : reservaController.listarReservas(rolUsuario)) {
            Cliente cliente = clienteController.getClientePorId(reserva.getIdCliente());
            String clienteNombre = (cliente != null) ? cliente.getNombre() : "Desconocido";
            tableModelReservas.addRow(new Object[]{
                reserva.getIdReserva(), clienteNombre, reserva.getIdHabitacion(),
                reserva.getFechaCheckIn(), reserva.getFechaCheckOut(), reserva.getNotas()
            });
        }
    }

    private void clearFields() {
        txtFechaCheckIn.setText("");
        txtFechaCheckOut.setText("");
        txtNotas.setText("");
        updateHabitacionesDisponibles();
    }
}