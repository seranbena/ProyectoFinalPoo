package com.gemelosgravedad.modelo.datos;

import com.gemelosgravedad.modelo.entidades.PowerUp;
import com.gemelosgravedad.modelo.juego.PoderActivo;
import java.awt.Color;

/**
 * Tipos de power-up disponibles en la partida.
 *
 * <p>Cada valor reune su duracion, el sprite que lo representa, la letra usada
 * en el dibujo de respaldo y su color. Asi se evitan cadenas y numeros magicos
 * repartidos entre el modelo y la vista.</p>
 *
 * @see PowerUp
 * @see PoderActivo
 */
public enum TipoPowerUp {

    /** Escudo temporal que absorbe un golpe. */
    ESCUDO(8, "sprites/powerup_escudo.png", "E", new Color(0, 209, 209)),

    /** Turbo temporal que destruye enemigos y aumenta el tamano. */
    TURBO(7, "sprites/powerup_turbo.png", "T", new Color(255, 116, 20));

    private final int duracionSegundos;
    private final String rutaSprite;
    private final String letra;
    private final Color color;

    TipoPowerUp(int duracionSegundos, String rutaSprite, String letra, Color color) {
        this.duracionSegundos = duracionSegundos;
        this.rutaSprite = rutaSprite;
        this.letra = letra;
        this.color = color;
    }

    /**
     * Devuelve cuantos segundos permanece activo el power-up.
     *
     * @return duracion en segundos
     */
    public int getDuracionSegundos() {
        return duracionSegundos;
    }

    /**
     * Devuelve la ruta del sprite asociado al power-up.
     *
     * @return ruta relativa dentro de {@code resources}
     */
    public String getRutaSprite() {
        return rutaSprite;
    }

    /**
     * Devuelve la letra que se dibuja cuando no hay sprite disponible.
     *
     * @return letra de respaldo
     */
    public String getLetra() {
        return letra;
    }

    /**
     * Devuelve el color representativo del power-up.
     *
     * @return color de acento
     */
    public Color getColor() {
        return color;
    }
}
