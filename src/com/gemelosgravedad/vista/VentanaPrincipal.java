package com.gemelosgravedad.vista;

import com.gemelosgravedad.modelo.EstadoJuego;
import com.gemelosgravedad.recursos.GestorRecursos;
import com.gemelosgravedad.vista.pantalla.PantallaBienvenida;
import com.gemelosgravedad.vista.pantalla.PantallaGameOver;
import com.gemelosgravedad.vista.pantalla.PantallaInstrucciones;
import com.gemelosgravedad.vista.pantalla.PantallaNombre;
import com.gemelosgravedad.vista.pantalla.PantallaTop3;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.io.Serial;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Unica ventana del juego.
 *
 * <p>Usa {@link CardLayout} para cambiar de pantalla sin abrir varias
 * ventanas. La vista no decide que pantalla sigue: eso lo hace el
 * {@link com.gemelosgravedad.controlador.ControladorNavegacion}.</p>
 *
 * @see PanelJuego
 * @see PantallaBienvenida
 */
public final class VentanaPrincipal extends JFrame {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final String BIENVENIDA = "BIENVENIDA";
    private static final String INSTRUCCIONES = "INSTRUCCIONES";
    private static final String NOMBRE = "NOMBRE";
    private static final String JUEGO = "JUEGO";
    private static final String GAME_OVER = "GAME_OVER";
    private static final String TOP3 = "TOP3";

    /** Panel con las tarjetas de las pantallas. */
    private final CardLayout tarjetas;

    /** Contenedor raiz que cambia de tarjeta. */
    private final JPanel contenedor;

    /** Pantalla inicial con el menu principal. */
    private final PantallaBienvenida pantallaBienvenida;

    /** Manual de operaciones. */
    private final PantallaInstrucciones pantallaInstrucciones;

    /** Formulario de identificacion del jugador. */
    private final PantallaNombre pantallaNombre;

    /** Panel donde se dibuja la partida. */
    private final PanelJuego panelJuego;

    /** Pantalla de resultado final. */
    private final PantallaGameOver pantallaGameOver;

    /** Tabla de records. */
    private final PantallaTop3 pantallaTop3;

    /**
     * Crea la ventana, sus pantallas y muestra la bienvenida.
     */
    public VentanaPrincipal() {
        super("Gemelos de Gravedad");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        Image icono = GestorRecursos.cargarImagen("sprites/jugador_azul_1.png");
        if (icono != null) {
            setIconImage(icono);
        }

        tarjetas = new CardLayout();
        contenedor = new JPanel(tarjetas);
        contenedor.setPreferredSize(new Dimension(EstadoJuego.ANCHO,
                EstadoJuego.ALTO));

        pantallaBienvenida = new PantallaBienvenida();
        pantallaInstrucciones = new PantallaInstrucciones();
        pantallaNombre = new PantallaNombre();
        panelJuego = new PanelJuego();
        pantallaGameOver = new PantallaGameOver();
        pantallaTop3 = new PantallaTop3();

        contenedor.add(pantallaBienvenida, BIENVENIDA);
        contenedor.add(pantallaInstrucciones, INSTRUCCIONES);
        contenedor.add(pantallaNombre, NOMBRE);
        contenedor.add(panelJuego, JUEGO);
        contenedor.add(pantallaGameOver, GAME_OVER);
        contenedor.add(pantallaTop3, TOP3);

        setContentPane(contenedor);
        pack();
        setLocationRelativeTo(null);
        mostrarBienvenida();
    }

    /**
     * Muestra la pantalla de bienvenida.
     */
    public void mostrarBienvenida() {
        tarjetas.show(contenedor, BIENVENIDA);
    }

    /**
     * Muestra el manual de instrucciones.
     */
    public void mostrarInstrucciones() {
        tarjetas.show(contenedor, INSTRUCCIONES);
    }

    /**
     * Prepara y muestra la pantalla de nombre.
     */
    public void mostrarNombre() {
        pantallaNombre.preparar();
        tarjetas.show(contenedor, NOMBRE);
    }

    /**
     * Muestra el panel de juego y le devuelve el foco.
     */
    public void mostrarJuego() {
        tarjetas.show(contenedor, JUEGO);
        panelJuego.pedirFoco();
    }

    /**
     * Muestra la pantalla de resultado final.
     */
    public void mostrarGameOver() {
        tarjetas.show(contenedor, GAME_OVER);
    }

    /**
     * Muestra la tabla de records.
     */
    public void mostrarTop3() {
        tarjetas.show(contenedor, TOP3);
    }

    /**
     * Devuelve la pantalla de bienvenida.
     *
     * @return pantalla de bienvenida
     */
    public PantallaBienvenida getPantallaBienvenida() {
        return pantallaBienvenida;
    }

    /**
     * Devuelve la pantalla de instrucciones.
     *
     * @return pantalla de instrucciones
     */
    public PantallaInstrucciones getPantallaInstrucciones() {
        return pantallaInstrucciones;
    }

    /**
     * Devuelve la pantalla de nombre.
     *
     * @return pantalla de nombre
     */
    public PantallaNombre getPantallaNombre() {
        return pantallaNombre;
    }

    /**
     * Devuelve el panel de juego.
     *
     * @return panel de juego
     */
    public PanelJuego getPanelJuego() {
        return panelJuego;
    }

    /**
     * Devuelve la pantalla de fin de partida.
     *
     * @return pantalla de resultado
     */
    public PantallaGameOver getPantallaGameOver() {
        return pantallaGameOver;
    }

    /**
     * Devuelve la pantalla de records.
     *
     * @return pantalla del Top 3
     */
    public PantallaTop3 getPantallaTop3() {
        return pantallaTop3;
    }
}
