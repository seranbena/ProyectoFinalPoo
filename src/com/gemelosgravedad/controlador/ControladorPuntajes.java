package com.gemelosgravedad.controlador;

import com.gemelosgravedad.modelo.datos.Puntaje;
import com.gemelosgravedad.modelo.datos.RegistroPartida;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Lee y guarda el historial de partidas en {@code data/historial.txt}.
 *
 * <p>El archivo se escribe en UTF-8 y cada resultado ocupa una linea. Los
 * errores de disco no interrumpen el juego: se registran y la partida continua
 * sin historial.</p>
 *
 * @see Puntaje
 * @see RegistroPartida
 */
public final class ControladorPuntajes {

    private static final Logger REGISTRO = Logger.getLogger(ControladorPuntajes.class.getName());
    private static final Path ARCHIVO = Path.of("data", "historial.txt");
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.ROOT);

    /**
     * Devuelve la fecha y hora actual con el formato usado en el historial.
     *
     * @return fecha actual en formato {@code dd/MM/yyyy HH:mm}
     */
    public String obtenerFechaActual() {
        return LocalDateTime.now().format(FORMATO_FECHA);
    }

    /**
     * Agrega un resultado al final del historial.
     *
     * @param puntaje resultado que se quiere guardar
     * @return {@code true} si el archivo se actualizo correctamente
     */
    public boolean guardar(Puntaje puntaje) {
        if (puntaje == null) {
            return false;
        }
        try {
            Path carpeta = ARCHIVO.getParent();
            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }
            Files.writeString(ARCHIVO, puntaje.aLinea() + System.lineSeparator(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            REGISTRO.log(Level.WARNING, "No se pudo guardar el historial", e);
            return false;
        }
    }

    /**
     * Carga todas las partidas validas del historial.
     *
     * @return registro con las partidas encontradas; vacio si el archivo no
     *         existe o no se puede leer
     */
    public RegistroPartida cargar() {
        RegistroPartida registro = new RegistroPartida();
        if (!Files.exists(ARCHIVO)) {
            return registro;
        }
        try (Stream<String> lineas = Files.lines(ARCHIVO, StandardCharsets.UTF_8)) {
            lineas.map(Puntaje::desdeLinea)
                    .flatMap(Optional::stream)
                    .forEach(registro::agregar);
        } catch (IOException e) {
            REGISTRO.log(Level.WARNING, "No se pudo leer el historial", e);
        }
        return registro;
    }
}
