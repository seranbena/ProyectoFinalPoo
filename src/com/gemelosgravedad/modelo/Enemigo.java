package com.gemelosgravedad.modelo;

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
public abstract class Enemigo extends EntidadDesplazable {

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
}
