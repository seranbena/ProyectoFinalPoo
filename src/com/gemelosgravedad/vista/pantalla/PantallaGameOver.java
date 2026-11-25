package com.gemelosgravedad.vista.pantalla;

import com.gemelosgravedad.modelo.Puntaje;
import com.gemelosgravedad.vista.componente.BarraBrutal;
import com.gemelosgravedad.vista.componente.BotonEstilo;
import com.gemelosgravedad.vista.componente.EstiloBrutal;
import com.gemelosgravedad.vista.componente.PanelTarjeta;
import com.gemelosgravedad.vista.componente.TextoDuro;
import java.awt.Component;
import java.awt.Dimension;
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
 * Resultado final de alto contraste con reinicio y vuelta al menu.
 */
public final class PantallaGameOver extends PantallaBase {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final int ANCHO_CARTEL = 414;
    private static final int ANCHO_RESULTADOS = 444;
    private static final int ALTO_PANEL = 430;
    private static final int ANCHO_BOTON = 352;
    private static final int ALTO_BOTON = 52;

    /** Etiqueta con el nombre del jugador. */
    private final JLabel etiquetaNombre;

    /** Etiqueta con el puntaje final. */
    private final JLabel etiquetaPuntos;

    /** Etiqueta con el tiempo sobrevivido. */
    private final JLabel etiquetaTiempo;

    /** Etiqueta con la fecha de la partida. */
    private final JLabel etiquetaFecha;

    /** Boton que repite la partida con el mismo jugador. */
    private final BotonEstilo botonReiniciar;

    /** Boton que vuelve al menu principal. */
    private final BotonEstilo botonMenu;

    /**
     * Crea la pantalla de resultado final.
     */
    public PantallaGameOver() {
        super();
        etiquetaNombre = new JLabel("—");
        etiquetaPuntos = new JLabel("0", SwingConstants.LEFT);
        etiquetaTiempo = new JLabel("TIEMPO: 00:00");
        etiquetaFecha = new JLabel("FECHA: —");
        botonReiniciar = new BotonEstilo("REINICIAR", EstiloBrutal.AMARILLO,
                ANCHO_BOTON, ALTO_BOTON);
        botonMenu = new BotonEstilo("MENÚ PRINCIPAL", EstiloBrutal.AZUL,
                ANCHO_BOTON, ALTO_BOTON);
        montar("RESULTADO FINAL", "FIN DE LA PARTIDA",
                "GD—02 / PARTIDA CERRADA", crearCentro(),
                "LA GRAVEDAD SIEMPRE COBRA SU PRECIO", RUTA_HISTORIAL);
    }

    /**
     * Crea el contenido central con el cartel y los resultados.
     *
     * @return contenedor central
     */
    private JPanel crearCentro() {
        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);
        centro.setBorder(BorderFactory.createEmptyBorder(26, 56, 22, 56));

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.weighty = 1;
        c.insets = new Insets(0, 0, 0, 28);
        centro.add(crearCartel(), c);

        c.gridx = 1;
        c.insets = new Insets(0, 0, 0, 0);
        centro.add(crearResultados(), c);
        return centro;
    }

    /**
     * Crea el cartel tipografico del fin de partida.
     *
     * @return tarjeta del cartel
     */
    private JPanel crearCartel() {
        PanelTarjeta cartel = new PanelTarjeta(EstiloBrutal.ROJO,
                EstiloBrutal.AMARILLO);
        cartel.setLayout(new BoxLayout(cartel, BoxLayout.Y_AXIS));
        cartel.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 30));
        cartel.setPreferredSize(new Dimension(ANCHO_CARTEL, ALTO_PANEL));

        JLabel rotulo = new JLabel("RESULTADO // 01");
        rotulo.setFont(EstiloBrutal.mono(11));
        rotulo.setForeground(EstiloBrutal.NEGRO);
        rotulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        cartel.add(rotulo);
        cartel.add(Box.createVerticalStrut(18));

        TextoDuro linea1 = new TextoDuro("GAME", EstiloBrutal.titulo(58),
                EstiloBrutal.NEGRO, EstiloBrutal.AMARILLO, 6);
        linea1.setAlineacion(SwingConstants.LEFT);
        linea1.setAlignmentX(Component.LEFT_ALIGNMENT);
        cartel.add(linea1);

        TextoDuro linea2 = new TextoDuro("OVER", EstiloBrutal.titulo(58),
                EstiloBrutal.NEGRO, EstiloBrutal.AMARILLO, 6);
        linea2.setAlineacion(SwingConstants.LEFT);
        linea2.setAlignmentX(Component.LEFT_ALIGNMENT);
        cartel.add(linea2);
        cartel.add(Box.createVerticalStrut(14));

        JPanel regla = new JPanel();
        regla.setBackground(EstiloBrutal.NEGRO);
        regla.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
        regla.setPreferredSize(new Dimension(300, 5));
        regla.setAlignmentX(Component.LEFT_ALIGNMENT);
        cartel.add(regla);
        cartel.add(Box.createVerticalStrut(14));

        JLabel lema = new JLabel("LA GRAVEDAD SIEMPRE");
        lema.setFont(EstiloBrutal.mono(13));
        lema.setForeground(EstiloBrutal.NEGRO);
        lema.setAlignmentX(Component.LEFT_ALIGNMENT);
        cartel.add(lema);

        JLabel lema2 = new JLabel("COBRA SU PRECIO.");
        lema2.setFont(EstiloBrutal.mono(13));
        lema2.setForeground(EstiloBrutal.NEGRO);
        lema2.setAlignmentX(Component.LEFT_ALIGNMENT);
        cartel.add(lema2);
        cartel.add(Box.createVerticalGlue());

        JPanel marcas = new JPanel();
        marcas.setOpaque(false);
        marcas.setLayout(new BoxLayout(marcas, BoxLayout.X_AXIS));
        marcas.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (int i = 0; i < 3; i++) {
            JLabel marca = new JLabel("X", SwingConstants.CENTER);
            marca.setOpaque(true);
            marca.setBackground(EstiloBrutal.NEGRO);
            marca.setForeground(EstiloBrutal.ROJO);
            marca.setFont(EstiloBrutal.titulo(16));
            marca.setPreferredSize(new Dimension(34, 34));
            marca.setMinimumSize(new Dimension(34, 34));
            marca.setMaximumSize(new Dimension(34, 34));
            marcas.add(marca);
            if (i < 2) {
                marcas.add(Box.createHorizontalStrut(8));
            }
        }
        marcas.add(Box.createHorizontalGlue());
        cartel.add(marcas);
        cartel.add(Box.createVerticalStrut(16));

        JLabel archivo = new JLabel("PARTIDA ARCHIVADA EN");
        archivo.setFont(EstiloBrutal.mono(9));
        archivo.setForeground(EstiloBrutal.NEGRO);
        archivo.setAlignmentX(Component.LEFT_ALIGNMENT);
        archivo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 14));
        cartel.add(archivo);

        JLabel ruta = new JLabel(RUTA_HISTORIAL);
        ruta.setFont(EstiloBrutal.mono(10));
        ruta.setForeground(EstiloBrutal.PAPEL);
        ruta.setAlignmentX(Component.LEFT_ALIGNMENT);
        ruta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 16));
        cartel.add(ruta);
        return cartel;
    }

    /**
     * Crea la tarjeta con los datos del resultado y los botones.
     *
     * @return tarjeta de resultados
     */
    private JPanel crearResultados() {
        PanelTarjeta resultados = new PanelTarjeta(EstiloBrutal.BLANCO,
                EstiloBrutal.NEGRO);
        resultados.setLayout(new BoxLayout(resultados, BoxLayout.Y_AXIS));
        resultados.setBorder(BorderFactory.createEmptyBorder(22, 28, 22, 34));
        resultados.setPreferredSize(new Dimension(ANCHO_RESULTADOS, ALTO_PANEL));

        JLabel rotuloNombre = new JLabel("JUGADOR");
        rotuloNombre.setFont(EstiloBrutal.mono(10));
        rotuloNombre.setForeground(EstiloBrutal.GRIS);
        rotuloNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        resultados.add(rotuloNombre);
        resultados.add(Box.createVerticalStrut(4));

        etiquetaNombre.setFont(EstiloBrutal.titulo(23));
        etiquetaNombre.setForeground(EstiloBrutal.NEGRO);
        etiquetaNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        etiquetaNombre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        resultados.add(etiquetaNombre);
        resultados.add(Box.createVerticalStrut(12));

        JLabel rotuloPuntos = BarraBrutal.chip("PUNTAJE FINAL",
                EstiloBrutal.NEGRO, EstiloBrutal.AMARILLO);
        rotuloPuntos.setFont(EstiloBrutal.titulo(11));
        rotuloPuntos.setAlignmentX(Component.LEFT_ALIGNMENT);
        resultados.add(rotuloPuntos);
        resultados.add(Box.createVerticalStrut(6));

        etiquetaPuntos.setOpaque(true);
        etiquetaPuntos.setBackground(EstiloBrutal.NEGRO);
        etiquetaPuntos.setForeground(EstiloBrutal.AMARILLO);
        etiquetaPuntos.setFont(EstiloBrutal.titulo(52));
        etiquetaPuntos.setBorder(BorderFactory.createEmptyBorder(0, 14, 4, 14));
        etiquetaPuntos.setAlignmentX(Component.LEFT_ALIGNMENT);
        etiquetaPuntos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 76));
        resultados.add(etiquetaPuntos);
        resultados.add(Box.createVerticalStrut(10));

        etiquetaTiempo.setFont(EstiloBrutal.mono(12));
        etiquetaTiempo.setForeground(EstiloBrutal.NEGRO);
        etiquetaTiempo.setAlignmentX(Component.LEFT_ALIGNMENT);
        etiquetaTiempo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));
        resultados.add(etiquetaTiempo);
        resultados.add(Box.createVerticalStrut(2));

        etiquetaFecha.setFont(EstiloBrutal.monoNormal(11));
        etiquetaFecha.setForeground(EstiloBrutal.GRIS);
        etiquetaFecha.setAlignmentX(Component.LEFT_ALIGNMENT);
        etiquetaFecha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 16));
        resultados.add(etiquetaFecha);
        resultados.add(Box.createVerticalGlue());

        JPanel zonaBotones = new JPanel();
        zonaBotones.setOpaque(false);
        zonaBotones.setLayout(new BoxLayout(zonaBotones, BoxLayout.Y_AXIS));
        zonaBotones.setAlignmentX(Component.LEFT_ALIGNMENT);
        zonaBotones.add(botonReiniciar);
        zonaBotones.add(Box.createVerticalStrut(12));
        zonaBotones.add(botonMenu);
        zonaBotones.setMaximumSize(new Dimension(359, 130));
        resultados.add(zonaBotones);
        return resultados;
    }

    /**
     * Rellena la pantalla con el resultado de la ultima partida.
     *
     * @param resultado resultado que se quiere mostrar; se ignora si es nulo
     */
    public void setDatos(Puntaje resultado) {
        if (resultado == null) {
            return;
        }
        etiquetaNombre.setText(resultado.getNombre());
        etiquetaPuntos.setText(String.valueOf(resultado.getPuntos()));
        etiquetaTiempo.setText("TIEMPO: " + resultado.getTiempoTexto());
        etiquetaFecha.setText("FECHA: " + resultado.getFecha());
    }

    /**
     * Registra el oyente del boton Reiniciar.
     *
     * @param oyente accion que se ejecuta al pulsar
     */
    public void alReiniciar(ActionListener oyente) {
        botonReiniciar.addActionListener(oyente);
    }

    /**
     * Registra el oyente del boton Menu principal.
     *
     * @param oyente accion que se ejecuta al pulsar
     */
    public void alMenu(ActionListener oyente) {
        botonMenu.addActionListener(oyente);
    }
}
