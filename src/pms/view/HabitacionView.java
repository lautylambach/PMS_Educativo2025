package pms.view;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import pms.controller.HabitacionController;
import pms.model.Habitacion;

public class HabitacionView extends JFrame {
    private String rolUsuario;
    private HabitacionController habitacionController;
    private JTable tableHabitaciones;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbEstado;
    private JButton btnModificar, btnRefrescar;

    public HabitacionView(String rol) {
        this.rolUsuario = rol;
        setTitle("Gestionar Habitaciones - PMS Educativo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        habitacionController = new HabitacionController();

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabla para listar habitaciones (CU-003)
        tableModel = new DefaultTableModel(new Object[]{"ID", "Tipo", "Estado"}, 0);
        tableHabitaciones = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableHabitaciones);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel para modificación (CU-004)
        JPanel modPanel = new JPanel(new FlowLayout());
        JLabel lblEstado = new JLabel("Nuevo Estado:");
        cmbEstado = new JComboBox<>(new String[]{"clean", "dirty", "maintenance", "inspected", "pick up"});
        btnModificar = new JButton("Modificar Estado");
        btnRefrescar = new JButton("Refrescar Lista");

        modPanel.add(lblEstado);
        modPanel.add(cmbEstado);
        modPanel.add(btnModificar);
        modPanel.add(btnRefrescar);

        // Habilitar modificación solo si tiene permiso
        boolean tienePermiso = habitacionController.tienePermisoGestionHabitaciones(rolUsuario);
        btnModificar.setEnabled(tienePermiso);
        if (!tienePermiso) {
            JOptionPane.showMessageDialog(this, "No tienes permiso para modificar estados. Solo visualización.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }

        btnModificar.addActionListener(e -> {
            int selectedRow = tableHabitaciones.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                String nuevoEstado = (String) cmbEstado.getSelectedItem();
                if (habitacionController.modificarEstado(id, nuevoEstado, rolUsuario)) {
                    JOptionPane.showMessageDialog(this, "Estado modificado exitosamente.");
                    updateTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al modificar estado o permiso insuficiente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una habitación para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnRefrescar.addActionListener(e -> updateTable());

        panel.add(modPanel, BorderLayout.SOUTH);
        add(panel);

        // Cargar lista inicial (tiempo real desde DB)
        updateTable();
    }

    private void updateTable() {
        tableModel.setRowCount(0); // Limpiar tabla
        for (Habitacion habitacion : habitacionController.listarHabitaciones()) {
            tableModel.addRow(new Object[]{habitacion.getIdHabitacion(), habitacion.getTipo(), habitacion.getEstado()});
        }
    }
}