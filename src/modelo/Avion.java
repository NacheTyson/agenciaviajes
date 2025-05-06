package modelo;

// Serializable ya se hereda de Transporte
public class Avion extends Transporte {
    private boolean esDirecto;

    public Avion(int duracionMinutos, double precio, boolean esDirecto) {
        super(duracionMinutos, precio);
        this.esDirecto = esDirecto;
    }

    //Getter
    public boolean isEsDirecto() {
        return esDirecto;
    }

    // Sobrescribimos toString para incluir si es directo

    @Override
    public String toString() {
        return "Avion{" +
                "esDirecto=" + esDirecto +
                '}';
    }
}
