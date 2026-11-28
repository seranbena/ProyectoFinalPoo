package com.gemelosgravedad.vista.componente;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.Serial;
import java.util.Locale;
import javax.swing.JButton;

/**
 * Boton rectangular brutalista: borde negro, sombra dura e inversion al pasar.
 *
 * <p>El tamano se fija en el constructor para que los paneles con
 * {@link javax.swing.BoxLayout} conserven la misma alineacion.</p>
 */
public final class BotonEstilo extends JButton {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final int BORDE = 4;
    private static final int SALTO = 7;
    private static final int BARRA = 9;
    private static final int DESPLAZAMIENTO_PULSADO = 3;
    private static final BasicStroke TRAZO_BORDE = new BasicStroke(BORDE);

    /** Color de fondo del boton en estado normal. */
    private final Color colorBase;

    /**
     * Crea un boton con un tamano concreto.
     *
     * @param texto etiqueta del boton
     * @param colorBase color de fondo en estado normal
     * @param ancho ancho en pixeles
     * @param alto alto en pixeles
     */
    public BotonEstilo(String texto, Color colorBase, int ancho, int alto) {
        super(texto == null ? "" : texto.toUpperCase(Locale.ROOT));
        this.colorBase = colorBase;
        setFont(EstiloBrutal.titulo(17));
        setForeground(EstiloBrutal.textoSobre(colorBase));
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        fijarTamano(ancho, alto);
    }

    /**
     * Ajusta el tamano en los tres metodos que Swing consulta.
     *
     * @param ancho ancho en pixeles
     * @param alto alto en pixeles
     */
    private void fijarTamano(int ancho, int alto) {
        Dimension tamano = new Dimension(ancho, alto);
        setPreferredSize(tamano);
        setMinimumSize(tamano);
        setMaximumSize(tamano);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            boolean pulsado = getModel().isPressed();
            boolean encima = getModel().isRollover();
            boolean activo = isEnabled();
            int desplazamiento = pulsado ? DESPLAZAMIENTO_PULSADO : 0;

            int ancho = getWidth() - SALTO;
            int alto = getHeight() - SALTO;
            if (ancho <= BORDE * 2 || alto <= BORDE * 2) {
                return;
            }

            Color fondo = activo ? colorBase : EstiloBrutal.GRIS_CLARO;
            Color texto = activo ? EstiloBrutal.textoSobre(colorBase) : EstiloBrutal.GRIS;
            boolean invertido = activo && (encima || pulsado);
            if (invertido) {
                fondo = EstiloBrutal.NEGRO;
                texto = colorBase;
            }

            g2.setColor(EstiloBrutal.NEGRO);
            g2.fillRect(SALTO, SALTO, ancho, alto);

            g2.setColor(fondo);
            g2.fillRect(desplazamiento, desplazamiento, ancho, alto);

            g2.setColor(invertido ? colorBase : EstiloBrutal.NEGRO);
            g2.setStroke(TRAZO_BORDE);
            g2.drawRect(desplazamiento + BORDE / 2, desplazamiento + BORDE / 2,
                    ancho - BORDE, alto - BORDE);

            g2.setColor(invertido ? colorBase : EstiloBrutal.NEGRO);
            g2.fillRect(desplazamiento + BARRA, desplazamiento + BARRA,
                    8, alto - BARRA * 2);

            g2.setFont(getFont());
            FontMetrics medidas = g2.getFontMetrics();
            String etiqueta = getText();
            int x = (ancho - medidas.stringWidth(etiqueta)) / 2 + 5;
            int y = (alto - medidas.getHeight()) / 2 + medidas.getAscent();
            g2.setColor(texto);
            g2.drawString(etiqueta, desplazamiento + x, desplazamiento + y);
        } finally {
            g2.dispose();
        }
    }
}
