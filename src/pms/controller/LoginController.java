package pms.controller;

import pms.dao.UsuarioDAO;

public class LoginController {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public boolean validateLogin(String username, String password) {
        return usuarioDAO.authenticate(username, password);
    }
}