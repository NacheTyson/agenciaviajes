package modelo;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class PaqueteVacacional implements Serializable {
    // Quitar el static nextId de aquí. Se gestionará en GestorPaquetes
    private int id; // El ID se asignará desde fuera
    private String ciudadOrigen;
    private String ciudadDestino;
    private Transporte transporte;
    private Alojamiento alojamiento;
    private List<Extra> extras;
    private String fechaSalida;
    private int duracionDias;

    // Constructor modificado: recibe el ID
    public PaqueteVacacional(int id, String ciudadOrigen, String ciudadDestino, Transporte transporte,
                             Alojamiento alojamiento, List<Extra> extras, String fechaSalida,
                             int duracionDias) {
        // Validación básica
        if (id <= 0 || ciudadOrigen == null || ciudadOrigen.trim().isEmpty() ||
                ciudadDestino == null || ciudadDestino.trim().isEmpty() ||
                transporte == null || alojamiento == null || extras == null ||
                fechaSalida == null || fechaSalida.trim().isEmpty() || duracionDias <= 0) {
            throw new IllegalArgumentException("Datos del paquete vacacional inválidos.");
        }

        this.id = id;
        this.ciudadOrigen = ciudadOrigen;
        this.ciudadDestino = ciudadDestino;
        this.transporte = transporte;
        this.alojamiento = alojamiento;
        this.extras = extras; // Asegúrate de que la lista que recibes es mutable si necesitas modificarla después
        this.fechaSalida = fechaSalida;
        this.duracionDias = duracionDias;
    }

    public double precioTotal() {
        double precioExtras = extras.stream()
                .mapToDouble(Extra::getPrecio)
                .sum();
        // Precio transporte ida y vuelta (se asume el mismo precio)
        double precioTransporteTotal = transporte.getPrecio() * 2;
        double precioAlojamientoTotal = alojamiento.getPrecioPorDia() * duracionDias;

        return precioTransporteTotal + precioAlojamientoTotal + precioExtras;
    }

    // --- Getters ---
    public int getId() { return id; }
    public String getCiudadOrigen() { return ciudadOrigen; }
    public String getCiudadDestino() { return ciudadDestino; }
    public Transporte getTransporte() { return transporte; }
    public Alojamiento getAlojamiento() { return alojamiento; }
    public List<Extra> getExtras() { return extras; }
    public String getFechaSalida() { return fechaSalida; }
    public int getDuracionDias() { return duracionDias; }

    // --- toString para mostrar info fácilmente ---
    @Override
    public String toString() {
        String extrasStr = extras.isEmpty() ? "Ninguno" :
                extras.stream()
                        .map(e -> e.getDescripcion() + " (" + e.getPrecio() + "€)")
                        .collect(Collectors.joining(", "));

        return "--- Paquete Vacacional ID: " + id + " ---\n" +
                "  Origen: " + ciudadOrigen + "\n" +
                "  Destino: " + ciudadDestino + "\n" +
                "  Fecha Salida: " + fechaSalida + "\n" +
                "  Duración: " + duracionDias + " días\n" +
                "  Transporte: " + transporte.toString() + "\n" +
                "  Alojamiento: " + alojamiento.toString() + "\n" +
                "  Extras: " + extrasStr + "\n" +
                "  Precio Total: " + String.format("%.2f", precioTotal()) + "€\n" +
                "-------------------------------------";
    }
}