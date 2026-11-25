package com.gemelosgravedad.vista.componente;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serial;
import javax.swing.JPanel;

/**
 * Franja horizontal de diagonales industriales.
 *
 * <p>Se coloca sobre las barras negras de cabecera y pie para reforzar el
 * lenguaje visual de senalizacion de peligro.</p>
 */
public final class FranjaPeligro extends JPanel {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final int ANCHO_MINIMO = 10;

    /** Color de las diagonales de la franja. */
    private final Color color;

    /**
     * Crea una franja de peligro.
     *
     * @param color color de las diagonales
     * @param alto alto fijo de la franja en pixeles
     */
    public FranjaPeligro(Color color, int alto) {
        this.color = color;
        setOpaque(false);
        setPreferredSize(new Dimension(ANCHO_MINIMO, alto));
        setMinimumSize(new Dimension(ANCHO_MINIMO, alto));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, alto));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            EstiloBrutal.franjas(g2, 0, 0, getWidth(), getHeight(), color);
        } finally {
            g2.dispose();
        }
    }
}
