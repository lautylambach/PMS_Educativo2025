package pms.controller;

import pms.dao.ClienteDAO;
import pms.model.Cliente;
import java.util.List;

public class ClienteController {
    private ClienteDAO clienteDAO = new ClienteDAO();

    public boolean crearCliente(Cliente cliente, String rol) {
        if (isValidCliente(cliente) && tienePermisoModificacion(rol)) {
            return clienteDAO.crear(cliente);
        }
        return false;
    }

    public boolean modificarCliente(Cliente cliente, String rol) {
        if (isValidCliente(cliente) && tienePermisoModificacion(rol)) {
            return clienteDAO.modificar(cliente);
        }
        return false;
    }

    public boolean borrarCliente(int id, String rol) {
        if ("Administrador".equals(rol)) { // Solo admin puede borrar
            return clienteDAO.borrar(id);
        }
        return false;
    }

    public List<Cliente> listarClientes() {
        return clienteDAO.listar();
    }

    /*  TODO: Implementar cuando tengamos ReservaDAO
    public List<Reserva> getHistorialReservas(int idCliente) {
        return new ArrayList<>(); // Placeholder: fetch from ReservaDAO
    }*/

    private boolean isValidCliente(Cliente cliente) {
        return cliente != null && cliente.getNombre() != null && !cliente.getNombre().isEmpty() &&
               cliente.getDocumento() != null && !cliente.getDocumento().isEmpty();
        
    }

    public boolean tienePermisoModificacion(String rol) {
        return "Administrador".equals(rol) || "Recepcionista".equals(rol);
    }
}