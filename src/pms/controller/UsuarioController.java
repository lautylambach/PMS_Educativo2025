package pms.controller;

import java.util.List;

import pms.dao.UsuarioDAO;
import pms.model.Usuario;

public class UsuarioController {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public boolean crearUsuario(Usuario usuario) {
        if (isValidUsuario(usuario)) {
            return usuarioDAO.crearUsuario(usuario);
        }
        return false;
    }

    public boolean modificarUsuario(Usuario usuario) {
        if (isValidUsuario(usuario)) {
            return usuarioDAO.modificarUsuario(usuario.getIdUsuario(), usuario);
        }
        return false;
    }

    public List<Usuario> listarUsuarios() {
        return usuarioDAO.listarUsuarios();
    }

    private boolean isValidUsuario(Usuario usuario) {
        return usuario != null && usuario.getNombre() != null && !usuario.getNombre().isEmpty() &&
               usuario.getRol() != null && !usuario.getRol().isEmpty() &&
               usuario.getPermisos() != null && !usuario.getPermisos().isEmpty() &&
               usuario.getContrasena() != null && !usuario.getContrasena().isEmpty();
    }
}