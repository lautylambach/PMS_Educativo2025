package pms.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import pms.model.Usuario;

public class UsuarioDAO {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/PMS_DB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public Usuario authenticate(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM Usuarios WHERE nombre = ? AND contrasena = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new Usuario(rs.getInt("id_usuario"), rs.getString("nombre"), rs.getString("rol"), rs.getString("permisos"), rs.getString("contrasena"));
                    }
                    return null;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en autenticación: " + e.getMessage());
            return null;
        }
    }
}