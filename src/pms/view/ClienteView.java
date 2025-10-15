package pms.view;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import pms.controller.ClienteController;
import pms.model.Cliente;

public class ClienteView extends JFrame {
    private String rolUsuario;
    private ClienteController clienteController;
    private JTextField txtNombre, txtDocumento, txtCorreo, txtTelefono;
    private JButton btnCrear, btnModificar, btnBorrar, btnListar;
    private JTable tableClientes, tableHistorial;
    private DefaultTableModel tableModelClientes, tableModelHistorial;

    public ClienteView(String rol) {
        this.rolUsuario = rol;
        setTitle("Gestionar Clientes - PMS Educativo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        clienteController = new ClienteController();

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de entrada
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Documento:"), gbc);
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Correo:"), gbc);
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Teléfono:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        txtNombre = new JTextField(15);
        inputPanel.add(txtNombre, gbc);
        gbc.gridy = 1;
        txtDocumento = new JTextField(15);
        inputPanel.add(txtDocumento, gbc);
        gbc.gridy = 2;
        txtCorreo = new JTextField(15);
        inputPanel.add(txtCorreo, gbc);
        gbc.gridy = 3;
        txtTelefono = new JTextField(15);
        inputPanel.add(txtTelefono, gbc);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnCrear = new JButton("Crear Cliente");
        btnModificar = new JButton("Modificar Cliente");
        btnBorrar = new JButton("Borrar Cliente");
        btnListar = new JButton("Listar Clientes");
        buttonPanel.add(btnCrear);
        buttonPanel.add(btnModificar);
        buttonPanel.add(btnBorrar);
        buttonPanel.add(btnListar);

        // Habilitar botones según permisos
        boolean puedeModificar = clienteController.tienePermisoModificacion(rolUsuario); 
        btnCrear.setEnabled(puedeModificar);
        btnModificar.setEnabled(puedeModificar);
        btnBorrar.setEnabled("Administrador".equals(rolUsuario)); // Solo admin

        // Tabla clientes
        tableModelClientes = new DefaultTableModel(new Object[]{"ID", "Nombre", "Documento", "Correo", "Teléfono"}, 0);
        tableClientes = new JTable(tableModelClientes);
        JScrollPane scrollClientes = new JScrollPane(tableClientes);
        /*
        // Tabla historial reservas (placeholder)
        tableModelHistorial = new DefaultTableModel(new Object[]{"ID Reserva", "Check-In", "Check-Out", "Habitación"}, 0);
        tableHistorial = new JTable(tableModelHistorial);
        JScrollPane scrollHistorial = new JScrollPane(tableHistorial);
        JPanel historialPanel = new JPanel(new BorderLayout());
        historialPanel.add(new JLabel("Historial de Reservas (seleccione un cliente para ver)"), BorderLayout.NORTH);
        historialPanel.add(scrollHistorial, BorderLayout.CENTER); */

        // Acciones
        btnCrear.addActionListener(e -> {
            Cliente cliente = new Cliente(0, txtNombre.getText(), txtDocumento.getText(), txtCorreo.getText(), txtTelefono.getText());
            if (clienteController.crearCliente(cliente, rolUsuario)) {
                JOptionPane.showMessageDialog(this, "Cliente creado exitosamente.");
                clearFields();
                updateTableClientes();
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear cliente o permiso insuficiente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnModificar.addActionListener(e -> {
            int selectedRow = tableClientes.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tableModelClientes.getValueAt(selectedRow, 0);
                Cliente cliente = new Cliente(id, txtNombre.getText(), txtDocumento.getText(), txtCorreo.getText(), txtTelefono.getText());
                if (clienteController.modificarCliente(cliente, rolUsuario)) {
                    JOptionPane.showMessageDialog(this, "Cliente modificado exitosamente.");
                    clearFields();
                    updateTableClientes();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al modificar cliente o permiso insuficiente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un cliente para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnBorrar.addActionListener(e -> {
            int selectedRow = tableClientes.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tableModelClientes.getValueAt(selectedRow, 0);
                if (clienteController.borrarCliente(id, rolUsuario)) {
                    JOptionPane.showMessageDialog(this, "Cliente borrado exitosamente.");
                    updateTableClientes();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al borrar cliente o permiso insuficiente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un cliente para borrar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnListar.addActionListener(e -> updateTableClientes());

        /*// Actualizar historial al seleccionar cliente
        tableClientes.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = tableClientes.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tableModelClientes.getValueAt(selectedRow, 0);
                updateTableHistorial(id);
            }
        });*/

        // Layout
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(scrollClientes, BorderLayout.SOUTH);
        add(panel, BorderLayout.CENTER);
        /*add(historialPanel, BorderLayout.EAST); */

        // Cargar lista inicial
        updateTableClientes();
    }

    private void clearFields() {
        txtNombre.setText("");
        txtDocumento.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
    }

    private void updateTableClientes() {
        tableModelClientes.setRowCount(0);
        for (Cliente cliente : clienteController.listarClientes()) {
            tableModelClientes.addRow(new Object[]{cliente.getIdCliente(), cliente.getNombre(), cliente.getDocumento(), cliente.getCorreo(), cliente.getTelefono()});
        }
    }

    /*private void updateTableHistorial(int idCliente) {
        tableModelHistorial.setRowCount(0);
        // TODO: Cargar reservas reales
        for (Reserva reserva : clienteController.getHistorialReservas(idCliente)) {
            tableModelHistorial.addRow(new Object[]{reserva.getIdReserva(), reserva.getFechaCheckIn(), reserva.getFechaCheckOut(), reserva.getIdHabitacion()});
        }
        if (tableModelHistorial.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay reservas para este cliente (aún).", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }*/
}