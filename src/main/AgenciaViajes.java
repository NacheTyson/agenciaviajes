package main;

import modelo.*;
import servicios.GestorPaquetes;

import java.io.FileWriter; // Para escribir en archivo de texto
import java.io.IOException; // Para manejar excepciones de archivo
import java.io.PrintWriter; // Para escribir en archivo de texto de forma más cómoda
import java.util.ArrayList;
import java.util.InputMismatchException; // Para captura de errores de entrada
import java.util.List;
import java.util.Scanner;

public class AgenciaViajes {
    private static final GestorPaquetes gestor = new GestorPaquetes();
    private static final Scanner sc = new Scanner(System.in);
    private static final String ARCHIVO_TEXTO = "paquetes.txt"; // Nombre del archivo de texto

    public static void main(String[] args) {
        int opcion = 0; // Inicializar opción

        do {
            mostrarMenu();
            try {
                opcion = sc.nextInt();
                sc.nextLine(); // Consumir el salto de línea

                switch (opcion) {
                    case 1:
                        crearPaquete();
                        break;
                    case 2:
                        mostrarPaquetes();
                        break;
                    case 3:
                        filtrarPaquetes();
                        break;
                    case 4:
                        eliminarPaquete();
                        break;
                    case 5:
                        System.out.println("Guardando datos y saliendo...");
                        gestor.guardarPaquetes(); // Guardar antes de salir
                        System.out.println("¡Hasta pronto!");
                        break;
                    default:
                        System.out.println("Opción no válida. Introduce un número entre 1 y 5.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.err.println("Error: Debes introducir un número entero.");
                sc.nextLine(); // Limpiar el buffer del scanner
                opcion = 0; // Resetear opción para que el bucle continúe
            } catch (Exception e) { // Captura general para otros posibles errores
                System.err.println("Ha ocurrido un error inesperado: " + e.getMessage());
                e.printStackTrace(); // Es útil para depurar
                opcion = 0; // Resetear
            }

        } while (opcion != 5);

        sc.close(); // Cerrar scanner al final
    }

    private static void mostrarMenu() {
        System.out.println("\n--- MENÚ AGENCIA DE VIAJES ---");
        System.out.println("1. Crear paquete vacacional");
        System.out.println("2. Mostrar todos los paquetes vacacionales (y guardar en " + ARCHIVO_TEXTO + ")");
        System.out.println("3. Mostrar paquetes filtrados (por duración transporte y sin escalas)");
        System.out.println("4. Eliminar paquete vacacional");
        System.out.println("5. Salir");
        System.out.print("Elige una opción: ");
    }

    private static void crearPaquete() {
        try {
            System.out.println("\n--- Creando Nuevo Paquete ---");
            System.out.print("Ciudad de origen: ");
            String origen = sc.nextLine();
            System.out.print("Ciudad de destino: ");
            String destino = sc.nextLine();

            // --- TRANSPORTE ---
            Transporte transporte = crearTransporte();
            if (transporte == null) return; // Si hubo error creando transporte

            // --- ALOJAMIENTO ---
            Alojamiento alojamiento = crearAlojamiento();
            if (alojamiento == null) return; // Si hubo error

            // --- EXTRAS ---
            List<Extra> extras = crearExtras();

            // --- FECHA Y DURACIÓN ---
            System.out.print("Fecha de salida (YYYY-MM-DD): ");
            String fecha = sc.nextLine();
            int dias = leerEnteroPositivo("Duración del viaje (días): ");

            // Generar ID y crear paquete
            int nuevoId = gestor.generarId();
            PaqueteVacacional nuevoPaquete = new PaqueteVacacional(nuevoId, origen, destino, transporte,
                    alojamiento, extras, fecha, dias);
            gestor.agregarPaquete(nuevoPaquete);
            System.out.println("¡Paquete creado con éxito!");

        } catch (IllegalArgumentException e) {
            System.err.println("Error al crear el paquete: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado durante la creación del paquete: " + e.getMessage());
        }
    }

    private static Transporte crearTransporte() {
        Transporte transporte = null;
        while (transporte == null) {
            System.out.println("Tipo de transporte (1: Avión, 2: Tren): ");
            int tipo = leerEnteroEnRango("Elige 1 o 2:", 1, 2);

            int duracion = leerEnteroPositivo("Duración del trayecto (minutos): ");
            double precio = leerDoublePositivo("Precio del trayecto (por viaje, €): ");

            if (tipo == 1) { // Avión
                boolean directo = leerSiNo("¿El vuelo es directo? (s/n): ");
                transporte = new Avion(duracion, precio, directo);
            } else { // Tren
                transporte = new Tren(duracion, precio);
            }
        }
        return transporte;
    }

    private static Alojamiento crearAlojamiento() {
        System.out.println("\n--- Datos del Alojamiento ---");
        System.out.print("Nombre del alojamiento: ");
        String nombreAloj = sc.nextLine();
        double distancia = leerDoublePositivo("Distancia al centro (metros): ");
        double precioDia = leerDoublePositivo("Precio por día (€): ");
        return new Alojamiento(nombreAloj, distancia, precioDia);
    }

    private static List<Extra> crearExtras() {
        List<Extra> extras = new ArrayList<>();
        System.out.println("\n--- Añadir Extras (opcional) ---");
        while (leerSiNo("¿Deseas añadir un extra? (s/n): ")) {
            System.out.print("Descripción del extra: ");
            String desc = sc.nextLine();
            double precioExtra = leerDoublePositivo("Precio del extra (€): ");
            extras.add(new Extra(desc, precioExtra));
        }
        return extras;
    }

    private static void mostrarPaquetes() {
        System.out.println("\n--- Listado de Paquetes Vacacionales ---");
        List<PaqueteVacacional> lista = gestor.getPaquetes();

        if (lista.isEmpty()) {
            System.out.println("No hay paquetes vacacionales creados.");
            return;
        }

        // StringBuilder para construir el texto para consola y archivo
        StringBuilder output = new StringBuilder();
        for (PaqueteVacacional p : lista) {
            output.append(p.toString()).append("\n"); // Usa el toString que definimos
        }

        // Mostrar en consola
        System.out.println(output.toString());

        // Guardar en archivo de texto
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_TEXTO))) {
            writer.println("--- Listado de Paquetes Vacacionales ---");
            writer.println("Generado el: " + java.time.LocalDateTime.now()); // Añadir fecha de generación
            writer.println("----------------------------------------");
            writer.print(output.toString()); // Escribir el mismo contenido
            System.out.println("Listado también guardado en " + ARCHIVO_TEXTO);
        } catch (IOException e) {
            System.err.println("Error al guardar el listado en " + ARCHIVO_TEXTO + ": " + e.getMessage());
        }
    }

    private static void filtrarPaquetes() {
        System.out.println("\n--- Filtrar Paquetes ---");
        int duracionMax = leerEnteroPositivo("Duración máxima del trayecto en transporte (minutos): ");
        //boolean soloDirectos = leerSiNo("¿Mostrar solo paquetes con vuelos directos (s/n)?: ");
        // Modificación según enunciado: "que además no incluyan vuelos con escalas"
        // Esto significa que SIEMPRE filtraremos los vuelos con escalas.
        boolean filtrarVuelosConEscalas = true;

        List<PaqueteVacacional> filtrados = gestor.filtrarPaquetes(duracionMax, filtrarVuelosConEscalas);

        if (filtrados.isEmpty()) {
            System.out.println("No se encontraron paquetes que cumplan los criterios.");
        } else {
            System.out.println("\n--- Paquetes Filtrados ---");
            for (PaqueteVacacional p : filtrados) {
                System.out.println(p.toString());
            }
        }
    }

    private static void eliminarPaquete() {
        System.out.println("\n--- Eliminar Paquete Vacacional ---");
        if (gestor.getPaquetes().isEmpty()) {
            System.out.println("No hay paquetes para eliminar.");
            return;
        }
        int idEliminar = leerEnteroPositivo("Introduce el ID del paquete a eliminar: ");
        gestor.eliminarPaquete(idEliminar); // El método ya imprime si lo encontró o no
    }

    // --- Métodos de ayuda para leer entrada de forma segura ---

    private static int leerEnteroPositivo(String mensaje) {
        int numero = -1;
        while (numero <= 0) {
            System.out.print(mensaje + " ");
            try {
                numero = sc.nextInt();
                sc.nextLine(); // Consumir nueva línea
                if (numero <= 0) {
                    System.err.println("Error: El número debe ser positivo.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Error: Debes introducir un número entero válido.");
                sc.nextLine(); // Limpiar buffer
                numero = -1; // Resetear para volver a pedir
            }
        }
        return numero;
    }

    private static int leerEnteroEnRango(String mensaje, int min, int max) {
        int numero = min - 1; // Valor inicial fuera de rango
        while (numero < min || numero > max) {
            System.out.print(mensaje + " ");
            try {
                numero = sc.nextInt();
                sc.nextLine(); // Consumir nueva línea
                if (numero < min || numero > max) {
                    System.err.println("Error: El número debe estar entre " + min + " y " + max + ".");
                }
            } catch (InputMismatchException e) {
                System.err.println("Error: Debes introducir un número entero válido.");
                sc.nextLine(); // Limpiar buffer
                numero = min - 1; // Resetear para volver a pedir
            }
        }
        return numero;
    }

    private static double leerDoublePositivo(String mensaje) {
        double numero = -1.0;
        while (numero < 0) {
            System.out.print(mensaje + " ");
            try {
                numero = sc.nextDouble();
                sc.nextLine(); // Consumir nueva línea
                if (numero < 0) {
                    System.err.println("Error: El número no puede ser negativo.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Error: Debes introducir un número decimal válido (usa ',' o '.' según tu configuración regional).");
                sc.nextLine(); // Limpiar buffer
                numero = -1.0; // Resetear
            }
        }
        return numero;
    }

    private static boolean leerSiNo(String mensaje) {
        String respuesta = "";
        while (!respuesta.equalsIgnoreCase("s") && !respuesta.equalsIgnoreCase("n")) {
            System.out.print(mensaje + " ");
            respuesta = sc.nextLine();
            if (!respuesta.equalsIgnoreCase("s") && !respuesta.equalsIgnoreCase("n")) {
                System.err.println("Responde 's' para sí o 'n' para no.");
            }
        }
        return respuesta.equalsIgnoreCase("s");
    }
}