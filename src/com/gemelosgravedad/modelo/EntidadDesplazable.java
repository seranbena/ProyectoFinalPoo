package com.gemelosgravedad.modelo;

/**
 * Entidad que avanza horizontalmente de derecha a izquierda.
 *
 * <p>Centraliza el desplazamiento, el avance de la animacion y la desactivacion
 * cuando el objeto termina de salir de la pantalla. De esta forma, cristales,
 * enemigos, orbes y power-ups comparten exactamente la misma logica de avance.</p>
 *
 * @see Cristal
 * @see Enemigo
 * @see ObjetoFlotante
 */
public abstract class EntidadDesplazable extends Entidad {

    /** Margen por defecto que se deja antes de desactivar una entidad. */
    private static final int MARGEN_SALIDA_POR_DEFECTO = 30;

    private final int margenSalida;
    private double velocidad;
    private double posicionX;

    /**
     * Crea una entidad desplazable con el margen de salida por defecto.
     *
     * @param x coordenada horizontal inicial
     * @param y coordenada vertical inicial
     * @param ancho ancho del rectangulo de colision
     * @param alto alto del rectangulo de colision
     * @param velocidad pixeles que avanza por paso de simulacion
     */
    protected EntidadDesplazable(int x, int y, int ancho, int alto, double velocidad) {
        this(x, y, ancho, alto, velocidad, MARGEN_SALIDA_POR_DEFECTO);
    }

    /**
     * Crea una entidad desplazable con un margen de salida personalizado.
     *
     * @param x coordenada horizontal inicial
     * @param y coordenada vertical inicial
     * @param ancho ancho del rectangulo de colision
     * @param alto alto del rectangulo de colision
     * @param velocidad pixeles que avanza por paso de simulacion
     * @param margenSalida distancia adicional que se conserva antes de desactivar
     */
    protected EntidadDesplazable(int x, int y, int ancho, int alto,
                                 double velocidad, int margenSalida) {
        super(x, y, ancho, alto);
        this.velocidad = velocidad;
        this.posicionX = x;
        this.margenSalida = margenSalida;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Avanza horizontalmente, actualiza la animacion y desactiva la entidad
     * cuando ya no puede ser visible.</p>
     */
    @Override
    public void actualizar() {
        contadorAnimacion++;
        posicionX -= velocidad;
        setX((int) Math.round(posicionX));
        if (getX() + getAncho() < -margenSalida) {
            setActiva(false);
        }
    }

    /**
     * Incrementa la velocidad horizontal de la entidad.
     *
     * @param incremento pixeles adicionales por paso
     */
    protected void aumentarVelocidad(double incremento) {
        velocidad += incremento;
    }
}
