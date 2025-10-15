package pms.controller;

import pms.dao.ReservaDAO;
import pms.model.Reserva;
import pms.model.Habitacion;
import java.sql.Date;
import java.util.List;

public class ReservaController {
    private ReservaDAO reservaDAO = new ReservaDAO();

    public boolean crearReserva(Reserva reserva, String rol) {
        if (!"Administrador".equals(rol) && !"Reservas".equals(rol)) {
            return false;
        }
        if (!isValidReserva(reserva)) {
            return false;
        }
        return reservaDAO.crear(reserva);
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
               !reserva.getFechaCheckIn().before(new Date(System.currentTimeMillis()));
    }
}