package com.gemelosgravedad.modelo.juego;

import com.gemelosgravedad.modelo.datos.TipoPowerUp;
import com.gemelosgravedad.modelo.entidades.Jugador;
import com.gemelosgravedad.modelo.entidades.PowerUp;

/**
 * Controla el power-up activo y su efecto sobre los dos gemelos.
 *
 * <p>Solo puede haber un power-up activo a la vez. Al activar uno nuevo se
 * desactiva el anterior, se restauran los tamanos si hacia falta y se aplica
 * el efecto correspondiente a ambos jugadores.</p>
 *
 * @see TipoPowerUp
 * @see PowerUp
 */
public class PoderActivo {

    private TipoPowerUp tipo;
    private int segundos;

    /**
     * Crea un control de poder sin ningun efecto activo.
     */
    public PoderActivo() {
        tipo = null;
        segundos = 0;
    }

    /**
     * Activa un power-up para los dos gemelos y desactiva el anterior.
     *
     * @param nuevoTipo tipo de power-up que se activa
     * @param piso gemelo que camina por el piso
     * @param techo gemelo que cuelga del techo
     */
    public synchronized void activar(TipoPowerUp nuevoTipo, Jugador piso, Jugador techo) {
        desactivar(piso, techo);
        tipo = nuevoTipo;
        segundos = nuevoTipo.getDuracionSegundos();
        switch (nuevoTipo) {
            case ESCUDO -> {
                piso.setEscudo(true);
                techo.setEscudo(true);
            }
            case TURBO -> {
                piso.setTurbo(true);
                techo.setTurbo(true);
                piso.cambiarTamano(Jugador.ANCHO_TURBO, Jugador.ALTO_TURBO);
                techo.cambiarTamano(Jugador.ANCHO_TURBO, Jugador.ALTO_TURBO);
            }
        }
    }

    /**
     * Descuenta un segundo de duracion y desactiva el poder al llegar a cero.
     *
     * @param piso gemelo que camina por el piso
     * @param techo gemelo que cuelga del techo
     */
    public synchronized void actualizarSegundo(Jugador piso, Jugador techo) {
        if (segundos > 0) {
            segundos--;
            if (segundos == 0) {
                desactivar(piso, techo);
            }
        }
    }

    /**
     * Desactiva el poder actual y restaura el tamano normal de los gemelos.
     *
     * @param piso gemelo que camina por el piso
     * @param techo gemelo que cuelga del techo
     */
    public synchronized void desactivar(Jugador piso, Jugador techo) {
        boolean teniaTurbo = piso.isTurbo() || techo.isTurbo();
        tipo = null;
        segundos = 0;
        piso.setEscudo(false);
        techo.setEscudo(false);
        piso.setTurbo(false);
        techo.setTurbo(false);
        if (teniaTurbo) {
            piso.cambiarTamano(Jugador.ANCHO_NORMAL, Jugador.ALTO_NORMAL);
            techo.cambiarTamano(Jugador.ANCHO_NORMAL, Jugador.ALTO_NORMAL);
        }
    }

    /**
     * Devuelve el tipo de power-up activo.
     *
     * @return tipo activo o {@code null} si no hay ninguno
     */
    public synchronized TipoPowerUp getTipo() {
        return tipo;
    }

    /**
     * Devuelve los segundos que quedan del power-up activo.
     *
     * @return segundos restantes o cero si no hay poder
     */
    public synchronized int getSegundos() {
        return segundos;
    }
}
