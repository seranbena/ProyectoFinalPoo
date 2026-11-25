package com.gemelosgravedad.vista.pantalla;

import com.gemelosgravedad.modelo.Marcador;
import com.gemelosgravedad.vista.componente.BarraBrutal;
import com.gemelosgravedad.vista.componente.BotonEstilo;
import com.gemelosgravedad.vista.componente.EstiloBrutal;
import com.gemelosgravedad.vista.componente.TextoDuro;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.Serial;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Pantalla de bienvenida: cartel negro y menu de cuatro acciones.
 *
 * <p>El cartel resume la propuesta del juego y el menu da acceso a la partida,
 * las instrucciones, los records y la salida.</p>
 */
public final class PantallaBienvenida extends PantallaBase {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final String[] DETALLES = {
        "EL PISO ESTÁ ROTO: BUSCA LAS PLATAFORMAS.",
        "SI CAES AL VACÍO, PIERDES UNA VIDA.",
        "LA SOGA UNE A LOS DOS GEMELOS.",
        "EL ORBE MORADO INTERCAMBIA SUS PUESTOS."
    };
    private static final int ANCHO_POSTER = 500;
    private static final int ALTO_POSTER = 404;
    private static final int ANCHO_MENU = 348;
    private static final int ANCHO_BOTON = 340;
    private static final int ALTO_BOTON = 62;

    /** Boton que inicia el flujo de nueva partida. */
    private final BotonEstilo botonJugar;

    /** Boton que abre el manual de operaciones. */
    private final BotonEstilo botonInstrucciones;

    /** Boton que abre la tabla de records. */
    private final BotonEstilo botonTop3;

    /** Boton que cierra la aplicacion. */
    private final BotonEstilo botonSalir;

    /**
     * Crea la pantalla de bienvenida y sus cuatro acciones.
     */
    public PantallaBienvenida() {
        super();
        botonJugar = new BotonEstilo("JUGAR", EstiloBrutal.AMARILLO,
                ANCHO_BOTON, ALTO_BOTON);
        botonInstrucciones = new BotonEstilo("INSTRUCCIONES", EstiloBrutal.CIAN,
                ANCHO_BOTON, ALTO_BOTON);
        botonTop3 = new BotonEstilo("TOP 3", EstiloBrutal.BLANCO,
                ANCHO_BOTON, ALTO_BOTON);
        botonSalir = new BotonEstilo("SALIR", EstiloBrutal.ROJO,
                ANCHO_BOTON, ALTO_BOTON);
        montar("GD—02 / SISTEMA DE GRAVEDAD", "ARCADE // MODO LOCAL",
                "POO / JAVA 21", crearCentro(),
                "← → MOVER   //   ESPACIO SALTAR", "P PAUSA   //   ESC MENÚ");
    }

    /**
     * Crea el cartel y el menu.
     *
     * @return contenedor central
     */
    private JPanel crearCentro() {
        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);
        centro.setBorder(BorderFactory.createEmptyBorder(22, 40, 24, 40));

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(0, 0, 0, 28);
        centro.add(crearPoster(), c);

        c.gridx = 1;
        c.weightx = 0;
        c.insets = new Insets(0, 0, 0, 0);
        centro.add(crearMenu(), c);
        return centro;
    }

    /**
     * Crea el cartel con el titulo y las reglas resumidas.
     *
     * @return cartel de bienvenida
     */
    private JPanel crearPoster() {
        JPanel poster = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                try {
                    int ancho = getWidth() - 10;
                    int alto = getHeight() - 10;
                    g2.setColor(EstiloBrutal.AMARILLO);
                    g2.fillRect(10, 10, ancho, alto);
                    g2.setColor(EstiloBrutal.NEGRO);
                    g2.fillRect(0, 0, ancho, alto);
                    g2.setColor(EstiloBrutal.AMARILLO);
                    g2.fillRect(ancho - 8, 0, 8, alto);
                    g2.setColor(EstiloBrutal.NEGRO);
                    g2.setStroke(new java.awt.BasicStroke(5f));
                    g2.drawRect(2, 2, ancho - 4, alto - 4);
                } finally {
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        poster.setOpaque(false);
        poster.setLayout(new BoxLayout(poster, BoxLayout.Y_AXIS));
        poster.setBorder(BorderFactory.createEmptyBorder(22, 30, 24, 30));
        poster.setPreferredSize(new Dimension(ANCHO_POSTER, ALTO_POSTER));
        poster.setMinimumSize(new Dimension(ANCHO_POSTER, ALTO_POSTER));

        JPanel sellos = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        sellos.setOpaque(false);
        sellos.setAlignmentX(Component.LEFT_ALIGNMENT);
        sellos.add(BarraBrutal.chip("GD—02", EstiloBrutal.AMARILLO, EstiloBrutal.NEGRO));
        sellos.add(BarraBrutal.chip("UNIDAD DE ENTRENAMIENTO",
                EstiloBrutal.PAPEL, EstiloBrutal.NEGRO));
        JPanel filaSellos = new JPanel(new BorderLayout());
        filaSellos.setOpaque(false);
        filaSellos.setAlignmentX(Component.LEFT_ALIGNMENT);
        filaSellos.add(sellos, BorderLayout.WEST);
        filaSellos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        poster.add(filaSellos);
        poster.add(Box.createVerticalStrut(16));

        TextoDuro titulo = new TextoDuro("GEMELOS", EstiloBrutal.titulo(58),
                EstiloBrutal.PAPEL, EstiloBrutal.AZUL, 6);
        titulo.setAlineacion(SwingConstants.LEFT);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        poster.add(titulo);

        TextoDuro subtitulo = new TextoDuro("DE GRAVEDAD", EstiloBrutal.titulo(42),
                EstiloBrutal.AMARILLO, EstiloBrutal.ROJO, 5);
        subtitulo.setAlineacion(SwingConstants.LEFT);
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        poster.add(subtitulo);
        poster.add(Box.createVerticalStrut(12));

        JPanel linea = new JPanel();
        linea.setBackground(EstiloBrutal.PAPEL);
        linea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
        linea.setPreferredSize(new Dimension(400, 5));
        linea.setAlignmentX(Component.LEFT_ALIGNMENT);
        poster.add(linea);
        poster.add(Box.createVerticalStrut(14));

        JLabel lema = new JLabel("DOS GEMELOS. UNA SOGA. UNA GRAVEDAD.");
        lema.setFont(EstiloBrutal.mono(15));
        lema.setForeground(EstiloBrutal.PAPEL);
        lema.setAlignmentX(Component.LEFT_ALIGNMENT);
        lema.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        poster.add(lema);
        poster.add(Box.createVerticalStrut(8));

        for (String detalle : DETALLES) {
            poster.add(crearDetalle(detalle));
        }
        poster.add(Box.createVerticalGlue());

        JPanel leyenda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leyenda.setOpaque(false);
        leyenda.setAlignmentX(Component.LEFT_ALIGNMENT);
        leyenda.add(crearCuadrado(EstiloBrutal.AZUL));
        leyenda.add(crearMini("GEMELO 01"));
        leyenda.add(crearCuadrado(EstiloBrutal.AMARILLO));
        leyenda.add(crearMini("GEMELO 02"));
        poster.add(leyenda);
        return poster;
    }

    /**
     * Crea una linea de detalle del cartel.
     *
     * @param texto contenido de la linea
     * @return etiqueta configurada
     */
    private JLabel crearDetalle(String texto) {
        JLabel detalle = new JLabel(texto);
        detalle.setFont(EstiloBrutal.monoNormal(12));
        detalle.setForeground(EstiloBrutal.GRIS_CLARO);
        detalle.setAlignmentX(Component.LEFT_ALIGNMENT);
        detalle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 19));
        return detalle;
    }

    /**
     * Crea un cuadrado de color para la leyenda.
     *
     * @param color color del cuadrado
     * @return panel cuadrado
     */
    private JPanel crearCuadrado(Color color) {
        JPanel cuadrado = new JPanel();
        cuadrado.setBackground(color);
        cuadrado.setPreferredSize(new Dimension(13, 13));
        cuadrado.setMinimumSize(new Dimension(13, 13));
        cuadrado.setMaximumSize(new Dimension(13, 13));
        cuadrado.setBorder(BorderFactory.createLineBorder(EstiloBrutal.PAPEL, 2));
        return cuadrado;
    }

    /**
     * Crea una etiqueta pequena para la leyenda.
     *
     * @param texto contenido de la etiqueta
     * @return etiqueta configurada
     */
    private JLabel crearMini(String texto) {
        JLabel mini = new JLabel(texto);
        mini.setFont(EstiloBrutal.mono(10));
        mini.setForeground(EstiloBrutal.GRIS_CLARO);
        return mini;
    }

    /**
     * Crea el menu de acciones.
     *
     * @return panel del menu
     */
    private JPanel crearMenu() {
        JPanel menu = new JPanel();
        menu.setOpaque(false);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setPreferredSize(new Dimension(ANCHO_MENU, ALTO_POSTER));

        JLabel rotulo = BarraBrutal.chip("SELECCIONA UNA OPCIÓN",
                EstiloBrutal.NEGRO, EstiloBrutal.PAPEL);
        rotulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        menu.add(rotulo);
        menu.add(Box.createVerticalStrut(18));

        menu.add(botonJugar);
        menu.add(Box.createVerticalStrut(14));
        menu.add(botonInstrucciones);
        menu.add(Box.createVerticalStrut(14));
        menu.add(botonTop3);
        menu.add(Box.createVerticalStrut(14));
        menu.add(botonSalir);
        menu.add(Box.createVerticalStrut(20));

        JLabel nota = new JLabel(Marcador.VIDAS_INICIALES
                + " VIDAS // DIFICULTAD PROGRESIVA", SwingConstants.CENTER);
        nota.setFont(EstiloBrutal.mono(10));
        nota.setForeground(EstiloBrutal.GRIS);
        nota.setAlignmentX(Component.CENTER_ALIGNMENT);
        nota.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));
        menu.add(nota);
        menu.add(Box.createVerticalGlue());
        return menu;
    }

    /**
     * Registra el oyente del boton Jugar.
     *
     * @param oyente accion que se ejecuta al pulsar
     */
    public void alJugar(ActionListener oyente) {
        botonJugar.addActionListener(oyente);
    }

    /**
     * Registra el oyente del boton Instrucciones.
     *
     * @param oyente accion que se ejecuta al pulsar
     */
    public void alInstrucciones(ActionListener oyente) {
        botonInstrucciones.addActionListener(oyente);
    }

    /**
     * Registra el oyente del boton Top 3.
     *
     * @param oyente accion que se ejecuta al pulsar
     */
    public void alTop3(ActionListener oyente) {
        botonTop3.addActionListener(oyente);
    }

    /**
     * Registra el oyente del boton Salir.
     *
     * @param oyente accion que se ejecuta al pulsar
     */
    public void alSalir(ActionListener oyente) {
        botonSalir.addActionListener(oyente);
    }
}
