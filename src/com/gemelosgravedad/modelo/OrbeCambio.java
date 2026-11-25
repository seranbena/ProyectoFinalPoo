package com.gemelosgravedad.modelo;

import com.gemelosgravedad.recursos.GestorRecursos;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Orbe de cambio: al recogerlo, los dos gemelos intercambian posicion y
 * sentido de la gravedad.
 *
 * <p>Flota mientras avanza para diferenciarse de los cristales y romper la
 * simetria de la partida.</p>
 *
 * @see ObjetoFlotante
 * @see GestorColisiones
 */
public class OrbeCambio extends ObjetoFlotante {

    /** Puntos que otorga recoger el orbe. */
    public static final int VALOR = 10;

    private static final Color MORADO = new Color(132, 74, 220);
    private static final Color AMARILLO = new Color(255, 211, 0);
    private static final Color NEGRO = new Color(18, 18, 18);
    private static final double AMPLITUD = 10.0;
    private static final double VELOCIDAD_ANGULAR = 0.06;
    private static final int FRECUENCIA_PARPADEO = 8;

    /**
     * Crea un orbe de cambio.
     *
     * @param x coordenada horizontal inicial
     * @param y coordenada vertical central de la oscilacion
     * @param velocidad velocidad de avance horizontal
     */
    public OrbeCambio(int x, int y, double velocidad) {
        super(x, y, 38, 38, velocidad, AMPLITUD, VELOCIDAD_ANGULAR);
        this.sprites = GestorRecursos.cargarCuadros("sprites/orbe", 2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actualizar() {
        super.actualizar();
        actualizarFlotacion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dibujar(Graphics2D g) {
        int x = getX();
        int y = getY();
        int ancho = getAncho();
        int alto = getAlto();

        if ((contadorAnimacion / FRECUENCIA_PARPADEO) % 2 == 0) {
            g.setColor(AMARILLO);
            g.drawRect(x - 5, y - 5, ancho + 10, alto + 10);
        }

        if (dibujarSprite(g, false)) {
            return;
        }

        int centroX = x + ancho / 2;
        int centroY = y + alto / 2;
        g.setColor(NEGRO);
        g.fillPolygon(crearDiamante(0, 0, 0));
        g.setColor(MORADO);
        g.fillPolygon(crearDiamante(4, 0, 0));

        g.setColor(AMARILLO);
        g.fillRect(centroX - 10, centroY - 3, 20, 6);
        g.fillPolygon(new int[]{centroX - 15, centroX - 8, centroX - 8},
                new int[]{centroY, centroY - 6, centroY + 6}, 3);
        g.fillPolygon(new int[]{centroX + 15, centroX + 8, centroX + 8},
                new int[]{centroY, centroY - 6, centroY + 6}, 3);
        g.setColor(NEGRO);
        g.fillRect(centroX - 2, centroY - 7, 4, 14);
    }

    /**
     * Devuelve los puntos que otorga el orbe.
     *
     * @return valor en puntos
     */
    public int getValor() {
        return VALOR;
    }
}
