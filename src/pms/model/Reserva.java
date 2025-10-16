package pms.model;

import java.sql.Date;

public class Reserva {
    private int idReserva;
    private int idCliente;
    private int idHabitacion;
    private Date fechaCheckIn;
    private Date fechaCheckOut;
    private String notas;

    // Constructor vacío
    public Reserva() {}

    // Constructor con todos los campos
    public Reserva(int idReserva, int idCliente, int idHabitacion, Date fechaCheckIn, Date fechaCheckOut, String notas) {
        this.idReserva = idReserva;
        this.idCliente = idCliente;
        this.idHabitacion = idHabitacion;
        this.fechaCheckIn = fechaCheckIn;
        this.fechaCheckOut = fechaCheckOut;
        this.notas = notas;
    }

    // Getters y Setters
    public int getIdReserva() { return idReserva; }
    public void setIdReserva(int idReserva) { this.idReserva = idReserva; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public int getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(int idHabitacion) { this.idHabitacion = idHabitacion; }
    public Date getFechaCheckIn() { return fechaCheckIn; }
    public void setFechaCheckIn(Date fechaCheckIn) { this.fechaCheckIn = fechaCheckIn; }
    public Date getFechaCheckOut() { return fechaCheckOut; }
    public void setFechaCheckOut(Date fechaCheckOut) { this.fechaCheckOut = fechaCheckOut; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    @Override
    public String toString() {
        return "Reserva #" + idReserva + " (Cliente: " + idCliente + ", Hab: " + idHabitacion + ", " + fechaCheckIn + " a " + fechaCheckOut + ")";
    }
}