package pms.controller;

import pms.dao.UsuarioDAO;
import pms.model.Usuario;

public class LoginController {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
//Validacion de login
    public Usuario validateLogin(String username, String password) {
        try {
            return usuarioDAO.authenticate(username, password);
        } catch (Exception e) {
            System.out.println("Error en autenticación: " + e.getMessage());
            return null;
        }
    }
}