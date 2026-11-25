package com.gemelosgravedad.vista.componente;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.Serial;
import javax.swing.JPanel;

/**
 * Fondo de papel con rejilla tecnica, marco negro y barra roja lateral.
 *
 * <p>Es solo decoracion: no contiene reglas del juego. Las pantallas de menu lo
 * usan como base y agregan sus propios componentes encima.</p>
 */
public class PanelFondo extends JPanel {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Color REJILLA = new Color(214, 208, 194);
    private static final Color REJILLA_FUERTE = new Color(195, 188, 173);
    private static final Color CRUZ = new Color(178, 171, 156);
    private static final int PASO_REJILLA = 40;
    private static final int PASO_REJILLA_FUERTE = 200;
    private static final int BARRA_LATERAL = 12;
    private static final int MARCO = 8;
    private static final int MARCA_ESQUINA = 10;
    private static final int MARGEN_MARCA = 24;

    /**
     * Crea un fondo de papel sin configuracion adicional.
     */
    public PanelFondo() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_OFF);

            int ancho = getWidth();
            int alto = getHeight();

            g2.setColor(EstiloBrutal.PAPEL);
            g2.fillRect(0, 0, ancho, alto);

            g2.setColor(REJILLA);
            g2.setStroke(new BasicStroke(1f));
            for (int x = 0; x < ancho; x += PASO_REJILLA) {
                g2.drawLine(x, 0, x, alto);
            }
            for (int y = 0; y < alto; y += PASO_REJILLA) {
                g2.drawLine(0, y, ancho, y);
            }

            g2.setColor(REJILLA_FUERTE);
            g2.setStroke(new BasicStroke(2f));
            for (int x = 0; x < ancho; x += PASO_REJILLA_FUERTE) {
                g2.drawLine(x, 0, x, alto);
            }
            for (int y = 0; y < alto; y += PASO_REJILLA_FUERTE) {
                g2.drawLine(0, y, ancho, y);
            }

            for (int x = PASO_REJILLA_FUERTE; x < ancho; x += PASO_REJILLA_FUERTE) {
                for (int y = PASO_REJILLA_FUERTE; y < alto; y += PASO_REJILLA_FUERTE) {
                    EstiloBrutal.cruz(g2, x, y, 11, CRUZ);
                }
            }

            g2.setColor(EstiloBrutal.ROJO);
            g2.fillRect(0, 0, BARRA_LATERAL, alto);

            g2.setColor(EstiloBrutal.NEGRO);
            g2.setStroke(new BasicStroke(MARCO));
            g2.drawRect(MARCO / 2, MARCO / 2, ancho - MARCO, alto - MARCO);

            g2.fillRect(MARGEN_MARCA, MARGEN_MARCA, MARCA_ESQUINA, MARCA_ESQUINA);
            g2.fillRect(ancho - MARGEN_MARCA - MARCA_ESQUINA,
                    alto - MARGEN_MARCA - MARCA_ESQUINA, MARCA_ESQUINA, MARCA_ESQUINA);
        } finally {
            g2.dispose();
        }
    }
}
