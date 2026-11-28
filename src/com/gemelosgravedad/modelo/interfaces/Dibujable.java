package com.gemelosgravedad.modelo.interfaces;

import java.awt.Graphics2D;

/**
 * Contrato de los elementos que se dibujan en el panel de juego.
 *
 * <p>La vista depende de este contrato y no de las clases concretas: cualquier
 * entidad puede pintarse sin exponer su tipo real.</p>
 */
public interface Dibujable {

    /**
     * Dibuja el elemento sobre el contexto grafico indicado.
     *
     * @param g contexto grafico de la partida
     */
    void dibujar(Graphics2D g);
}
