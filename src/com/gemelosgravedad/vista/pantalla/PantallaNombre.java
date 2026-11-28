package com.gemelosgravedad.vista.pantalla;

import com.gemelosgravedad.modelo.datos.Puntaje;
import com.gemelosgravedad.vista.componente.BotonEstilo;
import com.gemelosgravedad.vista.componente.EstiloBrutal;
import com.gemelosgravedad.vista.componente.PanelTarjeta;
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
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

/**
 * Pide el nombre del jugador o de los dos jugadores antes de comenzar.
 *
 * <p>La validacion definitiva la hace el controlador de navegacion; esta
 * pantalla solo recoge los textos y muestra el mensaje de error que recibe.
 * En el modo cooperativo se muestran dos campos y el resultado se guarda como
 * un unico nombre de equipo.</p>
 */
public final class PantallaNombre extends PantallaBase {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final int ALTO_PANEL = 350;
    private static final int ALTO_PANEL_2P = 450;
    private static final int ANCHO_FORMULARIO = 552;
    private static final int ANCHO_CAMPO = 480;
    private static final int ALTO_CAMPO = 58;
    private static final int ANCHO_BOTON = 226;
    private static final int ALTO_BOTON = 50;

    /** Campo donde se escribe el nombre del jugador 1. */
    private final JTextField campoNombre;

    /** Campo donde se escribe el nombre del jugador 2. */
    private final JTextField campoNombre2;

    /** Etiqueta que muestra los errores de validacion. */
    private final JLabel etiquetaError;

    /** Boton que comienza la partida. */
    private final BotonEstilo botonJugar;

    /** Boton que vuelve al menu principal. */
    private final BotonEstilo botonVolver;

    /** Bloque del segundo jugador, visible solo en el modo cooperativo. */
    private JPanel bloqueJugador2;

    /** Cabecera de la tarjeta, que cambia entre jugador y jugadores. */
    private JLabel cabeceraFormulario;

    /** Pista de longitud del primer nombre. */
    private JLabel pista;

    /** Tarjeta del formulario, cuyo alto cambia segun el modo. */
    private PanelTarjeta formulario;

    /** Indica si se estan pidiendo dos nombres. */
    private boolean dosJugadores;

    /**
     * Crea la pantalla de identificacion y su formulario.
     */
    public PantallaNombre() {
        super();
        campoNombre = new JTextField();
        campoNombre2 = new JTextField();
        etiquetaError = new JLabel(" ");
        botonJugar = new BotonEstilo("COMENZAR", EstiloBrutal.AMARILLO,
                ANCHO_BOTON, ALTO_BOTON);
        botonVolver = new BotonEstilo("VOLVER", EstiloBrutal.AZUL,
                ANCHO_BOTON, ALTO_BOTON);
        montar("", "IDENTIFICACIÓN", "", crearCentro(),
                "EL RESULTADO SE GUARDA EN EL TOP 3", "");
        configurarModo(false);
    }

    /**
     * Crea el contenido central con el formulario.
     *
     * @return contenedor central
     */
    private JPanel crearCentro() {
        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);
        centro.setBorder(BorderFactory.createEmptyBorder(32, 62, 32, 62));

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1;
        c.weighty = 1;
        centro.add(crearFormulario(), c);
        return centro;
    }

    /**
     * Crea el formulario con los campos de nombre y los botones.
     *
     * @return tarjeta del formulario
     */
    private JPanel crearFormulario() {
        formulario = new PanelTarjeta(EstiloBrutal.BLANCO,
                EstiloBrutal.AMARILLO);
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setBorder(BorderFactory.createEmptyBorder(0, 0, 9, 9));
        formulario.setPreferredSize(new Dimension(ANCHO_FORMULARIO, ALTO_PANEL));

        cabeceraFormulario = new JLabel("JUGADOR");
        cabeceraFormulario.setOpaque(true);
        cabeceraFormulario.setBackground(EstiloBrutal.NEGRO);
        cabeceraFormulario.setForeground(EstiloBrutal.AMARILLO);
        cabeceraFormulario.setFont(EstiloBrutal.titulo(13));
        cabeceraFormulario.setBorder(BorderFactory.createEmptyBorder(7, 18, 7, 18));
        cabeceraFormulario.setAlignmentX(Component.LEFT_ALIGNMENT);
        cabeceraFormulario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        formulario.add(cabeceraFormulario);

        JPanel cuerpo = new JPanel();
        cuerpo.setOpaque(false);
        cuerpo.setLayout(new BoxLayout(cuerpo, BoxLayout.Y_AXIS));
        cuerpo.setBorder(BorderFactory.createEmptyBorder(16, 28, 16, 30));
        cuerpo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel bloqueJugador1 = new JPanel();
        bloqueJugador1.setOpaque(false);
        bloqueJugador1.setLayout(new BoxLayout(bloqueJugador1, BoxLayout.Y_AXIS));
        bloqueJugador1.setAlignmentX(Component.LEFT_ALIGNMENT);
        bloqueJugador1.add(crearEtiqueta("NOMBRE"));
        bloqueJugador1.add(Box.createVerticalStrut(5));
        configurarCampo(campoNombre);
        bloqueJugador1.add(campoNombre);
        bloqueJugador1.add(Box.createVerticalStrut(4));
        pista = new JLabel("1 A " + Puntaje.NOMBRE_MAXIMO + " CARACTERES.");
        configurarPista(pista);
        bloqueJugador1.add(pista);
        cuerpo.add(bloqueJugador1);

        bloqueJugador2 = new JPanel();
        bloqueJugador2.setOpaque(false);
        bloqueJugador2.setLayout(new BoxLayout(bloqueJugador2, BoxLayout.Y_AXIS));
        bloqueJugador2.setAlignmentX(Component.LEFT_ALIGNMENT);
        bloqueJugador2.add(Box.createVerticalStrut(10));
        bloqueJugador2.add(crearEtiqueta("JUGADOR 2"));
        bloqueJugador2.add(Box.createVerticalStrut(5));
        configurarCampo(campoNombre2);
        bloqueJugador2.add(campoNombre2);
        bloqueJugador2.add(Box.createVerticalStrut(4));
        JLabel pista2 = new JLabel("MAXIMO 12. EL EQUIPO: 20 CARACTERES.");
        configurarPista(pista2);
        bloqueJugador2.add(pista2);
        cuerpo.add(bloqueJugador2);

        cuerpo.add(Box.createVerticalStrut(6));
        etiquetaError.setFont(EstiloBrutal.mono(11));
        etiquetaError.setForeground(EstiloBrutal.ROJO);
        etiquetaError.setAlignmentX(Component.LEFT_ALIGNMENT);
        etiquetaError.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        cuerpo.add(etiquetaError);
        cuerpo.add(Box.createVerticalStrut(10));

        JPanel filaBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        filaBotones.setOpaque(false);
        filaBotones.setAlignmentX(Component.LEFT_ALIGNMENT);
        filaBotones.add(botonJugar);
        filaBotones.add(botonVolver);
        cuerpo.add(filaBotones);

        formulario.add(cuerpo);
        return formulario;
    }

    /**
     * Crea una etiqueta pequena de campo.
     *
     * @param texto contenido de la etiqueta
     * @return etiqueta configurada
     */
    private JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(EstiloBrutal.mono(11));
        etiqueta.setForeground(EstiloBrutal.GRIS);
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
        return etiqueta;
    }

    /**
     * Aplica el estilo comun a un campo de nombre.
     *
     * @param campo campo que se quiere configurar
     */
    private void configurarCampo(JTextField campo) {
        campo.setFont(EstiloBrutal.mono(23));
        campo.setForeground(EstiloBrutal.NEGRO);
        campo.setCaretColor(EstiloBrutal.NEGRO);
        campo.setBackground(EstiloBrutal.AMARILLO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(EstiloBrutal.NEGRO, 4),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, ALTO_CAMPO));
        campo.setPreferredSize(new Dimension(ANCHO_CAMPO, ALTO_CAMPO));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    /**
     * Aplica el estilo comun a una pista de longitud.
     *
     * @param etiqueta pista que se quiere configurar
     */
    private void configurarPista(JLabel etiqueta) {
        etiqueta.setFont(EstiloBrutal.monoNormal(11));
        etiqueta.setForeground(EstiloBrutal.GRIS);
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
        etiqueta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 17));
    }

    /**
     * Ajusta la visibilidad del segundo campo y el tamano de la tarjeta.
     *
     * @param dosJugadores {@code true} para pedir dos nombres
     */
    private void configurarModo(boolean dosJugadores) {
        this.dosJugadores = dosJugadores;
        bloqueJugador2.setVisible(dosJugadores);
        cabeceraFormulario.setText(dosJugadores ? "JUGADORES" : "JUGADOR");
        pista.setText(dosJugadores
                ? "MAXIMO 12 CARACTERES CADA UNO."
                : "1 A " + Puntaje.NOMBRE_MAXIMO + " CARACTERES.");
        formulario.setPreferredSize(new Dimension(ANCHO_FORMULARIO,
                dosJugadores ? ALTO_PANEL_2P : ALTO_PANEL));
        revalidate();
        repaint();
    }

    /**
     * Limpia el formulario y lo prepara para el modo indicado.
     *
     * @param dosJugadores {@code true} para mostrar los dos campos
     */
    public void preparar(boolean dosJugadores) {
        campoNombre.setText("");
        campoNombre2.setText("");
        etiquetaError.setText(" ");
        configurarModo(dosJugadores);
        SwingUtilities.invokeLater(campoNombre::requestFocusInWindow);
    }

    /**
     * Devuelve el texto introducido por el jugador 1.
     *
     * @return nombre sin recortar
     */
    public String getNombre() {
        return campoNombre.getText();
    }

    /**
     * Devuelve el texto introducido por el jugador 2.
     *
     * @return nombre sin recortar, o una cadena vacia en el modo de un jugador
     */
    public String getNombre2() {
        return campoNombre2.getText();
    }

    /**
     * Muestra un mensaje de error bajo los campos y devuelve el foco.
     *
     * @param mensaje texto del error
     */
    public void mostrarError(String mensaje) {
        etiquetaError.setText(mensaje);
        JTextField destino = campoNombre.getText().isBlank()
                ? campoNombre
                : (dosJugadores && campoNombre2.getText().isBlank()
                        ? campoNombre2 : campoNombre);
        destino.requestFocusInWindow();
    }

    /**
     * Registra el oyente del boton Comenzar.
     *
     * @param oyente accion que se ejecuta al pulsar
     */
    public void alJugar(ActionListener oyente) {
        botonJugar.addActionListener(oyente);
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
