package com.gemelosgravedad.vista.pantalla;

import com.gemelosgravedad.vista.componente.BotonEstilo;
import com.gemelosgravedad.vista.componente.EstiloBrutal;
import com.gemelosgravedad.vista.componente.PanelTarjeta;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.io.Serial;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Pantalla donde se elige entre un jugador y dos jugadores cooperativos.
 *
 * <p>El modo de dos jugadores reparte los gemelos: el jugador 1 controla al
 * azul del piso y el jugador 2 al amarillo del techo. Las vidas, el puntaje y
 * los power-ups siguen siendo del equipo.</p>
 */
public final class PantallaModo extends PantallaBase {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final int ANCHO_TARJETA = 760;
    private static final int ALTO_TARJETA = 430;
    private static final int ANCHO_BOTON = 320;
    private static final int ALTO_BOTON = 56;

    /** Boton que inicia una partida de un jugador. */
    private final BotonEstilo botonUnJugador;

    /** Boton que inicia una partida cooperativa. */
    private final BotonEstilo botonDosJugadores;

    /** Boton que vuelve al menu principal. */
    private final BotonEstilo botonVolver;

    /**
     * Crea la pantalla de seleccion de modo y sus tres acciones.
     */
    public PantallaModo() {
        super();
        botonUnJugador = new BotonEstilo("1 JUGADOR", EstiloBrutal.AMARILLO,
                ANCHO_BOTON, ALTO_BOTON);
        botonDosJugadores = new BotonEstilo("2 JUGADORES", EstiloBrutal.CIAN,
                ANCHO_BOTON, ALTO_BOTON);
        botonVolver = new BotonEstilo("VOLVER", EstiloBrutal.AZUL,
                ANCHO_BOTON, ALTO_BOTON);
        montar("", "MODO DE JUEGO", "", crearCentro(), "",
                "VIDAS, PUNTOS Y POWER-UPS COMPARTIDOS");
    }

    /**
     * Crea el contenido central con la tarjeta de modos.
     *
     * @return contenedor central
     */
    private JPanel crearCentro() {
        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);
        centro.setBorder(BorderFactory.createEmptyBorder(24, 60, 24, 60));

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1;
        c.weighty = 1;
        centro.add(crearTarjeta(), c);
        return centro;
    }

    /**
     * Crea la tarjeta con la explicacion de cada modo y los botones.
     *
     * @return tarjeta de seleccion
     */
    private JPanel crearTarjeta() {
        PanelTarjeta tarjeta = new PanelTarjeta(EstiloBrutal.BLANCO,
                EstiloBrutal.AMARILLO);
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(0, 0, 9, 9));
        tarjeta.setPreferredSize(new Dimension(ANCHO_TARJETA, ALTO_TARJETA));

        JLabel cabecera = new JLabel("ELIGE CÓMO JUGAR");
        cabecera.setOpaque(true);
        cabecera.setBackground(EstiloBrutal.NEGRO);
        cabecera.setForeground(EstiloBrutal.AMARILLO);
        cabecera.setFont(EstiloBrutal.titulo(13));
        cabecera.setBorder(BorderFactory.createEmptyBorder(7, 18, 7, 18));
        cabecera.setAlignmentX(Component.LEFT_ALIGNMENT);
        cabecera.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        tarjeta.add(cabecera);

        JPanel cuerpo = new JPanel();
        cuerpo.setOpaque(false);
        cuerpo.setLayout(new BoxLayout(cuerpo, BoxLayout.Y_AXIS));
        cuerpo.setBorder(BorderFactory.createEmptyBorder(20, 34, 24, 40));
        cuerpo.setAlignmentX(Component.LEFT_ALIGNMENT);

        cuerpo.add(crearFila("01 // 1 JUGADOR",
                "CONTROLAS A LOS DOS GEMELOS CON LAS MISMAS TECLAS.",
                EstiloBrutal.AZUL));
        cuerpo.add(Box.createVerticalStrut(12));
        cuerpo.add(crearFila("02 // 2 JUGADORES",
                "P1: GEMELO AZUL (PISO) // P2: GEMELO AMARILLO (TECHO).",
                EstiloBrutal.CIAN));
        cuerpo.add(Box.createVerticalStrut(12));
        cuerpo.add(crearFila("03 // COOPERATIVO",
                "LA SOGA LOS MANTIENE JUNTOS Y EL RESULTADO ES DEL EQUIPO.",
                EstiloBrutal.AMARILLO));
        cuerpo.add(Box.createVerticalStrut(20));

        JPanel filaModos = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        filaModos.setOpaque(false);
        filaModos.setAlignmentX(Component.LEFT_ALIGNMENT);
        filaModos.add(botonUnJugador);
        filaModos.add(botonDosJugadores);
        cuerpo.add(filaModos);
        cuerpo.add(Box.createVerticalStrut(12));

        JPanel filaVolver = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        filaVolver.setOpaque(false);
        filaVolver.setAlignmentX(Component.LEFT_ALIGNMENT);
        filaVolver.add(botonVolver);
        cuerpo.add(filaVolver);

        tarjeta.add(cuerpo);
        return tarjeta;
    }

    /**
     * Crea una fila con un rotulo y una explicacion breve.
     *
     * @param titulo texto del rotulo
     * @param detalle explicacion del modo
     * @param acento color del rotulo
     * @return panel de la fila
     */
    private JPanel crearFila(String titulo, String detalle, Color acento) {
        JPanel fila = new JPanel();
        fila.setOpaque(false);
        fila.setLayout(new BoxLayout(fila, BoxLayout.Y_AXIS));
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        JLabel rotulo = new JLabel(titulo);
        rotulo.setOpaque(true);
        rotulo.setBackground(EstiloBrutal.NEGRO);
        rotulo.setForeground(acento);
        rotulo.setFont(EstiloBrutal.titulo(11));
        rotulo.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        rotulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        fila.add(rotulo);
        fila.add(Box.createVerticalStrut(3));

        JLabel texto = new JLabel(detalle);
        texto.setFont(EstiloBrutal.monoNormal(11));
        texto.setForeground(EstiloBrutal.NEGRO);
        texto.setAlignmentX(Component.LEFT_ALIGNMENT);
        fila.add(texto);
        return fila;
    }

    /**
     * Registra el oyente del boton de un jugador.
     *
     * @param oyente accion que se ejecuta al pulsar
     */
    public void alUnJugador(ActionListener oyente) {
        botonUnJugador.addActionListener(oyente);
    }

    /**
     * Registra el oyente del boton de dos jugadores.
     *
     * @param oyente accion que se ejecuta al pulsar
     */
    public void alDosJugadores(ActionListener oyente) {
        botonDosJugadores.addActionListener(oyente);
    }

    /**
     * Registra el oyente del boton Volver.
     *
     * @param oyente accion que se ejecuta al pulsar
     */
    public void alVolver(ActionListener oyente) {
        botonVolver.addActionListener(oyente);
    }
}
