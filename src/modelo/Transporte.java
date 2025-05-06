package modelo;

import java.io.Serializable; // Importar

// Implementar Serializable
public abstract class Transporte implements Serializable {
    private int duracionMinutos;
    private double precio;

    public Transporte(int duracionMinutos, double precio) {
        // Añadir validación básica si se desea
        if (duracionMinutos <= 0 || precio < 0) {
            throw new IllegalArgumentException("Duración y precio deben ser positivos.");
        }
        this.duracionMinutos = duracionMinutos;
        this.precio = precio;
    }

    // Getters
    public int getDuracionMinutos() { return duracionMinutos; }
    public double getPrecio() { return precio; }

    // Añadir un toString abstracto o uno genérico
    @Override
    public String toString() {
        return "Transporte [Duración=" + duracionMinutos + " min, Precio=" + precio + "€]";
    }
}