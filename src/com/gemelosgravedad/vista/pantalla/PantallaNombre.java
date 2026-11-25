package com.gemelosgravedad.vista.pantalla;

import com.gemelosgravedad.modelo.Puntaje;
import com.gemelosgravedad.vista.componente.BotonEstilo;
import com.gemelosgravedad.vista.componente.EstiloBrutal;
import com.gemelosgravedad.vista.componente.PanelTarjeta;
import com.gemelosgravedad.vista.componente.TextoDuro;
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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
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

    private static final int ANCHO_CREDENCIAL = 274;
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
        montar("IDENTIFICACIÓN DE PILOTO", "INGRESA TU NOMBRE",
                "GD—02 / REGISTRO 01", crearCentro(),
                "EL NOMBRE SE GUARDA EN EL TOP 3",
                "MÁXIMO " + Puntaje.NOMBRE_MAXIMO + " CARACTERES");
    }

    /**
     * Crea el contenido central con la credencial y el formulario.
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
        c.weighty = 1;
        c.insets = new Insets(0, 0, 0, 34);
        centro.add(crearCredencial(), c);

        c.gridx = 1;
        c.insets = new Insets(0, 0, 0, 0);
        centro.add(crearFormulario(), c);
        return centro;
    }

    /**
     * Crea la credencial decorativa del piloto.
     *
     * @return tarjeta de credencial
     */
    private JPanel crearCredencial() {
        PanelTarjeta credencial = new PanelTarjeta(EstiloBrutal.NEGRO,
                EstiloBrutal.AMARILLO);
        credencial.setLayout(new BoxLayout(credencial, BoxLayout.Y_AXIS));
        credencial.setBorder(BorderFactory.createEmptyBorder(28, 26, 24, 30));
        credencial.setPreferredSize(new Dimension(ANCHO_CREDENCIAL, ALTO_PANEL));

        JLabel rotulo = new JLabel("CREDENCIAL");
        rotulo.setFont(EstiloBrutal.mono(10));
        rotulo.setForeground(EstiloBrutal.GRIS_CLARO);
        rotulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        credencial.add(rotulo);
        credencial.add(Box.createVerticalStrut(10));

        TextoDuro numero = new TextoDuro("ID—01", EstiloBrutal.titulo(44),
                EstiloBrutal.AMARILLO, EstiloBrutal.ROJO, 5);
        numero.setAlineacion(SwingConstants.LEFT);
        numero.setAlignmentX(Component.LEFT_ALIGNMENT);
        credencial.add(numero);
        credencial.add(Box.createVerticalStrut(10));

        JLabel linea = new JLabel("OPERADOR DE GRAVEDAD");
        linea.setFont(EstiloBrutal.mono(11));
        linea.setForeground(EstiloBrutal.PAPEL);
        linea.setAlignmentX(Component.LEFT_ALIGNMENT);
        credencial.add(linea);
        credencial.add(Box.createVerticalStrut(4));

        JLabel gemelos = new JLabel("DOS GEMELOS.");
        gemelos.setFont(EstiloBrutal.titulo(17));
        gemelos.setForeground(EstiloBrutal.BLANCO);
        gemelos.setAlignmentX(Component.LEFT_ALIGNMENT);
        credencial.add(gemelos);

        JLabel gravedad = new JLabel("UNA GRAVEDAD.");
        gravedad.setFont(EstiloBrutal.titulo(17));
        gravedad.setForeground(EstiloBrutal.AMARILLO);
        gravedad.setAlignmentX(Component.LEFT_ALIGNMENT);
        credencial.add(gravedad);
        credencial.add(Box.createVerticalGlue());

        credencial.add(new FranjasCredencial());
        credencial.add(Box.createVerticalStrut(8));

        JLabel serie = new JLabel("GD—02 // POO / 2026");
        serie.setFont(EstiloBrutal.mono(9));
        serie.setForeground(EstiloBrutal.GRIS_CLARO);
        serie.setAlignmentX(Component.LEFT_ALIGNMENT);
        serie.setMaximumSize(new Dimension(Integer.MAX_VALUE, 14));
        credencial.add(serie);
        return credencial;
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

        JLabel cabecera = new JLabel("FORMULARIO // JUGADOR");
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

        JLabel etiqueta = new JLabel("NOMBRE DEL JUGADOR");
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

        JLabel pista = new JLabel("ENTRE 1 Y " + Puntaje.NOMBRE_MAXIMO
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

    /**
     * Franja de barras verticales usada como firma de la credencial.
     */
    private static final class FranjasCredencial extends JPanel {

        @Serial
        private static final long serialVersionUID = 1L;

        private static final int[] ANCHOS = {3, 7, 3, 10, 4, 3, 8, 3, 5, 9, 3, 6};
        private static final int SEPARACION = 5;
        private static final int ALTO_FRANJA = 30;

        /**
         * Crea la franja decorativa sin fondo propio.
         */
        private FranjasCredencial() {
            setOpaque(false);
            setPreferredSize(new Dimension(200, ALTO_FRANJA));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, ALTO_FRANJA));
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setColor(EstiloBrutal.PAPEL);
                int x = 0;
                for (int i = 0; i < ANCHOS.length && x < getWidth(); i++) {
                    g2.fillRect(x, 0, ANCHOS[i], getHeight());
                    x += ANCHOS[i] + SEPARACION;
                }
            } finally {
                g2.dispose();
            }
        }
    }
}
