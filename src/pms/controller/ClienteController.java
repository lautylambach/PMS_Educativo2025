package pms.controller;

import java.util.List;
import pms.dao.ClienteDAO;
import pms.dao.ReservaDAO;
import pms.model.Cliente;
import pms.model.Reserva;

public class ClienteController {
    private ClienteDAO clienteDAO = new ClienteDAO();
    private ReservaDAO reservaDAO = new ReservaDAO();

    public boolean crearCliente(Cliente cliente, String rol) {
        if (isValidCliente(cliente) && ("Administrador".equals(rol) || "Reservas".equals(rol))) {
            return clienteDAO.crear(cliente);
        }
        return false;
    }

    public boolean modificarCliente(Cliente cliente, String rol) {
        if (isValidCliente(cliente) && ("Administrador".equals(rol) || "Reservas".equals(rol))) {
            return clienteDAO.modificar(cliente);
        }
        return false;
    }

    public boolean borrarCliente(int id, String rol) {
        if ("Administrador".equals(rol)) {
            return clienteDAO.borrar(id);
        }
        return false;
    }

    public List<Cliente> listarClientes() {
        return clienteDAO.listar();
    }

    public List<Reserva> getHistorialReservas(int idCliente) {
        return reservaDAO.getReservasPorCliente(idCliente);
    }

    private boolean isValidCliente(Cliente cliente) {
        if (cliente == null || cliente.getNombre() == null || cliente.getNombre().isEmpty() ||
            cliente.getDocumento() == null || cliente.getDocumento().isEmpty()) {
            return false;
        }
        if (cliente.getCorreo() != null && !cliente.getCorreo().isEmpty()) {
            return cliente.getCorreo().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        }
        return true;
    }
    public Cliente getClientePorId(int idCliente) {
    for (Cliente cliente : clienteDAO.listar()) {
        if (cliente.getIdCliente() == idCliente) {
            return cliente;
        }
    }
    return null;
}
}