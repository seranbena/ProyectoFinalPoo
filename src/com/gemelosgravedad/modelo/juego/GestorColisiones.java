package com.gemelosgravedad.modelo.juego;

import com.gemelosgravedad.modelo.datos.Efecto;
import com.gemelosgravedad.modelo.entidades.Cristal;
import com.gemelosgravedad.modelo.entidades.Enemigo;
import com.gemelosgravedad.modelo.entidades.Entidad;
import com.gemelosgravedad.modelo.entidades.Jugador;
import com.gemelosgravedad.modelo.entidades.OrbeCambio;
import com.gemelosgravedad.modelo.entidades.PowerUp;
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
     *
     * <p>El {@code switch} es exhaustivo porque la jerarquia de {@link Entidad}
     * esta sellada: si se agrega un tipo nuevo, el compilador obliga a tratarlo
     * aqui.</p>
     */
    public void revisar() {
        for (int i = entidades.size() - 1; i >= 0; i--) {
            Entidad entidad = entidades.get(i);
            boolean golpea = (!jugadorPiso.isColgando() && entidad.colisionaCon(jugadorPiso))
                    || (!jugadorTecho.isColgando() && entidad.colisionaCon(jugadorTecho));
            if (!golpea) {
                continue;
            }
            switch (entidad) {
                case Cristal cristal -> recogerCristal(cristal, i);
                case PowerUp powerUp -> recogerPowerUp(powerUp, i);
                case OrbeCambio orbe -> recogerOrbe(orbe, i);
                case Enemigo enemigo -> chocarConEnemigo(i);
                case Jugador jugador -> {
                    // Los gemelos no colisionan contra si mismos.
                }
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
        consumir(indice, cristal.getValor(), Efecto.CRISTAL);
    }

    /**
     * Activa un power-up y lo elimina.
     *
     * @param powerUp power-up recogido
     * @param indice posicion del power-up en la lista
     */
    private void recogerPowerUp(PowerUp powerUp, int indice) {
        poder.activar(powerUp.getTipo(), jugadorPiso, jugadorTecho);
        consumir(indice, 0, Efecto.POWER_UP);
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
        consumir(indice, orbe.getValor(), Efecto.POWER_UP);
    }

    /**
     * Resuelve el choque con un enemigo segun el power-up activo.
     *
     * @param indice posicion del enemigo en la lista
     */
    private void chocarConEnemigo(int indice) {
        if (jugadorPiso.isTurbo()) {
            consumir(indice, PUNTOS_ENEMIGO_TURBO, Efecto.GOLPE);
            return;
        }
        if (jugadorPiso.isEscudo()) {
            poder.desactivar(jugadorPiso, jugadorTecho);
            marcador.darInvulnerabilidad(SEGUNDOS_INVULNERABILIDAD_ESCUDO);
            consumir(indice, 0, Efecto.GOLPE);
            return;
        }
        if (marcador.isInvulnerable()) {
            return;
        }
        marcador.perderVida();
        consumir(indice, 0,
                marcador.isJuegoTerminado() ? Efecto.GAME_OVER : Efecto.GOLPE);
    }

    /**
     * Aplica puntos y efecto, y retira la entidad consumida de la partida.
     *
     * @param indice posicion de la entidad en la lista
     * @param puntos puntos que otorga el objeto
     * @param efecto efecto sonoro que se debe reproducir
     */
    private void consumir(int indice, int puntos, Efecto efecto) {
        marcador.sumarPuntos(puntos);
        efectos.add(efecto);
        entidades.remove(indice);
    }
}
