package pms.model;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private int idCliente;
    private String nombre;
    private String documento;
    private String correo;
    private String telefono;
    private List<Reserva> historialReservas;

    // Constructor vacío
    public Cliente() {
        this.historialReservas = new ArrayList<>();
    }

    // Constructor con campos
    public Cliente(int idCliente, String nombre, String documento, String correo, String telefono) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.documento = documento;
        this.correo = correo;
        this.telefono = telefono;
        this.historialReservas = new ArrayList<>();
    }

    // Getters y Setters (mantén los existentes)
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public List<Reserva> getHistorialReservas() { return historialReservas; }
    public void setHistorialReservas(List<Reserva> historialReservas) { this.historialReservas = historialReservas; }

    public void agregarReserva(Reserva reserva) {
        historialReservas.add(reserva);
    }

    // Sobrescribir toString() para mostrar nombre legible en JComboBox
    @Override
    public String toString() {
        return nombre + " (Doc: " + documento + ")";
    }
}