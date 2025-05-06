package modelo;

import java.io.Serializable;

// Asegúrate de que implementa Serializable para poder guardarla en el archivo
public class Tren extends Transporte implements Serializable {
    // Tren no tiene atributos adicionales según el enunciado

    public Tren(int duracionMinutos, double precio) {
        super(duracionMinutos, precio);
    }

    // Sobrescribimos toString para una mejor representación
    @Override
    public String toString() {
        return "Tren [Duración=" + getDuracionMinutos() + " min, Precio=" + getPrecio() + "€]";
    }
}