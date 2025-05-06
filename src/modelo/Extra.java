package modelo;

import java.io.Serializable; // Importar

// Implementar Serializable
public class Extra implements Serializable {
    private String descripcion;
    private double precio;

    public Extra(String descripcion, double precio) {
        if (descripcion == null || descripcion.trim().isEmpty() || precio < 0) {
            throw new IllegalArgumentException("Datos de extra inválidos.");
        }
        this.descripcion = descripcion;
        this.precio = precio;
    }

    // Getters
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }

    @Override
    public String toString() {
        return "Extra [Descripción=" + descripcion + ", Precio=" + precio + "€]";
    }
}