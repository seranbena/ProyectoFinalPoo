package com.gemelosgravedad.vista.pantalla;

import com.gemelosgravedad.modelo.Puntaje;
import com.gemelosgravedad.modelo.RegistroPartida;
import com.gemelosgravedad.vista.componente.BarraBrutal;
import com.gemelosgravedad.vista.componente.BotonEstilo;
import com.gemelosgravedad.vista.componente.EstiloBrutal;
import com.gemelosgravedad.vista.componente.PanelTarjeta;
import com.gemelosgravedad.vista.componente.TextoDuro;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.Serial;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Tabla de records con los mejores puntajes del historial.
 *
 * <p>La lista se recibe ya ordenada desde {@link RegistroPartida}; la pantalla
 * solo se encarga de pintarla y de mostrar un estado vacio cuando todavia no
 * hay partidas guardadas.</p>
 */
public final class PantallaTop3 extends PantallaBase {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final int ANCHO_BOTON = 230;
    private static final int ALTO_BOTON = 48;
    private static final int ANCHO_DATOS = 230;
    private static final int ANCHO_RANGO = 52;
    private static final int ALTO_RANGO = 46;
    private static final Color TERCER_PUESTO = new Color(238, 222, 205);

    /** Panel que contiene las filas generadas en cada consulta. */
    private final JPanel filas;

    /** Boton que vuelve al menu principal. */
    private final BotonEstilo botonVolver;

    /**
     * Crea la tabla de records y su boton de vuelta.
     */
    public PantallaTop3() {
        super();
        filas = new JPanel();
        botonVolver = new BotonEstilo("VOLVER", EstiloBrutal.AZUL,
                ANCHO_BOTON, ALTO_BOTON);
        montar("", "TOP " + RegistroPartida.TAMANO_TOP, "", crearCentro(),
                "", "");
    }

    /**
     * Crea el contenido central con la tabla y las acciones.
     *
     * @return contenedor central
     */
    private JPanel crearCentro() {
        JPanel principal = new JPanel(new BorderLayout());
        principal.setOpaque(false);
        principal.setBorder(BorderFactory.createEmptyBorder(22, 64, 8, 64));

        PanelTarjeta tabla = new PanelTarjeta(EstiloBrutal.BLANCO,
                EstiloBrutal.AMARILLO);
        tabla.setLayout(new BorderLayout());
        tabla.setBorder(BorderFactory.createEmptyBorder(0, 0, 9, 9));

        JPanel cabecera = new JPanel(new BorderLayout(12, 0));
        cabecera.setBackground(EstiloBrutal.NEGRO);
        cabecera.setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));

        JLabel titulo = new JLabel("MEJORES PUNTAJES");
        titulo.setFont(EstiloBrutal.titulo(13));
        titulo.setForeground(EstiloBrutal.AMARILLO);
        cabecera.add(titulo, BorderLayout.WEST);
        tabla.add(cabecera, BorderLayout.NORTH);

        filas.setOpaque(false);
        filas.setLayout(new BoxLayout(filas, BoxLayout.Y_AXIS));
        filas.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 30));
        tabla.add(filas, BorderLayout.CENTER);

        principal.add(tabla, BorderLayout.CENTER);
        principal.add(BarraBrutal.acciones(botonVolver),
                BorderLayout.SOUTH);
        return principal;
    }

    /**
     * Reemplaza el contenido de la tabla por la lista indicada.
     *
     * @param lista resultados que se quieren mostrar; puede ser nula o vacia
     */
    public void mostrarTop(List<Puntaje> lista) {
        filas.removeAll();
        agregarCabecera();

        if (lista == null || lista.isEmpty()) {
            mostrarVacio();
        } else {
            for (int i = 0; i < lista.size(); i++) {
                filas.add(crearFila(i + 1, lista.get(i)));
                if (i < lista.size() - 1) {
                    filas.add(Box.createVerticalStrut(9));
                }
            }
            filas.add(Box.createVerticalGlue());
        }
        filas.revalidate();
        filas.repaint();
    }

    /**
     * Pinta el estado vacio cuando no hay partidas guardadas.
     */
    private void mostrarVacio() {
        filas.add(Box.createVerticalGlue());

        TextoDuro vacio = new TextoDuro("SIN REGISTROS", EstiloBrutal.titulo(34),
                EstiloBrutal.NEGRO, EstiloBrutal.AMARILLO, 5);
        vacio.setAlignmentX(Component.CENTER_ALIGNMENT);
        filas.add(vacio);
        filas.add(Box.createVerticalStrut(8));

        JLabel detalle = new JLabel("JUEGA UNA PARTIDA PARA VER TU TOP 3.");
        detalle.setFont(EstiloBrutal.monoNormal(11));
        detalle.setForeground(EstiloBrutal.GRIS);
        detalle.setAlignmentX(Component.CENTER_ALIGNMENT);
        filas.add(detalle);

        filas.add(Box.createVerticalGlue());
    }

    /**
     * Agrega la fila de titulos de columna.
     */
    private void agregarCabecera() {
        JPanel cabecera = new JPanel(new BorderLayout(12, 0));
        cabecera.setOpaque(false);
        cabecera.setBorder(BorderFactory.createEmptyBorder(0, 12, 6, 18));
        cabecera.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

        JLabel izquierda = new JLabel("JUGADOR");
        izquierda.setFont(EstiloBrutal.mono(10));
        izquierda.setForeground(EstiloBrutal.GRIS);
        cabecera.add(izquierda, BorderLayout.WEST);

        JLabel derecha = new JLabel("PUNTOS / TIEMPO");
        derecha.setFont(EstiloBrutal.mono(10));
        derecha.setForeground(EstiloBrutal.GRIS);
        derecha.setHorizontalAlignment(SwingConstants.RIGHT);
        derecha.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 18));
        derecha.setPreferredSize(new Dimension(200, 16));
        cabecera.add(derecha, BorderLayout.EAST);
        filas.add(cabecera);
    }

    /**
     * Crea la fila de un resultado.
     *
     * @param puesto posicion en la tabla, empezando en uno
     * @param puntaje resultado que se muestra
     * @return tarjeta de la fila
     */
    private JPanel crearFila(int puesto, Puntaje puntaje) {
        Color relleno = EstiloBrutal.BLANCO;
        Color acento = EstiloBrutal.AZUL;
        if (puesto == 1) {
            relleno = EstiloBrutal.AMARILLO;
            acento = EstiloBrutal.NEGRO;
        } else if (puesto == 2) {
            relleno = EstiloBrutal.PAPEL;
            acento = EstiloBrutal.AZUL;
        } else if (puesto == 3) {
            relleno = TERCER_PUESTO;
            acento = EstiloBrutal.NARANJA;
        }

        PanelTarjeta fila = new PanelTarjeta(relleno, acento);
        fila.setLayout(new BorderLayout(14, 0));
        fila.setBorder(BorderFactory.createEmptyBorder(6, 22, 6, 18));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 78));

        JLabel rango = new JLabel(String.valueOf(puesto), SwingConstants.CENTER);
        rango.setOpaque(true);
        rango.setBackground(EstiloBrutal.NEGRO);
        rango.setForeground(relleno);
        rango.setFont(EstiloBrutal.titulo(24));
        rango.setPreferredSize(new Dimension(ANCHO_RANGO, ALTO_RANGO));
        rango.setMinimumSize(new Dimension(ANCHO_RANGO, ALTO_RANGO));
        rango.setMaximumSize(new Dimension(ANCHO_RANGO, ALTO_RANGO));
        fila.add(rango, BorderLayout.WEST);

        JLabel nombre = new JLabel(puntaje.getNombre());
        nombre.setFont(EstiloBrutal.mono(18));
        nombre.setForeground(EstiloBrutal.NEGRO);
        fila.add(nombre, BorderLayout.CENTER);

        JPanel datos = new JPanel();
        datos.setOpaque(false);
        datos.setLayout(new BoxLayout(datos, BoxLayout.Y_AXIS));
        datos.setPreferredSize(new Dimension(ANCHO_DATOS, 52));
        datos.setMaximumSize(new Dimension(ANCHO_DATOS, 52));

        JLabel puntos = new JLabel(puntaje.getPuntos() + " PTS",
                SwingConstants.RIGHT);
        puntos.setFont(EstiloBrutal.titulo(20));
        puntos.setForeground(EstiloBrutal.NEGRO);
        puntos.setAlignmentX(Component.LEFT_ALIGNMENT);
        puntos.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 16));
        puntos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        datos.add(puntos);
        datos.add(Box.createVerticalStrut(3));

        JLabel detalle = new JLabel(puntaje.getTiempoTexto(),
                SwingConstants.RIGHT);
        detalle.setFont(EstiloBrutal.monoNormal(9));
        detalle.setForeground(EstiloBrutal.GRIS);
        detalle.setAlignmentX(Component.LEFT_ALIGNMENT);
        detalle.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 16));
        detalle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));
        datos.add(detalle);

        fila.add(datos, BorderLayout.EAST);
        return fila;
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
