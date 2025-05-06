package modelo;

import java.io.Serializable; // Importar

// Implementar Serializable
public class Alojamiento implements Serializable {
    private String nombre;
    private double distanciaCentroMetros;
    private double precioPorDia;

    public Alojamiento(String nombre, double distanciaCentroMetros, double precioPorDia) {
        if (nombre == null || nombre.trim().isEmpty() || distanciaCentroMetros < 0 || precioPorDia < 0) {
            throw new IllegalArgumentException("Datos de alojamiento inválidos.");
        }
        this.nombre = nombre;
        this.distanciaCentroMetros = distanciaCentroMetros;
        this.precioPorDia = precioPorDia;
    }

    // Getters
    public String getNombre() { return nombre; }
    public double getDistanciaCentroMetros() { return distanciaCentroMetros; }
    public double getPrecioPorDia() { return precioPorDia; }

    @Override
    public String toString() {
        return "Alojamiento [Nombre=" + nombre + ", Distancia Centro=" + distanciaCentroMetros + "m, Precio/día=" + precioPorDia + "€]";
    }
}