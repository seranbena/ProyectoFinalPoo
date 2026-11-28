package com.gemelosgravedad.modelo.datos;

import com.gemelosgravedad.util.FormatoTiempo;
import java.util.Optional;

/**
 * Resultado de una partida guardado en el historial.
 *
 * <p>El nombre y la fecha se normalizan en el constructor compacto del record
 * para que nunca rompan el formato de linea usado por el archivo
 * {@code data/historial.txt}: los campos se separan con punto y coma.</p>
 *
 * @param nombre   nombre del jugador o del equipo, ya normalizado
 * @param puntos   puntaje final
 * @param segundos tiempo sobrevivido en segundos
 * @param fecha    fecha de la partida, ya normalizada
 * @see RegistroPartida
 * @see com.gemelosgravedad.controlador.ControladorPuntajes
 */
public record Puntaje(String nombre, int puntos, int segundos, String fecha) {

    /** Longitud maxima permitida para el nombre o el equipo. */
    public static final int NOMBRE_MAXIMO = 20;

    /** Longitud maxima de cada nombre en el modo de dos jugadores. */
    public static final int NOMBRE_MAXIMO_2P = 12;

    private static final String NOMBRE_POR_DEFECTO = "SIN NOMBRE";
    private static final String FECHA_POR_DEFECTO = "sin fecha";
    private static final String SEPARADOR_CAMPOS = ";";
    private static final String SUSTITUTO_SEPARADOR = " ";
    private static final int CAMPOS_MINIMOS = 4;

    /**
     * Normaliza los campos de texto antes de almacenarlos.
     */
    public Puntaje {
        nombre = limpiarNombre(nombre);
        fecha = limpiarFecha(fecha);
    }

    /**
     * Normaliza el nombre para que pueda guardarse en una sola linea.
     *
     * @param texto nombre original
     * @return nombre sin separadores ni saltos de linea
     */
    private static String limpiarNombre(String texto) {
        if (texto == null) {
            return NOMBRE_POR_DEFECTO;
        }
        String limpio = texto.replace(SEPARADOR_CAMPOS, SUSTITUTO_SEPARADOR)
                .replace("\n", SUSTITUTO_SEPARADOR)
                .trim();
        return limpio.length() > NOMBRE_MAXIMO
                ? limpio.substring(0, NOMBRE_MAXIMO)
                : limpio;
    }

    /**
     * Normaliza la fecha para que no contenga el separador de campos.
     *
     * @param texto fecha original
     * @return fecha limpia o el texto por defecto
     */
    private static String limpiarFecha(String texto) {
        if (texto == null || texto.isBlank()) {
            return FECHA_POR_DEFECTO;
        }
        return texto.replace(SEPARADOR_CAMPOS, SUSTITUTO_SEPARADOR);
    }

    /**
     * Devuelve el tiempo sobrevivido en formato {@code mm:ss}.
     *
     * @return tiempo formateado
     */
    public String getTiempoTexto() {
        return FormatoTiempo.formatear(segundos);
    }

    /**
     * Serializa el resultado con el formato del historial.
     *
     * @return linea con los campos {@code nombre;puntos;segundos;fecha}
     */
    public String aLinea() {
        return String.join(SEPARADOR_CAMPOS, nombre, String.valueOf(puntos),
                String.valueOf(segundos), fecha);
    }

    /**
     * Reconstruye un resultado a partir de una linea del historial.
     *
     * @param linea linea leida del archivo
     * @return resultado reconstruido, o {@link Optional#empty()} si la linea no
     *         es valida
     */
    public static Optional<Puntaje> desdeLinea(String linea) {
        if (linea == null) {
            return Optional.empty();
        }
        String[] partes = linea.split(SEPARADOR_CAMPOS, -1);
        if (partes.length < CAMPOS_MINIMOS) {
            return Optional.empty();
        }
        try {
            int puntos = Integer.parseInt(partes[1].trim());
            int segundos = Integer.parseInt(partes[2].trim());
            return Optional.of(new Puntaje(partes[0], puntos, segundos, partes[3]));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
