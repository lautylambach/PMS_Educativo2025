package pms.model;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String rol;
    private String permisos;
    private String contrasena;

    // Constructor vacío
    public Usuario() {}

    // Constructor con todos los campos
    public Usuario(int idUsuario, String nombre, String rol, String permisos, String contrasena) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.rol = rol;
        this.permisos = permisos;
        this.contrasena = contrasena;
    }

    // Getters y Setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getPermisos() { return permisos; }
    public void setPermisos(String permisos) { this.permisos = permisos; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}