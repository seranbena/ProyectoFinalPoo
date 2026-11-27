package com.gemelosgravedad.vista.componente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Barras negras de cabecera y pie compartidas por las pantallas.
 *
 * <p>Tambien construye los chips de rotulo y la zona de acciones que aparece
 * bajo las tablas. Al reutilizar estas piezas, las pantallas solo declaran su
 * contenido central.</p>
 */
public final class BarraBrutal {

    private static final int MARGEN_HORIZONTAL = 22;
    private static final int SEPARACION = 16;

    private BarraBrutal() {
    }

    /**
     * Crea una etiqueta con el texto en mayusculas.
     *
     * @param texto texto de la etiqueta
     * @param fuente fuente que se aplica
     * @param color color del texto
     * @return etiqueta configurada
     */
    private static JLabel etiqueta(String texto, Font fuente, Color color) {
        JLabel etiqueta = new JLabel(texto == null ? "" : texto.toUpperCase());
        etiqueta.setFont(fuente);
        etiqueta.setForeground(color);
        return etiqueta;
    }

    /**
     * Crea la cabecera negra con metadato izquierdo, titulo central y metadato
     * derecho.
     *
     * @param izquierda texto del extremo izquierdo
     * @param centro titulo central
     * @param derecha texto del extremo derecho
     * @return panel de cabecera
     */
    public static JPanel banda(String izquierda, String centro, String derecha) {
        JPanel barra = new JPanel(new BorderLayout(SEPARACION, 0));
        barra.setBackground(EstiloBrutal.NEGRO);
        barra.setBorder(BorderFactory.createEmptyBorder(8, MARGEN_HORIZONTAL,
                8, MARGEN_HORIZONTAL));

        barra.add(etiqueta(izquierda, EstiloBrutal.mono(11), EstiloBrutal.GRIS_CLARO),
                BorderLayout.WEST);

        JLabel titulo = etiqueta(centro, EstiloBrutal.titulo(21), EstiloBrutal.AMARILLO);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        barra.add(titulo, BorderLayout.CENTER);

        JLabel derechaEtiqueta = etiqueta(derecha, EstiloBrutal.mono(11),
                EstiloBrutal.GRIS_CLARO);
        derechaEtiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
        barra.add(derechaEtiqueta, BorderLayout.EAST);
        return barra;
    }

    /**
     * Crea el pie con una franja de peligro sobre una barra negra.
     *
     * @param izquierda leyenda izquierda
     * @param derecha leyenda derecha
     * @return panel de pie
     */
    public static JPanel pie(String izquierda, String derecha) {
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setOpaque(false);
        contenedor.add(new FranjaPeligro(EstiloBrutal.AMARILLO, 10),
                BorderLayout.NORTH);

        JPanel barra = new JPanel(new GridLayout(1, 2, SEPARACION, 0));
        barra.setBackground(EstiloBrutal.NEGRO);
        barra.setBorder(BorderFactory.createEmptyBorder(9, MARGEN_HORIZONTAL,
                9, MARGEN_HORIZONTAL));

        barra.add(etiqueta(izquierda, EstiloBrutal.mono(11), EstiloBrutal.PAPEL));

        JLabel derechaEtiqueta = etiqueta(derecha, EstiloBrutal.mono(11),
                EstiloBrutal.GRIS_CLARO);
        derechaEtiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
        barra.add(derechaEtiqueta);

        contenedor.add(barra, BorderLayout.CENTER);
        return contenedor;
    }

    /**
     * Crea un chip negro con texto corto.
     *
     * @param texto texto del chip
     * @param fondo color de fondo
     * @param frente color del texto
     * @return etiqueta configurada como chip
     */
    public static JLabel chip(String texto, Color fondo, Color frente) {
        JLabel chip = new JLabel(texto == null ? "" : texto.toUpperCase());
        chip.setOpaque(true);
        chip.setBackground(fondo);
        chip.setForeground(frente);
        chip.setFont(EstiloBrutal.mono(11));
        chip.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        return chip;
    }

    /**
     * Crea una zona de acciones con los botones alineados a la derecha.
     *
     * @param botones botones que se colocan en el extremo derecho
     * @return panel de acciones
     */
    public static JPanel acciones(JComponent... botones) {
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        acciones.setOpaque(false);
        acciones.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));
        for (JComponent boton : botones) {
            acciones.add(boton);
        }
        return acciones;
    }
}
