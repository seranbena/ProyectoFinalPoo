package com.gemelosgravedad.modelo;

import com.gemelosgravedad.util.FormatoTiempo;

/**
 * Resultado de una partida guardado en el historial.
 *
 * <p>El nombre y la fecha se limpian para que nunca rompan el formato de linea
 * usado por el archivo {@code data/historial.txt}: los campos se separan con
 * punto y coma.</p>
 *
 * @see RegistroPartida
 * @see com.gemelosgravedad.controlador.ControladorPuntajes
 */
public final class Puntaje {

    /** Longitud maxima permitida para el nombre del jugador. */
    public static final int NOMBRE_MAXIMO = 12;

    private static final String NOMBRE_POR_DEFECTO = "SIN NOMBRE";
    private static final String FECHA_POR_DEFECTO = "sin fecha";
    private static final String SEPARADOR_CAMPOS = ";";
    private static final String SUSTITUTO_SEPARADOR = " ";
    private static final String SALTO_LINEA = "\n";
    private static final int CAMPOS_MINIMOS = 4;

    private final String nombre;
    private final int puntos;
    private final int segundos;
    private final String fecha;

    /**
     * Crea un resultado de partida.
     *
     * @param nombre nombre del jugador; se recorta a {@value #NOMBRE_MAXIMO}
     *               caracteres
     * @param puntos puntaje final
     * @param segundos tiempo sobrevivido en segundos
     * @param fecha fecha de la partida; si esta vacia se usa un texto por
     *              defecto
     */
    public Puntaje(String nombre, int puntos, int segundos, String fecha) {
        this.nombre = limpiarNombre(nombre);
        this.puntos = puntos;
        this.segundos = segundos;
        this.fecha = limpiarFecha(fecha);
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
                .replace(SALTO_LINEA, SUSTITUTO_SEPARADOR)
                .trim();
        if (limpio.length() > NOMBRE_MAXIMO) {
            limpio = limpio.substring(0, NOMBRE_MAXIMO);
        }
        return limpio;
    }

    /**
     * Normaliza la fecha para que no contenga el separador de campos.
     *
     * @param texto fecha original
     * @return fecha limpia o el texto por defecto
     */
    private static String limpiarFecha(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
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
        return nombre + SEPARADOR_CAMPOS + puntos + SEPARADOR_CAMPOS
                + segundos + SEPARADOR_CAMPOS + fecha;
    }

    /**
     * Reconstruye un resultado a partir de una linea del historial.
     *
     * @param linea linea leida del archivo
     * @return resultado reconstruido o {@code null} si la linea no es valida
     */
    public static Puntaje desdeLinea(String linea) {
        if (linea == null) {
            return null;
        }
        String[] partes = linea.split(SEPARADOR_CAMPOS, -1);
        if (partes.length < CAMPOS_MINIMOS) {
            return null;
        }
        try {
            int puntos = Integer.parseInt(partes[1].trim());
            int segundos = Integer.parseInt(partes[2].trim());
            return new Puntaje(partes[0], puntos, segundos, partes[3]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Devuelve el nombre del jugador.
     *
     * @return nombre ya normalizado
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve el puntaje final.
     *
     * @return puntos obtenidos
     */
    public int getPuntos() {
        return puntos;
    }

    /**
     * Devuelve el tiempo sobrevivido.
     *
     * @return segundos de partida
     */
    public int getSegundos() {
        return segundos;
    }

    /**
     * Devuelve la fecha de la partida.
     *
     * @return fecha normalizada
     */
    public String getFecha() {
        return fecha;
    }
}
