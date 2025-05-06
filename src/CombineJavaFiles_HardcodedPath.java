import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets; // Para asegurar codificación UTF-8
import java.nio.file.*; // API moderna para archivos y directorios
import java.util.stream.Stream; // Para procesar archivos encontrados
// Ya no necesitamos Scanner

public class CombineJavaFiles_HardcodedPath {

    public static void main(String[] args) {

        // --- Ruta de entrada FIJA ---
        // Asegúrate de escapar las barras invertidas (\) usando doble barra (\\)
        String inputDirPathStr = "C:\\Users\\jhonnconnor367\\PycharmProjects\\examenesieslapaz\\Scraping_Moodle_Completo\\curso_795_Nombre del curso Programación Programación\\archivos_descargados\\Examen Agencia de viajes";
        // -----------------------------

        Path inputDir = Paths.get(inputDirPathStr);
        // Crear el archivo de salida en el mismo directorio para facilidad
        Path outputFile = inputDir.resolve("codigo_combinado.txt"); // El archivo se creará DENTRO de "Examen Agencia de viajes"

        // 1. Validar que la ruta de entrada existe y es un directorio
        if (!Files.exists(inputDir) || !Files.isDirectory(inputDir)) {
            System.err.println("Error: La ruta fija proporcionada no existe o no es un directorio válido.");
            System.err.println("Ruta intentada: " + inputDir.toAbsolutePath());
            return; // Salir si la ruta no es válida
        }

        System.out.println("Directorio de entrada (fijo): " + inputDir.toAbsolutePath());
        System.out.println("Archivo de salida será: " + outputFile.toAbsolutePath());

        // 2. Usar try-with-resources para asegurar que el BufferedWriter se cierre
        try (BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8)) {

            System.out.println("Buscando y combinando archivos .java...");

            // 3. Recorrer el árbol de directorios usando Files.walk
            try (Stream<Path> stream = Files.walk(inputDir)) {
                stream
                        .filter(Files::isRegularFile) // Solo archivos
                        .filter(path -> path.toString().toLowerCase().endsWith(".java")) // Solo .java
                        .sorted() // Ordenar alfabéticamente (opcional)
                        .forEach(javaFile -> { // Procesar cada archivo
                            try {
                                Path relativePath = inputDir.relativize(javaFile);
                                String separator = "\n\n// ======================================================\n"
                                        + "// === Archivo: " + relativePath.toString().replace("\\", "/") + "\n"
                                        + "// ======================================================\n\n";

                                System.out.println("Añadiendo: " + relativePath);
                                writer.write(separator); // Escribir separador
                                String content = Files.readString(javaFile, StandardCharsets.UTF_8); // Leer contenido
                                writer.write(content); // Escribir contenido

                            } catch (IOException e) {
                                System.err.println("Error al leer o escribir el archivo " + javaFile + ": " + e.getMessage());
                            }
                        });
            } // Stream se cierra aquí

            System.out.println("\n¡Proceso completado! Los archivos .java han sido combinados en:");
            System.out.println(outputFile.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de salida " + outputFile + ": " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ocurrió un error inesperado durante el proceso: " + e.getMessage());
            e.printStackTrace();
        }
    }
}