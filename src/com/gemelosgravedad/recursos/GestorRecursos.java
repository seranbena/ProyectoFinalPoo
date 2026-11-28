package com.gemelosgravedad.recursos;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Carga las imagenes de {@code resources}.
 *
 * <p>Si un sprite falta o no se puede leer, se devuelve {@code null} y las
 * entidades dibujan su figura geometrica de respaldo. El juego nunca se detiene
 * por un recurso ausente.</p>
 *
 * @see LocalizadorRecursos
 */
public final class GestorRecursos {

    private static final Logger REGISTRO = Logger.getLogger(GestorRecursos.class.getName());

    private GestorRecursos() {
    }

    /**
     * Carga una imagen individual.
     *
     * @param ruta ruta relativa dentro de {@code resources}
     * @return imagen cargada o {@code null} si no esta disponible
     */
    public static Image cargarImagen(String ruta) {
        URL url = LocalizadorRecursos.localizar(ruta);
        if (url == null) {
            return null;
        }
        try {
            return ImageIO.read(url);
        } catch (IOException e) {
            REGISTRO.log(Level.WARNING, "No se pudo cargar la imagen: " + ruta, e);
            return null;
        }
    }

    /**
     * Carga una animacion formada por {@code prefijo_1.png}, {@code prefijo_2.png}
     * y asi sucesivamente.
     *
     * @param prefijo ruta base de la animacion
     * @param cantidad numero de cuadros que se quieren cargar
     * @return arreglo con los cuadros; las posiciones no disponibles quedan nulas
     */
    public static Image[] cargarCuadros(String prefijo, int cantidad) {
        Image[] cuadros = new Image[cantidad];
        for (int i = 0; i < cantidad; i++) {
            cuadros[i] = cargarImagen(prefijo + "_" + (i + 1) + ".png");
        }
        return cuadros;
    }

    /**
     * Copia los sprites pintandolos de otro color sin perder la transparencia.
     *
     * @param originales sprites de origen; puede ser {@code null}
     * @param color color que se aplica sobre la silueta
     * @param alfa opacidad del tinte entre cero y uno
     * @return nuevos sprites tintados o {@code null} si el origen es nulo
     */
    public static Image[] tintarCuadros(Image[] originales, Color color, float alfa) {
        if (originales == null) {
            return null;
        }
        Image[] tintados = new Image[originales.length];
        for (int i = 0; i < originales.length; i++) {
            Image original = originales[i];
            if (original == null) {
                continue;
            }
            int ancho = original.getWidth(null);
            int alto = original.getHeight(null);
            if (ancho <= 0 || alto <= 0) {
                continue;
            }
            BufferedImage copia = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = copia.createGraphics();
            try {
                g.drawImage(original, 0, 0, null);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alfa));
                g.setColor(color);
                g.fillRect(0, 0, ancho, alto);
            } finally {
                g.dispose();
            }
            tintados[i] = copia;
        }
        return tintados;
    }
}
