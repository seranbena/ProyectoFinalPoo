package com.gemelosgravedad.vista.pantalla;

import com.gemelosgravedad.modelo.Puntaje;
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
 * Pide el nombre del jugador antes de comenzar la partida.
 *
 * <p>La validacion definitiva la hace el controlador de navegacion; esta
 * pantalla solo recoge el texto y muestra el mensaje de error que recibe.</p>
 */
public final class PantallaNombre extends PantallaBase {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final int ALTO_PANEL = 350;
    private static final int ANCHO_FORMULARIO = 552;
    private static final int ANCHO_CAMPO = 480;
    private static final int ALTO_CAMPO = 58;
    private static final int ANCHO_BOTON = 226;
    private static final int ALTO_BOTON = 50;

    /** Campo donde se escribe el nombre. */
    private final JTextField campoNombre;

    /** Etiqueta que muestra los errores de validacion. */
    private final JLabel etiquetaError;

    /** Boton que comienza la partida. */
    private final BotonEstilo botonJugar;

    /** Boton que vuelve al menu principal. */
    private final BotonEstilo botonVolver;

    /**
     * Crea la pantalla de identificacion y su formulario.
     */
    public PantallaNombre() {
        super();
        campoNombre = new JTextField();
        etiquetaError = new JLabel(" ");
        botonJugar = new BotonEstilo("COMENZAR", EstiloBrutal.AMARILLO,
                ANCHO_BOTON, ALTO_BOTON);
        botonVolver = new BotonEstilo("VOLVER", EstiloBrutal.AZUL,
                ANCHO_BOTON, ALTO_BOTON);
        montar("", "INGRESA TU NOMBRE", "", crearCentro(),
                "TU NOMBRE SE GUARDA EN EL TOP 3", "");
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
     * Crea el formulario con el campo de nombre y los botones.
     *
     * @return tarjeta del formulario
     */
    private JPanel crearFormulario() {
        PanelTarjeta formulario = new PanelTarjeta(EstiloBrutal.BLANCO,
                EstiloBrutal.AMARILLO);
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setBorder(BorderFactory.createEmptyBorder(0, 0, 9, 9));
        formulario.setPreferredSize(new Dimension(ANCHO_FORMULARIO, ALTO_PANEL));

        JLabel cabecera = new JLabel("JUGADOR");
        cabecera.setOpaque(true);
        cabecera.setBackground(EstiloBrutal.NEGRO);
        cabecera.setForeground(EstiloBrutal.AMARILLO);
        cabecera.setFont(EstiloBrutal.titulo(13));
        cabecera.setBorder(BorderFactory.createEmptyBorder(7, 18, 7, 18));
        cabecera.setAlignmentX(Component.LEFT_ALIGNMENT);
        cabecera.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        formulario.add(cabecera);

        JPanel cuerpo = new JPanel();
        cuerpo.setOpaque(false);
        cuerpo.setLayout(new BoxLayout(cuerpo, BoxLayout.Y_AXIS));
        cuerpo.setBorder(BorderFactory.createEmptyBorder(18, 28, 18, 30));
        cuerpo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel etiqueta = new JLabel("NOMBRE");
        etiqueta.setFont(EstiloBrutal.mono(11));
        etiqueta.setForeground(EstiloBrutal.GRIS);
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
        cuerpo.add(etiqueta);
        cuerpo.add(Box.createVerticalStrut(7));

        campoNombre.setFont(EstiloBrutal.mono(23));
        campoNombre.setHorizontalAlignment(JTextField.LEFT);
        campoNombre.setForeground(EstiloBrutal.NEGRO);
        campoNombre.setCaretColor(EstiloBrutal.NEGRO);
        campoNombre.setBackground(EstiloBrutal.AMARILLO);
        campoNombre.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(EstiloBrutal.NEGRO, 4),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        campoNombre.setMaximumSize(new Dimension(Integer.MAX_VALUE, ALTO_CAMPO));
        campoNombre.setPreferredSize(new Dimension(ANCHO_CAMPO, ALTO_CAMPO));
        campoNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        cuerpo.add(campoNombre);
        cuerpo.add(Box.createVerticalStrut(7));

        JLabel pista = new JLabel("1 A " + Puntaje.NOMBRE_MAXIMO
                + " CARACTERES.");
        pista.setFont(EstiloBrutal.monoNormal(11));
        pista.setForeground(EstiloBrutal.GRIS);
        pista.setAlignmentX(Component.LEFT_ALIGNMENT);
        pista.setMaximumSize(new Dimension(Integer.MAX_VALUE, 17));
        cuerpo.add(pista);
        cuerpo.add(Box.createVerticalStrut(4));

        etiquetaError.setFont(EstiloBrutal.mono(11));
        etiquetaError.setForeground(EstiloBrutal.ROJO);
        etiquetaError.setAlignmentX(Component.LEFT_ALIGNMENT);
        etiquetaError.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        cuerpo.add(etiquetaError);
        cuerpo.add(Box.createVerticalStrut(12));

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
     * Limpia el formulario y devuelve el foco al campo de nombre.
     */
    public void preparar() {
        campoNombre.setText("");
        etiquetaError.setText(" ");
        SwingUtilities.invokeLater(campoNombre::requestFocusInWindow);
    }

    /**
     * Devuelve el texto introducido por el jugador.
     *
     * @return nombre sin recortar
     */
    public String getNombre() {
        return campoNombre.getText();
    }

    /**
     * Muestra un mensaje de error bajo el campo y devuelve el foco.
     *
     * @param mensaje texto del error
     */
    public void mostrarError(String mensaje) {
        etiquetaError.setText(mensaje);
        campoNombre.requestFocusInWindow();
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
