package pms.controller;

import pms.dao.HabitacionDAO;
import pms.model.Habitacion;
import java.util.List;

public class HabitacionController {
    private HabitacionDAO habitacionDAO = new HabitacionDAO();

    public List<Habitacion> listarHabitaciones() {
        return habitacionDAO.listar();
    }

    public boolean modificarEstado(int id, String nuevoEstado, String rolUsuario) {
        // Validar permiso: Administrador siempre puede, o si rol es Housekeeping/Mantenimiento
        if ("Administrador".equals(rolUsuario) || "Housekeeping".equals(rolUsuario) || "Mantenimiento".equals(rolUsuario)) {
            // Validar estado válido
            if (isValidEstado(nuevoEstado)) {
                return habitacionDAO.modificarEstado(id, nuevoEstado);
            }
        }
        return false;
    }

    public boolean tienePermisoGestionHabitaciones(String rol) {
        return "Administrador".equals(rol) || "Housekeeping".equals(rol) || "Mantenimiento".equals(rol);
    }

    private boolean isValidEstado(String estado) {
        return "clean".equals(estado) || "dirty".equals(estado) || "maintenance".equals(estado) ||
               "inspected".equals(estado) || "pick up".equals(estado);
    }
}