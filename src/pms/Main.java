package pms;

import javax.swing.*;
import java.sql.*;

import pms.view.LoginView; // Aseguramos que esté importado

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
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "PMS_DB";
        String username = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement stmt = conn.createStatement();

            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet rs = dbmd.getCatalogs();
            boolean dbExists = false;
            while (rs.next()) {
                if (rs.getString(1).equalsIgnoreCase(dbName)) { // Ignora mayúsculas/minúsculas
                    dbExists = true;
                    break;
                }
            }

            if (!dbExists) {
                try {
                    stmt.executeUpdate("CREATE DATABASE " + dbName);
                    System.out.println("Base de datos " + dbName + " creada.");
                } catch (SQLException e) {
                    // Ignora el error si la base ya existe (código 1007)
                    if (e.getErrorCode() != 1007) {
                        throw e; // Re-lanza otros errores
                    }
                    System.out.println("Base de datos " + dbName + " ya existe.");
                }

                conn = DriverManager.getConnection(url + dbName, username, password);

                stmt = conn.createStatement();
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Usuarios (" +
                    "id_usuario INT PRIMARY KEY AUTO_INCREMENT, " +
                    "nombre VARCHAR(50) NOT NULL, " +
                    "rol VARCHAR(50) NOT NULL, " +
                    "permisos VARCHAR(100) NOT NULL, " +
                    "contrasena VARCHAR(50) NOT NULL)");
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Habitaciones (" +
                    "id_habitacion INT PRIMARY KEY, " +
                    "tipo VARCHAR(20) NOT NULL, " +
                    "estado VARCHAR(20) NOT NULL DEFAULT 'clean')");

                stmt.executeUpdate("INSERT IGNORE INTO Usuarios (nombre, rol, permisos, contrasena) VALUES " +
                "('Administrador', 'Administrador', 'gestion_usuarios,gestion_reservas,gestion_habitaciones,gestion_clientes,gestion_reportes', 'admin123'), " +
                "('Recepcionista', 'Recepcionista', 'gestion_reservas,gestion_clientes', 'recep123'), " +
                "('Housekeeping', 'Housekeeping', 'gestion_habitaciones', 'house123'), " +
                "('Mantenimiento', 'Mantenimiento', 'gestion_habitaciones', 'maint123')");
                stmt.executeUpdate("INSERT IGNORE INTO Habitaciones (id_habitacion, tipo, estado) VALUES " +
                "(1, 'TWIN', 'clean'), (2, 'TWIN', 'clean'), (3, 'TWIN', 'clean'), (4, 'TWIN', 'clean'), (5, 'TWIN', 'clean'), " +
                "(6, 'KING', 'clean'), (7, 'KING', 'clean'), (8, 'KING', 'clean'), (9, 'KING', 'clean'), (10, 'KING', 'clean'), " +
                "(11, 'SUPERIOR', 'clean'), (12, 'SUPERIOR', 'clean'), " +
                "(14, 'SUITE', 'clean'), (15, 'SUITE', 'clean')");
                System.out.println("Tablas y datos predeterminados verificados en " + dbName);
            } else {
                System.out.println("Base de datos " + dbName + " ya existe. Conexión establecida.");
                conn = DriverManager.getConnection(url + dbName, username, password);
            }

            conn.close();
            return true;
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Driver JDBC no encontrado. Asegúrate de que mysql-connector-j-9.4.0.jar esté en lib/ y configurado en el classpath.");
            return false;
        } catch (SQLException e) {
            System.out.println("Error al inicializar la base de datos: " + e.getMessage());
            return false;
        }
    }
}