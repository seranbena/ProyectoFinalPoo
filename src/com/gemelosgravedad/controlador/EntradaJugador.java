package com.gemelosgravedad.controlador;

/**
 * Estado de las teclas de un jugador durante la partida.
 *
 * <p>Guarda si el jugador mantiene pulsada una direccion y si ya se proceso
 * el salto actual. Asi el controlador puede consultar una intencion de
 * movimiento sin repetir la logica de teclas para cada jugador.</p>
 */
final class EntradaJugador {

    private volatile boolean izquierda;
    private volatile boolean derecha;
    private volatile boolean saltoMantenido;

    /**
     * Marca la direccion izquierda como pulsada.
     */
    void presionarIzquierda() {
        izquierda = true;
    }

    /**
     * Marca la direccion izquierda como liberada.
     */
    void soltarIzquierda() {
        izquierda = false;
    }

    /**
     * Marca la direccion derecha como pulsada.
     */
    void presionarDerecha() {
        derecha = true;
    }

    /**
     * Marca la direccion derecha como liberada.
     */
    void soltarDerecha() {
        derecha = false;
    }

    /**
     * Devuelve la direccion solicitada en este instante.
     *
     * @return {@code -1} a la izquierda, {@code 1} a la derecha o {@code 0}
     */
    int getDireccion() {
        int direccion = 0;
        if (izquierda) {
            direccion--;
        }
        if (derecha) {
            direccion++;
        }
        return direccion;
    }

    /**
     * Indica si se debe procesar un salto nuevo.
     *
     * <p>El primer intento devuelve {@code true}; los siguientes se ignoran
     * hasta que se libere la tecla de salto.</p>
     *
     * @return {@code true} si el salto aun no se habia procesado
     */
    boolean intentarSalto() {
        if (saltoMantenido) {
            return false;
        }
        saltoMantenido = true;
        return true;
    }

    /**
     * Libera la tecla de salto para permitir otro intento.
     */
    void soltarSalto() {
        saltoMantenido = false;
    }

    /**
     * Borra todas las teclas al comenzar o terminar una partida.
     */
    void reiniciar() {
        izquierda = false;
        derecha = false;
        saltoMantenido = false;
    }
}
