package pms.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public boolean crearUsuario(Usuario usuario) {
        String sql = "INSERT INTO Usuarios (nombre, rol, permisos, contrasena) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getRol());
            pstmt.setString(3, usuario.getPermisos());
            pstmt.setString(4, usuario.getContrasena());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error al crear usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarUsuario(int id, Usuario usuario) {
        String sql = "UPDATE Usuarios SET nombre = ?, rol = ?, permisos = ?, contrasena = ? WHERE id_usuario = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getRol());
            pstmt.setString(3, usuario.getPermisos());
            pstmt.setString(4, usuario.getContrasena());
            pstmt.setInt(5, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error al modificar usuario: " + e.getMessage());
            return false;
        }
    }

    public List<Usuario> listarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                usuarios.add(new Usuario(rs.getInt("id_usuario"), rs.getString("nombre"), rs.getString("rol"), rs.getString("permisos"), rs.getString("contrasena")));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }
        return usuarios;
    }
}