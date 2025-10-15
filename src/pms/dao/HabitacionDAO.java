package pms.dao;

import pms.model.Habitacion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HabitacionDAO {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/PMS_DB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public List<Habitacion> listar() {
        List<Habitacion> habitaciones = new ArrayList<>();
        String sql = "SELECT * FROM Habitaciones";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                habitaciones.add(new Habitacion(rs.getInt("id_habitacion"), rs.getString("tipo"), rs.getString("estado")));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar habitaciones: " + e.getMessage());
        }
        return habitaciones;
    }

    public boolean modificarEstado(int id, String nuevoEstado) {
        String sql = "UPDATE Habitaciones SET estado = ? WHERE id_habitacion = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error al modificar estado: " + e.getMessage());
            return false;
        }
    }
}