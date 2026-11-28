package com.gemelosgravedad.vista.dibujo;

import com.gemelosgravedad.modelo.juego.EstadoJuego;
import com.gemelosgravedad.vista.componente.EstiloBrutal;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

/**
 * Dibuja el escenario industrial: arena plana, rejilla, railes y franjas de
 * peligro.
 *
 * <p>Es una utilidad sin estado. El unico dato variable es el contador de
 * pasos, que anima las marcas rojas de la linea central.</p>
 *
 * @see EstadoJuego
 */
public final class DibujoFondo {

    private static final Color ARENA = new Color(24, 24, 24);
    private static final Color REJILLA = new Color(38, 38, 38);
    private static final Color REJILLA_FUERTE = new Color(53, 53, 53);
    private static final Color CRUZ = new Color(66, 66, 66);
    private static final Color MARCA = new Color(33, 33, 33);
    private static final Color LINEA_CENTRAL = new Color(62, 58, 44);
    private static final Color LEYENDA_LATERAL = new Color(58, 58, 58);
    private static final Color PERNOS = new Color(55, 55, 55);
    private static final Color BORDE_RAIL = new Color(48, 48, 48);
    private static final int PASO_REJILLA = 40;
    private static final int PASO_REJILLA_FUERTE = 160;
    private static final int MARCAS_CENTRALES = 5;
    private static final int SEPARACION_MARCAS = 97;
    private static final int TAMANO_MARCA = 8;
    private static final int ALTO_MARCA_GD = 170;
    private static final int X_MARCA_GD = 700;
    private static final int Y_MARCA_GD = 430;
    private static final BasicStroke TRAZO_FINO = new BasicStroke(1f);
    private static final BasicStroke TRAZO_MEDIO = new BasicStroke(2f);
    private static final BasicStroke TRAZO_CENTRAL = new BasicStroke(2f,
            BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1f,
            new float[]{10f, 16f}, 0f);

    private DibujoFondo() {
    }

    /**
     * Pinta el fondo completo de la partida.
     *
     * @param g contexto grafico
     * @param tick contador de pasos usado por las animaciones
     */
    public static void dibujar(Graphics2D g, int tick) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int ancho = EstadoJuego.ANCHO;
        int alto = EstadoJuego.ALTO;

        g.setColor(ARENA);
        g.fillRect(0, 0, ancho, alto);
        dibujarRejilla(g, tick);
        dibujarMarca(g);
        dibujarBandas(g);
        dibujarRailes(g);
    }

    /**
     * Dibuja la rejilla tecnica y las marcas rojas que descienden.
     *
     * @param g contexto grafico
     * @param tick contador de pasos
     */
    private static void dibujarRejilla(Graphics2D g, int tick) {
        int techo = EstadoJuego.TECHO;
        int piso = EstadoJuego.PISO;

        g.setStroke(TRAZO_FINO);
        g.setColor(REJILLA);
        for (int x = 0; x <= EstadoJuego.ANCHO; x += PASO_REJILLA) {
            g.drawLine(x, techo, x, piso);
        }
        for (int y = techo; y <= piso; y += PASO_REJILLA) {
            g.drawLine(0, y, EstadoJuego.ANCHO, y);
        }

        g.setStroke(TRAZO_MEDIO);
        g.setColor(REJILLA_FUERTE);
        for (int x = 0; x <= EstadoJuego.ANCHO; x += PASO_REJILLA_FUERTE) {
            g.drawLine(x, techo, x, piso);
        }
        for (int y = techo + PASO_REJILLA_FUERTE; y <= piso; y += PASO_REJILLA_FUERTE) {
            g.drawLine(0, y, EstadoJuego.ANCHO, y);
        }

        for (int x = PASO_REJILLA_FUERTE; x < EstadoJuego.ANCHO; x += PASO_REJILLA_FUERTE) {
            for (int y = techo + PASO_REJILLA_FUERTE; y < piso;
                    y += PASO_REJILLA_FUERTE) {
                EstiloBrutal.cruz(g, x, y, 9, CRUZ);
            }
        }

        g.setColor(LINEA_CENTRAL);
        g.setStroke(TRAZO_CENTRAL);
        g.drawLine(EstadoJuego.ANCHO / 2, techo + 10,
                EstadoJuego.ANCHO / 2, piso - 10);

        g.setColor(EstiloBrutal.ROJO);
        int recorrido = piso - techo - 80;
        for (int i = 0; i < MARCAS_CENTRALES; i++) {
            int y = techo + 40 + ((i * SEPARACION_MARCAS + tick * 2) % recorrido);
            g.fillRect(EstadoJuego.ANCHO / 2 - TAMANO_MARCA / 2, y,
                    TAMANO_MARCA, TAMANO_MARCA);
        }
    }

    /**
     * Dibuja la marca de agua y la leyenda vertical del sector.
     *
     * @param g contexto grafico
     */
    private static void dibujarMarca(Graphics2D g) {
        g.setFont(EstiloBrutal.titulo(ALTO_MARCA_GD));
        g.setColor(MARCA);
        g.drawString("GD", X_MARCA_GD, Y_MARCA_GD);

        Font fuente = EstiloBrutal.mono(10);
        g.setFont(fuente);
        g.setColor(LEYENDA_LATERAL);
        AffineTransform original = g.getTransform();
        g.rotate(-Math.PI / 2);
        g.drawString("SECTOR 01 // GRAVEDAD NEGATIVA", -470, 988);
        g.setTransform(original);
    }

    /**
     * Dibuja la banda superior, la inferior y sus franjas de peligro.
     *
     * @param g contexto grafico
     */
    private static void dibujarBandas(Graphics2D g) {
        g.setColor(EstiloBrutal.NEGRO);
        g.fillRect(0, 0, EstadoJuego.ANCHO, EstadoJuego.TECHO);
        g.fillRect(0, EstadoJuego.PISO, EstadoJuego.ANCHO,
                EstadoJuego.ALTO - EstadoJuego.PISO);

        EstiloBrutal.franjas(g, 0, EstadoJuego.TECHO - 8, EstadoJuego.ANCHO, 8,
                EstiloBrutal.AMARILLO);
        EstiloBrutal.franjas(g, 0, EstadoJuego.PISO, EstadoJuego.ANCHO, 8,
                EstiloBrutal.AZUL);

        g.setColor(EstiloBrutal.NEGRO);
        g.fillRect(0, EstadoJuego.TECHO - 11, EstadoJuego.ANCHO, 3);
        g.fillRect(0, EstadoJuego.PISO + 8, EstadoJuego.ANCHO, 3);

        g.setColor(PERNOS);
        for (int x = 24; x < EstadoJuego.ANCHO; x += 96) {
            g.fillRect(x, EstadoJuego.PISO + 20, 4, 4);
            g.fillRect(x, 34, 4, 4);
        }
    }

    /**
     * Dibuja los railes laterales y sus marcas de color.
     *
     * @param g contexto grafico
     */
    private static void dibujarRailes(Graphics2D g) {
        g.setColor(EstiloBrutal.NEGRO);
        g.fillRect(0, 0, 7, EstadoJuego.ALTO);
        g.fillRect(EstadoJuego.ANCHO - 7, 0, 7, EstadoJuego.ALTO);

        g.setColor(BORDE_RAIL);
        g.fillRect(7, 0, 2, EstadoJuego.ALTO);
        g.fillRect(EstadoJuego.ANCHO - 9, 0, 2, EstadoJuego.ALTO);

        g.setColor(EstiloBrutal.AMARILLO);
        for (int y = EstadoJuego.TECHO + 24; y < EstadoJuego.PISO; y += 120) {
            g.fillRect(0, y, 7, 18);
        }
        g.setColor(EstiloBrutal.AZUL);
        for (int y = EstadoJuego.TECHO + 84; y < EstadoJuego.PISO; y += 120) {
            g.fillRect(EstadoJuego.ANCHO - 7, y, 7, 18);
        }
    }
}
