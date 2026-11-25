package com.gemelosgravedad.modelo;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Enemigo que camina por el piso.
 *
 * @see Enemigo
 */
public class EnemigoCaminante extends Enemigo {

    private static final Color NEGRO = new Color(18, 18, 18);
    private static final Color ROJO = new Color(223, 46, 38);
    private static final Color ROJO_OSCURO = new Color(120, 22, 48);
    private static final Color SOMBRA = new Color(0, 0, 0, 90);
    private static final int FRECUENCIA_PASO = 6;
    private static final int PUNTAS = 4;
    private static final int SEPARACION_PUNTAS = 13;

    /**
     * Crea un enemigo caminante.
     *
     * @param x coordenada horizontal inicial
     * @param y coordenada vertical inicial
     * @param velocidad velocidad de avance horizontal
     */
    public EnemigoCaminante(int x, int y, double velocidad) {
        super(x, y, 58, 50, velocidad, "sprites/enemigo_caminante", 2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dibujar(Graphics2D g) {
        if (dibujarSprite(g, false)) {
            return;
        }
        int x = getX();
        int y = getY();
        int ancho = getAncho();
        int alto = getAlto();
        int paso = (contadorAnimacion / FRECUENCIA_PASO) % 2;

        g.setColor(SOMBRA);
        g.fillRect(x + 2, y + alto - 6, ancho - 4, 6);

        g.setColor(ROJO_OSCURO);
        g.fillRect(x + 2, y + alto - 14, 16, 14);
        g.fillRect(x + ancho - 18, y + alto - 14, 16, 14);
        g.setColor(NEGRO);
        g.fillRect(x, y + alto - 6, 18, 6);
        g.fillRect(x + ancho - 18, y + alto - 6, 18, 6);

        g.setColor(NEGRO);
        g.fillRect(x, y + 8, ancho, alto - 22);
        g.setColor(ROJO);
        g.fillRect(x + 3, y + 11, ancho - 6, alto - 28);

        for (int i = 0; i < PUNTAS; i++) {
            int picoX = x + 6 + i * SEPARACION_PUNTAS;
            g.setColor(NEGRO);
            g.fillPolygon(new int[]{picoX, picoX + 6, picoX + 12},
                    new int[]{y + 12, y - 2 - paso * 2, y + 12}, 3);
            g.setColor(ROJO_OSCURO);
            g.fillPolygon(new int[]{picoX + 3, picoX + 6, picoX + 9},
                    new int[]{y + 10, y + 2, y + 10}, 3);
        }

        g.setColor(NEGRO);
        g.fillRect(x + 6, y + 20, ancho - 12, 8);
        g.setColor(Color.WHITE);
        g.fillRect(x + 10, y + 22, 9, 5);
        g.fillRect(x + ancho - 19, y + 22, 9, 5);
        g.setColor(NEGRO);
        g.fillRect(x + 13, y + 24, 3, 3);
        g.fillRect(x + ancho - 16, y + 24, 3, 3);

        g.setColor(NEGRO);
        g.fillRect(x + 7, y + alto - 22, ancho - 14, 10);
        for (int i = 0; i < PUNTAS; i++) {
            g.setColor(Color.WHITE);
            g.fillRect(x + 10 + i * 11, y + alto - 20, 5, 6);
        }

        int salto = paso == 0 ? 0 : 2;
        g.setColor(NEGRO);
        g.fillRect(x + 2, y + alto - 16 - salto, 6, 10 + salto);
        g.fillRect(x + ancho - 8, y + alto - 16, 6, 10);
    }
}
