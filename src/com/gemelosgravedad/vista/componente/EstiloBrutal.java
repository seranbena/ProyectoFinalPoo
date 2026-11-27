package com.gemelosgravedad.vista.componente;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;

/**
 * Paleta, tipografia y trazos del estilo brutalista.
 *
 * <p>Sin degradados, sin esquinas redondeadas y con bordes negros gruesos. Al
 * centralizar colores y fuentes, las pantallas comparten una identidad visual
 * coherente.</p>
 */
public final class EstiloBrutal {

    /** Negro base de bordes, sombras y fondos. */
    public static final Color NEGRO = new Color(18, 18, 18);

    /** Carbon oscuro usado como fondo de la partida. */
    public static final Color CARBON = new Color(21, 21, 21);

    /** Variante clara del carbon para bloques apagados. */
    public static final Color CARBON_CLARO = new Color(36, 36, 36);

    /** Papel claro usado en tarjetas y paneles. */
    public static final Color PAPEL = new Color(232, 227, 214);

    /** Blanco calido para textos sobre fondos oscuros. */
    public static final Color BLANCO = new Color(250, 248, 242);

    /** Rojo de peligro y acentos principales. */
    public static final Color ROJO = new Color(223, 46, 38);

    /** Azul del gemelo 01 y de los elementos de navegacion. */
    public static final Color AZUL = new Color(28, 82, 255);

    /** Amarillo de avisos y del gemelo 02. */
    public static final Color AMARILLO = new Color(255, 211, 0);

    /** Verde de enemigos voladores y estados correctos. */
    public static final Color VERDE = new Color(0, 168, 107);

    /** Cian del escudo y de los cristales. */
    public static final Color CIAN = new Color(0, 209, 209);

    /** Naranja del turbo y de los avisos secundarios. */
    public static final Color NARANJA = new Color(255, 116, 20);

    /** Morado del orbe de cambio. */
    public static final Color MORADO = new Color(132, 74, 220);

    /** Gris medio para textos secundarios. */
    public static final Color GRIS = new Color(111, 108, 100);

    /** Gris claro para textos sobre negro. */
    public static final Color GRIS_CLARO = new Color(176, 170, 158);

    private static final String FUENTE_TITULO = "Arial Black";
    private static final String FUENTE_MONO = "Consolas";

    private EstiloBrutal() {
    }

    /**
     * Devuelve la fuente de titulares.
     *
     * @param tamano tamano en puntos
     * @return fuente negrita de titulo
     */
    public static Font titulo(int tamano) {
        return new Font(FUENTE_TITULO, Font.BOLD, tamano);
    }

    /**
     * Devuelve la fuente monoespaciada en negrita.
     *
     * @param tamano tamano en puntos
     * @return fuente monoespaciada negrita
     */
    public static Font mono(int tamano) {
        return new Font(FUENTE_MONO, Font.BOLD, tamano);
    }

    /**
     * Devuelve la fuente monoespaciada normal.
     *
     * @param tamano tamano en puntos
     * @return fuente monoespaciada sin negrita
     */
    public static Font monoNormal(int tamano) {
        return new Font(FUENTE_MONO, Font.PLAIN, tamano);
    }

    /**
     * Dibuja un panel relleno con borde negro grueso y sombra desplazada.
     *
     * @param g contexto grafico
     * @param x coordenada horizontal del borde izquierdo
     * @param y coordenada vertical del borde superior
     * @param ancho ancho del panel
     * @param alto alto del panel
     * @param fondo color de relleno
     * @param salto desplazamiento de la sombra
     */
    public static void panel(Graphics2D g, int x, int y, int ancho, int alto,
                             Color fondo, int salto) {
        g.setColor(NEGRO);
        g.fillRect(x + salto, y + salto, ancho, alto);
        g.setColor(fondo);
        g.fillRect(x, y, ancho, alto);
        g.setColor(NEGRO);
        g.setStroke(new BasicStroke(4f));
        g.drawRect(x + 2, y + 2, ancho - 4, alto - 4);
    }

    /**
     * Dibuja una franja industrial de peligro con diagonales.
     *
     * @param g contexto grafico
     * @param x coordenada horizontal del borde izquierdo
     * @param y coordenada vertical del borde superior
     * @param ancho ancho de la franja
     * @param alto alto de la franja
     * @param color color de las diagonales
     */
    public static void franjas(Graphics2D g, int x, int y, int ancho, int alto,
                               Color color) {
        Shape recorte = g.getClip();
        g.clipRect(x, y, ancho, alto);
        g.setColor(NEGRO);
        g.fillRect(x, y, ancho, alto);
        g.setColor(color);
        g.setStroke(new BasicStroke(Math.max(4f, alto / 2f)));
        for (int i = -alto; i < ancho + alto; i += 26) {
            g.drawLine(x + i, y + alto, x + i + alto, y);
        }
        g.setClip(recorte);
        g.setColor(NEGRO);
        g.setStroke(new BasicStroke(2f));
        g.drawRect(x, y, ancho - 1, alto - 1);
    }

    /**
     * Dibuja texto con una sombra solida del desplazamiento indicado.
     *
     * @param g contexto grafico
     * @param texto texto que se quiere dibujar
     * @param x coordenada horizontal del texto
     * @param y linea base del texto
     * @param color color principal
     * @param sombra color de la sombra
     * @param salto desplazamiento de la sombra
     */
    public static void textoDuro(Graphics2D g, String texto, int x, int y,
                                 Color color, Color sombra, int salto) {
        g.setColor(sombra);
        g.drawString(texto, x + salto, y + salto);
        g.setColor(color);
        g.drawString(texto, x, y);
    }

    /**
     * Dibuja una cruz de referencia para la rejilla industrial.
     *
     * @param g contexto grafico
     * @param x centro horizontal de la cruz
     * @param y centro vertical de la cruz
     * @param tam longitud de los brazos
     * @param color color de la cruz
     */
    public static void cruz(Graphics2D g, int x, int y, int tam, Color color) {
        g.setColor(color);
        g.fillRect(x - tam / 2, y, tam, 2);
        g.fillRect(x, y - tam / 2, 2, tam);
    }

    /**
     * Devuelve negro o blanco segun el contraste del fondo.
     *
     * @param fondo color de fondo
     * @return color de texto con mayor contraste
     */
    public static Color textoSobre(Color fondo) {
        int brillo = (fondo.getRed() * 299 + fondo.getGreen() * 587
                + fondo.getBlue() * 114) / 1000;
        return brillo > 145 ? NEGRO : BLANCO;
    }
}
