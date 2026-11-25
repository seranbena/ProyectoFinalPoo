package com.gemelosgravedad.modelo;

import java.util.List;

/**
 * Revisa los choques y aplica sus consecuencias: puntos, golpes, power-ups y
 * cambios de gravedad.
 *
 * <p>Trabaja sobre la lista de entidades del {@link EstadoJuego}. La vista no
 * participa en esta logica; solo recibe los efectos para reproducir sonidos.</p>
 *
 * @see EstadoJuego
 * @see Efecto
 */
public class GestorColisiones {

    private static final int PUNTOS_ENEMIGO_TURBO = 50;
    private static final int SEGUNDOS_INVULNERABILIDAD_ESCUDO = 1;

    private final Jugador jugadorPiso;
    private final Jugador jugadorTecho;
    private final List<Entidad> entidades;
    private final List<Efecto> efectos;
    private final Marcador marcador;
    private final PoderActivo poder;

    /**
     * Crea el gestor de colisiones de una partida.
     *
     * @param jugadorPiso gemelo que camina por el piso
     * @param jugadorTecho gemelo que cuelga del techo
     * @param entidades lista de entidades de la partida
     * @param efectos lista compartida donde se acumulan los efectos
     * @param marcador marcador de la partida
     * @param poder control del power-up activo
     */
    public GestorColisiones(Jugador jugadorPiso, Jugador jugadorTecho,
                            List<Entidad> entidades, List<Efecto> efectos,
                            Marcador marcador, PoderActivo poder) {
        this.jugadorPiso = jugadorPiso;
        this.jugadorTecho = jugadorTecho;
        this.entidades = entidades;
        this.efectos = efectos;
        this.marcador = marcador;
        this.poder = poder;
    }

    /**
     * Recorre las entidades y resuelve las colisiones con los dos gemelos.
     */
    public void revisar() {
        for (int i = entidades.size() - 1; i >= 0; i--) {
            Entidad entidad = entidades.get(i);
            boolean golpea = (!jugadorPiso.isColgando() && entidad.colisionaCon(jugadorPiso))
                    || (!jugadorTecho.isColgando() && entidad.colisionaCon(jugadorTecho));
            if (!golpea) {
                continue;
            }
            if (entidad instanceof Cristal cristal) {
                recogerCristal(cristal, i);
            } else if (entidad instanceof PowerUp powerUp) {
                recogerPowerUp(powerUp, i);
            } else if (entidad instanceof OrbeCambio orbe) {
                recogerOrbe(orbe, i);
            } else if (entidad instanceof Enemigo) {
                chocarConEnemigo(i);
            }
        }
    }

    /**
     * Suma los puntos de un cristal y lo elimina.
     *
     * @param cristal cristal recogido
     * @param indice posicion del cristal en la lista
     */
    private void recogerCristal(Cristal cristal, int indice) {
        marcador.sumarPuntos(cristal.getValor());
        efectos.add(Efecto.CRISTAL);
        entidades.remove(indice);
    }

    /**
     * Activa un power-up y lo elimina.
     *
     * @param powerUp power-up recogido
     * @param indice posicion del power-up en la lista
     */
    private void recogerPowerUp(PowerUp powerUp, int indice) {
        poder.activar(powerUp.getTipo(), jugadorPiso, jugadorTecho);
        efectos.add(Efecto.POWER_UP);
        entidades.remove(indice);
    }

    /**
     * Intercambia los gemelos si ninguno esta colgado, suma los puntos del
     * orbe y lo elimina.
     *
     * @param orbe orbe recogido
     * @param indice posicion del orbe en la lista
     */
    private void recogerOrbe(OrbeCambio orbe, int indice) {
        if (!jugadorPiso.isColgando() && !jugadorTecho.isColgando()) {
            jugadorPiso.intercambiarCon(jugadorTecho);
        }
        marcador.sumarPuntos(orbe.getValor());
        efectos.add(Efecto.POWER_UP);
        entidades.remove(indice);
    }

    /**
     * Resuelve el choque con un enemigo segun el power-up activo.
     *
     * @param indice posicion del enemigo en la lista
     */
    private void chocarConEnemigo(int indice) {
        if (jugadorPiso.isTurbo()) {
            marcador.sumarPuntos(PUNTOS_ENEMIGO_TURBO);
            entidades.remove(indice);
            efectos.add(Efecto.GOLPE);
            return;
        }
        if (jugadorPiso.isEscudo()) {
            poder.desactivar(jugadorPiso, jugadorTecho);
            marcador.darInvulnerabilidad(SEGUNDOS_INVULNERABILIDAD_ESCUDO);
            entidades.remove(indice);
            efectos.add(Efecto.GOLPE);
            return;
        }
        if (marcador.isInvulnerable()) {
            return;
        }
        marcador.perderVida();
        entidades.remove(indice);
        efectos.add(marcador.isJuegoTerminado() ? Efecto.GAME_OVER : Efecto.GOLPE);
    }
}
