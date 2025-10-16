package pms;

import javax.swing.*;
import java.sql.*;

import pms.view.LoginView;

public class Main {
    public static void main(String[] args) {
        if (!initializeDatabase()) {
            JOptionPane.showMessageDialog(null, "No se pudo inicializar o conectar a la base de datos. Asegúrate de que MySQL esté en ejecución y el driver JDBC esté configurado.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }

    private static boolean initializeDatabase() {
        String urlBase = "jdbc:mysql://localhost:3306/";
        String dbName = "PMS_DB";
        String username = "root";
        String password = "";

        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver JDBC cargado.");

            conn = DriverManager.getConnection(urlBase, username, password);
            stmt = conn.createStatement();
            System.out.println("Conexión inicial al servidor MySQL establecida.");

            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet rs = dbmd.getCatalogs();
            boolean dbExists = false;
            while (rs.next()) {
                if (rs.getString(1).equalsIgnoreCase(dbName)) {
                    dbExists = true;
                    break;
                }
            }
            rs.close();

            if (!dbExists) {
                stmt.executeUpdate("CREATE DATABASE " + dbName);
                System.out.println("Base de datos " + dbName + " creada.");
            } else {
                System.out.println("Base de datos " + dbName + " ya existe.");
            }

            conn.close();
            conn = DriverManager.getConnection(urlBase + dbName, username, password);
            stmt = conn.createStatement();
            System.out.println("Conexión a " + dbName + " establecida.");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Usuarios (" +
                "id_usuario INT PRIMARY KEY AUTO_INCREMENT, " +
                "nombre VARCHAR(50) NOT NULL, " +
                "rol VARCHAR(50) NOT NULL, " +
                "permisos VARCHAR(100) NOT NULL, " +
                "contrasena VARCHAR(50) NOT NULL)");
            System.out.println("Tabla Usuarios verificada/creada.");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Habitaciones (" +
                "id_habitacion INT PRIMARY KEY, " +
                "tipo VARCHAR(20) NOT NULL, " +
                "estado VARCHAR(20) NOT NULL DEFAULT 'clean')");
            System.out.println("Tabla Habitaciones verificada/creada.");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Clientes (" +
                "id_cliente INT PRIMARY KEY AUTO_INCREMENT, " +
                "nombre VARCHAR(100) NOT NULL, " +
                "documento VARCHAR(50) NOT NULL UNIQUE, " +
                "correo VARCHAR(100), " +
                "telefono VARCHAR(50))");
            System.out.println("Tabla Clientes verificada/creada.");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Reservas (" +
                "id_reserva INT PRIMARY KEY AUTO_INCREMENT, " +
                "id_cliente INT NOT NULL, " +
                "id_habitacion INT NOT NULL, " +
                "fecha_checkin DATE NOT NULL, " +
                "fecha_checkout DATE NOT NULL, " +
                "notas TEXT, " +
                "FOREIGN KEY (id_cliente) REFERENCES Clientes(id_cliente), " +
                "FOREIGN KEY (id_habitacion) REFERENCES Habitaciones(id_habitacion))");
            System.out.println("Tabla Reservas verificada/creada.");

            stmt.executeUpdate("INSERT IGNORE INTO Usuarios (nombre, rol, permisos, contrasena) VALUES " +
                "('Administrador', 'Administrador', 'gestion_usuarios,gestion_reservas,gestion_habitaciones,gestion_clientes,gestion_reportes', 'admin123'), " +
                "('Recepcionista', 'Recepcionista', 'gestion_reservas', 'recep123'), " +
                "('Housekeeping', 'Housekeeping', 'gestion_habitaciones', 'house123'), " +
                "('Mantenimiento', 'Mantenimiento', 'gestion_habitaciones', 'maint123'), " +
                "('Reservas', 'Reservas', 'gestion_reservas,gestion_clientes', 'reserva123')");
            System.out.println("Usuarios iniciales insertados.");

            stmt.executeUpdate("INSERT IGNORE INTO Habitaciones (id_habitacion, tipo, estado) VALUES " +
                "(1, 'TWIN', 'clean'), (2, 'TWIN', 'clean'), (3, 'TWIN', 'clean'), (4, 'TWIN', 'clean'), (5, 'TWIN', 'clean'), " +
                "(6, 'KING', 'clean'), (7, 'KING', 'clean'), (8, 'KING', 'clean'), (9, 'KING', 'clean'), (10, 'KING', 'clean'), " +
                "(11, 'SUPERIOR', 'clean'), (12, 'SUPERIOR', 'clean'), " +
                "(14, 'SUITE', 'clean'), (15, 'SUITE', 'clean')");
            System.out.println("Habitaciones iniciales insertadas.");

            stmt.executeUpdate("INSERT IGNORE INTO Clientes (nombre, documento, correo, telefono) VALUES " +
                "('Juan Perez', '12345678', 'juan@example.com', '123-456-7890'), " +
                "('Maria Lopez', '87654321', 'maria@example.com', '987-654-3210')");
            System.out.println("Clientes iniciales insertados.");

            stmt.executeUpdate("INSERT IGNORE INTO Reservas (id_cliente, id_habitacion, fecha_checkin, fecha_checkout, notas) VALUES " +
                "(1, 1, '2025-10-20', '2025-10-22', 'Nota inicial para Juan'), " +
                "(2, 6, '2025-10-21', '2025-10-23', 'Reserva con desayuno incluido')");
            System.out.println("Reservas iniciales insertadas.");

            return true;
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Driver JDBC no encontrado. Asegúrate de que mysql-connector-j-9.4.0.jar esté en lib/ y configurado en el classpath.");
            return false;
        } catch (SQLException e) {
            System.out.println("Error SQL al inicializar la base de datos: " + e.getMessage() + " (Código: " + e.getErrorCode() + ")");
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}