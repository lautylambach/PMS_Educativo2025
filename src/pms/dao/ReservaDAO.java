package pms.dao;

import pms.model.Reserva;
import pms.model.Habitacion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/PMS_DB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public boolean crear(Reserva reserva) {
        String sql = "INSERT INTO Reservas (id_cliente, id_habitacion, fecha_checkin, fecha_checkout) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reserva.getIdCliente());
            pstmt.setInt(2, reserva.getIdHabitacion());
            pstmt.setDate(3, reserva.getFechaCheckIn());
            pstmt.setDate(4, reserva.getFechaCheckOut());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error al crear reserva: " + e.getMessage());
            return false;
        }
    }

    public List<Habitacion> getHabitacionesDisponibles(Date fechaCheckIn, Date fechaCheckOut) {
        List<Habitacion> disponibles = new ArrayList<>();
        String sql = "SELECT h.* FROM Habitaciones h " +
                     "WHERE h.estado = 'clean' AND h.id_habitacion NOT IN (" +
                     "SELECT r.id_habitacion FROM Reservas r " +
                     "WHERE (r.fecha_checkin <= ? AND r.fecha_checkout >= ?) OR " +
                     "(r.fecha_checkin <= ? AND r.fecha_checkout >= ?) OR " +
                     "(r.fecha_checkin >= ? AND r.fecha_checkout <= ?))";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, fechaCheckOut);
            pstmt.setDate(2, fechaCheckIn);
            pstmt.setDate(3, fechaCheckIn);
            pstmt.setDate(4, fechaCheckOut);
            pstmt.setDate(5, fechaCheckIn);
            pstmt.setDate(6, fechaCheckOut);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                disponibles.add(new Habitacion(rs.getInt("id_habitacion"), rs.getString("tipo"), rs.getString("estado")));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar habitaciones disponibles: " + e.getMessage());
        }
        return disponibles;
    }

    public List<Reserva> getReservasPorCliente(int idCliente) {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM Reservas WHERE id_cliente = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCliente);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                reservas.add(new Reserva(rs.getInt("id_reserva"), rs.getInt("id_cliente"),
                        rs.getInt("id_habitacion"), rs.getDate("fecha_checkin"), rs.getDate("fecha_checkout")));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar reservas por cliente: " + e.getMessage());
        }
        return reservas;
    }
}