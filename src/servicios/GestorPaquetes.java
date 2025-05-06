package servicios;

import modelo.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional; // Para buscar el máximo ID

public class GestorPaquetes {
    private List<PaqueteVacacional> paquetes;
    private int nextId; // ID para el próximo paquete
    private static final String ARCHIVO_DATOS = "paquetes.dat"; // Fichero binario para persistencia

    public GestorPaquetes() {
        paquetes = new ArrayList<>();
        cargarPaquetes(); // Carga los paquetes y establece nextId
    }

    // Método para generar un ID único
    public int generarId() {
        return nextId++; // Devuelve el ID actual y luego lo incrementa
    }

    public void agregarPaquete(PaqueteVacacional paquete) {
        if (paquete != null) {
            paquetes.add(paquete);
            System.out.println("Paquete añadido con ID: " + paquete.getId());
        } else {
            System.err.println("Error: No se puede añadir un paquete nulo.");
        }
    }

    // Modificado para devolver boolean indicando si se eliminó
    public boolean eliminarPaquete(int id) {
        boolean eliminado = paquetes.removeIf(p -> p.getId() == id);
        if (!eliminado) {
            System.out.println("No se encontró ningún paquete con el ID: " + id);
        } else {
            System.out.println("Paquete con ID " + id + " eliminado.");
        }
        return eliminado;
    }

    public List<PaqueteVacacional> filtrarPaquetes(int duracionMaxTrayecto, boolean soloVuelosDirectos) {
        List<PaqueteVacacional> filtrados = new ArrayList<>();
        for (PaqueteVacacional p : paquetes) {
            Transporte t = p.getTransporte();
            // Comprueba duración
            if (t.getDuracionMinutos() <= duracionMaxTrayecto) {
                // Si se requiere solo directos, comprueba si es avión y si es directo
                if (soloVuelosDirectos) {
                    if (t instanceof Avion) {
                        if (((Avion) t).isEsDirecto()) {
                            filtrados.add(p);
                        }
                    } else {
                        // Si no es avión (es Tren), y solo queremos directos, no lo añadimos?
                        // El enunciado dice "no incluyan vuelos con escalas". Trenes no tienen escalas.
                        // Así que si es Tren, y cumple duración, lo añadimos.
                        filtrados.add(p); // Añadir Tren si cumple duración
                    }
                } else {
                    // Si no se requiere solo directos, añadir si cumple duración
                    filtrados.add(p);
                }
            }
        }
        return filtrados;
    }


    // Método para obtener todos los paquetes (para mostrar)
    public List<PaqueteVacacional> getPaquetes() {
        // Devuelve una copia para evitar modificaciones externas accidentales
        return new ArrayList<>(paquetes);
    }


    @SuppressWarnings("unchecked") // Para suprimir el warning del cast de readObject
    private void cargarPaquetes() {
        File archivo = new File(ARCHIVO_DATOS);
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                paquetes = (List<PaqueteVacacional>) ois.readObject();
                // Calcular el siguiente ID basado en el máximo ID existente
                nextId = paquetes.stream()
                        .mapToInt(PaqueteVacacional::getId)
                        .max()
                        .orElse(0) + 1; // Si no hay paquetes, empieza en 1
                System.out.println("Paquetes cargados desde " + ARCHIVO_DATOS);

            } catch (FileNotFoundException e) {
                // No es un error si el archivo no existe la primera vez
                System.out.println("Archivo " + ARCHIVO_DATOS + " no encontrado. Se creará uno nuevo al guardar.");
                nextId = 1; // Empezar IDs desde 1 si no hay archivo
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al cargar paquetes desde " + ARCHIVO_DATOS + ": " + e.getMessage());
                // Si hay error cargando, mejor empezar con lista vacía y ID 1 para evitar corrupción
                paquetes = new ArrayList<>();
                nextId = 1;
            }
        } else {
            System.out.println("Archivo " + ARCHIVO_DATOS + " no encontrado. Se creará uno nuevo al guardar.");
            nextId = 1; // Empezar IDs desde 1 si no hay archivo
        }
        // Asegurarse de que nextId nunca sea menor que 1
        if (nextId < 1) {
            nextId = 1;
        }
    }

    public void guardarPaquetes() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DATOS))) {
            oos.writeObject(paquetes);
            System.out.println("Paquetes guardados en " + ARCHIVO_DATOS);
        } catch (IOException e) {
            System.err.println("Error al guardar paquetes en " + ARCHIVO_DATOS + ": " + e.getMessage());
            e.printStackTrace(); // Imprimir stack trace para más detalles
        }
    }
}