package pms.view;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import pms.controller.UsuarioController;
import pms.model.Usuario;

public class UsuarioView extends JFrame {
    private JTextField txtNombre, txtRol, txtPermisos, txtContrasena;
    private JButton btnCrear, btnModificar, btnListar;
    private UsuarioController usuarioController;
    private JTable tableUsuarios;
    private DefaultTableModel tableModel;

    public UsuarioView() {
        setTitle("Gestionar Usuarios - PMS Educativo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        usuarioController = new UsuarioController();

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
        inputPanel.add(new JLabel("Rol:"), gbc);
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Permisos:"), gbc);
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Contraseña:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        txtNombre = new JTextField(15);
        inputPanel.add(txtNombre, gbc);
        gbc.gridy = 1;
        txtRol = new JTextField(15);
        inputPanel.add(txtRol, gbc);
        gbc.gridy = 2;
        txtPermisos = new JTextField(15);
        inputPanel.add(txtPermisos, gbc);
        gbc.gridy = 3;
        txtContrasena = new JTextField(15);
        inputPanel.add(txtContrasena, gbc);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnCrear = new JButton("Crear Usuario");
        btnModificar = new JButton("Modificar Usuario");
        btnListar = new JButton("Listar Usuarios");
        buttonPanel.add(btnCrear);
        buttonPanel.add(btnModificar);
        buttonPanel.add(btnListar);

        // Tabla
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Rol", "Permisos", "Contraseña"}, 0);
        tableUsuarios = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableUsuarios);

        // Acción de botones
        btnCrear.addActionListener(e -> {
            Usuario usuario = new Usuario(0, txtNombre.getText(), txtRol.getText(), txtPermisos.getText(), txtContrasena.getText());
            if (usuarioController.crearUsuario(usuario)) {
                JOptionPane.showMessageDialog(this, "Usuario creado exitosamente.");
                clearFields();
                updateTable();
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnModificar.addActionListener(e -> {
            int selectedRow = tableUsuarios.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                Usuario usuario = new Usuario(id, txtNombre.getText(), txtRol.getText(), txtPermisos.getText(), txtContrasena.getText());
                if (usuarioController.modificarUsuario(usuario)) {
                    JOptionPane.showMessageDialog(this, "Usuario modificado exitosamente.");
                    clearFields();
                    updateTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al modificar usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un usuario para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnListar.addActionListener(e -> updateTable());

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);

        add(panel);
    }

    private void clearFields() {
        txtNombre.setText("");
        txtRol.setText("");
        txtPermisos.setText("");
        txtContrasena.setText("");
    }

    private void updateTable() {
        tableModel.setRowCount(0); // Limpiar tabla
        for (Usuario usuario : usuarioController.listarUsuarios()) {
            tableModel.addRow(new Object[]{usuario.getIdUsuario(), usuario.getNombre(), usuario.getRol(), usuario.getPermisos(), usuario.getContrasena()});
        }
    }
}