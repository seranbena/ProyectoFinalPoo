package com.gemelosgravedad.modelo;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Enemigo volador que avanza y sube o baja en forma de onda.
 *
 * @see Enemigo
 */
public class EnemigoVolador extends Enemigo {

    private static final Color NEGRO = new Color(18, 18, 18);
    private static final Color VERDE = new Color(0, 168, 107);
    private static final Color VERDE_CLARO = new Color(120, 245, 190);
    private static final Color VERDE_OSCURO = new Color(0, 88, 60);
    private static final Color AMARILLO = new Color(255, 211, 0);
    private static final Color NARANJA = new Color(255, 116, 20);
    private static final Color HUESO = new Color(232, 227, 214);
    private static final double AMPLITUD = 42.0;
    private static final double VELOCIDAD_ANGULAR = 0.09;
    private static final int MARGEN_TECHO = 8;
    private static final int MARGEN_PISO = 8;
    private static final int FRECUENCIA_LUZ = 6;

    private final double baseY;
    private double angulo;

    /**
     * Crea un enemigo volador.
     *
     * @param x coordenada horizontal inicial
     * @param y coordenada vertical central de la oscilacion
     * @param velocidad velocidad de avance horizontal
     */
    public EnemigoVolador(int x, int y, double velocidad) {
        super(x, y, 56, 44, velocidad, "sprites/enemigo_volador", 2);
        this.baseY = y;
        this.angulo = 0;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Ademas del avance horizontal, calcula la posicion vertical de la onda
     * y la mantiene dentro de la zona jugable.</p>
     */
    @Override
    public void actualizar() {
        super.actualizar();
        angulo += VELOCIDAD_ANGULAR;
        int nuevoY = (int) Math.round(baseY + Math.sin(angulo) * AMPLITUD);
        if (nuevoY < EstadoJuego.TECHO + MARGEN_TECHO) {
            nuevoY = EstadoJuego.TECHO + MARGEN_TECHO;
        }
        if (nuevoY + getAlto() > EstadoJuego.PISO - MARGEN_PISO) {
            nuevoY = EstadoJuego.PISO - MARGEN_PISO - getAlto();
        }
        setY(nuevoY);
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
        int luz = (contadorAnimacion / FRECUENCIA_LUZ) % 2;

        g.setColor(NEGRO);
        g.fillRect(x + 2, y + 10, 18, 6);
        g.fillRect(x + ancho - 20, y + 10, 18, 6);
        g.setColor(HUESO);
        g.fillRect(x + 5, y + 6, 10, 6);
        g.fillRect(x + ancho - 15, y + 6, 10, 6);

        g.setColor(NEGRO);
        g.fillRect(x + 8, y + 20, ancho - 16, alto - 22);
        g.setColor(VERDE);
        g.fillRect(x + 11, y + 23, ancho - 22, alto - 28);
        g.setColor(VERDE_OSCURO);
        g.fillRect(x + 11, y + 27, ancho - 22, 6);

        g.setColor(NEGRO);
        g.fillRect(x + 10, y + alto - 18, 10, 10);
        g.fillRect(x + ancho - 20, y + alto - 18, 10, 10);
        g.setColor(luz == 0 ? AMARILLO : NARANJA);
        g.fillRect(x + 13, y + alto - 15, 4, 4);
        g.fillRect(x + ancho - 17, y + alto - 15, 4, 4);

        g.setColor(VERDE_CLARO);
        g.fillRect(x + 24, y + 34, ancho - 48, 7);
        g.setColor(NEGRO);
        g.fillRect(x + (ancho - 6) / 2, y + 2, 6, 8);
        g.setColor(VERDE);
        g.fillRect(x + (ancho - 2) / 2, y, 2, 5);

        if (luz == 0) {
            g.setColor(NEGRO);
            g.fillPolygon(new int[]{x + 20, x + ancho / 2, x + ancho - 20},
                    new int[]{y + alto - 8, y + alto + 5, y + alto - 8}, 3);
            g.setColor(AMARILLO);
            g.fillPolygon(new int[]{x + 24, x + ancho / 2, x + ancho - 24},
                    new int[]{y + alto - 8, y + alto + 2, y + alto - 8}, 3);
        } else {
            g.setColor(NEGRO);
            g.fillPolygon(new int[]{x + 22, x + ancho / 2, x + ancho - 22},
                    new int[]{y + alto - 8, y + alto + 7, y + alto - 8}, 3);
            g.setColor(NARANJA);
            g.fillPolygon(new int[]{x + 25, x + ancho / 2, x + ancho - 25},
                    new int[]{y + alto - 8, y + alto + 3, y + alto - 8}, 3);
        }
    }
}
