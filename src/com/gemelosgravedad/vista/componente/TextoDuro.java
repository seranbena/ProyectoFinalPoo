package com.gemelosgravedad.vista.componente;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.Serial;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

/**
 * Texto decorativo con sombra solida y alineacion configurable.
 *
 * <p>Se usa en los carteles de las pantallas de menu alli donde un
 * {@link javax.swing.JLabel} no permite controlar la sombra con precision.</p>
 */
public final class TextoDuro extends JComponent {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final int MARGEN_EXTRA = 4;

    /** Contenido del texto. */
    private final String texto;

    /** Fuente usada para medir y dibujar. */
    private final Font fuente;

    /** Color principal del texto. */
    private final Color color;

    /** Color de la sombra solida. */
    private final Color sombra;

    /** Desplazamiento de la sombra en pixeles. */
    private final int salto;

    /** Alineacion horizontal del texto. */
    private int alineacion = SwingConstants.CENTER;

    /**
     * Crea un texto decorativo.
     *
     * @param texto contenido inicial
     * @param fuente fuente del texto
     * @param color color principal
     * @param sombra color de la sombra
     * @param salto desplazamiento de la sombra
     */
    public TextoDuro(String texto, Font fuente, Color color, Color sombra, int salto) {
        this.texto = texto;
        this.fuente = fuente;
        this.color = color;
        this.sombra = sombra;
        this.salto = salto;
        setFont(fuente);
        setOpaque(false);
    }

    /**
     * Cambia la alineacion horizontal del texto.
     *
     * @param alineacion una de las constantes de {@link SwingConstants}
     */
    public void setAlineacion(int alineacion) {
        this.alineacion = alineacion;
        repaint();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize() {
        FontMetrics medidas = getFontMetrics(fuente);
        return new Dimension(medidas.stringWidth(texto) + salto + MARGEN_EXTRA,
                medidas.getHeight() + salto + 2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(fuente);
            FontMetrics medidas = g2.getFontMetrics();
            int ancho = medidas.stringWidth(texto);
            int x;
            if (alineacion == SwingConstants.LEFT) {
                x = 0;
            } else if (alineacion == SwingConstants.RIGHT) {
                x = getWidth() - ancho - salto;
            } else {
                x = (getWidth() - ancho) / 2;
            }
            int y = (getHeight() - medidas.getHeight()) / 2 + medidas.getAscent();
            EstiloBrutal.textoDuro(g2, texto, x, y, color, sombra, salto);
        } finally {
            g2.dispose();
        }
    }
}
