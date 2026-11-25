package com.gemelosgravedad.modelo;

/**
 * Entidad desplazable que ademas oscila verticalmente con una onda senoidal.
 *
 * <p>Es la base de los objetos que flotan mientras avanzan, como los orbes de
 * cambio y los power-ups. La amplitud y la velocidad angular se fijan en el
 * constructor para que cada objeto conserve su propio ritmo.</p>
 *
 * @see PowerUp
 * @see OrbeCambio
 */
public abstract class ObjetoFlotante extends EntidadDesplazable {

    private final double baseY;
    private final double amplitud;
    private final double velocidadAngular;
    private double angulo;

    /**
     * Crea un objeto flotante.
     *
     * @param x coordenada horizontal inicial
     * @param y coordenada vertical central de la oscilacion
     * @param ancho ancho del rectangulo de colision
     * @param alto alto del rectangulo de colision
     * @param velocidad velocidad de avance horizontal
     * @param amplitud desplazamiento vertical maximo en pixeles
     * @param velocidadAngular incremento del angulo por paso de simulacion
     */
    protected ObjetoFlotante(int x, int y, int ancho, int alto, double velocidad,
                             double amplitud, double velocidadAngular) {
        super(x, y, ancho, alto, velocidad);
        this.baseY = y;
        this.amplitud = amplitud;
        this.velocidadAngular = velocidadAngular;
        this.angulo = 0;
    }

    /**
     * Aplica el siguiente paso de la oscilacion vertical alrededor de la
     * posicion vertical inicial.
     */
    protected void actualizarFlotacion() {
        angulo += velocidadAngular;
        setY((int) Math.round(baseY + Math.sin(angulo) * amplitud));
    }
}
