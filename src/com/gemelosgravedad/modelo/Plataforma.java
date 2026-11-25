package com.gemelosgravedad.modelo;

/**
 * Bloque solido del escenario.
 *
 * <p>El piso roto y las plataformas flotantes son plataformas: el gemelo
 * normal aterriza encima y el invertido cuelga debajo. El tipo determina que
 * lado de la plataforma ofrece apoyo.</p>
 *
 * @see Nivel
 * @see Jugador
 */
public class Plataforma {

    /** Tramo de piso que solo sostiene a la gravedad normal. */
    public static final int SUELO = 0;

    /** Plataforma flotante que sirve para ambas gravedades. */
    public static final int FLOTANTE = 1;

    /** Tramo de techo que solo sostiene a la gravedad invertida. */
    public static final int TECHO = 2;

    private static final int MARGEN_APOYO = 6;

    private final int x;
    private final int y;
    private final int ancho;
    private final int alto;
    private final int tipo;

    /**
     * Crea una plataforma rectangular.
     *
     * @param x coordenada horizontal del borde izquierdo
     * @param y coordenada vertical del borde superior
     * @param ancho ancho en pixeles
     * @param alto alto en pixeles
     * @param tipo uno de {@link #SUELO}, {@link #FLOTANTE} o {@link #TECHO}
     */
    public Plataforma(int x, int y, int ancho, int alto, int tipo) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.tipo = tipo;
    }

    /**
     * Comprueba si el cuerpo pisa una parte util de la plataforma.
     *
     * @param cuerpoX coordenada horizontal del cuerpo
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
     * <p>El piso solo sostiene al gemelo normal, el techo solo al invertido y
     * las plataformas flotantes sirven para los dos.</p>
     *
     * @param sentidoGravedad {@code 1} para el piso, {@code -1} para el techo
     * @return {@code true} si la plataforma puede sostener al jugador
     */
    public boolean apoyaA(int sentidoGravedad) {
        if (sentidoGravedad == 1) {
            return tipo != TECHO;
        }
        return tipo != SUELO;
    }

    /**
     * Devuelve la coordenada horizontal del borde izquierdo.
     *
     * @return coordenada X
     */
    public int getX() {
        return x;
    }

    /**
     * Devuelve la coordenada vertical del borde superior.
     *
     * @return coordenada Y
     */
    public int getY() {
        return y;
    }

    /**
     * Devuelve el ancho de la plataforma.
     *
     * @return ancho en pixeles
     */
    public int getAncho() {
        return ancho;
    }

    /**
     * Devuelve el alto de la plataforma.
     *
     * @return alto en pixeles
     */
    public int getAlto() {
        return alto;
    }

    /**
     * Devuelve el tipo de plataforma.
     *
     * @return {@link #SUELO}, {@link #FLOTANTE} o {@link #TECHO}
     */
    public int getTipo() {
        return tipo;
    }

    /**
     * Devuelve la coordenada vertical del borde inferior.
     *
     * @return coordenada Y del borde inferior
     */
    public int getBordeInferior() {
        return y + alto;
    }
}
