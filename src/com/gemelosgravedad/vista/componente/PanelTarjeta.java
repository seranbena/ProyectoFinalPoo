package com.gemelosgravedad.vista.componente;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.Serial;
import javax.swing.JPanel;

/**
 * Tarjeta plana con borde negro, sombra dura y barra de acento lateral.
 *
 * <p>Es el contenedor comun de formularios, tablas y carteles. La tarjeta no
 * pinta su contenido: las pantallas le agregan componentes con un
 * {@link javax.swing.BoxLayout} o un {@link java.awt.BorderLayout}.</p>
 */
public final class PanelTarjeta extends JPanel {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final int SALTO = 9;
    private static final int BORDE = 4;
    private static final int BARRA_ACENTO = 10;
    private static final int MARGEN_ACENTO = 7;

    /** Color de relleno de la tarjeta. */
    private final Color colorFondo;

    /** Color de la barra de acento lateral. */
    private final Color colorAcento;

    /**
     * Crea una tarjeta con los colores indicados.
     *
     * @param colorFondo color de relleno
     * @param colorAcento color de la barra lateral
     */
    public PanelTarjeta(Color colorFondo, Color colorAcento) {
        this.colorFondo = colorFondo;
        this.colorAcento = colorAcento;
        setOpaque(false);
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
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int ancho = getWidth() - SALTO;
            int alto = getHeight() - SALTO;
            if (ancho <= BORDE * 2 || alto <= BORDE * 2) {
                return;
            }

            g2.setColor(EstiloBrutal.NEGRO);
            g2.fillRect(SALTO, SALTO, ancho, alto);

            g2.setColor(colorFondo);
            g2.fillRect(0, 0, ancho, alto);

            g2.setColor(EstiloBrutal.NEGRO);
            g2.setStroke(new BasicStroke(BORDE));
            g2.drawRect(2, 2, ancho - 4, alto - 4);

            g2.setColor(colorAcento);
            g2.fillRect(MARGEN_ACENTO, MARGEN_ACENTO,
                    BARRA_ACENTO, alto - MARGEN_ACENTO * 2);
        } finally {
            g2.dispose();
        }
    }
}
