package com.gemelosgravedad.modelo.entidades;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Enemigo que camina cabeza abajo por el techo.
 *
 * @see Enemigo
 */
public final class EnemigoTecho extends Enemigo {

    private static final Color NEGRO = new Color(18, 18, 18);
    private static final Color MORADO = new Color(132, 74, 220);
    private static final Color MORADO_OSCURO = new Color(66, 28, 120);
    private static final int FRECUENCIA_ALETEO = 5;

    /**
     * Crea un enemigo de techo.
     *
     * @param x coordenada horizontal inicial
     * @param y coordenada vertical inicial
     * @param velocidad velocidad de avance horizontal
     */
    public EnemigoTecho(int x, int y, double velocidad) {
        super(x, y, 58, 50, velocidad, "sprites/enemigo_techo", 2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dibujar(Graphics2D g) {
        if (dibujarSprite(g, true)) {
            return;
        }
        int x = getX();
        int y = getY();
        int ancho = getAncho();
        int alto = getAlto();
        int aleteo = (contadorAnimacion / FRECUENCIA_ALETEO) % 2;

        int alaY = y + 4 + aleteo * 4;
        g.setColor(NEGRO);
        g.fillPolygon(new int[]{x + 2, x + 26, x + 26, x + 8},
                new int[]{alaY, y + 8, y + 30, alaY + 22}, 4);
        g.fillPolygon(new int[]{x + ancho - 2, x + ancho - 26, x + ancho - 26, x + ancho - 8},
                new int[]{alaY, y + 8, y + 30, alaY + 22}, 4);
        g.setColor(MORADO);
        g.fillPolygon(new int[]{x + 6, x + 22, x + 22, x + 11},
                new int[]{alaY + 4, y + 11, y + 27, alaY + 18}, 4);
        g.fillPolygon(new int[]{x + ancho - 6, x + ancho - 22, x + ancho - 22, x + ancho - 11},
                new int[]{alaY + 4, y + 11, y + 27, alaY + 18}, 4);

        g.setColor(NEGRO);
        g.fillRect(x + 18, y + 8, ancho - 36, alto - 12);
        g.setColor(MORADO_OSCURO);
        g.fillRect(x + 21, y + 11, ancho - 42, alto - 18);

        g.setColor(NEGRO);
        g.fillRect(x + 22, y + 18, ancho - 44, 8);
        g.setColor(Color.WHITE);
        g.fillRect(x + 24, y + 19, 6, 6);
        g.fillRect(x + ancho - 30, y + 19, 6, 6);
        g.setColor(NEGRO);
        g.fillRect(x + 26, y + 21, 3, 3);
        g.fillRect(x + ancho - 28, y + 21, 3, 3);

        g.setColor(NEGRO);
        g.fillRect(x + 23, y + 32, ancho - 46, 7);
        g.setColor(Color.WHITE);
        g.fillRect(x + 24, y + 33, 4, 5);
        g.fillRect(x + ancho - 28, y + 33, 4, 5);

        g.setColor(NEGRO);
        g.fillRect(x + 24, y + 2, 5, 10);
        g.fillRect(x + ancho - 29, y + 2, 5, 10);
        g.setColor(MORADO);
        g.fillRect(x + 26, y + 2, 2, 7);
        g.fillRect(x + ancho - 27, y + 2, 2, 7);
    }
}
