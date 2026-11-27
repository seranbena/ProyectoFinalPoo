package com.gemelosgravedad.vista.componente;

import com.gemelosgravedad.recursos.GestorRecursos;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.Serial;
import javax.swing.JPanel;

/**
 * Fondo de papel con rejilla tecnica, marco negro y barra roja lateral.
 *
 * <p>Es solo decoracion: no contiene reglas del juego. Las pantallas de menu lo
 * usan como base y agregan sus propios componentes encima. Si se indica una
 * imagen y el archivo existe, se dibuja en lugar del papel procedural.</p>
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

    /** Imagen opcional de fondo; {@code null} si no se configuro o no existe. */
    private final Image imagenFondo;

    /**
     * Crea un fondo que usa una imagen si esta disponible.
     *
     * @param rutaImagen ruta relativa dentro de {@code resources}, o
     *                   {@code null} para usar solo el fondo procedural
     */
    public PanelFondo(String rutaImagen) {
        imagenFondo = rutaImagen == null
                ? null
                : GestorRecursos.cargarImagen(rutaImagen);
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

            if (imagenFondo != null) {
                dibujarImagenCubriendo(g2, ancho, alto);
            } else {
                dibujarPapel(g2, ancho, alto);
            }
            dibujarMarco(g2, ancho, alto);
        } finally {
            g2.dispose();
        }
    }

    /**
     * Dibuja la imagen escalada para cubrir todo el panel sin deformarla.
     *
     * @param g2 contexto grafico
     * @param ancho ancho del panel
     * @param alto alto del panel
     */
    private void dibujarImagenCubriendo(Graphics2D g2, int ancho, int alto) {
        int anchoImagen = imagenFondo.getWidth(null);
        int altoImagen = imagenFondo.getHeight(null);
        if (anchoImagen <= 0 || altoImagen <= 0) {
            dibujarPapel(g2, ancho, alto);
            return;
        }
        double escala = Math.max((double) ancho / anchoImagen,
                (double) alto / altoImagen);
        int anchoDibujo = (int) Math.ceil(anchoImagen * escala);
        int altoDibujo = (int) Math.ceil(altoImagen * escala);
        int x = (ancho - anchoDibujo) / 2;
        int y = (alto - altoDibujo) / 2;
        g2.drawImage(imagenFondo, x, y, anchoDibujo, altoDibujo, null);
    }

    /**
     * Dibuja el fondo procedural de papel y rejilla.
     *
     * @param g2 contexto grafico
     * @param ancho ancho del panel
     * @param alto alto del panel
     */
    private void dibujarPapel(Graphics2D g2, int ancho, int alto) {
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
    }

    /**
     * Dibuja la barra lateral roja, el marco negro y las marcas de esquina.
     *
     * @param g2 contexto grafico
     * @param ancho ancho del panel
     * @param alto alto del panel
     */
    private void dibujarMarco(Graphics2D g2, int ancho, int alto) {
        g2.setColor(EstiloBrutal.ROJO);
        g2.fillRect(0, 0, BARRA_LATERAL, alto);

        g2.setColor(EstiloBrutal.NEGRO);
        g2.setStroke(new BasicStroke(MARCO));
        g2.drawRect(MARCO / 2, MARCO / 2, ancho - MARCO, alto - MARCO);

        g2.fillRect(MARGEN_MARCA, MARGEN_MARCA, MARCA_ESQUINA, MARCA_ESQUINA);
        g2.fillRect(ancho - MARGEN_MARCA - MARCA_ESQUINA,
                alto - MARGEN_MARCA - MARCA_ESQUINA, MARCA_ESQUINA, MARCA_ESQUINA);
    }
}
