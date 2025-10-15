package pms.model;

public class Habitacion {
    private int idHabitacion;
    private String tipo;
    private String estado;

    // Constructor vacío
    public Habitacion() {}

    // Constructor con todos los campos
    public Habitacion(int idHabitacion, String tipo, String estado) {
        this.idHabitacion = idHabitacion;
        this.tipo = tipo;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(int idHabitacion) { this.idHabitacion = idHabitacion; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}