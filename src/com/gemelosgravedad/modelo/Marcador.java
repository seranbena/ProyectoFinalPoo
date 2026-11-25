package com.gemelosgravedad.modelo;

import com.gemelosgravedad.util.FormatoTiempo;

/**
 * Lleva las vidas, el puntaje, el tiempo y la invulnerabilidad de una partida.
 *
 * <p>Los metodos estan sincronizados porque el hilo del juego y el temporizador
 * de Swing consultan y modifican estos datos a la vez.</p>
 *
 * @see EstadoJuego
 */
public class Marcador {

    /** Vidas con las que comienza una partida. */
    public static final int VIDAS_INICIALES = 3;

    /** Puntos que se suman por cada segundo sobrevivido. */
    public static final int PUNTOS_POR_SEGUNDO = 2;

    /** Segundos de invulnerabilidad que se otorgan al recibir un golpe. */
    public static final int SEGUNDOS_INVULNERABILIDAD = 2;

    /** Segundos que deben transcurrir para subir de nivel. */
    public static final int SEGUNDOS_POR_NIVEL = 10;

    private int vidas;
    private int puntaje;
    private int segundos;
    private int segundosInvulnerable;
    private boolean juegoTerminado;

    /**
     * Crea un marcador en el estado inicial de una partida.
     */
    public Marcador() {
        vidas = VIDAS_INICIALES;
        puntaje = 0;
        segundos = 0;
        segundosInvulnerable = 0;
        juegoTerminado = false;
    }

    /**
     * Suma puntos si la partida sigue activa.
     *
     * @param cantidad puntos que se quieren sumar
     */
    public synchronized void sumarPuntos(int cantidad) {
        if (!juegoTerminado) {
            puntaje += cantidad;
        }
    }

    /**
     * Avanza el cronometro y suma los puntos correspondientes al segundo.
     */
    public synchronized void sumarSegundo() {
        if (!juegoTerminado) {
            segundos++;
            puntaje += PUNTOS_POR_SEGUNDO;
        }
    }

    /**
     * Concede invulnerabilidad durante al menos los segundos indicados.
     *
     * @param segundos duracion solicitada en segundos
     */
    public synchronized void darInvulnerabilidad(int segundos) {
        if (segundos > segundosInvulnerable) {
            segundosInvulnerable = segundos;
        }
    }

    /**
     * Resta una vida y activa la invulnerabilidad temporal. Si no quedan
     * vidas, marca la partida como terminada.
     */
    public synchronized void perderVida() {
        if (juegoTerminado) {
            return;
        }
        vidas--;
        segundosInvulnerable = SEGUNDOS_INVULNERABILIDAD;
        if (vidas <= 0) {
            vidas = 0;
            juegoTerminado = true;
        }
    }

    /**
     * Descuenta un segundo de invulnerabilidad si queda alguno.
     */
    public synchronized void actualizarSegundo() {
        if (segundosInvulnerable > 0) {
            segundosInvulnerable--;
        }
    }

    /**
     * Indica si el jugador es invulnerable en este momento.
     *
     * @return {@code true} si quedan segundos de invulnerabilidad
     */
    public synchronized boolean isInvulnerable() {
        return segundosInvulnerable > 0;
    }

    /**
     * Devuelve el tiempo transcurrido en formato {@code mm:ss}.
     *
     * @return tiempo formateado
     */
    public String getTiempoTexto() {
        return FormatoTiempo.formatear(getSegundos());
    }

    /**
     * Devuelve el nivel actual, calculado a partir del tiempo.
     *
     * @return nivel empezando en uno
     */
    public int getNivel() {
        return 1 + getSegundos() / SEGUNDOS_POR_NIVEL;
    }

    /**
     * Devuelve las vidas restantes.
     *
     * @return vidas restantes
     */
    public synchronized int getVidas() {
        return vidas;
    }

    /**
     * Devuelve el puntaje acumulado.
     *
     * @return puntaje actual
     */
    public synchronized int getPuntaje() {
        return puntaje;
    }

    /**
     * Devuelve los segundos transcurridos.
     *
     * @return segundos de partida
     */
    public synchronized int getSegundos() {
        return segundos;
    }

    /**
     * Indica si la partida termino.
     *
     * @return {@code true} si no quedan vidas
     */
    public synchronized boolean isJuegoTerminado() {
        return juegoTerminado;
    }
}
