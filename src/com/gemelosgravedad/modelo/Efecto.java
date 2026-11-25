package com.gemelosgravedad.modelo;

import com.gemelosgravedad.recursos.GestorSonidos;

/**
 * Efectos de sonido que puede producir una partida.
 *
 * <p>Cada valor conserva la clave usada por
 * {@link com.gemelosgravedad.recursos.GestorSonidos}. De este modo el modelo
 * publica eventos tipados y el controlador decide cuando reproducirlos.</p>
 *
 * @see GestorColisiones
 * @see com.gemelosgravedad.recursos.GestorSonidos
 */
public enum Efecto {

    /** Se recogio un cristal. */
    CRISTAL(GestorSonidos.CRISTAL),

    /** Se recogio un power-up o un orbe de cambio. */
    POWER_UP(GestorSonidos.POWER_UP),

    /** El jugador recibio un golpe o destruyo un enemigo. */
    GOLPE(GestorSonidos.GOLPE),

    /** La partida termino. */
    GAME_OVER(GestorSonidos.GAME_OVER);

    private final String claveSonido;

    Efecto(String claveSonido) {
        this.claveSonido = claveSonido;
    }

    /**
     * Devuelve la clave del sonido asociado al efecto.
     *
     * @return clave registrada en el gestor de sonidos
     */
    public String getClaveSonido() {
        return claveSonido;
    }
}
