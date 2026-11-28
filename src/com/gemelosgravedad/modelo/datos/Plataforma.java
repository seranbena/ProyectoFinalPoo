package com.gemelosgravedad.modelo.datos;

import com.gemelosgravedad.modelo.entidades.Jugador;
import com.gemelosgravedad.modelo.juego.Nivel;
import java.util.Objects;

/**
 * Bloque solido del escenario.
 *
 * <p>El piso roto y las plataformas flotantes son plataformas: el gemelo
 * normal aterriza encima y el invertido cuelga debajo. El tipo determina que
 * lado de la plataforma ofrece apoyo.</p>
 *
 * @param x     coordenada horizontal del borde izquierdo
 * @param y     coordenada vertical del borde superior
 * @param ancho ancho en pixeles
 * @param alto  alto en pixeles
 * @param tipo  lado de apoyo de la plataforma
 * @see Nivel
 * @see Jugador
 */
public record Plataforma(int x, int y, int ancho, int alto, Tipo tipo) {

    private static final int MARGEN_APOYO = 6;

    /**
     * Lado de la plataforma que puede sostener a un gemelo.
     */
    public enum Tipo {

        /** Tramo de piso que solo sostiene a la gravedad normal. */
        SUELO,

        /** Plataforma flotante que sirve para ambas gravedades. */
        FLOTANTE,

        /** Tramo de techo que solo sostiene a la gravedad invertida. */
        TECHO;

        /**
         * Indica si este tipo de plataforma ofrece apoyo para una gravedad.
         *
         * @param sentidoGravedad {@code 1} para el piso, {@code -1} para el techo
         * @return {@code true} si la plataforma puede sostener al jugador
         */
        public boolean apoyaA(int sentidoGravedad) {
            return sentidoGravedad == 1 ? this != TECHO : this != SUELO;
        }
    }

    /**
     * Valida que la plataforma tenga un tipo.
     */
    public Plataforma {
        Objects.requireNonNull(tipo, "tipo");
    }

    /**
     * Comprueba si el cuerpo pisa una parte util de la plataforma.
     *
     * @param cuerpoX     coordenada horizontal del cuerpo
     * @param cuerpoAncho ancho del cuerpo
     * @return {@code true} si hay solapamiento horizontal suficiente
     */
    public boolean cubreHorizontalmente(int cuerpoX, int cuerpoAncho) {
        return cuerpoX + cuerpoAncho > x + MARGEN_APOYO
                && cuerpoX < x + ancho - MARGEN_APOYO;
    }

    /**
     * Indica si la plataforma ofrece apoyo para un sentido de gravedad.
     *
     * @param sentidoGravedad {@code 1} para el piso, {@code -1} para el techo
     * @return {@code true} si la plataforma puede sostener al jugador
     */
    public boolean apoyaA(int sentidoGravedad) {
        return tipo.apoyaA(sentidoGravedad);
    }

    /**
     * Devuelve la coordenada vertical del borde inferior.
     *
     * @return coordenada Y del borde inferior
     */
    public int bordeInferior() {
        return y + alto;
    }
}
