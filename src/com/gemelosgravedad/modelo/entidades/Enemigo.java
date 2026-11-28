package com.gemelosgravedad.modelo.entidades;

import com.gemelosgravedad.recursos.GestorRecursos;

/**
 * Base de los enemigos que avanzan de derecha a izquierda.
 *
 * <p>Hereda el desplazamiento y la desactivacion de
 * {@link EntidadDesplazable}. La velocidad aumenta con la dificultad de la
 * partida.</p>
 *
 * @see EnemigoCaminante
 * @see EnemigoTecho
 * @see EnemigoVolador
 */
public abstract sealed class Enemigo extends EntidadDesplazable
        permits EnemigoCaminante, EnemigoTecho, EnemigoVolador {

    /**
     * Crea un enemigo y carga su animacion.
     *
     * @param x coordenada horizontal inicial
     * @param y coordenada vertical inicial
     * @param ancho ancho del rectangulo de colision
     * @param alto alto del rectangulo de colision
     * @param velocidad velocidad de avance horizontal
     * @param carpetaSprites carpeta de sprites dentro de {@code resources}
     * @param cantidadCuadros numero de cuadros de la animacion
     */
    protected Enemigo(int x, int y, int ancho, int alto, double velocidad,
                      String carpetaSprites, int cantidadCuadros) {
        super(x, y, ancho, alto, velocidad);
        this.sprites = GestorRecursos.cargarCuadros(carpetaSprites, cantidadCuadros);
    }

    /**
     * Aumenta la velocidad de avance del enemigo.
     *
     * <p>La regla de dificultad vive en {@code EstadoJuego}; este metodo
     * mantiene encapsulado el ajuste de velocidad de la jerarquia.</p>
     *
     * @param incremento pixeles adicionales por paso
     */
    public void acelerar(double incremento) {
        aumentarVelocidad(incremento);
    }
}
