package com.gemelosgravedad.vista.pantalla;

import com.gemelosgravedad.modelo.juego.EstadoJuego;
import com.gemelosgravedad.vista.componente.BarraBrutal;
import com.gemelosgravedad.vista.componente.PanelFondo;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.io.Serial;

/**
 * Base comun de las pantallas de menu.
 *
 * <p>Concentra el tamano fijo, el layout de tarjetas y las barras de cabecera y
 * pie. Cada pantalla solo aporta el componente central y los textos de las
 * barras.</p>
 *
 * @see PanelFondo
 * @see BarraBrutal
 */
public abstract class PantallaBase extends PanelFondo {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Ruta de la imagen de fondo compartida por las pantallas de menu. */
    protected static final String RUTA_FONDO = "imagenes/fondo_bienvenida.png";

    /**
     * Crea una pantalla con el fondo comun de los menus.
     */
    protected PantallaBase() {
        super(RUTA_FONDO);
    }

    /**
     * Monta la estructura comun: cabecera, contenido central y pie.
     *
     * @param izquierda texto izquierdo de la cabecera
     * @param centro titulo de la cabecera
     * @param derecha texto derecho de la cabecera
     * @param contenido componente que ocupa el centro
     * @param pieIzquierda texto izquierdo del pie
     * @param pieDerecha texto derecho del pie
     */
    protected final void montar(String izquierda, String centro, String derecha,
                                Component contenido, String pieIzquierda,
                                String pieDerecha) {
        setPreferredSize(new Dimension(EstadoJuego.ANCHO, EstadoJuego.ALTO));
        setLayout(new BorderLayout());
        add(BarraBrutal.banda(izquierda, centro, derecha), BorderLayout.NORTH);
        add(contenido, BorderLayout.CENTER);
        add(BarraBrutal.pie(pieIzquierda, pieDerecha), BorderLayout.SOUTH);
    }
}
