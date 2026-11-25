package com.gemelosgravedad.modelo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Historial de partidas y calculo del Top 3.
 *
 * <p>El orden es descendente por puntos y, en caso de empate, por tiempo
 * sobrevivido. El comparador se define una sola vez para que cualquier consulta
 * del historial use el mismo criterio.</p>
 *
 * @see Puntaje
 * @see com.gemelosgravedad.controlador.ControladorPuntajes
 */
public final class RegistroPartida {

    /** Numero de resultados que se muestran en la tabla de records. */
    public static final int TAMANO_TOP = 3;

    private static final Comparator<Puntaje> ORDEN_TOP = Comparator
            .comparingInt(Puntaje::getPuntos).reversed()
            .thenComparing(Comparator.comparingInt(Puntaje::getSegundos).reversed());

    private final List<Puntaje> partidas = new ArrayList<>();

    /**
     * Crea un historial vacio.
     */
    public RegistroPartida() {
    }

    /**
     * Agrega una partida al historial si no es nula.
     *
     * @param puntaje resultado que se quiere registrar
     */
    public void agregar(Puntaje puntaje) {
        if (puntaje != null) {
            partidas.add(puntaje);
        }
    }

    /**
     * Devuelve una copia de todas las partidas registradas.
     *
     * @return lista independiente del historial interno
     */
    public List<Puntaje> obtenerTodas() {
        return new ArrayList<>(partidas);
    }

    /**
     * Devuelve los mejores resultados ordenados por puntos y tiempo.
     *
     * @return hasta {@value #TAMANO_TOP} resultados, del mejor al peor
     */
    public List<Puntaje> obtenerTop3() {
        List<Puntaje> copia = obtenerTodas();
        copia.sort(ORDEN_TOP);
        return new ArrayList<>(copia.subList(0, Math.min(TAMANO_TOP, copia.size())));
    }
}
