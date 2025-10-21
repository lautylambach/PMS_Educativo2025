package pms.controller;

import pms.dao.ReservaDAO;
import pms.model.Reserva;
import pms.model.Habitacion;
import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

public class ReservaController {
    private ReservaDAO reservaDAO = new ReservaDAO();
//validacion de permisos de usuario
    public boolean crearReserva(Reserva reserva, String rol) {
        if (!hasPermission(rol, "gestion_reservas")) {
            return false;
        }
        if (!isValidReserva(reserva)) {
            return false;
        }
        return reservaDAO.crear(reserva);
    }

    public List<Reserva> listarReservas(String rol) {
        if (hasPermission(rol, "gestion_reservas")) {
            return reservaDAO.listarReservas();
        }
        return new ArrayList<>();
    }

    public boolean modificarReserva(Reserva reserva, String rol) {
        if (!hasPermission(rol, "gestion_reservas")) {
            return false;
        }
        if (!isValidReserva(reserva)) {
            return false;
        }
        return reservaDAO.modificar(reserva);
    }

    public boolean cancelarReserva(int idReserva, String rol) {
        if (!hasPermission(rol, "gestion_reservas")) {
            return false;
        }
        return reservaDAO.cancelar(idReserva);
    }

    public List<Habitacion> getHabitacionesDisponibles(Date fechaCheckIn, Date fechaCheckOut) {
        return reservaDAO.getHabitacionesDisponibles(fechaCheckIn, fechaCheckOut);
    }

    public List<Reserva> getReservasPorCliente(int idCliente) {
        return reservaDAO.getReservasPorCliente(idCliente);
    }

    private boolean isValidReserva(Reserva reserva) {
        if (reserva == null || reserva.getIdCliente() <= 0 || reserva.getIdHabitacion() <= 0 ||
            reserva.getFechaCheckIn() == null || reserva.getFechaCheckOut() == null) {
            return false;
        }
        return !reserva.getFechaCheckIn().after(reserva.getFechaCheckOut()) &&
               !reserva.getFechaCheckIn().before(new Date(System.currentTimeMillis() - 86400000)); // No permitir fechas pasadas
    }

    private boolean hasPermission(String rol, String requiredPermission) {
        return "Administrador".equals(rol) || "Recepcionista".equals(rol) || "Reservas".equals(rol);
    }
}